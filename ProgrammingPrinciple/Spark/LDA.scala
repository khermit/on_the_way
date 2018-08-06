package quan

import org.apache.spark.ml.feature.MinHashLSH
import org.apache.spark.ml.linalg.{Vectors, _}
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.feature.{HashingTF, IDF, IDFModel}
import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.{SparseVector => SV}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SQLContext, SparkSession, _}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.{immutable, mutable}
import scala.util.Random
//import os
import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.mllib.util.MLUtils

// MyPartitioner Function!
class HQPartitioner(numParts: Int) extends Partitioner{
  //覆盖分区数
  override def numPartitions: Int = numParts

  override def getPartition(key: Any): Int = {
    key.toString.toInt % numParts
  }
  override def equals(other: Any): Boolean = other match {
    case mypartition: MyPartitioner =>
      mypartition.numPartitions == numPartitions
    case _ =>
      false
  }

}

object GibbsSampling1{
  println("hello gibbs sampling!")
  /*
  *print out topics
  * output topK words in each topic
   */
  def topicsInfo(nkv: Array[Array[Int]], allWords: List[String], kTopic: Int, vSize: Int, topK: Int)={
    var res = ""
    for(k <- 0 until kTopic){
      val distOnTopic = for(v <- 0 until vSize) yield (v, nkv(k)(v))
      val sorted = distOnTopic.sortWith((tupleA,tupleB)=>tupleA._2 > tupleB._2)
      res = res + "topic " + k + ":" + "\n"
      for(j <- 0 until topK){
        res = res + "n(" + allWords(sorted(j)._1) + ")=" + sorted(j)._2 + " "
      }
      res = res + "\n"
    }
    res
  }

  /*
  *Gibbs sampling
  * topicAssignArr Array[(word,topic)]
  * nmk:Array[n_{mk}]
   */
  def gibbsSampling(topicAssignArr: Array[(Int, Int)], nmk: Array[Int],
                    nkv: Array[Array[Int]], nk: Array[Int], kTopic: Int,
                    alpha: Double, vSize: Int, beta: Double)={
    val length = topicAssignArr.length
    print("topicAssignArr.length: " + length)
    for (i <- 0 until length){
      val topic = topicAssignArr(i)._2
      val word = topicAssignArr(i)._1
      //reset nkv,nk,nmk
      nmk(topic) = nmk(topic) - 1
      nkv(topic)(word) = nkv(topic)(word) - 1
      nk(topic) = nk(topic) - 1
      //sampling
      val topicDist = new Array[Double](kTopic)   //Important, not Array[Double](kTopic) which while lead to Array(4.0)
      for (k <- 0 until kTopic){
        nmk(k)
        nkv(k)(word)
        nk(k)
        topicDist(k) = (nmk(k).toDouble + alpha) * (nkv(k)(word) + beta) / (nk(k) + vSize*beta)
      }
      val newTopic = getRandFromMultiNomial(topicDist)
      topicAssignArr(i) = (word, newTopic)  //Important, not (newTopic,word)
      //update nkv,nk and nmk locally
      nmk(newTopic) = nmk(newTopic) + 1
      nkv(newTopic)(word) = nkv(newTopic)(word) + 1
      nk(newTopic) = nk(newTopic) + 1
    }
    (topicAssignArr, nmk)
  }

  /*
  *get mkv matrix
  * List(((0,0),2),((0,1),1),((word,topic),count))
  * =>Array[Array(...)]
   */
  def updateNKV(wordsTopicReduced: List[((Int, Int), Int)], kTopic: Int, vSize: Int)={
    val nkv: Array[Array[Int]] = new Array[Array[Int]](kTopic)
    for (k <- 0 until kTopic){
      nkv(k) = new Array[Int](vSize)
    }
    wordsTopicReduced.foreach(t=>{  //t is ((Int,Int),Int) which is ((word,topic),count)
      val word = t._1._1
      val topic = t._1._2
      val count = t._2
      nkv(topic)(word) = nkv(topic)(word) + count
    })
    nkv
  }

