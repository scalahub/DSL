/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package trap.xdsl.datalog

import org.apache.hadoop.fs.GlobPattern
import trap.xdsl.XDSLDataStructures.GLOB
import trap.xdsl.XDSLDataStructures.Op
import trap.xdsl.XDSLDataStructures.REGEX

object RuleDataStructures {
  var wcCounter = 0
  def getWCCtr = {wcCounter += 1; wcCounter.toString}

  // values
  abstract class DLValue[T](t:T)
  case class DLIntValue(i:Int) extends DLValue(i) { override def toString = i.toString }
  // Code is designed so that String contains quotes. To fix this!
  case class DLStringValue(s:String) extends DLValue(s) { override def toString = s }
  case class DLBooleanValue(s:Boolean) extends DLValue(s) { override def toString = "'"+s.toString+"'" }
  case class DLDecimalValue(s:BigDecimal) extends DLValue(s) { override def toString = s.toString }
  case class DLConstValue(s:String) extends DLValue(s) { override def toString = s }
  // variables
  trait DLVar
  object WildCard extends DLVar {
    override def toString = "?ANY"+getWCCtr 
  }
  case class DLAttrVar(name:String) extends DLValue(name) with DLVar { 
    override def toString = "?"+name.toString 
  }
  case class DLPrimaryKeyVar(name:String) extends DLValue(name) with DLVar { 
    override def toString = "?"+name.toString 
  }

  
  case class DLTailSchema(name:String, vars:Seq[DLVar], isNegated:Boolean = false) {
    override def toString = (if (isNegated) "!" else "") + name + "("+vars.map(_.toString).reduceLeft(_+","+_)+")"
    def negate = DLTailSchema(name, vars, true)
  }
  case class DLHeadSchema(name:String, vars:Seq[DLPrimaryKeyVar]) { 
    lazy val toTailSchema = DLTailSchema(name, vars) 
    override def toString = "?-"+name+"("+vars.map(_.toString).reduceLeft(_+","+_)+")."
  }
  case class DLVarOpVal(dlVar:DLAttrVar, op:Op, value:DLValue[_]){ 
    override def toString = value match {
      case DLStringValue(s) if op == REGEX => "REGEX("+dlVar.toString+","+value.toString+")"
      case DLStringValue(s) if op == GLOB => val regex = GlobPattern.getRegex(value.toString).replace("[]", "\\[\\]") 
          "REGEX("+dlVar.toString+","+regex+")"
      case _ =>    
        // NOTE: For strings, the quotes are part of the data. To fix this eventually, and insert
        // quotes when converting to string
        dlVar.toString+" "+op.toString+" "+value.toString 
    }
  }
  // Foo(A, B) :- Bar(B, D). Baz(A, C), A != 3
  // In above:
  //  Foo(A, B) is the head:DLHeadSchema
  //  Seq(Bar(B, D). Baz(A, C)) is tails:Seq[DLTailSchema]
  //  Seq(A != 3) is the varOpVal:Seq[DLVarOpVal]
  case class DLRule(head:DLHeadSchema, tails:Seq[DLTailSchema], varOpVal:Seq[DLVarOpVal]) {
    def getRuleSting = toString.replace(",", ", ")
    override def toString = 
      head.name+"("+head.vars.map(_.toString).reduceLeft(_+","+_)+")"+" :- "+
      //tails.map(tail => tail.name+"("+tail.vars.map(_.toString).reduceLeft(_+","+_)+")").reduceLeft(_+","+_)+
      tails.map(_.toString).reduceLeft(_+","+_)+
      (if (varOpVal.size == 0) "" else ","+varOpVal.map(_.toString).reduceLeft(_+","+_))+"."
  }
}
