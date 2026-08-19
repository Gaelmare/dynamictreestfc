package org.labellum.mc.dttfc.content;

public interface LastProcessedTick {
  long getLastProcessedTick();

  void setLastProcessedTick(long lastProcessedTick);

  boolean haveLastProcessedTick();
}
