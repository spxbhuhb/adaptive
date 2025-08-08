# Question

How are [file system paths](def://) represented in [Adaptive](def://)?

# Answer

[File system paths](def://) are represented with instances of `Path` class from `kotlinx.io.files`.

---

# Question

What utility functions do [Adaptive](def://) offers for working with [file system paths](def://).

# Answer

[Adaptive](def://) offers many convenience functions for working with [file system paths](def://).

[path](functionCollection://)

---

# Question

How is [random access](def://) supported by [Adaptive](def://)?

# Answer

[Adaptive](def://) offers an abstraction for [random access](def://) through [RandomAccessPersistence](class://).

This class defines functions commonly used when accessing binary data randomly. Descendants of the
class implement the platform-specific access functions.

Currently, the following implementations are provided:

[RandomAccessPersistence](classTree://)