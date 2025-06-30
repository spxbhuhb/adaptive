# Foundation

Processing chain:

```text
FoundationGenerationExtension
    + OriginalFunctionTransform
        + IrFunction2ArmClass
            + BoundaryVisitor
            + StateDefinitionTransform
            + InnerInstructionLowering
            + transformBlock 
    + ArmClassBuilder
```

## Patch Descendant

Generated `genPatchDescendant` functions always result in a call to `setStateVariable` of the descendant.

Mostly through `irSetDescendantStateVariable`, with the notable exception of state variable bindings which
call `setBinding`.

```text
ArmClassBuilder.genPatchDescendantBranch

    BranchBuilder.genPatchDescendantBranch
    
        ArmCallBuilder.genPatchDescendantBranch
            ArmValueArgument.toPatchExpression
            
                ArmValueArgumentBuilder.genPatchDescendantExpression
                    ArmValueArgumentBuilder.patchVariableValue
                        irSetDescendantStateVariable
                        
                ArmDefaultValueArgumentBuilder
                    skips patch intruction generation
                            
                ArmFragmentFactoryArgumentBuilder
                    ArmFragmentFactoryArgumentBuilder.patchVariableValue
                        irSetDescendantStateVariable
                
                ArmStateVariableBindingArgumentBuilder.genPatchDescendantExpression
                    ArmStateVariableBindingArgumentBuilder.patchVariableValue
                        AdaptiveFragment.setBinding
                            AdaptiveFragment.setStateVariable
                        
        ArmLoopBuilder.genPatchDescendantBranch
            irSetDescendantStateVariable
            
        ArmSelectBuilder.genPatchDescendantBranch
            irSetDescendantStateVariable
            
        ArmSequenceBuilder.genPatchDescendantBranch
            irSetDescendantStateVariable
```