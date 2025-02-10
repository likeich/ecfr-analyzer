package gov.doge.ecfr

interface Platform {
    val name: String
    val requiresProxy: Boolean
}

expect fun getPlatform(): Platform