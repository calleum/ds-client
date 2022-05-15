#!/usr/bin/env bash


# Job ID
# Server name/id
# Submission time
# Waiting time: submission_time - start_time
# Start time 
# End time
# Turnaround time: waiting_time + ( end_time - start_time )
# Rental cost: ( end_time - start_time ) * cost_per_second
# Resource utilisation: memory_

# JOBN submitTime jobID estRuntime core memory disk
#set -x
INPUT="$1"
outdir="$(mktemp -d)"
jobns="$outdir/jobns"
ts="$outdir/ts"
metrics="$outdir/metrics"
i=0
touch $jobns
touch $ts
touch $metrics


grep 'JOBN' "$INPUT" > $jobns
grep -e '^t' "$INPUT" > $ts

# SENT JOBN 55 2 260 2 900 1600
# t:         55 job     2 (waiting) on # 0 of server small (booting) SCHEDULED
# t:         95 job     2 on # 0 of server small RUNNING
# t:        272 job     2 on # 0 of server small COMPLETED

#echo "| jobID: | Server: | Submit: | Waiting: | start: | end: | Turnaround: | Cost: | Resource: |"
echo "jobID,ServerID:type,Submit_time,Waiting_time,Start_time,End_time,Turnaround_time,Cost,Resource utilisation" > $metrics
while read -r JOBN ; do
    job_log=$(grep "job     $i" $ts)
    submission_time=$(echo $job_log | grep 'SCHEDULED' | awk '{ print $2 }')
    start_time=$(echo $job_log | grep 'RUNNING' | awk '{ print $2 }')
    end_time=$(echo $job_log | grep 'COMPLETED' | awk '{ print $2 }')
    server=$(echo $job_log | grep 'COMPLETED' | sed 's/^.*#\(.*$\)/\1/' | awk '{ print $1 }')
    serv_type=$(echo "$job_log" | grep 'COMPLETED' | awk '{ print $10 }')
    [[ $serv_type = 'tiny' ]] && cost_sec='0.4'|| true
    [[ $serv_type = 'small' ]] && cost_sec='0.4'|| true
    [[ $serv_type = 'medium' ]] && cost_sec='0.8'|| true

    (( waiting_time=submission_time - start_time )) 
    (( run_time=end_time - start_time )) 
    (( turnaround_time=waiting_time + run_time ))
    total_cost=$(echo "$run_time.0 * ( $cost_sec / 3600.00 )" | bc )

    echo "$i,$server:$serv_type,$submission_time,$waiting_time,$start_time,$end_time,$turnaround_time,$total_cost,100%" >> $metrics
    #echo "jobID: $i Server: $server:$serv_type Submit: $submission_time Waiting: $waiting_time start: $start_time end: $end_time Turnaround: $turnaround_time Cost: $total_cost Resource: "
    ((i++))
done < $jobns
mlr --c2p cat "$metrics"
#set +x
