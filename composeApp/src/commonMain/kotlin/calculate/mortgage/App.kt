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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
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

    var amountf by remember { mutableStateOf(amount.toFloat()) }
    var termf by remember { mutableStateOf(term.toFloat()) }
    var ratef by remember { mutableStateOf(rate.toFloat()) }

    LaunchedEffect(amount) {
        amountf = if (amount.isNotEmpty()) amount.toFloatOrNull() ?: 0f else 0f
    }

    LaunchedEffect(term) {
        termf = if (term.isNotEmpty()) term.toFloatOrNull() ?: 0f else 0f
    }

    LaunchedEffect(rate) {
        ratef = if (rate.isNotEmpty()) rate.toFloatOrNull() ?: 0f else 0f
    }

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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
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
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))

            Spacer(modifier = Modifier.width(5.dp))

            OutlinedTextField(value = rate,
                onValueChange = { rate = it },
                label = { Text(interestRate) },
                modifier = Modifier.width(smallTextField),
                trailingIcon = {
                    Icon(Icons.Default.Clear, contentDescription = "Clear",
                        modifier = Modifier.clickable(enabled = true, onClick = { rate = "" })
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
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
    val termInMonths by remember { derivedStateOf { termf * 12 } }
    val monthlyInterestRate by remember { derivedStateOf { ratef / 100 / 12 } }

    val monthlyPayment by remember (amountf, monthlyInterestRate, termInMonths, isRepayment, isInterestOnly) {
        derivedStateOf {
            if (isRepayment){
                amountf * ((monthlyInterestRate * (1 + monthlyInterestRate).pow(termInMonths)) / ((1 + monthlyInterestRate).pow(termInMonths) - 1))
            }else if (isInterestOnly){
                amountf * monthlyInterestRate
            } else {
                0f
            }
        }
    }
    val totals by remember(monthlyPayment, termInMonths, amountf, isRepayment, isInterestOnly){
        derivedStateOf{
            if (isRepayment) {
                val totalRepayment = monthlyPayment * termInMonths
                val totalInterest = totalRepayment - amountf
                Pair(totalRepayment, totalInterest)
            } else if (isInterestOnly){
                val totalInterest = termInMonths * monthlyPayment
                val totalRepayment = totalInterest + amountf
                Pair(totalRepayment, totalInterest)
            } else {
                Pair (0f, 0f)
            }
        }
    }
// TODO SCOPE END
    Column(
        modifier = Modifier.width(500.dp).height(600.dp).clip(shape = RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

        Text(text = "Monthly Payment: $monthlyPayment")
        Text(text = "Total Repayment: ${totals.first}")
        Text(text = "Total Interest: ${totals.second}")
    }
}

//dist folder - wasmJsBrowserDistribution