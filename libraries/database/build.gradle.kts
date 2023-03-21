plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}

android {
    namespace = "com.filipe1309.abasteceai.libraries.database"
    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
        }
    }
}

dependencies {
    implementation(Deps.androidx_core)

    // Room
    api(Deps.androidx_room_runtime)
    ksp(Deps.androidx_room_compiler)
    implementation(Deps.androidx_room_ktx)

    // Tests
    testImplementation(Deps.junit)
}
