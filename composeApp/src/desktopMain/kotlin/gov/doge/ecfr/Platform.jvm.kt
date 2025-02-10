package gov.doge.ecfr

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val requiresProxy: Boolean = false
}

actual fun getPlatform(): Platform = JVMPlatform()