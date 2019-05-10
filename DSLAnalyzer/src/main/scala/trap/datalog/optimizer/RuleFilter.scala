package trap.datalog.optimizer

import scala.collection.JavaConversions._ 
import trap.dsl.DSLConfig
import trap.xdsl.datalog.RuleDataStructures.DLRule
import RuleUtil._

case class DSLRuleException(rule:String, message:String) extends Exception(message)
import DSLConfig._

class RuleFilter(val ruleString:String) {  
  // ruleString above gives the initial (human written) rules, from config
  
  lazy val (ruleDeps, nameDeps, ruleNames, rules) = getRuleMap(ruleString)
  // above contains some mappings and details about the INITIAL rules 
  
  // below find which of the INITIAL rules are accessed (reached) by the newRules
  // newRules are generated by DQL, and NOT given in the initial config
  def getUsedRules(newRules:Seq[DLRule]) = if (optimizeRules) {
    var visited:Set[String] = Set()
    def addVisited(rule:String) = visited = visited + rule
    val rulesToFollow = newRules.flatMap(x => x.tails.map(_.name).filter(ruleNames.contains(_))).toSet
    def followRule(rule:String):Unit = if (!visited.contains(rule)) {
      addVisited(rule)
      nameDeps.get(rule) match {
        case Some(dep) => dep.foreach(followRule)
        case None => throw DSLRuleException(rule, "No dependency found. Every rule must have a ")
      }
    }
    rulesToFollow.foreach(followRule)
    visited.flatMap(ruleDeps.get(_).get).toArray
  } else rules  
}
