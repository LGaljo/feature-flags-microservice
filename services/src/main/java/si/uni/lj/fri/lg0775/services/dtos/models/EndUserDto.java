package si.uni.lj.fri.lg0775.services.dtos.models;

public class EndUserDto {
    private Long id;
    private String client;

    public EndUserDto() {
    }

    public EndUserDto(Long id, String client) {
        this.id = id;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
