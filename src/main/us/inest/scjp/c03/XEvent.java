package us.inest.scjp.c03;

import java.util.EventObject;
import java.util.Objects;

public class XEvent extends EventObject {
    public XEvent(Objects source) {
        super(source);
    }
}
