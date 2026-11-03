package com.ahmed.studyos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val Navy = Color(0xFF101A35)
private val CardBlue = Color(0xFF1B2B50)
private val Blue = Color(0xFF477BFF)
private val Gold = Color(0xFFFFC857)
private val Turquoise = Color(0xFF35D6C5)
private val Purple = Color(0xFF9B83FF)
private val White = Color(0xFFF5F7FF)
private val Muted = Color(0xFF9EABC8)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyOSApp()
        }
    }
}

data class StudyTask(
    val title: String,
    val subject: String,
    val duration: String,
    val color: Color,
    var done: Boolean = false
)

@Composable
fun StudyOSApp() {
    var page by remember { mutableStateOf(0) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Blue,
            secondary = Gold,
            background = Navy,
            surface = CardBlue
        )
    ) {
        Scaffold(
            containerColor = Navy,
            bottomBar = {
                NavigationBar(
                    containerColor = Color(0xFF15213F)
                ) {
                    val items = listOf(
                        Triple("الرئيسية", Icons.Default.Home, 0),
                        Triple("المهام", Icons.Default.CheckCircle, 1),
                        Triple("التركيز", Icons.Default.Timer, 2),
                        Triple("العبادة", Icons.Default.Mosque, 3)
                    )

                    items.forEach { item ->
                        NavigationBarItem(
                            selected = page == item.third,
                            onClick = { page = item.third },
                            icon = {
                                Icon(item.second, contentDescription = item.first)
                            },
                            label = { Text(item.first) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Gold,
                                selectedTextColor = Gold,
                                unselectedIconColor = Muted,
                                unselectedTextColor = Muted,
                                indicatorColor = CardBlue
                            )
                        )
                    }
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (page) {
                    0 -> DashboardScreen { page = 1 }
                    1 -> TasksScreen()
                    2 -> FocusScreen()
                    3 -> WorshipScreen()
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(openTasks: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp),
        contentPadding = PaddingValues(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("AHMED STUDY OS", color = Gold, fontSize = 13.sp,
                        fontWeight = FontWeight.Bold)
                    Text("أهلاً بك 👋", color = White, fontSize = 26.sp,
                        fontWeight = FontWeight.Bold)
                    Text("ابدأ يومك بإنجاز جديد", color = Muted)
                }
                Surface(
                    shape = CircleShape,
                    color = CardBlue,
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.School, null, tint = Gold,
                            modifier = Modifier.size(28.dp))
                    }
                }
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = CardBlue,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("لوحة المعلومات", color = White, fontSize = 19.sp,
                        fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(18.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatCard("المهام", "5", Blue)
                        StatCard("الإنجاز", "0%", Turquoise)
                        StatCard("النقاط", "0", Gold)
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("هدفك اليومي: 4 ساعات", color = Muted)
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { 0f },
                        modifier = Modifier.fillMaxWidth().height(9.dp),
                        color = Turquoise,
                        trackColor = Navy
                    )
                }
            }
        }

        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("مهام اليوم", color = White, fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
                TextButton(onClick = openTasks) {
                    Text("عرض الكل", color = Gold)
                }
            }
            TaskPreview("فيزياء", "مراجعة التيار الكهربي", Blue)
            TaskPreview("كيمياء", "مذاكرة الدرس الجديد", Turquoise)
            TaskPreview("إنجليزي", "حل تدريبات الوحدة", Purple)
        }

        item {
            Surface(
                color = Color(0xFF26365D),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.EmojiEvents, null, tint = Gold,
                        modifier = Modifier.size(38.dp))
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text("كل خطوة بتفرق!", color = Gold,
                            fontWeight = FontWeight.Bold)
                        Text("استمر في التقدم وحقق هدفك اليوم.",
                            color = White)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontSize = 25.sp,
            fontWeight = FontWeight.Bold)
        Text(title, color = Muted, fontSize = 12.sp)
    }
}

@Composable
fun TaskPreview(subject: String, title: String, color: Color) {
    Surface(
        color = CardBlue,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
    ) {
        Row(
            Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(9.dp).background(color, CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(subject, color = color, fontSize = 12.sp)
                Text(title, color = White, fontWeight = FontWeight.Medium)
            }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.ChevronLeft, null, tint = Muted)
        }
    }
}

