# AI training

The documentation system compiles all available information into a dataset for AI training.

The `aaa:doc` and `grove-doc:compileAdaptiveDocumentation` Gradle tasks perform this
compilation and put the results in the `build/adaptive/doc` directory (under the repository root).

The `merged` directory contains the datasets for training.

To fast prompt engineering, instruct the AI to use the files from the `merged` directory
like this:

```text
I'm building an AI training data set to train a coding assistant for my software library.
The attached files contains definitions, guides, question and answer pairs that form
the data set.

The definitions are to build a concept graph, guides give detailed information and usage
 examples, while questions and answers target specific use cases.
```