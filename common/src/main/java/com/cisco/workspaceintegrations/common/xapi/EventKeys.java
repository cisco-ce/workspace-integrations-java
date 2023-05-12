package com.cisco.workspaceintegrations.common.xapi;

public final class EventKeys {

    public static final Key BOOT_EVENT = Key.parse("BootEvent");
    public static final Key CALL_DISCONNECT = Key.parse("CallDisconnect");
    public static final Key CALL_SUCCESSFUL = Key.parse("CallSuccessful");
    public static final Key UI_MESSAGE_PROMPT_RESPONSE = Key.parse("UserInterface.Message.Prompt.Response");
    public static final Key UI_MESSAGE_RATING_RESPONSE = Key.parse("UserInterface.Message.Rating.Response");
    public static final Key UI_MESSAGE_TEXT_INPUT_RESPONSE = Key.parse("UserInterface.Message.TextInput.Response");
    public static final Key UI_EXTENSIONS_PANEL_CLICKED = Key.parse("UserInterface.Extensions.Panel.Clicked");
    public static final Key UI_EXTENSIONS_WIDGET_ACTION = Key.parse("UserInterface.Extensions.Widget.Action");
    public static final Key UI_ASSISTANT_NOTIFICATION = Key.parse("UserInterface.Assistant.Notification");

    private EventKeys() {
    }
}
