package com.shiv.studysmart.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.shiv.studysmart.domain.model.Subject

@Composable
fun AddSubjectDialog(
    modifier: Modifier = Modifier,
    isOpen:Boolean,
    onDismissRequest:()->Unit,
    onConfirmButtonClick:()->Unit,
    title:String = "Add/Update Subject",
    selectColors:List<Color>,
    onColorChange:(List<Color>)->Unit,
    subjectName:String,
    goalHours:String,
    onSubjectNameChange:(String)->Unit,
    onGoalHoursChange:(String)->Unit
){
    var subjectNameError by rememberSaveable { mutableStateOf<String?>(null) }
    var goalHoursError by rememberSaveable { mutableStateOf<String?>(null) }

    subjectNameError = when{
        subjectName.isEmpty() -> "Please enter subject name"
        subjectName.length < 2 -> "Subject name is too short"
        subjectName.length > 20 -> "Subject name is too long"
        else -> null
    }
    goalHoursError = when{
        goalHours.isEmpty() -> "Please enter goal hours"
        goalHours.toFloatOrNull() == null -> "Please enter a valid number"
        goalHours.toFloat() < 1f -> "Please set at least 1 hour"
        goalHours.toFloat() > 1000f -> "Please set a maximum of 1000 hours"
        else -> null
    }
    if(isOpen){
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(text = title)
            },
            text = {
                Column{
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                        ) {
                        Subject.subjectCardColors.forEach {
                            colors ->
                            Box(
                                modifier = Modifier.size(24.dp).clip(CircleShape)
                                    .background(brush = Brush.horizontalGradient(colors))
                                    .border(
                                        width = 1.dp,
                                        color = if(colors == selectColors) Color.Black else Color.Transparent, shape = CircleShape
                                    )
                                    .clickable {
                                        onColorChange(colors)
                                    }
                            )
                        }
                    }

                    OutlinedTextField(
                        isError = subjectNameError != null && subjectName.isNotBlank(),
                        supportingText = {
                           Text(text = subjectNameError.orEmpty())
                        },

                        value = subjectName,
                        onValueChange = onSubjectNameChange,
                        label = {
                            Text(text = "Subject Name")
                        },
                        singleLine = true,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        isError = goalHoursError != null && goalHours.isNotBlank(),
                        supportingText = {
                            Text( text = goalHoursError.orEmpty())
                        },

                        value = goalHours,
                        onValueChange = onGoalHoursChange,
                        label = {
                            Text(text = "Goal Study Hours")
                        },
                        singleLine = true,
                       keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                TextButton(onClick = onConfirmButtonClick,
                    enabled = subjectNameError == null && goalHoursError == null
                    ) {
                    Text(text = "Save")
                }
            }
        )
    }
}