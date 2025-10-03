plugins {
    idea
    `maven-publish`
    signing
}

loadSecretProperties(project)
BuildTools.init(project)
BuildProperties.init(project)

// apply common configuration for this project and each sub project
allprojects {
    group = "de.comahe.i18n4k"
    version = "0.11.1"

    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }
}

// apply common configuration for each sub project
subprojects {
    // add signing and Maven publish to each project
    apply<MavenPublishPlugin>()

    val isSnapshot = version.toString().contains("SNAPSHOT");

    if (secretPropertiesLoaded
        && ((isSnapshot && BuildProperties.publishSnapshots) || (!isSnapshot && BuildProperties.publishReleases))
    ) {
        apply<SigningPlugin>()

        // configure common things for signing and Maven publish for each project
        afterEvaluate {
            // need another `afterEvaluate` here, as some publications are added in an `afterEvaluate`
            afterEvaluate {

                // register the task "publish" of this subproject at the parent project task "publishToMavenCentral" .
                parent!!.tasks.getByName("publishToMavenCentral").dependsOn(tasks.getByName("publish"))

                // sign artifacts, see buildSrc/readme.md
                signing {
                    publishing.publications.forEach { publication ->
                        sign(publication)
                    }
                }
                // read properties during configuration phase,
                // avoid use of `project` in task execution.
                val buildDirectoryUri = parent!!.layout.buildDirectory.dir("staging-deploy").get().asFile.toURI()
                val projectName = project.name

                // publishing artifacts, see buildSrc/readme.md
                publishing {
                    // use local staging repo for later upload to Maven Central.
                    // See [BuildTools.uploadStagingRepositoryToMavenCentral]
                    repositories {
                        maven {
                            url = buildDirectoryUri
                        }
                    }
                    // Update POM of all MavenPublication
                    publications {
                        publications.forEach { publication ->
                            if (publication !is MavenPublication)
                                return@forEach
                            publication.pom {
                                name.set(projectName)
                                description.set("i18n4k is a multiplatform (JVM, JS, native) library and code generator for Kotlin to handle internationalisation (i18n) in your program.")
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


                // Fix Gradle error about signing tasks, using publishing task outputs without explicit dependencies
                // https://github.com/gradle/gradle/issues/26091
                tasks.withType<AbstractPublishToMaven>().configureEach {
                    val signingTasks = tasks.withType<Sign>()
                    mustRunAfter(signingTasks)
                }
            }
        }
    }
}

tasks.register("publishToMavenCentral") {
    group = "publishing"
    dependsOn(tasks.getByName("publish"))
    doLast {
        if (!secretPropertiesLoaded)
            throw GradleException("Secret properties not loaded!")
        BuildTools.uploadStagingRepositoryToMavenCentral()
    }
}