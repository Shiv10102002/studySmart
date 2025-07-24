package com.shiv.studysmart.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shiv.studysmart.R
import com.shiv.studysmart.domain.model.Subject
import com.shiv.studysmart.presentation.components.CountCard
import com.shiv.studysmart.presentation.components.SubjectCard

@Composable
fun DashboardScreen(){
    val subjects = listOf(
        Subject(name = "English", goalHours = 10f, colors = Subject.subjectCardColors[0]),
        Subject(name = "Physics", goalHours = 10f, colors = Subject.subjectCardColors[1]),
        Subject(name = "Maths", goalHours = 10f, colors = Subject.subjectCardColors[2]),
        Subject(name="Chemistry", goalHours = 10f, colors = Subject.subjectCardColors[3]),
        Subject(name = "Fine Arts", goalHours = 10f, colors = Subject.subjectCardColors[4])
    )

    Scaffold(
        topBar ={ DashboardScreenTopBar() }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            item{
                CountCardSection(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    studiedHours = "10",
                    goalStudyHours = "15",
                    subjectCount = "5"
                )
            }
            item{
                SubjectCardsSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = subjects
                )
            }
        }

    }
}

@Composable
private fun CountCardSection(
    modifier: Modifier = Modifier,
    subjectCount:String,
    studiedHours:String,
    goalStudyHours:String

){
    Row(modifier = modifier ) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Subject Count",
            count = subjectCount
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Studied Hours",
            count = studiedHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hours",
            count = goalStudyHours
        )
    }
}

@Composable
private fun SubjectCardsSection(
    modifier: Modifier,
    subjectList:List<Subject>,
    emptyListText:String = "you don't have any subjects.\n Click the + button t o add new subjects"
){
    Column(modifier = modifier){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(
                onClick = { /*TODO*/ },
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
                )
            }
        }
        if(subjectList.isEmpty()){
            Image(
            modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.img_books),
                contentDescription = emptyListText
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(subjectList){subject->
                SubjectCard(
                    modifier = Modifier.size(150.dp),
                    subjectName = subject.name,
                    onClick = { /*TODO*/ },
                    gradientColor = subject.colors
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar(){
    CenterAlignedTopAppBar(
        title = {
            Text(text = "StudySmart",
            style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

@Preview
@Composable
private fun DashboradPreview(){
   DashboardScreen()
//    SubjectCardsSection()
}