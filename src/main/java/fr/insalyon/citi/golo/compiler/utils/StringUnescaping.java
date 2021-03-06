/*
 * Copyright (c) 2012-2015 Institut National des Sciences Appliquées de Lyon (INSA-Lyon)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package fr.insalyon.citi.golo.compiler.utils;

public class StringUnescaping {

  private static final String[] ESCAPE_STRINGS = {
      String.valueOf('\n'),
      String.valueOf('\t'),
      String.valueOf('\b'),
      String.valueOf('\r'),
      String.valueOf('\f'),
      String.valueOf('\''),
      String.valueOf('"'),
      String.valueOf('\\')
  };

  private static final String[] SEQS = {
      "\\n",
      "\\t",
      "\\b",
      "\\r",
      "\\f",
      "\\'",
      "\\\"",
      "\\\\"
  };

  public static String unescape(String str) {
    String result = str;
    for (int i = 0; i < ESCAPE_STRINGS.length; i++) {
      result = result.replace(SEQS[i], ESCAPE_STRINGS[i]);
    }
    return result;
    // TODO: this is a rather inefficient algorithm...
  }
}
