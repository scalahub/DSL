package trap.datalog

import org.deri.iris.api.basics.IQuery
import org.deri.iris.api.basics.ITuple
import org.deri.iris.storage.IRelation
import trap.dsl.FactDataStructures._
import scala.collection.JavaConversions._
import trap.xdsl.XMappingDataStructures._

class DatalogException(s:String) extends Exception(s)
object ResultUtil {
  /*
   * will be needed if we need to get answers without writing them to file, say to use them internally
   * Possibly we will need to explicitly get intermediate answers in some cases (instead of letting Datalog
   * do the final computation when needed. This could be required for the @count key
   */
  
  def getResultValues(results:Seq[(IQuery,IRelation)]):Seq[(String, Seq[Seq[ParaValue[_]]])] = results map (result => {
    val (query, relation) = result
    (getQueryName(query), (1 to relation.size).map(i => relation.get(i-1)).map(getTupleValues))
  })

  def getResultAggrValues(results:Seq[(IQuery,IRelation, Seq[AggrType])]):Seq[(String, Seq[Seq[ParaValue[_]]])] = results map (result => {
    val (query, relation, aggrTypes) = result  
    val numCols = query.getVariables.size
    val numRows = relation.size

    val columns = (1 to numCols).map{
      col => 
        (1 to numRows).map{
          row => 
            val values = getTupleValues(relation.get(row - 1))
            values(col - 1).value match{
              case int:Int => int.toLong
              case int:Long => int
              case any if aggrTypes(col - 1) == Count => any
              case _ => ???
            }             
        }        
    }
    
    val answer = (columns zip aggrTypes).collect {
      case (col, Max) => if (col.nonEmpty) col.map(_.asInstanceOf[Long]).max else 0
      case (col, Min) => if (col.nonEmpty) col.map(_.asInstanceOf[Long]).min else 0
      case (col, Avg) => if (col.nonEmpty) col.map(_.asInstanceOf[Long]).sum/col.size else 0
      case (col, Sum) => col.map(_.asInstanceOf[Long]).sum
      case (col, Count) => col.distinct.size
    }.map{
      int => ParaValue(int, ParaIntType)
    }
    
    (getQueryName(query), Seq(answer))
    //(getQueryName(query), (1 to relation.size).map(i => relation.get(i-1)).map(getTupleValues))
  })

  private def getQueryName(q:IQuery) = q.getLiterals.get(0).getAtom.getPredicate.toString   
  private def getTupleValues(t:ITuple) = (1 to t.size).map(i => getParamValue(t.get(i-1).getValue))
  private def getParamValue(o:Object):ParaValue[_] = o match {
    case i:java.math.BigDecimal => ParaValue(BigInt(i.toBigInteger).toLong, ParaIntType)
    case s:String => ParaValue(s, ParaStringType)
    case any => throw new DatalogException("unsupported param type: "+any.getClass)
  }

  /*
   * generates result Facts, which are not necessarily always needed. If writing results, use the above getResultValues method instead
   * This method is given in case we need to get results converted to Facts for any reason.
   */
  @deprecated def getResultFacts(results:Seq[(IQuery,IRelation)]):Seq[Seq[Fact]] = results map (result => {
    def getQueryParamNames(q:IQuery) = q.getVariables.map(_.getValue)
    def getQueryFact(q:IQuery, t:ITuple):Fact = {
      val paraValues = getTupleValues(t) 
      Fact(FactDef(getQueryName(q), getQueryParamNames(q).zip(paraValues).map(pv => ParaDef(pv._1, pv._2.valueType))), paraValues.toArray)
    }
    val (query, relation) = result
    (1 to relation.size) map(i => relation.get(i-1)) map(tuple => getQueryFact(query, tuple))
  })
}