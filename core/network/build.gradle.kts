plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("com.google.devtools.ksp")
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(project(":core:config"))
    ksp(libs.dagger.compiler)
    implementation(libs.dagger)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
}
