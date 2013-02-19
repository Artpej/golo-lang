package dispatch;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.Clock;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;
import fr.citilab.gololang.benchmarks.GoloBenchmark;
import org.jruby.embed.EmbedEvalUnit;
import org.jruby.embed.ScriptingContainer;
import org.junit.Test;

@BenchmarkOptions(clock = Clock.NANO_TIME, warmupRounds = 15, benchmarkRounds = 5)
@BenchmarkMethodChart(filePrefix = "trimorphic-dispatch")
@BenchmarkHistoryChart(filePrefix = "trimorphic-dispatch-history", labelWith = LabelType.TIMESTAMP)
public class TriMorphicTest extends GoloBenchmark {

  private static final Class<?> GoloModule = loadGoloModule("TriMorphic.golo");
  private static final Class<?> GroovyClass = loadGroovyClass("TriMorphic.groovy");
  private static final ScriptingContainer JRubyContainer;
  private static final EmbedEvalUnit JRubyScript;

  static {
    JRubyContainer = new ScriptingContainer();
    JRubyScript = jrubyEvalUnit(JRubyContainer, "trimorphic.rb");
  }

  @Test
  public void java() {
    TriMorphic.run();
  }

  @Test
  public void golo() throws Throwable {
    GoloModule.getMethod("run").invoke(null);
  }

  @Test
  public void groovy() throws Throwable {
    GroovyClass.getMethod("run").invoke(null);
  }

  @Test
  public void jruby() throws Throwable {
    JRubyScript.run();
  }
}