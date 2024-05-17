# Problems with invalidIndex

Related documentation:

* [Rendering Transformation](../internals/RenderingTransformation.md)
* [Problems with manualImplementation](./manualImplementation.md)

If the fragment indicated in the error message is a normal fragment:

* the bug is probably in Adaptive, please open a GitHub issue or message me on Slack

If the fragment has a manual implementation:

* if it's your code the problem is probably in the implementation, the indices in one of the `gen` functions is probably wrong
* otherwise, please open a GitHub issue or message me on Slack