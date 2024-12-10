# Layout Update

## Start

Layout update may be needed in these cases:

- intrinsic content of a fragment changes in size (text change for example)
- layout related instructions change
- new layout item is added to a container fragment
- a layout item is removed from a container fragment
- the root container is resized

These changes are results of fragment patching, except root container resize.

Root container resize is a special case which results in full re-layout. Some
fragments may optimize this re-calculation, but generally speaking it is a full re-layout.

Patching is initiated by:

- event handlers
  - started by `UIEvent.patchIfDirty`
  - `patchIfDirty` calls `patchInternalBatch` if the declaring fragment of the event handler is dirty
- producers 
  - started by `AdaptiveProducer.setDirtyBatch`
  - `setDirtyBatch` calls `targetFragment.setDirtyBatch` which in turn calls `patchInternalBatch`

The actual layout update starts when patching has been done and the fragment that initiated
the patching calls its own `closePatchBatch` function.

## Layout Update Batch

All layout updates happen in a layout update batch.

A layout update batch:

* executed by `AbstractAuiFragment.closePatchBatch`
* has a unique batch identifier, stored in `AbstractAuiAdapter.layoutBatchId`
* calls `computeLayout` and `placeLayout` of items in `AbstractAuiAdapter.layoutBatch`
* clears `AbstractAuiAdapter.layoutBatch`
* increments the `layoutBatchId` so the next batch will have its own unique id

## Fit Content, Fit Container

There are two main strategies for sizing a fragment:

* setting its size to the size of its content - `fit.content`
* setting its size to the size its container proposes - `fit.container`

Layout update optimization decisions have to know which strategy is used by the fragment.

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

fragment state change (patch) -> re-layout needed -> self or container?

* fit-container -> self
* fit-content -> container

When added to the batch:

* the fragment is added to `AbstractAuiAdapter.layoutBatch`
* `AbstractAuiFragment.layoutBatchId` is set to `AbstractAuiAdapter.layoutBatchId`

### Skip the calculation

The fragment may skip the calculation when:

* `fit.content`
* `AbstractAuiFragment.layoutBatchId` != `AbstractAuiAdapter.layoutBatchId`