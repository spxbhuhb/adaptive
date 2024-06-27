# Layout

The main goal of the layout system - apart providing layouts - is to be deterministic across
different platforms.

Adaptive layouts are writing direction dependent horizontally, but fixed vertically.

This means that surrounding values (padding, border and margin) use `start` and `end` instead
of `left` and `right`. The actual mapping to `left` and `right` depends on instructions.

Vertical surroundings always use `top` and `bottom`.

The difference between the two directions comes from the fact that arabic languages actually
do stick to right-to-left writing while easter ones use left-to-right instead of their traditional
top-to-down.

> [!IMPORTANT]
>
> When talking about child fragments in this document they **DO NOT** mean `AdaptiveFragment.children`,
> but rather the **children in the actual UI scope**. These are descendants in the fragment tree, but
> not necessarily children.
>

## Values

`sp` - Scaled Pixel

- for font size
- `SPixel` class
- conversion is adapter dependent

`fr` - Fraction

- for space distribution
- `Fraction` class
- used in calculations, no conversion

`dp` - Device Independent Pixel

- position, sizes in dimensions (width and height)
- `DPixel` class
- converted into `raw`
    - before any layout calculation (typically in `AdaptiveInstruction.apply`)
    - by `toPx(dPixel: DPixel): Double` of the adapter

`raw` - RawPixel

- position, sizes in dimensions (width and height)
- `Double`
- all layout calculations use `raw` values
- `RenderData` contains only `raw` values
- `+Inf` is used to indicate an "unbound" size (`Double.POSITIVE_INFINITY`)

## Layout data

Layout calculations work with these data:

- `instructed` = the position and sizes specified by instructions such as `width` or `position`
- `inner` = sizes of the inner content when measurable
- `surrounding` = `padding` + `border-width` + `margin`, directional (meaning each direction has its own value)
- `proposed` = position and sizes proposed by the parent layout
- `final` = final result of the layout calculations

The `final` data set is applied to the actual UI, but the exact application is adapter dependent.
For example, in browsers to have the `margin` effect (that is, no background), you have to use an
actual CSS margin but that does not count to width and height in the browser box model.

All layout data use `raw` values.

## Containers

- Fragments with actual UI representation are put into containers.
- All containers are descendants of the `AbstractContainer`class.
- Container fragments perform the layout calculations.

## Basic rules

All layout calculations start with known fixed sizes. This is typically the sizes of the root
container passed to the adapter.

When a top level container is added to the adapter (`AdaptiveAdapter.addActualRoot`):

- `computeLayout(rootContainer.width, rootContainer.height)` is called to
  - perform the layout calculations
  - apply the `final` values to the contained fragments
- the fragment is added to the actual UI (to `rootContainer`)

`instructed` values

- trump everything else **when has meaning in the layout type**
- when a value is instructed, that one is used, no matter the calculations
- if only one value is instructed (width for example), the others (height for example) will be computed

## Proposed sizes and overflow

- `proposed` sizes are container type dependent
- A container is **unbound** in a direction when
  - the `proposed` size is unbound in that direction, **or**
    - it has a scroll instruction in that direction
- When a container is **unbound** in a direction
  - `+Inf` is passed to its children as the `proposed` value for the size

Overflow happens when `final > proposed`. This means that the size the parent is willing to give
to the fragment is not enough to for the fragment.

An overflow is **always a programming error**. There is no easy way to around it.

When a fragment

- is **unbound** in a direction, **and**
- is **not instructed** in that direction, **then**
- it uses the **minimum** space in that direction, which is `inner` + `surrounding` for that given fragment

## Alignment

Instructions that align fragments in a container:

- `alignItems` which may have
    - `horizontal` value, an `alignment`
    - `vertical` value, an `alignment`

- `alignSelf` which may have
    - `horizontal` value, an `alignment`
    - `vertical` value, an `alignment`

- `gap` which may have
    - `width`
    - `height`

- `spaceDistribution` enum with values
    - `between`
    - `around`

`alignment` is an enum (but not an instruction) with values

- `start` (this also applies to top)
- `center`
- `end` (this also applies to bottom)

Gap and space distribution is:

- layout dependent
- some layouts may simply ignore these instructions
- space distribution is meaningful only for **bound** directions

A few examples when alignment is ignored:

- gap has no meaning in `box` with `absolute` mode
- gap height has no meaning in `row`
- `column` cannot distribute space horizontally