  /*
  *get nk vector
  * List(((0,0),2),((0,1),1),((word,topic),count))
  * =>Array[Array(...)]
   */
  def updateNK(wordsTopicReduced: List[((Int, Int), Int)], kTopic: Int, vSize: Int)={
    val nk: Array[Int] = new Array[Int](kTopic)
    wordsTopicReduced.foreach(t=>{
      val topic = t._1._2
      val count = t._2
      nk(topic) = nk(topic) + count
    })
    nk
  }

  /*
  *get a topic from Multinomial Distribution
  * usage example: k=getRand(Array(0.1,0.2,0.3,1.1))
   */
  def getRandFromMultiNomial(arrInput: Array[Double]):Int = {
    val rand = Random.nextDouble()
    val s = doubleArrayOps(arrInput).sum
    val arrNormalized = doubleArrayOps(arrInput).map{e=>e/s}
    var localsum = 0.0
    val cumArr = doubleArrayOps(arrNormalized).map{dist =>
      localsum = localsum + dist
      localsum
    }
    //return the new topic
    doubleArrayOps(cumArr).indexWhere(cumDist=>cumDist>=rand)
  }

//  def restartSpark(sc: SparkContext, scMaster: String, remote: Boolean): SparkContext={
//    //After iterations, Spark will create a lot of RDDs and I only have 4g mem for it
//    //So I have to restart the sdaprk. The thread.sleep is for the shutting down of Akka.
//    sc.stop()
//    Thread.sleep(2000)
//    if (remote == true){
//      val scMaster = "spark//202.117.0.140:7077"
//      val sparkContext = new SparkContext(scMaster, "Spark_GibbsSampling_LDA", "./", Seq("job.jar"))
//      (scMaster, sparkContext)
//    }else{
//      val scMaster = "local[4]"
//      val sparkContext = new SparkContext(scMaster, "Spark_GibbsSampling_LDA")
//      (scMaster,sparkContext)
//    }
//  }
//
//  /*
//  *start spark at 202.117.0.140:7077 if remote==true
//  * or start it locally(192.168.0.130:7077) when remote==false
//   */
//  def startSpark(remote: Boolean)={
//    if(remote==true){
//      val scMaster = "spark//202.117.0.140:7077"  //e.g.local[4]
//      val sparkContext = new SparkContext(scMaster, "", "./", Seq("job.jar"))
//      (scMaster, sparkContext)
//    }else{
//      val scMaster = "local[4]"  //e.g.local[4]
//      val sparkContext = new SparkContext(scMaster, "Spark_GibbsSampling_LDA")
//      (scMaster,sparkContext)
//    }
//  }


  /*
  *Get current time!
   */
  def NowDate(): String = {
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = dateFormat.format(now)
    return date
  }

  /*
  *save topic distribution of doc in HDFS
  * INPUT: documents which is RDD[(docId,topicAssignment,nmk)]
  * format: docID, topic distribution
   */
  def saveDocTopicDist(documents: RDD[(Long, Array[(Int, Int)],Array[Int])], pathTopicDistOnDoc: String)={
    documents.map{
      case (docId, topicAssign, nmk) =>
        val doclen = topicAssign.length
        val probabilities = nmk.map(n => n / doclen.toDouble).toList
        (docId, probabilities)
    }.coalesce(1).saveAsTextFile(pathTopicDistOnDoc)
  }

  /*
  *save word distribution on topic into HDFS
  * output format:
  * (topicID, List((#/x,0.05883571428571429)...(与/p0.04017857142857143))
   */
  def saveWordDistTopic(sc: SparkContext, nkv: Array[Array[Int]], nk: Array[Int], allWords: List[String],
                        vSize: Int, topKwordsForDebug: Int, pathWordDistOnTopic: String){
    val topicK = nkv.length
    //add topicid for array
    val nkvWithId = Array.fill(topicK){(0, Array[Int](vSize))}
    for (k <- 0 until topicK){
      nkvWithId(k) = (k, nkv(k))
    }
    //output topKwordsForDebug
    val res = sc.parallelize(nkvWithId).map{ t =>  //topicID,Array(2,3,3,4,...)
      {
        val k = t._1
        val distOnTopic = for (v <-0 until vSize) yield (v,t._2(v))
        val sorted = distOnTopic.sortWith((tupleA,tupleB)=>tupleA._2 > tupleB._2)
        val topDist = {for (v <- 0 until topKwordsForDebug) yield
          (allWords(sorted(v)._1),sorted(v)._2.toDouble / nk(k).toDouble)}.toList
        (k, topDist)
      }
    }
    res.coalesce(1).saveAsTextFile(pathWordDistOnTopic)
  }

