# What is Adaptive about

Adaptive is a Kotlin Multiplatform library for full-stack (server + Browser/JS, Android, iOS) application development.

It differs from existing libraries in how it approaches reactivity. Adaptive has been inspired by [Svelte](https://svelte.io),
but the idea has grown and turned into something much more interesting.

The soul of Adaptive is the so-called foundation transformation. This transformation turns the functions marked
with the `@Adaptive` annotation into stateful classes that are **reactive** by default. During runtime these
classes are used to build a tree of fragments that reflect the current state of the application.

Something like this:

![Adaptive Transform](adaptive-transform.png)

This approach provides a quite unique toolset:

- you can easily inspect and modify this fragment tree during runtime
- it is very easy to replace an implementation with something different
- it is possible to serialize/deserialize a fragment tree automatically
- it is rather easy to build a graphical editor for the fragment tree

Honestly, that list can go on. While all these functions are of course possible with other frameworks, the
difference is that Adaptive was designed for this.

The ultimate goal of Adaptive is to give the end users tools to interact with the UI during runtime through an AI.

For example: "please change the color of the middle line to blue"

With Adaptive this is "easy" to implement.
