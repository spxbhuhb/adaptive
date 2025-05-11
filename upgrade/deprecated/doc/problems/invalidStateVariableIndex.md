# Problems with invalidStateVariableIndex

Related documentation:

* [Higher Order Functions](../internals/mechanisms.md#higher-order-arguments)
* [Problems with manualImplementation](./manualImplementation.md)
 
This error means that the code tried to overreach the state of the fragment.

If the fragment indicated in the error message is a normal fragment:

* the bug is probably in Adaptive, please open a GitHub issue or message me on Slack

If the fragment has a manual implementation:

* if it's your code the problem is probably in the implementation, the indices in one of the `gen` functions is probably wrong
* otherwise, please open a GitHub issue or message me on Slack