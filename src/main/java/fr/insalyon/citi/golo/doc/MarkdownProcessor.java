/*
 * Copyright (c) 2012-2015 Institut National des Sciences Appliquées de Lyon (INSA-Lyon)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package fr.insalyon.citi.golo.doc;

import fr.insalyon.citi.golo.compiler.parser.ASTCompilationUnit;
import gololang.FunctionReference;
import gololang.Predefined;

import java.lang.invoke.MethodHandle;
import java.nio.file.Path;
import java.util.TreeMap;
import java.util.Map;

public class MarkdownProcessor extends AbstractProcessor {

  @Override
  public String render(ASTCompilationUnit compilationUnit) throws Throwable {
    FunctionReference template = template("template", "markdown");
    ModuleDocumentation documentation = new ModuleDocumentation(compilationUnit);
    return (String) template.handle().invokeWithArguments(documentation);
  }

  @Override
  public void process(Map<String, ASTCompilationUnit> units, Path targetFolder) throws Throwable {
    TreeMap<String, String> moduleDocFile = new TreeMap<>();
    ensureFolderExists(targetFolder);
    for (ASTCompilationUnit unit : units.values()) {
      String moduleName = moduleName(unit);
      Path docFile = outputFile(targetFolder, moduleName, ".markdown");
      ensureFolderExists(docFile.getParent());
      Predefined.textToFile(render(unit), docFile);
      moduleDocFile.put(moduleName, targetFolder.relativize(docFile).toString());
    }
    FunctionReference indexTemplate = template("index", "markdown");
    String index = (String) indexTemplate.handle().invokeWithArguments(moduleDocFile);
    Predefined.textToFile(index, targetFolder.resolve("index.markdown"));
  }
}
