package gov.doge.ecfr.api.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json

class RegulationClient {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                }
            )
        }
    }

    suspend fun getAgencies(): AgenciesData? {
        return try {
            httpClient.get("https://www.ecfr.gov/api/admin/v1/agencies.json").body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getCorrections(date: LocalDate? = null, title: Int? = null, errorCorrectedDate: LocalDate? = null): Corrections? {
        return try {
            httpClient.get("https://www.ecfr.gov/api/admin/v1/corrections.json") {
                parameter("date", date)
                parameter("title", title)
                parameter("error_corrected_date", errorCorrectedDate)
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getCorrectionsForTitle(title: Int): Corrections? {
        return try {
            httpClient.get("https://www.ecfr.gov/api/admin/v1/corrections/title/$title.json").body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

suspend fun main() {
    val client = RegulationClient()
    val agencies = client.getAgencies()
    //println(agencies?.agencies?.map { it.name })
    val corrections = client.getCorrections()
    //println(corrections)
    //corrections?.corrections?.filter { correction -> correction.position.any { !it.isDigit() } }?.let { println(it) }
    val correctionsForTitle = client.getCorrectionsForTitle(16)
    println(correctionsForTitle)
}