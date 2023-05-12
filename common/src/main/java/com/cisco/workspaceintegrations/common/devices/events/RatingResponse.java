package com.cisco.workspaceintegrations.common.devices.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.cisco.workspaceintegrations.common.ValueObject;

public final class RatingResponse extends ValueObject {
    private final String feedbackId;
    private final int rating;

    @JsonCreator
    private RatingResponse(@JsonProperty("FeedbackId") String feedbackId, @JsonProperty("Rating") int rating) {
        this.feedbackId = feedbackId;
        this.rating = rating;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public int getRating() {
        return rating;
    }
}
