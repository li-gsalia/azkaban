package cloudflow.daos;

import azkaban.user.User;
import cloudflow.models.FlowModel;
import cloudflow.models.FlowResponse;
import java.util.List;
import java.util.Optional;

public interface FlowDao {
  String createFlowModel(FlowModel project, User user);
  Optional<FlowResponse> getFlow(String flowId);
  List<FlowResponse> getAllFlows(String projectId);
}
