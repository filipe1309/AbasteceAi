import org.gradle.api.JavaVersion

@Suppress("unused")
object Config {
    const val min_sdk = 26
    const val target_sdk = 33
    const val compile_sdk = 33
    @JvmField val java_version = JavaVersion.VERSION_11
    const val jvm_target = "1.8"
    const val android_gradle_plugin = "3.0.1"
}

@Suppress("unused")
object Versions {
    const val kotlin = "1.8.20-Beta"
    const val android_app_lib = "7.4.1"
    const val version_code = 1
    const val version_name = "1.0"
    const val lifecycle = "2.5.1"
    const val androidx_core = "1.9.0"
    const val androidx_support_lib = "1.6.1"
    const val android_material_design = "1.8.0"
    const val androidx_const_lyt = "2.1.4"
    const val coroutines = "1.6.4"
    const val androidx_nav = "2.5.3"
    const val junit = "4.13.2"
    const val androidx_test_ext_junit = "1.1.5"
    const val androidx_espresso_core = "3.5.1"
    const val androidx_room = "2.5.0"
    const val gson = "2.8.9"
    const val play_services_location = "21.0.1"
    const val koin_version = "3.4.0"
}

@Suppress("unused")
object Deps {
    // Kotlin
    const val androidx_core = "androidx.core:core-ktx:${Versions.androidx_core}"

    const val androidx_support_lib = "androidx.appcompat:appcompat:${Versions.androidx_support_lib}"
    const val android_material_design = "com.google.android.material:material:${Versions.android_material_design}"
    const val androidx_constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.androidx_const_lyt}"

    // Play Services
    const val play_services_location = "com.google.android.gms:play-services-location:${Versions.play_services_location}"

    // ViewModel
    const val androidx_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"

    // LiveData
    const val androidx_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    // Coroutines
    const val kotlinx_coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val kotlinx_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    // Navigation
    const val androidx_nav_fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.androidx_nav}"
    const val androidx_nav_ui = "androidx.navigation:navigation-ui-ktx:${Versions.androidx_nav}"

    // Room
    const val androidx_room_runtime = "androidx.room:room-runtime:${Versions.androidx_room}"
    const val androidx_room_ktx = "androidx.room:room-ktx:${Versions.androidx_room}"
    const val androidx_room_compiler = "androidx.room:room-compiler:${Versions.androidx_room}"

    // Gson
    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    // Koin
    const val koin_core = "io.insert-koin:koin-core:${Versions.koin_version}"
    const val koin_android = "io.insert-koin:koin-android:${Versions.koin_version}"

    // Tests
    const val test_junit = "junit:junit:${Versions.junit}"
    const val test_androidx_test_ext_junit = "androidx.test.ext:junit:${Versions.androidx_test_ext_junit}"
    const val test_androidx_espresso_core = "androidx.test.espresso:espresso-core:${Versions.androidx_espresso_core}"
}


