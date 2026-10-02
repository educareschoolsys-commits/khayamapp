package com.example.ui.screens.website

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.*
import com.example.ui.components.EducareBadge
import com.example.ui.theme.*

enum class WebsiteSection {
    HOME,
    ABOUT,
    ADMISSIONS,
    ACADEMICS,
    FACULTY,
    ACTIVITIES,
    CONTACT
}

@Composable
fun PublicWebsiteScreen(
    teachers: List<TeacherEntity>,
    announcements: List<AnnouncementEntity>,
    gallery: List<GalleryItemEntity>,
    achievements: List<AchievementEntity>,
    onSubmitAdmission: (AdmissionApplicationEntity, (String) -> Unit) -> Unit,
    onNavigateToLogin: (UserRole?) -> Unit,
    onBack: () -> Unit
) {
    var currentSection by remember { mutableStateOf(WebsiteSection.HOME) }
    var showAdmissionDialog by remember { mutableStateOf(false) }
    var generatedAppNumber by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            Surface(
                color = EducareNavyPrimary,
                tonalElevation = 6.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onBack, modifier = Modifier.testTag("website_back_button")) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                            }
                            Surface(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, EducareGold, CircleShape),
                                color = Color.White
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_school_logo),
                                    contentDescription = "Logo",
                                    modifier = Modifier.padding(2.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Educare School System",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "Chagmalai Campus",
                                    style = MaterialTheme.typography.labelSmall.copy(color = EducareGoldLight)
                                )
                            }
                        }

                        Button(
                            onClick = { onNavigateToLogin(null) },
                            colors = ButtonDefaults.buttonColors(containerColor = EducareGold),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("website_login_button")
                        ) {
                            Icon(Icons.Default.Login, contentDescription = null, tint = EducareNavyDark, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Portals", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyDark))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Website Navigation Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(WebsiteSection.values()) { sec ->
                            FilterChip(
                                selected = currentSection == sec,
                                onClick = { currentSection = sec },
                                label = {
                                    Text(
                                        text = sec.name.replace("_", " "),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EducareGold,
                                    selectedLabelColor = EducareNavyDark,
                                    containerColor = EducareNavyLight.copy(alpha = 0.5f),
                                    labelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentSection) {
                WebsiteSection.HOME -> HomeSectionView(
                    onApplyNow = { showAdmissionDialog = true },
                    onNavigateToLogin = onNavigateToLogin,
                    gallery = gallery,
                    achievements = achievements,
                    announcements = announcements
                )
                WebsiteSection.ABOUT -> AboutSectionView()
                WebsiteSection.ADMISSIONS -> AdmissionsSectionView(onOpenForm = { showAdmissionDialog = true })
                WebsiteSection.ACADEMICS -> AcademicsSectionView()
                WebsiteSection.FACULTY -> FacultySectionView(teachers = teachers)
                WebsiteSection.ACTIVITIES -> ActivitiesSectionView(gallery = gallery, achievements = achievements)
                WebsiteSection.CONTACT -> ContactSectionView()
            }
        }
    }

    if (showAdmissionDialog) {
        OnlineAdmissionDialog(
            onDismiss = { showAdmissionDialog = false },
            onSubmit = { app ->
                onSubmitAdmission(app) { generatedNo ->
                    generatedAppNumber = generatedNo
                    showAdmissionDialog = false
                }
            }
        )
    }

    if (generatedAppNumber != null) {
        AlertDialog(
            onDismissRequest = { generatedAppNumber = null },
            title = { Text("Application Submitted Successfully!") },
            text = {
                Text("Your Online Admission Application Number is:\n\n$generatedAppNumber\n\nPlease save this number for future reference. Educare School System administration will contact you shortly.")
            },
            confirmButton = {
                Button(
                    onClick = { generatedAppNumber = null },
                    colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary)
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun HomeSectionView(
    onApplyNow: () -> Unit,
    onNavigateToLogin: (UserRole?) -> Unit,
    gallery: List<GalleryItemEntity>,
    achievements: List<AchievementEntity>,
    announcements: List<AnnouncementEntity>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Campus Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_school_hero),
                contentDescription = "Educare School Campus",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = EducareNavyDark.copy(alpha = 0.65f)
            ) {}

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Educare School System Chagmalai",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "ہم تراشتے ہیں آپ کے بچوں کا مستقبل",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = EducareGoldLight,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "We Shape Your Children's Future",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.85f),
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onApplyNow,
                        colors = ButtonDefaults.buttonColors(containerColor = EducareGold),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("hero_apply_now_button")
                    ) {
                        Text("Apply for Admission", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyDark))
                    }
                }
            }
        }

        // Quick Portal Buttons
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Online Portals",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PortalQuickButton("Staff Portal", Icons.Default.School, Modifier.weight(1f)) {
                    onNavigateToLogin(null)
                }
                PortalQuickButton("Parent Portal", Icons.Default.FamilyRestroom, Modifier.weight(1f)) {
                    onNavigateToLogin(null)
                }
                PortalQuickButton("Student Portal", Icons.Default.Person, Modifier.weight(1f)) {
                    onNavigateToLogin(null)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Recent Announcements
            Text(
                text = "Important Announcements & Circulars",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary)
            )
            Spacer(modifier = Modifier.height(8.dp))

            announcements.take(2).forEach { ann ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(ann.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            EducareBadge(text = ann.type, type = if (ann.isImportant) "ERROR" else "INFO")
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(ann.message, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155)))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Published: ${ann.date}", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Student Achievements Spotlight
            Text(
                text = "Student Achievements Spotlight",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary)
            )
            Spacer(modifier = Modifier.height(8.dp))

            achievements.forEach { ach ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = EducareNavyPrimary.copy(alpha = 0.05f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = EducareGold,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = EducareNavyDark, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(ach.achievementTitle, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text("${ach.studentName} (${ach.className}) - ${ach.position}", style = MaterialTheme.typography.bodySmall.copy(color = EducareNavyPrimary))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PortalQuickButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = EducareNavyPrimary),
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(icon, contentDescription = null, tint = EducareNavyPrimary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.height(4.dp))
                Text(title, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun AboutSectionView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("About Educare School System Chagmalai", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Educare School System Chagmalai is committed to academic distinction, moral development, and holistic character building for students from early childhood through secondary education.\n\nOur motto 'ہم تراشتے ہیں آپ کے بچوں کا مستقبل' reflects our core vision of molding tomorrow's leaders, scholars, and responsible citizens with contemporary science, computer literacy, Islamic values, and analytical thinking.",
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp, color = Color(0xFF334155))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Key Highlights & Facilities", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        listOf(
            "Experienced & Qualified Teaching Faculty",
            "Fully Equipped Science Laboratories (Physics, Chemistry, Biology)",
            "Modern Computer & Robotics Laboratory",
            "Disciplined Academic Environment & Character Grooming",
            "Sports Grounds & Annual Physical Competitions",
            "Continuous Progress Monitoring & Parent-Teacher Collaboration"
        ).forEach { item ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EducareSuccess, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(item, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun AdmissionsSectionView(onOpenForm: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Admissions 2026-2027 Open", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            "Admissions are currently underway from Playgroup/Nursery up to Class 10 (SSC). Parents can submit the online application form below or visit the campus office.",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF334155))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onOpenForm,
            colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Description, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Fill Online Admission Application", style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontWeight = FontWeight.Bold))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Admission Criteria & Required Documents:", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Spacer(modifier = Modifier.height(8.dp))

        listOf(
            "Child's NADRA B-Form or Birth Certificate copy",
            "Father / Guardian CNIC copy",
            "4 Passport-sized recent photographs with blue background",
            "School Leaving Certificate (if transferring from previous school)",
            "Previous Class Report Card / Progress Certificate"
        ).forEach { doc ->
            Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.TaskAlt, contentDescription = null, tint = EducareNavyPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(doc, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun AcademicsSectionView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Academics & Curricula", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Educare School System follows standard KP Board of Intermediate and Secondary Education (BISE) curriculum enriched with English language proficiency, conceptual mathematics, and scientific experiments.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(14.dp))

        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Pre-School & Primary Wing (Nursery - Class 5)", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
                Text("Activity-based phonics, handwriting, numeracy, and creative art.", style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Middle Wing (Class 6 - Class 8)", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
                Text("Focus on General Science, Mathematics problem solving, Urdu/English literature, and Computer fundamentals.", style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Secondary Wing (Class 9 - Class 10 / SSC)", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
                Text("Science Group: Physics, Chemistry, Biology/Computer Science, Mathematics. Board examination preparation with 100% past result pass rate.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun FacultySectionView(teachers: List<TeacherEntity>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Our Experienced Faculty", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        teachers.forEach { t ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
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
                    Column {
                        Text(t.fullName, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text(t.designation, style = MaterialTheme.typography.bodySmall.copy(color = EducareGoldDark))
                        Text(t.qualification, style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                        Text("Subjects: ${t.assignedSubjects}", style = MaterialTheme.typography.labelSmall.copy(color = EducareNavyPrimary))
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivitiesSectionView(
    gallery: List<GalleryItemEntity>,
    achievements: List<AchievementEntity>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("School Activities & Gallery", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        gallery.forEach { item ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(item.title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        EducareBadge(text = item.category, type = "INFO")
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(item.description, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155)))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Date: ${item.date}", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                }
            }
        }
    }
}

@Composable
private fun ContactSectionView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Contact Educare School System", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EducareNavyPrimary))
        Spacer(modifier = Modifier.height(8.dp))

        Card(shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Campus Address:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                Text("Main Campus, Chagmalai, Khyber Pakhtunkhwa", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))

                Spacer(modifier = Modifier.height(10.dp))
                Text("Phone Numbers:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                Text("+92 300 1234567 | +92 312 1234567", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))

                Spacer(modifier = Modifier.height(10.dp))
                Text("WhatsApp Inquiries:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                Text("+92 300 1234567", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, color = EducareSuccess))

                Spacer(modifier = Modifier.height(10.dp))
                Text("Email Address:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                Text("educareschoolsys@gmail.com", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))

                Spacer(modifier = Modifier.height(10.dp))
                Text("Office Hours:", style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray))
                Text("Monday to Saturday: 8:00 AM - 2:00 PM (Friday: 8:00 AM - 12:30 PM)", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun OnlineAdmissionDialog(
    onDismiss: () -> Unit,
    onSubmit: (AdmissionApplicationEntity) -> Unit
) {
    var studentName by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var motherName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("2012-05-15") }
    var gender by remember { mutableStateOf("Male") }
    var applyingClass by remember { mutableStateOf("Class 8") }
    var phone by remember { mutableStateOf("03001234567") }
    var whatsapp by remember { mutableStateOf("03001234567") }
    var address by remember { mutableStateOf("Chagmalai") }
    var previousSchool by remember { mutableStateOf("") }

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
                    text = "Online Admission Application",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EducareNavyPrimary
                    )
                )

                if (errorMsg.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMsg, color = EducareError, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = studentName,
                    onValueChange = { studentName = it },
                    label = { Text("Student Full Name *") },
                    modifier = Modifier.fillMaxWidth().testTag("admission_form_name"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = fatherName,
                    onValueChange = { fatherName = it },
                    label = { Text("Father / Guardian Name *") },
                    modifier = Modifier.fillMaxWidth().testTag("admission_form_father"),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = applyingClass,
                        onValueChange = { applyingClass = it },
                        label = { Text("Applying Class *") },
                        modifier = Modifier.weight(1f).testTag("admission_form_class"),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = gender,
                        onValueChange = { gender = it },
                        label = { Text("Gender") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = dob,
                    onValueChange = { dob = it },
                    label = { Text("Date of Birth (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Contact Phone *") },
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
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Residential Address *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = previousSchool,
                    onValueChange = { previousSchool = it },
                    label = { Text("Previous School Name") },
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
                            if (studentName.isBlank() || fatherName.isBlank() || applyingClass.isBlank() || phone.isBlank()) {
                                errorMsg = "Please fill in all required fields (*)"
                            } else {
                                val appNumber = "ADM-2026-${(100..999).random()}"
                                onSubmit(
                                    AdmissionApplicationEntity(
                                        applicationNumber = appNumber,
                                        studentName = studentName.trim(),
                                        fatherName = fatherName.trim(),
                                        motherName = motherName.trim(),
                                        dob = dob.trim(),
                                        gender = gender.trim(),
                                        applyingClass = applyingClass.trim(),
                                        phone = phone.trim(),
                                        whatsapp = whatsapp.trim(),
                                        address = address.trim(),
                                        previousSchool = previousSchool.trim(),
                                        status = "PENDING",
                                        submissionDate = "2026-10-02"
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EducareNavyPrimary),
                        modifier = Modifier.weight(1f).testTag("submit_admission_button")
                    ) {
                        Text("Submit Form")
                    }
                }
            }
        }
    }
}
