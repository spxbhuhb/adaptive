# Problems with removeActual

Related documentation: [Actual UI](../internals/Actual%20UI.md)

The fragment indicated in the error message should implement `removeActual`.

If this is just a normal fragment:

* the bug is probably in Adaptive, please open a GitHub issue or message me on Slack

If this is an expect or dependent fragment you've written:

* check if `removeActual` is actually implemented
* if so, this is probably a bug in Adaptive please open a GitHub issue or message me on Slack

