package calculate.mortgage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Header(txtColor: Color){
    Row(modifier = Modifier.padding(top = 10.dp, bottom = 20.dp), horizontalArrangement = Arrangement.Center){
        Text (text = title, fontWeight = FontWeight.Bold, fontSize = 30.sp, color = txtColor)
    }
}