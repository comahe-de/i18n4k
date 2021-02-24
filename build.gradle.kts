plugins {
    idea
    kotlin("jvm") version Versions.kotlin
    `maven-publish`
    signing
}

allprojects {
    group = "de.comahe.i18n4k"
    version = "0.1.0-SNAPSHOT"

    repositories{
        mavenLocal()
        mavenCentral()
        jcenter()
        maven { setUrl("https://jitpack.io") }
    }
}

subprojects {
    // add signing and Maven publish to each project
    loadSecretProperties(this)
    apply<MavenPublishPlugin>()
    apply<SigningPlugin>()

    // configure common things for signing and Maven publish for each project
    afterEvaluate {
        // sign artifacts, see buildSrc/readme.md
        signing {
            publishing.publications.forEach { publication ->
                sign(publication)
            }
        }
        // publishing artifacts, see buildSrc/readme.md
        publishing {
            if (project.name == "i18n4k-gradle-plugin")
                return@publishing
            // Set Maven-Central repository
            repositories {
                maven {
                    url = uri(
                        if (version.toString().contains("SNAPSHOT"))
                            "https://oss.sonatype.org/content/repositories/snapshots/"
                        else
                            "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                    )
                    credentials {
                        username = project.property("sonatype.username").toString()
                        password = project.property("sonatype.password").toString()
                    }
                }
            }
            // Update POM of all MavenPublication
            publications {
                publications.forEach { publication ->
                    if (publication !is MavenPublication)
                        return@forEach
                    publication.pom {
                        name.set(project.name)
                        description.set("i18n4k is a multiplatform (JVM, JS, native) library and code generator for Kotlin to handle internationalisation (i18n) in your programm.")
                        url.set("https://comahe-de.github.io/i18n4k/")
                        licenses {
                            license {
                                name.set("The Apache License, Version 2.0")
                                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            }
                        }
                        developers {
                            developer {
                                id.set("comahe.de")
                                name.set("Marcel Heckel")
                                email.set("info@comahede")
                            }
                        }
                        scm {
                            connection.set("scm:git@github.com:comahe-de/i18n4k.git")
                            developerConnection.set("scm:git@github.com:comahe-de/i18n4k.git")
                            url.set("https://github.com/comahe-de/i18n4k")
                        }
                    }
                }
            }
        }
    }
}