package example.compose

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform