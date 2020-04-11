package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.entities.db.EndUser;
import si.uni.lj.fri.lg0775.services.beans.EndUserBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("user")
public class EndUserResource {
    @Inject
    private EndUserBean endUserBean;

    @POST
    public Response registerUser(EndUser endUser) {
        endUserBean.saveEndUser(endUser);
        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    @GET
    public Response getRulesForApp(@QueryParam("app_id") String appId) {
        return Response
                .status(Response.Status.OK)
                .entity(endUserBean.getFlagsForApp(appId))
                .build();
    }
}
