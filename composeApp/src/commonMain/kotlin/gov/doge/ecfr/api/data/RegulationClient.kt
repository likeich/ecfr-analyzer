package gov.doge.ecfr.api.data

import gov.doge.ecfr.api.data.models.AgenciesResponse
import gov.doge.ecfr.api.data.models.Agency
import gov.doge.ecfr.api.data.models.CfrDocument
import gov.doge.ecfr.api.data.models.CfrHierarchy
import gov.doge.ecfr.api.data.models.Correction
import gov.doge.ecfr.api.data.models.CorrectionsResponse
import gov.doge.ecfr.api.data.models.Title
import gov.doge.ecfr.api.data.models.TitlesResponse
import gov.doge.ecfr.utils.CodeTimer
import gov.doge.ecfr.utils.forEachAsync
import gov.doge.ecfr.utils.mapAsync
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.serialization.kotlinx.xml.xml
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
            xml()
        }
    }

    suspend fun getAgencies(): List<Agency>? {
        return try {
            val response: AgenciesResponse = httpClient.get("https://www.ecfr.gov/api/admin/v1/agencies.json").body()
            response.agencies
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getCorrections(date: LocalDate? = null, title: Int? = null, errorCorrectedDate: LocalDate? = null): List<Correction>? {
        return try {
            val response: CorrectionsResponse = httpClient.get("https://www.ecfr.gov/api/admin/v1/corrections.json") {
                parameter("date", date)
                parameter("title", title)
                parameter("error_corrected_date", errorCorrectedDate)
            }.body()
            response.corrections
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getCorrectionsForTitle(title: Int): List<Correction>? {
        return try {
            val response: CorrectionsResponse = httpClient.get("https://www.ecfr.gov/api/admin/v1/corrections/title/$title.json").body()
            response.corrections
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getTitles(): List<Title>? {
        return try {
            val response: TitlesResponse = httpClient.get("https://www.ecfr.gov/api/versioner/v1/titles.json").body()
            response.titles
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

//    suspend fun getTitleStructure(title: Title): Title? {
//        return try {
//            httpClient.get("https://www.ecfr.gov/api/versioner/v1/structure/${title.upToDateAsOf!!}/title-${title.number}.json").body()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }

    suspend fun getTitleContent(title: Title, cfrHierarchy: CfrHierarchy? = null): String? {
        return try {
            httpClient.get("https://www.ecfr.gov/api/versioner/v1/full/${title.upToDateAsOf!!}/title-${title.number}.xml"){
                accept(ContentType.Application.Xml)
                cfrHierarchy?.let {
                    parameter("subtitle", it.subtitle)
                    parameter("chapter", it.chapter)
                    parameter("subchapter", it.subchapter)
                    parameter("part", it.part)
                    parameter("subpart", it.subpart)
                    parameter("section", it.section)
                    parameter("appendix", it.appendix)
                }
            }.body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getCfrReferencesForAgency(agency: Agency, titles: List<Title>): List<String> {
        val agencyContent = agency.cfrReferences.mapNotNull { cfrReference ->
            val cfrHierarchy = cfrReference.toCfrHierarchy()
            val title = titles.find { it.number == cfrHierarchy.title.toInt() }!!
            getTitleContent(title, cfrHierarchy)
        }
        return agencyContent
    }

    fun countWordsUsingRegex(xmlText: String): Int {
        // Remove XML tags and attributes
        val textOnly = xmlText.replace(Regex("<[^>]+>"), " ")

        // Match words using regex (ignoring numbers if needed)
        val wordPattern = Regex("\\b[a-zA-Z]+(?:[-'][a-zA-Z]+)?\\b")

        return wordPattern.findAll(textOnly).count()
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
    //println(correctionsForTitle)
    val titles = client.getTitles()
    //println(titles)
    val titleThreeThree = titles?.find { it.number == 33 }
    //val titleStructure = client.getTitleStructure(titleThreeThree!!)
    //println(titleStructure)
    //println(titleThreeThree)
    val timer = CodeTimer()
    agencies!!.forEach { agency ->
        val cfrReferences = client.getCfrReferencesForAgency(agency, titles!!)
        val words = cfrReferences.sumOf { client.countWordsUsingRegex(it) }
        println("Agency: ${agency.name} - $words words")
    }
    // Agency 1 = 12.7k words
    timer.stop()
}