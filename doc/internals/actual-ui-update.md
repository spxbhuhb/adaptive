# UI update

**NOTE** In this document "parent", "children", "ancestor" and "descendant" are used in the context of layout, not the
relative in the fragment tree.

There are two kind of UI updates:

* layout independent updates
* layout dependent updates

Layout independent updates are applied immediately, by `genPatchInternal`.

These do not use the results of the layout calculations but include padding, margin 
and border which have an effect on the calculations.

Layout dependent updates have to be optimized as layout is a CPU heavy process.

## Start

Layout update may be needed in these cases:

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

The actual layout update starts when patching has been done and the fragment that initiated
the patching calls its own `closePatchBatch` function.

## Update Batch

All layout relayed updates happen in an update batch.

An update batch:

* executed when `AbstractAuiFragment.closePatchBatch` is called
* calls `AbstractAuiAdapter.closePatchBatch`
* `AbstractAuiAdapter.updateBatchId`
  * copied into `AbstractAuiFragment.updateBatchId` when the fragment is added to the batch
  * incremented by 2 by `AbstractAuiAdapter.closePatchBatch`
  * used by patch optimization logic
* calls `updateLayout` of items in `AbstractAuiAdapter.updateBatch`
* clears `AbstractAuiAdapter.updateBatch`

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

## Optimization

When not qualified, all fields and functions are from `AbstractAuiAdapter`.

The optimization uses two numbers to skip fragments:

* `markedId` = `updateBatchId` at the time `closePatchBatch` is called
* `updateId` = `markedId` + 1

* `markedId` is copied into `AbstractAuiFragment.updateBatchId` when the fragment is added to the batch during patching
* `updateId` is copied into `AbstractAuiFragment.updateBatchid` when `AbstractAuiFragment.updateLayout` is called

`updateId` is used to avoid duplicate updates. This may happen if an ancestor fragment had already called
`AbstractAuiFragment.updateLayout`.

### Sorting the update batch

The layout optimization starts with sorting `updateBatch` by the fragment id.

As the fragment id immutable this guarantees that the order of the fragments in
the batch is such that ancestor is before descendants.

This makes moving fragments from a tree position to another impossible without copy (to generate a new fragment id),
but that's OK for now. The layout update is a much-more frequent operation than fragment move, it is better to
optimize update.

### Updating fragments

* The fragments are updated by going over the batch, calling `AbstractAuiFragment.updateLayout`.
* `AbstractAuiFragment.updateLayout` checks `updateId` and skips update if it has been already updated
* When `item` is null, `AbstractAuiFragment.updateLayout` is called by `AbstractAuiAdapter.closePatchBatch`
* When `item` is not null, `AbstractAuiFragment.updateLayout` is called by the child fragment

```text
fun updateLayout(updateId : Long, item : AbstractAuiFragment?) {
    // ...
}
```

The main layout optimization happens when the fragment decides to handle the update by itself or to propagate
it to its parent. This decision is made by calling `AbstractAuiFragment.shouldUpdateSelf`.

When `shouldUpdateSelf` returns with true, the fragment size won't change by the layout update. In general
this is true when the following condition is true.

```text
has instructed width || fit.container.horizontally
  &&
has instructed height || fit.container.vertically  
```

For grids there is an additional option. When there are only fixed columns the grid size won't change by the update.

### Container dependent optimization

Containers may have behaviour dependent optimization. For example, box does not have to reposition all items when one
of its items change in size, but it may have to reposition that one element.