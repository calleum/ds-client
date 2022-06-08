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

