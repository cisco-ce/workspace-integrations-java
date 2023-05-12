package com.cisco.workspaceintegrations.api.queue;

import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cisco.workspaceintegrations.api.core.WebexDeveloperApi;
import com.cisco.workspaceintegrations.api.core.WebexHttp;
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
    private Thread workerThread;

    public QueuePoller(WebexHttp webexHttp, URI queueUrl, Consumer<List<Message>> messageConsumer) {
        super(webexHttp, queueUrl);
        this.messageConsumer = messageConsumer;
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
                LOG.debug("Got poll response with {} message(s)", response.getMessages().size());
                consume(response.getMessages());
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
    }
}

