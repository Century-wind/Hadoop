
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object eg  {

  def main(args: Array[String]) = {

//    设置文件URL
    val logFile = "file:///home/hang/Documents/实验文档/hadoop/chapter5-data1.txt"
//    构建scala项目
    val conf = new SparkConf().setAppName("Simple Application").setMaster("local")
    val sc = new SparkContext(conf)
//    导入目标文件
    val logDate = sc.textFile(logFile, 2).cache()

//    该系总共有多少学生；
    val students = logDate.map(row=>row.split(",")(0))
    val s_total = students.distinct().count()
//    println("学生总数为：%d".format(s_total))

//    该系共开设来多少门课程；
    val courses = logDate.map(row=>row.split(",")(1))
    val c_total = courses.distinct().count()
//    println("课程总数为：%d".format(c_total))

//    Tom同学的总成绩平均分是多少；
    val T_grade = logDate.filter(row=>row.split(",")(0)=="Tom")

    T_grade.map(row=>(row.split(",")(0),row.split(",")(2).toInt)).
      mapValues(x=>(x,1)).reduceByKey((x,y) => (x._1+y._1,x._2 + y._2)).
      mapValues(x => (x._1 / x._2)).collect().foreach(println)

//    求每名同学的选修的课程门数；
    val s_course = logDate.map(row=>(row.split(",")(0),row.split(",")(1)))
      s_course.mapValues(x=>(x,1)).reduceByKey((x,y)=>(" ",x._2+y._2)).
        mapValues(x=>x._2).foreach(println)

//    该系DataBase课程共有多少人选修；
    val numb = logDate.filter(row=>row.split(",")(1)=="DataBase").count()
//        println("共有"+numb+"人选修DataBase")

//    各门课程的平均分是多少；
    val e_course = logDate.map(row=>(row.split(",")(1),row.split(",")(2).toInt))
        e_course.mapValues(x=>(x,1)).reduceByKey((x,y) => (x._1+y._1,x._2 + y._2)).
          mapValues(x => (x._1 / x._2)).collect().foreach(println)

//    使用累加器计算共有多少人选了DataBase这门课。
    val D_person = logDate.filter(row=>row.split(",")(1)=="DataBase").
      map(row=>(row.split(",")(1),1))
    val accum = sc.longAccumulator("My Accumulator")//累加器函数Accumulator
    D_person.values.foreach(x => accum.add(x))


    println("学生总数为：%d".format(s_total))
    println("课程总数为：%d".format(c_total))
    println("共有"+numb+"人选修DataBase")
    println("共有"+accum.value+"人选修DataBase")
  }
}
