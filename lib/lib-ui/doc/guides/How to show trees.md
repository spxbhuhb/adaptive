# How to show trees

Trees in [user interfaces](def://) are typically quite complex, especially if you want
automatic updates, tree editing functions such as adding, removing and re-ordering nodes.

---

## Hard-coded examples

[Basic tree](actualize:///cookbook/tree/example/basic)

[Value tree](actualize:///cookbook/tree/example/value)

---

## Playground

[Tree playground](actualize:///cookbook/tree/playground)

---

## Details

When coding a UI tree in [Adaptive](def://), you typically use the [tree](fragment://)
fragment with [TreeViewBackend](class://) as the [view backend](def://) and
[TreeItem](class://) as the tree item class.

## Basic trees

Code of [treeBasicExample](function://) and [treePlayground](function://) show you
how to use the [tree](fragment://) fragment if you manage the tree items manually.

[treeBasicExample](example://)

## AvUiTree

[AvUiTree](class://) can build the tree items for [TreeViewBackend](class://) automatically.

For this to work, you have to use the [lib-value](def://) module of [Adaptive](def://),
and have a [value tree](def://) in a [value store](def://).

Building a [value tree](def://) is actually quite easy, see [building value trees](guide://)
for more information.

[treeValueExample](example://)
