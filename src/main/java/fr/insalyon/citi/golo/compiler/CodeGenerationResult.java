/*
 * Copyright (c) 2012-2015 Institut National des Sciences Appliquées de Lyon (INSA-Lyon)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package fr.insalyon.citi.golo.compiler;

/**
 * A code generation result.
 * <p>
 * Compiling a single Golo source file may result in several JVM classes to be produced.
 * A <code>CodeGenerationResult</code> represents one such output.
 */
public final class CodeGenerationResult {

  private final byte[] bytecode;
  private final PackageAndClass packageAndClass;

  /**
   * Constructor for a code generation result.
   *
   * @param bytecode        the JVM bytecode as an array.
   * @param packageAndClass the package and class descriptor for the bytecode.
   */
  public CodeGenerationResult(byte[] bytecode, PackageAndClass packageAndClass) {
    this.bytecode = bytecode;
    this.packageAndClass = packageAndClass;
  }

  /**
   * @return the bytecode array.
   */
  public byte[] getBytecode() {
    return bytecode;
  }

  /**
   * @return the package and class descriptor.
   */
  public PackageAndClass getPackageAndClass() {
    return packageAndClass;
  }
}
