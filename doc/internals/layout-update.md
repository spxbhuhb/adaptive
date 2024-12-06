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

## Fit Content, Fit Container

There are two main strategies for sizing a fragment: 

* setting its size to the size of its content - `fitContent`
* setting its size to the size its container proposes - `fitContainer`

Layout update optimization decisions have to know which strategy is used by the fragment.

Fragments of the main root element generally use `fitContainer`.

Notable cases when `fitContent` is desired:

- dialog box
- snackbar
- pop-up

For buttons and textual information the choice between `fitContent` or `fitContainer` depends
on the context.

