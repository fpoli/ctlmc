CTL model checking
==================

[*CTL*](https://en.wikipedia.org/wiki/Computation_tree_logic)
[model checking](https://en.wikipedia.org/wiki/Model_checking),
implemented using
[BDDs](https://en.wikipedia.org/wiki/Binary_decision_diagram)
([JavaBDD](http://javabdd.sourceforge.net/) library).

## Usage

    Usage: program (CTL specification) (path to model file)
    e.g.: program 'AG(Not(And("c1", "c2")))' "model.fsm"

Arguments:

1. CTL specification (e.g. `Imply(AG("t1"), AF("c1"))`);
2. path to model file, in the [FSM format](http://www.mcrl2.org/release/user_manual/language_reference/lts.html) with the following assumptions:
    - the domain of the atomic propositions is restricted to "F" and "T" values only;
    - all the states are univocally identified by the bit-vector of their atomic propositions.

## Quick start

1. Install [Scala](http://www.scala-lang.org/) and [sbt](http://www.scala-sbt.org/)
2. Run tests with `make test`
3. Prepare jar package with `make package`
4. Run demo with `java -jar /path/to/ctl-model-checking.jar 'AG(Not(And("c1", "c2")))' ./src/test/resources/mutual-exclusion.fsm`

## Example (output)

    === CTL Model Checking ===
    Model: ./src/test/resources/mutual-exclusion.fsm
    Spec: AG(Not(And("c1","c2")))
    [OK] The model satisfies the specification

## License

Copyright (C) 2015 Federico Poli <federpoli@gmail.com>

Released under the GNU General Public License, version 3
