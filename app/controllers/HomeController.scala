package controllers

import org.bson.json.{JsonObject, JsonWriterSettings}
import org.mongodb.scala.*
import org.mongodb.scala.bson.{BsonDateTime, BsonTimestamp}
import play.api.*
import play.api.libs.functional.syntax.unlift
import play.api.libs.json.{Json, OWrites}
import play.api.mvc.*

import java.time.Instant
import javax.inject.*
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def insertDocument() = Action.async { implicit request: Request[AnyContent] =>

    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    val mongoClient = MongoClient("mongodb://localhost:27017")
    val database: MongoDatabase = mongoClient.getDatabase("test")
    val collection: MongoCollection[Document] = database.getCollection("restaurants")

    val document = Document(
      "name" -> "Café Con Leche",
      "contact" -> Document(
          "phone" -> "228-555-0149",
          "email" -> "cafeconleche@example.com",
          "location" -> Seq(-73.92502, 40.8279556)
      ),
      "stars" -> 3,
      "categories" -> Seq("Bakery", "Coffee", "Pastries"),
      "lastUpdated" -> BsonDateTime(Instant.now().toEpochMilli)
    )

    collection.insertOne(document).toFuture().map(_ => Ok(""))
  }

  def insertJson() = Action.async { implicit request: Request[AnyContent] =>

    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    val mongoClient = MongoClient("mongodb://localhost:27017")
    val database: MongoDatabase = mongoClient.getDatabase("test")
    val collection: MongoCollection[JsonObject] = database.getCollection("json")



    collection.insertOne(new JsonObject("""{"name":"John", "age":30, "car":"abc", "lastUpdated": ISODate("2019-12-21T00:00:00.000Z"), "tires":{"fl":"good","fr":"good","rl":"good","rr":"gone"}}""")).toFuture().map(_ => Ok(""))

  }

  def insertCaseClass() = Action.async { implicit request: Request[AnyContent] =>


    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    val mongoClient = MongoClient("mongodb://localhost:27017")
    val database: MongoDatabase = mongoClient.getDatabase("test")
    val collection: MongoCollection[JsonObject] = database.getCollection("case")

    case class Resident(name: String, age: Int, lastUpdated: Instant)
    import play.api.libs.json._

    //    implicit val writer = new Writes[Instant] {
    //      def writes(foo: Instant): JsValue = {
    //        JsString(s"""ISODate("${foo.toString}")""")
    //      }
    //    }

    val resident = Resident(name = "AB", age = 100, Instant.now())

    implicit val instantWrite: Writes[Instant] = (date: Instant) => Json.obj(
      "$date" -> date.toString
    )
    implicit val residentWrite = Json.writes[Resident]

    val residentJson = Json.toJson(resident).toString

    val bson = new JsonObject(residentJson)

    println("====" + bson)

    collection.insertOne(bson).toFuture().map(_ => Ok(""))
  }
}
