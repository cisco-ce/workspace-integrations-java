package com.cisco.workspaceintegrations.examples.common;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.Optional;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cisco.workspaceintegrations.api.WorkspaceIntegration;
import com.cisco.workspaceintegrations.api.WorkspaceIntegration.InitResult;
import com.cisco.workspaceintegrations.common.actions.Provisioning;
import com.cisco.workspaceintegrations.common.integration.IntegrationUpdate;
import com.cisco.workspaceintegrations.common.json.Json;
import com.cisco.workspaceintegrations.common.oauth.OAuthClient;

import static com.cisco.workspaceintegrations.common.integration.ProvisioningState.COMPLETED;
import static com.cisco.workspaceintegrations.common.integration.Queue.enabledQueue;
import static com.cisco.workspaceintegrations.examples.common.ExampleUtils.readContentFromFile;
import static com.cisco.workspaceintegrations.examples.common.ExampleUtils.writeToFile;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Abstract base class for all the examples
 */
public abstract class Example {

    private static final Logger LOG = LoggerFactory.getLogger(Example.class);

    private final String name;
    private final String provisioningFile;
    protected final WorkspaceIntegration integration;

    public Example(String name, OAuthClient oAuthClient) {
        this.name = name;
        this.provisioningFile = System.getProperty("java.io.tmpdir") + name + ".tmp";
        this.integration = new WorkspaceIntegration(getName(), oAuthClient);
    }

    public String getName() {
        return name;
    }

    public void run() {
        LOG.info("Running the {} example", getName());
        init();
        start();
    }

    /**
     * The example stores the provisioning details (refresh token and other details form the activation code)
     * in a temp file and will initialize from this file on startup.
     * When no provisioning file is found, the activation code must be entered on the command line.
     */
    private void init() {
        Scanner input = new Scanner(System.in, UTF_8);
        readProvisioningFile().ifPresentOrElse(this::init, () -> {
            LOG.info("Please enter the activation code (JWT) from Control Hub, followed by enter: ");
            String jwt = input.next();
            InitResult initResult = init(jwt);
            try {
                writeToFile(provisioningFile, Json.toJsonString(initResult.provisioning()));
            } catch (IOException e) {
                LOG.error("Writing the provisioning file failed");
            }
        });
    }

    protected InitResult init(String activationJwt) {
        return integration.init(
            activationJwt,
            IntegrationUpdate.builder()
                             .provisioningState(COMPLETED)
                             .queue(enabledQueue()) // Enables the change notification queue for long polling
                             .build());
    }

    protected void init(Provisioning provisioning) {
        integration.init(
            provisioning,
            IntegrationUpdate.builder()
                             .provisioningState(COMPLETED)
                             .queue(enabledQueue()) // Enables the change notification queue for long polling
                             .build());
    }

    protected abstract void start();

    private Optional<Provisioning> readProvisioningFile() {
        try {
            String fileContent = readContentFromFile(provisioningFile);
            LOG.info("Found existing provisioning file at {}. Delete this file if you want to activate with a new activation code.",
                     provisioningFile);
            return Optional.of(Json.fromJsonString(fileContent, Provisioning.class));
        } catch (NoSuchFileException ex) {
            LOG.info("No provisioning file found");
        } catch (Exception ex) {
            LOG.error("Error reading provision file: ", ex);
        }
        return Optional.empty();
    }
}
