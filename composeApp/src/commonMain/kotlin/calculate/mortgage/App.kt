package calculate.mortgage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun App() {
    MaterialTheme {
        Calculator()
    }
}

@Composable
fun Calculator(){
    var amount by remember { mutableStateOf("") }
    var term by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    val smallTextField = 170.dp
    val bigTextField = smallTextField * 2
    var isRepayment by remember { mutableStateOf(false) }
    var isInterestOnly by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center)
    {
        Header()

        OutlinedTextField(value = amount, onValueChange = {amount = it}, label = {Text (mortgageAmount)}, modifier = Modifier.width(bigTextField))

        Row(){
            OutlinedTextField(value = term, onValueChange = {term = it}, label = {Text (mortgageTerm)}, modifier = Modifier.width(smallTextField))
            Spacer(modifier = Modifier.width(5.dp))
            OutlinedTextField(value = rate, onValueChange = {rate = it}, label = {Text (interestRate)}, modifier = Modifier.width(smallTextField))
        }

        Text(text = mortgageType, modifier = Modifier.align(alignment = Alignment.CenterHorizontally).padding(top = 12.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(bigTextField)){
            Checkbox(checked = isRepayment, onCheckedChange = {isRepayment = it; isInterestOnly = false})
            Text(text = repayment)
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(bigTextField)){
            Checkbox(checked = isInterestOnly, onCheckedChange = {isInterestOnly = it; isRepayment = false})
            Text(text = interestOnly)
        }
    }
}

@Composable
fun Header(){
    Row(modifier = Modifier.padding(top = 10.dp, bottom = 20.dp), horizontalArrangement = Arrangement.Center){
        Text (text = title, fontWeight = FontWeight.Bold)
    }
}