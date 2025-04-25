plugins {
    java
    checkstyle
    jacoco

    alias(libs.plugins.axion)
    alias(libs.plugins.lombok)
}

group = "tech.ixirsii"
version = scmVersion.version

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.slf4j.api)

    testImplementation(libs.logback.classic)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.bundles.junit)
    testRuntimeOnly(libs.junit.platform.launcher)
}

checkstyle {
    toolVersion = "10.23.0"
}

jacoco {
    toolVersion = "0.8.13"
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

tasks.checkstyleTest {
    configFile = file("${rootDir}/config/checkstyle/checkstyle-test.xml")
}

tasks.withType<Checkstyle>().configureEach {
    reports {
        html.required = true
        xml.required = false
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        val coverageExclusions = listOf(
            "tech.ixirsii.rocket.container.RocketContainerApplication"
        )

        rule {
            excludes = coverageExclusions
            limit {
                counter = "CLASS"
                element = "CLASS"
                minimum = 1.0.toBigDecimal()
            }
        }
        rule {
            excludes = coverageExclusions
            limit {
                counter = "METHOD"
                element = "CLASS"
                minimum = 1.0.toBigDecimal()
            }
        }
        rule {
            excludes = coverageExclusions
            limit {
                counter = "LINE"
                element = "CLASS"
                minimum = 0.70.toBigDecimal()
            }
        }
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        csv.required = false
        html.required = true
        xml.required = true
    }
}

tasks.test {
    useJUnitPlatform()
}
