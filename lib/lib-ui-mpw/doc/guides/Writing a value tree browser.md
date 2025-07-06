# Writing a value tree browser

Implementing a [tool pane](def://) and [content pane](def://) for a [value tree](def://) is quite
straightforward:

1. Write a [spec](def://) class that [values](def://) in the [value tree](def://) use.
2. Write a [value tree definition](def://) (possibly as part of a [value domain definition](def://)).
3. Implement the [tool pane](def://). 
4. Implement the [content pane](def://).

If you add a [tool pane action](def://) to the [tool pane](def://) possibly with a [context menu](def://),
you can easily add/reorder/remove values.

## Writing a spec class

Customize the example below to store the data you will store in the [spec](def://) of tree nodes.
You can store any kind of data supported by [Adat classes](def://).

[ExampleTreeValueSpec](example://)

## Writing a value tree definition

Customize the example below based on [How to define a value domain](guide://) or
use [AvTreeDef](class://) to define a tree without a domain.

Note the type alias for the [values](def://) the tree handles, it uses the [spec](def://)
you've just written.

[treePaneExampleDomainDef](example://)

## Implementing the tool pane

Customize the [view backend](def://) and the [UI fragment](def://) in the examples below.
based on [How to implement a tool pane](guide://).

Remember to add the pane to your module definition as explained in the guide.

### Tool pane view backend

[ExampleTreeToolViewBackend](example://)

### Tool pane UI fragment

[exampleTreeToolPane](example://)

## Implementing the content pane

Customize the [view backend](def://) and the [UI fragment](def://) in the examples below.
based on [How to implement a content pane](guide://).

Remember to add the pane to your module definition as explained in the guide.

### Content pane view backend

[ExampleTreeContentViewBackend](example://)

### Content pane UI fragment

[exampleTreeContentPane](example://)