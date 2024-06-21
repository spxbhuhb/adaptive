# Motivation

> [!IMPORTANT]
>
> This writing is **extremely opinionated**.
>
> Please do not feel attacked in any ways, even if you love the technologies and
> patterns I mention and/or criticize.
>
> Attacking your believes and the work of others **is not the goal** of this writing.
> I respect how much time and effort goes into those works.
>
> Most of this might seem like I wage a (flame)war on Compose but that's not true. Compose is
> a mature, valuable product, developed by hundreds of pretty smart people. Adaptive
> is in its infancy, an experimental project, developed by lonely me. It is
> pointless to compare the two.
>
> The **goal is** to share my thoughts, realize a few things behind concepts we use in
> IT, and think about the ways forward.
>

I can summarize my motivation for writing Adaptive in three words:

- pretty code
- flow
- freedom

# The Beginning

## Remember { immutability is a lie }

I like immutability, it is a very useful **tool** in software development. Also, it is a shameless lie.

This example comes from Compose, but it is not Compose-specific. The general idea of immutability is actually
**impossible** at application level. An immutable application simply doesn't work.

```kotlin
val name by remember { mutableStateOf("") }
```

Programmers figured many ways to get around this. Stores, redux, messages, buses, and so on. These technologies are very
useful, because they provide patterns and infrastructure for the application to rely on.

On the other hand these patterns started to become forced. When I want a simple hello world application, I really
don't care about enterprise level best practices.

And this brings us to the real reason I started to write Adaptive.

**I wanted to be free and simply express what I want without most of the technological mumbo-jumbo.**

That line above look like this in my mind:

```kotlin
var name = ""
```

## The Good, the Bad and the Ugly

To be honest, I really don't like the syntax of Compose. I feel that code is simply ugly. I realize
it is probably possible to make it prettier. But the general concept is far from my taste.

--- 

No, just no.

```kotlin
Text(
    text = topAppBarText,
    textAlign = TextAlign.Center,
    modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
)
```

---

This is a bit better, but this would mean that the general code style has
to be changed, which is something I don't think I want:

```kotlin
text ( topAppBarText, AlignSelf.center, fillMaxSize )
```

---

The pretty one. Easy to read, easy to understand, easy to write. (This actually doesn't work yet, but
I'm pretty close to it, let's say 90%).

```kotlin
text { topAppBarText } + alignSelf.center + sizeFull
```

I figured that when I write UI code, I really don't like additional syntax like the parentheses, chained function
calls, and so on.

## The Present

As of now, in addition to the things above, I feel that Adaptive has started to become a very interesting
project.

It offers so much freedom and tools, that working with it is simply fun. When I have a problem, I can
just express in simple words what I want, and it is usually very close to the actual code I have to
write.

Navigation is the best example.

> I basically want to replace a part of the UI when the user clicks on something.

Ok, what do I need for this?

- a fragment that can replace its children - easy
- an instruction that creates the replacement - easy
- a way to find the fragment to replace - already exists

> I want to add history as well.

Ok, what do I need for this?

- just save the current children and restore them on back/forward

The whole process feels natural. This is the **flow** part.

## The Future

Well, I don't know. :)

That said, I have quite a few plans for Adaptive (check [What is Adaptive - Goals](./what-is-adaptive.md#goals)).

To be honest, I don't expect much enthusiasm about this project. It is a bit too far from the mainstream, there
are good viable - and well marketed - alternatives. Also, it is pretty hard to make it production ready for the
public, I don't really know if I'll ever mark it so.

We'll see how it turns out. I've put in the work, and I'll continue to do so in the foreseeable future.