package test.DatalogSolver

import trap.datalog.DatalogSolver
import trap.datalog.ResultWriter
import trap.file.Util._ 
import trap.Util._ 
import scala.collection.JavaConversions._

object TestIris extends App {
  org.deri.iris.factory.Factory.BASIC.createTuple(Nil)
  
  
}

object Main {
  def main(args: Array[String]): Unit = {
    if (args.size < 4) {
      println ("Usage java -jar TrapDatalog.jar <facts_dir_or_file> <rules_dir_or_file> <query_dir_or_file> <output_file>")
      println ("   <facts_dir_or_file> is a directory with '.fact' files (containing Datalog facts) or a file")
      println ("   <rules_dir_or file> is a directory with '.rule' files (containing Datalog rules) or a file")
      println ("   <query_dir_or_file> is a directory with '.query' files (containing Datalog queries) or a file")
      println ("   <output_file> is a file where output facts will be written")
    } else {
      val (facts, rules, queries, resultFile) = (args(0), args(1), args(2), args(3))
      val dl = new DatalogSolver
      dl.loadFacts(if (isDir(facts)) getAllFiles(facts, Array("dl"), false) else Array(facts))
      dl.loadRules(if (isDir(rules)) getAllFiles(rules, Array("rule"), false) else Array(rules))
      dl.loadQueries(if (isDir(queries)) getAllFiles(args(2), Array("query"), false) else Array(queries))
      ResultWriter.writeResults(resultFile, dl.getAnswers)
      // ResultWriter.writeResultsVerified(resultFile, dl.getAnswers)
      // ResultWriter.writeResultsSimple(resultFile, dl.getAnswers)
    }
  }
}