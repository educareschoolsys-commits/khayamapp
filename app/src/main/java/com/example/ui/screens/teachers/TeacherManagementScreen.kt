package com.example.ui.screens.teachers

import androidx.compose.foundation.background
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.TeacherEntity
import com.example.ui.components.EducareBadge
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.theme.*

@Composable
fun TeacherManagementScreen(
    teachers: List<TeacherEntity>,
    onSaveTeacher: (TeacherEntity, Boolean) -> Unit,
    onDeleteTeacher: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var editingTeacher by remember { mutableStateOf<TeacherEntity?>(null) }
    var isNewTeacher by remember { mutableStateOf(false) }

    val filteredTeachers = remember(teachers, searchQuery) {
        val query = searchQuery.trim().lowercase()
        if (query.isEmpty()) teachers
        else teachers.filter {
            it.fullName.lowercase().contains(query) ||
                    it.assignedSubjects.lowercase().contains(query) ||
                    it.designation.lowercase().contains(query) ||
                    it.phone.contains(query)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingTeacher = TeacherEntity(
                        fullName = "",
                        email = "",
                        phone = "03001234567",
                        qualification = "M.Sc",
                        designation = "Subject Lecturer",
                        assignedClasses = "Class 9, Class 10",
                        assignedSubjects = "Science",
                        joiningDate = "2026-10-01",
                        status = "Active"
                    )
                    isNewTeacher = true
                },
                containerColor = EducareNavyPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_teacher_fab")
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Add Faculty")
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
            Text(
                text = "Faculty & Teacher Management",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = EducareNavyPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().testTag("teacher_search_input"),
                placeholder = { Text("Search by teacher name or subject...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EducareNavyPrimary) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (filteredTeachers.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.Badge,
                    title = "No Teachers Found",
                    message = "Tap + to add a teacher to the faculty directory."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredTeachers, key = { it.id }) { teacher ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth().testTag("teacher_card_${teacher.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(EducareNavyPrimary.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = EducareNavyPrimary, modifier = Modifier.size(26.dp))
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(teacher.fullName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                            EducareBadge(text = teacher.status, type = teacher.status)
                                        }
                                        Text(teacher.designation, style = MaterialTheme.typography.bodySmall.copy(color = EducareGoldDark, fontWeight = FontWeight.SemiBold))
                                        Text(teacher.qualification, style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                                    }

                                    IconButton(onClick = {
                                        editingTeacher = teacher
                                        isNewTeacher = false
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = EducareNavyPrimary, modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(onClick = { onDeleteTeacher(teacher.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = EducareError, modifier = Modifier.size(18.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = EducareOutline.copy(alpha = 0.3f))
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Classes: ${teacher.assignedClasses}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155))
                                    )
                                    Text(
                                        text = "Subjects: ${teacher.assignedSubjects}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = EducareNavyPrimary, fontWeight = FontWeight.Medium)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Phone: ${teacher.phone} | Email: ${teacher.email}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (editingTeacher != null) {
        TeacherFormDialog(
            teacher = editingTeacher!!,
            isNew = isNewTeacher,
            onDismiss = { editingTeacher = null },
            onSave = { saved ->
                onSaveTeacher(saved, isNewTeacher)
                editingTeacher = null
            }
        )
    }
}

@Composable
private fun TeacherFormDialog(
    teacher: TeacherEntity,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (TeacherEntity) -> Unit
) {
    var fullName by remember { mutableStateOf(teacher.fullName) }
    var designation by remember { mutableStateOf(teacher.designation) }
    var qualification by remember { mutableStateOf(teacher.qualification) }
    var assignedClasses by remember { mutableStateOf(teacher.assignedClasses) }
    var assignedSubjects by remember { mutableStateOf(teacher.assignedSubjects) }
    var phone by remember { mutableStateOf(teacher.phone) }
    var email by remember { mutableStateOf(teacher.email) }

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
                    text = if (isNew) "Add Faculty Member" else "Edit Teacher Profile",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EducareNavyPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name *") },
                    modifier = Modifier.fillMaxWidth().testTag("teacher_name_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = designation,
                    onValueChange = { designation = it },
                    label = { Text("Designation") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = qualification,
                    onValueChange = { qualification = it },
                    label = { Text("Academic Qualification") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = assignedClasses,
                    onValueChange = { assignedClasses = it },
                    label = { Text("Assigned Classes (e.g. Class 9, Class 10)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = assignedSubjects,
                    onValueChange = { assignedSubjects = it },
                    label = { Text("Assigned Subjects (e.g. Physics, Math)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Contact Phone *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

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
                            if (fullName.isNotBlank()) {
                                onSave(
                                    teacher.copy(
                                        fullName = fullName.trim(),
                                        designation = designation.trim(),
                                        qualification = qualification.trim(),
                                        assignedClasses = assignedClasses.trim(),
                                        assignedSubjects = assignedSubjects.trim(),
                                        phone = phone.trim(),
                                        email = email.trim()
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1f).testTag("save_teacher_button")
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
