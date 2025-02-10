package gov.doge.ecfr.api.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val results: List<SearchResult>,
    val meta: SearchMeta
)

@Serializable
data class SearchResult(
    @SerialName("starts_on") val startsOn: String,
    @SerialName("ends_on") val endsOn: String?,
    val type: String,
    val hierarchy: CfrHierarchy,
    @SerialName("hierarchy_headings") val hierarchyHeadings: HierarchyHeadings,
    val headings: Headings,
    @SerialName("full_text_excerpt") val fullTextExcerpt: String? = null,
    val score: Double,
    @SerialName("structure_index") val structureIndex: Int,
    val reserved: Boolean,
    val removed: Boolean,
    @SerialName("change_types") val changeTypes: List<String>
)

@Serializable
data class HierarchyHeadings(
    val title: String,
    val subtitle: String?,
    val chapter: String?,
    val subchapter: String?,
    val part: String?,
    val subpart: String?,
    @SerialName("subject_group") val subjectGroup: String?,
    val section: String?,
    val appendix: String?
)

@Serializable
data class Headings(
    val title: String,
    val subtitle: String?,
    val chapter: String?,
    val subchapter: String?,
    val part: String?,
    val subpart: String?,
    @SerialName("subject_group") val subjectGroup: String?,
    val section: String?,
    val appendix: String?
)

@Serializable
data class SearchMeta(
    @SerialName("current_page") val currentPage: Int,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_count") val totalCount: Int,
    @SerialName("max_score") val maxScore: Double,
    val description: String
)
