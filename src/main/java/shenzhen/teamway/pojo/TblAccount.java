package shenzhen.teamway.pojo;

import java.io.Serializable;
import java.util.Date;

public class TblAccount  implements Serializable{
    private Integer userid;

    private String username;

    private String logonid;

    private String password;

    private Boolean enable;

    private Integer maxerror;

    private Boolean locked;

    private Date validtime;

    private String description;

    private Boolean isremote;

    private Integer centerid;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getLogonid() {
        return logonid;
    }

    public void setLogonid(String logonid) {
        this.logonid = logonid == null ? null : logonid.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getMaxerror() {
        return maxerror;
    }

    public void setMaxerror(Integer maxerror) {
        this.maxerror = maxerror;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Date getValidtime() {
        return validtime;
    }

    public void setValidtime(Date validtime) {
        this.validtime = validtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Boolean getIsremote() {
        return isremote;
    }

    public void setIsremote(Boolean isremote) {
        this.isremote = isremote;
    }

    public Integer getCenterid() {
        return centerid;
    }

    public void setCenterid(Integer centerid) {
        this.centerid = centerid;
    }

    @Override
    public String toString() {
        return "TblAccount{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", logonid='" + logonid + '\'' +
                ", password='" + password + '\'' +
                ", enable=" + enable +
                ", maxerror=" + maxerror +
                ", locked=" + locked +
                ", validtime=" + validtime +
                ", description='" + description + '\'' +
                ", isremote=" + isremote +
                ", centerid=" + centerid +
                '}';
    }
}