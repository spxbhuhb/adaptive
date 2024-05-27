# Tree Operations

As of now the only tree operation is replacing the content of a slot.

## Slot and replace

Components:

| Name             | Kind              | Purpose                                                                      |
|------------------|-------------------|------------------------------------------------------------------------------|
| `slot`           | `@Adaptive fun`   | Declare a slot which let's you replace its child fragment.                   |
| `AdaptiveSlot`   | fragment class    | The manual implementation of `slot`.                                         |
| `AdaptiveDetach` | annotation class  | Marks lambdas that detach children.                                          |
| `DetachHandler`  | interface         | Bridge between lambda and `AdaptiveSlot`.                                    |
| `replace`        | helper function   | Makes a `Replace` instance (declared in `ui-common`).                        |
| `Replace`        | instruction class | An instruction that actually performs the replace (declared in `ui-common`). |

```kotlin
interface DetachHandler {
    fun detach(origin: AdaptiveFragment, detachIndex: Int)
}

class Replace(@AdaptiveDetach val slotEntry: (handler: DetachHandler) -> Unit) : DetachHandler {
    override fun detach(origin: AdaptiveFragment, detachIndex: Int) {
        // find the AdaptiveSlot by whatever means
        // call `replace` of the slot with the two parameters
    }
}

fun replace(@AdaptiveDetach slotEntry: (handler: DetachHandler) -> Unit) = Replace(slotEntry)

// the call site looks like this:

text("Label", Replace { another() })
text("Label", replace { another() })
```

When the plugin encounters `@AdaptiveDetach` it treats the content of the lambda as a normal rendering 
function call.

This means that the call will get an index and branches in `genBuild` and `genPatchDescendant`.

Then the content of the lambda is replaced with a call to the `detach` function of the handler:

```kotlin
handler.detach(this, detachIndex)
```