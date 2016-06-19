/*
 * Copyright (c) 2012-2016 Institut National des Sciences Appliquées de Lyon (INSA-Lyon)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.golo.di;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.golo.di.annotations.Provides;

public class InjectionContext {

  private final Map<String, MethodHandle> providers = new ConcurrentHashMap<>();

  private final MethodHandles.Lookup lookup = MethodHandles.lookup();

  private static final InjectionContext instance = new InjectionContext();

  private InjectionContext() {
  }

  public static InjectionContext get() {
    return instance;
  }

  public void register(Object... configs) {

    for (Object config : configs) {
      for (Method method : config.getClass().getMethods()) {
        if (method.isAnnotationPresent(Provides.class)) {
          checkProvider(method);
          try {            
            String name = method.getAnnotation(Provides.class).value();

            MethodHandle provider = lookup.unreflect(method).bindTo(config);
            SingletonProvider resolver = new SingletonProvider(provider);

            MethodHandle target = lookup.findVirtual(Provider.class, "get", MethodType.methodType(Object.class));
            target = target.bindTo(resolver);
            target = MethodHandles.dropArguments(target, 0, Object.class);

            providers.put(name, target);
          } catch (NoSuchMethodException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
          }
        }
      }
    }
  }
  
  private static void checkProvider(Method method){
    if(method.getParameterCount() > 0) {
      throw new RuntimeException("The provider method must be a no-args method.");
    }
  }

  MethodHandle getProvider(String name) {
    return providers.get(name);
  }

}
