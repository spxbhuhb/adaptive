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

## Example

For example, let's assume we want to build a tree of spaces that represent a building.

In this case:

- the `space` marker marks all nodes of the tree
- the `sub-spaces` marker marks [values](def://) that contain an [AvRefListSpec](class://)
- the `parentSpaceRef` reference label references the parent node
- the `subSpaceRefs` reference label points to the [value](def://) which contains an [AvRefListSpec](class://)

[Value Tree Test](example://)