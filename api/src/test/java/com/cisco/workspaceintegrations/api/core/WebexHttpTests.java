package com.cisco.workspaceintegrations.api.core;

import java.net.URI;
import java.net.http.HttpRequest;

import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.cisco.workspaceintegrations.api.http.HttpException;
import com.cisco.workspaceintegrations.api.utils.MockHttp;
import com.cisco.workspaceintegrations.common.actions.Provisioning;
import com.cisco.workspaceintegrations.common.oauth.OAuthClient;
import com.cisco.workspaceintegrations.common.workspaces.Workspace;

import static com.cisco.workspaceintegrations.api.utils.MockHttp.mockResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WebexHttpTests {

    private WebexHttp webexHttp;
    private MockHttp mockHttp;
    private ArgumentCaptor<HttpRequest> requestCaptor;

    @BeforeMethod
    public void setUp() {
        Provisioning provisioning = mock(Provisioning.class);
        when(provisioning.getRefreshToken()).thenReturn("12345");
        when(provisioning.getOauthUrl()).thenReturn(URI.create("https://integration.webexapis.com/v1/access_token"));
        mockHttp = new MockHttp();
        webexHttp = new WebexHttp(mockHttp, new OAuthClient("fooClient", "barSecret"), provisioning);
        requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
    }

    @Test
    public void testTokenInitIsPerformedInitially() throws Exception {
        when(mockHttp.getClient().send(requestCaptor.capture(), any())).thenReturn(
            mockResponse("{ \"access_token\": \"123abc\", \"refresh_token\": \"12345\" }", 200),
            mockResponse("{}", 200)
        );
        // Run two requests: Initially we need to fetch a new access token from the refresh token and then the two
        // requests will be executed with the returned access token
        webexHttp.get(URI.create("https://integration.webexapis.com/v1/worskpaces/foo"), Workspace.class);
        webexHttp.get(URI.create("https://integration.webexapis.com/v1/worskpaces/bar"), Workspace.class);

        HttpRequest initTokenRequest = requestCaptor.getAllValues().get(0);
        assertThat(initTokenRequest.uri()).isEqualTo(URI.create("https://integration.webexapis.com/v1/access_token"));

        HttpRequest fooRequest = requestCaptor.getAllValues().get(1);
        assertThat(fooRequest.uri()).isEqualTo(URI.create("https://integration.webexapis.com/v1/worskpaces/foo"));
        assertThat(fooRequest.headers().firstValue("Authorization").get()).isEqualTo("Bearer 123abc");
        assertThat(fooRequest.headers().firstValue("Content-Type").get()).isEqualTo("application/json");

        HttpRequest barRequest = requestCaptor.getAllValues().get(2);
        assertThat(barRequest.uri()).isEqualTo(URI.create("https://integration.webexapis.com/v1/worskpaces/bar"));
        assertThat(barRequest.headers().firstValue("Authorization").get()).isEqualTo("Bearer 123abc");
        assertThat(barRequest.headers().firstValue("Content-Type").get()).isEqualTo("application/json");
    }

    @Test
    public void testTokenInitIsPerformedOn401Unauthorized() throws Exception {
        when(mockHttp.getClient().send(requestCaptor.capture(), any())).thenReturn(
            mockResponse("{ \"access_token\": \"123abc\", \"refresh_token\": \"12345\" }", 200),
            mockResponse("{}", 200),
            mockResponse("{}", 401),
            mockResponse("{ \"access_token\": \"456def\", \"refresh_token\": \"12345\" }", 200),
            mockResponse("{}", 200)
        );
        // 1. Initially we fetch a new access token from the refresh token
        // 2. The next requests is OK
        // 3. The following request fails with a 401 which should trigger a new token fetch
        webexHttp.get(URI.create("https://integration.webexapis.com/v1/worskpaces/foo"), Workspace.class);
        webexHttp.get(URI.create("https://integration.webexapis.com/v1/worskpaces/bar"), Workspace.class);

        HttpRequest initTokenRequest = requestCaptor.getAllValues().get(0);
        assertThat(initTokenRequest.uri()).isEqualTo(URI.create("https://integration.webexapis.com/v1/access_token"));

        HttpRequest fooRequest = requestCaptor.getAllValues().get(1);
        assertThat(fooRequest.uri()).isEqualTo(URI.create("https://integration.webexapis.com/v1/worskpaces/foo"));
        assertThat(fooRequest.headers().firstValue("Authorization").get()).isEqualTo("Bearer 123abc");

        HttpRequest barRequest = requestCaptor.getAllValues().get(2);
        assertThat(barRequest.uri()).isEqualTo(URI.create("https://integration.webexapis.com/v1/worskpaces/bar"));
        assertThat(barRequest.headers().firstValue("Authorization").get()).isEqualTo("Bearer 123abc");

        HttpRequest initTokenRequestOn401 = requestCaptor.getAllValues().get(3);
        assertThat(initTokenRequestOn401.uri()).isEqualTo(URI.create("https://integration.webexapis.com/v1/access_token"));

        HttpRequest barRequestRetried = requestCaptor.getAllValues().get(4);
        assertThat(barRequestRetried.uri()).isEqualTo(URI.create("https://integration.webexapis.com/v1/worskpaces/bar"));
        assertThat(barRequestRetried.headers().firstValue("Authorization").get()).isEqualTo("Bearer 456def");
        assertThat(barRequestRetried.headers().firstValue("Content-Type").get()).isEqualTo("application/json");
    }

    @Test
    public void testHttpExceptions() throws Exception {
        when(mockHttp.getClient().send(requestCaptor.capture(), any())).thenReturn(
            mockResponse("Bad boy!", 500)
        );
        HttpException ex = catchThrowableOfType(
            () -> webexHttp.get(URI.create("https://integration.webexapis.com/v1/worskpaces/foo"), Workspace.class),
            HttpException.class);
        assertThat(ex.getStatusCode()).isEqualTo(500);
        assertThat(ex.isInternalServerError()).isTrue();
        assertThat(ex.getTrackingId().get()).isEqualTo("MockTrackingId");
    }
}
