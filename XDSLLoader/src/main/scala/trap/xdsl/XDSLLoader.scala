package trap.xdsl

import trap.xdsl.XDSLDataStructures._ 
import java.io.File
import scala.xml.Node
import trap.xdsl.XMappingDataStructures.Mapping
import trap.xml.SafeLoader

class XDSLLoaderException(s:String) extends Exception(s)

class XDSLLoader(dslXML:Node, visible:Array[Pattern], mappings:Seq[Mapping]) {
  /*
  <dsl ver="0.4" file="input\dsl.txt">
    <patterns>
      <ID name="TestPatterns1"/>
      <def>
        <PATTERN name="#sink2c">
          <PATTERN name="#invoke">
            <ATTR name="@name">
              <EQ name="=">
                <PATTERN name="#assign"/>
              </EQ>
            </ATTR>
          </PATTERN>
        </PATTERN>
      </def>
    </patterns>
  </dsl>
  */  
  def this(dslXML:Node, visible:Array[Pattern]) = this(dslXML:Node, visible:Array[Pattern], Nil)
  def this(dslFile:String, s:Array[Pattern]) = this (SafeLoader.loadFile(dslFile), s)  
  def this(dslFile:String, s:Array[Pattern], mappings:Seq[Mapping]) = this (SafeLoader.loadFile(dslFile), s, mappings)  
  /* // other constructors
   * def this(dslFile:String, s:Array[String], t:Array[String]) = this (dslFile, s map (getLibPattern), t map (getLibPattern))
   * def this(dslXML:Node, s:Array[String], t:Array[String]) = this (dslXML, s map (getLibPattern), t map (getLibPattern))
   */
  private def trim(x:Node) = scala.xml.Utility.trim(x)
  private val dsl = trim(dslXML)
  var visiblePatterns:Seq[Pattern] = visible
  val srcFileFull = (dsl \ "@file").text 
//  val i = srcFileFull.lastIndexOf("\\")
  private val srcFile = new File(srcFileFull).getName
//  private val srcFile = "in file:"+(if (i < 0) srcFileFull else srcFileFull.drop(i+1))
  def srcLoc(x:Seq[Node]) = "["+srcFile+":"+(x(0) \ "@line").text+"]" 
  private def addVisiblePattern(p:Pattern) = visiblePatterns = visiblePatterns :+ p
  def loadDefinedPatterns = (dsl \ "patterns" \ "def") map getPattern
  /*
      <def>
        <PATTERN name="#sink2c">
          <PATTERN name="#invoke">
            <ATTR name="@name">
              <EQ name="=">
                <PATTERN name="#assign"/>
              </EQ>
            </ATTR>
          </PATTERN>
        </PATTERN>
      </def>
  */
  private def getFindPatternMatcher(n:Node) = {
    val name = (n \ "@name").text // "#sink3"
    visiblePatterns.find(_.name == name) match { 
      case Some(p) => PatternMatcher(p, getFilterBody(n(0), p))
      case _ => throw new XDSLLoaderException("no matching pattern found: "+name+" "+srcLoc(n))
    }
  }
  private def getFindPatternMapper(n:Node) = (n \ "@name").text // do not remove ':' from init of string
  
  //  def getFindPatternMappersOld = { // single mapping for a find
  //    dsl \ "patterns" \ "find" map (x => (x \ "MAPPING").headOption)    
  //  }.map(_.map(getFindPatternMapper))
  
  def getFindPatternMappers = { // multiple mapping for find
    (dsl \ "patterns" \ "find").map(x => (x \ "MAPPING").map(getFindPatternMapper))    
  }
  @deprecated("15 Jan 2017", "does not return mappings. Use getFindPatterns") 
  def getFindPatternMatchers = dsl \ "patterns" \ "find" map (x => getFindPatternMatcher((x \ "PATTERN")(0)))
  
