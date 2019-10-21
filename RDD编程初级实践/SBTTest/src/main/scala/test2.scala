import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object test2 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("hang")
    val sc = new SparkContext(conf)
    val textA = sc.textFile("file:///home/hang/Documents/实验文档/hadoop/textA.txt")
    val textB = sc.textFile("file:///home/hang/Documents/实验文档/hadoop/textB.txt")

//    合并文件
    val textC = textA.union(textB)
//    数据去重，并作为一个文件保持到目标路径
    textC.map(row => (row.split(" ")(0),row.split(" ")(1))).distinct().repartition(1).
      saveAsTextFile("file:///home/hang/Documents/实验文档/hadoop/textC.txt")

  }
}
