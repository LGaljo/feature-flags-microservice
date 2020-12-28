package si.uni.lj.fri.lg0775.services.dtos.models;

import java.util.List;

public class AppDto {
    private String name;
    private List<FlagDto> flags;
    private Long id;

    public AppDto() {
    }

    public AppDto(Long id, String name, List<FlagDto> flags) {
        this.name = name;
        this.flags = flags;
        this.id = id;
    }

    public AppDto(Long id, String name) {
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

    public List<FlagDto> getFlags() {
        return flags;
    }

    public void setFlags(List<FlagDto> flags) {
        this.flags = flags;
    }
}
