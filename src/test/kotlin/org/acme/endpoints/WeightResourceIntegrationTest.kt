package org.acme.endpoints

import io.quarkus.test.TestTransaction
import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@QuarkusTest
@TestTransaction
class WeightResourceIntegrationTest {

    val utils = IntegrationTestUtils("/weight")

    @Test
    @TestTransaction
    internal fun `weights are saved, meals are returned and are deletable`() {
        val id = utils.`post object and return its id`(WeightWeb(null, 80F, 1_600_000_000))

        utils.`get object by id and check if field matches`(id, "date", 1_600_000_000)

        utils.deleteObject(id)

        utils.`get all objects and assert object is not found`("measurement", 80F)
    }


    @Test
    @TestTransaction
    internal fun `weights are updated`() {
        var result = utils.getList<WeightWeb>()
        assertTrue(result.isEmpty())

        val id = utils.`post object and return its id`(WeightWeb(null, 80.5F, 1_600_000_000))
        utils.putObject(WeightWeb(id, 60.5F, 1_600_000_001))

        result = utils.getList()

        assertThat(result).containsExactly(WeightWeb(id, 60.5F, 1_600_000_001))

        utils.deleteObject(id) //cleanup
    }


    @Test
    @TestTransaction
    internal fun `weights are filtered`() {
        val id1 = utils.`post object and return its id`(WeightWeb(null, 80F, 1_600_000_000))
        val id2 = utils.`post object and return its id`(WeightWeb(null, 81F, 1_600_001_000))
        utils.`get all objects and assert object is found`("measurement", 81F) //just for verification meal gets inserted


        utils.`get objects in range, assert object is found`("measurement", 80F, 1_600_000_000, 1_600_000_001)
        utils.`get objects in range, assert object is not found`("measurement", 81F, 1_600_000_000, 1_600_000_001)

        utils.deleteObjects(id1, id2) //cleanup
    }
}