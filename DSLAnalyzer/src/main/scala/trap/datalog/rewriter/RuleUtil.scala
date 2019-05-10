
package trap.datalog.rewriter

import java.util.concurrent.atomic.AtomicInteger
import scala.collection.mutable.ArrayBuffer
import trap.datalog.validator.RuleParsingException
import trap.xdsl.XDSLDataStructures._
import trap.xdsl.datalog.RuleDataStructures._

class RuleUtil(rulesString:String) {
  
  ///////////// new stuff ///////////// 29 Dec 2017
  // Rules parser: rules (string) => Seq[DLRule] // needed for rule rewriting
  // RuleUtil parses rules (string) => Seq[IRISRule] // Iris rules are use for validation and don't contain all info
  // in particular they dont contain varopvals + some other things
  // 
  // text in string cannot contain the chars .//   Example Foo(?x, 'abc.//d'). 
  private val AUTO = "AUTO3d838d3c3"
  lazy val getRules = {
    /* input ruleString is any IRIS string representing rules (including comments). Example: 
        foo(?x, ?y) :- foo(?y, ?z), bar(?z, ?x), ?x = 'foo'. // some dummy rule
     */
    // first clean ruleString (remove spaces, tabs, blank lines)
    val cleanedString = subst(rulesString.replace(" ", "").replace("\t", "")).lines.map(_.trim).filterNot(_.isEmpty).toArray
    // now filter comments out and get rules as each list[String]
    val rules = cleanedString.filterNot(_.startsWith("//")).foldLeft("")(
      (x, y) => x + y
    ).split(ruleSeparator).map(_.trim).filterNot(_.isEmpty).filterNot(_.startsWith("//"))
    rules.map(getRuleFromString)
  }
  private def getRuleFromString(s:String) = {
    //Declaration(?x,?variableName,?userDefinedType,?variableType):-
    //FunctionCode(?f,?x),VariableDeclarationStatement(?x,?startChar1,?numChar1),VariableDeclaration(?y,?startChar2,?numChar2,?variableName,?variableType),UserDefinedTypeName(?z,?startChar3,?numChar3,?userDefinedType),Parent(?x,?y)"""
    
    val ar = s.split(":-", 2)    
    val left = ar(0) // Declaration(?x,?variableName,?userDefinedType,?variableType)
    val right = ar(1) // FunctionCode(?f,?x),VariableDeclarationStatement(?x,?startChar1,?numChar1),VariableDeclaration(?y,?startChar2,?numChar2,?variableName,?variableType),UserDefinedTypeName(?z,?startChar3,?numChar3,?userDefinedType),Parent(?x,?y)
    
    val headAsTail = getRelationFromString(left.init) // remote the ) from end
    if (headAsTail.isNegated) throw new Exception("Head cannot be negated")
    val headAttrVars = headAsTail.vars.map{
      case v:DLAttrVar => DLPrimaryKeyVar(v.name)
      case _ => ??? // should not happen
    }
    val head = DLHeadSchema(headAsTail.name, headAttrVars)
    
    val headAutoVarOpVals = head.vars.collect{
      case v if v.name.startsWith(AUTO) => DLVarOpVal(DLAttrVar(v.name), EQ, DLStringValue(varMap.get(v.name).get))
    }
    val remaining = right.split(')')
    val (tailStrs, varOpValStr) = remaining.partition(_.contains('('))
    
    val tails:Seq[DLTailSchema] = tailStrs.map(getRelationFromString)
    
    val varOpVals:Seq[DLVarOpVal] = getVarOpVals(varOpValStr.mkString)
    val tailAutoVarOpVals = tails.flatMap{tail =>
      tail.vars.collect{
        case v:DLAttrVar if v.name.startsWith(AUTO) => DLVarOpVal(DLAttrVar(v.name), EQ, DLStringValue(varMap.get(v.name).get))        
      }      
    }
    DLRule(head, tails, varOpVals ++ tailAutoVarOpVals ++ headAutoVarOpVals)
  }
  private def getVarOpVals(s:String):Seq[DLVarOpVal] = {
    // s can be:
    // ,?x=?y,?y=23,?x<4,?stmt=?sid
    // 
    // can be also ?x=?AUTO123 due to subst
    s.split(',').filterNot(_.isEmpty).map(getVarOpVal)    
  }
  private def getVarOpVal(s:String):DLVarOpVal = {
    //  s can be 
    // ?x=?AUTO123
    // ?x=233
    // ?x<34
    // ?x<=34
    // ?x!=34
    // ?x<?y
    // etc. See below ops
    // val (eql, neq, lt, gt, geq, leq, regex, glob) = ("=", "!=", "<", ">", ">=", "<=", "~~", "~")
    val seperated = Array(eql, neq, lt, gt, geq, leq, regex, glob).sortBy(-_.size).map{op =>
      (op, ruleSeparator+op+ruleSeparator)
    }
    val newStr = seperated.foldLeft(s){
      case (str, (oldOp, newOp)) => str.replace(oldOp, newOp)
    }
    val ar = newStr.split(ruleSeparator)
    DLVarOpVal(DLAttrVar(ar(0).drop(1)), getOpFromString(ar(1)), getVar(ar(2)))
  }
  private def getVar(s:String):DLValue[_] = {
    // ?AUTO123
    // 233
    // ?y
    if (s.startsWith("?")){ // variable or string
      val varName = s.drop(1)
      if (varMap.contains(varName)) DLStringValue(varMap.get(varName).get) else DLAttrVar(varName)
    } else DLIntValue(s.toInt)
  }
  private def getRelationFromString(s:String) = {
    //
    // s is any of the following
    //FunctionCode(?f,?x
    //,VariableDeclarationStatement(?x,?startChar1,?numChar1
    //,VariableDeclaration(?y,?startChar2,?numChar2,?variableName,?variableType
    //,UserDefinedTypeName(?z,?startChar3,?numChar3,?userDefinedType
    //,Parent(?x,?y
    //  
    //  foo(?y,?z),bar(?z,?x),?x='foo',?y=?z
    // maps s to 
    //  foo(?y,?z
    //  ,bar(?z,?x
    //  ,?x='foo',?y='a',?x<4,?stmt=?sid // actually cannot have strings anymore due to subst
    // 
    val ar = s.split("\\(", 2)
    val left = if (ar(0).startsWith(",")) ar(0).tail else ar(0) // ,Parent or Parent
    val right = ar(1) // ?x,?y or ?x,123
    val (name, isNegated) = getNameIsNegated(left)
    val (vars, varOpVals) = getVarsAndVarOps(right)
    DLTailSchema(name, vars, isNegated)
  }
  private def getNameIsNegated(s:String) = if (s.startsWith("!")) (s.tail, true) else (s, false)
  private def getVarsAndVarOps(s:String):(Array[DLAttrVar], Set[DLVarOpVal]) = {
    //?x,?variableName,234,?userDefinedType,?variableType
    var varOpVals:Set[DLVarOpVal] = Set()
    val vars:Array[DLAttrVar] = s.split(',').map{v =>
      if (v.startsWith("?")) { // variable
        val varName = v.drop(1)
        if (varMap.contains(varName)) { // auto variable
          varOpVals += DLVarOpVal(DLAttrVar(varName), EQ, DLStringValue(varMap.get(varName).get))
        }
        DLAttrVar(varName)
      } else { // number or boolean
        if (v.forall("0123456789".toSeq.contains)) {
          val varName = AUTO+ctr.getAndIncrement
          varOpVals += DLVarOpVal(DLAttrVar(varName), EQ, DLIntValue(v.toInt))
          DLAttrVar(varName)
        } else throw new RuleParsingException("Unknown parameter type: "+v, "Only String and Int supported")
      }
    }
    (vars, varOpVals)
  }
  private val charBuf = new ArrayBuffer[Char]()
  
