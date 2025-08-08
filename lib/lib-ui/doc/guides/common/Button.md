# Button

[examples](actualize://example-group?name=button)

## Playground

[Button playground](actualize://example/input/button/playground)

## Details

All button functions accept these parameters (except `theme`, which 
is available only for the basic [button](function://):

- `label` - the label of the button, optional
- `icon` - the icon of the button, optional
- `viewBackend` - view backend, optional, one will be created if not passed
- `theme` - theme to use, optional, variants use different themes
- `onClickFun` - the function to call when the button is clicked, optional

### Behavior

Buttons are focusable and handle the `Enter` key the same way they handle click.

### Sizing

- Button width defaults to label/icon size (plus decorations), if not instructed otherwise.
- Height is the default input height + 3 DP