# Resource tips

## Passing the resource key

If you do not want to pass the actual resource, but the key of the resource,
use `resourceKey(<resource-object>::<resource-key>)`.

This is useful in cases when you want to persist a message, but also you want
to display the translated message on the UI.

[example](example://resourceKeyExample)