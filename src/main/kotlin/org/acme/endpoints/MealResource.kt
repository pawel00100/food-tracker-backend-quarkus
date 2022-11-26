package org.acme.endpoints

import org.acme.model.Meal
import org.acme.services.MealService
import java.time.Instant
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/meal")
class MealResource(val mealService: MealService) {

    @POST
    fun create(meal: MealWeb): MealPostResponse {
        val id = mealService.create(meal.asMeal())
        return MealPostResponse(id)
    }

    @GET
    @Path("/id/{id}")
    fun get(@PathParam("id") id: Long) = mealService.getById(id)?.asMealWeb()

    @GET
    fun getAll() = mealService.getAll().map { MealWeb(it) }

    @GET
    @Path("/filtered")
    fun getFiltered(@QueryParam("from") from: Long, @QueryParam("to") to: Long) =
        mealService.getFiltered(Instant.ofEpochSecond(from), Instant.ofEpochSecond(to)).map { MealWeb(it) }

    @DELETE
    fun delete(meal: MealWeb): Response {
        mealService.delete(meal.asMeal())
        return OK
    }

    @DELETE
    @Path("/id/{id}")
    fun deleteMeal(@PathParam("id") id: Long): Response {
        mealService.deleteById(id)
        return OK
    }

    @PUT
    @Path("/id/{id}")
    fun putMeal(@PathParam("id") id: Long, reveivedMeal: MealWeb): Response {
        val meal = reveivedMeal.asMeal().withId(id)
        mealService.update(meal)
        return OK
    }

    @PUT
    fun putMeal(meal: MealWeb): Response {
        mealService.update(meal.asMeal())
        return OK
    }
}


data class MealWeb(val id: Long?, val name: String, val kcal: Int?, val kcalExpression: String?, val date: Long, val exercise: Boolean = false) {
    constructor(meal: Meal) : this(meal.id, meal.name, meal.kcal, meal.kcalExpression, meal.date.epochSecond, meal.exercise)

    fun asMeal() = Meal(id, name, kcal, kcalExpression, Instant.ofEpochSecond(date), exercise)
}

fun Meal.asMealWeb(): MealWeb = MealWeb(this)

data class MealPostResponse(val id: Long?)

