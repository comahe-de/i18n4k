plugins {
    `maven-publish`
    id("org.jetbrains.dokka")
}



// from https://stackoverflow.com/a/66352905/2611134
tasks {
    register<Jar>("javadocJar") {
        // we cannot use `dokkaJavadoc`, as it does not support multiplatform projects
        // https://slack-chats.kotlinlang.org/t/484606/is-there-a-workaround-for-getting-the-dokka-javadoc-plugin-t
        dependsOn(tasks.dokkaGenerateHtml)
        from(dokka.dokkaPublications.html.get().outputDirectory.get())

        archiveClassifier.set("javadoc")
    }
}

// Publishing....
afterEvaluate {
    publishing {
        publications {
            /**
             * When used with maven-publish, the Kotlin plugin automatically creates publications
             * for each target that can be built on the current host, except for the Android target,
             * which needs an additional step to configure publishing.
             *
             * Therefore no additional publication declaractions are needed here!
             *
             * https://kotlinlang.org/docs/mpp-publish-lib.html#structure-of-publications
             */
            // avoid duplicate publication for multi platform builds
            BuildTools.avoidDuplicateMultiPlatformPublications(project, this)

            // add JavaDoc
            all {
                val mavenPublication = this as? MavenPublication
                mavenPublication?.artifact(tasks["javadocJar"])
            }
        }
    }
}
