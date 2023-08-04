package com.cisco.workspaceintegrations.common.actions;

import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.cisco.workspaceintegrations.common.jwt.JWTInvalidException;

import static com.cisco.workspaceintegrations.common.xapi.Key.key;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class JwtDecoderTests {

    private final String wrongKeys = "{\"keys\":[{\"kty\":\"EC\",\"use\":\"sig\",\"crv\":\"P-256\",\"kid\":\"Afu2e0V8nDOOblVkuhmKijIK\",\"key_ops\":[\"verify\"],\"x\":\"uF1Fixjr89H-bmHnBV5lw_z-XSN18pjVqBTMDAXHBBk\",\"y\":\"jvLw8HL2TOQCm1rpp337GRvz_KSHt6j2S9TmuNMRsLg\",\"alg\":\"ES256\"},{\"kty\":\"EC\",\"use\":\"sig\",\"crv\":\"P-256\",\"kid\":\"Q4wkx3z85BTcUXVsMGpX154b\",\"key_ops\":[\"verify\"],\"x\":\"tNtRMTuHnLZRoLR46OGmthOoV1beRA9O0CCmwT4cF_U\",\"y\":\"j0E-HTzyLJFU_1PcA0hYroHAqbHpIHdWNiG7ocquZWE\",\"alg\":\"ES256\"},{\"kty\":\"EC\",\"use\":\"sig\",\"crv\":\"P-256\",\"kid\":\"PJs4BWAozAn7nef41GlWACEV\",\"key_ops\":[\"verify\"],\"x\":\"8702im35Tm0KHvg_WbYUeGff0VHMesWLWgfbrQZu2rk\",\"y\":\"NmFnXkN093h8240xzDL3W3IoFZGg2HDLuuNrysfndzA\",\"alg\":\"ES256\"}]}";
    private final String correctKeys = "{\"keys\":[{\"kty\":\"EC\",\"use\":\"sig\",\"crv\":\"P-256\",\"kid\":\"flFj9H9iErxPbyctlmqA9PlZ\",\"key_ops\":[\"verify\"],\"x\":\"5G6yjJRAHvbAhIgPu_ctAIfiksoFZT8MDQNU60hUQd4\",\"y\":\"56zfhOKBlmpakdBHvDo_OaY_a6LJmcz0c5sXcE8KezI\",\"alg\":\"ES256\"},{\"kty\":\"EC\",\"use\":\"sig\",\"crv\":\"P-256\",\"kid\":\"pouQGkfpKyw5i2Dmz1ncTExi\",\"key_ops\":[\"verify\"],\"x\":\"0nbgQ-C9s7FFFmOFzSKP_plUM2u_4FRCbmkSThKek5Q\",\"y\":\"pHL_7EJLKGNtHf5lB3uhmueoAJ7reUb2uZJkk2vot2Q\",\"alg\":\"ES256\"},{\"kty\":\"EC\",\"use\":\"sig\",\"crv\":\"P-256\",\"kid\":\"Y5GqYgR1ztZIxEYGach6GwRN\",\"key_ops\":[\"verify\"],\"x\":\"6jFYi36ICz2WUOtJ1Wwlx0SjvzZTNWXJGtiWlPidBSs\",\"y\":\"iNmQIRWnlWYYRaYtK5uxalGIqKxeIgDQzmWLgTIYG3E\",\"alg\":\"ES256\"}]}";
    private final String activationJwt = "eyJraWQiOiJwb3VRR2tmcEt5dzVpMkRtejFuY1RFeGkiLCJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiJ9.eyJzdWIiOiJZMmx6WTI5emNHRnlhem92TDNWeWJqcFVSVUZOT25WekxXVmhjM1F0TVY5cGJuUXhNeTlQVWtkQlRrbGFRVlJKVDA0dk0yRTJabVl6TnpNdE5qaGhOeTAwTkdVMExUa3haRFl0WVRJM05EWXdaVEJoWXpWaiIsIm9hdXRoVXJsIjoiaHR0cHM6Ly9pbnRlZ3JhdGlvbi53ZWJleGFwaXMuY29tL3YxL2FjY2Vzc190b2tlbiIsIm9yZ05hbWUiOiJBdGxhcyBEZXZpY2UgTWFuYWdlbWVudCB0ZXN0IG9yZyIsImFwcFVybCI6Imh0dHBzOi8veGFwaS1pbnRiLmNpc2Nvc3BhcmsuY29tL3hhcGkvYXBpL29yZ2FuaXphdGlvbnMvM2E2ZmYzNzMtNjhhNy00NGU0LTkxZDYtYTI3NDYwZTBhYzVjL2FwcHMvYmM2YjY5NzItNTM4ZS0xMWVjLWJmNjMtMDI0MmFjMTMwMDAyIiwibWFuaWZlc3RVcmwiOiJodHRwczovL3hhcGktaW50Yi5jaXNjb3NwYXJrLmNvbS94YXBpL2FwaS9vcmdhbml6YXRpb25zLzNhNmZmMzczLTY4YTctNDRlNC05MWQ2LWEyNzQ2MGUwYWM1Yy9hcHBNYW5pZmVzdHMvYmM2YjY5NzItNTM4ZS0xMWVjLWJmNjMtMDI0MmFjMTMwMDAyIiwiYXBwSWQiOiJiYzZiNjk3Mi01MzhlLTExZWMtYmY2My0wMjQyYWMxMzAwMDIiLCJleHBpcnlUaW1lIjoiMjAyMy0wNS0xMVQwOTo1ODozNy4zNDk0NDQyNzBaIiwiYWN0aW9uIjoicHJvdmlzaW9uIiwid2ViZXhhcGlzQmFzZVVybCI6Imh0dHBzOi8vaW50ZWdyYXRpb24ud2ViZXhhcGlzLmNvbS92MSIsInNjb3BlcyI6InNwYXJrLWFkbWluOmRldmljZXNfcmVhZCxzcGFyazp4YXBpX3N0YXR1c2VzLHNwYXJrLWFkbWluOndvcmtzcGFjZXNfcmVhZCxzcGFyazp4YXBpX2NvbW1hbmRzIiwicmVnaW9uIjoidXMtZWFzdC0xX2ludDEzIiwiaWF0IjoxNjgzNzEyNzE3LCJqdGkiOiJYSVJlY1FjT1N0Q0tlbnQ0VlFEZVVBPT0iLCJyZWZyZXNoVG9rZW4iOiJZMlV4TlRnMll6RXRaVGczTVMwME9HRmpMVGt3TVRNdE5HSmhZall3WlROalpqZzBOelUwTkdNME5UY3RNRE5qX0E1MkRfM2E2ZmYzNzMtNjhhNy00NGU0LTkxZDYtYTI3NDYwZTBhYzVjIiwieGFwaUFjY2VzcyI6IntcImNvbW1hbmRzXCI6W1wiTWVzc2FnZS5TZW5kXCJdLFwic3RhdHVzZXNcIjpbXCJSb29tQW5hbHl0aWNzLipcIixcIlBlcmlwaGVyYWxzLkNvbm5lY3RlZERldmljZVsqXS5Sb29tQW5hbHl0aWNzLipcIixcIlN5c3RlbVVuaXQuU3RhdGUuTnVtYmVyT2ZBY3RpdmVDYWxsc1wiLFwiU3RhbmRieS5TdGF0ZVwiXSxcImV2ZW50c1wiOltcIlVzZXJJbnRlcmZhY2UuTWVzc2FnZS5Qcm9tcHQuUmVzcG9uc2VcIixcIkJvb3RFdmVudFwiXX0ifQ.hVZiYKqmF5bo99ZLtDn3x0bu7DcRBzBGeOdbFd9HO40GHLDD12gvehZ4b1Wk0TRVuoVaMrBNpbnF5KgzQGb6GQ";
    private JwtDecoder decoder;

    @BeforeMethod
    public void setUp() {
        this.decoder = new JwtDecoder(uri -> correctKeys);
    }

    @Test
    public void testActivationJwtDecoding() {
        this.decoder = new JwtDecoder(uri -> correctKeys, true);
        Provisioning provisioning = (Provisioning) this.decoder.decodeAction(activationJwt);
        assertThat(provisioning.getOrgId()).isEqualTo("Y2lzY29zcGFyazovL3VybjpURUFNOnVzLWVhc3QtMV9pbnQxMy9PUkdBTklaQVRJT04vM2E2ZmYzNzMtNjhhNy00NGU0LTkxZDYtYTI3NDYwZTBhYzVj");
        assertThat(provisioning.getOrgName()).isEqualTo("Atlas Device Management test org");
        assertThat(provisioning.getAppId()).isEqualTo("bc6b6972-538e-11ec-bf63-0242ac130002");
        assertThat(provisioning.getAppUrl().toString()).isEqualTo("https://xapi-intb.ciscospark.com/xapi/api/organizations/3a6ff373-68a7-44e4-91d6-a27460e0ac5c/apps/bc6b6972-538e-11ec-bf63-0242ac130002");
        assertThat(provisioning.getOauthUrl().toString()).isEqualTo("https://integration.webexapis.com/v1/access_token");
        assertThat(provisioning.getManifestUrl().toString()).isEqualTo("https://xapi-intb.ciscospark.com/xapi/api/organizations/3a6ff373-68a7-44e4-91d6-a27460e0ac5c/appManifests/bc6b6972-538e-11ec-bf63-0242ac130002");
        assertThat(provisioning.getWebexApisBaseUrl().toString()).isEqualTo("https://integration.webexapis.com/v1");
        assertThat(provisioning.getRegion()).isEqualTo("us-east-1_int13");
        assertThat(provisioning.getRefreshToken()).isEqualTo("Y2UxNTg2YzEtZTg3MS00OGFjLTkwMTMtNGJhYjYwZTNjZjg0NzU0NGM0NTctMDNj_A52D_3a6ff373-68a7-44e4-91d6-a27460e0ac5c");
        assertThat(provisioning.getScopes()).isEqualTo(
            Set.of("spark-admin:devices_read", "spark:xapi_statuses", "spark-admin:workspaces_read", "spark:xapi_commands")
        );
        assertThat(provisioning.getXapiAccess().getCommands()).isEqualTo(
            Set.of(key("Message.Send"))
        );
        assertThat(provisioning.getXapiAccess().getStatuses()).isEqualTo(
            Set.of(key("RoomAnalytics.*"),
                   key("Peripherals.ConnectedDevice[*].RoomAnalytics.*"),
                   key("SystemUnit.State.NumberOfActiveCalls"),
                   key("Standby.State"))
        );
        assertThat(provisioning.getXapiAccess().getEvents()).isEqualTo(
            Set.of(key("UserInterface.Message.Prompt.Response"), key("BootEvent"))
        );
    }

    @Test
    public void testWrongKeys() {
        this.decoder = new JwtDecoder(uri -> wrongKeys);
        assertThatExceptionOfType(JWTInvalidException.class).isThrownBy(() -> {
            this.decoder.decodeAction(activationJwt);
        }).withMessageContaining("No such key");
    }

    @Test
    public void testExpired() {
        assertThatExceptionOfType(JwtExpiredException.class).isThrownBy(() -> {
            this.decoder.decodeAction(activationJwt);
        }).withMessageContaining("The activation code is expired");
    }
}
