# Fragment Store

## Use Cases

### Grove Designer

The grove designer could use a fragment store to provide different rendering of fragments.
More specifically, the main canvas shows the normal rendering, but there could be a tree
which displays the tree of fragments like this:

```text
f 1
  |-- f 1.1
  +-- f 1.2
    +--  f 1.2.3
f 2 
```

This tree should be updated real-time as the user adds/moves/removes fragments.

It could be also possible that the user moves fragments in this tree and those
moves should be applied to the main canvas as well.

### Navigation

Assuming a navigation model which is a tree fragments, we could show a navigation
on the UI, by simply rendering the tree. However, this tree should be updated
real-time on navigation change.

For example, if we show a file system, file system changes should change the
tree automatically. It could also be possible to let the user move fragments around
and then apply those changes to the file system.

### Tables

Tables may show real-time data. In this case we could use a fragment tree to store the
data and expect that all changes in the data will be reflected in the table.

It would be also possible for the user to edit the data in the table and those
changes would be propagated automatically to the server and to other users.
