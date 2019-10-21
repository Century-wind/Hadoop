import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object test3 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("Grade")
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR") //日志级别

    val algo = sc.textFile("file:///home/hang/Documents/实验文档/hadoop/Algorithm.txt")
    val daba = sc.textFile("file:///home/hang/Documents/实验文档/hadoop/Database.txt")
    val pyth = sc.textFile("file:///home/hang/Documents/实验文档/hadoop/Python.txt")

//    合并三个文件的数据
    val aver = algo.union(daba).union(pyth)

    aver.map(row => (row.split(" ")(0),row.split(" ")(1).toFloat)).// 拆分文本数据，分为（姓名+成绩）
      mapValues(x=>(x,1)).reduceByKey((x,y)=>(x._1+y._1,x._2+y._2)).//按key存为(成绩，1）格式，便于计算
      mapValues(x=>(x._1/x._2)).repartition(1).//计算平均数，存入一个文件输出
      saveAsTextFile("file:///home/hang/Documents/实验文档/hadoop/text3")
  }
}
