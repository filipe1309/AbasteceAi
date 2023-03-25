plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.filipe1309.abasteceai.ui.comparator"
}

dependencies {
    implementation(project(":domain:comparator"))
    implementation(project(":domain:fuels"))
    implementation(project(":domain:histories"))
    implementation(project(":libraries:ui-components"))
    implementation(project(":libraries:actions"))

    // TODO: Remove this dependency after implementing DI
    implementation(project(":data:fuels"))
    implementation(project(":data:histories"))
    implementation(project(":libraries:database"))

    implementation(Deps.androidx_core)
    implementation(Deps.androidx_support_lib)
    implementation(Deps.android_material_design)
    implementation(Deps.androidx_constraint_layout)

    // Play Services
    implementation(Deps.play_services_location)

    // ViewModel
    implementation(Deps.androidx_viewmodel)

    // LiveData
    implementation(Deps.androidx_livedata)

    // Coroutines
    implementation(Deps.kotlinx_coroutines_core)
    implementation(Deps.kotlinx_coroutines_android)

    // Navigation
    implementation(Deps.androidx_nav_fragment)
    implementation(Deps.androidx_nav_ui)

    // Tests
    testImplementation(Deps.test_junit)
    androidTestImplementation(Deps.test_androidx_test_ext_junit)
    androidTestImplementation(Deps.test_androidx_espresso_core)
}
