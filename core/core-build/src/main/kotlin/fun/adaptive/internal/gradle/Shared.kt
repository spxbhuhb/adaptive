package `fun`.adaptive.internal.gradle

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.Sign
import org.gradle.plugins.signing.SigningExtension
import kotlin.toString

fun Project.setupPublishing() {

    extensions.getByType<SigningExtension>().run {
        useGpgCmd()
        sign(extensions.getByType<PublishingExtension>().publications)
    }

    extensions.getByType<MavenPublishBaseExtension>().run {
        pom {
            defaultPom(project)
        }

        publishToMavenCentral()

        signAllPublications()

        coordinates("fun.adaptive", project.name, version.toString())
    }

//    extensions.getByType<PublishingExtension>().run {
//        publications.withType<MavenPublication>().all {
//            pom {
//                defaultPom(project)
//            }
//        }
//    }

    // Workaround https://github.com/gradle/gradle/issues/26091
    tasks.withType<AbstractPublishToMaven>().configureEach {
        val signingTasks = tasks.withType(Sign::class.java)
        mustRunAfter(signingTasks)
    }
}

fun MavenPom.defaultPom(
    project: Project
) {

    name.set(project.name)
    description.set(project.description ?: name.get())
    url.set("https://adaptive.fun")
    inceptionYear.set("2024")

    licenses {
        license {
            name.set("Apache 2.0")
            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            distribution.set("repo")
        }
    }
    developers {
        developer {
            id.set("toth-istvan-zoltan")
            name.set("Tóth István Zoltán")
            url.set("https://github.com/toth-istvan-zoltan")
            organization.set("Simplexion Kft.")
            organizationUrl.set("https://www.simplexion.hu")
        }
    }
    scm {
        url.set("https://github.com/spxbhuhb/adaptive")
        connection.set("scm:git:git://github.com/spxbhuhb/adaptive.git")
        developerConnection.set("scm:git:ssh://git@github.com/spxbhuhb/adaptive.git")
    }
}