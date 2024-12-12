# UI update

There are two kind of UI updates:

* layout independent updates
* layout dependent updates

## Layout independent updates

Layout independent updates are applied immediately, by `genPatchInternal` of the
UI fragment.

These do not use the results of the layout calculations but include padding, margin 
and border which have an effect on the calculations.

## Layout dependent updates

Layout dependent updates have to be optimized as layout is a CPU heavy process.

## Start

UI update may be needed in these cases:

- intrinsic content of a fragment changes in size (text change for example)
  - fragment dependent, in `auiPatchInternal`, checked by `AbstractAuiFragment.genPatchInternal`
- layout related instructions change
  - `AbstractAuiFragment.patchInstructions`, checked by `AbstractAuiFragment.genPatchInternal`
- new layout item is added to a container fragment
  - `AbstractContainer.addActual`
  - `AuiAdapter.addActualRoot`
- a layout item is removed from a container fragment
  - `AbstractContainer.removeActual`
  - root fragment removal does not trigger re-calculation
- the root container is resized
  - adapter dependent
    - `AuiAdapter.resizeObserver` (jsMain)

These changes are results of fragment patching, except root container resize.

Root container resize is a special case which results in full re-layout. Some
fragments may optimize this re-calculation, but generally speaking it is a full re-layout.

Patching is initiated by:

- initialization of the root fragment
  - the generated entry function calls `create` and `mount` of the root fragment
    - create builds the initial fragment tree
    - mounts call `AuiAdapter.addActual` or `AuiAdapter.addActualRoot`
- event handlers
  - started by `UIEvent.patchIfDirty`
  - `patchIfDirty` calls `patchInternalBatch` if the declaring fragment of the event handler is dirty
- producers 
  - started by `AdaptiveProducer.setDirtyBatch`
  - `setDirtyBatch` calls `targetFragment.setDirtyBatch` which in turn calls `patchInternalBatch`

The actual UI update starts when patching has been done and the fragment that initiated
the patching calls its own `closePatchBatch` function.

## Update Batch

All layout relayed updates happen in an update batch.

An update batch:

* executed by `AbstractAuiFragment.closePatchBatch`
* has a unique batch identifier, stored in `AbstractAuiAdapter.updateBatchId`
* calls `computeLayout`, `placeLayout` of items in `AbstractAuiAdapter.updateBatch`
* clears `AbstractAuiAdapter.updateBatch`
* increments the `updateBatchId` so the next batch will have its own unique id

## Fit Content, Fit Container

There are two main strategies for sizing a fragment:

* setting its size to the size of its content - `fit.content`
* setting its size to the size its container proposes - `fit.container`

Update optimization decisions have to know which strategy is used by the fragment.

Fragments of the main root element generally use `fit.container`.

Notable cases when `fit.content` is used:

- dialog box
- snackbar
- pop-up

For buttons and textual information the choice between `fit.content` or `fit.container` depends
on the context.

## Update decisions

There are two decisions a fragment has to make:

1. Which fragment to add to the batch, self or container?
2. May the fragment skip the calculation?

### Add to the batch

fragment state change (patch) -> update needed -> self or container?

| Change           | Strategy      | Fragment to add to batch |
|------------------|---------------|--------------------------|
| inner dimensions | fit-container | self                     |
| inner dimensions | fit-content   | container                |
| decoration       | *             | self                     |
| events           | *             | self                     |
| input            | *             | self                     |
| text             | *             | self                     |
| container        | fit-container | self                     |
| container        | fit-content   | container                |
| grid             | *             | container                |
| layout           | fit-container | self                     |
| layout           | fit-content   | container                |

When added to the batch:

* the fragment is added to `AbstractAuiAdapter.updateBatch`
* `AbstractAuiFragment.updateBatchId` is set to `AbstractAuiAdapter.updateBatchId`

### Skip the calculation

The fragment may skip the calculation when:

* `fit.content`
* `AbstractAuiFragment.updateBatchId` != `AbstractAuiAdapter.updateBatchId`