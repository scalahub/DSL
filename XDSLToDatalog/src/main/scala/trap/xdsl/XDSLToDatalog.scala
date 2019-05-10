package trap.xdsl

import trap.Util._
import trap.dsl.FactDataStructures._
import trap.dsl.DSLConfig
import trap.xdsl.XDSLDataStructures._
import trap.xdsl.XMappingDataStructures.Mapping
import trap.xdsl.datalog.RuleDataStructures._

class XDSLToDatalogNew(basisUtil:XDSLBasisUtil) {
  val xdslToDatalogUtil = new XDSLToDatalogUtil(basisUtil)
  import xdslToDatalogUtil._
  def getCreatedRules = createdRules
  
  def getFindQueryUnMapped(m:PatternMatcher) = 
    (getHeadSchemaFromFilters(m.pattern, m.filter), m.pattern.returnedCols)
  
  // returns a set of headSchemas for the given find query, one for each mapping
  // the find query is given inside PatternMatcher (contains ONE find query)
  // the mappings are specified as a Seq[Mapping]
  // if Seq[Mapping] is empty, it implies that no mappings are specified
  // if Seq[Mapping] is non-empty, it implies one or more mapping specified
  def getFindQueriesMapped(m:PatternMatcher, mappings:Seq[Mapping]): 
      Seq[(Option[Mapping], (DLHeadSchema, Seq[String]))] = 
      // last element, Seq[String], returns a list of names to be displayed in the final results
      // they are not used anywhere internally.
      // So it may return Seq("name", "age", "<?>", "name", "age")
      //  The <?> bit is to separate multiple returned keys such as in flow, which needs two pattern keys
      //  src and dest. These two will be separated by <?>
  {
    // add code here for mapping
    val basicHeadSchema = getHeadSchemaFromFilters(m.pattern, m.filter)
    
    if (mappings.isEmpty) Seq(
      // None below specifies that no mapping was used in this computation
      (None, (basicHeadSchema, m.pattern.returnedCols))
    ) else {
      val resolver = new ResultResolverNew(basisUtil, xdslToDatalogUtil)
      mappings.map{mapping =>
        // case class Mapping(name:String, keyMaps:Seq[KeyMap])
        (Some(mapping), resolver.resolve(basicHeadSchema, mapping))
      }
    }
  }
  private def createFactDef(pattern:Pattern):FactDef = {
    val matchers = if (pattern.patternMatchers.isDefined) pattern.patternMatchers.get else throw new XDSLCompilerException("cannot define library fact: "+pattern.name+" (pattern matchers necessary)")
    val paraDefs = pattern.returnedCols.map(col => ParaDef(col, ParaStringType, Some(col), None, false, true))
    val fact = FactDef(pattern.name.tail, paraDefs, true) 
    /* 
     *     before creating FactDef, remove the '#' character from pattern.name
     *     the .tail methods removes this, i.e., converts "#foo" into "foo"
     */
    addFactToMap(pattern, fact)
    addRule(DLRule(DLHeadSchema(fact.factName, pattern.returnedCols.map(DLPrimaryKeyVar(_))), Seq(getHeadSchemaFromMatchers(matchers).toTailSchema), Seq()))
    fact
  }
  private def getHeadSchemaFromMatchers(matchers:OneOrMore[PatternMatcher]):DLHeadSchema = matchers match {
    case Right(joined) => joinHeadSchema(getHeadSchemaFromMatchers(joined.left), joined.joinType, getHeadSchemaFromMatchers(joined.right))
    case Left(matcher) => getHeadSchemaFromFilters(matcher.pattern, matcher.filter)
  }
  private def getFactDef(pattern:Pattern):FactDef = findFactDef(pattern) match {
    case Some(fact) => fact
    case None => createFactDef(pattern)
  }
  def getHeadSchemaFromFilters(pattern:Pattern, filters:ZeroOrMore[Filter]):DLHeadSchema = filters match {
    case Some(Right(joined)) => joinHeadSchema(getHeadSchemaFromFilters(pattern, Some(joined.left)), joined.joinType, getHeadSchemaFromFilters(pattern, Some(joined.right))) //joinedFilter.joinType
    case Some(Left(filter)) => getHeadSchemaFromFilter(pattern, Some(filter))
      /*
       * find #invoke where { @name = 'foo' }
       * find #invoke where { @name = @foo }
       */
    case None =>  getHeadSchemaFromFilter(pattern, None)
  }
  private def getHeadSchemaFromFilter(pattern:Pattern, optionFilter:Option[Filter]):DLHeadSchema = {    
    /*
     * find #invoke where { @name = 'foo' }
     * find #invoke where { @name = @foo }
     */
    val headSchema = DLHeadSchema(getTempSchemaName, pattern.returnedCols.map(DLPrimaryKeyVar(_)))
    val patternFact = getFactDef(pattern)
    if (optionFilter.isDefined) {
      val filter = optionFilter.get
      val (dslAttr, dslOp, dslValue) = (filter.attr, filter.op, filter.value) match { 
        case (k, o, DSLValue(v, DSLAttrType)) if (v.owner == pattern && k.owner != pattern) => (v, o.invert, DSLValue(k, DSLAttrType))
          // if value (of type DSLAttrType) is from current pattern, while attr is from another pattern,
          // then swap attr and value. We prefer the attr to be always of the current pattern if possible        
        case any => any
      }
      val attrOwnerPattern = dslAttr.owner
      val attrOwnerFact = getFactDef(attrOwnerPattern)
      val (attrParam, attrIndex) = getParaDef(attrOwnerFact, dslAttr)
      val dlVar = getDlVar(attrOwnerFact, attrParam, attrIndex, dslAttr)
      var toInsert:Seq[(Int, DLVar)] = Seq((attrIndex, dlVar))
      val (dlValue, tailSchemasToAdd) = dslValue match {
        case DSLValue(i, DSLDecimalType) => (DLDecimalValue(i), Seq[DLTailSchema]())
        case DSLValue(i, DSLBooleanType) => (DLBooleanValue(i), Seq[DLTailSchema]())
        case DSLValue(i, DSLIntType) => (DLIntValue(i), Seq[DLTailSchema]())
        case DSLValue(s, DSLStringType) => (DLStringValue(s), Seq[DLTailSchema]())
        case DSLValue(rhsDSLAttr, DSLAttrType) => 
          /*
                   c          lhs         rhs
                  #foo where #bar.@bar = #baz.@baz
                  temp0(c1) :- foo(c1, ?any1, ?any2), faa(c2, c1), fee(c3, c2), baz(c3, ?any3, ?baz),  
                               foo(c1, ?any4, ?any5), faa(c2, c1), bar(c2, ?bar), ?baz = ?bar. 

                  #foo where #bar.@bar = @baz
                  #foo where #bar.@bar = #bar.@baz
                  #foo where #bar.@bar = #baz.@baz
                  #foo where @bar = #baz.@baz
                  #foo where @bar = @baz
                  #foo where @bar = #baz.@baz
           */
          val (lhsAttrOwnerPattern, rhsAttrOwnerPattern) = (attrOwnerPattern, rhsDSLAttr.owner)
          val rhsAttrOwnerFact = getFactDef(rhsAttrOwnerPattern)
          val (rhsAttrParam, rhsAttrIndex) = getParaDef(rhsAttrOwnerFact, rhsDSLAttr) 
          val rhsDlVar = getDlVar(rhsAttrOwnerFact, rhsAttrParam, rhsAttrIndex, rhsDSLAttr)
          (pattern, lhsAttrOwnerPattern, rhsAttrOwnerPattern) match {
            case (_, lhs, rhs) if lhs == rhs => // lhs attr owner pattern and rhs attr owner pattern are same
              toInsert = toInsert :+ ((rhsAttrIndex, rhsDlVar)) // insert rhs attr var into lhs attr owner pattern
              (rhsDlVar, Seq[DLTailSchema]())
            case any => // rhs attr owner pattern is neither equal to lhs attr owner pattern nor current pattern            
              val rhsTailVars = insertAt(getTailSchemaVars(rhsAttrOwnerFact), rhsAttrIndex, rhsDlVar)
              val chainToRhsAttrOwner = getChainInit(patternFact, rhsAttrOwnerFact) :+ DLTailSchema(rhsAttrOwnerFact.factName, rhsTailVars)
              (rhsDlVar, chainToRhsAttrOwner)
          }
        case DSLValue(rhsPattern, DSLPatternType(key)) => getSchemaForPatternKey(getFactDef(rhsPattern), key)
          /*
            #invoke where @foo = #bar
            #fee where #foo.@foo = #bar
            #inloop where @stmt = #invoke 

            #invoke where #foo.@foo = #bar
            #flow where @src = #invoke and @sink = #assign
            #flow where { @source = #a } and #dataflow

           temp1(?sid) :- inLoop(?stmt), invoke(?sid, _, _), ?stmt = ?sid
           
           temp1(?sid, ?cid) :- inLoop(?stmt), assign(?sid, ?cid, _), ?stmt = ?sid
             
           dataflow(_, ?sink), assign(?sink, _, _)
           */
      }
      val varOpVals = Seq(DLVarOpVal(dlVar, dslOp, dlValue))
      val tailVars = toInsert.foldLeft(getTailSchemaVars(attrOwnerFact))((x, y)=> insertAt(x, y._1, y._2)) 
      val chainToAttrOwner = getChainInit(patternFact, attrOwnerFact) :+ DLTailSchema(attrOwnerFact.factName, tailVars)
      addRule(DLRule(headSchema, chainToAttrOwner ++ tailSchemasToAdd, varOpVals))
    } else addRule(DLRule(headSchema, Seq(DLTailSchema(patternFact.factName, getTailSchemaVars(getFactDef(pattern)))), Seq()))
    headSchema
  }
}
class XDSLToDatalog(dslConfig:DSLConfig, basisUtil:XDSLBasisUtil) extends XDSLToDatalogNew(basisUtil:XDSLBasisUtil){
  val xdslToDatalogNew = new XDSLToDatalogNew(basisUtil)
//  import xdslToDatalogNew._
  @deprecated
  def getFindQuery(m:PatternMatcher, resolve:Boolean) = // resolve = true implies resolve results (i.e., convert sID to Statement, etc)
    if (resolve) new ResultResolver(dslConfig, basisUtil, xdslToDatalogUtil).resolve(getHeadSchemaFromFilters(m.pattern, m.filter))    
    else (getHeadSchemaFromFilters(m.pattern, m.filter), m.pattern.returnedCols)
}
/*  
    Pattern(name:String, patternMatchers:ZeroOrMore[PatternMatcher], returnedCols:Seq[String], traversible:Boolean) {
 
    PatternMatcher(pattern:Pattern, filter:ZeroOrMore[Filter]) 
 */
