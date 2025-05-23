## Built-in UI container fragments

[core-ui](def://) provides a few commonly used [ui container fragments](def://):

- [box](fragment://)
- [flowBox](fragment://)
- [rootBox](fragment://)
- [column](fragment://)
- [row](fragment://)
- [grid](fragment://)
- [splitPane](fragment://)
- [manualLayout](fragment://)

In addition, [lib-ui](def://) provides many advanced fragments that build on these base containers.

## Box

[box](fragment://) positions its children:

- directly with x and y coordinates **or**
- by aligning them with standard alignment instructions

[boxExample](example://built_in_ui_container_fragments)

## Flow Box

[flowBox](fragment://) positions its children in a row next to each other until there
is no more available space. When there is no more space, opens a new row below.

[flowBoxExample](example://built_in_ui_container_fragments)

Use [flowItemLimit](instruction://) to limit the number of fragments in a row:

[flowBoxWithLimitExample](example://built_in_ui_container_fragments)

## Root Box

[rootBox](fragment://) is the same as [box](fragment://) with one key difference: it is added
directly to the [root container](def://) instead of the parent fragment.

[rootBox](fragment://) can be used for popups, modals, snacks and other UI components that should
be placed outside the normal [ui layout](def://).

[rootBoxExample](example://built_in_ui_container_fragments)

## Row

[row](fragment://) positions its children next to each other.

[rowExample](example://built_in_ui_container_fragments)

Use [gap](instruction://) to add space between the items:

[rowGapExample](example://built_in_ui_container_fragments)

Use [spaceBetween](instruction://) or [spaceAround](instruction://) to adjust children's positions
according to the available space and the size of the children.:

[rowSpaceBetweenExample](example://built_in_ui_container_fragments)

[rowSpaceAroundExample](example://built_in_ui_container_fragments)

See [built-in layout instructions](guide://) for other child alignment instructions.

## Column

[column](fragment://) positions its children below to each other.

[columnExample](example://built_in_ui_container_fragments)

Use [gap](instruction://) to add space between the items:

[columnGapExample](example://built_in_ui_container_fragments)

Use [spaceBetween](instruction://) or [spaceAround](instruction://) to adjust children's positions
according to the available space and the size of the children.:

[columnSpaceBetweenExample](example://built_in_ui_container_fragments)

[columnSpaceAroundExample](example://built_in_ui_container_fragments)

See [built-in layout instructions](guide://) for other child alignment instructions.

## Grid

[grid](fragment://) provides a partial implementation of the [CSS grid layout](https://developer.mozilla.org/en-US/docs/Web/CSS/CSS_grid_layout).

* Basic use: instruct column and row sizes with [colTemplate](instruction://) and [rowTemplate](instruction://).
* Use `dp` to define fixed length columns, `fr` to define fractional length columns.
* [colTemplate](instruction://) and [rowTemplate](instruction://) defaults to `1.fr` if not specified.
* Rows also extend by `1.fr` if not instructed otherwise, see [extension](#automatic-grid-extension).

This example shows a grid with only fixed length columns:

[gridExample](example://built_in_ui_container_fragments)

This example shows a grid with fixed length and fractional length columns mixed:

[gridFrExample](example://built_in_ui_container_fragments)

### Automatic grid extension

* When not switched off, grids add rows to contain fragments if needed.
* Use the `extend` parameter of `rowTemplate` and `colTemplate` to set extension.
* If `extend` is set to `null` automatic extension is off.
* Default for `extend` is `1.fr` for rows, `null` for columns.
* If the automatic extension is off, the grid throws an exception on overflow.

This example adds `20.dp` rows as needed (in this case all rows are added automatically).

[gridExtendExample](example://built_in_ui_container_fragments)

### Grid sizing

* When a grid has an instructed width or height, that value is used.
* When the grid has only fixed size rows or columns, the sum of the row/column sizes is used.
* Otherwise, the size proposed by the parent container is used.

### Placing fragments directly

Direct fragment placing cannot be used for fragments placed by automatic grid extension.

To put a fragment at a given grid position and/or make it span columns/rows, use one or a combination of
these:

- [Number.gridCol](instruction://)
- [Number.gridRow](instruction://)
- [Number.colSpan](instruction://)
- [Number.rowSpan](instruction://)
- [gridCol](instruction://)
- [gridRow](instruction://)
- [rowSpan](instruction://)
- [colSpan](instruction://)
- [gridPos](instruction://)

[gridPlacingExample](example://built_in_ui_container_fragments)