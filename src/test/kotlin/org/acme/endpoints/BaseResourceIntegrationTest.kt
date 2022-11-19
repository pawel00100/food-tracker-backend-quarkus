package org.acme.endpoints

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.`when`
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class BaseResourceIntegrationTest {
    @Test
    fun testCheckEndpoint() {
        `when`().get("/check")
            .then()
            .statusCode(200)
    }

    @Test
    fun testHelloEndpoint() {
        `when`().get("/check/helloworld")
            .then()
            .statusCode(200)
            .body(`is`("hello"))
    }

}