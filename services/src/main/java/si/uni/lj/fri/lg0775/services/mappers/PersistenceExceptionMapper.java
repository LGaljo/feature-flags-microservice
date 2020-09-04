package si.uni.lj.fri.lg0775.services.mappers;


import si.uni.lj.fri.lg0775.services.dtos.ExceptionDto;

import javax.enterprise.context.RequestScoped;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@RequestScoped
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {
    @Override
    public Response toResponse(PersistenceException exception) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setStatus(1001);
        exceptionDto.setMessage(exception.getMessage());

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(exceptionDto)
                .build();
    }
}
