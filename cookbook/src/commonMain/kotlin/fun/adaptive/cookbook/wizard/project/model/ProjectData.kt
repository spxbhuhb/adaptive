package `fun`.adaptive.cookbook.wizard.project.model

import `fun`.adaptive.adat.Adat
import `fun`.adaptive.adat.api.properties

@Adat
class ProjectData(
    val projectName: String,
    val packageName: String,
    val browser : Boolean,
    val android : Boolean,
    val ios : Boolean,
    val ktor : Boolean,
    val auth : Boolean,
    val auto : Boolean,
    val exposed : Boolean
) {
    override fun descriptor() {
        properties {
            projectName blank false pattern "^[a-zA-Z0-9_.-]+\$"
            packageName blank false pattern "^[a-zA-Z]+[a-zA-z0-9]*(\\.[a-zA-Z]+[a-zA-Z0-9]*)*\$"
        }
    }
}