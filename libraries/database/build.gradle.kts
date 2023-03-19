plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.filipe1309.abasteceai.libraries.database"
}

dependencies {
    implementation(Deps.androidx_core)
    testImplementation(Deps.junit)
}
