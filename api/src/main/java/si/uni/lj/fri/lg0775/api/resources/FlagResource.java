package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.beans.FlagBean;
import si.uni.lj.fri.lg0775.services.dtos.models.FlagDto;
import si.uni.lj.fri.lg0775.services.lib.DtoMapper;

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
    public Response getFlag(@QueryParam("id") Long flag_id) {
        return Response
                .ok(DtoMapper.toFlagDto(flagBean.get(flag_id)))
                .build();
    }

    @GET
    @Path("app")
    public Response getFlagsForApp(@QueryParam("app_id") Long appId) {
        return Response
                .ok()
                .entity(flagBean.getFlagsDto(appId))
                .build();
    }

    @POST
    public Response create(List<FlagDto> data) {
        flagBean.createFlags(data);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    public Response delete(@QueryParam("id") Long id) {
        flagBean.removeFlag(id);

        return Response
                .status(Response.Status.ACCEPTED)
                .build();
    }
}
