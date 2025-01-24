

package fun.adaptive.kotlin.runners;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link fun.adaptive.kotlin.GenerateTestsKt}. DO NOT MODIFY MANUALLY */
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
    @TestMetadata("emptyConstructor.kt")
    public void testEmptyConstructor() {
      runTest("testData/box/adat/emptyConstructor.kt");
    }

    @Test
    @TestMetadata("extendConstructors.kt")
    public void testExtendConstructors() {
      runTest("testData/box/adat/extendConstructors.kt");
    }

    @Test
    @TestMetadata("newInstance.kt")
    public void testNewInstance() {
      runTest("testData/box/adat/newInstance.kt");
    }

    @Nested
    @TestMetadata("testData/box/adat/companion")
    @TestDataPath("$PROJECT_ROOT")
    public class Companion {
      @Test
      @TestMetadata("adatCompanionOf.kt")
      public void testAdatCompanionOf() {
        runTest("testData/box/adat/companion/adatCompanionOf.kt");
      }

      @Test
      @TestMetadata("adatCompanionResolve.kt")
      public void testAdatCompanionResolve() {
        runTest("testData/box/adat/companion/adatCompanionResolve.kt");
      }

      @Test
      @TestMetadata("adatCompanionResolveAbstract.kt")
      public void testAdatCompanionResolveAbstract() {
        runTest("testData/box/adat/companion/adatCompanionResolveAbstract.kt");
      }

      @Test
      public void testAllFilesPresentInCompanion() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/adat/companion"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("withCompanion.kt")
      public void testWithCompanion() {
        runTest("testData/box/adat/companion/withCompanion.kt");
      }

      @Test
      @TestMetadata("withTypedCompanion.kt")
      public void testWithTypedCompanion() {
        runTest("testData/box/adat/companion/withTypedCompanion.kt");
      }

      @Test
      @TestMetadata("withoutCompanion.kt")
      public void testWithoutCompanion() {
        runTest("testData/box/adat/companion/withoutCompanion.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/adat/default")
    @TestDataPath("$PROJECT_ROOT")
    public class Default {
      @Test
      public void testAllFilesPresentInDefault() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/adat/default"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("defaultArray.kt")
      public void testDefaultArray() {
        runTest("testData/box/adat/default/defaultArray.kt");
      }

      @Test
      @TestMetadata("defaultArrayFail.kt")
      public void testDefaultArrayFail() {
        runTest("testData/box/adat/default/defaultArrayFail.kt");
      }

      @Test
      @TestMetadata("defaultsBasic.kt")
      public void testDefaultsBasic() {
        runTest("testData/box/adat/default/defaultsBasic.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/adat/descriptor")
    @TestDataPath("$PROJECT_ROOT")
    public class Descriptor {
      @Test
      public void testAllFilesPresentInDescriptor() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/adat/descriptor"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("basic.kt")
      public void testBasic() {
        runTest("testData/box/adat/descriptor/basic.kt");
      }

      @Test
      @TestMetadata("stringEscape.kt")
      public void testStringEscape() {
        runTest("testData/box/adat/descriptor/stringEscape.kt");
      }
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

    @Nested
    @TestMetadata("testData/box/adat/functions")
    @TestDataPath("$PROJECT_ROOT")
    public class Functions {
      @Test
      public void testAllFilesPresentInFunctions() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/adat/functions"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("equals.kt")
      public void testEquals() {
        runTest("testData/box/adat/functions/equals.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/adat/immutable")
    @TestDataPath("$PROJECT_ROOT")
    public class Immutable {
      @Test
      public void testAllFilesPresentInImmutable() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/adat/immutable"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("basic.kt")
      public void testBasic() {
        runTest("testData/box/adat/immutable/basic.kt");
      }

      @Test
      @TestMetadata("immutableVar.kt")
      public void testImmutableVar() {
        runTest("testData/box/adat/immutable/immutableVar.kt");
      }

      @Test
      @TestMetadata("noBackingField.kt")
      public void testNoBackingField() {
        runTest("testData/box/adat/immutable/noBackingField.kt");
      }

      @Test
      @TestMetadata("openVal.kt")
      public void testOpenVal() {
        runTest("testData/box/adat/immutable/openVal.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/adat/polymorphic")
    @TestDataPath("$PROJECT_ROOT")
    public class Polymorphic {
      @Test
      public void testAllFilesPresentInPolymorphic() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/adat/polymorphic"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("anyProperty.kt")
      public void testAnyProperty() {
        runTest("testData/box/adat/polymorphic/anyProperty.kt");
      }

      @Test
      @TestMetadata("basic.kt")
      public void testBasic() {
        runTest("testData/box/adat/polymorphic/basic.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/adat/signature")
    @TestDataPath("$PROJECT_ROOT")
    public class Signature {
      @Test
      public void testAllFilesPresentInSignature() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/adat/signature"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("enum.kt")
      public void testEnum() {
        runTest("testData/box/adat/signature/enum.kt");
      }
    }
  }

  @Nested
  @TestMetadata("testData/box/backend")
  @TestDataPath("$PROJECT_ROOT")
  public class Backend {
    @Test
    public void testAllFilesPresentInBackend() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/backend"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("basic.kt")
    public void testBasic() {
      runTest("testData/box/backend/basic.kt");
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
    @TestMetadata("testData/box/foundation/dependency")
    @TestDataPath("$PROJECT_ROOT")
    public class Dependency {
      @Test
      public void testAllFilesPresentInDependency() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/dependency"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("independent.kt")
      public void testIndependent() {
        runTest("testData/box/foundation/dependency/independent.kt");
      }

      @Test
      @TestMetadata("intervariable.kt")
      public void testIntervariable() {
        runTest("testData/box/foundation/dependency/intervariable.kt");
      }

      @Test
      @TestMetadata("producer.kt")
      public void testProducer() {
        runTest("testData/box/foundation/dependency/producer.kt");
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

      @Test
      @TestMetadata("haveToPatch.kt")
      public void testHaveToPatch() {
        runTest("testData/box/foundation/expect/haveToPatch.kt");
      }

      @Test
      @TestMetadata("stateSizeEmpty.kt")
      public void testStateSizeEmpty() {
        runTest("testData/box/foundation/expect/stateSizeEmpty.kt");
      }

      @Test
      @TestMetadata("stateSizeMany.kt")
      public void testStateSizeMany() {
        runTest("testData/box/foundation/expect/stateSizeMany.kt");
      }

      @Test
      @TestMetadata("stateVariableVal.kt")
      public void testStateVariableVal() {
        runTest("testData/box/foundation/expect/stateVariableVal.kt");
      }

      @Test
      @TestMetadata("stateVariableVar.kt")
      public void testStateVariableVar() {
        runTest("testData/box/foundation/expect/stateVariableVar.kt");
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
      @TestMetadata("dependency.kt")
      public void testDependency() {
        runTest("testData/box/foundation/instruction/dependency.kt");
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
      @TestMetadata("innerRangeTo.kt")
      public void testInnerRangeTo() {
        runTest("testData/box/foundation/instruction/innerRangeTo.kt");
      }

      @Test
      @TestMetadata("innerRangeToParam.kt")
      public void testInnerRangeToParam() {
        runTest("testData/box/foundation/instruction/innerRangeToParam.kt");
      }

      @Test
      @TestMetadata("innerVariations.kt")
      public void testInnerVariations() {
        runTest("testData/box/foundation/instruction/innerVariations.kt");
      }

      @Test
      @TestMetadata("innerWhen.kt")
      public void testInnerWhen() {
        runTest("testData/box/foundation/instruction/innerWhen.kt");
      }

      @Test
      @TestMetadata("outerAccessor.kt")
      public void testOuterAccessor() {
        runTest("testData/box/foundation/instruction/outerAccessor.kt");
      }

      @Test
      @TestMetadata("outerBasic.kt")
      public void testOuterBasic() {
        runTest("testData/box/foundation/instruction/outerBasic.kt");
      }

      @Test
      @TestMetadata("outerChain.kt")
      public void testOuterChain() {
        runTest("testData/box/foundation/instruction/outerChain.kt");
      }

      @Test
      @TestMetadata("outerDeep.kt")
      public void testOuterDeep() {
        runTest("testData/box/foundation/instruction/outerDeep.kt");
      }

      @Test
      @TestMetadata("outerWhen.kt")
      public void testOuterWhen() {
        runTest("testData/box/foundation/instruction/outerWhen.kt");
      }

      @Test
      @TestMetadata("passEmpty.kt")
      public void testPassEmpty() {
        runTest("testData/box/foundation/instruction/passEmpty.kt");
      }

      @Test
      @TestMetadata("passthrough.kt")
      public void testPassthrough() {
        runTest("testData/box/foundation/instruction/passthrough.kt");
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
      @TestMetadata("lambdaPatch.kt")
      public void testLambdaPatch() {
        runTest("testData/box/foundation/lambda/lambdaPatch.kt");
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
      @TestMetadata("copyStore.kt")
      public void testCopyStore() {
        runTest("testData/box/foundation/producer/copyStore.kt");
      }

      @Test
      @TestMetadata("poll.kt")
      public void testPoll() {
        runTest("testData/box/foundation/producer/poll.kt");
      }

      @Test
      @TestMetadata("postfix.kt")
      public void testPostfix() {
        runTest("testData/box/foundation/producer/postfix.kt");
      }

      @Test
      @TestMetadata("postfixNullable1.kt")
      public void testPostfixNullable1() {
        runTest("testData/box/foundation/producer/postfixNullable1.kt");
      }

      @Test
      @TestMetadata("postfixNullable2.kt")
      public void testPostfixNullable2() {
        runTest("testData/box/foundation/producer/postfixNullable2.kt");
      }

      @Test
      @TestMetadata("postfixNullableWithAdatCompanion.kt")
      public void testPostfixNullableWithAdatCompanion() {
        runTest("testData/box/foundation/producer/postfixNullableWithAdatCompanion.kt");
      }
    }

    @Nested
    @TestMetadata("testData/box/foundation/query")
    @TestDataPath("$PROJECT_ROOT")
    public class Query {
      @Test
      public void testAllFilesPresentInQuery() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/foundation/query"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("first.kt")
      public void testFirst() {
        runTest("testData/box/foundation/query/first.kt");
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

      @Test
      @TestMetadata("whenSubjectEnum.kt")
      public void testWhenSubjectEnum() {
        runTest("testData/box/foundation/select/whenSubjectEnum.kt");
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
      @TestMetadata("closure.kt")
      public void testClosure() {
        runTest("testData/box/foundation/variables/closure.kt");
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
      @TestMetadata("noInitializer.kt")
      public void testNoInitializer() {
        runTest("testData/box/foundation/variables/noInitializer.kt");
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
  @TestMetadata("testData/box/grove")
  @TestDataPath("$PROJECT_ROOT")
  public class Grove {
    @Test
    public void testAllFilesPresentInGrove() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/grove"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Nested
    @TestMetadata("testData/box/grove/hydration")
    @TestDataPath("$PROJECT_ROOT")
    public class Hydration {
      @Test
      public void testAllFilesPresentInHydration() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/grove/hydration"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("basic.kt")
      public void testBasic() {
        runTest("testData/box/grove/hydration/basic.kt");
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

    @Test
    @TestMetadata("typeSignature.kt")
    public void testTypeSignature() {
      runTest("testData/box/reflect/typeSignature.kt");
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
    @TestMetadata("testData/box/service/polymorphic")
    @TestDataPath("$PROJECT_ROOT")
    public class Polymorphic {
      @Test
      @TestMetadata("abstract.kt")
      public void testAbstract() {
        runTest("testData/box/service/polymorphic/abstract.kt");
      }

      @Test
      public void testAllFilesPresentInPolymorphic() {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/service/polymorphic"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
      }

      @Test
      @TestMetadata("basic.kt")
      public void testBasic() {
        runTest("testData/box/service/polymorphic/basic.kt");
      }

      @Test
      @TestMetadata("generic.kt")
      public void testGeneric() {
        runTest("testData/box/service/polymorphic/generic.kt");
      }
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
