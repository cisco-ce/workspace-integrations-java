package com.cisco.workspaceintegrations.api;

import java.net.URI;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cisco.workspaceintegrations.api.core.HttpJWKSetProvider;
import com.cisco.workspaceintegrations.api.core.ProvisioningChangedListener;
import com.cisco.workspaceintegrations.api.core.WebexHttp;
import com.cisco.workspaceintegrations.api.devices.DevicesApi;
import com.cisco.workspaceintegrations.api.http.Http;
import com.cisco.workspaceintegrations.api.integration.IntegrationApi;
import com.cisco.workspaceintegrations.api.locations.WorkspaceLocationsApi;
import com.cisco.workspaceintegrations.api.queue.QueuePoller;
import com.cisco.workspaceintegrations.api.workspaces.WorkspacesApi;
import com.cisco.workspaceintegrations.api.xapi.XAPI;
import com.cisco.workspaceintegrations.common.actions.JwtDecoder;
import com.cisco.workspaceintegrations.common.actions.Provisioning;
import com.cisco.workspaceintegrations.common.integration.Integration;
import com.cisco.workspaceintegrations.common.integration.IntegrationUpdate;
import com.cisco.workspaceintegrations.common.messages.Message;
import com.cisco.workspaceintegrations.common.oauth.OAuthClient;

/**
 * The WorkspaceIntegration class provides a convenient abstraction on a workspace integration.
 * <p>
 * On init:
 * <p>
 * - Decodes an activation code from Control Hub
 * - Updates the Webex backend with the status of the integration
 * - Constructs developer API abstractions or the raw WebexHttp class for accessing the developer.webex.com APIs
 */
public class WorkspaceIntegration {

    private static final Logger LOG = LoggerFactory.getLogger(WorkspaceIntegration.class);

    private final Http http;

    private final JwtDecoder jwtDecoder;
    private final OAuthClient oauthClient;
    private final ProvisioningChangedListener provisioningChangedListener;
    private WebexHttp webexHttp;
    private IntegrationApi integrationApi;
    private WorkspacesApi workspacesApi;
    private XAPI xapi;
    private DevicesApi devicesApi;
    private WorkspaceLocationsApi workspaceLocationsApi;
    private QueuePoller queuePoller;
    private URI queueUrl;
    private Provisioning provisioning;

    public WorkspaceIntegration(String userAgent,
                                OAuthClient oauthClient,
                                ProvisioningChangedListener provisioningChangedListener) {
        this(oauthClient, new Http(userAgent), provisioningChangedListener);
    }

    public WorkspaceIntegration(OAuthClient oauthClient,
                                Http http,
                                ProvisioningChangedListener provisioningChangedListener) {
        this.oauthClient = oauthClient;
        this.http = http;
        this.provisioningChangedListener = provisioningChangedListener;
        this.jwtDecoder = new JwtDecoder(new HttpJWKSetProvider(http));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Shutdown detected");
            if (queuePoller != null) {
                queuePoller.stop();
            }
        }));
    }

    /**
     * Init from a new activation code. The code is verified and decoded to the provisioning object.
     * For later inits, the provisioning object should be stored and used.
     *
     * @param activationJwt The activation code from Control Hub
     * @param initialUpdate The update sent to the appUrl
     * @return The provisioning and integration output
     */
    public InitResult init(String activationJwt, IntegrationUpdate initialUpdate) {
        LOG.info("Initializing from activation code JWT");
        Provisioning provisioning = (Provisioning) jwtDecoder.decodeAction(activationJwt);
        return initApis(provisioning, initialUpdate);
    }

    /**
     * Init from a previous activation
     *
     * @param provisioning  The provisioning from the initial activation
     * @param initialUpdate The update that will be sent to the appUrl
     * @return The provisioning and integration output
     */
    public InitResult init(Provisioning provisioning, IntegrationUpdate initialUpdate) {
        LOG.info("Initializing from existing provisioning");
        return initApis(provisioning, initialUpdate);
    }

    public Http getHttp() {
        return http;
    }

    public JwtDecoder getJwtDecoder() {
        return jwtDecoder;
    }

    public OAuthClient getOauthClient() {
        return oauthClient;
    }

    public WebexHttp getWebexHttp() {
        return webexHttp;
    }

    public Provisioning getProvisioning() {
        return provisioning;
    }

    public IntegrationApi getIntegrationApi() {
        return integrationApi;
    }

    public WorkspacesApi getWorkspacesApi() {
        return workspacesApi;
    }

    public XAPI getXapi() {
        return xapi;
    }

    public DevicesApi getDevicesApi() {
        return devicesApi;
    }

    public WorkspaceLocationsApi getWorkspaceLocationsApi() {
        return workspaceLocationsApi;
    }

    public QueuePoller getQueuePoller(Consumer<List<Message>> consumer) {
        if (queuePoller == null) {
            if (queueUrl == null) {
                throw new IllegalStateException(
                    "The queueUrl has not been initialized. Make sure to init with an update that enables a queue: "
                        + "IntegrationUpdate.builder().queue(enabledQueue()).build()"
                );
            }
            queuePoller = new QueuePoller(webexHttp, queueUrl, consumer, provisioningChangedListener, jwtDecoder);
        }
        return queuePoller;
    }

    public QueuePoller getQueuePoller(URI queueUrl, Consumer<List<Message>> consumer) {
        if (queuePoller == null) {
            this.queueUrl = queueUrl;
            queuePoller = new QueuePoller(webexHttp, queueUrl, consumer, provisioningChangedListener, jwtDecoder);
        }
        return queuePoller;
    }

    private InitResult initApis(Provisioning provisioning, IntegrationUpdate initialUpdate) {
        this.provisioning = provisioning;
        this.webexHttp = new WebexHttp(http, oauthClient, provisioning, provisioningChangedListener);
        this.webexHttp.initTokens();
        this.integrationApi = new IntegrationApi(webexHttp);
        this.workspacesApi = new WorkspacesApi(webexHttp);
        this.xapi = new XAPI(webexHttp);
        this.devicesApi = new DevicesApi(webexHttp);
        this.workspaceLocationsApi = new WorkspaceLocationsApi(webexHttp);
        this.jwtDecoder.setDefaultRegion(provisioning.getRegion());

        Integration integration = integrationApi.postUpdate(initialUpdate);
        integration.getQueue().ifPresent(queue -> {
            if (queue.isEnabled() && queue.getPollUrl() != null) {
                this.queueUrl = URI.create(queue.getPollUrl());
            }
        });
        return new InitResult(provisioning, integration);
    }

    public record InitResult(Provisioning provisioning, Integration integration) {
    }
}
