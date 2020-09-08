package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.beans.FlagBean;
import si.uni.lj.fri.lg0775.services.dtos.FlagDto;
import si.uni.lj.fri.lg0775.services.dtos.IdDto;

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

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long flag_id) {
        return Response
                .ok(flagBean.get(flag_id))
                .build();
    }

    @GET
    public Response getFlagsForApp(@QueryParam("app_id") Long appId) {
        return Response
                .ok()
                .entity(flagBean.getFlagsDto(appId))
                .build();
    }

    @POST
    @Path("{app_id}")
    public Response create(List<FlagDto> flagList, @PathParam("app_id") Long appId) {
        flagBean.createFlags(flagList, appId);
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("delete")
    public Response delete(IdDto id) {
        flagBean.removeFlag(id.getId());

        return Response
                .status(Response.Status.ACCEPTED)
                .build();
    }
}