  private val ruleSeparator:String = -123.toChar.toString // ﾅ
  // example Foo(?x, 'abc') is mapped to Foo(?x, ?AUTO123), ?AUTO123 = 'abc'
  // below map stores AUTO123 -> 'abc'   NOTE: it does not store the ? char
  private var varMap:Map[String, String] = Map() // 
  private val ctr = new AtomicInteger(0)
  private def subst(chars:String) = {
    var (isWithinQuote, isEscaped) = (false, false) // within quote = Foo(?x,'hel\'l\\o():-)')    // escape = '\'' or '\\'
    for (i <- chars) yield {
      i match {
        case any if isEscaped => // last char was escape char \
          //      v  v                          
          // 'hel\'l\\o():-)' (next char after escape char)
          charBuf += i
          isEscaped = false 
          ""
        case '\'' if isWithinQuote => 
          //                v
          // 'hel\'l\\o():-)' (end quote encountered)
          val varName = AUTO+ctr.getAndIncrement
          val str = charBuf.toArray.mkString
          val dlStr = s"'$str'" // surround by single quotes
          varMap += varName -> dlStr
          // println(s"ADDING $varName -> $dlStr")
          charBuf.clear
          isWithinQuote = false
          "?"+varName
        case '\\' if isWithinQuote => 
          //     v  v         
          // 'hel\'l\\o():-)' (escape encountered)
          charBuf += i
          isEscaped = true;
          ""
        case any if isWithinQuote => 
          //                v
          // 'hel\'l\\o():-)' (end quote encountered)
          charBuf += i
          ""
        case '\'' =>           
          // v               
          // 'hel\'l\\o():-)' (first quote encountered)
          isWithinQuote = true 
          ""
        case '.' => 
          ruleSeparator
          //                          v               
          // Foo(?x, 'hel\'l\\o():-)'). (last point (dot) encountered)
        case _ => 
          i.toString
      }
    }
  }.mkString
  
/*
// -- RULESTRING -- 
Declaration(?x,?variableName,?userDefinedType,?variableType):-
FunctionCode(?f,?x),VariableDeclarationStatement(?x,?startChar1,?numChar1),VariableDeclaration(?y,?startChar2,?numChar2,?variableName,?variableType),UserDefinedTypeName(?z,?startChar3,?numChar3,?userDefinedType),Parent(?x,?y).Declaration(?x,?variableName,?elementaryType,?variableType):-FunctionCode(?f,?x),VariableDeclarationStatement(?x,?startChar1,?numChar1),VariableDeclaration(?y,?startChar2,?numChar2,?variableName,?variableType),ElementaryTypeName(?z,?startChar3,?numChar3,?elementaryType),Parent(?x,?y).Assign(?x,?variableName,?elementaryType,?literalType,?literalValue):-Declaration(?x,?variableName,?elementaryType,?variableType),Literal(?y,?startChar1,?numChar1,?hex,?subden,?token,?literalType,?literalValue),Parent(?x,?y).Assign(?x,?identifierValue,?variableType,?identifierType,?identifierValue):-FunctionCode(?f,?x),Parent(?x,?y),Assignment(?y,?startChar1,?numChar1,?variableType,?operator),Identifier(?z,?startChar2,?numChar2,?identifierType,?identifierValue),Parent(?y,?z).Nop(?x):-ExpressionStatement(?x,?startChar,?numChar).Nop(?x):-Identifier(?x,?startChar,?numChars,?type,?value).Nop(?x):-Literal(?x,?startChar,?numChars,?hexvalue,?subdenomination,?token,?type,?value).Nop(?x):-BinaryOperation(?x,?startChar,?numChar,?type,?operator).Nop(?x):-UnaryOperation(?x,?startChar,?numChar,?prefix,?type,?operator).Nop(?x):-Block(?x,?startChar,?numChar).Nop(?x):-UserDefinedTypeName(?x,?startChar,?numChar,?name).Nop(?x):-IfStatement(?x,?startChar,?numChar).Nop(?x):-WhileStatement(?x,?startChar,?numChar).Nop(?x):-DoWhileStatement(?x,?startChar,?numChar).Nop(?x):-MemberAccess(?x,?startChar,?numChars,?membername,?type).Nop(?x):-BreakStmt(?x).FirstChildAncestor(?ancestor,?descendent):-Ancestor(?x,?descendent),FirstChild(?ancestor,?x).InLoop(?x):-WhileStatement(?y,?startChar,?numChars),Ancestor(?y,?x).InLoop(?x):-ForStatement(?y,?startChar,?numChars),Ancestor(?y,?x),!FirstChildAncestor(?y,?x).BreakStmt(?x):-Throw(?x,?startChar,?numChars).BreakStmt(?x):-Return(?x,?startChar,?numChars).BreakStmt(?x):-Break(?x,?startChar,?numChars).BreakStmt(?x):-Continue(?x,?startChar,?numChars).Flow(?x,?y):-FunctionCode(?z,?x),FunctionCode(?z,?y),Ancestor(?ax,?x),Pred(?ax,?y),Ancestor(?x,?someStmt),!BreakStmt(?someStmt).Flow(?x,?y):-FunctionCode(?f,?x),FunctionCode(?f,?y),Parent(?x,?y).Flow(?x,?y):-Flow(?x,?z),Flow(?z,?y).FlowAcross(?x,?y):-Invoke(?x,?objType,?instanceName,?methodName,?retType),FunctionDefinition(?z,?startChar1,?numChars1,?constant,?payable,?visibility,?methodName),Block(?y,?startChar2,?numChars2),Parent(?z,?y).FunctionCode(?z,?y):-Ancestor(?x,?y),FunctionDefinition(?z,?startChar1,?numChars1,?constant,?payable,?visibility,?name),Block(?x,?startChar2,?numChars2),Parent(?z,?x).Ancestor(?x,?y):-Ancestor(?x,?z),Ancestor(?z,?y).Ancestor(?x,?y):-Parent(?x,?y).SecondChild(?x,?y):-FirstChild(?x,?z),Pred(?z,?y).FuncParamDef(?x,?y):-FirstChild(?x,?y),FunctionDefinition(?x,?startChar,?numChars,?constant,?payable,?visibility,?name).FuncRetDef(?x,?y):-FuncParamDef(?x,?z),Pred(?z,?y).FuncBody(?x,?y):-FuncRetDef(?x,?z),Pred(?z,?y).Func(?x,?x):-FuncParamDef(?x,?y).Invoke(?statementID,?objType,?instanceName,?methodName,?retType):-FunctionCall(?statementID,?startChar1,?numChars1,?typeConversion,?retType),MemberAccess(?subStatementID,?startChar2,?numChars2,?methodName,?type1),FirstChild(?statementID,?subStatementID),MemberAccess(?subSubStatementID,?startChar3,?numChars3,?instanceName,?objType),FirstChild(?subStatementID,?subSubStatementID).Invoke(?statementID,?objType,?instanceName,?methodName,?retType):-FunctionCall(?statementID,?startChar1,?numChars1,?typeConversion,?retType),MemberAccess(?subStatementID,?startChar2,?numChars2,?methodName,?type1),FirstChild(?statementID,?subStatementID),Identifier(?subSubStatementID,?startChar3,?numChars3,?objType,?instanceName),Parent(?subStatementID,?subSubStatementID).Invoke(?statementID,'unknown','this',?methodName,?retType):-FunctionCall(?statementID,?startChar1,?numChars1,?typeConversion,?retType),Identifier(?subStatementID,?startChar2,?numChars2,?idType,?methodName),FirstChild(?statementID,?subStatementID).Invoke(?statementID,'unknown','this','<constructor>',?retType):-FunctionCall(?statementID,?startChar1,?numChars1,?typeConversion,?retType),NewExpression(?subStatementID,?startChar2,?numChars2,?idType),FirstChild(?statementID,?subStatementID).

temp4(?statementID) :- Invoke(?statementID,?ANY157,?ANY158,?methodNameoaouvEtqFm,?ANY159),?methodNameoaouvEtqFm = 'send'.
send(?statementID) :- temp4(?statementID).
temp3(?statementID1,?statementID2) :- Flow(?statementID1,?statementID2),send(?statementIDeTWCZoO533),?statementID1 = ?statementIDeTWCZoO533.
all(?statementID1,?statementID2) :- temp3(?statementID1,?statementID2).
temp2(?statementID1,?statementID2) :- all(?statementID1,?statementID2).
temp6(?statementID1,?statementID2) :- Flow(?statementID1,?statementID2),send(?statementIDQh0VWrGTA7),?statementID1 = ?statementIDQh0VWrGTA7.
temp7(?statementID1,?statementID2) :- Flow(?statementID1,?statementID2),Nop(?statementID8saLTESLsL),?statementID2 = ?statementID8saLTESLsL.
temp8(?statementID1,?statementID2) :- temp6(?statementID1,?statementID2),temp7(?statementID1,?statementID2).
nop(?statementID1,?statementID2) :- temp8(?statementID1,?statementID2).
temp5(?statementID1,?statementID2) :- nop(?statementID1,?statementID2).
temp9(?statementID1,?statementID2) :- temp2(?statementID1,?statementID2),!temp5(?statementID1,?statementID2).
bad(?statementID1,?statementID2) :- temp9(?statementID1,?statementID2).
temp1(?statementID1,?statementID2) :- bad(?statementID1,?statementID2).
temp10(?textyXP33whGKnZFg84Vw6Xt,?k9eTUFSohj1,?textyXP33whGKnOawedZkCgB) :- Src(?statementID1,?startCharyXP33whGKnXRQyPUIxh7,?numCharsyXP33whGKn0Ros8RqVZd,?textyXP33whGKnZFg84Vw6Xt),Src(?statementID2,?startCharyXP33whGKn6VYjAKHeQD,?numCharsyXP33whGKnIiKHcEi9GP,?textyXP33whGKnOawedZkCgB),temp1(?statementID1,?statementID2),?k9eTUFSohj1 = '<0>'.

   
Generated rules below
Declaration(?x,?variableName,?userDefinedType,?variableType) :- FunctionCode(?f,?x),,VariableDeclarationStatement(?x,?startChar1,?numChar1),,VariableDeclaration(?y,?startChar2,?numChar2,?variableName,?variableType),,UserDefinedTypeName(?z,?startChar3,?numChar3,?userDefinedType),,Parent(?x,?y).
Declaration(?x,?variableName,?elementaryType,?variableType) :- FunctionCode(?f,?x),,VariableDeclarationStatement(?x,?startChar1,?numChar1),,VariableDeclaration(?y,?startChar2,?numChar2,?variableName,?variableType),,ElementaryTypeName(?z,?startChar3,?numChar3,?elementaryType),,Parent(?x,?y).
Assign(?x,?variableName,?elementaryType,?literalType,?literalValue) :- Declaration(?x,?variableName,?elementaryType,?variableType),,Literal(?y,?startChar1,?numChar1,?hex,?subden,?token,?literalType,?literalValue),,Parent(?x,?y).
Assign(?x,?identifierValue,?variableType,?identifierType,?identifierValue) :- FunctionCode(?f,?x),,Parent(?x,?y),,Assignment(?y,?startChar1,?numChar1,?variableType,?operator),,Identifier(?z,?startChar2,?numChar2,?identifierType,?identifierValue),,Parent(?y,?z).
Nop(?x) :- ExpressionStatement(?x,?startChar,?numChar).
Nop(?x) :- Identifier(?x,?startChar,?numChars,?type,?value).
Nop(?x) :- Literal(?x,?startChar,?numChars,?hexvalue,?subdenomination,?token,?type,?value).
Nop(?x) :- BinaryOperation(?x,?startChar,?numChar,?type,?operator).
Nop(?x) :- UnaryOperation(?x,?startChar,?numChar,?prefix,?type,?operator).
Nop(?x) :- Block(?x,?startChar,?numChar).
Nop(?x) :- UserDefinedTypeName(?x,?startChar,?numChar,?name).
Nop(?x) :- IfStatement(?x,?startChar,?numChar).
Nop(?x) :- WhileStatement(?x,?startChar,?numChar).
Nop(?x) :- DoWhileStatement(?x,?startChar,?numChar).
Nop(?x) :- MemberAccess(?x,?startChar,?numChars,?membername,?type).
Nop(?x) :- BreakStmt(?x).
FirstChildAncestor(?ancestor,?descendent) :- Ancestor(?x,?descendent),,FirstChild(?ancestor,?x).
InLoop(?x) :- WhileStatement(?y,?startChar,?numChars),,Ancestor(?y,?x).
InLoop(?x) :- ForStatement(?y,?startChar,?numChars),,Ancestor(?y,?x),,!FirstChildAncestor(?y,?x).
BreakStmt(?x) :- Throw(?x,?startChar,?numChars).
BreakStmt(?x) :- Return(?x,?startChar,?numChars).
BreakStmt(?x) :- Break(?x,?startChar,?numChars).
BreakStmt(?x) :- Continue(?x,?startChar,?numChars).
Flow(?x,?y) :- FunctionCode(?z,?x),,FunctionCode(?z,?y),,Ancestor(?ax,?x),,Pred(?ax,?y),,Ancestor(?x,?someStmt),,!BreakStmt(?someStmt).
Flow(?x,?y) :- FunctionCode(?f,?x),,FunctionCode(?f,?y),,Parent(?x,?y).
Flow(?x,?y) :- Flow(?x,?z),,Flow(?z,?y).
FlowAcross(?x,?y) :- Invoke(?x,?objType,?instanceName,?methodName,?retType),,FunctionDefinition(?z,?startChar1,?numChars1,?constant,?payable,?visibility,?methodName),,Block(?y,?startChar2,?numChars2),,Parent(?z,?y).
FunctionCode(?z,?y) :- Ancestor(?x,?y),,FunctionDefinition(?z,?startChar1,?numChars1,?constant,?payable,?visibility,?name),,Block(?x,?startChar2,?numChars2),,Parent(?z,?x).
Ancestor(?x,?y) :- Ancestor(?x,?z),,Ancestor(?z,?y).
Ancestor(?x,?y) :- Parent(?x,?y).
SecondChild(?x,?y) :- FirstChild(?x,?z),,Pred(?z,?y).
FuncParamDef(?x,?y) :- FirstChild(?x,?y),,FunctionDefinition(?x,?startChar,?numChars,?constant,?payable,?visibility,?name).
FuncRetDef(?x,?y) :- FuncParamDef(?x,?z),,Pred(?z,?y).
FuncBody(?x,?y) :- FuncRetDef(?x,?z),,Pred(?z,?y).
Func(?x,?x) :- FuncParamDef(?x,?y).
Invoke(?statementID,?objType,?instanceName,?methodName,?retType) :- FunctionCall(?statementID,?startChar1,?numChars1,?typeConversion,?retType),,MemberAccess(?subStatementID,?startChar2,?numChars2,?methodName,?type1),,FirstChild(?statementID,?subStatementID),,MemberAccess(?subSubStatementID,?startChar3,?numChars3,?instanceName,?objType),,FirstChild(?subStatementID,?subSubStatementID).
Invoke(?statementID,?objType,?instanceName,?methodName,?retType) :- FunctionCall(?statementID,?startChar1,?numChars1,?typeConversion,?retType),,MemberAccess(?subStatementID,?startChar2,?numChars2,?methodName,?type1),,FirstChild(?statementID,?subStatementID),,Identifier(?subSubStatementID,?startChar3,?numChars3,?objType,?instanceName),,Parent(?subStatementID,?subSubStatementID).
Invoke(?statementID,?ANY0,?ANY1,?methodName,?retType) :- FunctionCall(?statementID,?startChar1,?numChars1,?typeConversion,?retType),,Identifier(?subStatementID,?startChar2,?numChars2,?idType,?methodName),,FirstChild(?statementID,?subStatementID).
Invoke(?statementID,?ANY2,?ANY3,?ANY4,?retType) :- FunctionCall(?statementID,?startChar1,?numChars1,?typeConversion,?retType),,NewExpression(?subStatementID,?startChar2,?numChars2,?idType),,FirstChild(?statementID,?subStatementID).
temp4(?statementID) :- Invoke(?statementID,?ANY157,?ANY158,?methodNameoaouvEtqFm,?ANY159).
send(?statementID) :- temp4(?statementID).
temp3(?statementID1,?statementID2) :- Flow(?statementID1,?statementID2),,send(?statementIDeTWCZoO533).
all(?statementID1,?statementID2) :- temp3(?statementID1,?statementID2).
temp2(?statementID1,?statementID2) :- all(?statementID1,?statementID2).
temp6(?statementID1,?statementID2) :- Flow(?statementID1,?statementID2),,send(?statementIDQh0VWrGTA7).
temp7(?statementID1,?statementID2) :- Flow(?statementID1,?statementID2),,Nop(?statementID8saLTESLsL).
temp8(?statementID1,?statementID2) :- temp6(?statementID1,?statementID2),,temp7(?statementID1,?statementID2).
nop(?statementID1,?statementID2) :- temp8(?statementID1,?statementID2).
temp5(?statementID1,?statementID2) :- nop(?statementID1,?statementID2).
temp9(?statementID1,?statementID2) :- temp2(?statementID1,?statementID2),,!temp5(?statementID1,?statementID2).
bad(?statementID1,?statementID2) :- temp9(?statementID1,?statementID2).
temp1(?statementID1,?statementID2) :- bad(?statementID1,?statementID2).
temp10(?textyXP33whGKnZFg84Vw6Xt,?k9eTUFSohj1,?textyXP33whGKnOawedZkCgB) :- Src(?statementID1,?startCharyXP33whGKnXRQyPUIxh7,?numCharsyXP33whGKn0Ros8RqVZd,?textyXP33whGKnZFg84Vw6Xt),,Src(?statementID2,?startCharyXP33whGKn6VYjAKHeQD,?numCharsyXP33whGKnIiKHcEi9GP,?textyXP33whGKnOawedZkCgB),,temp1(?statementID1,?statementID2).
BUILD SUCCESSFUL (total time: 7 seconds)
   
 */  
}
object TestParser extends App {
  val rulesString = """// -- RULESTRING -- 
Declaration(?x,?variableName,?userDefinedType,?variableType):-FunctionCode(?f,?x),VariableDeclarationStatement(?x,?startChar1,?numChar1),VariableDeclaration(?y,?startChar2,?numChar2,?variableName,?variableType),UserDefinedTypeName(?z,?startChar3,?numChar3,?userDefinedType),Parent(?x,?y).Declaration(?x,?variableName,?elementaryType,?variableType):-FunctionCode(?f,?x),VariableDeclarationStatement(?x,?startChar1,?numChar1),VariableDeclaration(?y,?startChar2,?numChar2,?variableName,?variableType),ElementaryTypeName(?z,?startChar3,?numChar3,?elementaryType),Parent(?x,?y).Assign(?x,?variableName,?elementaryType,?literalType,?literalValue):-Declaration(?x,?variableName,?elementaryType,?variableType),Literal(?y,?startChar1,?numChar1,?hex,?subden,?token,?literalType,?literalValue),Parent(?x,?y).Assign(?x,?identifierValue,?variableType,?identifierType,?identifierValue):-FunctionCode(?f,?x),Parent(?x,?y),Assignment(?y,?startChar1,?numChar1,?variableType,?operator),Identifier(?z,?startChar2,?numChar2,?identifierType,?identifierValue),Parent(?y,?z).Nop(?x):-ExpressionStatement(?x,?startChar,?numChar).Nop(?x):-Identifier(?x,?startChar,?numChars,?type,?value).Nop(?x):-Literal(?x,?startChar,?numChars,?hexvalue,?subdenomination,?token,?type,?value).Nop(?x):-BinaryOperation(?x,?startChar,?numChar,?type,?operator).Nop(?x):-UnaryOperation(?x,?startChar,?numChar,?prefix,?type,?operator).Nop(?x):-Block(?x,?startChar,?numChar).Nop(?x):-UserDefinedTypeName(?x,?startChar,?numChar,?name).Nop(?x):-IfStatement(?x,?startChar,?numChar).Nop(?x):-WhileStatement(?x,?startChar,?numChar).Nop(?x):-DoWhileStatement(?x,?startChar,?numChar).Nop(?x):-MemberAccess(?x,?startChar,?numChars,?membername,?type).Nop(?x):-BreakStmt(?x).FirstChildAncestor(?ancestor,?descendent):-Ancestor(?x,?descendent),FirstChild(?ancestor,?x).InLoop(?x):-WhileStatement(?y,?startChar,?numChars),Ancestor(?y,?x).InLoop(?x):-ForStatement(?y,?startChar,?numChars),Ancestor(?y,?x),!FirstChildAncestor(?y,?x).BreakStmt(?x):-Throw(?x,?startChar,?numChars).BreakStmt(?x):-Return(?x,?startChar,?numChars).BreakStmt(?x):-Break(?x,?startChar,?numChars).BreakStmt(?x):-Continue(?x,?startChar,?numChars).Flow(?x,?y):-FunctionCode(?z,?x),FunctionCode(?z,?y),Ancestor(?ax,?x),Pred(?ax,?y),Ancestor(?x,?someStmt),!BreakStmt(?someStmt).Flow(?x,?y):-FunctionCode(?f,?x),FunctionCode(?f,?y),Parent(?x,?y).Flow(?x,?y):-Flow(?x,?z),Flow(?z,?y).FlowAcross(?x,?y):-Invoke(?x,?objType,?instanceName,?methodName,?retType),FunctionDefinition(?z,?startChar1,?numChars1,?constant,?payable,?visibility,?methodName),Block(?y,?startChar2,?numChars2),Parent(?z,?y).FunctionCode(?z,?y):-Ancestor(?x,?y),FunctionDefinition(?z,?startChar1,?numChars1,?constant,?payable,?visibility,?name),Block(?x,?startChar2,?numChars2),Parent(?z,?x).Ancestor(?x,?y):-Ancestor(?x,?z),Ancestor(?z,?y).Ancestor(?x,?y):-Parent(?x,?y).SecondChild(?x,?y):-FirstChild(?x,?z),Pred(?z,?y).FuncParamDef(?x,?y):-FirstChild(?x,?y),FunctionDefinition(?x,?startChar,?numChars,?constant,?payable,?visibility,?name).FuncRetDef(?x,?y):-FuncParamDef(?x,?z),Pred(?z,?y).FuncBody(?x,?y):-FuncRetDef(?x,?z),Pred(?z,?y).Func(?x,?x):-FuncParamDef(?x,?y).Invoke(?statementID,?objType,?instanceName,?methodName,?retType):-FunctionCall(?statementID,?startChar1,?numChars1,?typeConversion,?retType),MemberAccess(?subStatementID,?startChar2,?numChars2,?methodName,?type1),FirstChild(?statementID,?subStatementID),MemberAccess(?subSubStatementID,?startChar3,?numChars3,?instanceName,?objType),FirstChild(?subStatementID,?subSubStatementID).Invoke(?statementID,?objType,?instanceName,?methodName,?retType):-FunctionCall(?statementID,?startChar1,?numChars1,?typeConversion,?retType),MemberAccess(?subStatementID,?startChar2,?numChars2,?methodName,?type1),FirstChild(?statementID,?subStatementID),Identifier(?subSubStatementID,?startChar3,?numChars3,?objType,?instanceName),Parent(?subStatementID,?subSubStatementID).Invoke(?statementID,'unknown','this',?methodName,?retType):-FunctionCall(?statementID,?startChar1,?numChars1,?typeConversion,?retType),Identifier(?subStatementID,?startChar2,?numChars2,?idType,?methodName),FirstChild(?statementID,?subStatementID).Invoke(?statementID,'unknown','this','<constructor>',?retType):-FunctionCall(?statementID,?startChar1,?numChars1,?typeConversion,?retType),NewExpression(?subStatementID,?startChar2,?numChars2,?idType),FirstChild(?statementID,?subStatementID).

temp4(?statementID) :- Invoke(?statementID,?ANY157,?ANY158,?methodNameoaouvEtqFm,?ANY159),?methodNameoaouvEtqFm = 'send'.
send(?statementID) :- temp4(?statementID).
temp3(?statementID1,?statementID2) :- Flow(?statementID1,?statementID2),send(?statementIDeTWCZoO533),?statementID1 = ?statementIDeTWCZoO533.
all(?statementID1,?statementID2) :- temp3(?statementID1,?statementID2).
temp2(?statementID1,?statementID2) :- all(?statementID1,?statementID2).
temp6(?statementID1,?statementID2) :- Flow(?statementID1,?statementID2),send(?statementIDQh0VWrGTA7),?statementID1 = ?statementIDQh0VWrGTA7.
temp7(?statementID1,?statementID2) :- Flow(?statementID1,?statementID2),Nop(?statementID8saLTESLsL),?statementID2 = ?statementID8saLTESLsL.
temp8(?statementID1,?statementID2) :- temp6(?statementID1,?statementID2),temp7(?statementID1,?statementID2).
nop(?statementID1,?statementID2) :- temp8(?statementID1,?statementID2).
temp5(?statementID1,?statementID2) :- nop(?statementID1,?statementID2).
temp66(?statementID1,?statementID2) :- nop(?statementID1,'!!!!!!!!!!!!!!!!!!!!!!').
temp9(?statementID1,?statementID2) :- temp2(?statementID1,?statementID2),!temp5(?statementID1,?statementID2).
bad(?statementID1,?statementID2) :- temp9(?statementID1,?statementID2).
temp1(?statementID1,?statementID2) :- bad(?statementID1,?statementID2).
temp10(?textyXP33whGKnZFg84Vw6Xt,?k9eTUFSohj1,?textyXP33whGKnOawedZkCgB) :- Src(?statementID1,?startCharyXP33whGKnXRQyPUIxh7,?numCharsyXP33whGKn0Ros8RqVZd,?textyXP33whGKnZFg84Vw6Xt),Src(?statementID2,?startCharyXP33whGKn6VYjAKHeQD,?numCharsyXP33whGKnIiKHcEi9GP,?textyXP33whGKnOawedZkCgB),temp1(?statementID1,?statementID2),?k9eTUFSohj1 = '<0>'.
"""
  println("Generated rules below\n")
   new RuleUtil(rulesString).getRules foreach println
  val factStr = """A(1,2).A(2,3)."""
  val ruleStr = """C(?x,?y):-A(?x,?y).// comment"""
  //B(3,4).C(?x,?y):-A(?x,?z),B(?z,?y),?w='w'."""
  //val ruleStr = """C(?x, ?y, ?z) :- A(?x, ?x1),B(?x2, ?y),?z='33'."""
  val solver = new trap.datalog.DatalogSolver()
  solver.addFacts("""A(1,2).A(2,3).B(2,1).B(3,2).""")
  solver.addRules("""C(?x,?y,?z) :- A(?x,?any1), B(?any2, ?y), ?z='33'.""")
  solver.addQueries("?-C(?x,?y,?z).")
  solver.addQueries("?-A(?x,?y).")
//  solver.getAnswers foreach println
  
}
