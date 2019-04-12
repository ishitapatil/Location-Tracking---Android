package com.ishita.locationupdater.utility;

class Listener {

    private String type;
    private IEventHandler handler;

    Listener(String type, IEventHandler handler) {
        this.type = type;
        this.handler = handler;
    }

    String getType() {
        return type;
    }

    IEventHandler getHandler() {
        return handler;
    }
}
