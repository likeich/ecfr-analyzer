package gov.doge.ecfr.utils

import kotlinx.datetime.Clock

class CodeTimer {
    private var startTime: Long = Clock.System.now().toEpochMilliseconds()

    fun stop() {
        val endTime = Clock.System.now().toEpochMilliseconds()
        println("Elapsed time: ${endTime - startTime}ms")

        startTime = 0
    }

    fun printElapsedTime() {
        val endTime = Clock.System.now().toEpochMilliseconds()
        println("Elapsed time: ${endTime - startTime}ms")
    }
}