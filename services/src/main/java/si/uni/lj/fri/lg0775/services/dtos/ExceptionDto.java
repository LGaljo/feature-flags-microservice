package si.uni.lj.fri.lg0775.services.dtos;

public class ExceptionDto {
    private Integer status;
    private String message;

    public ExceptionDto() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
