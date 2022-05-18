#!/usr/bin/env bash

INPUT="$1"
## Test script usage is correct
[[ -z "$INPUT" ]] && echo "usage: $0 <logfile_name>" && exit 1 || true

## Create output directory and files
outdir="target"
i=0
total_resource_usage=1

jobns="$outdir/jobns"
ts="$outdir/ts"
metrics="$outdir/metrics"


touch $jobns
touch $ts
touch $metrics

## Create files with the 2 types of lines we want to scrape from the output log.
## Using '>' ensures these are fresh for each invocation of the script.
grep 'JOBN' "$INPUT" > $jobns
grep -e '^t' "$INPUT" > $ts

## Create the headers for the table, refreshes the file holding the table.
echo "jobID,ServerID:type,Submit_time,Waiting_time,Start_time,End_time,Turnaround_time,cost_cents,Resource_utilisation" > $metrics
echo ""
while read -r  ; do
    ## Hard coded for the current fmt of the output of ds-server, 
    ## looping through the job IDs via variable substitution.
    job_log=$(grep "job     $i" $ts) 

    ## Parse the log lines for needed metrics
    submission_time=$(echo "$job_log" | grep 'SCHEDULED' | awk '{ print $2 }')
    start_time=$(echo "$job_log" | grep 'RUNNING' | awk '{ print $2 }')
    end_time=$(echo "$job_log" | grep 'COMPLETED' | awk '{ print $2 }')
    server=$(echo "$job_log" | grep 'COMPLETED' | sed 's/^.*#\(.*$\)/\1/' | awk '{ print $1 }')
    serv_type=$(echo "$job_log" | grep 'COMPLETED' | awk '{ print $10 }')
    
    ## Assign the cost_per_sec according to the server size from the config file.
    [[ $serv_type = 'tiny' ]] && cost_sec='0.4'|| true
    [[ $serv_type = 'small' ]] && cost_sec='0.4'|| true
    [[ $serv_type = 'medium' ]] && cost_sec='0.8'|| true

    ## Calculate the waiting, run and turnaround times using builting arithmetic.
    (( waiting_time=start_time - submission_time )) 
    (( run_time=end_time - start_time )) 
    (( turnaround_time=waiting_time + run_time ))
    (( total_resource_usage=total_resource_usage + turnaround_time ))  
    (( running_exec_time+=run_time))
    (( avg_exec_time=running_exec_time  / ((i + 1)) ))

    echo "avg_exec_time = $avg_exec_time"

    ## Since bash does not handle fp arithmetic, delegate to bc.
    total_cost=$(echo "${run_time}.0 * ( $cost_sec / 3600.0 ) * 100" | bc -l )
    cost_cents=$(printf "%0.2f\n" $total_cost)
    resource_usage=$(echo "( $total_resource_usage / $turnaround_time )" | bc -l )
    
## trunk...origin/trunk
    ## Print the metrics into a csv format. Resource utilisation is currently hard coded.
    echo "$i,$server:$serv_type,$submission_time,$waiting_time,$start_time,$end_time,$turnaround_time,$cost_cents,$resource_usage" >> $metrics
    ((i++))
done < $jobns

## Pretty print the output of the metrics scraping.
column -s, -t <$metrics
#mlr --c2p cat "$metrics"
