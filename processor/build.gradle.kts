plugins {
    kotlin("kapt")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.auto.service:auto-service:1.0-rc7")
    implementation("com.squareup:kotlinpoet:1.7.2")
    implementation(project(":library"))

    kapt("com.google.auto.service:auto-service:1.0-rc7")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}