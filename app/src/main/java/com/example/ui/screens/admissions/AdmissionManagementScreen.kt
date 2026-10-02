package com.example.ui.screens.admissions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.AdmissionApplicationEntity
import com.example.ui.components.EducareBadge
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.theme.*

@Composable
fun AdmissionManagementScreen(
    admissions: List<AdmissionApplicationEntity>,
    onApprove: (AdmissionApplicationEntity) -> Unit,
    onReject: (AdmissionApplicationEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("ALL") }
    var viewingAdmission by remember { mutableStateOf<AdmissionApplicationEntity?>(null) }

    val filteredList = remember(admissions, selectedFilter) {
        if (selectedFilter == "ALL") admissions
        else admissions.filter { it.status.equals(selectedFilter, ignoreCase = true) }
    }

    val pendingCount = admissions.count { it.status == "PENDING" }
    val approvedCount = admissions.count { it.status == "APPROVED" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Summary Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = EducareNavyPrimary.copy(alpha = 0.08f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$pendingCount", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EducareWarning))
                    Text("Pending Review", style = MaterialTheme.typography.labelSmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$approvedCount", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EducareSuccess))
                    Text("Approved & Enrolled", style = MaterialTheme.typography.labelSmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${admissions.size}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
                    Text("Total Received", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filter chips
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("ALL", "PENDING", "APPROVED", "REJECTED").forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EducareNavyPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredList.isEmpty()) {
            EmptyPlaceholder(
                icon = Icons.Default.HowToReg,
                title = "No Applications",
                message = "New admission submissions from the public website will appear here."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredList, key = { it.id }) { app ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth().testTag("admission_card_${app.applicationNumber}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = app.studentName,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Applying for: ${app.applyingClass} | App No: ${app.applicationNumber}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = EducareNavyPrimary, fontWeight = FontWeight.SemiBold)
                                    )
                                }
                                EducareBadge(text = app.status, type = app.status)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Father: ${app.fatherName} | Contact: ${app.phone}",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155))
                            )
                            Text(
                                text = "Address: ${app.address}",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = EducareOutline.copy(alpha = 0.3f))
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Date: ${app.submissionDate}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    if (app.status == "PENDING") {
                                        OutlinedButton(
                                            onClick = { onReject(app) },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = EducareError),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text("Reject")
                                        }

                                        Button(
                                            onClick = { onApprove(app) },
                                            colors = ButtonDefaults.buttonColors(containerColor = EducareSuccess),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                            modifier = Modifier.testTag("approve_admission_${app.applicationNumber}")
                                        ) {
                                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Approve & Enroll")
                                        }
                                    } else {
                                        TextButton(onClick = { viewingAdmission = app }) {
                                            Text("View Details")
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

    if (viewingAdmission != null) {
        AdmissionDetailDialog(
            app = viewingAdmission!!,
            onDismiss = { viewingAdmission = null }
        )
    }
}

@Composable
private fun AdmissionDetailDialog(
    app: AdmissionApplicationEntity,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Admission Application",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EducareNavyPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text("Application Number: ${app.applicationNumber}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                Text("Applicant: ${app.studentName}", style = MaterialTheme.typography.bodyMedium)
                Text("Father Name: ${app.fatherName}", style = MaterialTheme.typography.bodyMedium)
                Text("Applying Class: ${app.applyingClass}", style = MaterialTheme.typography.bodyMedium)
                Text("Date of Birth: ${app.dob} (${app.gender})", style = MaterialTheme.typography.bodyMedium)
                Text("Contact Phone: ${app.phone}", style = MaterialTheme.typography.bodyMedium)
                Text("Residential Address: ${app.address}", style = MaterialTheme.typography.bodyMedium)
                Text("Previous School: ${app.previousSchool.ifBlank { "N/A" }}", style = MaterialTheme.typography.bodyMedium)
                Text("Status: ${app.status}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary)
                ) {
                    Text("Close")
                }
            }
        }
    }
}
