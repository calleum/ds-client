# DS-CLIENT

This project is an educational job scheduler based on the work done by Y. C.
LEE @Macquarie University in the COMP3100 Distributed Systems Course. 
The ds-sim code is included as a submodule and found
[here](https://github.com/distsys-MQ/ds-sim).

The stage1 and stage2 tests are also included as a submodule, hosted in a
separate [repository](https://github.com/calleum/ds-sim-tests).

---

## Quickstart


- Get the dependencies:
```sh
# git submodule init --recursive
```

- Build the scheduler, reference implementation of `ds-client` and the dependency `ds-server`:
```sh
# make all
```

- Run a test case against the scheduler:
```sh
# make test
```

- Run the university-provided test-suite for stage 2:
```sh
# make run-demo
```

---

## Assignment Details: 

Design and implement **at least one new scheduling algorithm** that ***optimises
average turnaround time***. 

In particular, your scheduling algorithm as part of your client shall schedule
jobs to servers aiming to minimise the average turnaround time without sacrificing
too much of other performance metrics, i.e., resource utilisation and server 
rental cost.

As these objectives are incompatible (conflicting performance objectives),
the *optimisation of one objective might lead to sacrificing the optimisation 
of other metrics*. For instance, if you ***attempt to minimise average turnaround 
time by minimising waiting time***, you ***may end up using more resources resulting 
in high costs*** (i.e., server rental costs). 

Therefore, ***you have to define the scheduling problem clearly indicating your
performance objective(s) and justifying your choice.*** In addition, you must 
discuss your algorithm and scheduling results in comparison with four baseline 
algorithms (FF, BF, WF and FC) and their results.
