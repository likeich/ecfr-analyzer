package gov.doge.ecfr.api.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TitleStructure(
    @SerialName("identifier") val id: String = "",
    @SerialName("label") val label: String,
    @SerialName("label_level") val labelLevel: String,
    @SerialName("label_description") val labelDescription: String,
    @SerialName("reserved") val isReserved: Boolean,
    @SerialName("type") val type: String,
    @SerialName("size") val size: Int,
    @SerialName("children") val children: List<TitleStructure> = emptyList(),
    @SerialName("descendant_range") val descendantRange: String? = null,
    @SerialName("received_on") val receivedOn: String? = null
) {
    fun findChild(reference: CfrHierarchy): TitleStructure? {
        // Title is skipped because it is the root
        val hierarchyParts = listOfNotNull(
            reference.subtitle,
            reference.chapter,
            reference.subchapter,
            reference.part,
            reference.subpart,
            reference.subjectGroup,
            reference.section,
            reference.appendix
        )

        var current: TitleStructure? = this

        for (part in hierarchyParts) {
            current = current?.children?.firstOrNull { it.id == part }
            if (current == null) {
                return null
            }
        }
        return current
    }

    // Size is the character count, estimating word count (regulations use long words)
    val wordCount get() = size / 6
}
