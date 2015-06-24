.PHONY: all build test run benchmark package todo clean

all: build

build:
	@sbt compile

test:
	@sbt clean coverage test

run:
	@sbt "run $(ARGS)"

benchmark: build
	@sbt \
		"run AG(Not(And(\"s11_T1\"=\"1\",\"s11_T1\"=\"2\"))) src/test/resources/brp.fsm" \
		"run AG(Not(And(\"x0\"=\"T\",\"x1\"=\"F\"))) src/test/resources/random-large.fsm" \
		|| true

package:
	@sbt one-jar

todo:
	@find src -type f -print0 \
		| xargs --null grep -i \
			"todo\|fixme\|not implemented" \
			--color=always -n \
			|| exit 0

clean:
	@sbt clean
