# Basic Fragments

## Canvas

Creates a canvas for drawing. For more detailed information check: [Graphics](../graphics)

```kotlin
canvas {
    
}
```

## Image

Displays an image with the given size:

### Instructions

```kotlin
image("/background.jpg", Size(92f, 92f))
```

## SVG

Creates a canvas and adds a `canvas:svg` fragment to it. Check [Graphics](../graphics) for details.

```kotlin
svg(R.drawable.thermometer)
```

You can perform customizations on the SVG, check [Graphics](../graphics) for details.
An example (switched the color of the icon to blue):

```kotlin
svg(R.drawable.thermometer, blue)
```

## Text

Displays the string representation (`toString()`) of the first argument:

```kotlin
text("Hello World")
text(12)
```

### Instructions

```kotlin
import hu.simplexion.adaptive.ui.common.instruction.TextAlign
import hu.simplexion.adaptive.ui.common.instruction.TextWrap

text("a", FontName("Courier New"))
text("a", FontSize(12))
text("a", FontWeight(12))
text("a", LetterSpacing(- 2))
text("a", TextAlign.Center)
text("a", TextWrap.NoWrap)
```