package cl.duoc.cordillera.infrastructure.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("/")
public class RootRedirectResource {

    @GET
    public Response redirect() {
        return Response.temporaryRedirect(URI.create("/swagger-ui")).build();
    }
}
