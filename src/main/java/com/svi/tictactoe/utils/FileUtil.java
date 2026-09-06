package com.svi.tictactoe.utils;

import java.nio.file.Path;

public class FileUtil {
  public static boolean isTxtFile(Path path) {
    return path.getFileName()
            .toString()
            .endsWith(".txt");
  }

  public static java.util.List<String> getFileNamesWithoutExtension(Path directoryPath, String errorMessage) {
    if (!java.nio.file.Files.exists(directoryPath)) {
      return java.util.Collections.emptyList();
    }

    try (java.util.stream.Stream<Path> paths = java.nio.file.Files.list(directoryPath)) {
      return paths
              .filter(java.nio.file.Files::isRegularFile)
              .filter(FileUtil::isTxtFile)
              .map(path -> {
                String name = path.getFileName().toString();
                return name.substring(0, name.length() - 4);
              })
              .collect(java.util.stream.Collectors.toList());
    } catch (java.io.IOException e) {
      throw new RuntimeException(errorMessage, e);
    }
  }
}
