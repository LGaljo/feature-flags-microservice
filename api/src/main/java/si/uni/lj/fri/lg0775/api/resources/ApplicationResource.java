package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.beans.ApplicationBean;
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
@Path("application")
public class ApplicationResource {
    @Inject
    ApplicationBean applicationBean;

    @GET
    @Path("flags")
    public Response getFlags(@QueryParam("app_id") String appId) {
        return Response.ok().entity(applicationBean.getFlags(appId)).build();
    }

    @POST
    @Path("flags")
    public Response createFlags(List<FlagDto> flagList, @QueryParam("app_id") String appId) {
        applicationBean.createFlags(flagList, appId);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    public Response createApp(@QueryParam("name") String appName) {
        applicationBean.createApp(appName);
        return Response.status(Response.Status.CREATED).build();
    }
}
