package com.example.ui.screens.announcements

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
import com.example.data.model.AnnouncementEntity
import com.example.ui.components.EducareBadge
import com.example.ui.components.EmptyPlaceholder
import com.example.ui.theme.*

@Composable
fun AnnouncementsScreen(
    announcements: List<AnnouncementEntity>,
    canCreate: Boolean = true,
    onSendAnnouncement: (AnnouncementEntity) -> Unit,
    onDeleteAnnouncement: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            if (canCreate) {
                FloatingActionButton(
                    onClick = { showCreateDialog = true },
                    containerColor = EducareNavyPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("send_announcement_fab")
                ) {
                    Icon(Icons.Default.Campaign, contentDescription = "New Announcement")
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
            Text(
                text = "Announcements & School Circulars",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = EducareNavyPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (announcements.isEmpty()) {
                EmptyPlaceholder(
                    icon = Icons.Default.Campaign,
                    title = "No Announcements Yet",
                    message = "Tap the + button to broadcast a circular to parents, teachers, or students."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(announcements, key = { it.id }) { ann ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth().testTag("announcement_item_${ann.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        EducareBadge(text = ann.type, type = if (ann.isImportant) "ERROR" else "INFO")
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = EducareNavyPrimary.copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                text = "Target: ${ann.targetRole}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    if (canCreate) {
                                        IconButton(onClick = { onDeleteAnnouncement(ann.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = EducareError, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = ann.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (ann.isImportant) EducareNavyPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = ann.message,
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF334155))
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "Date Published: ${ann.date}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateAnnouncementDialog(
            onDismiss = { showCreateDialog = false },
            onSave = {
                onSendAnnouncement(it)
                showCreateDialog = false
            }
        )
    }
}

@Composable
private fun CreateAnnouncementDialog(
    onDismiss: () -> Unit,
    onSave: (AnnouncementEntity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var targetRole by remember { mutableStateOf("ALL") }
    var type by remember { mutableStateOf("GENERAL") }
    var isImportant by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Broadcast Announcement",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EducareNavyPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title *") },
                    modifier = Modifier.fillMaxWidth().testTag("announcement_title_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Announcement Details *") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Target Role
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("ALL", "PARENT", "TEACHER", "STUDENT").forEach { role ->
                        FilterChip(
                            selected = targetRole == role,
                            onClick = { targetRole = role },
                            label = { Text(role, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isImportant,
                        onCheckedChange = { isImportant = it }
                    )
                    Text("Mark as High Priority / Urgent Notice", style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (title.isNotBlank() && message.isNotBlank()) {
                                onSave(
                                    AnnouncementEntity(
                                        title = title.trim(),
                                        message = message.trim(),
                                        targetRole = targetRole,
                                        targetClass = "",
                                        date = "2026-10-02",
                                        type = if (isImportant) "EMERGENCY" else type,
                                        isImportant = isImportant
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1f).testTag("publish_announcement_button")
                    ) {
                        Text("Publish")
                    }
                }
            }
        }
    }
}
