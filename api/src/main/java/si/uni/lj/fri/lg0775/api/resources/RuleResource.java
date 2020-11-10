package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.beans.RuleBean;
import si.uni.lj.fri.lg0775.services.beans.GradualRolloutBean;
import si.uni.lj.fri.lg0775.services.dtos.CreateRolloutDto;
import si.uni.lj.fri.lg0775.services.dtos.CreateRuleDto;
import si.uni.lj.fri.lg0775.services.dtos.GradualRolloutDto;
import si.uni.lj.fri.lg0775.services.lib.DtoMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("rules")
public class RuleResource {
    private static final Logger LOG = Logger.getLogger(RuleResource.class.getName());

    @Inject
    private RuleBean ruleBean;

    @Inject
    private GradualRolloutBean srb;

    @POST
    public Response create(
            @QueryParam("flag_id") long flag_id,
            @QueryParam("app_id") long app_id,
            CreateRuleDto crd
    ) {
        return Response
                .ok(ruleBean.createRule(crd, app_id, flag_id))
                .build();
    }

    @POST
    @Path("rollout")
    public Response createRollout(CreateRolloutDto crd) {
        ruleBean.scheduleRollout(crd);
        return Response
                .ok()
                .build();
    }

    @GET
    @Path("rollout")
    public Response getActiveRollouts(@QueryParam("appId") Long appId) {
        if (appId == null) {
            return Response.ok(DtoMapper.toRolloutDto(srb.getUnfinishedRollouts())).build();
        } else {
            return Response.ok(DtoMapper.toRolloutDto(srb.getUnfinishedRolloutsForApp(appId))).build();
        }
    }

    @GET
    @Path("rollout/{srId}")
    public Response getRollout(@PathParam("srId") Long srId) {

        return Response.ok(DtoMapper.toRolloutDto(srb.getRollout(srId))).build();
    }

    @GET
    public Response getRulesForAppByClientID(@QueryParam("client_id") String clientId) {
        return Response
                .ok(ruleBean.getRulesForApp(clientId))
                .build();
    }

    @GET
    @Path("flag")
    public Response getRulesForFlag(@QueryParam("flag_id") long flag_id) {
        return Response
                .ok(ruleBean.getRulesDtoForFlag(flag_id))
                .build();
    }
}