@Composable
fun TasksScreen() {
    val tasks = remember {
        mutableStateListOf(
            StudyTask("مراجعة التيار الكهربي", "فيزياء", "60 دقيقة", Blue),
            StudyTask("حل مسائل الدرس", "فيزياء", "45 دقيقة", Turquoise),
            StudyTask("مذاكرة الوحدة", "إنجليزي", "60 دقيقة", Purple),
            StudyTask("مراجعة النحو", "عربي", "45 دقيقة", Gold),
            StudyTask("حل تدريبات", "كيمياء", "50 دقيقة", Blue)
        )
    }

    Column(
        Modifier.fillMaxSize().padding(20.dp)
    ) {
        Text("مهام اليوم", color = White, fontSize = 26.sp,
            fontWeight = FontWeight.Bold)
        Text("أنجز مهامك خطوة بخطوة", color = Muted)
        Spacer(Modifier.height(20.dp))

        tasks.forEachIndexed { index, task ->
            Surface(
                color = CardBlue,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
            ) {
                Row(
                    Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.done,
                        onCheckedChange = {
                            tasks[index] = task.copy(done = it)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Turquoise
                        )
                    )
                    Column(Modifier.weight(1f)) {
                        Text(task.title, color = White,
                            fontWeight = FontWeight.Bold)
                        Text("${task.subject} • ${task.duration}",
                            color = Muted, fontSize = 12.sp)
                    }
                    Box(
                        Modifier.size(9.dp).background(task.color, CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun FocusScreen() {
    var seconds by remember { mutableIntStateOf(25 * 60) }
    var running by remember { mutableStateOf(false) }

    LaunchedEffect(running) {
        while (running && seconds > 0) {
            delay(1000)
            seconds--
        }
        if (seconds == 0) running = false
    }

    val minutesText = (seconds / 60).toString().padStart(2, '0')
    val secondsText = (seconds % 60).toString().padStart(2, '0')

    Column(
        Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("غرفة التركيز", color = White, fontSize = 27.sp,
            fontWeight = FontWeight.Bold)
        Text("وقتك الآن للاستثمار في نفسك", color = Muted)
        Spacer(Modifier.height(38.dp))

        Box(
            Modifier.size(245.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { seconds / (25f * 60f) },
                modifier = Modifier.fillMaxSize(),
                color = Turquoise,
                trackColor = CardBlue,
                strokeWidth = 13.dp
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Timer, null, tint = Gold,
                    modifier = Modifier.size(35.dp))
                Text("$minutesText:$secondsText", color = White,
                    fontSize = 43.sp, fontWeight = FontWeight.Bold)
                Text("وقت التركيز", color = Muted)
            }
        }

        Spacer(Modifier.height(35.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { running = !running },
                colors = ButtonDefaults.buttonColors(containerColor = Blue)
            ) {
                Icon(
                    if (running) Icons.Default.Pause else Icons.Default.PlayArrow,
                    null
                )
                Spacer(Modifier.width(6.dp))
                Text(if (running) "إيقاف مؤقت" else "ابدأ التركيز")
            }

            OutlinedButton(
                onClick = {
                    running = false
                    seconds = 25 * 60
                }
            ) {
                Text("إعادة")
            }
        }
    }
}

@Composable
fun WorshipScreen() {
    val prayers = listOf("الفجر", "الظهر", "العصر", "المغرب", "العشاء")
    val checked = remember { mutableStateListOf(false, false, false, false, false) }
    var quranDone by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().padding(20.dp)
    ) {
        Text("متابعة العبادة", color = White, fontSize = 26.sp,
            fontWeight = FontWeight.Bold)
        Text("متابعة شخصية بدون نقاط أو خصومات", color = Muted)
        Spacer(Modifier.height(20.dp))

        prayers.forEachIndexed { index, prayer ->
            Surface(
                color = CardBlue,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
            ) {
                Row(
                    Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(prayer, color = White, modifier = Modifier.weight(1f))
                    Checkbox(
                        checked = checked[index],
                        onCheckedChange = { checked[index] = it },
                        colors = CheckboxDefaults.colors(checkedColor = Turquoise)
                    )
                }
            }
        }

        Spacer(Modifier.height(15.dp))

        Surface(
            color = Color(0xFF26365D),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("ورد القرآن اليومي", color = White,
                    modifier = Modifier.weight(1f))
                Checkbox(
                    checked = quranDone,
                    onCheckedChange = { quranDone = it }
                )
            }
        }
    }
}
