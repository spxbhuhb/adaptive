# Button

---

## Hard-coded examples

[Normal button](actualize:///cookbook/input/button/example/normal)

[Disabled button](actualize:///cookbook/input/button/example/disabled)

[Submit button](actualize:///cookbook/input/button/example/submit)

[Danger button](actualize:///cookbook/input/button/example/danger)

---

## Playground

[Button playground](actualize:///cookbook/input/button/playground)

---

## Details

```kotlin
button(Strings.someLabel) .. onClick { "stuff" } 
```

### Variants

* `button`
* `submitButton`
* `dangerButton`

### Sizing

- Button width defaults to label/icon size (plus decorations), if not instructed otherwise.
- Height is the default input height + 3 DP