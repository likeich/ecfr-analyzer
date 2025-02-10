package gov.doge.ecfr.api.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TitlesResponse(
    val titles: List<Title> = emptyList(),
    val meta: Meta? = null
)

@Serializable
data class Title(
    val number: Int,
    val name: String,
    @SerialName("latest_amended_on")
    val latestAmendedOn: String? = null,
    @SerialName("latest_issue_date")
    val latestIssueDate: String? = null,
    @SerialName("up_to_date_as_of")
    val upToDateAsOf: String? = null,
    val reserved: Boolean
)

@Serializable
data class Meta(
    val date: String,
    @SerialName("import_in_progress")
    val importInProgress: Boolean
)
