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

  val scn = scenario("Create board and save")
    .exec(http("request_1").post("/board/create"))
    .pause(5)
    .exec(http("request_3").get("/save"))
    .pause(5)

  setUp(scn.inject(atOnceUsers(10000)).protocols(httpProtocol))
}
