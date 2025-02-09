package gov.doge.ecfr.core.screenmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import gov.doge.ecfr.api.data.RegulationClient
import gov.doge.ecfr.api.data.models.Title
import kotlinx.coroutines.launch

class TitleScreenModel : ScreenModel {
    val client = RegulationClient()
    val titles: SnapshotStateList<Title> = mutableStateListOf()

    fun load() {
        screenModelScope.launch {
            titles.clear()
            titles.addAll(client.getTitles() ?: emptyList())
        }
    }
}