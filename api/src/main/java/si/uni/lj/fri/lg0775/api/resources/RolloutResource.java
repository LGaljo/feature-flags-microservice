package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.beans.GradualRolloutBean;
import si.uni.lj.fri.lg0775.services.beans.RuleBean;
import si.uni.lj.fri.lg0775.services.dtos.CreateRolloutDto;
import si.uni.lj.fri.lg0775.services.lib.DtoMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("rollout")
public class RolloutResource {
    @Inject
    private RuleBean ruleBean;

    @Inject
    private GradualRolloutBean srb;

    @POST
    public Response createRollout(CreateRolloutDto crd) {
        ruleBean.scheduleRollout(crd);
        return Response
                .ok()
                .status(Response.Status.CREATED)
                .build();
    }

    @GET
    public Response getActiveRollouts(@QueryParam("appId") Long appId) {
        if (appId == null) {
            return Response.ok(DtoMapper.toRolloutDto(srb.getUnfinishedRollouts())).build();
        } else {
            return Response.ok(DtoMapper.toRolloutDto(srb.getUnfinishedRolloutsForApp(appId))).build();
        }
    }

    @GET
    @Path("{srId}")
    public Response getRollout(@PathParam("srId") Long srId) {

        return Response.ok(DtoMapper.toRolloutDto(srb.getRollout(srId))).build();
    }
}
