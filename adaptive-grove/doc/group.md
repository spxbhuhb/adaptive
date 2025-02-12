# Group

* a sheet item is part of a group when its `group` property is not null
* `component` is a sheet item that is part of a group
* the `containing frame` of the group is the smallest rectangle that contains all components of the group
* a group is a sheet item itself

## Operation behaviour

### Select

* the group is selected when one of its component is selected
  * clicking inside the containing frame but not on a component should not select the group
* double-click on a component should select that component specifically
  * in this case operations apply to that component only
* when a group is selected, only the group item is put into the selection

### Remove

* When a group is removed, all the items in the group are removed.


