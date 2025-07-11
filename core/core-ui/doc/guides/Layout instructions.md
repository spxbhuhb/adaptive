# Layout instructions

## Size and position

```text
frame(top: DPixel, left: DPixel, width: DPixel, height: DPixel)

position(top: DPixel, left: DPixel)

size(size: DPixel) = Size(size, size)
size(width: DPixel, height: DPixel)
maxSize

height(height: DPixel)
height(calc: () -> DPixel)
maxHeight

width(width: DPixel)
width(calc: () -> DPixel)
maxWidth

sizeStrategy.container
sizeStrategy.container.vertical
sizeStrategy.container.horizontal

sizeStrategy.content
sizeStrategy.content.vertical
sizeStrategy.content.horizontal

fillStrategy.constrain
fillStrategy.constrainReverse
fillStrategy.resizeToMax
fillStrategy.none
```

## Gap

```text
gap(calcBoth: () -> DPixel)
gap(both: DPixel)

gap(width: DPixel? = null, height: DPixel? = null)

gapHeight(height: () -> DPixel)
gapHeight(height: DPixel):

gapWidth(width: () -> DPixel)
gapWidth(width: DPixel)
```

## Padding

```text
padding(all: () -> DPixel)
padding(all: DPixel)

padding(top: DPixel? = null, right: DPixel? = null, bottom: DPixel? = null, left: DPixel? = null)

paddingHorizontal(horizontal: DPixel)
paddingHorizontal(horizontal: () -> DPixel)

paddingVertical(vertical: DPixel)
paddingVertical(vertical: () -> DPixel)

paddingTop(top: DPixel)
paddingTop(top: () -> DPixel)

paddingRight(right: DPixel)
paddingRight(right: () -> DPixel)

paddingBottom(bottom: DPixel)
paddingBottom(bottom: () -> DPixel)

paddingLeft(left: DPixel)
paddingLeft(left: () -> DPixel)
```

## Margin

```text
margin(all: () -> DPixel)
margin(all: DPixel)

margin(top: DPixel? = null, right: DPixel? = null, bottom: DPixel? = null, left: DPixel? = null)

marginTop(top: DPixel)
marginTop(top: () -> DPixel)

marginRight(right: DPixel)
marginRight(right: () -> DPixel)

marginBottom(bottom: DPixel)
marginBottom(bottom: () -> DPixel)

marginLeft(left: DPixel)
marginLeft(left: () -> DPixel)
```

## Alignment

### Align self

```text
alignSelf.center

alignSelf.top

alignSelf.topStart
alignSelf.topCenter
alignSelf.topEnd

alignSelf.start

alignSelf.startTop
alignSelf.startCenter
alignSelf.startBottom

alignSelf.end

alignSelf.endTop 
alignSelf.endCenter
alignSelf.endBottom

alignSelf.bottom

alignSelf.bottomStart
alignSelf.bottomCenter
alignSelf.bottomEnd
```

### Align items

```kotlin
alignItems.center

alignItems.top

alignItems.topStart
alignItems.topCenter
alignItems.topEnd

alignItems.start

alignItems.startTop
alignItems.startCenter
alignItems.startBottom

alignItems.end

alignItems.endTop 
alignItems.endCenter
alignItems.endBottom

alignItems.bottom

alignItems.bottomStart
alignItems.bottomCenter
alignItems.bottomEnd
```

### Space distribution

```text
spaceAround
spaceBetween
```

### Scroll

```text
scroll
verticalScroll
horizontalScroll

fixed 
overflow
```

### Popup

```text
popupAlign.beforeAbove
popupAlign.beforeTop
popupAlign.beforeCenter
popupAlign.beforeBottom
popupAlign.beforeBelow

popupAlign.afterAbove
popupAlign.afterTop
popupAlign.afterCenter
popupAlign.afterBottom
popupAlign.afterBelow

popupAlign.aboveBefore
popupAlign.aboveStart
popupAlign.aboveCenter
popupAlign.aboveEnd
popupAlign.aboveAfter

popupAlign.belowBefore
popupAlign.belowStart
popupAlign.belowCenter
popupAlign.belowEnd
popupAlign.belowAfter

popupAlign.centerCenter

popupAlign.absoluteCenter(modal: Boolean = false,topMax: DPixel? = null)
```

## Z-index

```text
zIndex(value: Int)
zIndex(value: () -> Int)
```

## Flow box

```text
flowItemLimit(limit: Int)
flowItemLimit(limit: () -> Int)
```

## Grid

```text
colTemplate(vararg tracks: GridTrack, extend : GridTrack? = null)
rowTemplate(vararg tracks: GridTrack, extend : GridTrack? = null)

infix GridTrack.repeat(count: Int)

colSpan(span: Int)
rowSpan(span: Int)

gridCol(col: Int, span: Int = 1)
gridRow(row: Int, span: Int = 1)

gridPos(row: Int, col: Int, rowSpan: Int = 1, colSpan: Int = 1)

Number.gridCol
Number.gridRow
Number.rowSpan
Number.colSpan
```