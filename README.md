CTL model checking
==================

[*CTL*](https://en.wikipedia.org/wiki/Computation_tree_logic)
[model checking](https://en.wikipedia.org/wiki/Model_checking),
implemented using
[BDDs](https://en.wikipedia.org/wiki/Binary_decision_diagram)
([JavaBDD](http://javabdd.sourceforge.net/) library).

[![Build Status](https://travis-ci.org/fpoli/ctlmc.svg?branch=master)](https://travis-ci.org/fpoli/ctlmc)

## Usage

    Usage: program (CTL specification) (path to model file)
    e.g.: program 'AG(Not(And("c1"="T", "c2"="T")))' "model.fsm"

Arguments:

1. CTL specification (e.g. `Imply(AG("t1"="T"), AF("c1"="T"))`);
2. path to model file, in the [FSM format](http://www.mcrl2.org/release/user_manual/language_reference/lts.html) with the following restriction:
    - all the states must be univocally identified by the value of their atomic propositions.

## Quick start

1. Install [Scala](http://www.scala-lang.org/) and [sbt](http://www.scala-sbt.org/)
2. Run tests with `make test`
3. Prepare jar package with `make package`
4. Run demo with `java -jar /path/to/ctl-model-checking.jar 'AG(Not(And("c1"="T", "c2"="T")))' ./src/test/resources/mutual-exclusion.fsm`

## Example (output)

    === CTL Model Checking ===
    (*) Parsing CTL specification AG(Not(And("c1"="T", "c2"="T"))) ...
    (*) Parsing FSM model ./src/test/resources/mutual-exclusion.fsm ...
    (*) Model checking...
    [OK] The model satisfies the specification

## License

Copyright (C) 2015 Federico Poli <federpoli@gmail.com>

Released under the GNU General Public License, version 3
