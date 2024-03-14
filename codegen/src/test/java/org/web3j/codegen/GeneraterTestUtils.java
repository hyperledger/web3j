package org.web3j.codegen;

import javax.tools.*;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeneraterTestUtils {

  public static void verifyGeneratedCode(String sourceFile) throws IOException {
      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

      try (StandardJavaFileManager fileManager =
              compiler.getStandardFileManager(diagnostics, null, null)) {
          Iterable<? extends JavaFileObject> compilationUnits =
                  fileManager.getJavaFileObjectsFromStrings(
                          Collections.singletonList(sourceFile));
          JavaCompiler.CompilationTask task =
                  compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
          assertTrue(task.call(), "Generated contract contains compile time error");
      }
  }
}
