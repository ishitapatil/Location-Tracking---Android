package com.ishita.locationupdater.utility;


public interface IEventDispatcher {

    public void addEventListener(String type, IEventHandler cbInterface);
    public void removeEventListener(String type);
    public void dispatchEvent(int eventType, EventModel event);
    public void removeAllListeners();

}
