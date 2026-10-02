package com.example.ui.screens.students

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.StudentEntity
import com.example.ui.components.EducareBadge
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.theme.*

@Composable
fun StudentManagementScreen(
    students: List<StudentEntity>,
    onSaveStudent: (StudentEntity, Boolean) -> Unit,
    onDeleteStudent: (Long) -> Unit,
    onViewResultCard: (StudentEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedClass by remember { mutableStateOf("All") }
    var editingStudent by remember { mutableStateOf<StudentEntity?>(null) }
    var isNewStudent by remember { mutableStateOf(false) }
    var selectedStudentForDetail by remember { mutableStateOf<StudentEntity?>(null) }
    var studentToDelete by remember { mutableStateOf<StudentEntity?>(null) }

    val classesList = listOf("All", "Class 10", "Class 9", "Class 8", "Class 7", "Class 6", "Nursery", "Prep")

    val filteredStudents = remember(students, searchQuery, selectedClass) {
        students.filter { s ->
            val matchesClass = selectedClass == "All" || s.className.equals(selectedClass, ignoreCase = true)
            val query = searchQuery.trim().lowercase()
            val matchesQuery = query.isEmpty() ||
                    s.fullName.lowercase().contains(query) ||
                    s.rollNo.lowercase().contains(query) ||
                    s.admissionNo.lowercase().contains(query) ||
                    s.fatherName.lowercase().contains(query) ||
                    s.phone.contains(query)
            matchesClass && matchesQuery
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingStudent = StudentEntity(
                        admissionNo = "ADM-2026-${(100..999).random()}",
                        rollNo = "${(students.size + 101)}",
                        fullName = "",
                        fatherName = "",
                        dob = "2010-01-01",
                        gender = "Male",
                        className = if (selectedClass != "All") selectedClass else "Class 10",
                        section = "A",
                        phone = "03001234567",
                        address = "Chagmalai",
                        admissionDate = "2026-10-01",
                        status = "Active"
                    )
                    isNewStudent = true
                },
                containerColor = EducareNavyPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_student_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Student")
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
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("student_search_input"),
                placeholder = { Text("Search by name, roll no, admission no...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EducareNavyPrimary) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Class Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(classesList) { cls ->
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

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Enrolled: ${filteredStudents.size} students",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredStudents.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.School,
                    title = "No Students Found",
                    message = "Try adjusting your search criteria or add a new student using the + button."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(filteredStudents, key = { it.id }) { student ->
                        StudentCardItem(
                            student = student,
                            onClick = { selectedStudentForDetail = student },
                            onEdit = {
                                editingStudent = student
                                isNewStudent = false
                            },
                            onDelete = { studentToDelete = student },
                            onResultCard = { onViewResultCard(student) }
                        )
                    }
                }
            }
        }
    }

    // Add / Edit Student Dialog
    if (editingStudent != null) {
        StudentFormDialog(
            student = editingStudent!!,
            isNew = isNewStudent,
            onDismiss = { editingStudent = null },
            onSave = { saved ->
                onSaveStudent(saved, isNewStudent)
                editingStudent = null
            }
        )
    }

    // Detail Dialog
    if (selectedStudentForDetail != null) {
        StudentDetailDialog(
            student = selectedStudentForDetail!!,
            onDismiss = { selectedStudentForDetail = null },
            onEdit = {
                editingStudent = selectedStudentForDetail
                isNewStudent = false
                selectedStudentForDetail = null
            },
            onResultCard = {
                onViewResultCard(selectedStudentForDetail!!)
                selectedStudentForDetail = null
            }
        )
    }

    // Delete Confirmation Dialog
    if (studentToDelete != null) {
        AlertDialog(
            onDismissRequest = { studentToDelete = null },
            title = { Text("Delete Student Record") },
            text = { Text("Are you sure you want to delete ${studentToDelete!!.fullName} (Roll: ${studentToDelete!!.rollNo})? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteStudent(studentToDelete!!.id)
                        studentToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EducareError)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { studentToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun StudentCardItem(
    student: StudentEntity,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onResultCard: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("student_card_${student.rollNo}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with Roll No badge
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(EducareNavyPrimary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = EducareNavyPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = student.fullName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        EducareBadge(text = student.status, type = student.status)
                    }

                    Text(
                        text = "Father: ${student.fatherName}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "${student.className} (${student.section})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = EducareNavyPrimary
                            )
                        )
                        Text(
                            text = "Roll: ${student.rollNo}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = EducareGoldDark
                            )
                        )
                        Text(
                            text = student.admissionNo,
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                        )
                    }
                }

                IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = EducareNavyPrimary, modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = EducareError, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = EducareOutline.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Contact: ${student.phone}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                )
                TextButton(
                    onClick = onResultCard,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(14.dp), tint = EducareNavyPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Result Card", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
                }
            }
        }
    }
}