  ///////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////
  /*
  *This is the lda's executing function, do the following things:
  *1. Start spark
  *2. Read files into HDFS
  *3. Bulid a dictionary for alphabet: wordIndexMap
  *4. Compute tf-idf for each word
  *5. Compute LSH value for each document
  *6. KMeans algorithms for corpus for data partition
  *7. Init topic assignments for each word in the corpus
  *8. Use gibbs sampling to infer the topic distribution of doc and estimate the parameter nkv and nk
  *9. Save the result in HDFS
    (result part1:topic distribution of doc, result part2:top word in each topic)
   */
  //def lda(filename:String, kTopic:Int, alpha:Double, beta:Double, maxIter:Int, remote:Boolean,
  //        topKwordsForDebug:Int, pathTopicDistOnDoc:String, pathWordDistOnTopic:String) {
  def lda(kTopic:Int, alpha:Double, beta:Double, maxIter:Int, remote:Boolean,
          topKwordsForDebug:Int, pathTopicDistOnDoc:String, pathWordDistOnTopic:String) {
    //Step 1: Start spark
    val conf = new SparkConf().setAppName("Spark_GibbsSampling_LDA").setMaster("local[2]")
    val sc = new SparkContext(conf)
    //Step 2: Read files into HDFS
//    val file: RDD[String] = sc.textFile(filename)
//    file.foreach(println)
//
//    val rawFiles: RDD[(Long, List[String])] = file.map{ line=>
//      {
//        val vs: Array[String] = line.split("\t")
//        val words = vs(1).split(" ").toList
//        (vs(0).toLong, words)
//      }
//    }.filter(_._2.length>0)
//    rawFiles.foreach(println)

    //Step 2: Read words form txt
    //val rawFile: RDD[String] = sc.textFile("/home/hanqing/IdeaProjects/Gibbs_sampling/rawFiles_Results.txt")
    val rawFile: RDD[String] = sc.textFile("/home/hanqing/IdeaProjects/Gibbs_sampling/adlda_gibbs_doc.txt")
    println("1. RawFile: ")
    rawFile.foreach(println)
    //val s: Array[String] = rawFile.collect()
    println()
    val rawFiless: RDD[(String, Long)] = rawFile.zipWithIndex()
    rawFiless.foreach(println)

    val rawFiles: RDD[(Long, List[String])] = rawFiless.map{
      line => //(line._1.toCharArray, line._2.toLong)
      {
        val words = line._1.split(" ").toList
        (line._2.toLong, words)
      }
    }
    println("2. RawFiles: ")
    rawFiles.foreach(println)

    //Step 3: build a dictionary for alphabet: wordIndexMap
    val allWords: List[String] = rawFiles.flatMap {
      t => t._2.distinct
    }.map{t=>(t,1)}.reduceByKey(_+_).map{_._1}.collect().toList.sortWith(_ < _)

    val vSize = allWords.length
    println("vSize: " + vSize)
    println()
    println("AllWords: " + allWords)
    println()

    val wordIndexMap = new mutable.HashMap[String, Int]()
    for (i <- 0 until allWords.length){
      wordIndexMap(allWords(i)) = i
    }

    val bWordIndexMap: mutable.HashMap[String, Int] = wordIndexMap
    println("bWordIndexMap: " + bWordIndexMap.toList)

    //Step 4: compute tf-idf for each word
    //计算每篇文章中每个单词对应的TF-IDF值，得到M个TF-IDF的向量模型
    //val docs: RDD[(Seq[String], Long)] = sc.textFile("adlda_gibbs_doc.txt").map(line => line.split(" ").toSeq
    //val documents: RDD[String] = sc.parallelize(Source.fromFile("CHANGELOG").getLines().filter(_.trim.length > 0).toSeq)
    // .map(_.split(" ").toSeq).zipWithIndex()
    //docs.foreach(println)
    val words: RDD[(immutable.Seq[String], Long)] = rawFiles.map{
      line => {
        val seqwords = line._2.toSeq
        (seqwords, line._1.toLong)
      }
    }

    val hashingTF = new HashingTF(Math.pow(2, 18).toInt)
    //这里将每一行的行号作为doc id，每一行的分词结果生成tf词频向量
    val tf_num_pairs: RDD[(Long, linalg.Vector)] = words.map {
      case (seq, num) =>
        val tf = hashingTF.transform(seq)
        (num, tf)
    }
    tf_num_pairs.cache()
    println("3. TF_Num_pairs: ")
    tf_num_pairs.foreach(println)

    //构建idf model
    val idf: IDFModel = new IDF().fit(tf_num_pairs.values)
    //将tf向量转换成tf-idf向量
    val num_idf_pairs: RDD[(Long, linalg.Vector)] = tf_num_pairs.mapValues(v => idf.transform(v))
    //num_idf_pairs.foreach(println)
    //广播一份tf-idf向量集
    val b_num_idf_pairs = sc.broadcast(num_idf_pairs.collect())

    //计算doc之间余弦相似度
    val docSims: RDD[(Long, Long, Double)] = num_idf_pairs.flatMap {
      case (id1, idf1) =>
        val idfs: Array[(Long, linalg.Vector)] = b_num_idf_pairs.value.filter(_._1 != id1)
        val sv1 = idf1.asInstanceOf[SV]
        import breeze.linalg._
        val bsv1 = new SparseVector[Double](sv1.indices, sv1.values, sv1.size)
        idfs.map {
          case (id2, idf2) =>
            val sv2 = idf2.asInstanceOf[SV]
            val bsv2 = new SparseVector[Double](sv2.indices, sv2.values, sv2.size)
            val cosSim = bsv1.dot(bsv2).asInstanceOf[Double] / (norm(bsv1) * norm(bsv2))
            (id1, id2, cosSim)
        }
    }
    //docSims.foreach(println)

    //Step 5: compute LSH value for each document
    //////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////
    //////////第一个创新点：将相似的文章分到一个数据分区里。(基于局部敏感哈希)//
    //////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////
    println("****************************************************************")
    println("***********基于局部敏感哈希将相似的文章分到同一个分区内**************")
    val sqlContext = new SQLContext(sc)
    //用局部敏感哈希LSH对文本进行处理，再聚类以对语料库进行分区
    val tf_num_pairs_Dataframee = sqlContext.createDataFrame(tf_num_pairs)
    tf_num_pairs_Dataframee.printSchema()
    tf_num_pairs_Dataframee.show()
    tf_num_pairs_Dataframee.select("_1").show()
    tf_num_pairs_Dataframee.select("_2").show()

    val rt = sqlContext.createDataFrame(words)
    rt.show()

    // 修改Dataframe的列名
    val tf_num_pairs_Dataframe = tf_num_pairs_Dataframee.withColumnRenamed("_2", "features").withColumnRenamed("_1", "id")
    println("4. TF_Num_pairs_Dataframe: ")
    tf_num_pairs_Dataframe.printSchema()
    tf_num_pairs_Dataframe.show()

    // org.apache.spark.ml.linalg.Vectors与org.apache.spark.mllib.linalg.Vectors相互转换
    val tf_num_pairs_Dataframe_ML = MLUtils.convertVectorColumnsToML(tf_num_pairs_Dataframe, "features")
    // 计算每一片文章的局部敏感哈希值
    val mh = new MinHashLSH().setNumHashTables(1).setInputCol("features").setOutputCol("hashValues")
    val modell = mh.fit(tf_num_pairs_Dataframe_ML)
    val hash_model = modell.transform(tf_num_pairs_Dataframe_ML)
    println("5. Hash_model: ")
    hash_model.printSchema()
    hash_model.show()

    val spark =SparkSession.builder().getOrCreate()
    import spark.implicits._
    val convertToVec = udf((array: Seq[Vector]) =>
      Vectors.dense(array.flatMap(_.toArray).toArray)
    )
    val hashValue_dataframe = hash_model.withColumn("hashValues", convertToVec($"hashValues"))
    println("6. Hashmodel_Dataframe: ")
    hashValue_dataframe.printSchema()
    hashValue_dataframe.show()

    // 对hashValue进行转化
    val dd = hashValue_dataframe.select("hashValues")
    val ddrdd: RDD[Row] = dd.rdd
    //ddrdd.foreach(println)
    val dss: RDD[linalg.Vector] = dd.rdd.map(row =>
      row.getAs[Vector](0)
    ).map(v => v.apply(0)).map( v =>{
      org.apache.spark.mllib.linalg.Vectors.dense(v)
    })

    //Step 6: KMeans algorithms for corpus
    /////////////////////////////////////////////////////////////////////
    ////////基于每篇文档的哈希值对语料库做KMeans聚类//////////////////////////
    ////////////////////////////////////////////////////////////////////
    ///// partitions_num == numClusters
    println("7. HQ_KMeans: ")
    // 设置主题数==分区数
    val partitions_num = 2
    val numClusters = partitions_num
    val numiterations = 60
    val clusters_model = KMeans.train(dss, numClusters, numiterations)
    //val centers = clusters_model.clusterCenters
    dss.collect().foreach(t=>{println("cluster: " + clusters_model.predict(t))})
    //println("cluster:" + clusters_model.predict(dss))
    //合并
    val rr = rt.join(hashValue_dataframe, rt("_2") === hashValue_dataframe("id"))
    rr.show()

    // 得到聚类结果！
    val mycluster_results = rr.rdd.map(row => {
      val a = row(4)
      val c = a.asInstanceOf[org.apache.spark.ml.linalg.DenseVector].toDense
      val b = org.apache.spark.mllib.linalg.Vectors.fromML(c)
      val e = (row(1),row(0))
      (clusters_model.predict(b), e) //(clusters,(docIndex, wordSeq))
    })
    println("8. Mycluster_Results: ")
    mycluster_results.foreach(println)

    // 用聚类结果进行数据分区
    val mypartition_results: RDD[(Int, (Any, Any))] = mycluster_results.partitionBy(new HQPartitioner(partitions_num))
    println("9. Mypartition_Results: ")
    mypartition_results.foreach(println)
    println("Repartition num: " + mypartition_results.partitions.size)

    //Step 7: init topic assignment for each word in the corpus
    println("10. Init topic assignment for each word in the corpus: ")
    var documents: RDD[(Long, Array[(Int, Int)], Array[Int])] = rawFiles.map { t =>  //t means (docId,words) where words is a List
      val docId: Long = t._1
      val length: Int = t._2.length
      val topicAssignArr: Array[(Int, Int)] = new Array[(Int, Int)](length)
      val nmk = new Array[Int](kTopic)
      for (i <- 0 until length){
        val topic = Random.nextInt(kTopic)
        topicAssignArr(i) = (bWordIndexMap(t._2(i)), topic)
        nmk(topic) = nmk(topic) + 1
      }
      (docId, topicAssignArr, nmk)  //t._1 means docId, t._2 means words
    }.cache()

    //////////////////////////////////////////////////////////////////////////////////////////////////
    mypartition_results.foreach(println)
    val mydocuments: RDD[(Long, Array[(Int, Int)], Array[Int])] = mypartition_results.map(row=>{
      val partitionNum = row._1
      val docId_Words = row._2
      val docId: Long = docId_Words._1.asInstanceOf[Long]
      val words_seq = docId_Words._2.asInstanceOf[Seq[String]]
      val length = words_seq.length
      val topicAssignArr: Array[(Int, Int)] = new Array[(Int, Int)](length)
      val nmk = new Array[Int](kTopic)
      for (i <- 0 until length){
        val topic = Random.nextInt(kTopic)
        topicAssignArr(i) = (bWordIndexMap(words_seq(i)), topic)
        nmk(topic) = nmk(topic) + 1
      }
      (docId, topicAssignArr, nmk)
    })

    print("Doucuments: ")
    documents.foreach(println)
    print("MyDocuments: ")
    mydocuments.foreach(println)

    var wordsTopicReduced = documents.flatMap(t => t._2).map(t => (t,1)).reduceByKey(_ + _).collect().toList
    //update nkv,nk. and broadcast to global
    var nkv: Array[Array[Int]] = updateNKV(wordsTopicReduced, kTopic, vSize)
    var nkvGlobal = sc.broadcast(nkv)
    var nk = updateNK(wordsTopicReduced, kTopic, vSize)
    var nkGlobal = sc.broadcast(nk)
    //nk.foreach(println)
    var len_nk = nk.length
    var len_nkv_a = nkv.length
    var len_nkv_b = nkv(0).length

    var mynkvGlobal = Array.ofDim[Int](len_nkv_a,len_nkv_b)
    var mynkGlobal = new Array[Int](len_nk)

    //Step 8: Use gibbs sampling to infer the topic distribution of doc and estimate the parameter nkv and nk
    //var iterativeInputDocuments: RDD[(Long, Array[(Int, Int)], Array[Int])] = documents.repartition(3)
    var iterativeInputDocuments: RDD[(Long, Array[(Int, Int)], Array[Int])] = mydocuments
    var updatedDocuments: RDD[(Long, Array[(Int, Int)], Array[Int])] = iterativeInputDocuments

    val beforetimes = NowDate()
    //println("beforetimes: " + beforetimes)

    for (iter <- 0 until maxIter) {
      iterativeInputDocuments.persist(StorageLevel.MEMORY_ONLY) //same as cache
      updatedDocuments.persist(StorageLevel.MEMORY_ONLY)   //same as cache
      //broadcast the global data
      nkvGlobal = sc.broadcast(nkv)
      nkGlobal = sc.broadcast(nk)

      //copy
      mynkGlobal = nkGlobal.value.clone()
      for(i <- 0 until len_nkv_a){
        mynkvGlobal(i) = nkvGlobal.value(i).clone()
      }

      // 打印广播前后的全局参数（nkv nk）
      println("Before Broadcast :")
      println("\nEach nkGlobal:")
      for (j <- 0 until nk.length){
        print(nk(j) + " ")
      }
      println("\nEach nkvGlobal:")
      for ( i <- 0 until nkv.length){
        for(j <- 0 until nkv(0).length){
          print(nkv(i)(j) + " ")
        }
        println(' ')
      }

      println("\n\nAfter Broadcast :")
      println("\nEach nkGlobal:")
      for (j <- 0 until nkGlobal.value.length){
        print(nkGlobal.value(j) + " ")
      }
      println("\nEach nkvGlobal:")
      for ( i <- 0 until nkvGlobal.value.length){
        for(j <- 0 until nkvGlobal.value(0).length){
          print(nkvGlobal.value(i)(j) + " ")
        }
        println(' ')
      }

      //val newTopicAssignArr = iterativeInputDocuments

      //对每一个分区进行处理mapPartitions
//      updatedDocuments = iterativeInputDocuments.mapPartitions(
//        (documents: Iterator[(Long, Array[(Int, Int)], Array[Int])]) =>
//        documents.map(input=>{
//          val (newTopicAssignArr, newNmk) = gibbsSampling(input._2,
//            input._3, nkvGlobal.value, nkGlobal.value, kTopic, alpha, vSize, beta)
//          (input._1, newTopicAssignArr, newNmk)-
//        })
//      )

      // 对每个分区里的文章进行处理
      val eachpartition: RDD[((Int, Int), Int)] = iterativeInputDocuments.mapPartitionsWithIndex{ (x, docs)=>{
        val c: Iterator[((Int, Int), Int)] = docs.map(input=>{
          val (newTopicAssignArr, newNmk) = gibbsSampling(input._2, input._3, nkvGlobal.value, nkGlobal.value, kTopic, alpha, vSize, beta)
          (input._1, newTopicAssignArr, newNmk) }).flatMap(t=>t._2).map(t => (t,1))
        c
      }}
      println("eachpartition: ")
      eachpartition.foreach(println)

      val eachpartitions: RDD[(Int, (Array[Array[Int]], Array[Int]))] = eachpartition.mapPartitionsWithIndex((Ind, iter)=> {

        var part_map: mutable.Map[(Int, Int), Int] = scala.collection.mutable.Map[(Int, Int),Int]()
        var part_res: mutable.Map[Int, (Array[Array[Int]], Array[Int])] = scala.collection.mutable.Map[Int,(Array[Array[Int]],Array[Int])]()

        while (iter.hasNext) {
          var a: ((Int, Int), Int) = iter.next()
          if (part_map.contains(a._1))
            part_map(a._1) = part_map(a._1) + a._2
          else
            part_map += a
        }
        var vv: List[((Int, Int), Int)] = part_map.toList
        val nkvl: Array[Array[Int]] = updateNKV(vv, kTopic, vSize)
        val nkl: Array[Int] =  updateNK(vv, kTopic, vSize)
        part_res(Ind) = (nkvl, nkl)
        //然后返回每个分区的part_res 也就是(nkv, nk)
        part_res.iterator
      })
      println("eachpartitions: ")
      eachpartitions.foreach(println)



      println("bn._1:分区编号； bn._2:nkv,nk; bn._2._1:nkv;bn._2._2:nk ")
      val bn: Array[(Int, (Array[Array[Int]], Array[Int]))] = eachpartitions.collect().toArray
      println("bn_length:" + bn.length)

      var nkSum: Array[Int] = new Array[Int](len_nk)
      var nkvSum: Array[Array[Int]] = Array.ofDim[Int](len_nkv_a,len_nkv_b)

      for (i <- 0 until bn.length){
        println("\nEach partition nk:")
        val bb: Array[Int] = bn(i)._2._2
        for (j <- 0 until bb.length){
          nkSum(j) += bb(j)
          print(bb(j) + " ")
        }
        println("\nEach partition nkv:")
        val ba: Array[Array[Int]] = bn(i)._2._1
        for ( i <- 0 until ba.length){
          for(j <- 0 until ba(0).length){
            nkvSum(i)(j) += ba(i)(j)
            print(ba(i)(j) + " ")
          }
          println(' ')
        }
      }

      println("\n\n nkSum:")
      for (i <- 0 until nkSum.length){
        print(nkSum(i) + " ")
      }
      println("\n nkvSum:")
      for ( i <- 0 until nkvSum.length){
        for (j <- 0 until nkvSum(0).length){
          print(nkvSum(i)(j) + " ")
        }
        println(' ')
      }

      println("\nMynkGlobal:")
      for (j <- 0 until mynkGlobal.length){
        print(mynkGlobal(j) + " ")
      }
      println("\nMynkvGlobal:")
      for ( i <- 0 until mynkvGlobal.length){
        for(j <- 0 until mynkvGlobal(0).length){
          print(mynkvGlobal(i)(j) + " ")
        }
        println(' ')
      }

      println("\n\nUpdate nk:")
      for (i <- 0 until len_nk){
//        nk(i) = mynkGlobal(i) + (nkSum(i) - bn.length * mynkGlobal(i))/bn.length
        nk(i) = nkSum(i)
        print(nk(i) + " ")
      }

      println("\nUpdate nkv:")
      for ( i <- 0 until len_nkv_a){
        for (j <- 0 until len_nkv_b){
//          print(nkvSum(i)(j) + "-" + (bn.length-1) + "*" + mynkvGlobal(i)(j) + "=")
//          nkv(i)(j) = mynkvGlobal(i)(j) + (nkvSum(i)(j) - bn.length * mynkvGlobal(i)(j))/bn.length
          nkv(i)(j) = nkvSum(i)(j)
          print(nkv(i)(j) + "  ")
        }
        println(' ')
      }
      nkGlobal.unpersist()
      nkvGlobal.unpersist()

      //对每一篇文章进行处理map
//      updatedDocuments = iterativeInputDocuments.map{
//        case (docId, topicAssignArr, nmk) =>
//          //gibbs sampling
//        val (newTopicAssignArr, newNmk) = gibbsSampling(topicAssignArr,
//          nmk, nkvGlobal.value, nkGlobal.value, kTopic, alpha, vSize, beta)
//        (docId, newTopicAssignArr, newNmk)
//      }

      //output to hdfs for DEBUG
      //updateDocuments.flatMap(t=>t._2).map(t=>(t,1))
      // .saveAsTextFile("hdfs://192.168.0.140:9000/GibbsSampling/out/collect"+iter)
      //wordsTopicReduced = updatedDocuments.flatMap(t => t._2).map(t => (t,1)).reduceByKey(_+_).collect().toList
      //terativeInputDocuments = updatedDocuments
      //update nkv,nk
      //nkv = updateNKV(wordsTopicReduced, kTopic, vSize)
      //nk =  updateNK(wordsTopicReduced, kTopic, vSize)
      //nkGlobal.value.foreach(println)
      //打印进行一次采样后的主题信息情况
      println(topicsInfo(nkvGlobal.value, allWords, kTopic, vSize, topKwordsForDebug))
      println("iteration " + iter + " finished")

      updatedDocuments.mapPartitionsWithIndex{(x,iter)=>{
        var result = List[String]()
        //x.map(t=>t)
        result.::(x).iterator
      }}
//      //restart spark to optimize the memory
//      if (iter % 20 ==0){
//        // save RDD temporally
//        var pathDocument1 = ""
//        var pathDocument2 = ""
//        if(remote==true){
//          pathDocument1 = "hdfs://192.168.0.140:9000/GibbsSampling/out/gibbsLDAtmp"
//          pathDocument2 = "hdfs://192.168.0.140:9000/GibbsSampling/out/gibbsLDAtmp2"
//        }else{
//          pathDocument1 = "out/gibbsLDAtmp"
//          pathDocument2 = "out/gibbsLDAtmp2"
//        }
//        var storedDocuments1 = iterativeInputDocuments
//        storedDocuments1.persist(StorageLevel.DISK_ONLY)
//        storedDocuments1.saveAsObjectFile(pathDocument1)
//        var storedDocuments2 = updatedDocuments
//        storedDocuments2.persist(StorageLevel.DISK_ONLY)
//        storedDocuments2.saveAsObjectFile(pathDocument2)
//
//        //restart Spark to solve the memory leak problem
//        //sc = restartSpark(sc, scMaster, remote)
//        // As the restart of spark, all of RDD are cleared
//        // we need to read files in order to rebuild RDD
//        iterativeInputDocuments = sc.objectFile(pathDocument1)
//        updatedDocuments = sc.objectFile(pathDocument2)
//      }
    }

    println()
    println("beforetimes: " + beforetimes)
    println()
    val behindtimes = NowDate()
    println("behindtimes: " + behindtimes)

    //Step 9: save the result in HDFS()
    //(result part1:topic distribution of doc, result part2:top word in each topic)
    var resultDocuments = iterativeInputDocuments
    saveDocTopicDist(resultDocuments, pathTopicDistOnDoc)
    saveWordDistTopic(sc, nkv, nk, allWords, vSize, topKwordsForDebug, pathWordDistOnTopic)
  }

  def main(args: Array[String]): Unit = {
    println("hello gibbs sampling!")
    //val fileName = "gibbs_doc.txt"
    val kTopic = 2
    val alpha = 2
    val beta = 0.01
    val maxIter = 10
    val remote = true
    val topKwordForDebug = 10
    var pathTopicDistOnDoc = ""
    var pathWordDistOnTopic = ""
    pathTopicDistOnDoc = "out/topicDistOnDoc"
    pathWordDistOnTopic = "out/wordDistOnTopic"
//    if(remote == true){
//      pathTopicDistOnDoc = "hdfs://192.168.0.140:9000/GibbsSampling/out/topivDistOnDoc"
//      pathWordDistOnTopic = "hdfs://192.168.0.140:9000/GibbsSampling/out/wordDictOnTopic"
//
//    } else{
//      pathTopicDistOnDoc = "out/topivDistOnDoc"
//      pathWordDistOnTopic = "out/wordDictOnTopic"
//    }
    lda(kTopic, alpha, beta, maxIter, remote, topKwordForDebug, pathTopicDistOnDoc, pathWordDistOnTopic)

  }

}



