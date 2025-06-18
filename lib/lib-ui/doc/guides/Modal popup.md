# Modal popup

[modal popup](def://?inline)

## Hard-coded examples

[Open popup on click](actualize:///cookbook/popup/modal/example/open-on-click)

[Custom popup](actualize:///cookbook/popup/modal/example/custom)

[Empty popup](actualize:///cookbook/popup/modal/example/empty)

[Multi-page popup](actualize:///cookbook/popup/modal/example/multi-page)

## Details

To display modal popups, use the [dialog](function://) function.

* [dialog](function://) is **NOT** a [UI fragment](def://), it is a simple function that adds the modal as a [root fragment](def://).
* You can call it from anywhere, assuming you have access to the frontend [workspace](def://) or the frontend [adapter](def://).
* Second parameter of [dialog](function://) is the data you pass to the modal.
* Third parameter of [dialog](function://) is the fragment that renders the content of the modal.
* [dialog](function://) provides two state variables for the rendering fragment:
    * the data from its second parameter
    * a function that can be called to close the modal

[customPopupExample](example://)

### Built-in convenience fragments

There are a few built-in fragments you can use as building blocks for the content of the modal.

These mostly provide styling and some commonly used logic such as cancel and save buttons.

Complete surrounding (header, buttons):

* [basicModal](fragment://) - title, you supply content and buttons
* [editorModal](fragment://) - title, cancel and save buttons, you add content
* [multiPageModal](fragment://) - title, cancel and save buttons, you define pages and content for each page

Components:

* [modalPopupTitle](fragment://) - the title used by the fragments above
* [modalCancelSave](fragment://) - cancel and save buttons for the fragments above