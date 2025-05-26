# Question

How do I include [definitions](def://) in [guides](def://) to avoid duplicating the text?

# Answer

Use the shorthand `[definition-name](def://?inline)`. The [documentation compiler](def://) recognizes
the `inline` argument and adds the text of the definition to the guide.

The included definition has only the actual text included, the title and the `See also` section is
removed.