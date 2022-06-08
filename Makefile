.PHONY: target

all: target ds-server

target: 
	mvn clean package

ds-server: 
	$(MAKE) -C ds-sim/src

clean: 
	rm -rf target
	rm -f ds-sim/src/ds-server ds-sim/src/ds-client

test: 
	$(MAKE) -C test

