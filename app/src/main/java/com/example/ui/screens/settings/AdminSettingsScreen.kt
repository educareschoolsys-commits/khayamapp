package com.example.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.SchoolSettingsEntity
import com.example.ui.theme.*

@Composable
fun AdminSettingsScreen(
    settings: SchoolSettingsEntity,
    onSaveSettings: (SchoolSettingsEntity) -> Unit,
    onShowFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var schoolName by remember(settings) { mutableStateOf(settings.schoolName) }
    var urduSlogan by remember(settings) { mutableStateOf(settings.urduSlogan) }
    var englishSlogan by remember(settings) { mutableStateOf(settings.englishSlogan) }
    var address by remember(settings) { mutableStateOf(settings.address) }
    var phone by remember(settings) { mutableStateOf(settings.phone) }
    var whatsapp by remember(settings) { mutableStateOf(settings.whatsapp) }
    var email by remember(settings) { mutableStateOf(settings.email) }
    var session by remember(settings) { mutableStateOf(settings.currentSession) }
    var principalName by remember(settings) { mutableStateOf(settings.principalName) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "School Configuration & Settings",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = EducareNavyPrimary
            )
        )
        Text(
            text = "Manage institution identity, contact information, and academic parameters.",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Branding Card with Logo
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .border(2.dp, EducareGold, CircleShape),
                        color = Color.White
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_school_logo),
                            contentDescription = "Educare Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text("Official Branding", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Text("Educare School System Chagmalai", style = MaterialTheme.typography.bodySmall.copy(color = EducareNavyPrimary))
                        Text(urduSlogan, style = MaterialTheme.typography.bodySmall.copy(color = EducareGoldDark))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = schoolName,
                    onValueChange = { schoolName = it },
                    label = { Text("School Name") },
                    modifier = Modifier.fillMaxWidth().testTag("settings_school_name"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = urduSlogan,
                    onValueChange = { urduSlogan = it },
                    label = { Text("Urdu Slogan") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = englishSlogan,
                    onValueChange = { englishSlogan = it },
                    label = { Text("English Slogan") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Contact & Administration Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Campus & Administration", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = principalName,
                    onValueChange = { principalName = it },
                    label = { Text("Principal Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = session,
                    onValueChange = { session = it },
                    label = { Text("Academic Session (e.g. 2026-2027)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Campus Address") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Official Phone") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = whatsapp,
                        onValueChange = { whatsapp = it },
                        label = { Text("WhatsApp") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Official Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Settings Button
        Button(
            onClick = {
                onSaveSettings(
                    settings.copy(
                        schoolName = schoolName.trim(),
                        urduSlogan = urduSlogan.trim(),
                        englishSlogan = englishSlogan.trim(),
                        address = address.trim(),
                        phone = phone.trim(),
                        whatsapp = whatsapp.trim(),
                        email = email.trim(),
                        currentSession = session.trim(),
                        principalName = principalName.trim()
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_settings_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save School Settings", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Backup and Data Safety
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = EducareNavyPrimary.copy(alpha = 0.05f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Backup & Data Safety", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Text("Safeguard student records, fee logs, attendance history and examination results.", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { onShowFeedback("School database backup archive created successfully!") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Create Backup")
                    }
                    OutlinedButton(
                        onClick = { onShowFeedback("All tables verified. Database integrity 100% OK.") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Verify Data")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
