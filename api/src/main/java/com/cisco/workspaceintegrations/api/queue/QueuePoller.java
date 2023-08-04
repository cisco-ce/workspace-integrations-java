package com.cisco.workspaceintegrations.api.queue;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cisco.workspaceintegrations.api.core.ProvisioningChangedListener;
import com.cisco.workspaceintegrations.api.core.WebexDeveloperApi;
import com.cisco.workspaceintegrations.api.core.WebexHttp;
import com.cisco.workspaceintegrations.common.actions.Action;
import com.cisco.workspaceintegrations.common.actions.JwtDecoder;
import com.cisco.workspaceintegrations.common.actions.UpdateApproved;
import com.cisco.workspaceintegrations.common.messages.ActionMessage;
import com.cisco.workspaceintegrations.common.messages.Message;

import static java.lang.Thread.sleep;

/**
 * Implements long polling of the devices change notification queue.
 * Messages received will be passed on to the messageConsumer.
 */
public class QueuePoller extends WebexDeveloperApi {
    private static final Logger LOG = LoggerFactory.getLogger(QueuePoller.class);
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final Consumer<List<Message>> messageConsumer;
    private final ProvisioningChangedListener provisioningChangedListener;
    private final JwtDecoder jwtDecoder;
    private Thread workerThread;

    public QueuePoller(WebexHttp webexHttp,
                       URI queueUrl,
                       Consumer<List<Message>> messageConsumer,
                       ProvisioningChangedListener provisioningChangedListener,
                       JwtDecoder jwtDecoder) {
        super(webexHttp, queueUrl);
        this.messageConsumer = messageConsumer;
        this.provisioningChangedListener = provisioningChangedListener;
        this.jwtDecoder = jwtDecoder;
    }

    public void start() {
        LOG.info("Starting integration queue poller (change notification long polling)");
        isRunning.set(true);
        workerThread = new Thread(this::pollerLoop, "queue-poller");
        workerThread.setUncaughtExceptionHandler((th, ex) -> LOG.error("Unhandled queue poller error", ex));
        workerThread.start();
    }

    public void stop() {
        LOG.info("Stopping integration queue poller");
        isRunning.set(false);
        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    private void pollerLoop() {
        while (isRunning.get()) {
            try {
                QueuePollResponse response = this.getWebexHttp().get(getBaseUrl(), QueuePollResponse.class);
                List<Message> messages = response.getMessages();
                LOG.debug("Got poll response with {} message(s)", messages.size());
                consume(messages);
            } catch (Exception ex) {
                LOG.error("Unexpected error. Let's wait a bit and start another loop", ex);
                try {
                    sleep(10000);
                } catch (InterruptedException ie) {
                    LOG.info("Interrupt detected, stopping");
                    break;
                }
            }
        }
        LOG.info("Queue poller loop ended");
    }

    private void consume(List<Message> messages) {
        try {
            messageConsumer.accept(messages);
        } catch (Exception ex) {
            LOG.error("Unexpected error in message consumer", ex);
        }
        try {
            handleUpdateApprovedMessage(messages);
        } catch (Exception ex) {
            LOG.error("Unexpected error handling the update approved message", ex);
        }
    }

    private void handleUpdateApprovedMessage(List<Message> messages) {
        messages.stream().filter(m -> m instanceof ActionMessage)
                .reduce((first, second) -> second)
                .ifPresent(actionMessage -> {
                    Action action = jwtDecoder.decodeAction(((ActionMessage) actionMessage).getJwt());
                    if (action instanceof UpdateApproved) {
                        provisioningChangedListener.updateApproved((UpdateApproved) action);
                    }
                });
    }
}
