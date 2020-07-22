package spark

import com.hurence.historian.model.ChunkRecordV0
import com.hurence.historian.spark.ml.Chunkyfier
import com.hurence.historian.spark.sql
import com.hurence.historian.spark.sql.reader.MeasuresReaderType
import com.hurence.historian.spark.sql.reader.ReaderFactory
import com.hurence.historian.spark.sql.writer.{WriterFactory, WriterType}
import org.apache.spark.sql.SparkSession

object JustSimpleTest {

  def main(args: Array[String]): Unit = {


    val origpath = "/home/nemsi/Work/historian/loader/src/test/resources/it-data-4metrics.csv.gz"

    val spark = SparkSession.builder
      .config("spark.master", "local[1]")
      .getOrCreate()

    import spark.implicits._


    val dot = spark.read.format("csv").option("inferSchema", "true").option("header", "true").load(origpath)

    val reader = ReaderFactory.getMeasuresReader(MeasuresReaderType.GENERIC_CSV)
    val measuresDS = reader.read(sql.Options(
      origpath,
      Map(
        "inferSchema" -> "true",
        "delimiter" -> ",",
        "header" -> "true",
        "nameField" -> "metric_name",
        "timestampField" -> "timestamp",
        "timestampDateFormat" -> "ms",
        "valueField" -> "value",
        "tagsFields" -> "metric_id,warn,crit"
      )))
    //measuresDS.show(20,200)

    val chunkyfier = new Chunkyfier().setGroupByCols(Array("name", "tags.metric_id"))
    val chunksDS = chunkyfier.transform(measuresDS).as[ChunkRecordV0]

    chunksDS.show()

    val writer = WriterFactory.getChunksWriter(WriterType.SOLR)
    writer.write(sql.Options("historian", Map(
      "zkhost" -> "localhost:9983",
      "collection" -> "historian",
      "tag_names" -> "metric_id,warn,crit"
    )), chunksDS)


    spark.stop()
  }
}