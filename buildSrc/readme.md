# Configurations

## Dependencies

All project dependencies are managed in this class
`buildSrc/src/main/kotlin/Dependencies.kt`

## Signing

File
`buildSrc/src/main/resources/secret.properties`
must contain the following properties

```properties
signing.keyId=A4FF2279
signing.password=MyPerfectPassword
signing.secretKeyRingFile=PathToKeyRingFile.gpg
```

* `signing.keyId`: is a substring (last 8 characters) of the fingerprint (Short key ID), e.g.
   ```
  Fingerprint: 0D69 E11F 12BD BA07 7B37  26AB 4E1F 799A A4FF 2279
  Long key ID:                                4E1F 799A A4FF 2279
  Short key ID:                                         A4FF 2279
  ```

* `signing.password`: Passwort of the secret key

* `signing.secretKeyRingFile`: The key ring file with the secret key.
    * with "Kleopatra" go to "Backup secret keys..." and in the file chooser select the "gpg" file
      format.
    * In CLI type `gpg --export-secret-keys <KEY-ID>`

## Deploy

File
`buildSrc/src/main/resources/secret.properties`
must contain the following properties

```properties
gradle.publish.key=<Gradle Plugin Portal Key>
gradle.publish.secret=<Gradle Plugin Portal Secret>
sonatype.username=<Sonatype Username>
sonatype.password=<Sonatype Password>
```

* `gradle.publish.key` and `gradle.publish.secret`: Your Gradle Plugin Portal publication key+secret
* `sonatype.username` and `sonatype.password`: credentials for your Maven Central
  account (https://central.sonatype.org/pages/ossrh-guide.html)
  