/*
PatternMatcher(Pattern(#foo,Some(Right(Joined(Left(PatternMatcher(Pattern(#invoke,None,ArrayBuffer(sID),true),
Some(Left(Filter(Key(Pattern(#class,None,ArrayBuffer(cID),true),@name,DSLStringType$),=,DSLValue('foo',DSLStringType$)))))),
And,Left(PatternMatcher(Pattern(#assign,None,ArrayBuffer(sID),true),Some(Left(Filter(Key(Pattern(#class,
None,ArrayBuffer(cID),true),@name,DSLStringType$),=,DSLValue('bar',DSLStringType$))))))))),ArrayBuffer(sID),true),None) 
 */
/* Scratch pad
 * 
 * => find #foo where #invoke.name = 'b'
 * "#temp1" <- [#foo where #invoke.name = 'b']
 * "#temp1" <- [["#foo" <- [(#invoke where @foo='foo') and (#assign where @bar = 'bar)]] where #invoke.name = 'b']
 * "#temp1" <- [["#foo" <- [(["#invoke" <- None] where @foo='foo') and (["#assign" <- None] where @bar = 'bar)]] where #invoke.name = 'b']
                               ----------------        ----------        -----------------        -----------
 * "#temp1" <- [["#foo" <- [(#invoke where @foo='foo') and (#assign where @bar = 'bar)]] where #invoke.name = 'b']
 
 * define #foo as [(#invoke where @foo='foo') and (#assign where @bar = 'bar)]
 * define #invoke as [NONE]
 * define #assign as [NONE]

 * => find #foo where #invoke.name = 'b'
 * define #temp1 as [#foo where #invoke.name = 'b']
 * define #foo as [(#invoke where @foo='foo') and (#assign where @bar = 'bar)]
 * define #invoke as [NONE]
 * define #assign as [NONE]
 
 => "#temp1" <- ( ("#foo" <- ( (#invoke where @foo='foo') and (#assign where @bar = 'bar) ) ) where #bar.name = 'b')
 
 */
