package cather.lfree.workdscather

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavigationItem(var route: String, val icon: ImageVector?, var title: Int) {
    data object Home : NavigationItem("Home", Icons.Rounded.List, R.string.data_main_d)
    data object Vector : NavigationItem("Vector", Icons.Rounded.DateRange, R.string.vector_main_d)
    data object Settings : NavigationItem("Settings", Icons.Rounded.Settings, R.string.sett_main_d)

    //  Icons.Rounded.List,
}