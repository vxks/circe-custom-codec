import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Decoder, Encoder, Json, JsonObject}

case class GitLabJobs(
  jobsMap: Map[String, GitLabJob]
)

object GitLabJobs {

  // https://docs.gitlab.com/ee/ci/yaml/
  private val gitlabTopLevelKeywords = List(
    "include",
    "variables",
    "workflow",
    "default",
    "stages"
  )

  private def prepareTemplateMapJson(json: Json): Json =
    json.mapObject { jsonObject =>
      val templatesOnly = gitlabTopLevelKeywords
        .foldLeft(jsonObject)((obj, key) => obj.remove(key)) // a bit redundant
        .filterKeys(_.startsWith("."))
        .toMap
      JsonObject.fromMap(templatesOnly)
    }

  implicit val dec: Decoder[GitLabJobs] =
    Decoder[Map[String, GitLabJob]].prepare(_.withFocus(prepareTemplateMapJson)).map(GitLabJobs.apply)

  implicit val enc: Encoder[GitLabJobs] =
    deriveEncoder[GitLabJobs].mapJson { json =>
      (json \\ "jobsMap").headOption
        .getOrElse(Json.Null) // should never be the case
    }

}
