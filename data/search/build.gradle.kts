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
    implementation(project(":domain:search"))
    implementation(project(":network"))
    implementation(project(":core:util"))
    implementation(project(":network:search"))
    implementation(project(":core:network"))

    ksp(libs.dagger.compiler)
    implementation(libs.dagger)

    implementation(libs.kotlinx.coroutines.android)
}