package com.shiv.studysmart.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteDialog(
    isOpen:Boolean,
    onDismissRequest:()->Unit,
    onConfirmButtonClick:()->Unit,
    title:String = "Add/Update Subject",
    bodyText:String
){

    if(isOpen){
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(text = title)
            },
            text = {
               Text(text = bodyText)
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(onClick = onConfirmButtonClick,
                ) {
                    Text(text = "Delete")
                }
            }
        )
    }
}