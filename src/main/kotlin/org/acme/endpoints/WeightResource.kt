package org.acme.endpoints

import org.acme.model.Weight
import org.acme.services.WeightService
import java.time.Instant
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Path("/weight")
class WeightResource(val weightService: WeightService) {

    @POST
    fun create(weight: WeightWeb): WeightPostResponse {
        val id = weightService.create(weight.asWeight())
        return WeightPostResponse(id)
    }

    @GET
    @Path("/id/{id}")
    fun get(@PathParam("id") id: Long) = weightService.getById(id)?.asWeightWeb()

    @GET
    fun getAll() = weightService.getAll().map { WeightWeb(it) }

    @GET
    @Path("/filtered")
    fun getFiltered(@QueryParam("from") from: Long, @QueryParam("to") to: Long) =
        weightService.getFiltered(Instant.ofEpochSecond(from), Instant.ofEpochSecond(to)).map { WeightWeb(it) }

    @DELETE
    fun delete(weight: WeightWeb): Response {
        weightService.delete(weight.asWeight())
        return OK
    }

    @DELETE
    @Path("/id/{id}")
    fun deleteMeal(@PathParam("id") id: Long): Response {
        weightService.deleteById(id)
        return OK
    }

    @PUT
    @Path("/id/{id}")
    fun putMeal(@PathParam("id") id: Long, receivedWeight: WeightWeb): Response {
        val weight = receivedWeight.asWeight().withId(id)
        weightService.update(weight)
        return OK
    }

    @PUT
    fun putMeal(weight: WeightWeb): Response {
        weightService.update(weight.asWeight())
        return OK
    }
}


data class WeightWeb(val id: Long?, val measurement: Float, val date: Long) {
    constructor(weight: Weight) : this(weight.id, weight.measurement, weight.date.epochSecond)

    fun asWeight() = Weight(id, measurement, Instant.ofEpochSecond(date))
}

fun Weight.asWeightWeb(): WeightWeb = WeightWeb(this)

data class WeightPostResponse(val id: Long?) //TODO: make shared instance

