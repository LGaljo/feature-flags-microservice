package si.uni.lj.fri.lg0775.services.dtos.models;

public class AppDto {
    private String name;
    private Long id;

    public AppDto() {
    }

    public AppDto(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
