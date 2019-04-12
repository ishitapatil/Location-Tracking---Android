package com.ishita.locationupdater.utility;

import java.util.ArrayList;
import java.util.Objects;

public class EventDispatcher implements IEventDispatcher {

    private ArrayList<Listener> listenerList = new ArrayList<>();
    private Class<? extends EventDispatcher> context;
    private static EventDispatcher instance;
    private EventDispatcher(){

    }

    public static EventDispatcher getInstance(){
        if (instance == null) {
            instance= new EventDispatcher();
        }
        return instance;
    }

    @Override
    public void addEventListener(String type, IEventHandler cbInterface) {

        Listener listener = new Listener(type,cbInterface);
        removeEventListener(type);
        listenerList.add(0,listener);

    }

    @Override
    public void removeEventListener(String type) {

        for (Listener listener : listenerList) {

            if (Objects.equals(listener.getType(), type)) {
                listenerList.remove(listener);
            }
        }

    }

    @Override
    public void dispatchEvent(int eventType, EventModel event) {

        for (Listener listener : listenerList) {       /** replacement of "for(Iterator<Listener> iterator = listenerList.iterator();iterator.hasNext();)" with "for-each"*/

            if (Objects.equals(event.getStrType(), listener.getType())) {
                IEventHandler handler = listener.getHandler();
                handler.callback(eventType,event);
            }
        }
    }

    /*@Override
    public Boolean hasEventListener(String type) {
        return false;
    }*/

    @Override
    public void removeAllListeners() {
        for (Listener listener : listenerList) {

            listenerList.remove(listener);
            removeAllListeners();
        }
    }
}
