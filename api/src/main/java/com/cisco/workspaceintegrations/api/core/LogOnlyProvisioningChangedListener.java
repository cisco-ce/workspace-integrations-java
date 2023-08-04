package com.cisco.workspaceintegrations.api.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cisco.workspaceintegrations.api.queue.QueuePoller;
import com.cisco.workspaceintegrations.common.actions.UpdateApproved;

/**
 * Simple implementation that only logs the changes.
 * Do not use in production!
 */
public class LogOnlyProvisioningChangedListener implements ProvisioningChangedListener {

    private static final Logger LOG = LoggerFactory.getLogger(QueuePoller.class);

    public LogOnlyProvisioningChangedListener() {
        LOG.warn("Do not use the logging only listener in production!");
    }

    @Override
    public void refreshTokenChanged(String newRefreshToken) {
        LOG.info("Refresh token changed");
    }

    @Override
    public void updateApproved(UpdateApproved updateApproved) {
        LOG.info("Manifest {}", updateApproved);
    }
}
