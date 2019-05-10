package trap.datalog

import java.io.FileWriter
import trap.dsl._ 
import org.deri.iris.api.basics.IQuery
import org.deri.iris.storage.IRelation
import trap.Util._ 
import trap.dsl.FactDataStructures._
import trap.xdsl.XMappingDataStructures._

object ResultParser {
  def getResults(results:Seq[(IQuery,IRelation)]):(Int, Seq[String]) = getResultsFromFacts(ResultUtil.getResultValues(results))
    /*
    
results.map(_._2).foreach(x => println(" x : "+x))
results.map(_._1).foreach(x => println(" y : "+x.getVariables.size))

 x : [('Adam', 'Smith', 123, 345, 456), ('Alex', 'Brown', 3323, 1110, 300), ('Bob', 'Mayo', 1355, 9292, 499), ('James', 'Brian', 6757, 390505, 267), ('John', 'Nash', 7887, 790864, 500), ('Alice', 'Baker', 4849, 44272, 650)]
 y : 5
 ---
IQuery example below (via println)
(?- temp2(?firstNameyXP33whGKneufBWRFMKU, ?lastNameyXP33whGKn4XSd72S37u, ?timeyXP33whGKn0STYbbIFGW, ?balanceyXP33whGKn3ybzOYVgwt, ?salaryyXP33whGKnet5csGVpKZ).,

IRelation example below     
[('Adam', 'Smith', 123, 345, 456), ('Alex', 'Brown', 3323, 1110, 300), ('Bob', 'Mayo', 1355, 9292, 499), ('James', 'Brian', 6757, 390505, 267), ('John', 'Nash', 7887, 790864, 500), ('Alice', 'Baker', 4849, 44272, 650)])
 --- 
     */    
    
  def getAggrResults(results:Seq[(IQuery, IRelation, Seq[AggrType])]) = getResultsFromFacts(ResultUtil.getResultAggrValues(results))
  
  def getResultsFromFacts(factSeq:Seq[(String, Seq[Seq[ParaValue[_]]])]):(Int, Seq[String]) = {
    // return values
    //   first parameter is number of rows returned
    //   second parameter is each row in string format
    val resultSize = factSeq.foldLeft(0)((x, y)=>x + y._2.size)
    val strings = factSeq.flatMap{
      case (name, facts) =>
        facts.map{
          case fact =>
            FactWriter.getStr(name, fact.map(_.value))
        }
    }
    (resultSize, strings)
  }
  
}
object ResultWriter {
  
  def writeResults(resultFile:String, results:Seq[(IQuery,IRelation)]):Int = using (new FileWriter(resultFile, true)) { fw => 
    //    results.map(_._2).foreach(x => println(" x : "+x))
    //    results.map(_._1).foreach(x => println(" y : "+x.getVariables.size))
    //     
    //     x : [('Adam', 'Smith', 123, 345, 456), ('Alex', 'Brown', 3323, 1110, 300), ('Bob', 'Mayo', 1355, 9292, 499), ('James', 'Brian', 6757, 390505, 267), ('John', 'Nash', 7887, 790864, 500), ('Alice', 'Baker', 4849, 44272, 650)]
    //     y : 5

    /*
 ---
IQuery example below (via println)
(?- temp2(?firstNameyXP33whGKneufBWRFMKU, ?lastNameyXP33whGKn4XSd72S37u, ?timeyXP33whGKn0STYbbIFGW, ?balanceyXP33whGKn3ybzOYVgwt, ?salaryyXP33whGKnet5csGVpKZ).,

IRelation example below     
[('Adam', 'Smith', 123, 345, 456), ('Alex', 'Brown', 3323, 1110, 300), ('Bob', 'Mayo', 1355, 9292, 499), ('James', 'Brian', 6757, 390505, 267), ('John', 'Nash', 7887, 790864, 500), ('Alice', 'Baker', 4849, 44272, 650)])
 --- 
     */
    val writer = new FactWriter(fw, Seq())
    val factSeq = ResultUtil.getResultValues(results)
    val resultSize = factSeq.foldLeft(0)((x, y)=>x + y._2.size)
    factSeq.foreach(x => {
        val name = x._1
        val facts = x._2
        facts.foreach(values => writer.factUnverified(name, values))
      }
    )
    resultSize
  }
  
  def writeAggrResults(resultFile:String, results:Seq[(IQuery, IRelation, Seq[AggrType])]):Int = using (new FileWriter(resultFile, true)) { fw => 
    val writer = new FactWriter(fw, Seq())
    val factSeq = ResultUtil.getResultAggrValues(results)
    val resultSize = factSeq.foldLeft(0)((x, y)=>x + y._2.size)
    factSeq.foreach(x => {
        val name = x._1
        val facts = x._2
        facts.foreach(values => writer.factUnverified(name, values))
      }
    )
    resultSize
  }
  
  @deprecated 
  def writeResultsVerified(resultFile:String, answers:Seq[(IQuery,IRelation)]):Unit = using (new FileWriter(resultFile)) {
    // uses FactWriter to write facts (unified way, since DatalogCompiler (JILToDatalog) also uses FactWriter.
    // FactWriter requires FactDef verification. That is, a corresponding FactDef must already exist before writing a fact.
    // To change this, use writeAnswerFact
    fw => fw.append("// generated facts from DatalogSolver \n")
    val factSeq = ResultUtil.getResultFacts(answers)
    factSeq foreach ( facts => {
        if (facts.size > 0) { // if at least one fact in answer
          val factWriter = new FactWriter(fw, Array(facts(0).factDef)) // create FactDef and write it
          facts.foreach(factWriter.fact)
        }
      }
    )
  }
  
  @deprecated 
  def writeResultsSimple(resultFile:String, results:Seq[(IQuery,IRelation)]):Unit = using (new FileWriter(resultFile)) {
    // for writing directly using string (no verification with factDefs), does not use FactWriter

    fw => fw.append("// generated facts from DatalogSolver\n")
    getResultStrings(results) foreach (fw.append(_).append("\n"))

    def getFactString(query:String, result:String):Array[String] = if (result == "[]") Array() else 
      result.tail.init.tail.init.split("\\), \\(") map (query.split("\\?- ")(1).split("\\(")(0)+"("+_+").")
      /*       "?- DefVar(?x, ?y).", 
               "[('1', 'c'), ('6', 'tmp1'), ('2', 'b'), ('4', 'a'), ('5', 'b')]\
                [('2', 'b', '4'), ('4', 'a', '6'), ('5', 'b', '4'), ('1', 'c', '5')]\
                [('6')]\
                [('2', 'b', 'good'), ('4', 'a', 'b'), ('5', 'b', 'c')]"
       */
    def getResultStrings(results:Seq[(IQuery,IRelation)]) = results.map (x => getFactString(x._1.toString, x._2.toString)).flatten
  }
}
 