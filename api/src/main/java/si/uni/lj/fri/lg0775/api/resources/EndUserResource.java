package si.uni.lj.fri.lg0775.api.resources;

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
    public Response heartbeat(
            @QueryParam("client_id") String clientId,
            @QueryParam("app_name") String appName
    ) {
        return Response
                .status(Response.Status.CREATED)
                .entity(endUserBean.heartbeat(clientId, appName))
                .build();
    }

    @GET
    public Response getUsersForApp(@QueryParam("app_id") Long appId) {
        return Response
                .ok()
                .entity(endUserBean.getUsers(appId))
                .build();
    }

    @DELETE
    public Response delete(@QueryParam("app_id") Long id) {
        endUserBean.markDeleted(endUserBean.find(id));

        return Response
                .status(Response.Status.OK)
                .build();
    }
}
