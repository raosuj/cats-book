package sandbox

object Main extends App {
  import JsonWriterInstances._
  import JsonSyntax._
  Json.toJson(Person("Dave", "dave@example.com"))
  val person = Person("dave", "a").toJson

  print(person)
}

// type class
sealed trait Json
final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
final case class JsNumber(get: Double) extends Json
case object JsNull extends Json

trait JsonWriter[A] {
  def write(value: A) : Json
}

final case class Person(name: String, email: String)

// type class instance
object JsonWriterInstances {
  implicit val stringWriter: JsonWriter[String] = (value: String) => JsString(value)

  implicit val personWriter : JsonWriter[Person] = new JsonWriter[Person] {
    override def write(value: Person): Json = JsObject(Map("name" -> JsString(value.name), "email" -> JsString(value.email)))
  }
}

// interface objects
object Json {
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = w.write(value)
}

//Interface Syntax (“type enrichment” or “pimping”)
object JsonSyntax {
  implicit class JsonWriterOps[A](value: A) {
    def toJson(implicit w: JsonWriter[A]): Json = w.write(value)
  }
}
