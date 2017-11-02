import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class PerfMonAgent {

  /**
   * This method is called before the application’s main-method is called, when this agent is
   * specified to the Java VM.
   **/
  public static void premain(String agentArgs, Instrumentation inst) {
    // Initialize the static variables we use to track information.
    // Set up the class-file transformer. DpThrallClient.java
    ClassFileTransformer trans = new PerfMonXformer();
    inst.addTransformer(trans);
  }
}
