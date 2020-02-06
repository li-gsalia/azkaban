package cloudflow.servlets;

import azkaban.server.HttpRequestUtils;
import azkaban.server.session.Session;
import azkaban.user.User;
import azkaban.webapp.AzkabanWebServer;
import azkaban.webapp.servlet.LoginAbstractAzkabanServlet;
import cloudflow.services.FlowService;
import cloudflow.models.FlowModel;
import com.linkedin.jersey.api.uri.UriTemplate;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowServlet extends LoginAbstractAzkabanServlet {

  /* Flow endpoints*/
  private static final String ALL_FLOWS_URI = "/flows";
  private static final String FLOW_ID_KEY = "flowId";

  private static final UriTemplate GET_FLOW_URI_TEMPLATE = new UriTemplate(
      String.format("/flows/{%s}", FLOW_ID_KEY));

  /* Flow version endpoints*/
  private static final String VERSION_ID_KEY = "versionId";
  private static final UriTemplate ALL_FLOW_VERSIONS_URI_TEMPLATE = new UriTemplate(
      String.format("%s/{%s}/versions", ALL_FLOWS_URI, FLOW_ID_KEY));
  private static final UriTemplate FLOW_VERSION_URI_TEMPLATE = new UriTemplate(
      String.format("%s/{%s}/versions/{%s}", ALL_FLOWS_URI, FLOW_ID_KEY, VERSION_ID_KEY));

  private FlowService flowService;
  private ObjectMapper objectMapper;

  private static final Logger log = LoggerFactory.getLogger(FlowServlet.class);

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
    final AzkabanWebServer server = (AzkabanWebServer) getApplication();
    this.flowService = server.flowService();
    this.objectMapper = server.objectMapper();
  }

  private void validateId(String id, HttpServletResponse resp) throws IOException {
    /* Validate id is an Integer. */
    try {
      Integer.parseInt(id);
    } catch (NumberFormatException e) {
      log.error("Invalid id: ", id);
      sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid id, id must be an "
          + "integer");
      return;
    }
  }


  private void returnAllFlows(HttpServletRequest req, HttpServletResponse resp, Session session)
      throws IOException {
    sendResponse(resp, HttpServletResponse.SC_OK,
        flowService.getAllFlows(session.getUser(), req.getParameterMap()));
  }

  private void returnFlow(HttpServletRequest req, HttpServletResponse resp, Session session)
      throws IOException {
    Map<String, String> templateVariableToValue = new HashMap<>();
    try {
      String flowId = templateVariableToValue.get(FLOW_ID_KEY);
      validateId(flowId, resp);
      sendResponse(resp, HttpServletResponse.SC_OK,
          flowService.getFlow(flowId, req.getParameterMap()));
    } catch (Exception e) {
      log.error("Exception while fetching flow: " + e);
      sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Flow id not found");
      return;
    }

  }

  @Override
  protected void handleGet(HttpServletRequest req, HttpServletResponse resp, Session session)
      throws IOException, ServletException {
    Map<String, String> templateVariableToValue = new HashMap<>();
    if (ALL_FLOWS_URI.equals(req.getRequestURI())) {
      returnAllFlows(req, resp, session);
    } else if (GET_FLOW_URI_TEMPLATE.match(req.getRequestURI(), templateVariableToValue)) {
      returnFlow(req, resp, session);

    } else if (ALL_FLOW_VERSIONS_URI_TEMPLATE.match(req.getRequestURI(), templateVariableToValue)) {
      /* Get all versions for specified flow id */
      String flowId = templateVariableToValue.get(FLOW_ID_KEY);
      validateId(flowId, resp);
      /*
      String response = objectMapper.writeValueAsString(flowService.getAllFlowVersions(flowId,
          session.getUser()));
      this.writeResponse(resp, response);
      */
    } else if (FLOW_VERSION_URI_TEMPLATE.match(req.getRequestURI(), templateVariableToValue)) {
      String flowId = templateVariableToValue.get(FLOW_ID_KEY);
      validateId(flowId, resp);
      String versionId = templateVariableToValue.get(VERSION_ID_KEY);
      validateId(versionId, resp);

      /*
      String response = objectMapper.writeValueAsString(flowService.getFlowVersion(flowId,
          versionId, session.getUser()));
      this.writeResponse(resp, response);
       */
    } else {
      /* Unsupported route, return an error */
      log.error("Invalid route for flows endpoint: " + req.getRequestURI());
      sendErrorResponse(resp, HttpServletResponse.SC_NOT_IMPLEMENTED, "Unsupported flows API "
          + "endpoint");
      return;
    }
  }


  @Override
  protected void handlePost(HttpServletRequest req, HttpServletResponse resp, Session session)
      throws ServletException, IOException {
    try {
      Map<String, String> templateVariableToValue = new HashMap<>();

      /* Create a new flow version */
      if (ALL_FLOW_VERSIONS_URI_TEMPLATE.match(req.getRequestURI(), templateVariableToValue)) {
        String flowId = templateVariableToValue.get(FLOW_ID_KEY);
        validateId(flowId, resp);
        /*
        String body = HttpRequestUtils.getBody(req);
        FlowVersion flowVersion = objectMapper.readValue(body, FlowVersion.class);

        String versionId = flowService.createFlowVersion(flowId, session.getUser(), flowVersion);
        sendResponse(resp, HttpServletResponse.SC_CREATED, versionId); */
        return;
      } else {
        /* Unsupported route, return an error */
        log.error("Invalid route for flows endpoint: " + req.getRequestURI());
        sendErrorResponse(resp, HttpServletResponse.SC_NOT_IMPLEMENTED, "Unsupported flows API "
            + "endpoint");
        return;
      }
    } catch (Exception e) {
      log.error("Error while handling POST request for projects. Got Exception: " + e);
      sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD request to create project");
      return;
    }
  }
}
