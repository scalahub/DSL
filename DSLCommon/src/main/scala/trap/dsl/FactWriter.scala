package trap.dsl

import java.io.FileWriter
import FactDataStructures._
import FactDefUtil._
import FactWriter._ 

class FactWriter(fw:FileWriter, factDefs:Seq[FactDef]) {
  def fact(factName:String, data:Any*) = {  // used for writing java / JIL facts. data is validated against factDefs
    if (factDefs.exists(_.matches(factName, data))) append(fw, factName, data) 
    else throw new DSLConfigException("definition not found for fact: "+getStr(factName, data map (_.getClass.getName)))
  }

  // for writing facts from solver (recommened use) 
  def fact(factName:String, values:Seq[ParaValue[_]]) = 
    if (factDefs.exists(_.matches(factName, values))) 
    factUnverified(factName, values) 

  def factUnverified(factName:String, values:Seq[ParaValue[_]]) = append(fw, factName, values.map(_.value)) 
  
  // write a Fact object directly (not used as of now)
  def fact(fact:Fact) = if (factDefs.contains(fact.factDef)) factUnverified(fact)
  def factUnverified(fact:Fact) = append(fw, fact.factDef.factName, fact.params.map(_.value))  
}

object FactWriter {
  private def append(fw:FileWriter, factName:String, vals:Seq[Any]) = fw.append(getStr(factName, vals)+"\n")  
  
  
  private def quote(a:Any):String = a match {
    case str:String => // "'"+str.replace("\"", "<dquot>").replace("'", "<squot>")+"'" // surround string by single quote 
      val st = str.replace("\"", "<dquot>").replace("'", "<squot>") // surround string by single quote 
      "'"+(if (st.contains("<dquot>")||st.contains("<squot>")) "<const>" else st) +"'"
    case int:Int => int.toString
    case int:Long => int.toString
    case boolean:Boolean => "'"+boolean.toString+"'"
    case decimal:BigDecimal => decimal.toString
    case any => throw new DSLConfigException("unsupported type: "+any.getClass.getName+" ==> any: "+any)
  }
  def getStr(factName:String, params:Seq[Any]) = {
    if (params.isEmpty) throw new DSLConfigException("empty fact: "+factName+":"+params)
    val quoted = params.map(quote)
    val first = quoted.head
    val last = quoted.tail
    factName+last.foldLeft("("+first)((x, y) => x+","+y)+")."
  }
}