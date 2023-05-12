package com.cisco.workspaceintegrations.common.xapi;

import java.util.Optional;

public final class StatusKeys {

    public static final Key STANDBY_STATE = Key.parse("Standby.State");
    public static final Key NUMBER_OF_ACTIVE_CALLS = Key.parse("SystemUnit.State.NumberOfActiveCalls");
    public static final Key PRESENTATION_SENDING_MODE = Key.parse("Conference.Presentation.LocalInstance[*].SendingMode");
    public static final Key BOOKINGS_AVAILABILITY_STATUS = Key.parse("Bookings.Availability.Status");
    public static final Key HOTDESKING_STATUS = Key.parse("Webex.DevicePersonalization.Hotdesking.SessionStatus");
    public static final Key ROOM_ANALYTICS_CLOSE_PROXIMITY = Key.parse("RoomAnalytics.Engagement.CloseProximity");
    public static final Key ROOM_ANALYTICS_PEOPLE_PRESENCE = Key.parse("RoomAnalytics.PeoplePresence");
    public static final Key ROOM_ANALYTICS_PEOPLE_COUNT = Key.parse("RoomAnalytics.PeopleCount.Current");
    public static final Key ROOM_ANALYTICS_AMBIENT_NOISE = Key.parse("RoomAnalytics.AmbientNoise.Level.A");
    public static final Key ROOM_ANALYTICS_TEMPERATURE = Key.parse("RoomAnalytics.AmbientTemperature");
    public static final Key ROOM_ANALYTICS_REVERB_TIME = Key.parse("RoomAnalytics.ReverberationTime.Middle.RT60");
    public static final Key ROOM_ANALYTICS_SOUND_LEVEL = Key.parse("RoomAnalytics.Sound.Level.A");
    public static final Key ROOM_ANALYTICS_HUMIDITY = Key.parse("RoomAnalytics.RelativeHumidity");
    public static final Key PERIPHERALS_TEMPERATURE = Key.parse("Peripherals.ConnectedDevice[*].RoomAnalytics.AmbientTemperature");
    public static final Key PERIPHERALS_HUMIDITY = Key.parse("Peripherals.ConnectedDevice[*].RoomAnalytics.RelativeHumidity");
    public static final Key PERIPHERALS_AIR_QUALITY = Key.parse("Peripherals.ConnectedDevice[*].RoomAnalytics.AirQuality.Index");

    private StatusKeys() {
    }

    public static Optional<Integer> peripheralDeviceIdFrom(Key key) {
        if (Key.parse("Peripherals.ConnectedDevice[1..n].*").encloses(key)) {
            return Optional.of(key.segments().get(1).array().absoluteIndex());
        }
        return Optional.empty();
    }

    public static Key peripheralsTemperatureKey(int id) {
        return Key.parse("Peripherals.ConnectedDevice[" + id + "].RoomAnalytics.AmbientTemperature");
    }

    public static Key peripheralsHumidityKey(int id) {
        return Key.parse("Peripherals.ConnectedDevice[" + id + "].RoomAnalytics.RelativeHumidity");
    }

    public static Key peripheralsAirQualityKey(int id) {
        return Key.parse("Peripherals.ConnectedDevice[" + id + "].RoomAnalytics.AirQuality.Index");
    }
}
