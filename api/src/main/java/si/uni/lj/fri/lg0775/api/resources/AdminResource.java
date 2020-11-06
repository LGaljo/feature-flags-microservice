package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.bussines_beans.SeedBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("admin")
public class AdminResource {
    @Inject
    private SeedBean seedBean;

    @POST
    @Path("clear")
    public Response clearDatabase() {
        seedBean.clearDatabase();
        return Response.ok().build();
    }

    @POST
    @Path("seed")
    public Response seedDatabase() {
        seedBean.onInit();
        return Response.ok().build();
    }
}
