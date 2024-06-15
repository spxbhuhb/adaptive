# Instructions

- `ArmStatVariable.isInstrutions` is true when the state variable is an instruction array
- `ArmClassBuilder` checks if there is any state variable with `isInstructions` on true and passes the index of that variable in the constructor

## Inner instructions

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive

@Adaptive
fun someFun() {
    grid {
        rowTemplate(260.dp, 1.fr, 100.dp, 100.dp)
        colTemplate()
        traceLayout
        
        text("Hello World!", white)
    }
}
```

For this to work:

- the called function has to have an `instructions` parameter
- the called function has to have an `@Adaptive` function parameter

Processing:

- `InnerInstructionLowering` called in `IrFunction2ArmClass.transform`
- lowering may happen on calls which
    - have a `vararg instructions : AdaptiveInstruction` parameter
    - have a function parameter annotated with `@Adaptive`
- `InnerInstructionLowering` calls `extractInnerInstructions` on the last function parameter argument
    - `extractInnerInstructions` transforms the function parameter argument
        - when a call is
            - normal function call that returns with
                - an AdaptiveInstruction
                - an array of AdaptiveInstruction
            - getter with type of `AdaptiveInstruction`
            - getter with type of `Array<AdaptiveInstruction>`
        - it is removed from the function body and added to the result list
    - if the argument for the `instructions` parameter is missing and there are extracted instructions
      - add an empty vararg argument
    - add the extracted instructions to the `instructions` argument