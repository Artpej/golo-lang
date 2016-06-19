/*
 * Copyright (c) 2012-2016 Institut National des Sciences Appliquées de Lyon (INSA-Lyon)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.golo.di;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class InjectionSupport {

  public static CallSite bootstrap(MethodHandles.Lookup caller, String field, MethodType type, String name) {
    MethodHandle provider = InjectionContext.get().getProvider(name);
    if (provider == null) {
      throw new RuntimeException("no provider found for the injection point named '" + name + "'.");
    }
    return new ConstantCallSite(provider.asType(type));
  }

}
