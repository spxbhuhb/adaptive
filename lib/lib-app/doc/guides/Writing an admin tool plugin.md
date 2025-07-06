# Writing an admin tool plugin

[admin tool plugin](def://?inline)

1. Write the [view backend](def://) (optional)
2. Write the [UI fragment](def://)
3. Register the [admin tool plugin](def://) in the [admin tool](def://)

## Writing the view backend

[Admin tool](def://) [view backends](def://) are standard [view backends](def://).

[AdminToolPluginViewBackendExample](example://)

## Writing the UI fragment

[Admin tool](def://) [UI fragments](def://) are standard [UI fragments](def://).

[adminToolPluginExample](function://)


## Registering the admin tool plugin

To register the [admin tool plugin](def://) with the [admin tool](def://) use
the [addAdminPlugin](function://fun.adaptive.app.ui.mpw.util) function.

The example below shows registration using a [singular content pane](def://).

[ExampleAdminToolModuleMpw](class://)