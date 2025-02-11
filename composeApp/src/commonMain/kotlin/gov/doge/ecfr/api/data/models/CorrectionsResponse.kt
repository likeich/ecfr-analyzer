package gov.doge.ecfr.api.data.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CorrectionsResponse(
    @SerialName("ecfr_corrections")
    val corrections: List<Correction>
)

@Serializable
data class Correction(
    val id: Int,
    @SerialName("cfr_references")
    val cfrReferences: List<CorrectionCfrReference>,
    @SerialName("corrective_action")
    val correctiveAction: String,
    @SerialName("error_corrected")
    val errorCorrected: String,
    @SerialName("error_occurred")
    val errorOccurred: String? = null,
    @SerialName("fr_citation")
    val frCitation: String,
    val position: Int? = null,
    @SerialName("display_in_toc")
    val displayInToc: Boolean,
    val title: Int,
    val year: Int,
    @SerialName("last_modified")
    val lastModified: String
) {
    val date = LocalDate.parse(errorCorrected)
}

@Serializable
data class CorrectionCfrReference(
    @SerialName("cfr_reference")
    val cfrReference: String,
    val hierarchy: CfrHierarchy
)

@Serializable
data class CfrHierarchy(
    val title: String,
    val subtitle: String? = null,
    val chapter: String? = null,
    val subchapter: String? = null,
    val part: String? = null,
    val subpart: String? = null,
    @SerialName("subject_group")
    val subjectGroup: String? = null,
    val section: String? = null,
    val appendix: String? = null
) {
    fun toReadableString(): String {
        return listOfNotNull(
            title.let { "Title: $it" },
            subtitle?.let { "Subtitle: $it" },
            chapter?.let { "Chapter: $it" },
            subchapter?.let { "Subchapter: $it" },
            part?.let { "Part: $it" },
            subpart?.let { "Subpart: $it" },
            section?.let { "Section: $it" },
            appendix?.let { "Appendix: $it" }
        ).joinToString(" | ")
    }

    // Example: https://www.ecfr.gov/current/title-7/subtitle-B/chapter-I/subchapter-A/part-28/subpart-E/subject-group-ECFR6f664639cfb67fd/section-28.952
    fun toUrl(): String {
        val baseUrl = "https://www.ecfr.gov/current/title-$title"
        val list = listOfNotNull(
            subtitle?.let { "subtitle-$it" },
            chapter?.let { "chapter-$it" },
            subchapter?.let { "subchapter-$it" },
            part?.let { "part-$it" },
            subpart?.let { "subpart-$it" },
            subjectGroup?.let { "subject-group-$it" },
            section?.let { "section-$it" },
            appendix?.let { "appendix-$it" }
        )

        return list.joinToString("/", prefix = "$baseUrl/")
    }
}
