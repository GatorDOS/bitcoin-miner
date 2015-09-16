package rdf

/**
 * @author mebin
 */
object Query {
  var a:Array[Byte] = "1".getBytes
  def main(args: Array[String]): Unit = {
    //    val rtLoad = Runtime.getRuntime.exec(Array("rdf3xload", "rdfdb"))
    //    rtLoad.waitFor
    val rtQuery = Runtime.getRuntime.exec(Array("rdf3xquery", "rdfdb"))
    val is = rtQuery.getInputStream
    val os = rtQuery.getOutputStream
    os.write("SELECT  ?X ?Y ?Z WHERE { ?X <http://dummy.org/sample#9> <http://dummy.org/sample#-1> . ?Y <http://dummy.org/sample#9> <http://dummy.org/sample#-7> . ?Z <http://dummy.org/sample#9> <http://dummy.org/sample#-6> . ?X <http://dummy.org/sample#6> ?Z . ?Z <http://dummy.org/sample#4> ?Y . ?X <http://dummy.org/sample#12> ?Y}".getBytes)
    is.read(a)
    println(new String(a))
  }
}