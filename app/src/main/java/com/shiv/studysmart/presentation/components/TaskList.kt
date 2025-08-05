package com.shiv.studysmart.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.shiv.studysmart.R
import com.shiv.studysmart.domain.model.Task
import com.shiv.studysmart.util.Priority
import com.shiv.studysmart.util.changeMillisToDateString

fun LazyListScope.tasksList(
    sectionTitle:String,
    tasks:List<Task>,
    emptyListTask:String,
    onTaskCardClicked:(Int?)->Unit,
    onCheckBoxClicked:(Task)->Unit
){
    item {
        Text(
            text = sectionTitle,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
    if(tasks.isEmpty()){
        item {
           Column(){
               Image(
                   modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally),
                   painter = painterResource(R.drawable.img_tasks),
                   contentDescription = emptyListTask
               )
               Spacer(modifier = Modifier.height(12.dp))
               Text(
                   modifier = Modifier.fillMaxWidth(),
                   text = "You don't have any tasks",
                   style = MaterialTheme.typography.bodySmall,
                   color = Color.Gray,
                   textAlign = TextAlign.Center
               )
           }
        }

    }
    items(tasks){task->
        TaskCard(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            task = task,
            onCheckBoxClicked = { onCheckBoxClicked(task) },
            onClick = { onTaskCardClicked(task.taskId) }
        )
    }

}


@Composable
private fun TaskCard(
    modifier: Modifier = Modifier,
    task:Task,
    onCheckBoxClicked:()->Unit,
    onClick:()->Unit
){
   ElevatedCard(
       modifier = modifier.clickable {  onClick() }
   ) {
       Row(
           modifier = Modifier.fillMaxWidth().padding(8.dp),
           verticalAlignment = Alignment.CenterVertically
       ) {
           TaskCheckBox(
               isComplete =  task.isComplete,
               borderColor = Priority.fromInt(task.priority).color,
               onCheckBoxClicked = {onCheckBoxClicked()}
           )
           Spacer(modifier = Modifier.width(10.dp))
           Column {
               Text(
                   text = task.title,
                   maxLines = 1,
                   overflow = TextOverflow.Ellipsis,
                   style = MaterialTheme.typography.titleMedium,
                   textDecoration = if(task.isComplete){
                       TextDecoration.LineThrough
                   }else{
                       TextDecoration.None
                   }
               )
               Spacer(modifier = Modifier.height(4.dp))
               Text(
                   text = task.dueDate.changeMillisToDateString(),
                   style = MaterialTheme.typography.bodySmall
               )
           }
       }
   }
}