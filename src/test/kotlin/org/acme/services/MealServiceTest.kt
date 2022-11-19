package org.acme.services

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.acme.dao.MealDAO
import org.acme.model.Meal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

internal class MealServiceTest {
    private var mealDAO: MealDAO = mockk()

    var mealService = MealService(mealDAO)

    @BeforeEach
    internal fun setUp() {
        every { mealDAO.create(any()) } returns 1L
        justRun { mealDAO.delete(any()) }
        justRun { mealDAO.deleteById(any()) }
        every { mealDAO.getAll() } returns listOf(
            Meal(1, "aa", 200, Instant.ofEpochSecond(1_600_000_000), false),
            Meal(2, "bb", 500, Instant.ofEpochSecond(1_600_001_000), false)
        )
        every { mealDAO.getFiltered(any(), any()) } returns listOf(
            Meal(1, "aa", 200, Instant.ofEpochSecond(1_600_000_000), false),
        )
    }

    @Test
    fun getAll() {
        val meals = listOf(
            Meal(null, "aa", 200, Instant.now()!!, false),
            Meal(null, "bb", 500, Instant.now()!!, false)
        )

        meals.forEach { mealService.create(it) }

        val response = mealService.getAll()

        assertEquals(meals.map { it.name }, response.map { it.name })
    }

    @Test
    fun getFiltered() {
        val meals = listOf(
            Meal(null, "aa", 200, Instant.now()!!, false),
        )

        meals.forEach { mealService.create(it) }

        val response = mealService.getFiltered(Instant.ofEpochSecond(1_600_000_000), Instant.ofEpochSecond(1_600_000_001))

        assertEquals(meals.map { it.name }, response.map { it.name })
    }

    @Test
    fun create() {
        val meal = Meal(null, "cc", 200, Instant.now()!!, false)

        mealService.create(meal)
    }

    @Test
    fun delete() {
        val meal = Meal(1, "aa", 200, Instant.ofEpochSecond(1_600_000_000)!!, false)

        mealService.delete(meal)
    }

    @Test
    fun deleteById() { //TODO: add checking with wrong id
        mealService.deleteById(1)
    }


    //TODO: add checking if DAO methods are called
}