## Standalone date input

```kotlin
var date = localDate()
 
withLabel("Date") {
    dateInput(date, it) { v -> date = v }
}
```

* Click on the input to show the popup.
* Pressing `Esc` closes the popup.
* `Cancel` resets the date to the value before the popup has been opened.
* `Ok` just closes the popup.

* **Month and year positioning does not work yet.**
* The text is not editable, that's a can of worms I really don't want to open now.

> I'm not happy with the popup design. It is based on Material 3, but I feel that it clashes with the other styles used on the page.