package com.cisco.workspaceintegrations.api.core;

import java.net.URI;

public abstract class WebexDeveloperApi {

    private final WebexHttp webexHttp;
    private final URI baseUrl;

    public WebexDeveloperApi(WebexHttp webexHttp, String apiRoot) {
        this.webexHttp = webexHttp;
        this.baseUrl = URI.create(webexHttp.getProvisioning().getWebexApisBaseUrl() + "/" + apiRoot);
    }

    public WebexDeveloperApi(WebexHttp webexHttp, URI baseUrl) {
        this.webexHttp = webexHttp;
        this.baseUrl = baseUrl;
    }

    protected URI urlFor(String path) {
        return URI.create(baseUrl + "/" + path);
    }

    public WebexHttp getWebexHttp() {
        return webexHttp;
    }

    public URI getBaseUrl() {
        return baseUrl;
    }
}
