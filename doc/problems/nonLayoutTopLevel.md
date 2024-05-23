# Non-Layout top-level fragment

The top level fragments of an adaptive UI must be a layout fragment such as [stack](../ui/ui.md#stack),
[grid](../ui/ui.md#grid) or [pixel](../ui/ui.md#pixel).

For example this code will throw an exception:

```kotlin
ios {
    text("Hello World!")
}
```

To fix it, surround the content with a layout:

```kotlin
ios {
    stack {
        text("Hello World!")
    }
}
```