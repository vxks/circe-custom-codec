import io.circe.yaml._

import scala.io.{BufferedSource, Source}
import scala.util.Try

object App {

  def main(args: Array[String]): Unit = {
    val fileSource: BufferedSource = Source.fromResource("plans.yml")
    val stringYamlTry: Try[String] = Try(fileSource.mkString)
    fileSource.close()

    val stringYaml = stringYamlTry.get
    val json       = parser.parse(stringYaml).fold(throw _, identity)
    val jobs       = json.as[GitLabJobs].fold(throw _, identity)

    import io.circe.syntax._
    val backToYaml = printer.print(jobs.asJson)
    println(backToYaml)
  }

}
