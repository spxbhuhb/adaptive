# Motivation

I can summarize my motivation for writing Adaptive in three words (ok, it's four, I know):

- pretty code
- flow
- freedom

I've been struggling for years because I've always felt that the existing tools are very capable, but they
are not for me. Ugly code, a lot of boilerplate, breaking my flow of thoughts with syntax, just to mention
a few of the problems.

I always wanted to **simply** express what I want and voilà, it works!

I love the strictness of Kotlin, I love the strong typing, the well-thought-out language rules. Kotlin itself
is very close to what I like.

On the other hand I've always felt that the ecosystem lacks the fluency, lacks a particular kind of beauty
code can reach when it's clean and expressive.

Not to mention that many official libraries are very, very closed. This is for a reason of course. To help
the developers and the programmers write easy to upgrade, error free code.

This closeness strong-handles me into ways which I really-really don't like.

So, at the end it is personal preference. I like my freedom, my flow and my pretty-pretty code so much that
I've spent countless hours on writing Adaptive. And honestly, the more I spend the more I enjoy working with it.

In addition to the things above, I feel that Adaptive has started to become a very interesting project.

It offers a quite unique toolset. The fragments and the instructions give so much freedom to work with,
many ideas are trivial to implement.

There is a cost for this freedom. Adaptive is in its infancy, covers mostly what I need for my own internal
projects. It lacks many of the usual strictness at the high level, the compiler plugin is a kind of
"I've seen it work, sometimes, I think, maybe?" thing. It is nowhere close to a complete toolset,
I don't even start to compare it to Compose.

To be honest, I don't expect much enthusiasm about this project. It is a bit too far from the mainstream, there
are good viable - and well marketed - alternatives. Also, it is pretty hard to make it production ready for the
public, I don't really know if I'll ever mark it so.

We'll see how it turns out. I've put in the work, and I'll continue to do so in the foreseeable future.

# Credits

**Multiplatform**

* [Kotlin](https://kotlinlang.org) (by the Kotlin Foundation, Apache 2.0)
* [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) (by JetBrains, Apache 2.0)
* [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) (?, Apache 2.0)

**Server side**

* [PostgreSQL](https://www.postgresql.org) (by The PostgreSQL Global Development Group, PostgreSQL Licence)
* [Exposed](https://github.com/JetBrains/Exposed) (by the JB Team, Apache 2.0)
* [HikariCP](https://github.com/brettwooldridge/HikariCP) (by Brett Wooldridge, Apache 2.0)
* [LOGBack](http://logback.qos.ch) (by QOS.ch, EPL v1.0 or LGPL 2.1)
* [JavaMail](https://javaee.github.io/javamail/)  (by Oracle, CDDL 1.0)

**Building**

* [Gradle](https://gradle.org) (by Gradle Inc., Apache 2.0)

**Testing**

* [H2](https://www.h2database.com/) (by multiple contributors, MPL 2.0 or EPL 1.0)
* [SubEtha SMTP](https://github.com/voodoodyne/subethasmtp) (by SubEthaMail.org, Apache 2.0)
* [kotlin-compiler-testing](https://github.com/ZacSweers/kotlin-compile-testing) (by multiple contributors, MPL 2.0)

**Copied from**

* [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform) (by JetBrains, Apache 2.0)
    * parts of the resources module
    * the resources part of the Gradle plugin
* bcrypt (by Damien Miller, public domain)
* [Rails](https://github.com/rails/rails)
  * contributing guidelines

**Inspiration**

* [Svelte](https://svelte.dev) (the whole idea)
* [KVision](https://kvision.io) (some service related ideas)
* [Tailwindcss](https://tailwindcss.com) (style concept)

**Images and colors**

* [SvgPathEditor](https://yqnn.github.io/svg-path-editor/) SVG Path Editor by Yann Armelin
* [ChatGPT](https://chatgpt.com)
    * cartoon-like images are generated with ChatGPT 4o
    * colors are generated with ChatGPT 4o

# License

> Copyright (c) 2024 Simplexion Kft, Hungary and contributors
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this work except in compliance with the License.
> You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.