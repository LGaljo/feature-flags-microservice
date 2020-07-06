package si.uni.lj.fri.lg0775.services.mappers;

import si.uni.lj.fri.lg0775.services.exceptions.InvalidDataException;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@RequestScoped
public class InvalidDataExceptionMapper implements ExceptionMapper<InvalidDataException> {
    @Override
    public Response toResponse(InvalidDataException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .build();
    }
}