  def getFindPatterns = {
    // val findPatterns = dsl \ "patterns" \ "find" \ "PATTERN" 
    /* dsl =
          <dsl>
            <patterns>
               <find>
                 <PATTERN name="#sink3">
                   <ATTR name="@name">
                     <EQ name="=">
                       <STRING name="'foo'"/>
                     </EQ>
                   </ATTR>
                 </PATTERN>
                 <MAPPING line="6" name=":a"/>
               </find>
             </patterns>
          </dsl>

        pattern XML = 
                <PATTERN name="#sink3">
                  <ATTR name="@name">
                    <EQ name="=">
                      <STRING name="'foo'"/>
                    </EQ>
                  </ATTR>
                </PATTERN>
        or 
                <PATTERN name="#sink3">
                </PATTERN>
        or 
        
                <PATTERN name="#sink3">
                </PATTERN>
                <MAPPING line="6" name=":a"/>

    */
    
    dsl \ "patterns" \ "find" map {x =>
      val findMappings = (x \ "MAPPING").map(getFindPatternMapper)      
      val findPattern = getFindPatternMatcher((x \ "PATTERN")(0))      
      // check if mappings are compatible with find      
      findMappings.foreach{findMapping =>
        mappings.find(_.name == findMapping) match { // mappings DON'T have '$' prepended to name, unlike other items
          case Some(mapping) => 
            val findReturnedKeys = findPattern.pattern.returnedCols
            val mappingKeys = mapping.keyMaps.map(_.keyID)
            findReturnedKeys zip mappingKeys foreach{
              case (frk, mk) if frk.startsWith(mk) => // name matches ... all ok; do nothing
              //case (frk, mk) if frk == mk => // name matches ... all ok; do nothing
              case (frk, mk) => 
                throw new XDSLLoaderException(
                  s"key name mismatch: $mk$findMapping != $frk:$findPattern ${srcLoc(x)}"
                )
            }
            // check validity of keys
          case _ => throw new XDSLLoaderException(s"no such mapping $findMapping ${srcLoc(x)}")
        }
      }      
      (findPattern, findMappings)
    }
  }
  private def getOp(n:Node):Op = getOpFromString(n \ "@name" text) 
    /*          n = 
                <EQ name="=">
                  <PATTERN name="#assign"/>
                </EQ>
    */    
   
