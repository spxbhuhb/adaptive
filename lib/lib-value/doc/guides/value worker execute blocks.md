# Value worker `execute` blocks

[execute](function://AvValueWorker) function of [value workers](def://) executes a code 
block inside the main event loop of the worker.

This example shows how to add a value to a value store inside an `execute` block:

[basicExample](example://value_worker_execute_blocks)

Using [execute](function://AvValueWorker) guarantees that:
 
- no other changes are applied to the values during the execution of the block
- the changes made by the block are atomic to all other worker functions

Behavior:

- [execute](function://AvValueWorker) waits until the block is executed or timeout has been reached (this wait does not lock the worker)
- [execute](function://AvValueWorker) throws an exception if the block throws an exception
- **It is possible that the block is executed after time out**.
- The block **MUST BE FAST**, it runs under the value worker lock, stops everything else related to he values in the store.
- rollback **MUST BE HANDLED** by the block, the worker does not guarantee it

Calling [execute](function://AvValueWorker) does not run the block immediately. Instead, it adds the block
to the operation queue of the worker and waits for the execution. If timeout (5 seconds by default) is reached
[execute](function://AvValueWorker) throws an exception.

It is important that the block may be executed even if the exception has been thrown.

## Compute context

When the block is about to run, the worker creates a [compute context](def://), an instance 
of [AvComputeContext](class://). The [compute context](def://) gives the block access to inner
worker functions and provides the transactional behavior.

Values changed by the block are collected, and when the block returns, the worker notifies
everyone subscribed for those values.

## Compute context functions

The [compute context](def://) provides a number of functions to make working with
values convenient.

### Adding values

[unaryPlus](function://AvComputeContext)
[addValue](function://AvComputeContext) 

[addExample](example://value_worker_execute_blocks)

### Getting values

[get](function://AvComputeContext)
[getOrNull](function://AvComputeContext)

[getExample](example://value_worker_execute_blocks)

### Updating values

[unaryPlus](function://AvComputeContext)

### Adding and removing references

[addRef](function://AvComputeContext)
[removeRef](function://AvComputeContext)

### Getting referred values

[ref](function://AvComputeContext)
[refOrNull](function://AvComputeContext)

[addRefExample](example://value_worker_execute_blocks)

[removeRefExample](example://value_worker_execute_blocks)

### Working with value trees

[AvComputeContext](class://) provides convenience functions to work with [value trees](def://).

- [addTreeNode](function://AvComputeContext)
- [linkTreeNode](function://AvComputeContext)
- [removeTreeNode](function://AvComputeContext)
- [moveTreeNodeUp](function://AvComputeContext)
- [moveTreeNodeDown](function://AvComputeContext)
- [getTreeChildIds](function://AvComputeContext)
- [getTreeSiblingIds](function://AvComputeContext)

For details see [Building value trees](guide://).