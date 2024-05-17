# Problems with replacedByPlugin

Possible causes:

* the plugin is not applied to the project:
    * if you use gradle catalog: add `alias(libs.plugins.adaptive)` to `plugins` in `build.gradle.kts`
    * otherwise: add `id("hu.simplexion.adaptive")` to `plugins` in `build.gradle.kts`
* otherwise, it is a bug in Adaptive
  * open a GitHub issue or contact me on Slack