# Document, Markdown, Excel

[lib-document](def://) provides:

- a document data model
- UI fragments to display the data model
- utilities to convert various formats (e.g. Markdown) into the document data model
- visitors and transformers for processing documents (such as collecting table of contents)

The document data model is a somewhat abstract model of everyday documents: headers, paragraphs, lists etc.

The basic concept is that we can convert whatever we want (for example, a Markdown file) into this model 
and then use one set of UI fragments that can process the model to display the document.

Hopefully, sooner or later, we'll have a visual editor based on the model as well.
