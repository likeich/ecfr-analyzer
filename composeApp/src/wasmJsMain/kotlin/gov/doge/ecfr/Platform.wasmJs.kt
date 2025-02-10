package gov.doge.ecfr

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val requiresProxy: Boolean = true
}

actual fun getPlatform(): Platform = WasmPlatform()