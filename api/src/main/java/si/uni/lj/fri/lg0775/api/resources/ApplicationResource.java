package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.beans.ApplicationBean;
import si.uni.lj.fri.lg0775.services.dtos.NewAppDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("applications")
public class ApplicationResource {
    @Inject
    private ApplicationBean applicationBean;

    @GET
    public Response getAll() {
        return Response
                .ok()
                .entity(applicationBean.getAll())
                .build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") String id) {
        Long lid = Long.decode(id);
        return Response
                .ok()
                .entity(applicationBean.find(lid))
                .build();
    }

    @POST
    public Response create(NewAppDto name) {
        return Response
                .status(Response.Status.CREATED)
                .entity(applicationBean.createApp(name.getName()))
                .build();
    }

    @DELETE
    public Response delete(@QueryParam("app_id") Long id) {
        applicationBean.removeApp(id);

        return Response
                .status(Response.Status.OK)
                .build();
    }
}
