package cloudflow.models;

import java.util.List;
import java.util.Objects;

public class FlowModel {
  private String id;
  private String name;
  private String projectId;
  private String latestVersion;
  private List<String> admins;
  private String createdOn;
  private String createdByUser;
  private String lastModifiedOn;
  private String lastModifiedByUser;


  public FlowModel() {

  }

  public FlowModel(String id, String name, String projectId, String latestVersion,
      List<String> admins, String createdOn, String createdByUser, String lastModifiedOn,
      String lastModifiedByUser) {
    this.id = id;
    this.name = name;
    this.projectId = projectId;
    this.latestVersion = latestVersion;
    this.admins = admins;
    this.createdOn = createdOn;
    this.createdByUser = createdByUser;
    this.lastModifiedOn = lastModifiedOn;
    this.lastModifiedByUser = lastModifiedByUser;
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

  public String getProjectId() {
    return projectId;
  }

  public void setProjectId(String projectId) {
    this.projectId = projectId;
  }

  public String getLatestVersion() {
    return latestVersion;
  }

  public void setLatestVersion(String latestVersion) {
    this.latestVersion = latestVersion;
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

  public String getLastModifiedOn() {
    return lastModifiedOn;
  }

  public void setLastModifiedOn(String lastModifiedOn) {
    this.lastModifiedOn = lastModifiedOn;
  }

  public String getLastModifiedByUser() {
    return lastModifiedByUser;
  }

  public void setLastModifiedByUser(String lastModifiedByUser) {
    this.lastModifiedByUser = lastModifiedByUser;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof FlowModel)) {
      return false;
    }
    FlowModel flowModel = (FlowModel) o;
    return getId().equals(flowModel.getId()) &&
        getName().equals(flowModel.getName()) &&
        getProjectId().equals(flowModel.getProjectId()) &&
        getLatestVersion().equals(flowModel.getLatestVersion()) &&
        getAdmins().equals(flowModel.getAdmins()) &&
        getCreatedOn().equals(flowModel.getCreatedOn()) &&
        getCreatedByUser().equals(flowModel.getCreatedByUser()) &&
        getLastModifiedOn().equals(flowModel.getLastModifiedOn()) &&
        getLastModifiedByUser().equals(flowModel.getLastModifiedByUser());
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(getId(), getName(), getProjectId(), getLatestVersion(), getAdmins(), getCreatedOn(),
            getCreatedByUser(), getLastModifiedOn(), getLastModifiedByUser());
  }

  @Override
  public String toString() {
    return "FlowModel{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", projectId='" + projectId + '\'' +
        ", latestVersion='" + latestVersion + '\'' +
        ", admins=" + admins +
        ", createdOn='" + createdOn + '\'' +
        ", createdByUser='" + createdByUser + '\'' +
        ", lastModifiedOn='" + lastModifiedOn + '\'' +
        ", lastModifiedByUser='" + lastModifiedByUser + '\'' +
        '}';
  }
}
