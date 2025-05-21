# UI Rendering

UI rendering is the process that transforms [ui fragments](def://) into an [actual UI](def://). This is a 
[platform-dependent](def://) process as each [actual UI](def://) have their own way to position and style elements.

UI rendering is typically performed by [ui adapters](def://) after the [ui layout](def://) is calculated.

Creating the [actual UI](def://) element, such as a DOM node, an Android View or an iOS UIView, is
usually the responsibility of [dependent ui fragments](def://). 

Applying [ui layout](def://), styles, adding event handlers is usually done by the [ui adapter](def://) based 
on the [render data](def://) of the [ui fragment](def://).

## See also

- [render data](def://)
- [dependent ui fragment](def://)
- [ui adapter](def://)
- [actual ui](def://)
- [ui layout](def://)