tasks.register("clean") {
    group = "build"
    dependsOn(gradle.includedBuild("adaptive-site").task(":clean"))
}

tasks.register("build") {
    group = "build"
    dependsOn(gradle.includedBuild("adaptive-site").task(":build"))
}