  private def getValue(n:Node, parent:Pattern) = {
    /*            n = 
                  <PATTERN name="#assign"/>
                  or 
                  <ATTR name="@foo" line="34"/>
                  or 
                  <STRING name="'b'"/>
                  or
                  <INT name="2"/>
    */    
    val name = (n \ "@name").text // "#assign"
    n match {
      case <ATTR></ATTR> => DSLValue(getAttr(parent, (n \ "@name").text, srcFile+":"+(n \ "@line").text), DSLAttrType)
        /*            n = 
                      <ATTR name="@foo" line="34"/>
                      name can be "@foo" or "#bar.@foo"
        */            
      case <DECIMAL></DECIMAL> => DSLValue(BigDecimal(name), DSLDecimalType)
      case <BOOLEAN></BOOLEAN> => DSLValue(name.toBoolean, DSLBooleanType)
      case <STRING></STRING> => DSLValue(name, DSLStringType)
        /*            n = 
                      <STRING name="'b'"/>
        */            
      case <INT></INT> =>  DSLValue(name.toInt, DSLIntType)
      case <PATTERN></PATTERN> => 
        /*            n = 
                      <PATTERN name="#assign" line="58"/>
        */            
        visiblePatterns.find(_.name == name) match {
          case Some(pattern) if pattern.returnedCols.size == 1 => DSLValue(pattern, DSLPatternType(pattern.returnedCols(0)))
          case Some(pattern) => throw new XDSLLoaderException("a pattern key cannot be mapped to multiple primary keys: "+name+" "+srcLoc(n))
          case _ => throw new XDSLLoaderException("no matching pattern found: "+name+" "+srcLoc(n))
        }
    }
  }
  def getAttr(parent:Pattern, name:String, errorLocation:String) = { // name = "@foo" or "#bar.@foo"
    val (owner, attrName) = if (name.contains(".")) { 
      val split = name.split('.')
      val ownerName = split(0)
      if (ownerName == parent.name) throw new XDSLLoaderException("Attr owner and current pattern must not be same: "+ownerName+" "+errorLocation)
      val attrName = split(1)
      if (! parent.traversible) throw new XDSLLoaderException("pattern not traversible: "+parent.name+" "+errorLocation)
      visiblePatterns.find(_.name == ownerName) match {
        case Some(p) if p.traversible => (p, attrName)
        case None => throw new XDSLLoaderException("No traversible pattern found: "+ownerName+" "+errorLocation)
      }
    } else (parent, name)
    
    owner.getAttr(attrName) match {
      case Some(attr) => attr
      case _ => throw new XDSLLoaderException("No such attribute: "+attrName+" "+errorLocation)
    }
  }
  private def getFilterBody(n:Node, parent:Pattern):ZeroOrMore[Filter] = n match {
        /*  n= non-empty pattern
              <PATTERN name="#invoke">
                <ATTR name="@name">
                  <EQ name="=">
                    <PATTERN name="#assign"/>
                  </EQ>
                </ATTR>
              </PATTERN>
            or empty pattern
              <PATTERN name="#invoke">
              </PATTERN>        
        */
      case <PATTERN></PATTERN> => None
        /*
          <PATTERN name="#invoke">
          </PATTERN>
        */
      case <PATTERN>{x @ _*}</PATTERN>  if x.size > 0 => Some(getFilters(x(0), parent)) 
        /*
            x(0) =
            <ATTR name="@name">
              <EQ name="=">
                <PATTERN name="#assign"/>
              </EQ>
            </ATTR>
        */
  }
  private def getFilters(n:Node, parent:Pattern):OneOrMore[Filter] = n match {
      /*   n = 
                <ATTR name="@name">
                  <EQ name="=">
                    <STRING name="'foo'"/>
                  </EQ>
                </ATTR>

           or

                <ATTR name="@name">
                  <EQ name="=">
                    <PATTERN name="#assign"/>
                  </EQ>
                </ATTR>
      */    
    case <and>{x @ _*}</and> => Right(Joined(getFilters(x(0), parent), And , getFilters(x(1), parent)))
    case <or>{x @ _*}</or> => Right(Joined(getFilters(x(0), parent), Or , getFilters(x(1), parent)))
    case <ATTR>{x @ _*}</ATTR> => 
      /*        n = 
                <ATTR name="@foo">
                  <EQ name="=">
                    <PATTERN name="#assign"/>
                  </EQ>
                </ATTR>

                  x = 
                  <EQ name="=">
                    <PATTERN name="#assign"/>
                  </EQ>
      */    
      val name = (n \ "@name").text   // name can be "@foo" or "#bar.@foo"
      val attr = getAttr(parent, name, srcFile+":"+(n \ "@line").text)
      val op = getOp(x(0))
      val value = getValue((x(0) \ "_")(0), parent)
      try { Left(Filter(attr, op, value)) } catch {
        case e:XDSLException => throw new XDSLLoaderException(e.getMessage+" "+srcLoc(x))
      }
  }
  private def getJoinedPatternMatcher(n:Node):(OneOrMore[PatternMatcher], Seq[String], Boolean) = {
    val (x, join) = n match { 
      case <and>{x @ _*}</and> => (x, And)
      case <or>{x @ _*}</or> =>(x, Or)
      case <not>{x @ _*}</not> => (x, Not)
      case <xor>{x @ _*}</xor> => (x, Xor)
    }
    val (left, right) = (getPatternMatchers(x(0)), getPatternMatchers(x(1)))
    val (leftPattern, rightPattern) = (left._1, right._1)
    val (leftKeys, rightKeys) = (left._2, right._2)
    val (leftTraversible, rightTraversible) = (left._3, right._3)
    if (leftKeys != rightKeys) throw new XDSLLoaderException("left and right key mismatch (left["+leftKeys.reduce(_+","+_)+"], right["+rightKeys.reduce(_+","+_)+"]): Join["+join+"] "+srcLoc(n))
    if (leftTraversible != rightTraversible) throw new XDSLLoaderException("left and right transversible mismatch (left["+leftTraversible+"], right["+rightTraversible+"]): Join["+join+"] "+srcLoc(n))
    (Right(Joined(leftPattern, join , rightPattern)), leftKeys, leftTraversible)
    /*
            <and>
              <PATTERN name="#invoke"/>
              <PATTERN name="#assign">
                <ATTR name="@name">
                  <EQ name="=">
                    <STRING name="'b'"/>
                  </EQ>
                </ATTR>
              </PATTERN>
            </and>
    */
  }
  private def getPatternMatchers(n:Node):(OneOrMore[PatternMatcher], Seq[String], Boolean) = { // params: patternmatchers, returnedCols, traversible
    n match {
      /*  n= non-empty pattern
              <PATTERN name="#invoke">
                <ATTR name="@name">
                  <EQ name="=">
                    <PATTERN name="#assign"/>
                  </EQ>
                </ATTR>
              </PATTERN>
          or empty pattern
              <PATTERN name="#invoke">
              </PATTERN>        
      */
      case <and>{_*}</and> | <or>{_*}</or> | <not>{_*}</not> | <xor>{_*}</xor> => getJoinedPatternMatcher(n)
      case any => 
        val name = (n \ "@name").text // eg. "#invoke"
        visiblePatterns.find(_.name == name) match {
          case Some(pattern) => (Left(PatternMatcher(pattern, getFilterBody(n, pattern))), pattern.returnedCols, pattern.traversible)
          case _ => throw new XDSLLoaderException("no matching pattern found: "+name+" "+srcLoc(n))
        }
    }
  }
  private def getPatternMatcherBody(n:Node):(ZeroOrMore[PatternMatcher], Seq[String], Boolean) = n match{
    // Seq[String] represents the primary keys for the Pattern (contained within PatterMatcher), Boolean represents if the pattern is traversible
    /*    // dsl Definition 
          <PATTERN=name="#foo">
            <PATTERN name="#invoke">
              <ATTR name="@name">
                <EQ name="=">
                  <PATTERN name="#assign"/>
                </EQ>
              </ATTR>
            </PATTERN>
          </PATTERN>
      or 
          <PATTERN=name="#foo">
            <PATTERN name="#invoke">
            </PATTERN>        
          </PATTERN>
      or  // predefined/library patterns (no pattern matcher body)
          <PATTERN=name="#foo">
          </PATTERN>
    */
    case <PATTERN></PATTERN> => // empty pattern // library pattern  
      // should not be allowed. Throw exception. If problems, uncomment below and debug why it happened
      throw new XDSLLoaderException("empty pattern not allowed here: "+srcLoc(n))
      // (None, Seq()) 
    case <PATTERN>{x @ _*}</PATTERN> if x.size > 0 => 
      val (patternMatchers, returnedKeys, transversible) = getPatternMatchers(x(0))
      (Some(patternMatchers), returnedKeys, transversible) // user defined pattern
  }    
  private def getPattern(d:Node):Pattern = {
    /*  d=
        <def>
          <PATTERN name="#sink2c" line="33">
            <PATTERN name="#invoke">
              <ATTR name="@name">
                <EQ name="=">
                  <PATTERN name="#assign"/>
                </EQ>
              </ATTR>
            </PATTERN>
          </PATTERN>
        </def>
    */
    val name = (d \ "PATTERN" \ "@name").text // "#sink2c"
    visiblePatterns.find(_.name == name) match {
      case Some(p) => throw new XDSLLoaderException("pattern already exists: "+name+" "+srcLoc(d \ "PATTERN"))
      case None => 
        val (patternMatcherBody, returnedKeys, transversible) = getPatternMatcherBody((d \ "PATTERN")(0))
        val newPattern = Pattern(name, patternMatcherBody, returnedKeys, transversible)
        
        addVisiblePattern(newPattern)
        newPattern
    }
  }
}

  