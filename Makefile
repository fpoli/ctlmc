.PHONY: default build test run clean

all: build

build:
	@sbt compile

test:
	@sbt clean coverage test

run:
	@sbt "run $(ARGS)"

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