@Composable
private fun StudentDetailDialog(
    student: StudentEntity,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onResultCard: () -> Unit
) {
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Student Profile",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = EducareNavyPrimary
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(EducareNavyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(student.fullName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("Roll No: ${student.rollNo} | ${student.className} (${student.section})", style = MaterialTheme.typography.bodySmall.copy(color = EducareNavyPrimary, fontWeight = FontWeight.SemiBold))
                        EducareBadge(text = student.status, type = student.status)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = EducareOutline)
                Spacer(modifier = Modifier.height(12.dp))

                DetailItem("Father's Name", student.fatherName)
                DetailItem("Admission Number", student.admissionNo)
                DetailItem("Date of Birth", student.dob)
                DetailItem("Gender", student.gender)
                DetailItem("Phone", student.phone)
                DetailItem("WhatsApp", student.whatsapp.ifBlank { student.phone })
                DetailItem("Address", student.address)
                DetailItem("Admission Date", student.admissionDate)
                DetailItem("Previous School", student.previousSchool.ifBlank { "Educare Primary Section" })

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onEdit, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit")
                    }
                    Button(
                        onClick = onResultCard,
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Result")
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
        Text(value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
    }
}

@Composable
private fun StudentFormDialog(
    student: StudentEntity,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (StudentEntity) -> Unit
) {
    var fullName by remember { mutableStateOf(student.fullName) }
    var fatherName by remember { mutableStateOf(student.fatherName) }
    var rollNo by remember { mutableStateOf(student.rollNo) }
    var admissionNo by remember { mutableStateOf(student.admissionNo) }
    var className by remember { mutableStateOf(student.className) }
    var section by remember { mutableStateOf(student.section) }
    var phone by remember { mutableStateOf(student.phone) }
    var address by remember { mutableStateOf(student.address) }
    var gender by remember { mutableStateOf(student.gender) }
    var dob by remember { mutableStateOf(student.dob) }

    var errorMsg by remember { mutableStateOf("") }

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
                    text = if (isNew) "Add New Student" else "Edit Student Record",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EducareNavyPrimary
                    )
                )

                if (errorMsg.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMsg,
                        color = EducareError,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Student Full Name *") },
                    modifier = Modifier.fillMaxWidth().testTag("student_form_name"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = fatherName,
                    onValueChange = { fatherName = it },
                    label = { Text("Father / Guardian Name *") },
                    modifier = Modifier.fillMaxWidth().testTag("student_form_father"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = className,
                        onValueChange = { className = it },
                        label = { Text("Class *") },
                        modifier = Modifier.weight(1f).testTag("student_form_class"),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = section,
                        onValueChange = { section = it },
                        label = { Text("Section") },
                        modifier = Modifier.weight(0.7f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = rollNo,
                        onValueChange = { rollNo = it },
                        label = { Text("Roll No *") },
                        modifier = Modifier.weight(1f).testTag("student_form_roll"),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = admissionNo,
                        onValueChange = { admissionNo = it },
                        label = { Text("Adm No") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Parent Contact Number *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Residential Address") },
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
                            if (fullName.isBlank() || fatherName.isBlank() || rollNo.isBlank() || className.isBlank()) {
                                errorMsg = "Please fill in all mandatory fields (*)"
                            } else {
                                onSave(
                                    student.copy(
                                        fullName = fullName.trim(),
                                        fatherName = fatherName.trim(),
                                        rollNo = rollNo.trim(),
                                        admissionNo = admissionNo.trim(),
                                        className = className.trim(),
                                        section = section.trim().ifBlank { "A" },
                                        phone = phone.trim(),
                                        address = address.trim(),
                                        gender = gender,
                                        dob = dob
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1f).testTag("save_student_button")
                    ) {
                        Text("Save Student")
                    }
                }
            }
        }
    }
}
