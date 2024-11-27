package `fun`.adaptive.cookbook

import `fun`.adaptive.auto.api.auto
import `fun`.adaptive.backend.backend
import `fun`.adaptive.backend.builtin.service
import `fun`.adaptive.backend.builtin.store
import `fun`.adaptive.backend.builtin.worker
import `fun`.adaptive.backend.setting.dsl.inline
import `fun`.adaptive.backend.setting.dsl.settings
import `fun`.adaptive.cookbook.auth.AccountService
import `fun`.adaptive.cookbook.auth.AccountTable
import `fun`.adaptive.cookbook.file.FileService
import `fun`.adaptive.exposed.inMemoryH2
import `fun`.adaptive.ktor.ktor
import `fun`.adaptive.lib.auth.auth

fun main() {

    cookbookCommon()

    backend(wait = true) {

        settings {
            inline("KTOR_WIREFORMAT" to "JSON")
        }

        inMemoryH2("db")

        auth()
        ktor()
        auto()

        store { AccountTable }
        service { AccountService() }
        worker { CookbookWorker() }
        service { FileService() }
    }

}