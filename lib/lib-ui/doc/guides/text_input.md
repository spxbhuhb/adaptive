# Text input

---

## Hard-coded examples

[Simple text input example](actualize:///cookbook/input/text/example/simple)

---

## Playground

[Text input playground](actualize:///cookbook/input/text/playground)

---

## Details

### Variants

**standalone**

* `textInput`
* `textAreaInput`

**editor**

* `textEditor`
* `textAreaEditor`

### Standalone

```kotlin
val backend = textInputBackend("initial value")

textInput(backend)
```

### Editor

```kotlin
textEditor { template.someText }
```