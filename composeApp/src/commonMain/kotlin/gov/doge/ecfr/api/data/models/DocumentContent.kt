package gov.doge.ecfr.api.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@SerialName("CFRDOC")
data class CfrDocument(
    @SerialName("AMDDATE") val amendmentDate: String,
    @SerialName("TITLE") val title: DocumentTitle
)

@Serializable
@SerialName("TITLEPG")
data class TitlePage(
    @SerialName("TITLENUM") val titleNumber: String,
    @SerialName("SUBJECT") val subject: String,
    @SerialName("REVISED") val revised: String,
    @SerialName("CONTAINS") val contains: String,
    @SerialName("DATE") val date: String
)

@Serializable
@SerialName("BTITLE")
data class BodyTitle(
    @SerialName("OENOTICE") val officialNotice: OfficialNotice
)

@Serializable
@SerialName("OENOTICE")
data class OfficialNotice(
    @SerialName("HD") val headings: List<Heading>,
    @SerialName("P") val paragraphs: List<String>
)

@Serializable
@SerialName("HD")
data class Heading(
    @SerialName("SOURCE") val source: String,
    val text: String
)

@Serializable
@SerialName("TOC")
data class TableOfContents(
    @SerialName("TITLENO") val titleNumbers: List<TitleNumber>
)

@Serializable
@SerialName("TITLENO")
data class TitleNumber(
    @SerialName("HD") val header: Heading,
    @SerialName("CHAPTI") val chapters: List<Chapter>
)

@Serializable
@SerialName("CHAPTI")
data class Chapter(
    @SerialName("SUBJECT") val subject: String,
    @SerialName("PG") val page: Int
)

@Serializable
@SerialName("TITLE")
data class DocumentTitle(
    @SerialName("LRH") val leftHeader: String,
    @SerialName("RRH") val rightHeader: String,
    @SerialName("CFRTITLE") val cfrTitle: CfrTitle
)

@Serializable
@SerialName("CFRTITLE")
data class CfrTitle(
    @SerialName("TITLEHD") val titleHeader: TitleHeader,
    @SerialName("CFRTOC") val cfrTableOfContents: CfrTableOfContents
)

@Serializable
@SerialName("TITLEHD")
data class TitleHeader(
    @SerialName("HD") val title: Heading
)

@Serializable
@SerialName("CFRTOC")
data class CfrTableOfContents(
    @SerialName("SUBTI") val subtitles: List<Subtitle>
)

@Serializable
@SerialName("SUBTI")
data class Subtitle(
    @SerialName("HD") val subtitleHeader: Heading
)