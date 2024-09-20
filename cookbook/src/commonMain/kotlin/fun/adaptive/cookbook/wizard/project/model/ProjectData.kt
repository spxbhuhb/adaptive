package `fun`.adaptive.cookbook.wizard.project.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties

@Adat
class ProjectData(
    val projectName: String = "",
    val packageName: String = "",
    val browser : Boolean = true,
    val android : Boolean = false,
    val ios : Boolean = false,
    val ktor : Boolean = true,
    val auth : Boolean = true,
    val auto : Boolean = true,
    val exposed : Boolean = true
) {
    override fun descriptor() {
        properties {
            projectName blank false pattern "^[a-zA-Z0-9_.-]+\$"
            packageName blank false pattern "^[a-zA-Z]+[a-zA-z0-9]*(\\.[a-zA-Z]+[a-zA-Z0-9]*)*\$"
        }
    }
}