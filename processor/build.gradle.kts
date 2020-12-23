plugins {
    kotlin("kapt")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.auto.service:auto-service:1.0-rc7")
    implementation(project(":library"))

    kapt("com.google.auto.service:auto-service:1.0-rc7")
}
