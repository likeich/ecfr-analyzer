package gov.doge.ecfr.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.staticCompositionLocalOf
import gov.doge.ecfr.api.data.RegulationClient
import gov.doge.ecfr.api.data.models.Agency
import gov.doge.ecfr.api.data.models.Correction
import gov.doge.ecfr.api.data.models.Title
import gov.doge.ecfr.utils.mapAsync

class AppState {
    private val client = RegulationClient()
    val titles: SnapshotStateList<Title> = mutableStateListOf()
    val agencies: SnapshotStateList<Agency> = mutableStateListOf()
    val corrections: SnapshotStateList<Correction> = mutableStateListOf()
    val titleCorrections: SnapshotStateMap<Title, List<Correction>> = mutableStateMapOf()
    var averageWordCount: Int by mutableStateOf(0)
    var state: State by mutableStateOf(State.Loading(""))

    suspend fun load() {
        try {
            state = State.Loading("Loading titles...")
            titles.clear()
            titles.addAll(client.getTitles() ?: emptyList())

            state = State.Loading("Loading agencies...")
            client.getAgencies()?.let { agencyList ->
                agencies.clear()
                agencies.addAll(agencyList.sortedBy { it.sortableName })
            }

            state = State.Loading("Loading corrections...")
            corrections.clear()
            corrections.addAll(client.getCorrections() ?: emptyList())
            titleCorrections.clear()
            titleCorrections.putAll(titles.associateWith { title ->
                corrections.filter { it.title == title.number }
            })

            state = State.Loading("Loading word counts...")
            var agenciesLoaded = 0
            agencies.chunked(10).forEach {
                it.mapAsync { agency ->
                    agency.wordCount = client.getWordCountForAgency(agency, titles)
                }.await()

                agenciesLoaded += it.size
                state = State.Loading("Loading word counts... $agenciesLoaded/${agencies.size}")
                averageWordCount = agencies.filter { it.wordCount > 0 }.map { it.wordCount }.average().toInt()
            }
        } catch (e: Exception) {
            state = State.Error(e.message ?: "An error occurred")
        } finally {
            state = State.Loaded("Data loaded")
        }
    }
}

val LocalAppState = staticCompositionLocalOf<AppState> {
    error("No AppState provided")
}

sealed class State(open val message: String) {
    data class Loading(override val message: String) : State(message)
    data class Loaded(override val message: String) : State(message)
    data class Error(override val message: String) : State(message)
}
