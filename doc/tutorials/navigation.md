# Navigation

> [!IMPORTANT]
>
> This tutorial is somewhat **OBSOLETED**. It should still work, but I've found that using
> slots for navigation raise a few hard to solve issues. Now `lib-ui` offers `NavState` 
> with `auto` support, the cookbook uses it and it works quite OK.
> 
> I'm keeping the slot concept because it might be useful later, but I'm not 100% sure about that.
>

> [!NOTE]
>
> This tutorial uses browser for examples but - when stated otherwise - all examples apply to all
> platforms.
>

## Bread and Butter

The bread and butter of navigation in Adaptive is the `slot` fragment.

Let's have a look at a very simple case:

* `slot` let's you change its content
* `navClick` does just that, it changes the content of a `slot`

```kotlin
val slotOneKey = name("slotOne")

@Adaptive
fun slotOne() {
    slot(slotOneKey) { content1() }
}

@Adaptive
private fun content1() {
    text("this is content 1 (click to change)") .. navClick(slotOneKey) { content2() }
}

@Adaptive
fun content2() {
    text("this is content 2 (click to change)") .. navClick(slotOneKey) { content1() }
}
```

## Routing

Let's make this a bit more complicated and add routing,

* You can use the `route` instruction on a `slot` to activate routing.
* For mobile applications routing is useful only for deep links.
* For web application you usually want routing.

```kotlin
val slotTwoKey = name("slotTwo")

@Adaptive
fun slotTwo() {
    slot(slotTwoKey) {
        route { content3() }
        route { content4() }

        content3()
    }
}

@Adaptive
private fun content3() {
    text("this is content 3 (click to change)") .. navClick(slotTwoKey) { content3() }
}

@Adaptive
fun content4() {
    text("this is content 4 (click to change)") .. navClick(slotTwoKey) { content4() }
}
```

## Internals

Related internal details:

- [Detach](../internals/detach.md)