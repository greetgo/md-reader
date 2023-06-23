package kz.greetgo.md_reader.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.SneakyThrows;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Execute {
  private final Path currentDir;
  private final Path errTextFile;

  private final List<List<String>> cmdOfCommands = new ArrayList<>();

  public int exitCode = 0;

  private Execute(Path currentDir, Path errTextFile) {
    this.currentDir  = currentDir;
    this.errTextFile = errTextFile;
  }

  public static Execute of(Path currentDir, Path errTextFile) {
    return new Execute(currentDir, errTextFile);
  }

  public Execute cmd(List<String> cmd) {
    cmdOfCommands.add(cmd);
    return this;
  }

  @SneakyThrows
  public Execute execute() {
    List<String> cmd = cmdOfCommands.stream().flatMap(Collection::stream).toList();

    File errFile   = errTextFile.toFile();
    Path outErrTxt = errFile.getParentFile().toPath().resolve(errFile.getName() + ".__out__.txt");

    errFile.getParentFile().mkdirs();

    Process process = new ProcessBuilder().directory(currentDir.toFile())
                                          .command(cmd)
                                          .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                                          .redirectError(ProcessBuilder.Redirect.to(outErrTxt.toFile()))
                                          .start();
    exitCode = process.waitFor();

    try (FileOutputStream txt = new FileOutputStream(errFile)) {

      txt.write(("CURRENT DIR: " + currentDir + "\n\n").getBytes(UTF_8));

      txt.write(("CMD: " + String.join(" ", cmd) + "\n\n").getBytes(UTF_8));

      txt.write(("EXIT_CODE: " + exitCode + "\n\n").getBytes(UTF_8));

      txt.write(("ERR_OUT:\n\n").getBytes(UTF_8));

      try (FileInputStream in = new FileInputStream(outErrTxt.toFile())) {

        byte[] buffer = new byte[16 * 1024];

        while (true) {

          int count = in.read(buffer);
          if (count < 0) {
            return this;
          }

          txt.write(buffer, 0, count);
        }
      }
    }
  }

  public Path err() {
    return errTextFile;
  }
}
