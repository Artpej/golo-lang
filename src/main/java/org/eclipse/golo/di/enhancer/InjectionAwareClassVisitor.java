/*
 * Copyright (c) 2012-2016 Institut National des Sciences Appliquées de Lyon (INSA-Lyon)
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.golo.di.enhancer;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.golo.di.annotations.Inject;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;
import org.objectweb.asm.Type;

public class InjectionAwareClassVisitor extends ClassVisitor {

  private static final String INJECT_ANNOTAION_DESC = "L" + Type.getInternalName(Inject.class) + ";";

  private static final Handle BSM = new Handle(
      Opcodes.H_INVOKESTATIC,
      "org/eclipse/golo/di/InjectionSupport",
      "bootstrap",
      "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;)Ljava/lang/invoke/CallSite;",
      false);

  private final Map<String, String> injectionPoints = new HashMap<>();

  public InjectionAwareClassVisitor(ClassVisitor cv) {
    super(Opcodes.ASM5, cv);
  }

  @Override
  public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
    return new InjectionAwareFieldVisitor(super.visitField(access, name, desc, signature, value), name);
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
    return new InjectionAwareMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions));
  }

  private class InjectionAwareFieldVisitor extends FieldVisitor {

    private final String field;

    public InjectionAwareFieldVisitor(FieldVisitor fv, String field) {
      super(Opcodes.ASM5, fv);
      this.field = field;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
      if (INJECT_ANNOTAION_DESC.equals(desc)) {
        return new InjectionAwareAnnotationVisitor(super.visitAnnotation(desc, visible), field);
      }
      return super.visitAnnotation(desc, visible);
    }

  }

  private class InjectionAwareAnnotationVisitor extends AnnotationVisitor {

    private final String field;

    public InjectionAwareAnnotationVisitor(AnnotationVisitor av, String field) {
      super(Opcodes.ASM5, av);
      this.field = field;
    }

    @Override
    public void visit(String name, Object value) {
      injectionPoints.put(field, (String) value);
      super.visit(name, value);
    }

  }

  private class InjectionAwareMethodVisitor extends MethodVisitor {

    public InjectionAwareMethodVisitor(MethodVisitor mv) {
      super(Opcodes.ASM5, mv);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
      if (opcode == Opcodes.GETFIELD && injectionPoints.containsKey(name)) {
        super.visitInvokeDynamicInsn(name, "(L" + owner + ";)" + desc, BSM, injectionPoints.get(name));
      } else {
        super.visitFieldInsn(opcode, owner, name, desc);
      }
    }
  }

}
