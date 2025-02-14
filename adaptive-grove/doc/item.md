# Item

Sheet items are:

- the fragments the user edits
- stored in `SheetViewController.items`
- instances of `SheetItem`

- Each item has a unique `index` which is the index in `SheetViewController.items`.
- `SheetViewController.nextIndex` is used to get the next index.
- When put on the clipboard, `SheetClipboardItem` stores copied data.

These operations add new items:

- `Add`
- `Paste`
- `Group`
- `Undo`
- `Redo`

These operations mark items removed:

- `Remove`
- `Cut`
- `Ungroup`
- `Undo`
- `Redo`

## Item lifecycle

Main events of the item lifecycle:

1. The item is created. This is a one-time operation, cannot be reversed.
2. The item is shown: additions and undo of removals.
3. The item is hidden: removals and undo of additions.

Creation of an item use `SheetController.createItem`:

- creates an instance of `SheetItem`
- adds the created instance ot `SheetViewController.items`

Additions and undo of removals use `SheetViewController.showItem`:

- sets `SheetItem.removed` to false
- sets group of the item
- sets members if the item is a group
  - also sets `group` for all members
- adds all removed links to the other items
- adds the fragment by calling `GroveDrawingLayer.plusAssign`

Removes and undo of additions use `SheetViewController.hideItem`:

- keeps the item in `SheetViewController.items`
- backups the item information into `SheetItem.beforeRemove`
  - can be used by undo/redo
- set `SheetItem.removed` to true
- clears the group of the item
- clears the members of the item if it is a group
- removes all links to the removed item from other items
- removes the fragment by calling `GroveDrawingLayer.minusAssign`