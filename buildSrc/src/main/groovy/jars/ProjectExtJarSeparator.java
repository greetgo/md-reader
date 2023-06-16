package jars;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputFiles;
import org.gradle.api.tasks.TaskAction;
import util.FileUtil;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static util.FileUtil.copyToDir;
import static util.FileUtil.isParent;

public abstract class ProjectExtJarSeparator extends DefaultTask {

  private final List<File> inputFileJars  = new ArrayList<>();
  private final List<File> outputFileJars = new ArrayList<>();

  private Path   rootDir;
  private Path   distDir;
  private String mainClassName;
  private String jarBaseName;
  private String version;

  private String projectDirName = "project";
  private String externDirName  = "extern";

  @SuppressWarnings("unused")
  public void projectDirName(String name) {
    projectDirName = name;
  }

  @SuppressWarnings("unused")
  public void externDirName(String name) {
    externDirName = name;
  }

  @SuppressWarnings("unused")
  @OutputFiles
  public List<File> getOutputFileJars() {
    return outputFileJars;
  }

  @InputFiles
  public List<File> getInputFileJars() {
    return inputFileJars;
  }

  public void addFileJar(File file) {
    getInputFileJars().add(file);
  }

  public void rootDir(File file) {
    rootDir = file.toPath();
  }

  public void distDir(File file) {
    distDir = file.toPath();
  }

  public void mainClass(String mainClassName) {
    this.mainClassName = mainClassName;
  }

  public void jarBaseName(String jarBaseName) {
    this.jarBaseName = jarBaseName;
  }

  public void version(String version) {
    this.version = version;
  }

  boolean isMine(File file) {
    Path localJarsPath = rootDir.resolve("jars");
    return isParent(rootDir, file) && !isParent(localJarsPath, file);
  }

  @TaskAction
  public void execute() throws Exception {

    FileUtil.removeFileOrDir(distDir);

    Path projectJars = distDir.resolve(projectDirName);
    Path externJars  = distDir.resolve(externDirName);

    for (File jar : inputFileJars) {
      outputFileJars.add(copyToDir(jar, isMine(jar) ? projectJars : externJars));
    }

    List<Path> projectJarList;

    try (var s = Files.list(projectJars)) {
      projectJarList = s.filter(fn -> fn.toFile().getName().toLowerCase().endsWith(".jar"))
                        .sorted()
                        .collect(toList());
    }

    List<Path> externJarList;

    try (var s = Files.list(externJars)) {
      externJarList = s.filter(fn -> fn.toFile().getName().toLowerCase().endsWith(".jar"))
                       .sorted()
                       .collect(toList());
    }

    String classPath = Stream.concat(projectJarList.stream(), externJarList.stream())
                             .map(distDir::relativize)
                             .map(Path::toString)
                             .collect(joining(" "));

    Manifest manifest = new Manifest();
    manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
    manifest.getMainAttributes().put(Attributes.Name.CLASS_PATH, classPath);

    if (mainClassName != null && mainClassName.length() > 0) {
      manifest.getMainAttributes().put(Attributes.Name.MAIN_CLASS, mainClassName);
    }

    File mainJarFile = distDir.resolve(jarName()).toFile();
    mainJarFile.getParentFile().mkdirs();
    try (var jarStream = new JarOutputStream(new FileOutputStream(mainJarFile))) {
      jarStream.putNextEntry(new ZipEntry("META-INF/"));
      {
        jarStream.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
        manifest.write(jarStream);
        jarStream.closeEntry();
      }
      jarStream.closeEntry();
    }
  }

  private String jarName() {
    return jarBaseName + (version == null ? "" : '-' + version) + ".jar";
  }

}
