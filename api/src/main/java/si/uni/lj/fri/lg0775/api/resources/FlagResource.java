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
@Path("flags")
public class FlagResource {
    @Inject
    private FlagBean flagBean;

    @Inject
    private ApplicationBean applicationBean;

    @GET
    @Path("{id}")
    public Response getFlag(@PathParam("id") Long flag_id) {
        return Response
                .ok(flagBean.find(flag_id))
                .build();
    }

    @GET
    public Response getFlags(@QueryParam("app_id") Long appId) {
        return Response
                .ok()
                .entity(applicationBean.getFlagsDto(appId))
                .build();
    }

    @POST
    @Path("{app_id}")
    public Response createFlags(List<FlagDto> flagList, @PathParam("app_id") Long appId) {
        flagBean.createFlags(flagList, appId);
        return Response.status(Response.Status.CREATED).build();
    }

}
