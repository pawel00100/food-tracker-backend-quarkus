package org.acme.endpoints

import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.Matchers.*

class IntegrationTestUtils(val baseEndpoint: String) {

    fun <T> `get object by id and check if field matches`(id: Long, fieldName: String, fieldValue: T) {
        When {
            get(baseEndpoint + "/id/${id}")
        } Then {
            statusCode(200)
            body(fieldName, equalTo(fieldValue))
        }
    }

    fun <T> `get all objects and assert object is found`(fieldName: String, fieldValue: T) {
        When {
            get(baseEndpoint)
        } Then {
            statusCode(200)
//            body("$fieldName.any { it == '$fieldValue' }", Is.`is`(true)
            body(fieldName, hasItem(fieldValue))

        }
    }

    fun <T> `get all objects and assert object is not found`(fieldName: String, fieldValue: T) {
        When {
            get(baseEndpoint)
        } Then {
            statusCode(200)
//            body("$fieldName.any { it == '$fieldValue' }", Is.`is`(false))
            body(fieldName, not(hasItem(fieldValue)))

        }
    }

    fun <T> `get objects in range, assert object is found`(fieldName: String, fieldValue: T, rangeFrom: Long, rangeTo: Long) {
        Given {
            param("from", rangeFrom)
            param("to", rangeTo)
        } When {
            get(baseEndpoint + "/filtered")
        } Then {
            statusCode(200)
            body(fieldName, hasItem(fieldValue))
        }
    }

    fun <T> `get objects in range, assert object is not found`(fieldName: String, fieldValue: T, rangeFrom: Long, rangeTo: Long) {
        Given {
            param("from", rangeFrom)
            param("to", rangeTo)
        } When {
            get(baseEndpoint + "/filtered")
        } Then {
            statusCode(200)
            body(fieldName, not(hasItem(fieldValue)))
        }
    }

    inline fun <reified T> getList(): List<T> {
        return When {
            get(baseEndpoint)
        } Then {
            statusCode(200)
        } Extract {
            body().jsonPath().getList(".", T::class.java)
        }
    }

    fun `post object and return its id`(obj: Any): Long {
        val returnedId: Int = Given {
            contentType(ContentType.JSON)
            body(obj)
        } When {
            post(baseEndpoint)
        } Then {
            statusCode(200)
        } Extract { path("id") }
        return returnedId.toLong()
    }

    fun putObject(obj: Any) {
        Given {
            contentType(ContentType.JSON)
            body(obj)
        } When {
            put(baseEndpoint)
        } Then {
            statusCode(200)
        }
    }

    fun deleteObject(id: Long) {
        Given {
            contentType(ContentType.JSON)
        } When {
            delete(baseEndpoint + "/id/$id")
        } Then {
            statusCode(200)
        }
    }

    fun deleteObjects(vararg ids: Long) {
        ids.forEach { deleteObject(it) }
    }
}