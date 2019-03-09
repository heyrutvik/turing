package io.github.heyrutvik.turing.encode

import io.github.heyrutvik.turing.compile.Move._
import io.github.heyrutvik.turing.standard.O._
import io.github.heyrutvik.turing.standard.Table.Entry
import io.github.heyrutvik.turing.standard._

trait StandardDescription[T] {
  def encode(x: T): String
}

object StandardDescriptionInstance {

  implicit val mconfigStandardDescription: StandardDescription[C] = {
    (mc: C) => "D" + (1 to mc.n.value).map(_ => "A").mkString
  }

  implicit val symbolStandardDescription: StandardDescription[S] = {
    (s: S) => "D" + (1 to s.n.value).map(_ => "C").mkString
  }

  implicit def rightOpStandardDescription(implicit s: StandardDescription[S]): StandardDescription[R] = {
    (r: R) => s.encode(r.p) + RIGHT
  }

  implicit def leftOpStandardDescription(implicit s: StandardDescription[S]): StandardDescription[L] = {
    (l: L) => s.encode(l.p) + LEFT
  }

  implicit def noneOpStandardDescription(implicit s: StandardDescription[S]): StandardDescription[N] = {
    (n: N) => s.encode(n.p) + NONE
  }

  implicit def entryStandardDescription(
                                         implicit q: StandardDescription[C],
                                         s: StandardDescription[S],
                                         op: StandardDescription[O]): StandardDescription[Entry] = {
    (e: Entry) => q.encode(e.mc) + s.encode(e.sym) + op.encode(e.op) + q.encode(e.fc) + ";"
  }

  implicit def tableStandardDescription(implicit e: StandardDescription[Entry]): StandardDescription[Table] = {
    (t: Table) => t.es.map(x => e.encode(x)).mkString
  }
}