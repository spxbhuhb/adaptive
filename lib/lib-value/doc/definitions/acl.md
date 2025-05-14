# ACL

An ACL, an acronym for Access Control List, describes who can access data in what way. It is used
for authorization in [lib-value](def://) where each [value](def://) may have an ACL.

Each ACL is an [AvValue](class://) itself.

The [acl](property://AvValue) property of [AvValue](class://) contains the ACL of the given [value](def://).
This property is used to decide if the given [principal](def://) can access the [value](def://) or not.