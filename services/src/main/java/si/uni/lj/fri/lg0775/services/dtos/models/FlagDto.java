package si.uni.lj.fri.lg0775.services.dtos.models;

import si.uni.lj.fri.lg0775.entities.enums.DataType;

import java.time.Instant;

public class FlagDto {
    private Long appId;
    private DataType dataType;
    private int defaultValue;
    private String description;
    private Instant expirationDate;
    private Long id;
    private String name;

    public FlagDto() {
    }

    public FlagDto(Long appId, Long id, int defaultValue, String name, String description, DataType dataType, Instant expirationDate) {
        this.appId = appId;
        this.id = id;
        this.defaultValue = defaultValue;
        this.name = name;
        this.description = description;
        this.dataType = dataType;
        this.expirationDate = expirationDate;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
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

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }
}
