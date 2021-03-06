package si.uni.lj.fri.lg0775.api.resources;

import si.uni.lj.fri.lg0775.services.beans.ApplicationBean;
import si.uni.lj.fri.lg0775.services.beans.FlagBean;
import si.uni.lj.fri.lg0775.services.dtos.models.AppDto;
import si.uni.lj.fri.lg0775.services.lib.DtoMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("applications")
public class ApplicationResource {
    @Inject
    private ApplicationBean applicationBean;

    @Inject
    private FlagBean flagBean;

    @POST
    public Response create(AppDto name) {
        return Response
                .status(Response.Status.CREATED)
                .entity(applicationBean.createApp(name.getName()))
                .build();
    }

    @GET
    public Response getAll() {
        return Response
                .ok()
                .entity(DtoMapper.toAppsDto(applicationBean.getAll()))
                .build();
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        return Response
                .ok()
                .entity(DtoMapper.toAppDto(applicationBean.find(id), flagBean.getFlagsForApp(id)))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        applicationBean.removeApp(id);

        return Response
                .status(Response.Status.ACCEPTED)
                .build();
    }
}
