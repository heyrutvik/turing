# turing
Turing machine DSL and simulator

> A Turing machine is a mathematical model of computation that defines 
an abstract machine, which manipulates symbols on a strip of tape according 
to a table of rules. Despite the model's simplicity, given any computer 
algorithm, a Turing machine capable of simulating that algorithm's logic 
can be constructed. - [Wikipedia](https://en.wikipedia.org/wiki/Turing_machine)

## DSL to describe Turing machine rules
```scala
import io.github.heyrutvik.turing.implicits._
import io.github.heyrutvik.turing.dsl.DSL._

val t = "II. Compute the sequence: 0010110111011110..." table {
  {"b" read blank perform "Pe, R, Pe, R, P0, R, R, P0, L, L" goto "o"} |:
  {"o" read "1"   perform "R, Px, L, L, L"                   goto "o"} |:
  {"o" read "0"   perform ""                                 goto "q"} |:
  {"q" read "0"   perform "R, R"                             goto "q"} |:
  {"q" read "1"   perform "R, R"                             goto "q"} |:
  {"q" read blank perform "P1, L"                            goto "p"} |:
  {"p" read "x"   perform "E, R"                             goto "q"} |:
  {"p" read "e"   perform "R"                                goto "f"} |:
  {"p" read blank perform "L, L"                             goto "p"} |:
  {"f" read any   perform "R, R"                             goto "f"} |:
  {"f" read blank perform "P0, L, L"                         goto "o"}
}

t print 500
// |e|e|0|_|0|_|1|_|0|_|1|_|1|_|0|_|1|_|1|_|1|_|0|_|1|_|1|_|1|_|1|_|0|_|1|_|1|_|1|_|1|x|1|x|0|

t simulate 500
// check out the video below
```

## Turing machine simulation
`t simulate 500` will [simulate](https://twitter.com/heyrutvik/status/1103690457031237633) 500 steps defined as in table `t`.