package org.example;



import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Path("/")
public class StaticResource {

    @GET
    @Path("/favicon.ico")
    @Produces("image/x-icon")
    public Response getFavicon() {
        InputStream in = getClass().getResourceAsStream("/assets/favicon.ico");
        if (in == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(in).build();
    }
}
