package trap.xdsl

//import trap.datalog.FactDataStructures
import java.io.PrintStream
import trap.dsl.AbstractDSLConfig
import trap.dsl.FactDataStructures._

import XDSLDataStructures._
/*
 * High level idea: 
 *  There are some basic facts generated from the "JIL to Datalog" compiler; facts such as Class, Method, AssignInvoke, AssignSimple, etc
 *  
 *  There are some additional facts that are generated from the initial facts that would form the starting point of our DSL
 *  These new facts comprise: (1) facts visible to end user such as assign, invoke, etc and (2) facts invisible to the end user (such as 
 *  pred, etc). The visible facts will be used to write patterns, while invisible facts will be used internally 
 *  
 *  Patterns are of two types:
 *  
 *    library patterns (such as assign, invoke, etc)
 *    and
 *    user defined patterns formed from combining library patterns and other user defined patterns
 *    
 *    patterns have attribute, which are inherited from the parent pattern.
 *    
 *    we refer to attribute as @attribute or #pattern.@attribute
 *    
 *    for example @methodName or #class.@className
 *    
 *    a pattern is defined as 
 *      define <patternName> as <match other patterns>. for example
 *          define #myPattern as <pattenrn matcher>
 *      
 *      <pattern matcher> is most simply defined as another pattern and a filter, for example
 *          #invoke where {#method.@methodName = 'foo' or @returnType = 'bar' }
 *   
 *    Thus a full pattern is defined something like
 *          define #myPattern as #invoke where { #method.@methodName = 'foo' or '@returnType' = 'bar' }
 */         
import XDSLBasisUtil._

class XDSLBasisException(s:String) extends Exception(s)

object XDSLBasisUtil {
  type Node[T] = Set[T]
  type Edge[T] = Set[Node[T]] // Edge is actually an unordered Pair of Nodes, which is not available in Scala, hence using Set. The set will always contain exactly two elements.
  type LabelSet[T] = Set[(String, Node[T])] // set of labels (SetIDs) and Nodes (which are sets of type T)

  type Basis = Seq[FactDef]   // basis is a seq of FactDef 
  type Column = (FactDef, ParaDef)
  
  type ColumnSet = LabelSet[Column]   // contains detailed info
  type NameSet = LabelSet[String] // contains just fact names such as "Method"
  
