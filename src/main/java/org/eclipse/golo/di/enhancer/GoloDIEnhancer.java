/*
 * Copyright (c) 2012-2016 Institut National des Sciences Appliquées de Lyon (INSA-Lyon)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.golo.di.enhancer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class GoloDIEnhancer {

  public static void main(String[] args) throws IOException {
    Path classesDir = Paths.get(args[0]);
    Files.walkFileTree(classesDir, new ClassFileVisitor());
  }

  private static class ClassFileVisitor extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {

      try (InputStream inputstream = Files.newInputStream(path);) {
        ClassReader reader = new ClassReader(inputstream);
        ClassWriter writer = new ClassWriter(reader, 0/*ClassWriter.COMPUTE_MAXS*/);
        ClassVisitor trasnformer = new InjectionAwareClassVisitor(writer);
        reader.accept(trasnformer, 0);
        OutputStream file = Files.newOutputStream(path);
        inputstream.close();
        file.write(writer.toByteArray());
        file.flush();
        file.close();
      }
      return super.visitFile(path, attrs);
    }

  }

}
