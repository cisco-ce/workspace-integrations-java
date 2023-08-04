package com.cisco.workspaceintegrations.api.core;

import com.cisco.workspaceintegrations.common.actions.UpdateApproved;

/**
 * Callback for changes to the integration provisioning details.
 * Any changes must be persisted.
 */
public interface ProvisioningChangedListener {

    /**
     * The refresh token can change when a customer is moved from one data locality region to another (say US to EU)
     */
    void refreshTokenChanged(String newRefreshToken);

    /**
     * Received when a new manifest version is approved by the administrator
     * The integration should update the provisioning accordingly (say by reading the new details from the appUrl GET)
     * <p>
     * Note: Will be called only for integrations that use a queue for change notifications.
     * Webhook based integrations must handle this in the webhook code specifically.
     */
    void updateApproved(UpdateApproved updateApproved);
}
