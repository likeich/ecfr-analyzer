package gov.doge.ecfr.core.screenmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import gov.doge.ecfr.api.data.models.Agency

class HomeScreenModel : ScreenModel {
    var sortBy: SortBy by mutableStateOf(SortBy.WORD_COUNT)
    var filterBy: FilterBy by mutableStateOf(FilterBy.TOP_5)
    var selectedAgency: Agency? by mutableStateOf(null)

    fun sortAgencies(allAgencies: List<Agency>): List<Agency> {
        return when (sortBy) {
            SortBy.NAME -> allAgencies.sortedBy { it.sortableName }
            SortBy.WORD_COUNT -> allAgencies.sortedByDescending { it.wordCount }
        }
    }

    fun getFilteredAgencies(allAgencies: List<Agency>): List<Agency> {
        val sortedAgencies = sortAgencies(allAgencies)

        return when (filterBy) {
            FilterBy.TOP_5 -> sortedAgencies.take(5)
            FilterBy.TOP_10 -> sortedAgencies.take(10)
            FilterBy.TOP_25 -> sortedAgencies.take(25)
            FilterBy.ALL -> sortedAgencies
        }
    }

    fun onAgencySelected(agency: Agency?) {
        selectedAgency = agency
    }
}

enum class SortBy {
    NAME,
    WORD_COUNT
}

enum class FilterBy {
    TOP_5,
    TOP_10,
    TOP_25,
    ALL,
}