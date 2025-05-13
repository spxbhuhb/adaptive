# Building value trees

[Values](def://) in a [value store](def://) can be organized into value trees.

This relies on the proper uses of [markers](def://) and on the [AvRefListSpec](class://) class.

To build a value tree, you need to designate two markers and two [reference labels](def://):

Markers:

- one for the [values](def://) that are nodes in the tree
- one for the [values](def://) that contain lists of children references

Reference labels:

- one for referencing the parent node
- one for referencing the [value](def://) that contains the list of children references

## Order of children

This coding pattern maintains the order of the children. The [refs](property://AvRefListSpec) property
of [AvRefListSpec](class://) is list, so order is kept.

[AvComputeContext](class://) offers convenience functions to manage children lists:

- [getSiblingIds](fun://AvComputeContext)
- [addChild](fun://AvComputeContext)
- [removeChild](fun://AvComputeContext)
- [moveUp](fun://AvComputeContext)
- [moveDown](fun://AvComputeContext)

## Example

For example, let's assume we want to build a tree of spaces that represent a building.

In this case:

- the `space` marker marks all nodes of the tree
- the `space-children` marker marks [values](def://) that contain an [AvRefListSpec](class://)
- the `spaceParentRef` reference label references the parent node
- the `spaceChildrenRef` reference label points to the [value](def://) which contains an [AvRefListSpec](class://)

This test case uses different markers and reference labels, but the concept is the same.

- [AvTreeSubscriberTest](example://)