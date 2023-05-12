package com.cisco.workspaceintegrations.examples.roomcapacitymonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cisco.workspaceintegrations.api.queue.QueuePoller;
import com.cisco.workspaceintegrations.api.workspaces.WorkspaceCache;
import com.cisco.workspaceintegrations.common.messages.WebhookStatusMessage;
import com.cisco.workspaceintegrations.common.oauth.OAuthClient;
import com.cisco.workspaceintegrations.common.workspaces.Workspace;
import com.cisco.workspaceintegrations.common.xapi.CommandRequest;
import com.cisco.workspaceintegrations.examples.common.Example;

import static com.cisco.workspaceintegrations.common.xapi.CommandRequest.arguments;
import static com.cisco.workspaceintegrations.common.xapi.Key.key;
import static com.cisco.workspaceintegrations.common.xapi.StatusKeys.ROOM_ANALYTICS_PEOPLE_COUNT;

public class RoomCapacityMonitor extends Example {

    private static final Logger LOG = LoggerFactory.getLogger(RoomCapacityMonitor.class);

    public RoomCapacityMonitor(OAuthClient oAuthClient) {
        super("RoomCapacityMonitor", oAuthClient);
    }

    @Override
    protected void start() {
        WorkspaceCache workspaceCache = new WorkspaceCache(integration.getWorkspacesApi());
        QueuePoller poller = integration.getQueuePoller((messages -> {
            LOG.info("Received messages: " + messages);
            messages.forEach((message -> {
                if (message instanceof WebhookStatusMessage statusMessage) {
                    statusMessage.getUpdatedIntegerValue(ROOM_ANALYTICS_PEOPLE_COUNT)
                                 .ifPresent(peopleCount -> {
                                     LOG.info("People count updated to {} for device {}", peopleCount, statusMessage.deviceId());
                                     Workspace workspace = workspaceCache.get(statusMessage.workspaceId());
                                     if (workspace.hasCapacity() && peopleCount > workspace.getCapacity()) {
                                         LOG.info("Capacity exceeded, showing message on screen");
                                         integration.getXapi().executeCommand(
                                             key("UserInterface.Message.Alert.Display"),
                                             new CommandRequest(
                                                 statusMessage.deviceId(),
                                                 arguments(
                                                     "Title", "Playtime demo",
                                                     "Text", "Number of people exceeds room capacity",
                                                     "Duration", 10
                                                 )
                                             )
                                         );
                                     }
                                 });
                }
            }));
        }));
        poller.start();
    }
}
