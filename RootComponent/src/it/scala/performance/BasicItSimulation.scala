package performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicItSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8090")                                                                            // Here is the root for all relative URLs
    .acceptHeader("text/plain,application/json,text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // val init = scenario("Create board")
  //   .exec(http("request_1").post("/board/create"))
  //   .pause(2)

  val scn = scenario("Move and save")
    .exec(
      http("request_1")
        .put("/board/1/move")
        .header("content-type", "application/json")
        .body(StringBody(""" {
          "from": "b1",
          "to": "c3"
        } """))
        .asJson
    )
    .pause(5)
    .exec(
      http("request_2")
        .put("/board/1/move")
        .header("content-type", "application/json")
        .body(StringBody(""" {
          "from": "b8",
          "to": "c6"
        } """))
        .asJson
    )
    .pause(5)
    .exec(
      http("request_1")
        .put("/board/1/move")
        .header("content-type", "application/json")
        .body(StringBody(""" {
          "from": "c3",
          "to": "b1"
        } """))
        .asJson
    )
    .pause(5)
    .exec(
      http("request_2")
        .put("/board/1/move")
        .header("content-type", "application/json")
        .body(StringBody(""" {
          "from": "c6",
          "to": "b8"
        } """))
        .asJson
    )
    .pause(5)
    // .exec(http("request_3").get("/save"))
    // .pause(5)

  setUp(scn.inject(atOnceUsers(10000)).protocols(httpProtocol))
  // setUp(scn.inject(constantUsersPerSec(2000).during(10))).protocols(httpProtocol);
}
