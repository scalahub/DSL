
package trap.xdsl

import java.io.File
import scala.xml._
import trap.xdsl.XDSLDataStructures._
import trap.xdsl.XMappingDataStructures._
import trap.xml.SafeLoader

/*
 * 5 types of objects
 *  mapping name starts with ':' (example :a)
 *  key name starts with '$' (example $userID)
 *  pattern name starts with '#' (example #users)
 *  attribute name starts with '@' (example @age)
 *  aggregate start with nothing (example max)
 *  
 *  The below lines give examples  
 * 
map :a as $userID => #users;
map :b as $userID => #users;
map :c as $userID => #users.@age.max, $userID2 => #user.@age.count;
map :g as $userID2 => #users.@age.count;
map :ga as $userID2 => #user.@type.count;
map :g2 as $userID2 => #user.count;
map :g3 as $userID2 => count;
map :h as $userID2 => #user.count;
 *
 * Internally when we load the mappings, we keep the prefix for all EXCEPT the key name (so "$userID" will be stored as "userID" internally.
 * This is to ensure compatibality with old code, where we keep all the prefixes except for the key name (referred to as "returned Cols" in old code)
 *
 */
case class XMappingLoaderException(s:String) extends Exception(s)

class XMappingLoader(mappingXML:Node, visible:Seq[Pattern]) {  
  def this(mappingFile:String, s:Array[Pattern]) = this (SafeLoader.loadFile(mappingFile), s)  
  private val mapping = xml.Utility.trim(mappingXML)
  
  private type LineNo = String
  private type MappingName = String
  val mappingsID = (mappingXML \ "ID" \ "@name").text
  private var loadedMappingNames = Set[(MappingName, LineNo)]()
  private val srcFileFull = (mappingXML \ "@file").text 
  private val srcFile = new File(srcFileFull).getName
  private def srcLoc(x:Seq[Node]) = "["+srcFile+":"+(x(0) \ "@line").text+"]" 
  val loadedMappings = loadDefinedMappings
  private def loadDefinedMappings = (mapping \\ "MAP").map{x =>
    val mapping = getMapping(x)
    val lineNo = (x \ "@line").text
    
    loadedMappingNames.find{case (name, lineNo) => name == mapping.name} match {
      case Some((name, oldLineNo)) => // already loaded earlier
        loadedMappingNames.foreach(println)
        throw XMappingLoaderException(s"""Mapping $name already defined at line: $oldLineNo. Names must be unique. ${srcLoc(x)}""")
      case _ => // new definition; not loaded earlier
        loadedMappingNames = loadedMappingNames + ((mapping.name, lineNo))
        mapping        
    }
  }
  
