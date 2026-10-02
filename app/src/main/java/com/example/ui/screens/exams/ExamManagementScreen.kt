package com.example.ui.screens.exams

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ExamEntity
import com.example.data.model.ExamMarkEntity
import com.example.data.model.StudentEntity
import com.example.ui.components.EducareBadge
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.theme.*

@Composable
fun ExamManagementScreen(
    exams: List<ExamEntity>,
    students: List<StudentEntity>,
    onCreateExam: (ExamEntity) -> Unit,
    onSaveMarks: (List<ExamMarkEntity>) -> Unit,
    onViewResultCard: (StudentEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateExamDialog by remember { mutableStateOf(false) }
    var enteringMarksForStudent by remember { mutableStateOf<StudentEntity?>(null) }
    var selectedClass by remember { mutableStateOf("Class 10") }

    val classStudents = remember(students, selectedClass) {
        students.filter { it.className.equals(selectedClass, ignoreCase = true) }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateExamDialog = true },
                containerColor = EducareNavyPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("create_exam_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Exam")
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
            // Active Exams Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = EducareNavyPrimary.copy(alpha = 0.08f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Examinations & Grading",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = EducareNavyPrimary
                            )
                        )
                        EducareBadge(text = "${exams.size} Scheduled", type = "INFO")
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    exams.forEach { ex ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(ex.examName, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            Text(ex.session, style = MaterialTheme.typography.bodySmall.copy(color = EducareGoldDark))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Class Selector for Entering / Viewing Results
            Text(
                text = "Select Class to Manage Marks:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(6.dp))

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

            if (classStudents.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.Assessment,
                    title = "No Students Found",
                    message = "No students are enrolled in $selectedClass yet."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(classStudents, key = { it.id }) { student ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth().testTag("exam_student_${student.rollNo}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = student.fullName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Roll No: ${student.rollNo} | ${student.className} (${student.section})",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                    )
                                    Text(
                                        text = "Father: ${student.fatherName}",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    OutlinedButton(
                                        onClick = { enteringMarksForStudent = student },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Icon(Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Enter Marks")
                                    }

                                    Button(
                                        onClick = { onViewResultCard(student) },
                                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.testTag("view_result_card_${student.rollNo}")
                                    ) {
                                        Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Result Card")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Create Exam Dialog
    if (showCreateExamDialog) {
        CreateExamDialog(
            onDismiss = { showCreateExamDialog = false },
            onSave = { exam ->
                onCreateExam(exam)
                showCreateExamDialog = false
            }
        )
    }

    // Enter Marks Dialog
    if (enteringMarksForStudent != null) {
        EnterMarksDialog(
            student = enteringMarksForStudent!!,
            exam = exams.firstOrNull() ?: ExamEntity(examName = "First Term Examination 2026", startDate = "2026-09-15", endDate = "2026-09-25", classesCovered = "All"),
            onDismiss = { enteringMarksForStudent = null },
            onSaveMarks = { marks ->
                onSaveMarks(marks)
                enteringMarksForStudent = null
            }
        )
    }
}

@Composable
private fun CreateExamDialog(
    onDismiss: () -> Unit,
    onSave: (ExamEntity) -> Unit
) {
    var examName by remember { mutableStateOf("Second Term Examination 2026") }
    var session by remember { mutableStateOf("2026-2027") }
    var startDate by remember { mutableStateOf("2026-11-15") }
    var endDate by remember { mutableStateOf("2026-11-25") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Schedule Examination",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EducareNavyPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = examName,
                    onValueChange = { examName = it },
                    label = { Text("Exam Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = session,
                    onValueChange = { session = it },
                    label = { Text("Academic Session") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Start Date") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text("End Date") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (examName.isNotBlank()) {
                                onSave(
                                    ExamEntity(
                                        examName = examName.trim(),
                                        session = session.trim(),
                                        startDate = startDate,
                                        endDate = endDate,
                                        classesCovered = "All Classes"
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Create")
                    }
                }
            }
        }
    }
}

@Composable
private fun EnterMarksDialog(
    student: StudentEntity,
    exam: ExamEntity,
    onDismiss: () -> Unit,
    onSaveMarks: (List<ExamMarkEntity>) -> Unit
) {
    val subjects = listOf("Mathematics", "Physics", "Chemistry", "English", "Urdu", "Islamiat")
    val marksState = remember {
        mutableStateMapOf<String, String>().apply {
            subjects.forEach { put(it, "85") }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Text(
                    text = "Enter Examination Marks",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EducareNavyPrimary
                    )
                )
                Text(
                    text = "${student.fullName} (Roll: ${student.rollNo}) - ${exam.examName}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )

                Spacer(modifier = Modifier.height(14.dp))

                subjects.forEach { subj ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(subj, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), modifier = Modifier.weight(1f))

                        OutlinedTextField(
                            value = marksState[subj] ?: "",
                            onValueChange = { marksState[subj] = it },
                            label = { Text("Obt/100") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.width(100.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            val markEntities = subjects.map { subj ->
                                val obt = (marksState[subj]?.toDoubleOrNull() ?: 0.0).coerceIn(0.0, 100.0)
                                val grade = when {
                                    obt >= 90.0 -> "A+"
                                    obt >= 80.0 -> "A"
                                    obt >= 70.0 -> "B"
                                    obt >= 60.0 -> "C"
                                    obt >= 50.0 -> "D"
                                    else -> "F"
                                }
                                ExamMarkEntity(
                                    examId = exam.id,
                                    examName = exam.examName,
                                    studentId = student.id,
                                    studentName = student.fullName,
                                    rollNo = student.rollNo,
                                    className = student.className,
                                    section = student.section,
                                    subjectName = subj,
                                    totalMarks = 100.0,
                                    obtainedMarks = obt,
                                    grade = grade
                                )
                            }
                            onSaveMarks(markEntities)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1f).testTag("save_marks_button")
                    ) {
                        Text("Save Marks")
                    }
                }
            }
        }
    }
}
