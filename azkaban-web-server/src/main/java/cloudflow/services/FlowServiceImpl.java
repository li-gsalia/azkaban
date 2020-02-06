package cloudflow.services;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import azkaban.user.User;
import cloudflow.daos.FlowDao;
import cloudflow.daos.ProjectDao;
import cloudflow.error.CloudFlowException;
import cloudflow.error.CloudFlowNotFoundException;
import cloudflow.models.FlowModel;
import cloudflow.models.FlowResponse;
import cloudflow.models.Project;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowServiceImpl implements FlowService {

  private static final String PROJECT_ID_PARAM = "project_id";
  private static final String PROJECT_VERSION_PARAM = "project_version";
  private static final String PROJECT_NAME_PARAM = "project_name";

  private final FlowDao flowDao;
  private final ProjectDao projectDao;
  private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

  @Inject
  public FlowServiceImpl(FlowDao flowDao, ProjectDao projectDao) {
    this.flowDao = flowDao;
    this.projectDao = projectDao;
  }

  /*
  @Override
  public String createFlowModel(FlowModel flowModel, User user) {
    return this.flowDao.createFlowModel(flowModel, user);
  }*/

  @Override
  public FlowResponse getFlow(String flowId, Map<String, String[]> queryParamMap) {
    Optional<FlowResponse> flow = flowDao.getFlow(flowId);

    if (!flow.isPresent()) {
      log.error("Flow record doesn't exist for id: " + flowId);
      throw new NoSuchElementException();
    }
    return flow.get();
  }

  private String extractArgumentValue(String argName, String[] argValues) {
    requireNonNull(argName);
    requireNonNull(argValues);
    if (argValues.length != 1) {
      throw new RuntimeException(format("Argument %s should have exactly one value", argName));
    }
    return argValues[0];
  }

  @Override
  public List<FlowResponse> getAllFlows(User user, Map<String, String[]> queryParamMap) {
    /* Currently we support only one value for each of the parameters and return an error
    otherwise.
    */
    Optional<String> projectId = Optional.empty();
    Optional<String> projectVersion = Optional.empty();
    Optional<String> projectName = Optional.empty();

    for (Map.Entry<String, String[]> entry : queryParamMap.entrySet()) {
      String key = entry.getKey();
      String[] value = entry.getValue();
      if (key.equals(PROJECT_ID_PARAM)) {
        projectId = Optional.of(extractArgumentValue(key, value));
      } else if (key.equals(PROJECT_VERSION_PARAM)) {
        projectVersion = Optional.of(extractArgumentValue(key, value));
      } else if (key.equals(PROJECT_NAME_PARAM)) {
        projectName = Optional.of(extractArgumentValue(key, value));
      }
    }

    /* Both projectId and projectName cannot be passed as query params */
    if (projectId.isPresent() && projectName.isPresent()) {
      throw new CloudFlowException("Filtering by both project id and project name is not "
          + "supported");
    }

    /* projectVersion cannot be passed as Query param without projectId or projectName */
    if (!(projectId.isPresent() || projectName.isPresent()) && projectVersion.isPresent()) {
      throw new CloudFlowException("Project id or project name must be passed along with project "
          + "Version");
    }

    List <Project> projects = new ArrayList<Project>();

    /* If projectId or projectName is present, get the project based on query params */
    if (projectId.isPresent() || projectName.isPresent()) {
      Optional<Project> project = projectDao.getProjectByParams(projectId, projectName, projectVersion);
      /* Throw exception if no project was found based on the query params */
      if (!project.isPresent()) {
        String errorMsg = String.format("Project record doesn't exist for query params passed");
        log.error(errorMsg);
        throw new CloudFlowNotFoundException(errorMsg);
      }
      projects.add(project.get());
    } else {
      /* No query params passed, get all projects for the user */
      projects.addAll(projectDao.getAll(user));
    }

    List<FlowResponse> flows = new ArrayList<FlowResponse>();

    /* For each project, get all flows and append them to a list */
    for (Project project : projects) {
      flows.addAll(flowDao.getAllFlows(project.getId()));



    }

    return flows;
  }
}
