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