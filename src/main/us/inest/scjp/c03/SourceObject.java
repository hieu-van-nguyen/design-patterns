package us.inest.scjp.c03;

import java.util.ArrayList;
import java.util.List;

public class SourceObject {
    private final List<XListener> listeners;
    public SourceObject() {
        listeners = new ArrayList<>();
    }
    public synchronized void addXListener(XListener listener) {
        // TODO
        listeners.add(listener);
    }
    public synchronized void removeXListener(XListener listener) {
        // TODO
        listeners.remove(listener);
    }

    public void notifyXEvent() {
        for (XListener listener : listeners) {
            listener.methodAInXListener(null);
        }
    }
}
