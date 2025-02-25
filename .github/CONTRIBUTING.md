## How to contribute to Adaptive

First of all, join the `#fun-adaptive` [kotlinlang](https://slack-chats.kotlinlang.org/) channel on Slack.

I try to check the channel daily, there you can ask anything related to the project.

## Trying out things

When you open the project in IDEA, the Gradle import creates shorthands to start
the most used tasks:

```text
adaptive
  adaptive
    Tasks
       aaa
          cookbook-js
          cookbook-jvm
          grove-js
          grove-jvm
          plugin-generate-tests
          plugin-test
          sandbox-js
          sandbox-jvm
          
```

For applications (cookbook, grove, sandbox) use `jvm` to start the server side, then `js`
to start the client in a browser.

The `plugin` tasks are used during the Kotlin compiler plugin development (generate tests
and run the test).

You can also start these from the command line (I haven't tested this too much, the page opens,
so I guess it works somewhat):

```shell
./gradlew cookbook-jvm
```

And in a separate terminal (as these do not exit until you manually stop them):

```shell
./gradlew cookbook-js
```

## Running core unit tests

This is only needed after version change, otherwise it is not necessary.

To run unit tests in core you have to publish `adaptive-core` to Maven local. If you don't
do so, you'll get a dependency error.

The core actually does not use the plugin, but Gradle wants to compile it for other projects
and for that compilation you need the core artifact.

```shell
./gradlew :adaptive-core:publishToMavenLocal
./gradlew :adaptive-core:allTests
```

## Names

**core modules**: The `adaptive-core`, `adaptive-gradle-plugin`, `adaptive-kotlin-plugin` modules.

These often require special handling because they do not use the compiler plugin.
So, usual stuff like `@Adat` or `@CallSiteName` does not work.

## Adding code 

If you would like to add code, please ask first on the channel or open a GitHub issue for discussion.
This avoids unnecessary work and ensures that the code goes into one, well-defined direction.

### Code Style

Code style is in the repo, check [idea/codeStyles/Project.xml](../.idea/codeStyles/Project.xml). Formatting the code
with this code style is mandatory, PRs do not adhere these rules will be rejected.

Apart code style there are a some conventions to follow:

- write unit tests when appropriate
- do not write unnecessary unit tests for the sake of unit tests
- use `private` when something is used only locally and it does not have a unit test
- use `internal` when something is used only locally and it does have a unit test
- put functions/classes into one file in these cases only:
  - they are small and clearly belong to each other
    - extensions of the same class
    - helper/builder/access functions of the same domain
  - there is one public function which calls the others, the others are private or internal
- do not write unnecessary documentation
  - self-explaining variable and function names should not be documented
- write the necessary documentation
- add comments and explain your reasoning for non-trivial logic

### Unit tests

Unit tests that work with files should use the [clearedTestPath](/adaptive-core/src/commonMain/kotlin/fun/adaptive/utility/path.kt) function to clear the test path.

All files produced by unit tests should go to the `build/adaptive/test/<package-name>.<file-name>.<function-name>` directory.

In core modules build the path manually, in others use [@CallSiteName](/adaptive-core/src/commonMain/kotlin/fun/adaptive/reflect/CallSiteName.kt)

### @DangerousApi

- Always have a backup on a different computer or in the cloud.
- Clearly mark all dangerous operations (like deleting many files) with `@DangerousApi`.
- Delegate `@DangerousApi` if the code does not strictly ensure safety.
- Use `@OptIn(DangerousApi::class)` in the closest scope possible (e.g. the actual function call).
- Add a comment to the opt-in explaining why is it there (see example below).
- Always use named parameters for dangerous calls (see example below).

```kotlin
@OptIn(DangerousApi::class) // clearedTestPath confines delete into test working directory
processResources(
    sourcePath = testBase.resolve("set1"),
    packageName = "fun.adaptive.resource.codegen.test",
    kmpSourceSet = "jvmTest",
    generatedCodePath = generatedSources,
    preparedResourcesPath = preparedResources
)
```

### Debugging and tracing

Debugging code in a browser is not necessarily trivial, mostly because of coroutine stack traces does 
not contain enough information.

Tips that might help:

* add the `trace` instruction to the fragment
* add the `traceAll` instruction to the adapter
  * this will print out a lot, you can use a regex to filter out some entries
* use `println` (in state definition) or `debug` (after parameters or instructions):
  * `box { someInstruction.debug() }`
  * `someFragment(stuff)` -> `someFragment(stuff.debug())`
* use the `AdaptiveFragment.dumpFragmentTree()` function
* use the `name("this is my fragment")` instruction (in browser the ID of the HTMLElement will be the name)

### Notes

These are helpful during development (and I don't want to forget them).

To get layout update statistics:

```kotlin
adapter().dumpStatistics()
```