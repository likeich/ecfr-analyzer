package gov.doge.ecfr.core.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import gov.doge.ecfr.utils.readableName

@Composable
fun <T : Enum<T>> EnumDropdownButton(
    label: String,
    options: Array<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { isExpanded = true }) {
            Text("$label: ${selectedOption.readableName()}")
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.readableName()) },
                    onClick = {
                        onOptionSelected(option)
                        isExpanded = false
                    }
                )
            }
        }
    }
}
