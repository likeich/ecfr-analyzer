package gov.doge.ecfr.core

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.staticCompositionLocalOf
import gov.doge.ecfr.api.data.RegulationClient
import gov.doge.ecfr.api.data.models.Agency
import gov.doge.ecfr.api.data.models.Title

class AppState {
    private val client = RegulationClient()
    val titles: SnapshotStateList<Title> = mutableStateListOf()
    val agencies: SnapshotStateList<Agency> = mutableStateListOf()

    suspend fun load() {
        titles.clear()
        titles.addAll(client.getTitles() ?: emptyList())
        agencies.clear()
        agencies.addAll(client.getAgencies() ?: emptyList())
    }
}

val LocalAppState = staticCompositionLocalOf<AppState> {
    error("No AppState provided")
}