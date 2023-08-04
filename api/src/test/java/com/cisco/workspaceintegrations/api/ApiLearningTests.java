package com.cisco.workspaceintegrations.api;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.cisco.workspaceintegrations.api.core.ListResponse;
import com.cisco.workspaceintegrations.api.core.LogOnlyProvisioningChangedListener;
import com.cisco.workspaceintegrations.api.devices.DevicesApi;
import com.cisco.workspaceintegrations.api.devices.DevicesFilter;
import com.cisco.workspaceintegrations.api.http.HttpException;
import com.cisco.workspaceintegrations.api.integration.IntegrationApi;
import com.cisco.workspaceintegrations.api.locations.WorkspaceLocationsApi;
import com.cisco.workspaceintegrations.api.locations.WorkspaceLocationsFilter;
import com.cisco.workspaceintegrations.api.queue.QueuePoller;
import com.cisco.workspaceintegrations.api.workspaces.WorkspacesApi;
import com.cisco.workspaceintegrations.api.workspaces.WorkspacesFilter;
import com.cisco.workspaceintegrations.api.xapi.XAPI;
import com.cisco.workspaceintegrations.common.devices.ActivationCode;
import com.cisco.workspaceintegrations.common.devices.Device;
import com.cisco.workspaceintegrations.common.integration.IntegrationUpdate;
import com.cisco.workspaceintegrations.common.locations.CountryCode;
import com.cisco.workspaceintegrations.common.locations.WorkspaceLocation;
import com.cisco.workspaceintegrations.common.locations.WorkspaceLocationRequest;
import com.cisco.workspaceintegrations.common.messages.WebhookStatusMessage;
import com.cisco.workspaceintegrations.common.oauth.OAuthClient;
import com.cisco.workspaceintegrations.common.workspaces.CreateWorkspace;
import com.cisco.workspaceintegrations.common.workspaces.UpdateWorkspace;
import com.cisco.workspaceintegrations.common.workspaces.Workspace;
import com.cisco.workspaceintegrations.common.xapi.CommandRequest;
import com.cisco.workspaceintegrations.common.xapi.Key;
import com.cisco.workspaceintegrations.common.xapi.StatusResponse;

