/*
 * Copyright (c) 2012-2016 Institut National des Sciences Appliquées de Lyon (INSA-Lyon)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.golo.di;

import org.eclipse.golo.di.annotations.Inject;
import org.eclipse.golo.di.annotations.Provides;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.testng.annotations.Test;

public class InjectionTest {

  @Inject("singleton")
  private String singleton;

  public static class Config {

    int i;

    @Provides("singleton")
    public String singleton() {
      i++;
      return "singleton! " + i;
    }

  }

  @Test
  public void test() {
    Config config = new Config();
    InjectionContext.get().register(config);
    assertThat(this.singleton, is("singleton! 1"));
    assertThat(this.singleton, is("singleton! 1"));
    InjectionContext.get().register(config);
    assertThat(this.singleton, is("singleton! 2"));

  }

}
