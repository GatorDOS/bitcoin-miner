package rdf

import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader

/**
 * @author mebin
 */
object Query {
  def main(args: Array[String]): Unit = {

    val query = "SELECT  ?X ?Y ?Z WHERE { ?X <http://dummy.org/sample#9> <http://dummy.org/sample#-1> . ?Y <http://dummy.org/sample#9> <http://dummy.org/sample#-7> . ?Z <http://dummy.org/sample#9> <http://dummy.org/sample#-6> . ?X <http://dummy.org/sample#6> ?Z . ?Z <http://dummy.org/sample#4> ?Y . ?X <http://dummy.org/sample#12> ?Y}"
    val file = new File("q2.txt")
    val fileWriter = new FileWriter(file)
    fileWriter write query
    fileWriter close
    val rtQueryProcess = Runtime.getRuntime.exec(Array("rdf3xquery", "/tmp/shared/output/final/rdfdb", file.getAbsolutePath))
//    rtQueryProcess waitFor
    val is = rtQueryProcess.getInputStream
    val in = new BufferedReader(new InputStreamReader(rtQueryProcess.getInputStream ));
    var line: String = in.readLine()
    while (line != null) {
      line = in.readLine()
      println(line);
    }
  }
}