# Bindings

Bindings are a bit messy at the moment, should clean them up.

Used in two cases:

- producers
- accessors

Points of interest:

`AdaptiveStateVariableBinding.equals`

- decides which binding is replaced

`AdaptiveFragment.setBinding`

- dirty mask magic
- not particularly nice, but `setStateVariable` skips update on equals

`ArmClassBuilder.genPatchInternalBody`

- dirtyMask is mutable so producers can modify it

`ArmInternalStateVariableBuilder.transformProducer`

- updates the dirty mask temporary to keep it up-to-date

Now `genPatchInternal` generates a new binding on each dependency change. Maybe this is OK,
technically a dirty mask change would be enough.