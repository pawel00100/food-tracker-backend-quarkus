package org.acme.endpoints

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/check")
class CheckResource {

    @GET
    fun hello() = Response.ok().build()

    @GET
    @Path("/helloworld")
    @Produces(MediaType.TEXT_PLAIN)
    fun helloworld() = "hello"
}

@Path("")
class BaseResource {

    @GET
    fun hello() = Response.ok().build()

    @GET
    @Path("/helloworld")
    @Produces(MediaType.TEXT_PLAIN)
    fun helloworld() = "hello"
}


