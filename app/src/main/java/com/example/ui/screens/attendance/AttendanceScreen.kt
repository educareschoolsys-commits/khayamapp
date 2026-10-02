package com.example.ui.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceEntity
import com.example.data.model.StudentEntity
import com.example.ui.components.EducareBadge
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.theme.*

@Composable
fun AttendanceScreen(
    students: List<StudentEntity>,
    onSaveAttendance: (List<AttendanceEntity>) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedClass by remember { mutableStateOf("Class 10") }
    var selectedSection by remember { mutableStateOf("A") }
    var selectedDate by remember { mutableStateOf("2026-10-02") }

    val classOptions = listOf("Class 10", "Class 9", "Class 8", "Class 7", "Class 6", "Nursery", "Prep")

    val classStudents = remember(students, selectedClass, selectedSection) {
        students.filter { it.className.equals(selectedClass, ignoreCase = true) }
    }

    // Map of studentId -> status ("PRESENT", "ABSENT", "LEAVE")
    val attendanceMap = remember(classStudents) {
        mutableStateMapOf<Long, String>().apply {
            classStudents.forEach { s ->
                put(s.id, "PRESENT") // Default to present
            }
        }
    }

    val presentCount = attendanceMap.values.count { it == "PRESENT" }
    val absentCount = attendanceMap.values.count { it == "ABSENT" }
    val leaveCount = attendanceMap.values.count { it == "LEAVE" }
    val totalCount = attendanceMap.size
    val attendancePercentage = if (totalCount > 0) (presentCount.toDouble() / totalCount) * 100.0 else 0.0

    Scaffold(
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            classStudents.forEach { attendanceMap[it.id] = "PRESENT" }
                        },
                        modifier = Modifier.weight(1f).testTag("mark_all_present_button")
                    ) {
                        Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("All Present")
                    }

                    Button(
                        onClick = {
                            val records = classStudents.map { s ->
                                AttendanceEntity(
                                    studentId = s.id,
                                    studentName = s.fullName,
                                    rollNo = s.rollNo,
                                    className = selectedClass,
                                    section = selectedSection,
                                    date = selectedDate,
                                    status = attendanceMap[s.id] ?: "PRESENT"
                                )
                            }
                            onSaveAttendance(records)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1.3f).testTag("save_attendance_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Attendance")
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Selectors row
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EducareNavyPrimary.copy(alpha = 0.05f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Mark Class Attendance",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = EducareNavyPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Class selector
                        var expandedClass by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { expandedClass = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(selectedClass, maxLines = 1)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                            DropdownMenu(
                                expanded = expandedClass,
                                onDismissRequest = { expandedClass = false }
                            ) {
                                classOptions.forEach { opt ->
                                    DropdownMenuItem(
                                        text = { Text(opt) },
                                        onClick = {
                                            selectedClass = opt
                                            expandedClass = false
                                        }
                                    )
                                }
                            }
                        }

                        // Section selector
                        OutlinedButton(
                            onClick = { selectedSection = if (selectedSection == "A") "B" else "A" },
                            modifier = Modifier.width(90.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Sec: $selectedSection")
                        }

                        // Date display
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = EducareNavyPrimary,
                            modifier = Modifier.height(40.dp)
                        ) {
                            Box(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = selectedDate,
                                    style = MaterialTheme.typography.labelMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Attendance KPI summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AttendanceMiniKpi("Present", "$presentCount", EducareSuccess, Modifier.weight(1f))
                AttendanceMiniKpi("Absent", "$absentCount", EducareError, Modifier.weight(1f))
                AttendanceMiniKpi("Leave", "$leaveCount", EducareWarning, Modifier.weight(1f))
                AttendanceMiniKpi("Rate", "${"%.0f".format(attendancePercentage)}%", EducareNavyPrimary, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (classStudents.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.EventBusy,
                    title = "No Students in $selectedClass",
                    message = "Switch to another class or add students from the Student Management section."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(classStudents, key = { it.id }) { student ->
                        val currentStatus = attendanceMap[student.id] ?: "PRESENT"

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(EducareNavyPrimary.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = student.rollNo,
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = EducareNavyPrimary
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Text(
                                            text = student.fullName,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = "Father: ${student.fatherName}",
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                        )
                                    }
                                }

                                // Status toggle buttons: P, A, L
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    StatusToggleButton(
                                        label = "P",
                                        isSelected = currentStatus == "PRESENT",
                                        color = EducareSuccess,
                                        onClick = { attendanceMap[student.id] = "PRESENT" }
                                    )
                                    StatusToggleButton(
                                        label = "A",
                                        isSelected = currentStatus == "ABSENT",
                                        color = EducareError,
                                        onClick = { attendanceMap[student.id] = "ABSENT" }
                                    )
                                    StatusToggleButton(
                                        label = "L",
                                        isSelected = currentStatus == "LEAVE",
                                        color = EducareWarning,
                                        onClick = { attendanceMap[student.id] = "LEAVE" }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusToggleButton(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        color = if (isSelected) color else color.copy(alpha = 0.12f),
        shape = CircleShape
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else color
                )
            )
        }
    }
}

@Composable
private fun AttendanceMiniKpi(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = color))
            Text(label, style = MaterialTheme.typography.labelSmall.copy(color = color, fontSize = 10.sp))
        }
    }
}
