# Motivation

> [!NOTE]
>
> This writing is **extremely opinionated**. Please do not feel attacked in any ways, even
> if you love the technologies and patterns I mention and/or criticize.
>
> Attacking your believes is not the goal of this writing. The goal is to share mine.
>

I like immutability, it is a very useful **tool** in software development. Also, it is a shameless lie.

This example comes from Compose, but it is not Compose-specific. The general idea of immutability is
simply **impossible** at application level. An immutable application simply doesn't work.

```kotlin
val name by remember { mutableStateOf("") }
```

Programmers figured many ways to get around this. Stores, redux, messages, buses, and so on. These technologies are very
useful, because they provide patterns and infrastructure for the application to relay on.

On the other hand these patterns started to become forced. When I want a simple hello world application, I really
don't care about enterprise level best practices.

And this brings us to the real reason I started to write Adaptive.

**I wanted to be free and simply express what I want without most of the technological mumbo-jumbo.**

To be honest, I really don't like the syntax of Compose. I feel that code is simply ugly.

```kotlin
Text(
    text = topAppBarText,
    textAlign = TextAlign.Center,
    modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
)
```

No, just no, this is a bit better, but still bad:

```kotlin
text(topAppBarText, AlignSelf.center, fillMaxSize)
```

This is what I want. Easy to read, easy to understand, easy to write. (This actually doesn't work yet, but
I'm pretty close to it, let's say 90%).

```kotlin
text { topAppBarText } .. AlignSelf.center .. fillMaxSize
```

I figured that when I write UI code, I really don't like all the chained function calls, parenthesis and commas.
All these make the code hard to read and understand.