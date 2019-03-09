package io.github.heyrutvik.turing.dsl

import io.github.heyrutvik.turing.compile._
import io.github.heyrutvik.turing.dsl.DSL.{Table => DSLTable, _}
import io.github.heyrutvik.turing.dsl.Table.Entry
import io.github.heyrutvik.turing.elaborate.Elaborator

case class Table(name: String, es: List[Entry]) {

  implicit val symbols: Set[String] = es.map(_.symbol).filterNot(_ == Keyword.ANY).toSet
  val elaborated: List[Entry] = Elaborator.entries(es)

  def mkDSL: DSL = {
    val temp = elaborated.map {
      case Entry(mc, sym, op, fc) => Goto(Perform(Read(Define(mc), sym), op), fc)
    }.reverse
    temp.tail.foldLeft(temp.head: DSL)((dsl, goto) => DSLTable(goto, dsl))
  }

  private def _pretty(es: List[Entry]): String = {
    val maxop = es.map(e => e.operation.length).max
    val op_padding = "operations".length.max(maxop)
    val padding = "%1$8s | %2$6s | %3$"+op_padding+"s | %4$14s"

    "\n" +
    name + "\n" +
    padding.format("--------", "------", "----------", "--------------") + "\n" +
    padding.format("m-config", "symbol", "operations", "final m-config") + "\n" +
    padding.format("--------", "------", "----------", "--------------") + "\n" +
    es.map { e =>
      padding.format(e.name, if (e.symbol == "") "None" else e.symbol, e.operation, e.next)
    }.mkString("\n") + "\n" +
    padding.format("--------", "------", "----------", "--------------") + "\n"
  }

  def pretty: String = _pretty(es)
  def prettyElaborated: String = _pretty(elaborated)
}

object Table {

  case class Entry(name: String, symbol: String, operation: String, next: String)
}