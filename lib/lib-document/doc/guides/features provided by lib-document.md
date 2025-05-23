# Features provided by lib-document

[convert lib-document docs](todo://)

`lib-document` provides:

- a document data model
- UI fragments to display the data model
- utilities to convert various formats into the document data model
- visitors and transformers to process documents (such as collecting table of contents)

The document data model is a somewhat abstract model of everyday documents: headers, paragraphs, lists etc.

The basic concept is that we can convert whatever we want (for example, a Markdown file) into this model 
and then use one set of UI fragments that can process the model to display the document.

Hopefully sooner or later we'll have a visual editor based on the model as well.

## Rendering

To render a whole document:

```kotlin
@Adaptive
fun someFun(resource : DocumentResourceSet) {
    docDocument(resource)
}
```

Documents are resources, `docDocument`:

- uses the `uri` of the resource to figure out what kind the document is 
- compiles the resource data into a `DocDocument`
- displays the document

For now, only Markdown is supported, in this case the URI has to end with `.md`.

## Resource variations

You can load documents as:

- normal application resources, see [Resources](def://).
- remote resources loaded from wherever
- an inline resource specified directly

```kotlin
docDocument(
    Documents.markdown_demo
)

docDocument(
    remoteDocument("http://127.0.0.1:3000/resources/fun.adaptive.cookbook/documents/markdown_demo.md")
)

docDocument(
    inlineDocument(".md", "# Header\n\nJust some inline markdown".encodeToByteArray())
)
```

## Markdown support

Supported Markdown structures:

- header
- paragraph
- link
- list (bullet and  numbered)
- bold, italic, inline code
- code fence
- quote
- horizontal line
- image


### Actualize fragments

To actualize a fragment from markdown (assuming it is in the adapters fragment factory):

```markdown
[Stuff](actualize://cookbook:recipe:demo:goodmorning)
```

### Include data

> This feature is in-development, it does not work yet.

Pass an Adat class in the `arguments` paramter of `docDocument`:

```kotlin
@Adat
class MyDocumentData(
    val text : String
)

docDocument(Documents.some_markdown, MyDocumentData("Hello World!"))
```

And then in the markdown: 

```markdown
[Message](argument://text)
```