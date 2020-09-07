package si.uni.lj.fri.lg0775.services.mappers;


import si.uni.lj.fri.lg0775.services.dtos.ExceptionDto;

import javax.enterprise.context.RequestScoped;
import javax.persistence.PersistenceException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
@RequestScoped
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {
    private static final Logger LOG = Logger.getLogger(PersistenceExceptionMapper.class.getName());

    @Override
    public Response toResponse(PersistenceException exception) {
        LOG.warning(exception.getMessage());

        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setStatus(1001);
        exceptionDto.setMessage(exception.getMessage());

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(exceptionDto)
                .build();
    }
}
