package si.uni.lj.fri.lg0775.services.dtos;

public class Share {
    private Integer value;
    private Integer share;

    public Share() {
    }

    public Share(Integer value, Integer share) {
        this.value = value;
        this.share = share;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }
}
