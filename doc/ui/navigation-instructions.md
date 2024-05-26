# Common Instructions

## ExternalLink

Opens a link in a browser (on mobile) or on a new tab (in browser) when clicked/tapped.

```kotlin
text("Terms of Service", ExternalLink("https://my.site/terms.txt"))
text("Terms of Service", externalLink("https://my.site/terms.txt"))
```

You might want to create a helper function similar to this:

```kotlin
fun siteLink(href : String) = ExternalLink("https://my.site/${href.removePrefix("/")}")

text("Terms of Service", siteLink("terms.txt"))
```