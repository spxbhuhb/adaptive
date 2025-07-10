# Navigation

## Browser

[navState](property://BrowserApplication) contains the current navigation state of the application.

This is an observable property, you can add a listener to it to get notifications when it changes.

[historyStateListener](property://BrowserApplication) of [BrowserApplication](class://) (an instance of
[BrowserHistoryStateListener](class://)) handles history events from the browser. On `popstate`
browser event it changes the value of the [navState](property://BrowserApplication).

[Multi-pane workspace](guide://) has its own approach to navigation. See [Navigation](guide://lib-ui-mpw) for
more information.