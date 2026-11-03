package com.ahmed.studyos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF101B35)
private val Blue = Color(0xFF3478F6)
private val Gold = Color(0xFFFFC857)
private val Background = Color(0xFFF2F5FC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyOS()
        }
    }
}

data class StudyTask(
    val title: String,
    val subject: String,
    val minutes: Int,
    val done: Boolean = false
)

@Composable
fun StudyOS() {
    var page by remember { mutableStateOf(0) }
    var tasks by remember {
        mutableStateOf(
            listOf(
                StudyTask("مراجعة قوانين الفيزياء", "فيزياء", 45),
                StudyTask("حل تدريبات الرياضيات", "رياضيات", 60),
                StudyTask("مذاكرة كلمات الإنجليزي", "إنجليزي", 30),
                StudyTask("مراجعة النحو", "عربي", 45)
            )
        )
    }

    val completed = tasks.count { it.done }
    val points = completed * 10

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Blue,
            background = Background
        )
    ) {
        Scaffold(
            containerColor = Background,
            bottomBar = {
                NavigationBar(containerColor = Navy) {
                    val labels = listOf("الرئيسية", "المهام", "التركيز", "العبادات")
                    labels.forEachIndexed { index, label ->
                        NavigationBarItem(
                            selected = page == index,
                            onClick = { page = index },
                            icon = {
                                Text(
                                    listOf("⌂", "✓", "◷", "☾")[index],
                                    color = Color.White,
                                    fontSize = 20.sp
                                )
                            },
                            label = {
                                Text(label, color = Color.White, fontSize = 10.sp)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Blue
                            )
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    "AHMED STUDY OS",
                    color = Blue,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                when (page) {
                    0 -> Dashboard(completed, tasks.size, points)
                    1 -> TasksPage(tasks) { index ->
                        tasks = tasks.mapIndexed { i, task ->
                            if (i == index) task.copy(done = !task.done)
                            else task
                        }
                    }
                    2 -> FocusPage()
                    3 -> WorshipPage()
                }
            }
        }
    }
}

@Composable
fun Dashboard(completed: Int, total: Int, points: Int) {
    Text(
        "أهلاً بك في مساحة إنجازك",
        fontSize = 23.sp,
        fontWeight = FontWeight.Bold,
        color = Navy
    )
    Text("كل خطوة صغيرة تقرّبك من هدفك", color = Color.Gray)
    Spacer(Modifier.height(20.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        StatCard("المكتمل", "$completed", Modifier.weight(1f))
        StatCard("المتبقي", "${total - completed}", Modifier.weight(1f))
        StatCard("النقاط", "$points", Modifier.weight(1f))
    }

    Spacer(Modifier.height(20.dp))
    Card(
        colors = CardDefaults.cardColors(containerColor = Navy),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(20.dp)) {
            Text("هدفك اليومي", color = Color.White)
            Spacer(Modifier.height(10.dp))
            Text("4 ساعات دراسة", color = Gold, fontSize = 26.sp,
                fontWeight = FontWeight.Bold)
            Text("ابدأ بمهمة واحدة، واستمر في التقدم.",
                color = Color.White)
        }
    }

    Spacer(Modifier.height(20.dp))
    Text("تقدم المهام", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(Modifier.height(8.dp))
    LinearProgressIndicator(
        progress = { if (total == 0) 0f else completed.toFloat() / total },
        modifier = Modifier.fillMaxWidth().height(10.dp),
        color = Blue
    )
    Text("$completed من $total مهام مكتملة",
        color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
    Spacer(Modifier.height(18.dp))
    Text("ملاحظة: سجل المهام هنا تجريبي مبدئي، وسيتم ربطه بالتخزين والتقارير لاحقًا.",
        color = Color.Gray, fontSize = 12.sp)
}

@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = Blue, fontSize = 23.sp,
                fontWeight = FontWeight.Bold)
            Text(title, color = Color.Gray, fontSize = 11.sp)
        }
    }
}

@Composable
fun TasksPage(tasks: List<StudyTask>, onToggle: (Int) -> Unit) {
    Text("لوحة المعلومات والمهام", fontSize = 22.sp,
        fontWeight = FontWeight.Bold, color = Navy)
    Text("تابع ما أنجزته وما تبقى عليك.", color = Color.Gray)
    Spacer(Modifier.height(12.dp))

    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(tasks.size) { index ->
            val task = tasks[index]
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.done,
                        onCheckedChange = { onToggle(index) }
                    )
                    Column(Modifier.weight(1f)) {
                        Text(task.title, fontWeight = FontWeight.Bold,
                            color = Navy)
                        Text("${task.subject} • ${task.minutes} دقيقة",
                            color = Color.Gray, fontSize = 12.sp)
                    }
                    Text(
                        if (task.done) "مكتملة" else "متبقية",
                        color = if (task.done) Color(0xFF16865A) else Blue,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FocusPage() {
    var minutes by remember { mutableIntStateOf(25) }
    var running by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("مساحة التركيز", fontSize = 24.sp,
            fontWeight = FontWeight.Bold, color = Navy)
        Spacer(Modifier.height(25.dp))
        Box(
            modifier = Modifier
                .size(220.dp)
                .background(Navy, RoundedCornerShape(110.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("◉", color = Gold, fontSize = 38.sp)
                Text("${minutes}:00", color = Color.White,
                    fontSize = 35.sp, fontWeight = FontWeight.Bold)
                Text("وقت التركيز", color = Color.White)
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { running = !running }) {
                Text(if (running) "إيقاف" else "ابدأ")
            }
            OutlinedButton(onClick = {
                running = false
                minutes = 25
            }) {
                Text("إعادة ضبط")
            }
        }
        Text("المؤقت هنا واجهة أولية، وسيتم تطوير العد التنازلي لاحقًا.",
            color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun WorshipPage() {
    val prayers = listOf("الفجر", "الظهر", "العصر", "المغرب", "العشاء")
    var checked by remember { mutableStateOf(setOf<String>()) }
    var quran by remember { mutableStateOf(false) }

    Text("متابعة العبادات", fontSize = 23.sp,
        fontWeight = FontWeight.Bold, color = Navy)
    Text("متابعة يومية مستقلة عن نقاط الدراسة.",
        color = Color.Gray)
    Spacer(Modifier.height(15.dp))

    prayers.forEach { prayer ->
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = prayer in checked,
                    onCheckedChange = {
                        checked = if (it) checked + prayer else checked - prayer
                    }
                )
                Text(prayer, color = Navy, fontWeight = FontWeight.Medium)
            }
        }
    }

    Spacer(Modifier.height(12.dp))
    Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(quran, onCheckedChange = { quran = it })
            Text("ورد القرآن اليومي", color = Navy)
        }
    }
}
