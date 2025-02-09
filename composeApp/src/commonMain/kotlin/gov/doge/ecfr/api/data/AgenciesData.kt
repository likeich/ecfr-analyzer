package gov.doge.ecfr.api.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AgenciesData(
    val agencies: List<Agency>
)

@Serializable
data class Agency(
    val name: String,
    @SerialName("short_name")
    val shortName: String? = null,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("sortable_name")
    val sortableName: String,
    val slug: String,
    val children: List<Agency> = emptyList(),
    @SerialName("cfr_references")
    val cfrReferences: List<AgencyCfrReference> = emptyList()
)

@Serializable
data class AgencyCfrReference(
    val title: Int,
    val subtitle: String? = null,
    val chapter: String? = null,
    val part: String? = null,
    val subchapter: String? = null,
)