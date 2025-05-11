# Detach

Detach is used to create a fragment that is detached from its declaration. The detached fragment then
can be added to another place in the fragment tree.

* The general pattern is to define an instruction class that:
  * implements `DetachHandler`.
  * has an `@AdaptiveDetach` parameter

```kotlin
interface DetachHandler {
    fun detach(origin: AdaptiveFragment, detachIndex: Int)
}

class Replace(
  @AdaptiveDetach val buildFun: (handler: DetachHandler) -> Unit
) : AdaptiveInstruction, DetachHandler {

  // `origin` is the declaring fragment, `someFun` in the call example below
  // `detachIndex` is the declaration index in the declaring fragment

  override fun detach(origin: AdaptiveFragment, detachIndex: Int) {
    // parent has to be retrieved from somewhere
    val detachedFragment = origin.genBuild(parentFragment, detachIndex)
    // do whatever with the detached fragment
  }

}

// the call site looks like this:

@Adative
fun someFun() {
  text("Label", Replace { another() })
}
```

### Processing

The first two steps are important because those guarantee that `transformDetachExpressions` have
all instructions in place already.

- `InnerInstructionLowering` moves all inner instructions into the `instructions` vararg
- `OuterInstructionLowering` moves all outer instructions into the `instructions` vararg
- `IrFunction2ArmClass.transformDetachExpressions` transforms detach instructions
- `transformDetachExpressions` treats the content of the lambda as a normal rendering function call
  - so, the call will get an index and branches in `genBuild` and `genPatchDescendant`.
- the content of the lambda is replaced with a call to the `detach` function of the handler:

```kotlin
Replace { handler.detach(this, detachIndex) }
```

So, to create the detached fragment the lambda function marked with `@AdaptiveDetach` can be called.
That call in turn will call `DetachHandler.detach` with the origin fragment and the declaration index.

**IMPORTANT**

Whatever code gets the origin fragment and `detachIndex`, it has to call `genBuild`, `genPatchDescendant`
and then leave the origin fragment alone. This is necessary, so we won't keep inactive stuff alive.
Probably we should check that there are leftover connections because of parameters.