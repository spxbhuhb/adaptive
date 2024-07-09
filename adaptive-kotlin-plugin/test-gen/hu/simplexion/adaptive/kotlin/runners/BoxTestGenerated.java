

package hu.simplexion.adaptive.kotlin.runners;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link hu.simplexion.adaptive.kotlin.GenerateTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("testData/box")
@TestDataPath("$PROJECT_ROOT")
public class BoxTestGenerated extends AbstractBoxTest {
  @Test
  public void testAllFilesPresentInBox() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
  }

  @Nested
  @TestMetadata("testData/box/adat")
  @TestDataPath("$PROJECT_ROOT")
  public class Adat {
    @Test
    public void testAllFilesPresentInAdat() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/adat"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("basic.kt")
    public void testBasic() {
      runTest("testData/box/adat/basic.kt");
    }

    @Test
    @TestMetadata("dateTimeDefaults.kt")
    public void testDateTimeDefaults() {
      runTest("testData/box/adat/dateTimeDefaults.kt");
    }

    @Test
    @TestMetadata("defaultsBasic.kt")
    public void testDefaultsBasic() {
      runTest("testData/box/adat/defaultsBasic.kt");
    }

    @Test
    @TestMetadata("withCompanion.kt")
    public void testWithCompanion() {
      runTest("testData/box/adat/withCompanion.kt");
    }

    @Nested
    @TestMetadata("testData/box/adat/exposed")
    @TestDataPath("$PROJECT_ROOT")
    public class Exposed {
      @Test
      public void testAllFilesPresentInExposed() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/adat/exposed"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("basic.kt")
      public void testBasic() {
        runTest("testData/box/adat/exposed/basic.kt");
      }
    }
  }

  @Nested
  @TestMetadata("testData/box/foundation")
  @TestDataPath("$PROJECT_ROOT")
  public class Foundation {
    @Test
    public void testAllFilesPresentInFoundation() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("basic.kt")
    public void testBasic() {
      runTest("testData/box/foundation/basic.kt");
    }

    @Test
    @TestMetadata("emptyComponent.kt")
    public void testEmptyComponent() {
      runTest("testData/box/foundation/emptyComponent.kt");
    }

    @Test
    @TestMetadata("emptyEntry.kt")
    public void testEmptyEntry() {
      runTest("testData/box/foundation/emptyEntry.kt");
    }

    @Test
    @TestMetadata("root.kt")
    public void testRoot() {
      runTest("testData/box/foundation/root.kt");
    }

    @Nested
    @TestMetadata("testData/box/foundation/call")
    @TestDataPath("$PROJECT_ROOT")
    public class Call {
      @Test
      @TestMetadata("accessBinding.kt")
      public void testAccessBinding() {
        runTest("testData/box/foundation/call/accessBinding.kt");
      }

      @Test
      public void testAllFilesPresentInCall() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/call"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("higherOrder.kt")
      public void testHigherOrder() {
        runTest("testData/box/foundation/call/higherOrder.kt");
      }

      @Test
      @TestMetadata("propertyAccessBinding.kt")
      public void testPropertyAccessBinding() {
        runTest("testData/box/foundation/call/propertyAccessBinding.kt");
      }

      @Test
      @TestMetadata("withDefault.kt")
      public void testWithDefault() {
        runTest("testData/box/foundation/call/withDefault.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/foundation/expect")
    @TestDataPath("$PROJECT_ROOT")
    public class Expect {
      @Test
      public void testAllFilesPresentInExpect() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/expect"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("expect.kt")
      public void testExpect() {
        runTest("testData/box/foundation/expect/expect.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/foundation/helpers")
    @TestDataPath("$PROJECT_ROOT")
    public class Helpers {
      @Test
      @TestMetadata("adapter.kt")
      public void testAdapter() {
        runTest("testData/box/foundation/helpers/adapter.kt");
      }

      @Test
      public void testAllFilesPresentInHelpers() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/helpers"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("fragment.kt")
      public void testFragment() {
        runTest("testData/box/foundation/helpers/fragment.kt");
      }

      @Test
      @TestMetadata("thisState.kt")
      public void testThisState() {
        runTest("testData/box/foundation/helpers/thisState.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/foundation/instruction")
    @TestDataPath("$PROJECT_ROOT")
    public class Instruction {
      @Test
      public void testAllFilesPresentInInstruction() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/instruction"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("basic.kt")
      public void testBasic() {
        runTest("testData/box/foundation/instruction/basic.kt");
      }

      @Test
      @TestMetadata("detach.kt")
      public void testDetach() {
        runTest("testData/box/foundation/instruction/detach.kt");
      }

      @Test
      @TestMetadata("detachWithName.kt")
      public void testDetachWithName() {
        runTest("testData/box/foundation/instruction/detachWithName.kt");
      }

      @Test
      @TestMetadata("eventHandler.kt")
      public void testEventHandler() {
        runTest("testData/box/foundation/instruction/eventHandler.kt");
      }

      @Test
      @TestMetadata("innerBasic.kt")
      public void testInnerBasic() {
        runTest("testData/box/foundation/instruction/innerBasic.kt");
      }

      @Test
      @TestMetadata("innerDeep.kt")
      public void testInnerDeep() {
        runTest("testData/box/foundation/instruction/innerDeep.kt");
      }

      @Test
      @TestMetadata("innerVariations.kt")
      public void testInnerVariations() {
        runTest("testData/box/foundation/instruction/innerVariations.kt");
      }

      @Test
      @TestMetadata("outerBasic.kt")
      public void testOuterBasic() {
        runTest("testData/box/foundation/instruction/outerBasic.kt");
      }

      @Test
      @TestMetadata("passEmpty.kt")
      public void testPassEmpty() {
        runTest("testData/box/foundation/instruction/passEmpty.kt");
      }

      @Test
      @TestMetadata("variations.kt")
      public void testVariations() {
        runTest("testData/box/foundation/instruction/variations.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/foundation/lambda")
    @TestDataPath("$PROJECT_ROOT")
    public class Lambda {
      @Test
      public void testAllFilesPresentInLambda() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/lambda"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("fromRoot.kt")
      public void testFromRoot() {
        runTest("testData/box/foundation/lambda/fromRoot.kt");
      }

      @Test
      @TestMetadata("lambdaCallOut.kt")
      public void testLambdaCallOut() {
        runTest("testData/box/foundation/lambda/lambdaCallOut.kt");
      }

      @Test
      @TestMetadata("outreach.kt")
      public void testOutreach() {
        runTest("testData/box/foundation/lambda/outreach.kt");
      }

      @Test
      @TestMetadata("return.kt")
      public void testReturn() {
        runTest("testData/box/foundation/lambda/return.kt");
      }

      @Test
      @TestMetadata("stateUpdate.kt")
      public void testStateUpdate() {
        runTest("testData/box/foundation/lambda/stateUpdate.kt");
      }

      @Test
      @TestMetadata("suspend.kt")
      public void testSuspend() {
        runTest("testData/box/foundation/lambda/suspend.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/foundation/loop")
    @TestDataPath("$PROJECT_ROOT")
    public class Loop {
      @Test
      public void testAllFilesPresentInLoop() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/loop"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("basic.kt")
      public void testBasic() {
        runTest("testData/box/foundation/loop/basic.kt");
      }

      @Test
      @TestMetadata("list.kt")
      public void testList() {
        runTest("testData/box/foundation/loop/list.kt");
      }

      @Test
      @TestMetadata("patch.kt")
      public void testPatch() {
        runTest("testData/box/foundation/loop/patch.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/foundation/producer")
    @TestDataPath("$PROJECT_ROOT")
    public class Producer {
      @Test
      public void testAllFilesPresentInProducer() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/producer"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("poll.kt")
      public void testPoll() {
        runTest("testData/box/foundation/producer/poll.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/foundation/select")
    @TestDataPath("$PROJECT_ROOT")
    public class Select {
      @Test
      public void testAllFilesPresentInSelect() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/select"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("ifElse.kt")
      public void testIfElse() {
        runTest("testData/box/foundation/select/ifElse.kt");
      }

      @Test
      @TestMetadata("ifElsePatch.kt")
      public void testIfElsePatch() {
        runTest("testData/box/foundation/select/ifElsePatch.kt");
      }

      @Test
      @TestMetadata("ifOnlyFalse.kt")
      public void testIfOnlyFalse() {
        runTest("testData/box/foundation/select/ifOnlyFalse.kt");
      }

      @Test
      @TestMetadata("ifOnlyTrue.kt")
      public void testIfOnlyTrue() {
        runTest("testData/box/foundation/select/ifOnlyTrue.kt");
      }

      @Test
      @TestMetadata("whenNoSubjectElse.kt")
      public void testWhenNoSubjectElse() {
        runTest("testData/box/foundation/select/whenNoSubjectElse.kt");
      }

      @Test
      @TestMetadata("whenNoSubjectNoElse.kt")
      public void testWhenNoSubjectNoElse() {
        runTest("testData/box/foundation/select/whenNoSubjectNoElse.kt");
      }

      @Test
      @TestMetadata("whenSubjectCalc.kt")
      public void testWhenSubjectCalc() {
        runTest("testData/box/foundation/select/whenSubjectCalc.kt");
      }

      @Test
      @TestMetadata("whenSubjectConditions.kt")
      public void testWhenSubjectConditions() {
        runTest("testData/box/foundation/select/whenSubjectConditions.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/foundation/sequence")
    @TestDataPath("$PROJECT_ROOT")
    public class Sequence {
      @Test
      public void testAllFilesPresentInSequence() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/sequence"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("inHigherOrder.kt")
      public void testInHigherOrder() {
        runTest("testData/box/foundation/sequence/inHigherOrder.kt");
      }

      @Test
      @TestMetadata("sequence.kt")
      public void testSequence() {
        runTest("testData/box/foundation/sequence/sequence.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/foundation/stateAccess")
    @TestDataPath("$PROJECT_ROOT")
    public class StateAccess {
      @Test
      public void testAllFilesPresentInStateAccess() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/stateAccess"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("lambdaPatchExternal.kt")
      public void testLambdaPatchExternal() {
        runTest("testData/box/foundation/stateAccess/lambdaPatchExternal.kt");
      }

      @Test
      @TestMetadata("lambdaPatchInternal.kt")
      public void testLambdaPatchInternal() {
        runTest("testData/box/foundation/stateAccess/lambdaPatchInternal.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/foundation/variables")
    @TestDataPath("$PROJECT_ROOT")
    public class Variables {
      @Test
      public void testAllFilesPresentInVariables() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/variables"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("basic.kt")
      public void testBasic() {
        runTest("testData/box/foundation/variables/basic.kt");
      }

      @Test
      @TestMetadata("inline.kt")
      public void testInline() {
        runTest("testData/box/foundation/variables/inline.kt");
      }

      @Test
      @TestMetadata("many.kt")
      public void testMany() {
        runTest("testData/box/foundation/variables/many.kt");
      }

      @Test
      @TestMetadata("onlyExternal.kt")
      public void testOnlyExternal() {
        runTest("testData/box/foundation/variables/onlyExternal.kt");
      }

      @Test
      @TestMetadata("onlyInternal.kt")
      public void testOnlyInternal() {
        runTest("testData/box/foundation/variables/onlyInternal.kt");
      }

      @Test
      @TestMetadata("variables.kt")
      public void testVariables() {
        runTest("testData/box/foundation/variables/variables.kt");
      }

      @Test
      @TestMetadata("withFunction.kt")
      public void testWithFunction() {
        runTest("testData/box/foundation/variables/withFunction.kt");
      }
    }
  }

  @Nested
  @TestMetadata("testData/box/reflect")
  @TestDataPath("$PROJECT_ROOT")
  public class Reflect {
    @Test
    public void testAllFilesPresentInReflect() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/reflect"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("callSiteName.kt")
    public void testCallSiteName() {
      runTest("testData/box/reflect/callSiteName.kt");
    }
  }

  @Nested
  @TestMetadata("testData/box/server")
  @TestDataPath("$PROJECT_ROOT")
  public class Server {
    @Test
    public void testAllFilesPresentInServer() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/server"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("basic.kt")
    public void testBasic() {
      runTest("testData/box/server/basic.kt");
    }
  }

  @Nested
  @TestMetadata("testData/box/service")
  @TestDataPath("$PROJECT_ROOT")
  public class Service {
    @Test
    public void testAllFilesPresentInService() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/service"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("basic.kt")
    public void testBasic() {
      runTest("testData/box/service/basic.kt");
    }

    @Nested
    @TestMetadata("testData/box/service/types")
    @TestDataPath("$PROJECT_ROOT")
    public class Types {
      @Test
      public void testAllFilesPresentInTypes() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/service/types"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("int.kt")
      public void testInt() {
        runTest("testData/box/service/types/int.kt");
      }

      @Test
      @TestMetadata("list.kt")
      public void testList() {
        runTest("testData/box/service/types/list.kt");
      }

      @Test
      @TestMetadata("string.kt")
      public void testString() {
        runTest("testData/box/service/types/string.kt");
      }

      @Test
      @TestMetadata("unit.kt")
      public void testUnit() {
        runTest("testData/box/service/types/unit.kt");
      }
    }
  }
}
