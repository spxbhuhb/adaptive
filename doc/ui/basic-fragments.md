# Basic Fragments

## Image

Displays an image with the given size:

### Instructions

```kotlin
image("/background.jpg", Size(92f, 92f))
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