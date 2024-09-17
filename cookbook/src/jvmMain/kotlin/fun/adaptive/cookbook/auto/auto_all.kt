package `fun`.adaptive.cookbook.auto

suspend fun main() {
    `fun`.adaptive.cookbook.auto.autoFile_autoFile.Recipe().run()
    `fun`.adaptive.cookbook.auto.autoFolder.Recipe().run()
    `fun`.adaptive.cookbook.auto.autoFolder_autoFile.Recipe().run()
    `fun`.adaptive.cookbook.auto.autoFolder_autoList.Recipe().run()
    `fun`.adaptive.cookbook.auto.autoFolder_autoListPoly.Recipe().run()
    `fun`.adaptive.cookbook.auto.autoListPoly_autoFolder_autoListPoly.Recipe().run()
}