## How to contribute to Adaptive

First of all, join the `#fun-adaptive` [kotlinlang](https://slack-chats.kotlinlang.org/) channel on Slack.

I try to check the channel daily, there you can ask anything related to Adaptive.

## Names

**core modules**: The `adaptive-core`, `adaptive-gradle-plugin`, `adaptive-kotlin-plugin` modules. These often require
special handling because they do not have the compiler plugin. So, usual stuff like `@Adat` or `@CallSiteName` does not work.

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
  - they are small and clearly belongs to each other
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