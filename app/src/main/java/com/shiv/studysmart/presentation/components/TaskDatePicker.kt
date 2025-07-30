package com.shiv.studysmart.presentation.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import java.time.Instant
import java.time.ZoneId
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TaskDatePicker(
//    state: DatePickerState,
//    isOpen:Boolean,
//    confirmButtonText:String = "Ok",
//    dismissButtonText:String="Cancel",
//    onConfirmButtonClicked:()->Unit,
//    onDismissRequest:()->Unit
//    ){
//
//   if(isOpen){
//       DatePickerDialog(
//           onDismissRequest = onDismissRequest,
//           confirmButton = {
//               TextButton(onClick = onConfirmButtonClicked) {
//                   Text(text = confirmButtonText)
//               }
//           },
//           dismissButton = {
//               TextButton(onClick = onDismissRequest) {
//                   Text(text = dismissButtonText)
//               }
//           },
//           content = {
//               DatePicker(
//                   state = state,
//                   dateValidator = {
//                       timestamp->
//                       val selectedDate = Instant.ofEpochMilli(timestamp)
//                           .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
//                       val currentDate = java.time.LocalDate.now(ZoneId.systemDefault())
//                       selectedDate >=currentDate
//                   }
//
//               )
//           }
//       )
//   }
//
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDatePicker(
    state: DatePickerState,
    isOpen: Boolean,
    confirmButtonText: String = "Ok",
    dismissButtonText: String = "Cancel",
    onConfirmButtonClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    if (isOpen) {
        DatePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onConfirmButtonClicked) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = dismissButtonText)
                }
            },
            content = {
                DatePicker(
                    state = state,
                )
            }
        )
    }
}
