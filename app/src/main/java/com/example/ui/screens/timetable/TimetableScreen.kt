package com.example.ui.screens.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Room
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.data.model.TimetableEntity
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.theme.*

@Composable
fun TimetableScreen(
    timetables: List<TimetableEntity>,
    onAddTimetable: (TimetableEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedClass by remember { mutableStateOf("Class 10") }
    var showAddDialog by remember { mutableStateOf(false) }

    val classTimetable = remember(timetables, selectedClass) {
        timetables.filter { it.className.equals(selectedClass, ignoreCase = true) }
            .sortedBy { it.periodNumber }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = EducareNavyPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_period_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Period")
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
                text = "Class Timetable & Daily Schedule",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = EducareNavyPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Class Filter
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

            Spacer(modifier = Modifier.height(12.dp))

            if (classTimetable.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.AccessTime,
                    title = "No Periods Scheduled",
                    message = "Tap + to add a period for $selectedClass."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(classTimetable, key = { it.id }) { period ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = EducareNavyPrimary,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "P${period.periodNumber}",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = period.subjectName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = EducareNavyLight, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(period.teacherName, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155)))
                                    }
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.padding(top = 2.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = EducareGoldDark, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("${period.startTime} - ${period.endTime}", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Room, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(period.roomNumber, style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddPeriodDialog(
            className = selectedClass,
            onDismiss = { showAddDialog = false },
            onSave = {
                onAddTimetable(it)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AddPeriodDialog(
    className: String,
    onDismiss: () -> Unit,
    onSave: (TimetableEntity) -> Unit
) {
    var periodNo by remember { mutableStateOf("1") }
    var subject by remember { mutableStateOf("Physics") }
    var teacher by remember { mutableStateOf("Sir Ahmad Ali") }
    var startTime by remember { mutableStateOf("08:00 AM") }
    var endTime by remember { mutableStateOf("08:45 AM") }
    var room by remember { mutableStateOf("Room 201") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Add Period for $className",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EducareNavyPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("Teacher Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text("Start Time") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text("End Time") },
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
                            onSave(
                                TimetableEntity(
                                    className = className,
                                    section = "A",
                                    dayOfWeek = "Monday",
                                    periodNumber = periodNo.toIntOrNull() ?: 1,
                                    startTime = startTime,
                                    endTime = endTime,
                                    subjectName = subject,
                                    teacherName = teacher,
                                    roomNumber = room
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}
