
package trap

import java.io.File
import java.io.FileInputStream
//import amit.common.Util._
import Util._

object CryptoUtil {
  /** code from 
   * http://stackoverflow.com/a/2932513/243233
   */
  def md5(file:File) = try {
    using(new FileInputStream(file)){
      fis => org.apache.commons.codec.digest.DigestUtils.md5Hex(fis).toUpperCase     
    } 
  } catch { case any:Exception => "No file found"}
}
