package kz.greetgo.md_reader.core;

public interface NowSource {

  long nowMs();

  NowSource CURRENT_TIME_MS = System::currentTimeMillis;
}
