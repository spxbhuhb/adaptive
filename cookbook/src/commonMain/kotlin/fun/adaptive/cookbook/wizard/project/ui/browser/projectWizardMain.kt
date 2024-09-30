package `fun`.adaptive.cookbook.wizard.project.ui.browser

import `fun`.adaptive.adat.AdatClass
import `fun`.adaptive.adat.api.isTouched
import `fun`.adaptive.adat.api.isValid
import `fun`.adaptive.adat.store.copyStore
import `fun`.adaptive.cookbook.shared.bodySmall
import `fun`.adaptive.ui.checkbox.api.checkbox
import `fun`.adaptive.cookbook.shared.cornerRadius8
import `fun`.adaptive.cookbook.shared.darkGray
import `fun`.adaptive.cookbook.shared.inputErrorContainer
import `fun`.adaptive.cookbook.shared.inputErrorText
import `fun`.adaptive.cookbook.shared.lightGray
import `fun`.adaptive.cookbook.shared.titleSmall
import `fun`.adaptive.cookbook.wizard.project.model.ProjectData
import `fun`.adaptive.foundation.Adaptive
import `fun`.adaptive.foundation.binding.AdaptiveStateVariableBinding
import `fun`.adaptive.foundation.rangeTo
import `fun`.adaptive.ui.api.border
import `fun`.adaptive.ui.api.colTemplate
import `fun`.adaptive.ui.api.column
import `fun`.adaptive.ui.api.gap
import `fun`.adaptive.ui.api.grid
import `fun`.adaptive.ui.api.input
import `fun`.adaptive.ui.api.marginTop
import `fun`.adaptive.ui.api.padding
import `fun`.adaptive.ui.api.repeat
import `fun`.adaptive.ui.api.row
import `fun`.adaptive.ui.api.rowTemplate
import `fun`.adaptive.ui.api.text
import `fun`.adaptive.ui.api.textColor
import `fun`.adaptive.ui.instruction.dp

@Adaptive
fun fieldNote(
    hint: String? = null,
    message: String? = null,
    // TODO add an annotation that performs a compiler check: selector should be an adat class
    binding: AdaptiveStateVariableBinding<String>? = null,
    selector: () -> String
) {
    checkNotNull(binding)

    val path = binding.path
    checkNotNull(path)

    val instance = binding.stateVariableValue
    check(instance is AdatClass)

    if (binding.path!![0] == "packageName") {
        val regex = Regex("^[a-zA-Z]+[a-zA-z0-9]*(\\.[a-zA-Z]+[a-zA-Z0-9]*)*\$")
        println(regex.matches("a.b"))
        println("WTH? ${binding.value == "a.b"} >${binding.value}< ${regex.matches(binding.value)} ${instance.adatContext?.validationResult?.failedConstraints}")
        println(binding.adatCompanion!!.adatMetadata.properties.first { it.name == "packageName" }.descriptors)
    }

    when {
        ! instance.isValid(path) && instance.isTouched(path) -> {
            column {
                inputErrorContainer
                for (line in message?.split("\n") ?: listOf("Invalid field value")) {
                    text(line) .. inputErrorText
                }
            }
        }

        hint != null -> {
            column {
                marginTop { 4.dp }
                for (line in hint.split("\n")) {
                    text(line) .. bodySmall .. textColor(darkGray)
                }
            }
        }
    }
}

@Adaptive
fun projectWizardMain() {
    val setup = copyStore { ProjectData() }

    column {
        padding { 16.dp } .. gap(16.dp)

        section("Project Name") {
            column {
                input { setup.projectName }
                fieldNote("Name of the project in setting.gradle.kts.", "Not a valid Gradle project name.\nMay contain only characters: a-z A-Z 0-9 _ . - ") { setup.projectName }
            }
        }

        section("Package Name") {
            column {
                input { setup.packageName }
                fieldNote(
                    "The root package to put source codes into.",
                    "Not a valid Java package name.\nMay contain only characters: a-z A-Z 0-9 .\nHave to start with a letter.\nMay not contain two consecutive dots."
                ) { setup.packageName }
            }
        }

        section("Targets", "Targets to generate code for, server is included by default.\nI suggest starting with a browser-only project.") {
            grid {
                colTemplate(200.dp, 400.dp) .. rowTemplate(32.dp repeat 3)

                text("Browser")
                checkbox { setup.browser }
                text("Android")
                checkbox { setup.android }
                text("iOS")
                checkbox { setup.ios }
            }
        }

        section("Modules", "Library modules to add as dependencies") {
            grid {
                colTemplate(200.dp, 400.dp) .. rowTemplate(32.dp repeat 4)

                text("Ktor")
                checkbox { setup.ktor }
                text("Auth")
                checkbox { setup.auth }
                text("Exposed")
                checkbox { setup.exposed }
                text("Auto")
                checkbox { setup.auto }
            }
        }
    }
}

@Adaptive
private fun section(title: String, explanation: String = "", @Adaptive contentFun: () -> Unit) {
    column {
        border(lightGray, 1.dp) .. padding(16.dp) .. cornerRadius8

        text(title) .. titleSmall
        for (line in explanation.split("\n")) {
            text(line) .. bodySmall .. textColor(darkGray)
        }

        row {
            marginTop { 16.dp }
            contentFun()
        }
    }
}