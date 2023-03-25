plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.filipe1309.abasteceai.domain.histories"
}

dependencies {
    implementation(project(":domain:fuels"))

    implementation(Deps.androidx_core)

    // Tests
    testImplementation(Deps.test_junit)
}
