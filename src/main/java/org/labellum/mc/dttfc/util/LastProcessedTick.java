package org.labellum.mc.dttfc.util;

public interface LastProcessedTick {
    long getLastProcessedTick();

    void setLastProcessedTick(long lastProcessedTick);

    boolean haveLastProcessedTick();
}
