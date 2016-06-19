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

public abstract class Provider<T> {

  private final MethodHandle getter;

  public Provider(MethodHandle getter) {
    this.getter = getter;
  }

  protected MethodHandle getter() {
    return getter;
  }

  public abstract T get();
}
