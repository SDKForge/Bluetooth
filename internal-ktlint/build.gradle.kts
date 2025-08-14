plugins {
    id("kotlin")
}

group = "dev.sdkforge.ktlint"

// used ktlint project setup
//noinspection UseTomlInstead
dependencies {
    implementation("com.pinterest.ktlint:ktlint-cli-ruleset-core:1.7.1")
    implementation("com.pinterest.ktlint:ktlint-rule-engine-core:1.7.1")

    testImplementation(kotlin("test"))

    testImplementation("com.pinterest.ktlint:ktlint-test:1.7.1")

    testRuntimeOnly("org.slf4j:slf4j-simple:2.0.17")
}
