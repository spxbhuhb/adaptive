---
status: review
---

# Question

How do I register an enum class for wireformat?

# Answer

1. You have to add a companion object for your enum class as shown below.
2. You have to add the enum class to the registry in your [application module definition](def://) by overriding [wireFormatInit](function://AppModule).

[ExampleEnum](example://)