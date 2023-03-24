plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.filipe1309.abasteceai.domain.comparator"
}

dependencies {
    implementation(Deps.androidx_core)

    // Tests
    testImplementation(Deps.junit)
}
