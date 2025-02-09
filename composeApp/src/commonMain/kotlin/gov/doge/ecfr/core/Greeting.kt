package gov.doge.ecfr.core

import gov.doge.ecfr.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}