import static com.cisco.workspaceintegrations.common.integration.ProvisioningState.COMPLETED;
import static com.cisco.workspaceintegrations.common.integration.Queue.enabledQueue;
import static com.cisco.workspaceintegrations.common.workspaces.WorkspaceCategory.huddle;
import static com.cisco.workspaceintegrations.common.workspaces.WorkspaceCategory.meetingRoom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ApiLearningTests {

    private static final Logger LOG = LoggerFactory.getLogger(ApiLearningTests.class);
    private static final boolean ENABLE_TESTS = false;

    private final String activationJwt = "...";
    private final String clientId = "...";
    private final String clientSecret = "...";

    private WorkspaceIntegration integration;
    private IntegrationApi integrationApi;
    private WorkspacesApi workspacesApi;
    private XAPI xapi;
    private DevicesApi devicesApi;
    private WorkspaceLocationsApi workspaceLocationsApi;

    @BeforeMethod
    public void setUp() {
        integration = new WorkspaceIntegration("E2ETesting",
                                               new OAuthClient(clientId, clientSecret),
                                               new LogOnlyProvisioningChangedListener());
        integration.init(activationJwt, IntegrationUpdate.builder().provisioningState(COMPLETED).queue(enabledQueue()).build());
        integrationApi = integration.getIntegrationApi();
        workspacesApi = integration.getWorkspacesApi();
        xapi = integration.getXapi();
        devicesApi = integration.getDevicesApi();
        workspaceLocationsApi = integration.getWorkspaceLocationsApi();
    }

    @Test(enabled = ENABLE_TESTS)
    public void testManagement() {
        integrationApi.postUpdate(IntegrationUpdate.builder().provisioningState(COMPLETED).build());
    }

    @Test(enabled = ENABLE_TESTS)
    public void testWorkspaces() {
        String workspaceName = "Integration created workspace: " + UUID.randomUUID();
        Workspace workspace = workspacesApi.createWorkspace(
            CreateWorkspace.builder()
                           .displayName(workspaceName)
                           .type(huddle)
                           .notes("Testing 123 testing").build()
        );
        assertThat(workspace.getNotes()).isEqualTo("Testing 123 testing");
        assertThat(workspace.getType()).isEqualTo(huddle);
        assertThat(workspace.getDisplayName()).isEqualTo(workspaceName);

        Workspace workspaceAfterGet = workspacesApi.getWorkspace(workspace.getId());
        assertThat(workspace).isEqualTo(workspaceAfterGet);

        ListResponse<Workspace> workspaces = workspacesApi.getWorkspaces(
            0, 10, WorkspacesFilter.builder().type(huddle).displayName(workspaceName).build()
        );
        assertThat(workspaces.getItems().size()).isEqualTo(1);
        assertThat(workspaces.getItems().get(0)).isEqualTo(workspace);

        String newWorkspaceName = "Renamed " + workspaceName;
        Workspace updatedWorkspace = workspacesApi.updateWorkspace(
            workspace.getId(), UpdateWorkspace.builder()
                                              .displayName(newWorkspaceName)
                                              .type(meetingRoom)
                                              .notes("").build()
        );

        assertThat(updatedWorkspace.getNotes()).isEqualTo("");
        assertThat(updatedWorkspace.getType()).isEqualTo(meetingRoom);
        assertThat(updatedWorkspace.getDisplayName()).isEqualTo(newWorkspaceName);

        ActivationCode activationCode = devicesApi.createActivationCode(workspace.getId());
        assertThat(activationCode.getCode()).isNotNull();

        workspacesApi.deleteWorkspace(workspace.getId());

        try {
            workspacesApi.getWorkspace(workspace.getId());
            fail("Should throw not found exception");
        } catch (HttpException ex) {
            assertThat(ex.isNotFound()).isTrue();
        }
    }

    @Test(enabled = ENABLE_TESTS)
    public void testXapi() {
        String deviceId = "Y2lzY29zcGFyazovL3VybjpURUFNOnVzLWVhc3QtMV9pbnQxMy9ERVZJQ0UvZTdiMDA4MjktZmEwNS00ZWMzLTljZGQtZDA0YjE4NjE3ZDU0";
        StatusResponse response = xapi.getAllStatuses(deviceId);
        assertThat(response.result().getNode("RoomAnalytics")).isNotEmpty();
        response = xapi.getStatus(deviceId, Key.parse("RoomAnalytics.*"));
        assertThat(response.result().getNode("RoomAnalytics", "PeopleCount", "Current")).isNotEmpty();
        assertThat(response.result().getNode("Network")).isEmpty();
        xapi.executeCommand(Key.parse("Call.Disconnect"), new CommandRequest(deviceId));
    }

    @Test(enabled = ENABLE_TESTS)
    public void testDevices() {
        String deviceId = "Y2lzY29zcGFyazovL3VybjpURUFNOnVzLWVhc3QtMV9pbnQxMy9ERVZJQ0UvZTdiMDA4MjktZmEwNS00ZWMzLTljZGQtZDA0YjE4NjE3ZDU0";
        Device device = devicesApi.getDevice(deviceId);
        ListResponse<Device> devices = devicesApi.getDevices(
            0,
            10,
            DevicesFilter.builder()
                         .displayName(device.getDisplayName())
                         .type(device.getType())
                         .software(device.getSoftware())
                         .mac(device.getMac())
                         .serial(device.getSerial())
                         .build()
        );
        assertThat(devices.getItems().size()).isEqualTo(1);
        assertThat(devices.getItems().get(0)).isEqualTo(device);
    }

    @Test(enabled = ENABLE_TESTS)
    public void testQueue() throws Exception {
        AtomicBoolean messagesReceived = new AtomicBoolean();
        QueuePoller poller = integration.getQueuePoller((messages -> {
            LOG.info("Received messages: " + messages);
            messagesReceived.set(true);
            messages.forEach(message -> {
                if (message instanceof WebhookStatusMessage) {
                    WebhookStatusMessage statusMessage = (WebhookStatusMessage) message;
                    statusMessage.getUpdatedStringValue(Key.parse("RoomAnalytics.PeopleCount.Current"))
                                 .ifPresent(peopleCount -> {
                                     LOG.info("People count updated to {} for device {}", peopleCount, statusMessage.deviceId());
                                 });
                    statusMessage.getUpdatedIntegerValue(Key.parse("SystemUnit.State.NumberOfActiveCalls"))
                                 .ifPresent(numActiveCalls -> {
                                     LOG.info("Number of active calls updated to {} for device {}", numActiveCalls, statusMessage.deviceId());
                                 });
                    statusMessage.getUpdatedStringValue(Key.parse("RoomAnalytics.AmbientTemperature"))
                                 .ifPresent(temp -> {
                                     LOG.info("Temperature updated to {} for device {}", temp, statusMessage.deviceId());
                                 });
                }
            });
        }));
        poller.start();
        Thread.sleep(30000);
        assertThat(messagesReceived.get()).isTrue();
    }

    @Test(enabled = ENABLE_TESTS)
    public void testLocations() {
        String locationName = "Integration created location: " + UUID.randomUUID();
        WorkspaceLocation location = workspaceLocationsApi.createLocation(
            WorkspaceLocationRequest.builder()
                                    .displayName(locationName)
                                    .address("Kunnskapsveien 50, 3430 Spikkestad")
                                    .cityName("Spikkestad")
                                    .countryCode(CountryCode.NO)
                                    .latitude(59.750202916417535)
                                    .longitude(10.334877764109024)
                                    .notes("Testing 123 testing").build()
        );
        assertThat(location.getNotes().get()).isEqualTo("Testing 123 testing");
        assertThat(location.getAddress()).isEqualTo("Kunnskapsveien 50, 3430 Spikkestad");
        assertThat(location.getCityName().get()).isEqualTo("Spikkestad");
        assertThat(location.getCountryCode()).isEqualTo(CountryCode.NO);
        assertThat(location.getLatitude().get()).isEqualTo(59.750202916417535);
        assertThat(location.getLongitude().get()).isEqualTo(10.334877764109024);

        WorkspaceLocation locationAfterGet = workspaceLocationsApi.getLocation(location.getId());
        assertThat(location).isEqualTo(locationAfterGet);

        ListResponse<WorkspaceLocation> locations = workspaceLocationsApi.getLocations(
            WorkspaceLocationsFilter.builder().displayName(locationName).build()
        );
        assertThat(locations.getItems().size()).isEqualTo(1);
        assertThat(locations.getItems().get(0)).isEqualTo(location);

        String newName = "Renamed " + locationName;
        WorkspaceLocation updateLocation = workspaceLocationsApi.updateLocation(
            location.getId(), WorkspaceLocationRequest.builder(location)
                                                      .displayName(newName)
                                                      .address("Kunnskapsveien 50 -> 3430 Spikkestad")
                                                      .notes("")
                                                      .build()
        );

        assertThat(updateLocation.getNotes().get()).isEqualTo("");
        assertThat(updateLocation.getAddress()).isEqualTo("Kunnskapsveien 50 -> 3430 Spikkestad");
        assertThat(updateLocation.getDisplayName()).isEqualTo(newName);

        workspaceLocationsApi.deleteLocation(location.getId());

        try {
            workspaceLocationsApi.getLocation(location.getId());
            fail("Should throw not found exception");
        } catch (HttpException ex) {
            assertThat(ex.isNotFound()).isTrue();
        }
    }
}
