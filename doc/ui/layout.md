# Layout

The main goal of the layout system - apart providing layouts - is to be deterministic across
different platforms.

The layout system is Adaptive is writing direction dependent horizontally, but fixed vertically.

This means that surrounding values (padding, border and margin) use `start` and `end` instead
of `left` and `right`. The actual mapping to `left` and `right` depends on instructions.

Vertical surroundings use `top` and `bottom`.

The difference between the two directions comes from the fact that arabic languages actually
do stick to right-to-left writing while easter ones use left-to-right instead of their traditional
top-to-down.

## Values

`sp` - Scaled Pixel

- for font size
- `SPixel` class
- conversion is adapter dependent

`dp` - Device Independent Pixel

- position, dimensions (width and height)
- `DPixel` class
- converted into `raw`
    - before any layout calculation (typically in `AdaptiveInstruction.apply`)
    - by `toPx(dPixel: DPixel): Double` of the adapter

`raw` - RawPixel

- position, dimensions (width and height)
- `Double`
- all layout calculations use `raw` values
- `RenderData` contains only `raw` values
- `+Inf` is used to indicate an "unbound" dimension (`Double.POSITIVE_INFINITY`)

## Layout data

Layout calculations work with these data:

- `instructed` = the position and dimensions specified by instructions such as `width` or `position`
- `inner` = dimensions of the inner content when measurable
- `surrounding` = `padding` + `border-width` + `margin`, directional (meaning each direction has its own value)
- `outer` = `measured` + `surrounding`
- `proposed` = position and dimensions proposed by the parent layout
- `computed` = final result of the layout calculations

The `computed` data set is applied to the actual UI, but the exact application is adapter dependent.
For example, in browsers to have the `margin` effect (that is, no background), you have to use an
actual CSS margin but that does not count to width and height in the browser box model.

All layout data use `raw` values.

## Containers

- Fragments with actual UI representation are put into containers.
- All containers are descendants of the `AbstractContainer`class.
- Container fragments perform the layout calculations.

## Basic rules

All layout calculations start with known fixed dimensions. This is typically the dimensions of the root
container passed to the adapter.

When a top level container is added to the adapter (`AdaptiveAdapter.addActualRoot`):

- a `proposedSize` size is created: `RawSize(rootContainer.width, rootContainer.height)`
- `layout(proposedSize)` is called to
    - perform the layout calculations
    - apply the `computed` values to the contained fragments
- the fragment is added to the actual UI (to `rootContainer`)

`instructed` values

- trump everything else
- when a value is instructed, that one is used, no matter the calculations
- if only one value is instructed (width for example), the others (height for example) will be computed

## Proposed dimensions and overflow

- Proposed dimensions are container type dependent.
- A container is **unbound** in a direction when
    - its `proposedFrame` is unbound in that direction, **or**
    - it has a scroll instruction in that direction
- When a container is **unbound** in a direction
    - `+Inf` is passed as the proposed value for the dimension

Overflow happens when `outer > proposed`. This means that the size the parent is willing to give
to the fragment is not enough to for the fragment.

An overflow is **always a programming error**. There is no easy way to around it.

When a fragment

- is **unbound** in a direction, **and**
- is **not instructed** in that direction, **then**
- it uses the **minimum** space in that direction, which is `outer` for that given fragment

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

- gap has no meaning in `box`
- gap height has no meaning in `row`
- `column` cannot distribute space horizontally

## Layout process

The layout process starts with calling `computeLayout`:

`computeLayout(proposedWidth : Double, proposedHeight: Double)`

- computes the inner layout of the fragment if needed (`grid` for example)
- calls `computeLayout` of all child fragments
- computes `outer` of the fragment
- calls `placeLayout` of all child fragments

`placeLayout(proposedPosition: RawPosition)`

- places the fragment relative to its container

### Box

measured:

- width: `max(child.start + child.width)`
- height is `max(child.top + child.height)`

### Row

measured

- width: `sum(child.box.width) + (gapWidth * children.size - 1)`
- height: `max(child.box.height)`

### Column

measured

- width: `max(child.box.width)`
- height: `sum(child.box.height) + (gapHeight * children.size - 1)`
 

