package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.beans.ApplicationBean;
import si.uni.lj.fri.lg0775.services.beans.FlagBean;
import si.uni.lj.fri.lg0775.services.dtos.FlagDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("applications")
public class ApplicationResource {
    @Inject
    ApplicationBean applicationBean;

    @Inject
    private FlagBean flagBean;

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
    public Response createApp(@QueryParam("name") String appName) {
        return Response
                .status(Response.Status.CREATED)
                .entity(applicationBean.createApp(appName))
                .build();
    }

    @GET
    @Path("flags")
    public Response getFlags(@QueryParam("app_id") Long appId) {
        return Response
                .ok()
                .entity(applicationBean.getFlagsDto(appId))
                .build();
    }

    @GET
    @Path("users")
    public Response getUsers(@QueryParam("app_id") Long appId) {
        return Response
                .ok()
                .entity(applicationBean.getUsers(appId))
                .build();
    }

    @POST
    @Path("{app_id}/flags")
    public Response createFlags(List<FlagDto> flagList, @PathParam("app_id") Long appId) {
        flagBean.createFlags(flagList, appId);
        return Response.status(Response.Status.CREATED).build();
    }
}
