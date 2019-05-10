package trap.xdsl

//import trap.xdsl.XMappingDataStructures.Count
//import trap.xdsl.XMappingDataStructures.KeyMap
//import trap.xdsl.XMappingDataStructures.Mapping
//import trap.xdsl.XMappingDataStructures.PatternAttrAggr
import trap.xdsl.datalog.RuleDataStructures._
import trap.dsl.DSLConfig
import trap.Util._
import scala.util.Random._
import trap.xdsl.XDSLDataStructures.Attr
import trap.xdsl.XDSLDataStructures.EQ

import trap.xdsl._
import trap.xdsl.XDSLDataStructures.Pattern
import trap.xdsl.XMappingDataStructures._


class ResultResolverNew(basisUtil:XDSLBasisUtil, xdslToDatalogUtil:XDSLToDatalogUtil) {
  /* get result start */
  import xdslToDatalogUtil._
  def resolve(head:DLHeadSchema, mapping:Mapping) = {
    // suppose find result rule was "foo(a1, a2) :- bar(a1, c), baz(a2, c).""
    // head is "foo(a1, a2)"
    // head.vars are "a1", "a2""
    // mapping is "a -> something", "a -> somethingElse"
    //    println("---- "+mapping.getCode)
    //    head.vars foreach println
    //    println("****")    
    if (head.vars.size != mapping.keyMaps.size) 
      throw new Exception(s"num returned keys in ${head.name}(head.vars.size) != num keys in mapping ${mapping.name}(mapping.keyMaps.size)")

    val headVarsKeyMaps = (head.vars zip mapping.keyMaps)
    headVarsKeyMaps foreach{
      case (DLPrimaryKeyVar(keyID), KeyMap(keyMapKeyID, _)) =>
        if (!keyID.startsWith(keyMapKeyID)) throw new Exception("key mismatch in result($keyID) and $keyMapKeyID")
    }

    val (heads, tails, names) = headVarsKeyMaps.collect{
      // keyID is "a1", 
      // keyMap is "a => something", 
      // keyMapKeyID is "a"
      case (DLPrimaryKeyVar(keyID), KeyMap(_, Right(PatternAttrAggr(pattern, None)))) => 
          // implies         a => #Users
        getPrimaryKeySchemaMapped(keyID, pattern, false, pattern.availableAttrs)
      case (DLPrimaryKeyVar(keyID), KeyMap(_, Right(PatternAttrAggr(pattern, Some(Right(attrs)))))) => 
          // implies         a => #Users.@age,@sal       or    
          //                 a => #Users.@age.max,@sal.avg        
        getPrimaryKeySchemaMapped(keyID, pattern, false, attrs.map(_.attr)) // ignore aggr for now. First compute attr
      case (DLPrimaryKeyVar(keyID), KeyMap(_, Right(PatternAttrAggr(pattern, Some(Left(Count)))))) => 
          // a => #Users.count
          // Seq[DLPrimaryKeyVar], TailSchema, names_of_non-PriKeyAttrs
        getPrimaryKeySchemaMapped(keyID, pattern, true, Nil) // ignore aggr for now. First compute attr
      case (priKeyVar@DLPrimaryKeyVar(keyID), KeyMap(_, Left(Count))) => 
          // a => count    
        (Seq(priKeyVar), head.toTailSchema, Nil)
//      case (priKeyVar@DLPrimaryKeyVar(keyID), KeyMap(_, Left(Ignore))) => 
//        (Seq(priKeyVar), null, Nil)
    }.unzip3

    var joinVars:List[String] = Nil
    var joinVarCtr = 0
    val joinStr = getHash(nextInt.toString) // "join"
    def getJoinVar = {joinVarCtr += 1; val joinVar = joinStr+joinVarCtr; joinVars :+= joinVar; joinVar}
    val isSingleSchema = heads.size == 1
    val headSchema = DLHeadSchema(
      getTempSchemaName, 
      if (isSingleSchema) heads.headOption.get else heads.reduceLeft((left, right) =>( left :+ DLPrimaryKeyVar(getJoinVar)) ++ right)
    ) //else schemas.flatMap(_._1))
    val tailSchemas = tails :+ head.toTailSchema
    val resultRule = DLRule(
      headSchema, 
      tailSchemas, 
      if (isSingleSchema) Nil else joinVars.zipWithIndex map{
        case (joinVar, index) => DLVarOpVal(DLAttrVar(joinVar), EQ, DLConstValue("'<"+index+">'"))
      }
    )
    addRule(resultRule)
    val headSchemaDef = 
      if (isSingleSchema) names.headOption.get else names.reduceLeft((left, right) =>( left :+ "<?>") ++ right)
    (headSchema, headSchemaDef)
  }
  private def getPrimaryKeySchemaMapped(keyID:String, pattern:Pattern, includeKey:Boolean, includeOnly:Seq[Attr]) = {     
    // includeKey implies include the primary key in newHeadVars
    // includeOnly implies include only those attributes in Seq. Empty Seq implies include all attrs
    // 
    // val fact = basisUtil.basis.find(_.factName == "#Users").get
    // val fact = <fact that corresponds to "#Users" from basis>
    // 
    val separatorStr = getHash("")
    //    println("-------->")
    //    includeOnly foreach println
    //    println("--------<")    

    val fact = basisUtil.basis.find(_.factName == pattern.name.tail).get // tail converts "#Users" to "Users"    
    val factVars = fact.getKeyVars // valid! (idFact is in basis. No pattern key is skipped)
    val factKeys = factVars.filter(_.isDefined).map(_.get)
    if (factKeys.size == 1 && keyID.startsWith(factKeys.headOption.get)) {
      val vars = factVars zip(fact.paramDefs) map{
        case (Some(_), _) => DLPrimaryKeyVar(keyID) // only one will be matched (due to size == 1 above)
        case (_, paraDef) => DLPrimaryKeyVar(paraDef.paraName++separatorStr+getHash(nextInt.toString))
      }
      // "vars" has the primary keys of new head relation
      val names = fact.paramDefs.filter(_.primaryKey.isEmpty).map(_.paraName)
      // "names" are those parameter names that are NOT primary keys      
      
      val includeOnlyVarNames = includeOnly.map(_.name.tail)
      val returnedAttrVars = includeOnlyVarNames.map{varName => 
        vars.find{someVar =>
          someVar.name.startsWith(varName+separatorStr)
        }.get                
      }
      val returnedVars = vars.collect{
        case someVar if someVar.name == keyID && includeKey => someVar
      } ++ returnedAttrVars
      
      val tail = DLTailSchema(fact.factName, vars)
      //      println("--------> ")
      //      returnedVars foreach println
      //      println("--------< "+tail)    
      
      (returnedVars, tail, names)
    } else throw new XDSLCompilerException("invalid id->Fact mapping: "+keyID+"->"+fact.factName)
  }
}

