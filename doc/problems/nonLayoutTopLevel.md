# Non-Layout top-level fragment

The top level fragments of an adaptive UI must be a layout fragment from [Layout Fragments](../ui/layout-fragments.md).

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