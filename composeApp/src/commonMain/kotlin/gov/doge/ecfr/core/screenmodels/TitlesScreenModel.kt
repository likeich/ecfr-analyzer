package gov.doge.ecfr.core.screenmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import gov.doge.ecfr.api.data.models.Correction
import gov.doge.ecfr.api.data.models.Title
import gov.doge.ecfr.core.AppState

class TitlesScreenModel : ScreenModel {
    var sortBy: SortBy by mutableStateOf(SortBy.NAME)
    var filterBy: FilterBy by mutableStateOf(FilterBy.TOP_5)
    var selectedTitle: Title? by mutableStateOf(null)

    fun sortTitles(allTitles: List<Title>, appState: AppState): List<Title> {
        return when (sortBy) {
            SortBy.NAME -> allTitles.sortedBy { it.number }
            SortBy.CORRECTION_COUNT -> allTitles.sortedByDescending {
                appState.titleCorrections[it]?.size ?: 0
            }
            SortBy.DATE -> allTitles.sortedByDescending { it.latestIssueDate }
            else -> allTitles.sortedBy { it.number }
        }
    }

    fun getFilteredTitles(allTitles: List<Title>, appState: AppState): List<Title> {
        val sortedTitles = sortTitles(allTitles, appState)

        val filtered = when (filterBy) {
            FilterBy.TOP_5 -> sortedTitles.take(5)
            FilterBy.TOP_10 -> sortedTitles.take(10)
            FilterBy.TOP_25 -> sortedTitles.take(25)
            FilterBy.ALL -> sortedTitles
        } + listOfNotNull(selectedTitle)

        return filtered.distinct()
    }

    fun getFilteredCorrections(appState: AppState): List<Correction> {
        return getFilteredTitles(appState.titles, appState).map {
            appState.titleCorrections[it] ?: emptyList()
        }.flatten()
    }

    fun onTitleSelected(title: Title?) {
        selectedTitle = title
    }
}