@file:Suppress("unused")

package `fun`.adaptive.example

import `fun`.adaptive.backend.builtin.ServiceImpl
import `fun`.adaptive.service.ServiceProvider

@ServiceProvider
class ExampleServiceImpl : ServiceImpl<ExampleServiceImpl>()
