# Grove-Doc UI

The UI has two tool panes and one content pane:

- reference tool
- doc tool
- doc content

The doc tool shows the documentation tree from `Documentation.md` in `doc-main`.
The reference tool shows everything grouped by subproject and type.

Doc content is opened both from doc tool and ref tool.

Documentation lookup happens when:

- browser is opened with a URL that starts with `/documentation`
- back and forward browser navigation
- the user clicks on a link in the doc content
- the user uses Double-Shift and selects a document

Browser URL format: `/documentation/<path>` where path is the path in `groveDocDomain.treeDef`
MD link format is `guide://` or `definition://`

`ReferenceToolViewBackend` implements `MultiPaneUrlResolver` and translates between 
the two formats.
