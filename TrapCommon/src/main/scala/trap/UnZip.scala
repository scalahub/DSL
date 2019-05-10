/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package trap

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import Util._

object UnZip {
  def unzipFiles(zipfile:File, outputdir:String) = {
    using(new FileInputStream(zipfile)) {
      val buffer = new Array[Byte](1024)
      is => using (new ZipInputStream(is)) {
        zis => {
          var ze:ZipEntry = null
          while({ze = zis.getNextEntry; ze != null}){
            val fileName=ze.getName
            
            val newFile = new File(outputdir+File.separator+fileName)            
            //println("file:"+newFile.getAbsolutePath)
            //println("file unzip : "+ newFile.getAbsoluteFile())
//            //create all non exists folders
//            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();
            if (!ze.isDirectory) {
              using (new FileOutputStream(newFile)){              
                fos => 
                  var len:Int = 0
                  while ({len = zis.read(buffer); len > 0}) {
                    fos.write(buffer, 0, len)
                  }
              }
            } else {
              //println("dir:"+newFile.getAbsolutePath)
              newFile.mkdir();
            }
          }
          zis.closeEntry();
        }
      }
    }
  }
}
