package com.example.ui.screens.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.*
import com.example.ui.components.EducareBadge
import com.example.ui.components.EducareHeader
import com.example.ui.components.StatCard
import com.example.ui.screens.attendance.AttendanceScreen
import com.example.ui.screens.homework.HomeworkScreen
import com.example.ui.screens.timetable.TimetableScreen
import com.example.ui.theme.*

enum class TeacherTab {
    DASHBOARD,
    ATTENDANCE,
    HOMEWORK,
    TIMETABLE,
    STUDENTS
}

@Composable
fun TeacherPortalScreen(
    currentUser: UserEntity,
    teachers: List<TeacherEntity>,
    students: List<StudentEntity>,
    timetables: List<TimetableEntity>,
    homeworkList: List<HomeworkEntity>,
    announcements: List<AnnouncementEntity>,
    onSaveAttendance: (List<AttendanceEntity>) -> Unit,
    onCreateHomework: (HomeworkEntity) -> Unit,
    onDeleteHomework: (Long) -> Unit,
    onLogout: () -> Unit,
    onOpenWebsite: () -> Unit
) {
    var activeTab by remember { mutableStateOf(TeacherTab.DASHBOARD) }

    val teacherProfile = remember(teachers, currentUser) {
        teachers.find { it.id == currentUser.linkedTeacherId }
            ?: teachers.firstOrNull()
            ?: TeacherEntity(
                fullName = currentUser.fullName,
                email = currentUser.username,
                phone = "03121234567",
                qualification = "M.Sc",
                designation = "Senior Lecturer",
                assignedClasses = "Class 10, Class 9",
                assignedSubjects = "Physics, Mathematics",
                joiningDate = "2022-01-01"
            )
    }

    val teacherSchedule = remember(timetables, teacherProfile) {
        timetables.filter { it.teacherName.contains(teacherProfile.fullName, ignoreCase = true) || it.className in listOf("Class 10", "Class 9") }
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
                    selected = activeTab == TeacherTab.DASHBOARD,
                    onClick = { activeTab = TeacherTab.DASHBOARD },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                    label = { Text("Overview") }
                )
                NavigationBarItem(
                    selected = activeTab == TeacherTab.ATTENDANCE,
                    onClick = { activeTab = TeacherTab.ATTENDANCE },
                    icon = { Icon(Icons.Default.FactCheck, contentDescription = null) },
                    label = { Text("Attendance") }
                )
                NavigationBarItem(
                    selected = activeTab == TeacherTab.HOMEWORK,
                    onClick = { activeTab = TeacherTab.HOMEWORK },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = null) },
                    label = { Text("Homework") }
                )
                NavigationBarItem(
                    selected = activeTab == TeacherTab.TIMETABLE,
                    onClick = { activeTab = TeacherTab.TIMETABLE },
                    icon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                    label = { Text("Timetable") }
                )
                NavigationBarItem(
                    selected = activeTab == TeacherTab.STUDENTS,
                    onClick = { activeTab = TeacherTab.STUDENTS },
                    icon = { Icon(Icons.Default.Groups, contentDescription = null) },
                    label = { Text("My Class") }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (activeTab) {
                TeacherTab.DASHBOARD -> TeacherDashboardView(
                    teacher = teacherProfile,
                    schedule = teacherSchedule,
                    homework = homeworkList,
                    announcements = announcements,
                    onNavigateToAttendance = { activeTab = TeacherTab.ATTENDANCE },
                    onNavigateToHomework = { activeTab = TeacherTab.HOMEWORK }
                )
                TeacherTab.ATTENDANCE -> AttendanceScreen(
                    students = students,
                    onSaveAttendance = onSaveAttendance
                )
                TeacherTab.HOMEWORK -> HomeworkScreen(
                    homeworkList = homeworkList,
                    canCreate = true,
                    onCreateHomework = onCreateHomework,
                    onDeleteHomework = onDeleteHomework
                )
                TeacherTab.TIMETABLE -> TimetableScreen(
                    timetables = timetables,
                    onAddTimetable = {}
                )
                TeacherTab.STUDENTS -> TeacherClassRosterView(
                    students = students,
                    assignedClasses = teacherProfile.assignedClasses
                )
            }
        }
    }
}

@Composable
private fun TeacherDashboardView(
    teacher: TeacherEntity,
    schedule: List<TimetableEntity>,
    homework: List<HomeworkEntity>,
    announcements: List<AnnouncementEntity>,
    onNavigateToAttendance: () -> Unit,
    onNavigateToHomework: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Teacher Profile Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = EducareNavyPrimary.copy(alpha = 0.08f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PersonPin, contentDescription = null, tint = EducareNavyPrimary, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(teacher.fullName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("${teacher.designation} | ${teacher.qualification}", style = MaterialTheme.typography.bodySmall.copy(color = EducareGoldDark, fontWeight = FontWeight.SemiBold))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Assigned Classes: ${teacher.assignedClasses}", style = MaterialTheme.typography.bodySmall)
                Text("Assigned Subjects: ${teacher.assignedSubjects}", style = MaterialTheme.typography.bodySmall.copy(color = EducareNavyPrimary, fontWeight = FontWeight.Medium))
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Quick Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onNavigateToAttendance,
                colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                modifier = Modifier.weight(1f).height(46.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.FactCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Mark Attendance")
            }

            Button(
                onClick = onNavigateToHomework,
                colors = ButtonDefaults.buttonColors(containerColor = EducareGold),
                modifier = Modifier.weight(1f).height(46.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = EducareNavyDark, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Assign Homework", color = EducareNavyDark, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Today's Teaching Schedule
        Text("Today's Timetable / Periods", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        if (schedule.isEmpty()) {
            Text("No classes scheduled for today.", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
        } else {
            schedule.forEach { p ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Period ${p.periodNumber}: ${p.subjectName}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text("${p.className} (${p.section}) - ${p.roomNumber}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                        }
                        Text("${p.startTime} - ${p.endTime}", style = MaterialTheme.typography.labelSmall.copy(color = EducareNavyPrimary, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Recent Circulars
        Text("School Announcements & Notices", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
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
private fun TeacherClassRosterView(
    students: List<StudentEntity>,
    assignedClasses: String
) {
    var selectedClass by remember { mutableStateOf("Class 10") }
    val classStudents = remember(students, selectedClass) {
        students.filter { it.className.equals(selectedClass, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Student Academic Roster", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("Class 10", "Class 9", "Class 8").forEach { cls ->
                FilterChip(
                    selected = selectedClass == cls,
                    onClick = { selectedClass = cls },
                    label = { Text(cls) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EducareNavyPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(classStudents, key = { it.id }) { s ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(s.rollNo, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(s.fullName, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text("Father: ${s.fatherName} | Contact: ${s.phone}", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                        }
                        EducareBadge(text = s.status, type = s.status)
                    }
                }
            }
        }
    }
}
