package controllers

import org.bson.json.JsonObject
import org.mongodb.scala.*
import play.api.*
import play.api.mvc.*

import javax.inject.*
import scala.concurrent.ExecutionContext

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

    val document = Document("name" -> "Café Con Leche",
      "contact" -> Document("phone" -> "228-555-0149",
        "email" -> "cafeconleche@example.com",
        "location" -> Seq(-73.92502, 40.8279556)),
      "stars" -> 3, "categories" -> Seq("Bakery", "Coffee", "Pastries"))



    collection.insertOne(document).toFuture().map(_ => Ok(""))

  }

  def insertJson() = Action.async { implicit request: Request[AnyContent] =>

    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    val mongoClient = MongoClient("mongodb://localhost:27017")
    val database: MongoDatabase = mongoClient.getDatabase("test")
    val collection: MongoCollection[JsonObject] = database.getCollection("json")



    collection.insertOne(new JsonObject("""{"name":"John", "age":30, "car":"abc", "tires":{"fl":"good","fr":"good","rl":"good","rr":"gone"}}""")).toFuture().map(_ => Ok(""))
  }

}
