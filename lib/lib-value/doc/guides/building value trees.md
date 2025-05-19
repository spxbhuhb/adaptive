# Building value trees

[Values](def://) in a [value store](def://) can be organized into [value trees](def://).

This relies on the proper uses of [markers](def://), [reference labels](def://) and
the [AvRefListSpec](class://) class.

## Tree setup

Use the utility class [AvTreeSetup](class://) to store the tree-specific [markers](def://)
and [reference labels](def://):

Markers:

- [nodeMarker](property://AvTreeSetup) marks the [values](def://) that are nodes in the tree
- [childListMarker](property://AvTreeSetup) marks the [values](def://) that contain lists of children references
- [rootListMarker](prooperty://AvTreeSetup) marks the [value](def://) that contains the root node list of the tree (optional)

Reference labels:

- [parentRefLabel](prooperty://AvTreeSetup) for referencing the parent node of a child node
- [childListRefLabel](prooperty://AvTreeSetup) for referencing the [value](def://) that contains the list of child references

## Tree functions

[AvComputeContext](class://) provides functions to manage the tree:

- [addTreeNode](function://AvComputeContext)
- [removeTreeNode](function://AvComputeContext)
- [moveTreeNodeUp](function://AvComputeContext)
- [moveTreeNodeDown](function://AvComputeContext)
- [getTreeChildIds](function://AvComputeContext)
- [getTreeSiblingIds](function://AvComputeContext)

## Order of children

Trees built this way maintain the order of the children. The [refs](property://AvRefListSpec) property
of [AvRefListSpec](class://) is list, so order is kept.

## Example

For example, let's assume we want to build a tree of spaces that represent a building.

In this case:

- the `space` marker marks all nodes of the tree
- the `space-children` marker marks [values](def://) that contain an [AvRefListSpec](class://)
- the `space-roots` marker marks the [value](def://) that contains the root list
- the `spaceParentRef` reference label references the parent node
- the `spaceChildrenRef` reference label points to the [value](def://) which contains an [AvRefListSpec](class://)

## Subscribing to value trees

The [AvTreeSubscriber](class://) abstract class provides logic to subscribe for complete [value trees](def://).

This is an abstract class to be extended. The extending class has to override the tree-specific
properties and functions.

See the class used for the unit tests for an example:

- [TestTreeSubscriber](class://)
- [TestTreeItem](class://)
