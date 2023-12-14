plugins {
    kotlin("jvm") version "1.9.20"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}

dependencies {
    implementation(platform("io.arrow-kt:arrow-stack:1.2.0"))
    // no versions on libraries
    implementation("io.arrow-kt:arrow-core")
    implementation("io.arrow-kt:arrow-fx-coroutines")

}
