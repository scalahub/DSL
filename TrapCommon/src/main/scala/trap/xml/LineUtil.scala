package trap.xml

import org.xml.sax.{helpers, Locator, SAXParseException}

@deprecated("Unused as of now. To remove", "09 June 2019")
trait WithLocationAlt extends helpers.DefaultHandler {
    var locator: org.xml.sax.Locator = _
    def printLocation(msg: String) {
        println("%s at line %d, column %d" format (msg, locator.getLineNumber, locator.getColumnNumber))
    }

    // Get location
    abstract override def setDocumentLocator(locator: Locator) {
        this.locator = locator
        super.setDocumentLocator(locator)
    }

    // Display location messages
    abstract override def warning(e: SAXParseException) {
        printLocation("warning")
        super.warning(e)
    }
    abstract override def error(e: SAXParseException) {
        printLocation("error")
        super.error(e)
    }
    abstract override def fatalError(e: SAXParseException) {
        printLocation("fatal error")
        super.fatalError(e)
    }
}

// import scala.xml.{factory, parsing, Elem}
// object MyLoader extends factory.XMLLoader[Elem] {
    // override def adapter = new parsing.NoBindingFactoryAdapter with WithLocation
// }

// object MyLoader extends factory.XMLLoader[Elem] {
    // override def adapter = new parsing.NoBindingFactoryAdapter with parsing.ConsoleErrorHandler
// }

import org.xml.sax.Locator
import scala.xml._
import parsing.NoBindingFactoryAdapter

@deprecated("Unused as of now. To remove", "09 June 2019")
trait WithLocation extends NoBindingFactoryAdapter {
    var locator: org.xml.sax.Locator = _

    // Get location
    abstract override def setDocumentLocator(locator: Locator) {
        this.locator = locator
        super.setDocumentLocator(locator)
    }

    abstract override def createNode(pre: String, label: String, attrs: MetaData, scope: NamespaceBinding, children: List[Node]): Elem = (
        super.createNode(pre, label, attrs, scope, children) 
        % Attribute("line_xml", Text(locator.getLineNumber.toString), Null) 
        % Attribute("column_xml", Text(locator.getColumnNumber.toString), Null)
    )
}

object MyLoader extends factory.XMLLoader[Elem] {
    // Keeping ConsoleErrorHandler for good measure
    override def adapter = new parsing.NoBindingFactoryAdapter with parsing.ConsoleErrorHandler with WithLocation
}

object GetLineNos {
	def putLines(fileName:String) = {
		trap.file.Util.writeToTextFile(fileName+".xml", MyLoader.loadFile(fileName).toString)
	}
}











