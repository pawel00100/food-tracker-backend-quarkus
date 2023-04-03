package org.acme.endpoints

import org.acme.model.Dish
import org.acme.services.DishService
import java.time.Instant
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/dish")
class DishResource(val dishService: DishService) {

    @POST
    fun create(dish: DishWeb): DishPostResponse {
        val id = dishService.create(dish.asDish())
        return DishPostResponse(id)
    }

    @GET
    @Path("/id/{id}")
    fun get(@PathParam("id") id: Long) = dishService.getById(id)?.asDishWeb()

    @GET
    fun getAll() = dishService.getAll().map { DishWeb(it) }

    @DELETE
    fun delete(dish: DishWeb): Response {
        dishService.delete(dish.asDish())
        return OK
    }

    @DELETE
    @Path("/id/{id}")
    fun deleteDish(@PathParam("id") id: Long): Response {
        dishService.deleteById(id)
        return OK
    }

    @PUT
    @Path("/id/{id}")
    fun putDish(@PathParam("id") id: Long, reveivedDish: DishWeb): Response {
        val dish = reveivedDish.asDish().withId(id)
        dishService.update(dish)
        return OK
    }

    @PUT
    fun putDish(dish: DishWeb): Response {
        dishService.update(dish.asDish())
        return OK
    }
}


data class DishWeb(
    val id: Long?,
    val name: String,
    val kcal: Int?,
    val kcalExpression: String?,
    val created: Long,
    val barcode: Long?
) {
    constructor(dish: Dish) : this(
        dish.id,
        dish.name,
        dish.kcal,
        dish.kcalExpression,
        dish.created.epochSecond,
        dish.barcode
    )

    fun asDish() = Dish(id, name, kcal, kcalExpression, Instant.ofEpochSecond(created), barcode)
}

fun Dish.asDishWeb(): DishWeb = DishWeb(this)

data class DishPostResponse(val id: Long?)

