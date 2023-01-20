import io.circe._
import io.circe.generic.semiauto._

case class GitLabJob(
  `extends`: Option[String],
  needs: Option[String],
  before_script: Option[String],
  script: List[String],
  vars: Option[Map[String, Json]]
)

object GitLabJob {
  private def prepareTemplateJson(json: Json): Json = {
    val gitLabJobKeys = classOf[GitLabJob].getDeclaredFields.map(_.getName)
    json.mapObject { jsonObject =>
      val knownKeysObject   = jsonObject.filterKeys(gitLabJobKeys.contains)
      val unknownKeysObject = jsonObject.filterKeys(!gitLabJobKeys.contains(_))

      if (unknownKeysObject.isEmpty) knownKeysObject
      else knownKeysObject.add("vars", Json.fromFields(unknownKeysObject.toList))
    }
  }

  implicit val enc: Encoder[GitLabJob] = deriveEncoder[GitLabJob].mapJson(_.deepDropNullValues)
  implicit val dec: Decoder[GitLabJob] = deriveDecoder[GitLabJob].prepare(_.withFocus(prepareTemplateJson))
}
