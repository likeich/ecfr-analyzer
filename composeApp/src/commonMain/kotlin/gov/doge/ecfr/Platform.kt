package gov.doge.ecfr

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform