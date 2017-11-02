import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class PerfMonXformer implements ClassFileTransformer {

  public byte[] transform(ClassLoader loader, String className,
                          Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws IllegalClassFormatException {
    byte[] transformed = null;
    ClassPool pool = ClassPool.getDefault();
    CtClass cl = null;
    try {
      cl = pool.makeClass(new ByteArrayInputStream(
          classfileBuffer));
      if (cl.getPackageName().contains("datapipeline")) {
        if (!cl.isInterface()) {
          CtBehavior[] methods = cl.getDeclaredBehaviors();
          for (CtBehavior method : methods) {
            if (!method.isEmpty()) {
              doMethod(method);
            }
          }
        }
      }
      transformed = cl.toBytecode();
    } catch (Exception e) {
//      System.err.println("Could not instrument  " + className
//          + ",  exception : " + e.getMessage());
      e.printStackTrace();
    } finally {
      if (cl != null) {
        cl.detach();
      }
    }
    return transformed;
  }

  private void doMethod(CtBehavior method) throws CannotCompileException {
    // method.insertBefore("long stime = System.nanoTime();");
    // method.insertAfter("System.out.println(/"leave "+method.getName()+" and time:/"+(System.nanoTime()-stime));");
    method.instrument(new ExprEditor() {
      @Override
      public void edit(MethodCall m) throws CannotCompileException {
        m.replace("{long stime = System.currentTimeMillis(); " +
            "$_ = $proceed($$);    " +
            "System.out.println(\"" + m.getClassName() + "." + m.getMethodName() + ":\t\"+ (System.currentTimeMillis()-stime) +\" ms\" ); }");
      }
    });
  }
}

