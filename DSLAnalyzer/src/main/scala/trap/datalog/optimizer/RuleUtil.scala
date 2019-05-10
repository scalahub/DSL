/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package trap.datalog.optimizer

object RuleUtil {
  def getName(s:String) = {
    val x = s.split('(')(0) 
    if (x.startsWith("!")) x.tail else x 
  }
  def getRuleMap(ruleString:String) = {
    /* input ruleString is any IRIS string representing rules (including comments). Example: 
        foo(?x, ?y) :- foo(?y, ?z), bar(?z, ?x), ?x = 'foo'. // some dummy rule
     */
    // first clean ruleString (remove spaces, tabs, blank lines)
    val cleanedString = ruleString.replace(" ", "").replace("\t", "").lines.filter(_ != "")
    // now filter comments out and get rules as each list[String]
    val rules = cleanedString.filterNot(_.startsWith("//")).foldLeft("")(
      (x, y) => x + y
    ).split('.').filterNot(_.isEmpty).map(_ + ".")
    // each element of rule is a string of type "foo(?x,?y):-foo(?y,?z),bar(?z,?x),?x='foo'."
    val headTailMap = rules.map(_.split(":-")).map(x=>
      (getName(x(0)), x(1).split(',').filter(_.contains('(')).map(getName).toSet)
    )  
    // headTailMap is a tuple, with first element the head (i.e., "foo(?x,?y)") and second element is an set of tail rules
    // with final conditions skipped (final condition is ?x='foo')
    
    val ruleMap = (headTailMap zip rules) map (x => (x._1._1, x._1._2, x._2))
    val ruleNames = ruleMap.map(_._1).toSet
    val grouped = ruleMap.groupBy(_._1)
    val nameDeps = grouped.map(x => (x._1, x._2.flatMap(_._2).filter(y => y != x._1 && ruleNames.contains(y))))
    val ruleDeps = grouped.map(x => (x._1, x._2.map(_._3)))
    (ruleDeps, nameDeps, ruleNames, rules)
  }
}
