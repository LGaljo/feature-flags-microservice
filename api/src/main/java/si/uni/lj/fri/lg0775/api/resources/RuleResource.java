package si.uni.lj.fri.lg0775.api.resources;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("rules")
public class RuleResource {

    @POST
    public Response createRule(
            @QueryParam("flagId") Long flag_id,
            @QueryParam("appId") Long app_id
    ) {
        return Response
                .status(Response.Status.NOT_IMPLEMENTED)
                .build();
    }
}
