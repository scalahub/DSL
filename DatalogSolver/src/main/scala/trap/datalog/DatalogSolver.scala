package trap.datalog

import trap.file.TraitFilePropertyReader
import trap.file.Util._
import java.util.{Map => JMap}
import java.util.{List => JList}
import java.util.ArrayList
import java.util.HashMap
import org.deri.iris.optimisations.magicsets.MagicSets
import org.deri.iris.storage.IRelation
import scala.collection.JavaConversions._
import org.deri.iris.api.basics.IPredicate
import org.deri.iris.api.basics.IQuery
import org.deri.iris.api.basics.IRule
import org.deri.iris.compiler.Parser
import org.deri.iris.KnowledgeBaseFactory._

import trap.Util._ 

class DatalogSolver(kbFiles:String*) extends TraitFilePropertyReader {
  val propertyFile = "datalog.properties"
  val useMagicSets = true // read("useMagicSets", true)
  kbFiles foreach loadFacts
  private val conf = getDefaultConfiguration
  if (useMagicSets) conf.programOptmimisers.add(new MagicSets());
  private var facts:Seq[JMap[IPredicate, IRelation]] = Nil
  var numFacts = 0
  def getNumFacts = numFacts
  private var rules:Seq[JList[IRule]] = Nil
  private var queries:Seq[JList[IQuery]] = Nil
  private def convergeMap(seq:Seq[JMap[IPredicate, IRelation]]) = seq.foldLeft(new HashMap[IPredicate, IRelation])((left, right) => {
      right.foreach(kv => {
          val (key, value) = (kv._1, kv._2)
          if (left.contains(key)) left.get(key).addAll(value)
          else left.put(key, value)
        }
      )
      left
    })
  private def convergeList[A](seq:Seq[JList[A]]):JList[A] = 
    seq.foldLeft(new ArrayList[A].asInstanceOf[JList[A]])((left, right) => {right.foreach(x => left.add(x)); left})

  private def parseFile(dataFile:String) = parseString(readTextFileToString(dataFile))
  private def parseString(s:String) = {
    val p = new Parser
    p.parse(s)
    p
  }
  def loadRules(ruleFiles:Array[String]):Unit = ruleFiles foreach loadRules
  def loadRules(ruleFile:String) = rules = rules :+ parseFile(ruleFile).getRules
  def addRules(rulesString:String) = {
    rules = rules :+ parseString(rulesString).getRules
  }

  def loadFacts(factFiles:Array[String]):Unit = factFiles foreach loadFacts
  def loadFacts(factFile:String) = facts = facts :+ parseFile(factFile).getFacts
  def addFacts(factsString:String) = facts = facts :+ parseString(factsString).getFacts
  
  def loadQueries(queryFiles:Array[String]):Unit = queryFiles foreach loadQueries
  def loadQueries(queryFile:String) = queries = queries :+ parseFile(queryFile).getQueries
  def addQueries(queriesString:String) = queries = queries :+ parseString(queriesString).getQueries
  def removeAllQuries = queries = Nil
  def removeAllRules = rules = Nil
  def getAnswers = {
    val convergedFacts = convergeMap(facts)
    numFacts = convergedFacts.foldLeft(0)((x, y) => x+y._2.size)
    val convergedRules = convergeList(rules)
    val kb = createKnowledgeBase(convergedFacts, convergedRules, conf)    
    convergeList(queries).toSeq map (q => (q, kb.execute(q)))
  }
}

