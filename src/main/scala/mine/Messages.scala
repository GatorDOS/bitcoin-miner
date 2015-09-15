package mine

/**
 * @author mebin
 */

case class Result(bitcoin: List[String])
case object BackendRegistration
final case class JobFailed(reason: String, job: Array[Int])
