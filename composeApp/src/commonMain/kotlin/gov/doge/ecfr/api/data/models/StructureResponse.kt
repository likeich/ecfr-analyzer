package gov.doge.ecfr.api.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TitleStructure(
    @SerialName("identifier") val id: String = "",
    @SerialName("label") val label: String? = null,
    @SerialName("label_level") val labelLevel: String? = null,
    @SerialName("label_description") val labelDescription: String? = null,
    @SerialName("reserved") val isReserved: Boolean = false,
    @SerialName("type") val type: String? = null,
    @SerialName("size") val size: Int = 0,
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
            val foundChild = current?.children?.firstOrNull { it.id == part }
            if (foundChild == null) { // Some parts are skipped (ex: Title 2, Chapter IV skips subtitle)
                val grandchildren = current?.children?.map { it.children }?.flatten()
                current = grandchildren?.firstOrNull { it.id == part }
            } else {
                current = foundChild
            }
        }
        return current
    }

    // Size is the character count, estimating word count (regulations use long words)
    val wordCount get() = size / 6
}
