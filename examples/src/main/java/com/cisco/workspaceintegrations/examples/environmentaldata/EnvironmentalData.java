package com.cisco.workspaceintegrations.examples.environmentaldata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cisco.workspaceintegrations.api.queue.QueuePoller;
import com.cisco.workspaceintegrations.common.messages.WebhookStatusMessage;
import com.cisco.workspaceintegrations.common.oauth.OAuthClient;
import com.cisco.workspaceintegrations.examples.common.Example;

import static com.cisco.workspaceintegrations.common.xapi.StatusConverter.getObject;
import static com.cisco.workspaceintegrations.common.xapi.StatusKeys.ROOM_ANALYTICS_AMBIENT_NOISE;
import static com.cisco.workspaceintegrations.common.xapi.StatusKeys.ROOM_ANALYTICS_HUMIDITY;
import static com.cisco.workspaceintegrations.common.xapi.StatusKeys.ROOM_ANALYTICS_SOUND_LEVEL;
import static com.cisco.workspaceintegrations.common.xapi.StatusKeys.ROOM_ANALYTICS_TEMPERATURE;
import static com.cisco.workspaceintegrations.common.xapi.StatusKeys.peripheralDeviceIdFrom;
import static com.cisco.workspaceintegrations.common.xapi.StatusKeys.peripheralsAirQualityKey;
import static com.cisco.workspaceintegrations.common.xapi.StatusKeys.peripheralsHumidityKey;
import static com.cisco.workspaceintegrations.common.xapi.StatusKeys.peripheralsTemperatureKey;

public class EnvironmentalData extends Example {

    private static final Logger LOG = LoggerFactory.getLogger(EnvironmentalData.class);

    public EnvironmentalData(OAuthClient oAuthClient) {
        super("EnvironmentalData", oAuthClient);
    }

    @Override
    protected void start() {
        QueuePoller poller = integration.getQueuePoller((messages -> {
            LOG.info("Received messages: " + messages);
            messages.forEach((message) -> {
                if (message instanceof WebhookStatusMessage statusMessage) {
                    statusMessage.getUpdatedStringValue(ROOM_ANALYTICS_TEMPERATURE)
                                 .ifPresent(temp -> {
                                     LOG.info("Temperature updated to {}°C for device {}", temp, statusMessage.deviceId());
                                 });
                    statusMessage.getUpdatedStringValue(ROOM_ANALYTICS_AMBIENT_NOISE)
                                 .ifPresent(ambientNoise -> {
                                     LOG.info("Ambient noise level updated to {} dBA for device {}", ambientNoise, statusMessage.deviceId());
                                 });
                    statusMessage.getUpdatedStringValue(ROOM_ANALYTICS_HUMIDITY)
                                 .ifPresent(humidity -> {
                                     LOG.info("Relative humidity updated to {}% for device {}", humidity, statusMessage.deviceId());
                                 });
                    statusMessage.getUpdatedStringValue(ROOM_ANALYTICS_SOUND_LEVEL)
                                 .ifPresent(humidity -> {
                                     LOG.info("Sound level updated to {} dBA for device {}", humidity, statusMessage.deviceId());
                                 });
                    // Room Navigator sensor data is in the peripherals section
                    statusMessage
                        .changes().updated().keySet()
                        .forEach(k -> peripheralDeviceIdFrom(k)
                            .flatMap(id -> getObject(peripheralsTemperatureKey(id), String.class, statusMessage.changes().updated()))
                            .ifPresent(temp -> {
                                LOG.info("Peripherals temperature updated to {}°C for device {}", temp, statusMessage.deviceId());
                            }));
                    statusMessage
                        .changes().updated().keySet()
                        .forEach(k -> peripheralDeviceIdFrom(k)
                            .flatMap(id -> getObject(peripheralsHumidityKey(id), String.class, statusMessage.changes().updated()))
                            .ifPresent(humidity -> {
                                LOG.info("Peripherals humidity updated to {}% for device {}", humidity, statusMessage.deviceId());
                            }));
                    statusMessage
                        .changes().updated().keySet()
                        .forEach(k -> peripheralDeviceIdFrom(k)
                            .flatMap(id -> getObject(peripheralsAirQualityKey(id), String.class, statusMessage.changes().updated()))
                            .ifPresent(temp -> {
                                LOG.info("Peripherals air quality updated to {} (TVOC) for device {}", temp, statusMessage.deviceId());
                            }));
                }
            });
        }));
        poller.start();
    }
}
