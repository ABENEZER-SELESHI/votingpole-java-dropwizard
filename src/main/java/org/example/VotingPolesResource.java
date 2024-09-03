package org.example;

import io.dropwizard.auth.Auth;
import org.example.auth.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/votingpoles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VotingPolesResource {

    private static final Logger logger = LoggerFactory.getLogger(VotingPolesResource.class);
    private final VotingPolesService service;

    //  call service class
    public VotingPolesResource(VotingPolesService service) {
        this.service = service;
    }

    //  get all method: done
    @GET
    @PermitAll
    public List<VotingPoles> getAllVotingPoles(@Auth User user) {
        return service.getAllVotingPoles();
    }

    //  get by id method: done
    @GET
    @Path("/{id}")
    @PermitAll
    public Response getVotingPoleById(@PathParam("id") String id, @Auth User user) {
        VotingPoles votingPole = service.getVotingPoleById(id);
        if (votingPole == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(votingPole).build();
    }


    //  post method: done
    @POST
    @RolesAllowed({"ADMIN"})
    public Response createVotingPole(VotingPoles votingPole, @Auth User user) {
        service.createVotingPole(votingPole);
        return Response.status(Response.Status.CREATED).entity(votingPole).build();
    }

    //    put method : not working after auth system
    @PUT
    @Path("/{id}/options/{optionId}")
    public Response incrementOptionCount(@PathParam("id") String id, @PathParam("optionId") String optionId) {

        if (id == null || id.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("VotingPole ID cannot be null or empty").build();
        }
        if (optionId == null || optionId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Option ID cannot be null or empty").build();
        }

        logger.info("Incrementing option count for VotingPole ID: {} and Option ID: {}", id, optionId);
        service.incrementOptionCount(id, optionId);
        logger.info("Option count incremented and saved for VotingPole ID: {} and Option ID: {}", id, optionId);

        return Response.ok("Option count incremented successfully").build();
    }







    //  delete method: finished
    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADMIN"})
    public Response deleteVotingPole(@PathParam("id") String id, @Auth User user) {
        boolean deleted = service.deleteVotingPole(id);
        if (deleted) {
            return Response.ok("VotingPole deleted successfully").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("VotingPole not found").build();
        }
    }

}
