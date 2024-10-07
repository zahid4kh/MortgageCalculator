package calculate.mortgage

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun backgroundColor(): Color{
    val bkg = if (isSystemInDarkTheme()){
        Color.Black
    }else{
        Color.White
    }
    return bkg
}