     /*  example of a LabelSet[String] 
                Set {
                     Pair("1", Set{"Class", "Method", "Modifier"}),
                     Pair("2", Set{"MLabel", "Param", "Stmt", "Method"}),
                     Pair("3", Set{"Return", "Nop", "Stmt", "ThrowEx",  ...})
                    } 
      */     
  private def isTraversible(fact:FactDef) = fact.paramDefs.exists(_.primaryKey.isDefined)
  private def getTraversibleFacts(basis:Seq[FactDef]) = basis.filter(isTraversible)  
  private def getVisiblePatterns(basis:Seq[FactDef]) = {// these will be visible to DSL    
    if (basis.map(_.factName).toSet.size != basis.size) throw new XDSLBasisException("Fact names have duplicates.")
    else 
      basis.toArray.filter(_.visible).map(fact => {
        var patternKeyCtr = 0
        def getPatternKeyCtr = {patternKeyCtr += 1; patternKeyCtr}
        val keyedParaDefs = fact.paramDefs.filter(para => para.primaryKey.isDefined | para.patternKey.isDefined)
        // val returnedCols = fact.getKeyVars.filter(_.isDefined).map(_.get)
        val returnedCols = fact.getHeadKeyVars.filter(_.isDefined).map(_.get)
        if (returnedCols.size == 0) throw new XDSLBasisException("at least one primary key or pattern key must be returned.")
        // val pattern = Pattern("#"+fact.factName.toLowerCase, None, returnedCols, isTraversible(fact))
        val pattern = Pattern("#"+fact.factName, None, returnedCols, isTraversible(fact))
        var patternKeyIndexCtr = 0        
        fact.paramDefs.filter(_.visible).map(para => {
          val keyName = "@"+para.paraName
          val keyType = if (para.patternKey.isDefined) DSLPatternType(para.patternKey.get) else 
            para.paraType match {
              case ParaIntType => DSLIntType
              case ParaStringType => DSLStringType
              case ParaBooleanType => DSLBooleanType
              case ParaDecimalType => DSLDecimalType
              case any => throw new XDSLBasisException("no such type: "+any)  
            }
          Attr(pattern, keyName, keyType)
        }).foreach(pattern.addAttr)
        (pattern, fact)
      })
  }
  private def getConnections[T](labelSet:LabelSet[T]) = {    
    /*
     takes input Set{
                     Pair("1", Set{"Class", "Method", "Modifier"}),
                     Pair("2", Set{"MLabel", "Param", "Stmt", "Method"}),
                     Pair("3", Set{"Return", "Nop", "Stmt", "ThrowEx"})
                    }
     and outputs: 
      Set{
          ("Method", Set(2){ Pair("1", Set{ "Class", "Method", "Modifier" }), Pair("2", Set{"MLabel", "Param", "Stmt", "Method"}) }),
          ("Stmt", Set(2){ Pair("2", Set{"MLabel", "Param", "Stmt", "Method"}), Pair("3", Set{"Return", "Nop", "Stmt", "ThrowEx"}) })
         }  
    */
    val allPairs = for(x <- labelSet; y <- labelSet) yield Set(x, y)
    val edges = allPairs.filter(_.size == 2)
    val connections = edges.map(edge => (edge map(_._2) reduceLeft(_ & _), edge)).filter(! _._1.isEmpty)
    if (connections.exists(_._1.size > 1)) throw new XDSLBasisException("Input facts are not normalized (two nodes have more than one edge).")
    connections.map(x => (x._1.toList(0), x._2))
  } 
  /* // following is not used as of now. If needed, uncomment it 
   * private def getColumnConnections(columnSet:ColumnSet) = getConnections(columnSet.map(x => (x._1, x._2.map(_._1)))) 
   */
  private def checkNormalized[T](labelSet:LabelSet[T]) = { 
    /*
     sets is of type Set{
                         Pair("1", Set{"Class", "Method", "Modifier"}),
                         Pair("2", Set{"MLabel", "Param", "Stmt", "Method"}),
                         Pair("3", Set{"Return", "Nop", "Stmt", "ThrowEx"})
                        }
     getConnectedSet(sets) outputs this
      Set{
          ("Method", Set(2){ Pair("1", Set{ "Class", "Method", "Modifier" }), Pair("2", Set{"MLabel", "Param", "Stmt", "Method"}) }),
          ("Stmt", Set(2){ Pair("2", Set{"MLabel", "Param", "Stmt", "Method"}), Pair("3", Set{"Return", "Nop", "Stmt", "ThrowEx"}) })
         }  
     graph is just a filtered version of above:
      Set{
          Set(2){ Set{"Class", "Method", "Modifier"}, Set{"MLabel", "Param", "Stmt", "Method"} },
          Set(2){ Set{"MLabel", "Param", "Stmt", "Method"}, Set{"Return", "Nop", "Stmt", "ThrowEx"}})
         }  
    */
    val graph:Set[Edge[T]] = getConnections(labelSet) map(_._2.map(_._2))
    var nodes:Set[Node[T]] = Set()
    var edges:Set[Edge[T]] = Set()
    def addNode(n:Node[T]) = if (nodes.contains(n)) throw new XDSLBasisException("Not normalized. Loop detected: "+n) else nodes = nodes + n
    def addEdge(e:Edge[T]) = edges = edges + e
    def visit(edgeToVisit:Edge[T], lastNode:Node[T]):Unit = {
      if (! edges.contains(edgeToVisit)) {
        addEdge(edgeToVisit)
        edgeToVisit.foreach(node => {
          if (lastNode != node) addNode(node) 
          graph.filter(edge => edge.contains(node) && ! edges.contains(edge)).foreach(edge => visit(edge, node))
        })
      }
    }
    visit(graph.toList(0), Set()) // graph foreach (visit(_, Set()))
    if (nodes.reduceLeft(_ | _) != labelSet.map(_._2).reduceLeft(_ | _)) throw new XDSLBasisException("Not all sets are reachable") 
  }
  private def getLabelSets(traversibleFacts:Basis):(ColumnSet, NameSet) = {
    if (! traversibleFacts.forall(fact => fact.paramDefs.exists(_.primaryKey.isDefined))) throw new XDSLBasisException("Some facts do not have primary key(s).")
    val a = traversibleFacts.map(f => (f, f.paramDefs.filter(p => p.primaryKey.isDefined).map(p => (p.primaryKey.get, p))))
    val seq = a.flatMap(x => x._2.map(y => (y._1, (x._1, y._2)))).groupBy(_._1).toSeq.map(x => (x._1, x._2.map(_._2).toSet)) 
    val columnSet = seq.toSet
    val nameSet = columnSet.map(g => (g._1, g._2.map(_._1.factName)))
    checkNormalized(nameSet)
    (columnSet, nameSet)
  }
  private def checkIsolatedFacts(basis:Basis) = {
    basis foreach (x => {
      if (x.paramDefs.forall(p => p.patternKey.isEmpty && p.primaryKey.isEmpty)) {
        throw new XDSLBasisException("isolated fact: "+x.factName)
      }
    })
  }
}