@deprecated
class ResultResolver(dslConfig:DSLConfig, basisUtil:XDSLBasisUtil, xdslToDatalogUtil:XDSLToDatalogUtil) {
  /* get result start */
  import xdslToDatalogUtil._
  private var resultMap = dslConfig.resultMap
  def resolve(head:DLHeadSchema)={
    val schemas = head.vars.map(x => getPrimaryKeySchema(x.name))
    var joinVars:List[String] = Nil
    var joinVarCtr = 0
    val joinStr = getHash(nextInt.toString) // "join"
    def getJoinVar = {joinVarCtr += 1; val joinVar = joinStr+joinVarCtr; joinVars :+= joinVar; joinVar}
    val singleSchema = schemas.size == 1
    val headSchema = DLHeadSchema(getTempSchemaName, 
                                  if (singleSchema) schemas(0)._1 
                                  else schemas.map(_._1).reduceLeft((x, y) =>( x :+ DLPrimaryKeyVar(getJoinVar)) ++ y)) //else schemas.flatMap(_._1))
    val tailSchemas = schemas.map(_._2) :+ head.toTailSchema
    val resultRule = DLRule(headSchema, tailSchemas, 
                            if (singleSchema) Seq() 
                            else joinVars.zipWithIndex map (x => DLVarOpVal(DLAttrVar(x._1), EQ, DLConstValue("'<"+x._2+">'"))))
    addRule(resultRule)
    val headSchemaDef = if (singleSchema) schemas(0)._3 
                        else schemas.map(_._3).reduceLeft((x, y) =>( x :+ "<?>") ++ y)
    (headSchema, headSchemaDef)
  }
  private def getMapKey(keyID:String) = {
    resultMap.toArray.filter(key => keyID.startsWith(key._1)) match {
      case map if map.size == 1 => map(0)._1
      case _ => throw new XDSLCompilerException("ambiguous keyID (matches more than one): "+keyID)
    }
  }
  private def getPrimaryKeySchema(keyID:String) = {
    val idFact = basisUtil.basis.find(_.factName == resultMap.get(getMapKey(keyID)).get).get
    val keyVars = idFact.getKeyVars // valid! (idFact is in basis. No pattern key is skipped)
    val priKeys = keyVars.filter(_.isDefined).map(_.get)
    val vars = if (priKeys.size == 1 && keyID.startsWith(priKeys(0))) {
      keyVars zip(idFact.paramDefs) map(x => 
        if (x._1.isDefined) keyID else x._2.paraName+getHash(nextInt.toString)) map(DLPrimaryKeyVar(_))
    } else throw new XDSLCompilerException("invalid id->Fact mapping: "+keyID+"->"+idFact.factName)
    val names = idFact.paramDefs.filter(_.primaryKey.isEmpty).map(_.paraName)
    (vars.filter(_.name != keyID), DLTailSchema(idFact.factName, vars), names)
  }
  /* get result end */
}


