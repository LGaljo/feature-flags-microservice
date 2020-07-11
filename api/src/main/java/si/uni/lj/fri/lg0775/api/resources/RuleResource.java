package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.beans.ApplicationBean;
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
    public Response createRule(
            @QueryParam("flagId") long flag_id,
            @QueryParam("appId") long app_id,
            CreateRuleDto crd
    ) {
        return Response
                .ok(ruleBean.createRule(crd, app_id, flag_id))
                .build();
    }

    @GET
    public Response getRule(
            @QueryParam("userId") Long user_id
    ) {
        return Response
                .ok(ruleBean.getRule(user_id))
                .build();
    }
}
