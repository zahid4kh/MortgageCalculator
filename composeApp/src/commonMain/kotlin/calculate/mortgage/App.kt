//MIT License
//
//Copyright (c) 2024 Zahid Khalilov
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in all
//copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
//SOFTWARE.

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
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import mortgagecalculator.composeapp.generated.resources.Res
import mortgagecalculator.composeapp.generated.resources.moon
import mortgagecalculator.composeapp.generated.resources.sun
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import ui.inverseSurfaceDark
import ui.inverseSurfaceLight
import ui.onPrimaryDark
import ui.onPrimaryLight
import ui.onSecondaryDark
import ui.onSecondaryLight
import ui.outlineDark
import ui.outlineLight
import ui.primaryContainerDark
import ui.primaryContainerLight
import ui.primaryDark
import ui.primaryLight
import ui.secondaryContainerDark
import ui.secondaryContainerLight
import ui.secondaryDark
import ui.secondaryLight
import ui.surfaceDark
import ui.surfaceLight
import ui.tertiaryContainerDark
import ui.tertiaryContainerLight
import ui.tertiaryDark
import ui.tertiaryLight
import kotlin.math.floor
import kotlin.math.pow

@Composable
fun App() {
    MaterialTheme {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()){

            Calculator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calculator() {
    var amount by remember { mutableStateOf("0") }
    var term by remember { mutableStateOf("0") }
    var rate by remember { mutableStateOf("0") }

    var amountf by remember { mutableStateOf(amount.toFloat()) }
    var termf by remember { mutableStateOf(term.toFloat()) }
    var ratef by remember { mutableStateOf(rate.toFloat()) }

    var isLightMode by remember {mutableStateOf(true)}
    val moon: ImageBitmap =  imageResource(Res.drawable.moon)
    val sun: ImageBitmap = imageResource(Res.drawable.sun)

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


    Switch (
        checked = isLightMode,
        onCheckedChange = {isLightMode = it},
        thumbContent = {
            if (isLightMode){
                Icon(bitmap = sun, contentDescription = "Sun")
            }else{
                Icon(bitmap = moon, contentDescription = "Moon")
            }
        })

    Row(modifier = Modifier.fillMaxSize().background(if (isLightMode) surfaceLight else surfaceDark),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center){

        Column(modifier = Modifier.width(500.dp).height(600.dp).clip(shape = RoundedCornerShape(20.dp))
            .background(if (isLightMode) primaryLight else primaryDark),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            Header(if (isLightMode) onPrimaryLight else onPrimaryDark)

            OutlinedTextField(value = amount,
                onValueChange = { amount = it },
                label = { Text(mortgageAmount) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.width(rowWidth),
                trailingIcon = {
                    Icon(Icons.Default.Clear, contentDescription = "Clear",
                        modifier = Modifier.clickable(enabled = true, onClick = { amount = "" })
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    focusedBorderColor = if (isLightMode) primaryContainerLight else primaryContainerDark,
                    focusedLabelColor = if (isLightMode) onPrimaryLight else onPrimaryDark,
                    unfocusedLabelColor = if (isLightMode) tertiaryLight else tertiaryDark,
                    focusedTextColor = if (isLightMode) onPrimaryLight else onPrimaryDark,
                    unfocusedTextColor = if (isLightMode) onPrimaryLight else onPrimaryDark)
            )

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
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedBorderColor = if (isLightMode) primaryContainerLight else primaryContainerDark,
                        focusedLabelColor = if (isLightMode) onPrimaryLight else onPrimaryDark,
                        unfocusedLabelColor = if (isLightMode) outlineLight else outlineDark,
                        focusedTextColor = if (isLightMode) onPrimaryLight else onPrimaryDark,
                        unfocusedTextColor = if (isLightMode) outlineLight else outlineDark),

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
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.Transparent,
                        focusedBorderColor = if (isLightMode) primaryContainerLight else primaryContainerDark,
                        focusedLabelColor = if (isLightMode) onPrimaryLight else onPrimaryDark,
                        unfocusedLabelColor = if (isLightMode) outlineLight else outlineDark,
                        focusedTextColor = if (isLightMode) onPrimaryLight else onPrimaryDark,
                        unfocusedTextColor = if (isLightMode) outlineLight else outlineDark),

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
            }

            Text(
                text = mortgageType,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally).padding(top = 12.dp),
                color = if (isLightMode) onPrimaryLight else onPrimaryDark
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(rowWidth)) {
                Checkbox(
                    checked = isRepayment,
                    onCheckedChange = { isRepayment = it; isInterestOnly = false },
                    colors = CheckboxColors(
                        checkedBoxColor = if (isLightMode) secondaryLight else secondaryDark,
                        uncheckedBoxColor = Color.Transparent,
                        checkedCheckmarkColor = if (isLightMode) onPrimaryLight else onPrimaryDark,
                        uncheckedCheckmarkColor = Color.Transparent,
                        disabledCheckedBoxColor = Color.Transparent,
                        disabledUncheckedBoxColor = Color.Transparent,
                        disabledIndeterminateBoxColor = Color.Transparent,
                        checkedBorderColor = if (isLightMode) tertiaryContainerLight else tertiaryContainerDark,
                        uncheckedBorderColor = if (isLightMode) tertiaryContainerLight else tertiaryContainerDark,
                        disabledBorderColor = Color.Transparent,
                        disabledUncheckedBorderColor = Color.Unspecified,
                        disabledIndeterminateBorderColor = Color.Unspecified
                    ))

                Text(text = repayment, color = if (isLightMode) onPrimaryLight else onPrimaryDark)
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(rowWidth)) {
                Checkbox(
                    checked = isInterestOnly,
                    onCheckedChange = { isInterestOnly = it; isRepayment = false },
                    colors = CheckboxColors(
                        checkedBoxColor = if (isLightMode) secondaryLight else secondaryDark,
                        uncheckedBoxColor = Color.Transparent,
                        checkedCheckmarkColor = if (isLightMode) onPrimaryLight else onPrimaryDark,
                        uncheckedCheckmarkColor = Color.Transparent,
                        disabledCheckedBoxColor = Color.Transparent,
                        disabledUncheckedBoxColor = Color.Transparent,
                        disabledIndeterminateBoxColor = Color.Transparent,
                        checkedBorderColor = if (isLightMode) tertiaryContainerLight else tertiaryContainerDark,
                        uncheckedBorderColor = if (isLightMode) tertiaryContainerLight else tertiaryContainerDark,
                        disabledBorderColor = Color.Transparent,
                        disabledUncheckedBorderColor = Color.Unspecified,
                        disabledIndeterminateBorderColor = Color.Unspecified
                    ))
                Text(text = interestOnly, color = if (isLightMode) onPrimaryLight else onPrimaryDark)
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
                .background(if (isLightMode) secondaryLight else secondaryDark),
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

            Text(text = "Monthly Payment: ${floor(monthlyPayment * 100) / 100}", color = if (isLightMode) onSecondaryLight else onSecondaryDark)
            Text(text = "Total Repayment: ${floor(totals.first * 100) / 100}", color = if (isLightMode) onSecondaryLight else onSecondaryDark)
            Text(text = "Total Interest: ${floor(totals.second * 100) / 100}", color = if (isLightMode) onSecondaryLight else onSecondaryDark)
        }
    }

}

//dist folder - wasmJsBrowserDistribution