# Unit test strategy

* DO NOT USE BeforeTest
* unit tests are written in Kotlin assuming multiplatform environment
* no Java specific testing functions may be used
* assume that unit tests will run parallel
* unit tests shall be independent and do not use the results of each other
* for file operation use `kotlinx.io`
* to get a path to a test directory use `clearedTestPath()` from the "`fun`.adaptive.utility" package
* cover corner cases, initialization, re-initialization