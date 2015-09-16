package rdf

import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @author mebin
 */
object Query {
  var a: Array[Byte] = "1".getBytes
  def main(args: Array[String]): Unit = {
    //    val rtLoad = Runtime.getRuntime.exec(Array("rdf3xload", "rdfdb"))
    //    rtLoad.waitFor
    val rtQueryProcess = Runtime.getRuntime.exec(Array("rdf3xquery", "rdfdb"))
    val is = rtQueryProcess.getInputStream
    val os = rtQueryProcess.getOutputStream
    
    os.write("SELECT  ?X ?Y ?Z WHERE { ?X <http://dummy.org/sample#9> <http://dummy.org/sample#-1> . ?Y <http://dummy.org/sample#9> <http://dummy.org/sample#-7> . ?Z <http://dummy.org/sample#9> <http://dummy.org/sample#-6> . ?X <http://dummy.org/sample#6> ?Z . ?Z <http://dummy.org/sample#4> ?Y . ?X <http://dummy.org/sample#12> ?Y}".getBytes)

    val in = new BufferedReader(new InputStreamReader(rtQueryProcess.getInputStream()));
    var line: String = in.readLine()
    while (line != null) {
      line = in.readLine()
      println(line);
    }
  }
}