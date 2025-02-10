package gov.doge.ecfr.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import gov.doge.ecfr.api.data.models.Agency
import gov.doge.ecfr.theme.Dimensions

@Composable
fun AgencyInformation(agency: Agency, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.smallPadding),
            modifier = Modifier.padding(Dimensions.defaultPadding)
        ) {
            Text(
                text = "${agency.displayName} - ${agency.shortName ?: "Information"}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            if (agency.children.isNotEmpty()) {
                Text(
                    text = "Child Agencies: ${agency.children.joinToString("\n") { "${it.displayName} - ${it.shortName}" }}",
                )
            }
            Text(
                text = "CFR References:\n${agency.cfrReferences.joinToString(separator = "\n") { it.toCfrHierarchy().toString() }}"
            )
        }
    }
}