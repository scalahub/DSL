package trap.dsl

import FactDataStructures._

object FactDefUtil { 
  /* creates a fact definition from a string such as:
     "myfact(foo:String[SomeKey], bar:Int, baz:String[])" 
     into a FaceDef 
   */
  def createFactDef(signature:String):FactDef = {
    /*    This method convertes a string of type: 
        "myfact(foo:String, bar:Int)"
     To 
          FactDef("myfact", Array(ParaDef("foo", StringType, None, true), ParaDef("bar", IntType, None, true))

        where the above types are defined in FactDataStructures
        
     The full signature is 
       "myfact(foo:String[SomeKey], bar:Int)"
       
     where the optional "[SomeKey]" part indicates a primary key to be used in datalog for linking different tables
     the primary key must be the same for two columns to be linked for instance.
       "myfact1(foo:String[SomeKey], bar:Int)"
       "myfact2!(foo:String, bar:String[SomeKey], baz:Int)"
       
     If the fact name starts with a cap letter, then it is a hidden fact. So we check that too.
     
     Columns corresponding to a primary key will not be visible for datalog users. Additionally, a column with empty 
     SomeKey value will also not be visible, as in:
       "myfact(foo:String[SomeKey], bar:Int, baz:String[])"

     In the above baz will not be visible
     
     The exclamation indicates that entire fact is invisible to user
     
   */  
    val s = signature.replace(" ", "").replace("\t","")
    val split = s.split("\\(")
    val (factName, visibility) = if (split(0).last == '!') (split(0).init, false) else (split(0), true)
    val paraSig = split(1).split("\\)")(0) // "invokeID:String[4],statementID:String[3],objClass:String,methodName:String,numParams:Int"
    val paras = paraSig.split(",").map(_.split(":")).map(x => getParaDef(x(0), x(1)))
    // val isInvisible = visibility && paras.exists(_.visible) // factName.toUpperCase.head == factName.head // invisible fact if first char upper case
    val isInvisible = visibility  // factName.toUpperCase.head == factName.head // invisible fact if first char upper case
    FactDef(factName, paras, isInvisible)
  }
  private def getParaDef(n:String, s:String) = {
    /* s is a string of type "String[1]" or "String[]" or "String" or "String{2}, etc
     */
    val (paramType, colKey, patKey, visible, returned) = getParaDefAttributes(s)
    types.find(_.text == paramType) match {
      case Some(ParaBooleanType) if colKey.isDefined || patKey.isDefined => 
        throw new DSLConfigException("Boolean type cannot be pattern or primary key: "+n)
      case Some(pType) => 
        ParaDef(n, pType, colKey, patKey, visible, returned)
      case any => throw new DSLConfigException("type not found: "+paramType)
    }
  }
  private def getParaDefAttributes(s:String) = { 
    /*
     * takes a string of type "String" or "String[4]" or "String{3}" and outputs 
     * ("String", None, None, true) or ("String", Some(4), None, false) or ("String", None, Some(4), true)
     */
    
    val (isColKey, startStr, endStr) = if (s.contains("{")) (false, "\\{", "\\}") else (true, "\\[", "\\]")
    val s1 = s.split(startStr)
    val paramType = s1(0)
    
    val (colKey, patKey, visible, returned) = if (s1.size == 1) (None, None, true, false) else {
      val s2 = s1(1).split(endStr)
      if (s2.size == 0) (None, None, false, false) else {
        val (keyName, keyReturned) = if (s2(0).endsWith("!")) (s2(0).init, false) else (s2(0), true)
        if (keyName.matches("^.*\\d$")) throw DSLConfigException("primary key must not end with a digit: "+keyName)
        // above regex matches if keyName ends with a number (this is disallowed because we use such strings for internal purpose)
        // see http://stackoverflow.com/a/7690348/243233
        val key = Some(keyName)
        if (isColKey) (key, None, false, keyReturned) else (None, key, true, keyReturned) 
      }
    }
    (paramType, colKey, patKey, visible, returned)
  }
}

