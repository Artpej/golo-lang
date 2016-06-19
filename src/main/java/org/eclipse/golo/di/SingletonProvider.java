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
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class SingletonProvider<T> extends Provider<T> {

  private final ReentrantLock lock = new ReentrantLock();

  private T instance;

  public SingletonProvider(MethodHandle getter) {
    super(getter);
  }

  @Override
  public T get() {
    lock.lock();
    try {
      if (instance == null) {
        instance = (T) getter().invoke();
      }
    } catch (Throwable ex) {
      ex.printStackTrace();
    } finally {
      lock.unlock();
    }
    return instance;
  }

}
