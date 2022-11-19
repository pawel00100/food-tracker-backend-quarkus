package org.acme.endpoints

import io.quarkus.test.TestTransaction
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Test

@QuarkusTest
class MealResourceIntegrationTest {
    val utils = IntegrationTestUtils("/meal")

    @Test
    @TestTransaction
    internal fun `meals are saved, meals are returned and are deletable`() {
        val returnedId = utils.`post object and return its id`(MealWeb(null, "dd", 200, 1_600_000_000))

        utils.`get object by id and check if field matches`(returnedId, "name", "dd")

        utils.deleteObject(returnedId)

        utils.`get all objects and assert object is not found`("name", "dd")
    }


    @Test
    @TestTransaction
    internal fun `meals are updated`() {
        utils.`get all objects and assert object is not found`("name", "aa")
        utils.`get all objects and assert object is not found`("name", "bb")


        val id = utils.`post object and return its id`(MealWeb(null, "aa", 200, 1_600_000_000))
        utils.`get all objects and assert object is found`("name", "aa") //just for verification meal gets inserted
        utils.putObject(MealWeb(id, "bb", 250, 1_600_000_000))

        utils.`get all objects and assert object is found`("name", "bb")

        utils.`get all objects and assert object is not found`("name", "aa")

        utils.deleteObject(id) // cleanup
    }


    @Test
    @TestTransaction
    internal fun `meals are filtered`() {
        val id1 = utils.`post object and return its id`(MealWeb(null, "aa", 200, 1_600_000_000))
        val id2 = utils.`post object and return its id`(MealWeb(null, "bb", 200, 1_600_001_000))
        utils.`get all objects and assert object is found`("name", "bb") //just for verification meal gets inserted


        utils.`get objects in range, assert object is found`("name", "aa", 1_600_000_000, 1_600_000_001)
        utils.`get objects in range, assert object is not found`("name", "bb", 1_600_000_000, 1_600_000_001)

        utils.deleteObjects(id1, id2) // cleanup
    }
}