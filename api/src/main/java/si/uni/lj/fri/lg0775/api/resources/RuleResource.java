package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.beans.RuleBean;
import si.uni.lj.fri.lg0775.services.dtos.CreateRuleDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("rules")
public class RuleResource {
    @Inject
    private RuleBean ruleBean;

    @POST
    public Response create(
            @QueryParam("flag_id") long flag_id,
            @QueryParam("app_id") long app_id,
            CreateRuleDto crd
    ) {
        return Response
                .ok(ruleBean.createRule(crd, app_id, flag_id))
                .status(Response.Status.CREATED)
                .build();
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
