import androidx.compose.ui.graphics.Color
import com.shiv.studysmart.domain.model.Session
import com.shiv.studysmart.domain.model.Subject
import com.shiv.studysmart.domain.model.Task

data class SubjectState(
    val currentSubjectId:Int? = null,
    val subjectName:String = "",
    val goalStudyHours:String = "",
    val studiedHours:Float = 0f,
    val subjectCardColors: List<Color> = Subject.subjectCardColors.random(),
    val recentSession:List<Session> = emptyList(),
    val upcomingTasks:List<Task> = emptyList(),
    val completedTask:List<Task> = emptyList(),
    val session:Session? = null,
    val progress: Float = 0f,
    val isLoading:Boolean  = false
    )

