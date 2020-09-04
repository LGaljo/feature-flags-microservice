package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.beans.RuleBean;
import si.uni.lj.fri.lg0775.services.dtos.CreateRuleDto;

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

    @POST
    public Response create(
            @QueryParam("flagId") long flag_id,
            @QueryParam("appId") long app_id,
            CreateRuleDto crd
    ) {
        return Response
                .ok(ruleBean.createRule(crd, app_id, flag_id))
                .build();
    }

    @GET
    @Path("flag")
    public Response getRulesForFlag(@QueryParam("flagId") long flag_id) {
        return Response.ok(ruleBean.getRulesForFlag(flag_id)).build();
    }

    @GET
    @Path("app")
    public Response getRulesForAppByClientID(@QueryParam("client_id") String clientId) {
        return Response
                .status(Response.Status.OK)
                .entity(ruleBean.getRulesForApp(clientId))
                .build();
    }

    @GET
    @Path("{id}")
    public Response getRulesForAppByUserID(@PathParam("id") Long user_id) {
        return Response
                .ok(ruleBean.getRulesForUserID(user_id))
                .build();
    }
}
