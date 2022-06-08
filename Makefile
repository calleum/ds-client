.PHONY: target
.PHONY: test
.PHONY: run-demo

all: target ds-server

target: 
	mvn clean package

ds-server: 
	$(MAKE) -C ds-sim/src

clean: 
	rm -rf target
	rm -f ds-sim/src/ds-server ds-sim/src/ds-client

test: 
	./run.sh
	
run-demo-m1: demo
	cd test && ./stage2-test-aarch64 "java -Djava.util.logging.config.file=src/main/resources/logging.properties -jar ../target/ds-client-0.1.jar -a LERT" -o tt 

run-demo: demo
	cd test && ./stage2-test-x86 "java -Djava.util.logging.config.file=src/main/resources/logging.properties -jar ../target/ds-client-0.1.jar -a LERT" -o tt 

run-client: 
	java -Djava.util.logging.config.file=src/main/resources/logging.properties -jar ./target/ds-client-0.1.jar -a LERT  2>&1 | tee client.log

demo: ds-server
	cp ds-sim/src/ds-server test/
