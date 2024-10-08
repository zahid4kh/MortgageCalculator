package calculate.mortgage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.pow

@Composable
fun App() {
    MaterialTheme {
        Row(modifier = Modifier.fillMaxSize().background(Color.LightGray),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
            Calculator()
        }
    }
}

@Composable
fun Calculator() {
    var amount by remember { mutableStateOf("0") }
    var term by remember { mutableStateOf("0") }
    var rate by remember { mutableStateOf("0") }
    val rowWidth = 410.dp
    val smallTextField = rowWidth / 2
    var isRepayment by remember { mutableStateOf(true) }
    var isInterestOnly by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.width(500.dp).height(600.dp).clip(shape = RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
    )
    {
        Header()

        OutlinedTextField(value = amount,
            onValueChange = { amount = it },
            label = { Text(mortgageAmount) },
            modifier = Modifier.width(rowWidth),
            trailingIcon = {
                Icon(Icons.Default.Clear, contentDescription = "Clear",
                    modifier = Modifier.clickable(enabled = true, onClick = { amount = "" })
                )
            })

        Row(modifier = Modifier.width(rowWidth)) {

            OutlinedTextField(value = term,
                onValueChange = { term = it },
                label = { Text(mortgageTerm) },
                modifier = Modifier.width(smallTextField),
                trailingIcon = {
                    Icon(Icons.Default.Clear, contentDescription = "Clear",
                        modifier = Modifier.clickable(enabled = true, onClick = { term = "" })
                    )
                })

            Spacer(modifier = Modifier.width(5.dp))

            OutlinedTextField(value = rate,
                onValueChange = { rate = it },
                label = { Text(interestRate) },
                modifier = Modifier.width(smallTextField),
                trailingIcon = {
                    Icon(Icons.Default.Clear, contentDescription = "Clear",
                        modifier = Modifier.clickable(enabled = true, onClick = { rate = "" })
                    )
                })
        }

        Text(
            text = mortgageType,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally).padding(top = 12.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(rowWidth)) {
            Checkbox(
                checked = isRepayment,
                onCheckedChange = { isRepayment = it; isInterestOnly = false })
            Text(text = repayment)
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(rowWidth)) {
            Checkbox(
                checked = isInterestOnly,
                onCheckedChange = { isInterestOnly = it; isRepayment = false })
            Text(text = interestOnly)
        }

    }
// TODO SCOPE START
    val termInMonths by remember { derivedStateOf { term.toFloat() * 12 } }
    val monthlyInterestRate by remember { derivedStateOf { rate.toFloat() / 12 } }

    val monthlyPayment by remember (amount, monthlyInterestRate, termInMonths, isRepayment) {
        derivedStateOf {
            if (isRepayment){
                amount.toFloat() * ((monthlyInterestRate * (1 + monthlyInterestRate).pow(termInMonths)) / ((1 + monthlyInterestRate).pow(termInMonths) - 1))
            }else{
                amount.toFloat() * monthlyInterestRate
            }
        }
    }
    val totals by remember(monthlyPayment, termInMonths, amount, isRepayment){
        derivedStateOf{
            if (isRepayment) {
                val totalRepayment = monthlyPayment * termInMonths
                val totalInterest = totalRepayment - amount.toFloat()
                Pair(totalRepayment, totalInterest)
            } else {
                val totalInterest = termInMonths * monthlyPayment
                val totalRepayment = totalInterest + amount.toFloat()
                Pair(totalRepayment, totalInterest)
            }
        }
    }
// TOD SCOPE END
    Column(
        modifier = Modifier.width(500.dp).height(600.dp).clip(shape = RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        Text(text = "Monthly Payment: $monthlyPayment")
        Text(text = "Total Repayment: ${totals.first}")
        Text(text = "Total Interest: ${totals.second}")
    }
}