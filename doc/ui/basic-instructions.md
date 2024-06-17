# Basic Instructions

## Sizing

```text
frame(top: DPixel, left: DPixel, width: DPixel, height: DPixel)
position(top: DPixel, left: DPixel)
size(width: DPixel, height: DPixel)
height(height: DPixel)
width(width: DPixel)

sizeFull
heightFull
widthFull
```

## backgroundColor

```text
backgroundColor(color : Color)
```

## backgroundGradient

```text
leftToRightGradient(leftColor : Color, rightColor: Color)
backgroundGradient(startPosition: RawPosition, endPosition : RawPosition, start: Color, end: Color)
```

## border

```text
border(color: Color, width: DPixel = 1.dp)
borderTop(color: Color, width: DPixel = 1.dp)
borderRight(color: Color, width: DPixel = 1.dp)
borderBottom(color: Color, width: DPixel = 1.dp)
borderLeft(color: Color, width: DPixel = 1.dp
```

## cornerRadius

```text
cornerRadius(all: DPixel)
cornerRadius(topLeft: DPixel? = null, topRight: DPixel? = null, bottomLeft: DPixel? = null, bottomRight: DPixel? = null)

cornerTopRadius(top: DPixel)
cornerBottomRadius(bottom: DPixel)

cornerTopLeftRadius(topLeft: DPixel)
cornerTopRightRadius(topRight: DPixel)
cornerBottomLeftRadius(bottomLeft: DPixel)
cornerBottomRightRadius(bottomRight: DPixel)
```

## color

```text
color(value: UInt)
```

## dropShadow

dropShadow(color: Color, offsetX: DPixel, offsetY: DPixel, standardDeviation: DPixel)

## gap

```text
gap(both: DPixel)
gap(width: DPixel? = null, height: DPixel? = null)
```

## margin

```text
margin(top: DPixel? = null, right: DPixel? = null, bottom: DPixel? = null, left: DPixel? = null)
margin(all: DPixel)
marginTop(top: DPixel)
marginRight(right: DPixel)
marginBottom(bottom: DPixel)
marginLeft(left: DPixel)
```

## padding

```text
padding(top: DPixel? = null, right: DPixel? = null, bottom: DPixel? = null, left: DPixel? = null)
padding(all: DPixel)
paddingTop(top: DPixel)
paddingRight(right: DPixel)
paddingBottom(bottom: DPixel)
paddingLeft(left: DPixel)
```