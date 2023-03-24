plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

android {
    namespace = "com.filipe1309.abasteceai.data.histories"
}

dependencies {
    implementation(project(":domain:histories"))
    implementation(project(":domain:fuels"))
    implementation(project(":libraries:database"))

    implementation(Deps.androidx_core)

    // Tests
    testImplementation(Deps.junit)
}
