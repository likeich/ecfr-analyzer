package gov.doge.ecfr.core.screens

import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.DrawableResource

abstract class DogeScreen : Screen {
    abstract val icon: DrawableResource
    abstract val title: String
}