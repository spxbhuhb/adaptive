# Paragraph

Paragraph is a fragment that arranges instances of [AbstractParagraphItem](class://) into
a width-restricted flow.

The paragraph has a list of styles (instance of [AdaptiveInstructionGroup](class://)), and 
each item selects one of those styles to use.

Both the styles and the items are passed in a [ParagraphViewBackend](class://).

[examples](actualize://example-group?name=paragraph)

## Details

### Browser

Text items are rendered into `span`, links are rendered into `a`. Links get an `click` event
handler attached.

The event handler:

1. Tries to find a [local context](guide://) with a [UiNavigator](class://).
2. If there is no such context:
   1. The paragraph uses the browser default behavior for the link.
3. If there is such a context:
   1. The paragraph calls [onUrlTarget](function://UiNavigator) and checks the return value.
   2. If the return value is `true`:
      1. the navigator handled the link.
   3. If the return value is `false`:
      1. the paragraph uses the browser default behavior for the link.