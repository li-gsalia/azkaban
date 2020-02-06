package cloudflow.models;

import java.util.List;
import java.util.Objects;

public class FlowResponse {
  private String id;
  private String name;
  private int flowVersion;
  private String projectId;
  private String projectVersion;
  private List<String> admins;
  private String createdOn;
  private String createdByUser;
  private Boolean isExperimental;
  private int dslVersion;
  private Boolean locked;

  public FlowResponse(String id, String name, int flowVersion, String projectId,
      String projectVersion, List<String> admins, String createdOn, String createdByUser,
      Boolean isExperimental, int dslVersion, Boolean locked) {
    this.id = id;
    this.name = name;
    this.flowVersion = flowVersion;
    this.projectId = projectId;
    this.projectVersion = projectVersion;
    this.admins = admins;
    this.createdOn = createdOn;
    this.createdByUser = createdByUser;
    this.isExperimental = isExperimental;
    this.dslVersion = dslVersion;
    this.locked = locked;
  }

  public FlowResponse(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getFlowVersion() {
    return flowVersion;
  }

  public void setFlowVersion(int flowVersion) {
    this.flowVersion = flowVersion;
  }

  public String getProjectId() {
    return projectId;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  public String getProjectVersion() {
    return projectVersion;
  }

  public void setProjectVersion(String projectVersion) {
    this.projectVersion = projectVersion;
  }

  public List<String> getAdmins() {
    return admins;
  }

  public void setAdmins(List<String> admins) {
    this.admins = admins;
  }

  public String getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
  }

  public String getCreatedByUser() {
    return createdByUser;
  }

  public void setCreatedByUser(String createdByUser) {
    this.createdByUser = createdByUser;
  }

  public Boolean getExperimental() {
    return isExperimental;
  }

  public void setExperimental(Boolean experimental) {
    isExperimental = experimental;
  }

  public int getDslVersion() {
    return dslVersion;
  }

  public void setDslVersion(int dslVersion) {
    this.dslVersion = dslVersion;
  }

  public Boolean getLocked() {
    return locked;
  }

  public void setLocked(Boolean locked) {
    this.locked = locked;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FlowResponse)) {
      return false;
    }
    FlowResponse that = (FlowResponse) o;
    return getFlowVersion() == that.getFlowVersion() &&
        getDslVersion() == that.getDslVersion() &&
        Objects.equals(getId(), that.getId()) &&
        Objects.equals(getName(), that.getName()) &&
        Objects.equals(getProjectId(), that.getProjectId()) &&
        Objects.equals(getProjectVersion(), that.getProjectVersion()) &&
        Objects.equals(getAdmins(), that.getAdmins()) &&
        Objects.equals(getCreatedOn(), that.getCreatedOn()) &&
        Objects.equals(getCreatedByUser(), that.getCreatedByUser()) &&
        Objects.equals(isExperimental, that.isExperimental) &&
        Objects.equals(getLocked(), that.getLocked());
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(getId(), getName(), getFlowVersion(), getProjectId(), getProjectVersion(),
            getAdmins(),
            getCreatedOn(), getCreatedByUser(), isExperimental, getDslVersion(), getLocked());
  }

  @Override
  public String toString() {
    return "FlowResponse{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", flowVersion=" + flowVersion +
        ", projectId='" + projectId + '\'' +
        ", projectVersion='" + projectVersion + '\'' +
        ", admins=" + admins +
        ", createdOn='" + createdOn + '\'' +
        ", createdByUser='" + createdByUser + '\'' +
        ", isExperimental=" + isExperimental +
        ", dslVersion=" + dslVersion +
        ", locked=" + locked +
        '}';
  }
}
