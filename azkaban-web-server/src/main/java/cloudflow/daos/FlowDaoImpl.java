package cloudflow.daos;

import azkaban.db.DatabaseOperator;
import azkaban.db.SQLTransaction;
import azkaban.spi.AzkabanException;
import azkaban.user.User;
import cloudflow.models.FlowModel;
import cloudflow.models.FlowResponse;
import cloudflow.models.Project;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.management.InstanceAlreadyExistsException;
import org.apache.commons.dbutils.ResultSetHandler;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowDaoImpl implements FlowDao {

  private DatabaseOperator dbOperator;
  private FlowAdminDaoImpl flowAdminDaoImpl;
  private static final Logger log = LoggerFactory.getLogger(ProjectDaoImpl.class);

  static String INSERT_FLOW =
      "INSERT into flow (name, project_id, latest_version, created_by, creation_time,"
          + "last_modified_by, last_modified_time) values"
          + "(?,?,?,?,?,?,?)";

  @Inject
  public FlowDaoImpl(DatabaseOperator dbOperator, FlowAdminDaoImpl flowAdminDaoImpl) {
    this.dbOperator = dbOperator;
    this.flowAdminDaoImpl = flowAdminDaoImpl;
  }


  @Override
  public String createFlowModel(FlowModel flowModel, User user) {
    String name = flowModel.getName();
    FetchFlowHandler fetchFlowHandler = new FetchFlowHandler();

    // Check if the same flow name exists.
    try {
      final List<FlowModel> flowModels = this.dbOperator
          .query(FetchFlowHandler.SELECT_FLOW_WITH_NAME, fetchFlowHandler, name);
      if (!flowModels.isEmpty()) {
        throw new AzkabanException("Flow with name " + name + " already exists.");
      }
    } catch (final SQLException ex) {
      log.error("Check if flow {} exists failed with: ", name, ex);
      throw new AzkabanException("Check if flow " + name + " exists failed");
    }

    final SQLTransaction<Long> insertAndGetProjectId = transOperator -> {
      String currentTime = DateTime.now().toLocalDateTime().toString();
      transOperator.update(INSERT_FLOW, flowModel.getName(), flowModel.getProjectId(),
          flowModel.getLatestVersion(), user.getUserId(), currentTime, user.getUserId(),
          currentTime);

      transOperator.getConnection().commit();
      return transOperator.getLastInsertId();
    };

    String flowId = "";
    try {
      flowId = dbOperator.transaction(insertAndGetProjectId).toString();
      if (flowId.isEmpty()) {
        throw new AzkabanException("Insert flow for '" + name + "' failed.");
      }
      flowAdminDaoImpl.addAdmins(flowId, flowModel.getAdmins());
    } catch (SQLException ex) {
      log.error(INSERT_FLOW + " failed with exception:", ex);
      throw new AzkabanException("Insert flow for '" + name + "' failed.",
          ex);
    }
    return flowId;
  }


  @Override
  public Optional<FlowResponse> getFlow(String flowId){
    List<FlowResponse> flows = new ArrayList<>();
    FetchFlowHandler fetchFlowHandler = new FetchFlowHandler();
    try {
      flows = dbOperator.query(FetchFlowHandler.SELECT_FLOW_WITH_ID,
          fetchFlowHandler, flowId);
      for(FlowResponse flow : flows) {
        flow.setAdmins(flowAdminDaoImpl.findAdminsByFlowId(flow.getId()));
      }
    } catch (SQLException ex) {
      log.error("Select for flow id '{}' failed: ", flowId, ex);
    }
    return flows.isEmpty() ? Optional.empty() : Optional.of(flows.get(0));
  }


  @Override
  public List<FlowResponse> getAllFlows(String projectId) {
    List<FlowResponse> flows = new ArrayList<FlowResponse>();
    FetchFlowHandler fetchFlowHandler = new FetchFlowHandler();
    try {

    } catch (SQLException ex) {
      log.error("Get all projects for project id '{}' failed: ", projectId, ex);
    }
    return flows;
  }


  public static class FetchFlowHandler implements ResultSetHandler<List<FlowModel>> {

    static String SELECT_FLOW_WITH_ID =
        "SELECT id, name, project_id, latest_version, created_by, creation_time, last_modified_by, "
            + "last_modified_time FROM flow WHERE id = ?";

    static String SELECT_FLOW_WITH_NAME =
        "SELECT id, name, project_id, latest_version, created_by, creation_time, last_modified_by, "
            + "last_modified_time FROM flow WHERE name = ?";

    static String SELECT_ALL_FLOWS_BY_PROJECT_ID =
        "SELECT id, name, project_id, latest_version, created_by, creation_time, last_modified_by, "
            + "last_modified_time FROM flow WHERE project_id = ?";


    @Override
    public List<FlowModel> handle(ResultSet rs) throws SQLException {
      if (!rs.next()) {
        return Collections.emptyList();
      }

      List<FlowModel> flowModels = new ArrayList<>();
      do {
        FlowModel flow = new FlowModel();
        flow.setId(rs.getString(1));
        flow.setName(rs.getString(2));
        flow.setProjectId(rs.getString(3));
        flow.setLatestVersion(rs.getString(4));
        flow.setCreatedByUser(rs.getString(5));
        flow.setCreatedOn(rs.getString(6));
        flow.setLastModifiedByUser(rs.getString(7));
        flow.setLastModifiedOn(rs.getString(8));

        flowModels.add(flow);
      } while (rs.next());

      return flowModels;
    }
  }
}
