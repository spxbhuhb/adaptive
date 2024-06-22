# Navigation

In a mobile application you typically just define a slot and replace its content whenever necessary:

```kotlin
import hu.simplexion.adaptive.foundation.Adaptive

fun main() {
    android {
        column {
            slot(historySize = 3) {
                text("default slot content")
            }

            text("click to change to 1") .. onClick { next() } .. target { content1() }
            text("click to change to 2") .. link { content1() }

            text("back") .. back
            text("forward") .. forward
        }    
    }
}

@Adaptive
fun content1() {
    text("this is content 1")
}

@Adaptive
fun content2() {
    text("this is content 1")
}
```

Let's see what is what in the code above.

### slot

`slot` is a fragment that provides an easy way to replace its children. The default content
is loaded when the slot is created. Later then `next`, `link`, `back` and `forward` replaces
the content of the slot.

`historySize` instructs the slot to keep a history, this lets you go back and forward between fragments.

### target

`target` tells the component what should be loaded into slot. `target` itself does not do anything,
it just provides the information.

You can also pass parameters to the replacements:

```kotlin
@Adaptive
fun someFragment(i : Int) {
    text("Click to replace with a parameter") .. target { someOtherFragment(i + 1) }
}
```

> [!CAUTION]
>
> The values you pass to the new fragment are **detached** from the fragment that contains `target`.
> This means that the changes of `i` in the example above are **NOT** propagated to `someOtherFragment`.
>
> This is intentional.
>

### next

`next` is a function you can call in an event handler to replace the content of a slot.

`next` looks for a `target` instruction and uses the fragment specified in target to replace the
content of the slot.

### link

`link` is a shorthand for `onClick { next() } .. target { content() }`. When you do not need
additional processing before the navigation you can just use link.

### back

`back` is a shorthand for `onClick { back() }`. It replaces the content of the slot with the
first backward history content if there are any. If there is no previous entry, the current
content will remain active.

### forward

`forward` is a shorthand for `onClick { forward() }`. It replaces the content of the slot with
the first forward history content if there are any. If there is no previous entry, the current
content will remain active.

## Routing

> [!NOTE]
>
> This approach to routing might feel unusual as there are no textual paths written into the code.
>
> Thinking about it, I've realized that I really haven't had a look at the paths in my
> applications for quite a while. Honestly, I have no idea what paths I use, except a few
> special cases (activation link sent in e-mail for example).
>

When applications use deep links, they have to provide routing information, so it is
possible to decide which fragments to use.

The `route` instruction can be used for this:

```kotlin
slot {
    route { content1(navInt, navString) }
    route { content2() }
    
    text("default content")
}
```

When `route` instructions are added to the slot, the slot:

* gets the [navigation data](#navigation-data) from the [navigation support](#navigation-support)
* gets the next navigation segment, and builds the fragment that belongs to that segment.
* if there are parameters use `navInt`, `navString` etc. to get them from the segments.

So, assuming the slot above is the topmost slot of the application, the following routes are handled:

* `/content1/123/hello` results in `content1(123, "hello")`
* `/content2` results in `content2()`
* everything else results in `text("default content")`

## Navigation support

Navigation support provides the connection between Adaptive fragments and the underlying platform.

It is reachable through the adapter in the `navSupport` property, but it is rarely needed to use
it directly.

The support stores the **active navigation data** in the `data` property. Navigation data is
built from the URI the application starts with. During slot changes it is updated to reflect
the current navigation state of the application.

The navigation data can be used for routing or getting deep links for the users to share.

### Navigation data

URIs are mapped into the `NavData` class.

```kotlin
data class NavData(
    val segments : List<String>,
    val query : Map<String,String>,
    val tag : String?
)
```

Functions that provide navigation typically consume the `segments` part of the

### navParam

Fragments can get a navigation query parameter with the `navParam` producer:

```kotlin
fun myFun() {
    val param = navParam("myNumber")
    
    if (param == "1") {
        text("this is param 1")
    } else {
        text("this is not param 1")
    }
}
```

## Internals

Related internal details:

- [Tree operations](../internals/tree-operations.md)