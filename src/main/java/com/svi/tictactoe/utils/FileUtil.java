package com.svi.tictactoe.utils;

import java.nio.file.Path;

public class FileUtil {
  public static boolean isTxtFile(Path path) {
    return path.getFileName()
            .toString()
            .endsWith(".txt");
  }
}
