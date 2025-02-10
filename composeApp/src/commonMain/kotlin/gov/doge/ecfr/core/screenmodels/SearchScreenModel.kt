package gov.doge.ecfr.core.screenmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import gov.doge.ecfr.api.data.RegulationClient
import gov.doge.ecfr.api.data.models.SearchResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchScreenModel : ScreenModel {
    var searchQuery: String by mutableStateOf("")
    val searchResults: SnapshotStateList<SearchResult> = mutableStateListOf()

    fun search(client: RegulationClient) {
        CoroutineScope(Dispatchers.Default).launch {
            searchResults.clear()
            searchResults.addAll(client.search(searchQuery))
        }
    }
}