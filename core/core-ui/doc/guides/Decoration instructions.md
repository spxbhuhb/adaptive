# Decoration instructions

## backgroundColor

```text
backgroundColor(color: Color)
backgroundColor(color: () -> Color)

backgroundColor(color: Int, opacity: Double = 1.0)
backgroundColor(color: UInt, opacity: Double = 1.0)
```

## backgroundGradient

```text
leftToRightGradient(leftColor : Color, rightColor: Color)
backgroundGradient(startPosition: RawPosition, endPosition : RawPosition, start: Color, end: Color)
```

## border

```text
border(color : () -> Color)
border(color: Color, width: DPixel = 1.dp)

border(color: Color, top: DPixel = 1.dp, right: DPixel = 1.dp, bottom: DPixel = 1.dp, left: DPixel = 1.dp)

borderTop(color: Color, width: DPixel = 1.dp)
borderRight(color: Color, width: DPixel = 1.dp)
borderBottom(color: Color, width: DPixel = 1.dp)
borderLeft(color: Color, width: DPixel = 1.dp

noBorder
```

## cursor

```text
cursor.pointer
cursor.colResize
cursor.rowResize
```

## cornerRadius

```text
cornerRadius(all: DPixel)
cornerRadius(all : () -> DPixel)
cornerRadius(topLeft: DPixel? = null, topRight: DPixel? = null, bottomLeft: DPixel? = null, bottomRight: DPixel? = null)

cornerTopRadius(top: DPixel)
cornerTopRadius(top: () -> DPixel)
 
cornerBottomRadius(bottom: DPixel)
cornerBottomRadius(bottom: () -> DPixel)

cornerTopLeftRadius(topLeft: DPixel)
cornerTopRightRadius(topRight: DPixel)
cornerBottomLeftRadius(bottomLeft: DPixel)
cornerBottomRightRadius(bottomRight: DPixel)
```

## dropShadow

```text
dropShadow(color: Color, offsetX: DPixel, offsetY: DPixel, standardDeviation: DPixel)
```

## textColor

```text
textColor(value: Int)
textColor(value: UInt)
textColor(value: Color)
```
