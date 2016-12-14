package com.mikolajprzybysz.busroute.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mikolajprzybysz.busroute.logic.repository.RoutingRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("direct")
public class DirectResource {

    @Inject
    private RoutingRepository repo;

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response direct(
        @QueryParam("dep_sid") int depSid,
        @QueryParam("arr_sid") int arrSid
    ) {
        Boolean isDirectBusRoute = repo.isDirectBusRoute(depSid, arrSid);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("dep_sid", depSid);
        node.put("arr_sid", arrSid);
        node.put("direct_bus_route", isDirectBusRoute);

        String json = node.toString();

        return Response.ok(json).build();
    }
}

