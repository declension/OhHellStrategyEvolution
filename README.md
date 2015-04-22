# Oh Hell! : Strategy Evolution
[![Build Status](https://travis-ci.org/declension/OhHellStrategyEvolution.svg?branch=master)](https://travis-ci.org/declension/OhHellStrategyEvolution)

### Err, what?
OhHellStrategyEvolution is an exploration into using a simple [Genetic Programming](http://en.wikipedia.org/wiki/Genetic_programming)
subset as a [hyper-heuristic](http://en.wikipedia.org/wiki/Hyper-heuristic) to evolve strategies 
for the card game [Oh Hell](http://en.wikipedia.org/wiki/Oh_Hell),
evaluating their fitness by playing tournaments against a selection of (varyingly) naive (fixed) algorithmic strategies
using an included multi-player game engine.

Well you did ask.

### Features
* The game engine can play (and log in a human-friendly way) entire games or tournaments (multiple games)
  using the most typical.
* Evolution is orchestrated using the excellent [Watchmaker](http://watchmaker.uncommons.org/) \[Java\] framework, 
with a few additions and hacks to support this usage.
* Designed throughout for use of Java 8's functional and stream features.
* Most of the many, many rule and [scoring variants](http://en.wikipedia.org/wiki/Oh_Hell#Scoring) are pluggable, though for now only one typical setup is used
(related aside: see an earlier product of this real-world complexity, [HellOh](https://play.google.com/store/apps/details?id=net.declension.android.rikiki) Android app)

### Requirements
 * Something that can run Java 8 and Swing
 * The build uses Maven 3, and some of the more common plugins
 * JUnit, [AssertJ](http://joel-costigliola.github.io/assertj/) and Mockito are used for testing, and currently Travis.ci runs these per commit.
 
### How does it work?

* Set up engine and UI
* For each generation:
 * Randomly generate bidding evaluation tree (more on this later)
 * Create a `GeneticStrategy` based on this bidding 
 * Generate `n` (bot) players based on these, and a playing strategy (currently: random (!))
 * Split the `n` players up into `n` tournaments, adding `m` "outsiders" (bots with algorithmic strategies)
 * Start the game engines, using a threadpool to maximise throughput on multi-core machines
   (these are the _Fitness Evaluators_). For each game:
    * Run the games through their sequences of hands comprinsing variable numbers of tricks, until finished - typically 13-19 rounds.
 * Then, collate the scores for each player by summing and normalising slightly the scores for each player
  (this seems to work slightly better than averaged rankings)
 * Select candidates for  (currently this uses _Sigma-Scaling_).

### _Does_ it work?
As of time of writing: yes, a bit. The problem domain is (deliberately) unusual and therefore possibly ill-suited
to GP in general; in addition the usual GP (and more generally EA) concerns exist - balancing crossover, mutation, simplification,
elitism, population size, initial tree complexity, symbol sets and so on to produce _enough but not too much_ diversity to get at least some reasonable solutions.
It rapidly becomes clear [there are no silver bullets](http://cswww.essex.ac.uk/staff/poli/gp-field-guide/133ThereareNoSilverBullets.html#20_3).

There's now a basic Swing UI, also using the Watchmaker base classes.

### What's next?
A lot! See the [issues](./issues), really, but here are some general ideas so far:

 1. A separate, but related playing strategy evaluator, which co-evolves.
 2. Some kind of persistence, to start and stop, and eventually use these
 3. Lots of bug-fixes, and performance enhancements (though this has got a lot better recently)
 4. Lots of tweaks to the (many) EA parameters and symbol sets that can make the difference between success and failure.
 5. Enhancements to the UI, possibly even to the level of being able to freeze evolution
  and see individual strategies playing their games.