class XDSLBasisUtil(val basis:Basis) {
  def this(config:AbstractDSLConfig) = this(config.reducedBasis ++ config.extendedBasis)
  type Path = List[Column] 
  private lazy val (columnSet, nameSet) = getLabelSets(getTraversibleFacts(basis))  // private val (columnSet, nameSet) = getLabelSets(basisFacts) 
  lazy val factMap = getVisiblePatterns(basis) // get all visible patterns   // some of which may not be traversible
  lazy val patterns = factMap map (_._1)
  private lazy val nameConnections = getConnections(nameSet)   // private val columnConnnections = getColumnConnections(columnSet)
  private lazy val routes = nameConnections.map(x => (x._1, x._2.map(_._1)))
  private def getSetIDs(factName:String) = nameSet.filter(_._2.contains(factName)).map(_._1)
  private def getColumn(setID:String, factName:String):Column = {
    columnSet.find(_._1 == setID) match {
      case Some(col) => 
        col._2.find(_._1.factName == factName) match {
          case Some(column) => column
          case _ => throw new XDSLBasisException ("no such column: "+factName)
        }
      case _ => throw new XDSLBasisException ("no such setID: "+setID)
    }
  }
  private def getKeyPath(current:(String, String, String), end:(String, String), path:Path):Option[Path] = {
    val (currentFact, currentSetID, otherSetID) = current
    val (endFact, endSetID) = end
    val newPath = path ::: List(getColumn(currentSetID, currentFact), getColumn(otherSetID, currentFact))
    if (currentFact == endFact) Some(newPath)
    else {
      routes.find(r => r._1 != currentFact && r._2.contains(otherSetID)) match {
        case Some(route) => getKeyPath((route._1, otherSetID, route._2.find(_ != otherSetID).get), end, newPath)
        case _ => None
      }
    }
  }
  private def getSetPath(startSetID:String, endSetID:String):Path = {
    val startTuples = routes.filter(_._2.contains(startSetID)).map(r => (r._1, startSetID, r._2.find(_ != startSetID).get))
    val endTuples = routes.filter(_._2.contains(endSetID)).map(r => (r._1, endSetID))
    val startEndTuples = for(s <- startTuples; e <-endTuples) yield (s, e)
    val validPaths = startEndTuples.map(x => getKeyPath(x._1, x._2, Nil)).filter(_.isDefined).map(_.get).toSeq
    val shortestLength = validPaths.map(_.size).sorted.apply(0)
    validPaths.find(_.size == shortestLength).get    
  }
  def getSetsToSetsPath(startSetIDs:Set[String], endSetIDs:Set[String]):((String, String), Path) = { // first returned param is the start, end node for the path
    startSetIDs & endSetIDs match {
      case intersect if intersect.isEmpty => 
      /*  no common elements in set. Need to traverse graph
            {"1", "2"} & {"3", "4"}   => {}
            or
            {"1", "2"} & {"4"}        => {}
            or
            {"1"}      & {"2", "4"}   => {}
            or
            {"1"}      & {"2"}        => {}
      */
        val edges = (for (start <-startSetIDs; end <- endSetIDs) yield (start, end)).toSeq
        val paths = edges.map(edge => getSetPath(edge._1, edge._2))
        val sortedLengths = paths.map(_.length).sorted
        val shortest = sortedLengths(0)
        (edges zip paths).find(_._2.size == shortest).get
      case intersect =>
       /*  common elements in set
            {"1", "2"} & {"2", "3"}   => {"2"}
            or
            {"1", "2"} & {"1"}        => {"1"}
            there can be maximum one element in this set if facts are normalized. We assume this.
       */
        val id = intersect.toSeq(0) // get first (the only element) of this set
        ((id, id), Nil)
    }
  }
  def printDetails(out:PrintStream) {
    printDetailsForPatterns(out, patterns)
    //    out.println ("\n"+patterns.size+" available DSL patterns: ([T] indicates the pattern is traversible. Values inside {} indicate returned key(s))\n")
    //    patterns.map(_.signature) foreach out.println    
    //    out.println
    //    /* 
    //       // [following code is for debug purpose only]. 
    //       // It prints out all defined patterns. Not really needed unless trying to debug a problem
    //       val allPatternNames = nameSet.flatMap(_._2).map("#"+_)  
    //       println ("\nall patterns =>\n")
    //       allPatternNames foreach println
    //     */  
  }
  def printDetailsForPatterns(out:PrintStream, patternsToPrint:Seq[Pattern]) {
    out.println ("\n"+patternsToPrint.size+" available DSL patterns: ([T] indicates the pattern is traversible. Values inside {} indicate returned key(s))\n")
    patternsToPrint.map(_.signature) foreach out.println    
    out.println
    /* 
       // [following code is for debug purpose only]. 
       // It prints out all defined patterns. Not really needed unless trying to debug a problem
       val allPatternNames = nameSet.flatMap(_._2).map("#"+_)  
       println ("\nall patterns =>\n")
       allPatternNames foreach println
     */  
  }
  def getSetsToFactPath(startSetIDs:Set[String], endFact:String):((String, String), Path) = {
    val endSetIDs = getSetIDs(endFact)
    if (endSetIDs.size == 0) throw new XDSLBasisException("No such traversible fact (maybe it is a patternKey-only fact): "+endFact)
    val (edge, path) = getSetsToSetsPath(startSetIDs, endSetIDs)
    val (startSetID, endSetID) = edge
    (edge, if (path.isEmpty || path.last._1.factName != endFact) path ::: List(getColumn(endSetID, endFact)) else path)
  }
  def getPath(startFact:String, endFact:String):Path = if (startFact == endFact) Nil else {
    val startSetIDs = getSetIDs(startFact)
    if (startSetIDs.size == 0) throw new XDSLBasisException("No such traversible fact (maybe it is a patternKey-only fact): "+startFact)
    val (edge, path) = getSetsToFactPath(startSetIDs, endFact)
    val (startSetID, endSetID) = edge
    if (path.head._1.factName != startFact) List(getColumn(startSetID, startFact)) ::: path else path
  }
  checkIsolatedFacts(basis)
  // printDetails  
}
 