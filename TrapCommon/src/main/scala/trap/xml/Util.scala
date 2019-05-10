/* Author:Amitabh Saxena (amitabh.saxena@accenture.com)
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package trap.xml
//import java.io.BufferedInputStream
//import java.io.BufferedOutputStream
//import java.io.BufferedReader
//import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

import scala.xml.Elem
import scala.xml.Attribute
import scala.xml.Text
import scala.xml.Node
import scala.xml.NodeSeq
import scala.xml.Null
import scala.xml.PrettyPrinter
import scala.xml.XML
import trap.Util._
import trap.file.Util._
object Util {
  
  def formatNicely(xmlFile:String) = 
    writeToTextFile(xmlFile, new PrettyPrinter(200, 2).format(loadXML(xmlFile)))

  def formatNicely(xml:Elem) = new PrettyPrinter(200, 2).format(xml)
    
  def getAttributeText(xml:NodeSeq, attrName:String) = {
    printdbg ("    ===> getAttributeTest "+xml+": "+attrName)
    (xml \ ("@"+attrName)).text
  }
  private val r = <root/>
  def getNode(n:String) = r.copy(label=n)
  def addChild(n: Node, newChild: Node) = n match {
    case Elem(prefix, label, attribs, scope, child @ _*) =>
      Elem(prefix, label, attribs, scope, child ++ newChild : _*)
    case _ =>  throw new Exception("Can only add children to elements!")
  }
  def addChildren(n:Node, children:NodeSeq) = children.foldLeft(n)((x, y) => addChild(x, y))
  def addAttribute(n:Elem, attrName:String, attrValue:String) = n % Attribute(None, attrName, Text(attrValue), Null)
  def attr(xml:Node, attrName:String) = (xml \ ("@"+attrName)).text
  def loadXML(fileName:String) =  
    using(new FileInputStream(fileName)){
      fis => using(new InputStreamReader(fis)){
        isr => XML.load(isr)
      }
    }

  def getElementName (x:Node) = x.nameToString(new StringBuilder).toString
  def filterByAttr(xml:NodeSeq, attrName:String, attrValue:String) = 
    xml.filter(x  => (x \ ("@"+attrName)).text == attrValue)
  
  def getElementsWithAttribute(xml:NodeSeq, elementName:String, attrName:String, attrVal:String) = 
    xml \ elementName collect { x => (x \ ("@"+attrName)).map(_.text).contains(attrVal) match {
        case true => x
      }   
    }
  def getElementWithAttribute(xml:NodeSeq, elementName:String, attrName:String, attrVal:String) = 
    ((xml \ elementName).find{ x => (x \ ("@"+attrName)).map(_.text).contains(attrVal) }).get
  def getChildren(xml:NodeSeq) = xml \ "_"
}