## Layout process

The layout process starts with calling `computeLayout`:

`computeLayout(proposedWidth : Double, proposedHeight: Double)`

- computes the inner layout of the fragment if needed (`grid` for example)
- calls `computeLayout` of all child fragments
- computes `final` of the fragment
- calls `placeLayout` of all child fragments

`placeLayout(proposedTop: Double, proposedLeft: Double)`

- places the fragment relative to its container

## Type dependent calculations

### Intrinsic UI fragments

Intrinsic UI fragments are fragments that

- have actual UI representation, **and**
- do not have any descendant fragments **under the same adapter** that has actual UI representation.

Examples: are `text`, `image`, `canvas`. Canvas is tricky here because it has fragments with actual UI
representation but those have a canvas specific adapter.

Intrinsic fragments may or may not be able to measure their own `inner` content.

final height =

- instructed, if present
- `inner` + surrounding, if possible to measure
- proposed, if not unbound
- surrounding

final width =

- instructed, if present
- `inner` + surrounding, if possible to measure
- proposed, if not unbound
- surrounding

### Box

Box has three modes:

- `absolute` which means that all items have instructed positions or self-alignment
- `horizontalFlow` which means that items are placed in a flow horizontally
- `verticalFlow` which means that the items are placed in a flow vertically

#### Absolute

final height =

- instructed, if present
- proposed, if not unbound
- `max(child.instructedTop + child.finalHeight)` + surrounding

final width =

- instructed, if present
- proposed, if not unbound
- `max(child.instructedLeft + child.finalWidth)` + surrounding

#### Horizontal Flow

The horizontal flow box places the children in rows.

**bound** horizontally

- children are put into rows until there is available space in the row, the rows are **bound**
- final width = `instructed` or `proposed` width

**unbound** horizontally

- there is only one row that contains all children
- final width = `sum(child.finalWidth) + gapWidth * (children.count - 1)` + surrounding

final height =

- instructed, if present
- proposed, if not unbound vertically
- `sum(row.finalHeight) + gapHeight * (row.count - 1)` + surrounding

#### Vertical Flow

The vertical flow box places the children in columns.

**bound** vertically

- children are put into columns until there is available space in the column, the columns are **bound**
- final height = `instructed` or `proposed` height

**unbound** vertically

- there is only one column that contains all children
- final height = `sum(child.finalHeight) + gapHeight * (children.count - 1)` + surrounding

final width =

- instructed, if present
- proposed, if not unbound vertically
- `sum(column.finalWidth) + gapWidth * (column.count - 1)` + surrounding

### Row

final height =

- instructed, if present
- proposed, if not unbound
- `max(child.finalHeight)` + surrounding

final width =

- instructed, if present
- proposed, if not unbound
- `sum(child.finalWidth) + gapWidth * (children.count - 1)` + surrounding

### Column

final height =

- instructed, if present
- proposed, if not unbound
- `sum(child.finalHeight) + gapHeight * (children.count - 1)` + surrounding

final width =

- instructed, if present
- proposed, if not unbound
- `max(child.finalWidth)` + surrounding

### Grid

A grid track is:

- **bound** when it has a fix size (like `20.dp`)
- **unbound** when
  - it is a fraction (like `1.fr`) **or**
  - is a `minContent` track

#### Cell assignment

The first step of grid layout is to assign children to grid cells.
This is used during space distribution.

#### Distributable space

`availableSpace` is the space the grid have in a given direction:

- instructed, if present
- proposed (both bound and unbound)

When the available space is **unbound**:

- all **bound tracks**
  - use their instructed size
- `minContent` tracks use
  - `max(track.fragments.final)` with unbound proposed size (to make them minimal sized)
- all fraction track use
  - `max(track.fragments.final)` with unbound proposed size (to make them minimal sized)

When the available space is **bound**:

- all **bound tracks**
  - use their instructed size
- `minContent` tracks use
  - `max(track.fragments.final)` with proposed size unbound (to make them minimal sized)
- fraction tracks use
  - `availableSpace - sum(bound-tracks) - sum(minContent-tracks) - surrounding`

#### Distributing spanned tracks

Spanned track distribution:

- assigns **bound** tracks to their fix size
- assigns `0` to `minContent` tracks
- splits the remaining space between fraction tracks

In the following example the height of the min-content track is unknown.

```text
1.fr           1   2
minContent     1   2   3
1.fr           1       3
```