  private def getMapping(d:Node):Mapping = try {
    //  <MAP line="3" name=":a"><KEY line="3" name="$userID"><PATTERN line="3" name="#users"/></KEY></MAP>
    //  <MAP line="3" name=":a"><KEY line="3" name="$userID"><COUNT line="3" name="count"/></KEY></MAP>
    //  <MAP line="8" name=":b"><KEY line="8" name="$userID"><PATTERN line="8" name="#users"/></KEY><KEY line="8" name="$userID2"><PATTERN line="8" name="#user"><ATTR line="8" name="@age"/></PATTERN></KEY></MAP>
    //  <MAP line="12" name=":c"><KEY line="13" name="$userID"><PATTERN line="13" name="#users"><ATTR line="13" name="@age"><AGGR line="13" name="max"/></ATTR></PATTERN></KEY><KEY line="14" name="$userID2"><PATTERN line="14" name="#user"><ATTR line="14" name="@age"><AGGR line="14" name="count"/></ATTR></PATTERN></KEY></MAP>

    Mapping(((d \ "@name").text /* do not remove ':' */), getKeyMaps(d))
    
  } catch {
    case a:Any => 
      a.printStackTrace
      throw XMappingLoaderException(s"""${a.getMessage} ${srcLoc(d)}""")
  }
  private def getAggr(d:Node, parent:Attr):Aggr = {
    // <AGGR line="13" name="max"/>
    Aggr(AggrType.getAggrType(d \ "@name" text), parent)
  }
  private def getAttr(d:Node, parent:Pattern):AttrAggr = {
    // ATTR line="8" name="@age"/>
    // <ATTR line="13" name="@age"><AGGR line="13" name="max"/></ATTR>
    val name = (d \ "@name").text // do not remove '@'
    parent.availableAttrs.find(_.name == name) match {
      case Some(attr) => AttrAggr(attr, ((d \ "AGGR") ++ (d \ "COUNT")).map(getAggr(_, attr)).headOption)
      case _ => throw XMappingLoaderException(s"""No visible attribute $parent.$name ${srcLoc(d)}""")
    }
  }
  private def getPattAttrAggr(d:Node, keyName:String):PatternAttrAggr = {
    // <PATTERN line="3" name="#users"/>
    // <PATTERN line="8" name="#user"><COUNT line="8" name="count"/></PATTERN>
    // <PATTERN line="8" name="#user"><ATTR line="8" name="@age"/></PATTERN>
    // <PATTERN line="13" name="#users"><ATTR line="13" name="@age"><AGGR line="13" name="max"/></ATTR></PATTERN>
    // <PATTERN line="13" name="#users"><ATTR line="13" name="@age"><COUNT line="13" name="count"/></ATTR></PATTERN>
    val name = (d \ "@name" text) // do not remove '#' (as pattern loader (older code) uses this
    visible.find(_.name == name) match{
      case Some(pattern) =>
        if (!pattern.traversible) throw XMappingLoaderException(s"""pattern $pattern mapped to key $$$keyName is not traversible ${srcLoc(d)}""")
        if (!pattern.returnedCols.contains(keyName))
           throw XMappingLoaderException(s"""pattern $pattern does not return key $$$keyName ${srcLoc(d)}""")
//        val optAttr = (d \ "ATTR").map(getAttr(_, pattern)).headOption
        val attrs = (d \ "ATTR").map(getAttr(_, pattern))
        
        ////  case class PatternAttrAggr(pattern:Pattern, optionAttrAggr:Option[Either[AggrType, AttrAggr]]) {    
        //  case class PatternAttrAggr(pattern:Pattern, optionAttrAggr:Option[Either[AggrType, Seq[AttrAggr]]]) {    
        
        val optCount = (d \ "COUNT").headOption.isDefined        
        PatternAttrAggr(
          pattern, 
          if (optCount) Some(Left(Count)) else { 
            if (attrs.nonEmpty) 
              Some(Right(attrs))
            else None
          }
        )
      case _ => 
        // println("visible pattens below")
        // visible foreach println
        throw XMappingLoaderException(s"""No visible pattern $name ${srcLoc(d)}""")
    }
  }

  private def getKeyMap(d:Node):KeyMap = {
    // <KEY line="3" name="$userID"><PATTERN line="3" name="#users"/></KEY>
    val keyName = (d \ "@name").text.tail // remove '$'
    
    
    if (!visible.exists(x => x.returnedCols.exists(_ == keyName))) {
      // visible.flatMap(_.returnedCols).foreach(println)
      throw XMappingLoaderException(s"""No visible pattern found returing key [$$$keyName] ${srcLoc(d)}""")
    }
    // made \\ to \ below line
    KeyMap(keyName, (d \ "PATTERN").headOption match{
        case Some(x) => Right(getPattAttrAggr(x, keyName))
        case None => 
          // added next few lines
          (d \ "COUNT").headOption match {
            case Some(_) => Left(Count)
            case None => 
              (d \ "_").headOption match {
                case Some(x) => 
                  throw XMappingLoaderException(s"""Invalid aggregate [$x] for key [$$$keyName] ${srcLoc(d)}""")
                case _ => Left(Ignore)
              }
          }
          // commented below line
          //Left(Count) // getCount(d \ "COUNT")
      }
    )
  }
  private def getKeyMaps(d:Node):Seq[KeyMap] = d \\ "KEY" map(getKeyMap)  
}
