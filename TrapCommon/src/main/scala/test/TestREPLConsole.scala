package test

import trap.repl.REPLConsole

object TestREPLConsole extends App {
  def consoleCode(s:String):Seq[String] = {
    Seq("Hello", "world")
  }
  val console = new REPLConsole(consoleCode, 1000, Seq("exit", "q"), "DQL console")
  console.start
}
