# Building value trees

[value tree](def://?inline)

[Values](def://) in a [value store](def://) can be organized into [value trees](def://).

This relies on the proper uses of [markers](def://), [reference labels](def://) and
the [AvRefListSpec](class://) class.

## Tree definition

[value tree definitions](def://) are used to define how the [values](def://) in the tree are linked
together.

They are typically part of a [value domain definition](def://) (see [how to define a value domain](guide://)
for more information).

Tree definitions are modeled by the [AvTreeDef](class://) that stores tree-specific
[markers](def://) and [reference labels](def://):

Markers:

- [nodeMarker](property://AvTreeDef) marks the [values](def://) that are nodes in the tree
- [childListMarker](property://AvTreeDef) marks the [values](def://) that contain lists of children references
- [rootListMarker](prooperty://AvTreeDef) marks the [value](def://) that contains the root node list of the tree (optional)

Reference labels:

- [parentRefLabel](prooperty://AvTreeDef) for referencing the parent node of a child node
- [childListRefLabel](prooperty://AvTreeDef) for referencing the [value](def://) that contains the list of child references

## Tree functions

[AvComputeContext](class://) provides functions to manage the tree:

### Adding tree nodes

[addTreeNode](function://AvComputeContext)

Adding a root node:

[addRootNodeExample](example://value_tree_examples)

Adding a child node:

[addChildNodeExample](example://value_tree_examples)

### Linking tree nodes

[linkTreeNode](function://AvComputeContext)

[linkTreeNodeExample](example://value_tree_examples)

### Removing tree nodes

[removeTreeNode](function://AvComputeContext)

[removeTreeNodeExample](example://value_tree_examples)

### Reordering children

[moveTreeNodeUp](function://AvComputeContext)

[moveTreeNodeUpExample](example://value_tree_examples)

[moveTreeNodeDown](function://AvComputeContext)

[moveTreeNodeDownExample](example://value_tree_examples)

### Querying the tree structure

[getTreeChildIdsExample](function://AvComputeContext)

[linkTreeNodeExample](example://value_tree_examples)

[getTreeSiblingIdsExample](function://AvComputeContext)

[linkTreeNodeExample](example://value_tree_examples)

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

- [AvTreeSubscriberTest](class://)
- [TestTreeItem](class://)

## Displaying value trees

[AvUiTree](class://) and [tree](fragment://) from [lib-ui](def://) can be used to 
display value trees with just a few lines of code.

See [How to show trees](guide://) for more information.