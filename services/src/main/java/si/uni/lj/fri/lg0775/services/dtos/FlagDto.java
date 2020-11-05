package si.uni.lj.fri.lg0775.services.dtos;

import si.uni.lj.fri.lg0775.entities.enums.DataType;

public class FlagDto {
    private Long id;
    private int defaultValue;
    private String name;
    private String description;
    private DataType dataType;

    public FlagDto() {
    }

    public FlagDto(Long id, int defaultValue, String name, String description, DataType dataType) {
        this.id = id;
        this.defaultValue = defaultValue;
        this.name = name;
        this.description = description;
        this.dataType = dataType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}