/* 
    PatternMatcher(
     |PATTERN(#c,
     | |SingleMatcher(
     | | |PatternMatcher(
     | | | |PATTERN(#a,
     | | | | |MultipleMatchers(
     | | | | | |Joined(
     | | | | | | |SingleMatcher(
     | | | | | | | |PatternMatcher(
     | | | | | | | | |PATTERN(#invoke, 
     | | | | | | | | | |NoMatcher,
     | | | | | | | | | |returns(sID),
     | | | | | | | | | |traversible:true
     | | | | | | | | |),
     | | | | | | | | |NoFilter
     | | | | | | | |)
     | | | | | | |),
     | | | | | | |And,
     | | | | | | |SingleMatcher(
     | | | | | | | |PatternMatcher(
     | | | | | | | | |PATTERN(#assign,
     | | | | | | | | | |NoMatcher,
     | | | | | | | | | |returns(sID),
     | | | | | | | | | |traversible:true
     | | | | | | | | |),
     | | | | | | | | |NoFilter
     | | | | | | | |)
     | | | | | | |)
     | | | | | |)
     | | | | |)
     | | | | ,
     | | | | |returns(sID),
     | | | | |traversible:true
     | | | |),
     | | | |Filter(
     | | | | |SingleFilter(
     | | | | | |Filter(
     | | | | | | |Key(
     | | | | | | | |PATTERN(#class,
     | | | | | | | | |NoMatcher,
     | | | | | | | | |returns(cID),
     | | | | | | | | |traversible:true),
     | | | | | | | |name: @name,
     | | | | | | | |type: String
     | | | | | | |),
     | | | | | | |EQ,
     | | | | | | |DSLValue('foo',String)
     | | | | | |)
     | | | | |)
     | | | |)
     | | |)
     | |),
     | |returns(sID),
     | |traversible:true
     |),
     |NoFilter
    )

   * Pattern(name:String, matchers:ZeroOrMore[PatternMatcher], returnedCol:Seq[String], traversible:Boolean)
   *         val availableKeys:Seq[Key]
   * 
   *   PatternMatcher(pattern:Pattern, filters:ZeroOrMore[Filter])
   * 
   *     Filter(key:Key, op:Op, value:DSLValue[_])
   * 
   *       Key(owner:Pattern, name:String, keyType:PrimitiveType[_])
   * 
   *         DSLValue[V](value:V, valueType:Type[V]) // val int = DSLValue(1, IntType)
   * 
   */  
/*
     PatternMatcher
      |PATTERN[#c]
      | |SingleMatcher
      | | |PATTERN[#a]
      | | | |JoinedMatchers
      | | | | |SingleMatcher
      | | | | | |PatternMatcher
      | | | | | | |PATTERN[#invoke] 
      | | | | | | | |NoMatcher
      | | | | | | | |returns(sID)
      | | | | | | | |traversible:true
      | | | | | | |NoFilter
      | | | | |And
      | | | | |SingleMatcher
      | | | | | |PatternMatcher
      | | | | | | |PATTERN[#assign]
      | | | | | | | |NoMatcher
      | | | | | | | |returns(sID)
      | | | | | | | |traversible:true
      | | | | | | |NoFilter
      | | | |returns(sID)
      | | | |traversible:true
      | | |SingleFilter
      | | | |Key
      | | | | |PATTERN[#class]
      | | | | | |NoMatcher
      | | | | | |returns(cID)
      | | | | | |traversible:true
      | | | | |name: @name
      | | | | |type: String
      | | | |EQ
      | | | |DSLValue('foo',String)
      | |returns(sID)
      | |traversible:true
      |NoFilter
 */
