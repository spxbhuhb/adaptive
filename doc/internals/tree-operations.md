# Tree Operations

As of now the only tree operation is replacing the content of a slot.

## Slot and replace

Components:

| Name             | Kind              | Purpose                                                                      |
|------------------|-------------------|------------------------------------------------------------------------------|
| `slot`           | `@Adaptive fun`   | Declare a slot which let's you replace its child fragment.                   |
| `FoundationSlot` | fragment class    | The manual implementation of `slot`.                                         |
| `AdaptiveDetach` | annotation class  | Marks lambdas that detach children.                                          |
| `DetachHandler`  | interface         | Bridge between lambda and `FoundationSlot`.                                  |
| `replace`        | helper function   | Makes a `Replace` instance (declared in `ui-common`).                        |
| `Replace`        | instruction class | An instruction that actually performs the replace (declared in `ui-common`). |

```kotlin
interface DetachHandler {
    fun detach(origin: AdaptiveFragment, detachIndex: Int)
}

class Replace(@AdaptiveDetach val slotEntry: (handler: DetachHandler) -> Unit) : DetachHandler {
    override fun detach(origin: AdaptiveFragment, detachIndex: Int) {
        // find the FoundationSlot by whatever means
        // call `replace` of the slot with the two parameters
    }
}

fun replace(@AdaptiveDetach slotEntry: (handler: DetachHandler) -> Unit) = Replace(slotEntry)

// the call site looks like this:

text("Label", Replace { another() })
text("Label", replace { another() })
```

### Processing

- `InnerInstructionLowering` moves all inner instructions into the `instructions` vararg
- `IrFunction2ArmClass.transformDetachExpressions` transforms detach instructions
- `transformDetachExpressions` treats the content of the lambda as a normal rendering function call
- the call will get an index and branches in `genBuild` and `genPatchDescendant`.
- the content of the lambda is replaced with a call to the `detach` function of the handler:

```kotlin
handler.detach(this, detachIndex)
```

**IMPORTANT**

Whatever code gets the origin fragment and `detachIndex`, it has to call `genBuild` and then leave the
origin fragment alone. This is necessary, so we won't keep inactive stuff alive. Probably we should
check that there are leftover connections because of parameters.