# Popup

Variations:

- hover
    - Shows when the mouse is over a container for the given time period.
    - Disappears if the mouse moves out of the container.
- primary
    - Shows when the user clicks on the container with the primary button (main action).
    - Remains if when the mouse moves our of the container.
- secondary
    - Shows when the user clicks on the container with the secondary button (context menu).
    - Remains if when the mouse moves our of the container.

Sizing strategy is `fit.content` for all variations.

```kotlin
box {
    hoverPopup {
        text(Strings.hover)
    }
    primaryPopup {
        text(Strings.primary)
    }
    secondaryPopup {
        text(Strings.primary)
    }
}
```

## Positioning

Use `popupAlign` to set the preferred position of the popup (default is `belowStart`).

```kotlin
box {
    hoverPopup {
        popupAlign.belowEnd
        text(Strings.explanation)
    }
}
```

The popup checks if there is enough space for display in the preferred position.

If there is not enough space it tries to find another position. Check `PopupAlign`
for the exact algorithm (I created that with ChatGPT, seems reasonable.)

## Size

You can set the size of the popup with the usual sizing instructions.

When `maxWidth` or `maxHeight` is used the popup will use the final sizes of
the container it belongs to.

> [!NOTE]
>
> I have no idea what happens if the size of the container that the popup belongs
> to change while the popup is shown.
>

## Internals

Popups functions use the manually coded, platform-dependent fragments, all of which extend `AbstractPopup`:

- `AuiHoverPopup`
- `AuiPrimaryPopup`
- `AuiSecondaryPopup`

`AbstractPopup`

- is a *structural* fragment, hence it does not count in the layout of the parent container,
- stops propagation of `addActual` and `removeActual`,
    - instead it creates a box which contains the anonymous fragment created by the builder function
- adds/removes event handlers to/from the container fragment for hover and click
- creates the popup on-demand, when the popup is about to be shown
- disposes the popup content when the popup hides
- uses standard select fragment to implement logics