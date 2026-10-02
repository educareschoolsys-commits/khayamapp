package com.example.ui.screens.student

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.*
import com.example.ui.components.EducareBadge
import com.example.ui.components.EducareHeader
import com.example.ui.theme.*

enum class StudentTab {
    DASHBOARD,
    TIMETABLE,
    HOMEWORK,
    RESULTS
}

@Composable
fun StudentPortalScreen(
    currentUser: UserEntity,
    students: List<StudentEntity>,
    timetables: List<TimetableEntity>,
    homeworkList: List<HomeworkEntity>,
    announcements: List<AnnouncementEntity>,
    onViewResultCard: (StudentEntity) -> Unit,
    onLogout: () -> Unit,
    onOpenWebsite: () -> Unit
) {
    var activeTab by remember { mutableStateOf(StudentTab.DASHBOARD) }

    val studentProfile = remember(students, currentUser) {
        students.find { it.id == currentUser.linkedStudentId }
            ?: students.firstOrNull()
            ?: StudentEntity(
                admissionNo = "ADM-2024-101",
                rollNo = "101",
                fullName = currentUser.fullName,
                fatherName = "Haji Abdul Rehman",
                dob = "2009-08-14",
                gender = "Male",
                className = "Class 10",
                section = "A",
                phone = "03331234567",
                address = "Main Bazaar Chagmalai",
                admissionDate = "2024-04-01"
            )
    }

    val classTimetable = remember(timetables, studentProfile) {
        timetables.filter { it.className.equals(studentProfile.className, ignoreCase = true) }
    }

    val classHomework = remember(homeworkList, studentProfile) {
        homeworkList.filter { it.className.equals(studentProfile.className, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            EducareHeader(
                currentUser = currentUser,
                onLogout = onLogout,
                onOpenWebsite = onOpenWebsite
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = activeTab == StudentTab.DASHBOARD,
                    onClick = { activeTab = StudentTab.DASHBOARD },
                    icon = { Icon(Icons.Default.School, contentDescription = null) },
                    label = { Text("My Desk") }
                )
                NavigationBarItem(
                    selected = activeTab == StudentTab.TIMETABLE,
                    onClick = { activeTab = StudentTab.TIMETABLE },
                    icon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                    label = { Text("Periods") }
                )
                NavigationBarItem(
                    selected = activeTab == StudentTab.HOMEWORK,
                    onClick = { activeTab = StudentTab.HOMEWORK },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
                    label = { Text("Homework") }
                )
                NavigationBarItem(
                    selected = activeTab == StudentTab.RESULTS,
                    onClick = { activeTab = StudentTab.RESULTS },
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = null) },
                    label = { Text("Result Card") }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when (activeTab) {
                StudentTab.DASHBOARD -> StudentDeskView(
                    student = studentProfile,
                    homeworkCount = classHomework.size,
                    announcements = announcements,
                    onViewResult = { onViewResultCard(studentProfile) },
                    onNavigateHomework = { activeTab = StudentTab.HOMEWORK },
                    onNavigateTimetable = { activeTab = StudentTab.TIMETABLE }
                )
                StudentTab.TIMETABLE -> StudentTimetableView(schedule = classTimetable)
                StudentTab.HOMEWORK -> StudentHomeworkView(homework = classHomework)
                StudentTab.RESULTS -> StudentResultCardTriggerView(
                    student = studentProfile,
                    onViewResult = { onViewResultCard(studentProfile) }
                )
            }
        }
    }
}

@Composable
private fun StudentDeskView(
    student: StudentEntity,
    homeworkCount: Int,
    announcements: List<AnnouncementEntity>,
    onViewResult: () -> Unit,
    onNavigateHomework: () -> Unit,
    onNavigateTimetable: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Child-friendly welcome banner
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = EducareNavyPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .border(2.dp, EducareGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Welcome Back,", style = MaterialTheme.typography.labelMedium.copy(color = EducareGoldLight))
                        Text(student.fullName, style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold))
                        Text("${student.className} (${student.section}) - Roll No: ${student.rollNo}", style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.9f)))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Action Tiles
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ActionTile(
                title = "My Homework",
                badge = "$homeworkCount tasks",
                icon = Icons.Default.MenuBook,
                color = EducareGoldDark,
                modifier = Modifier.weight(1f),
                onClick = onNavigateHomework
            )
            ActionTile(
                title = "Timetable",
                badge = "6 periods",
                icon = Icons.Default.Schedule,
                color = EducareNavyLight,
                modifier = Modifier.weight(1f),
                onClick = onNavigateTimetable
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Result Card CTA
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = EducareNavyPrimary.copy(alpha = 0.08f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("First Term Examination Result", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text("Grade: A+ (90.3%) - Top 3 Position", style = MaterialTheme.typography.bodySmall.copy(color = EducareSuccess, fontWeight = FontWeight.SemiBold))
                }
                Button(
                    onClick = onViewResult,
                    colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("student_view_result_button")
                ) {
                    Text("View")
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text("Important School Notices", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        announcements.take(2).forEach { ann ->
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(ann.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                    Text(ann.message, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155)))
                }
            }
        }
    }
}

@Composable
private fun ActionTile(
    title: String,
    badge: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFF0F172A)),
            contentPadding = PaddingValues(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.height(6.dp))
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                Text(badge, style = MaterialTheme.typography.labelSmall.copy(color = color, fontWeight = FontWeight.SemiBold))
            }
        }
    }
}

@Composable
private fun StudentTimetableView(schedule: List<TimetableEntity>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("My Daily Class Schedule", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(schedule, key = { it.id }) { p ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Period ${p.periodNumber}: ${p.subjectName}", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            Text("Teacher: ${p.teacherName} | ${p.roomNumber}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                        }
                        Text("${p.startTime} - ${p.endTime}", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
                    }
                }
            }
        }
    }
}

@Composable
private fun StudentHomeworkView(homework: List<HomeworkEntity>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Homework Assignments", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(homework, key = { it.id }) { hw ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(hw.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            EducareBadge(text = "Due: ${hw.dueDate}", type = "WARNING")
                        }
                        Text("Subject: ${hw.subjectName} (Teacher: ${hw.teacherName})", style = MaterialTheme.typography.bodySmall.copy(color = EducareNavyPrimary, fontWeight = FontWeight.SemiBold))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(hw.description, style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF334155)))
                    }
                }
            }
        }
    }
}

@Composable
private fun StudentResultCardTriggerView(student: StudentEntity, onViewResult: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = EducareGold, modifier = Modifier.size(56.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text("Official Report Card", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Text("${student.fullName} - ${student.className}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onViewResult,
                    colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Assignment, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Open Printable Result Card")
                }
            }
        }
    }
}
