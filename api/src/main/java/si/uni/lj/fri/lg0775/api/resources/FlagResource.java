package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.beans.FlagBean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("flags")
public class FlagResource {
    @Inject
    private FlagBean flagBean;

    @GET
    @Path("{id}")
    public Response getFlag(@PathParam("id") Long flag_id) {
        return Response
                .ok(flagBean.find(flag_id))
                .build();
    }
}
