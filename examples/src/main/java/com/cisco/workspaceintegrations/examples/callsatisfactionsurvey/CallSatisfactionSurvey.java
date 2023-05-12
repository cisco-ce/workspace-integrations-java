package com.cisco.workspaceintegrations.examples.callsatisfactionsurvey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cisco.workspaceintegrations.api.devices.DeviceCache;
import com.cisco.workspaceintegrations.api.queue.QueuePoller;
import com.cisco.workspaceintegrations.common.devices.events.CallDisconnect;
import com.cisco.workspaceintegrations.common.devices.events.RatingResponse;
import com.cisco.workspaceintegrations.common.json.Json;
import com.cisco.workspaceintegrations.common.messages.WebhookEventsMessage;
import com.cisco.workspaceintegrations.common.oauth.OAuthClient;
import com.cisco.workspaceintegrations.common.xapi.CommandRequest;
import com.cisco.workspaceintegrations.examples.common.Example;

import static com.cisco.workspaceintegrations.common.xapi.CommandRequest.arguments;
import static com.cisco.workspaceintegrations.common.xapi.EventKeys.CALL_DISCONNECT;
import static com.cisco.workspaceintegrations.common.xapi.EventKeys.UI_MESSAGE_RATING_RESPONSE;
import static com.cisco.workspaceintegrations.common.xapi.Key.key;

/**
 * This call satisfaction survey example displays a rating survey on the devices after a call disconnects using:
 * 1. The CallDisconnect event to detect when a call has ended,
 * 2. The UserInterface.Message.Rating.Display command to show a rating dialog on the device user interface and
 * 3. The UserInterface.Message.Rating.Response event to capture the rating.
 */
public class CallSatisfactionSurvey extends Example {

    private static final Logger LOG = LoggerFactory.getLogger(CallSatisfactionSurvey.class);

    public CallSatisfactionSurvey(OAuthClient oAuthClient) {
        super("CallSatisfactionSurvey", oAuthClient);

    }

    @Override
    protected void start() {
        DeviceCache deviceCache = new DeviceCache(integration.getDevicesApi());
        QueuePoller poller = integration.getQueuePoller((messages -> {
            LOG.info("Received messages: " + messages);
            messages.forEach((message -> {
                if (message instanceof WebhookEventsMessage eventsMessage) {
                    eventsMessage.events().forEach(event -> {
                        if (event.key().equals(CALL_DISCONNECT)) {
                            var callDisconnect = Json.fromJsonNode(event.valueAsJson(), CallDisconnect.class);
                            if (callDisconnect.getDurationInSeconds() > 10) {
                                LOG.info("Show survey on call disconnect");
                                integration.getXapi().executeCommand(
                                    key("UserInterface.Message.Rating.Display"),
                                    new CommandRequest(
                                        eventsMessage.deviceId(),
                                        arguments(
                                            "Title", "Call Satisfaction Survey",
                                            "Text", "How would you rate the quality of your call?",
                                            "Duration", 10,
                                            "FeedbackId", eventsMessage.deviceId()
                                        )
                                    )
                                );
                            }
                        }
                        if (event.key().equals(UI_MESSAGE_RATING_RESPONSE)) {
                            var response = Json.fromJsonNode(event.valueAsJson(), RatingResponse.class);
                            var device = deviceCache.get(response.getFeedbackId());
                            LOG.info("Got call rating response from \"{}\": {}", device.getDisplayName(), response.getRating());
                        }
                    });
                }
            }));
        }));
        poller.start();
    }
}
