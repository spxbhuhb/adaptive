tasks.register("clean") {
    group = "build"
    dependsOn(gradle.includedBuild("z2-site").task(":clean"))
}

tasks.register("build") {
    group = "build"
    dependsOn(gradle.includedBuild("z2-site").task(":build"))
}