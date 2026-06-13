package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.*
import com.example.ui.AppViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

enum class AppTab(val title: String, val icon: ImageVector) {
    DASHBOARD("Dashboard", Icons.Default.GridView),
    NOTICES("Notices & AI", Icons.Default.Campaign),
    ACADEMICS("Academics", Icons.Default.School),
    CAREER("Career Hub", Icons.Default.Work),
    VAULT("Vault", Icons.Default.FolderOpen),
    PROJECTS("Portfolio", Icons.Default.Code)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: AppViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                    StudentOsMainScreen(viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudentOsMainScreen(viewModel: AppViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val studentEmail by viewModel.studentEmail.collectAsState()

    if (!isLoggedIn) {
        com.example.ui.LoginScreen(viewModel = viewModel)
        return
    }

    var activeTab by remember { mutableStateOf(AppTab.DASHBOARD) }

    // Collect Room database flows reactively as Compose state
    val notices by viewModel.notices.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val exams by viewModel.exams.collectAsState()
    val attendance by viewModel.attendance.collectAsState()
    val financialReminders by viewModel.financialReminders.collectAsState()
    val careerOpportunities by viewModel.careerOpportunities.collectAsState()
    val documents by viewModel.documents.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val collegeEvents by viewModel.collegeEvents.collectAsState()
    val tasks by viewModel.tasks.collectAsState()

    val isParsing by viewModel.isParsing.collectAsState()
    val parsingError by viewModel.parsingError.collectAsState()
    val parsingSuccess by viewModel.parsingSuccessMessage.collectAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding()
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Student OS Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "STUDENT OS",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.testTag("app_title").weight(1f)
                    )
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "v1.2 AI Active",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    var showLogoutMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(
                            onClick = { showLogoutMenu = true },
                            modifier = Modifier.size(32.dp).testTag("account_profile_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Active user session details",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = showLogoutMenu,
                            onDismissRequest = { showLogoutMenu = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AlternateEmail,
                                        contentDescription = "Session login email details",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                text = { Text(studentEmail, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface) },
                                onClick = {},
                                enabled = false
                            )
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Logout,
                                        contentDescription = "Logout current session",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                text = { Text("Secure Sign Out", fontSize = 12.sp, color = MaterialTheme.colorScheme.error) },
                                onClick = {
                                    showLogoutMenu = false
                                    viewModel.logout()
                                }
                            )
                        }
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f))
            }
        },
        bottomBar = {
            Column {
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    windowInsets = WindowInsets.navigationBars
                ) {
                    AppTab.values().forEach { tab ->
                        NavigationBarItem(
                            selected = activeTab == tab,
                            onClick = { activeTab = tab },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 10.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF001D36),
                                selectedTextColor = Color.White,
                                indicatorColor = Color(0xFFD1E4FF),
                                unselectedIconColor = MaterialTheme.colorScheme.secondary,
                                unselectedTextColor = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                        )
                    )
                )
        ) {
            // Screen router
            AnimatedContent(
                targetState = activeTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "ScreenTransition"
            ) { tab ->
                when (tab) {
                    AppTab.DASHBOARD -> DashboardScreen(
                        notices, assignments, exams, attendance, financialReminders,
                        careerOpportunities, documents, projects, collegeEvents, tasks,
                        onNavigate = { nextTab -> activeTab = nextTab },
                        viewModel = viewModel
                    )
                    AppTab.NOTICES -> NoticesAndAiScreen(notices, viewModel)
                    AppTab.ACADEMICS -> AcademicsScreen(assignments, exams, attendance, viewModel)
                    AppTab.CAREER -> CareerHubScreen(careerOpportunities, collegeEvents, viewModel)
                    AppTab.VAULT -> DocumentVaultScreen(documents, viewModel)
                    AppTab.PROJECTS -> ProjectsPortfolioScreen(projects, tasks, viewModel)
                }
            }

            // Global Parsing States Overlays
            if (isParsing) {
                Dialog(onDismissRequest = {}) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.width(280.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Student OS AI Parsing...",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Reading notice text, analyzing dates, and structuring academic entries in Room database.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Parsing Success Overlay
            parsingSuccess?.let { successText ->
                AlertDialog(
                    onDismissRequest = { viewModel.clearParsingStatus() },
                    icon = { Icon(Icons.Default.Verified, contentDescription = "Success", tint = SuccessColor, modifier = Modifier.size(36.dp)) },
                    title = { Text("AI Extraction Complete", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) },
                    text = {
                        Text(
                            text = successText,
                            fontSize = 14.sp,
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { viewModel.clearParsingStatus() }) {
                            Text("Confirm")
                        }
                    }
                )
            }

            // Parsing Error Overlay
            parsingError?.let { errorText ->
                AlertDialog(
                    onDismissRequest = { viewModel.clearParsingStatus() },
                    icon = { Icon(Icons.Default.Warning, contentDescription = "Warning", tint = ErrorColor, modifier = Modifier.size(36.dp)) },
                    title = { Text("Information Extraction Alert", fontWeight = FontWeight.Bold) },
                    text = {
                        Text(
                            text = errorText,
                            fontSize = 13.sp
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { viewModel.clearParsingStatus() }) {
                            Text("Close")
                        }
                    }
                )
            }
        }
    }
}

// ==========================================
// 1. DASHBOARD SCREEN
// ==========================================
@Composable
fun DashboardScreen(
    notices: List<Notice>,
    assignments: List<Assignment>,
    exams: List<Exam>,
    attendance: List<Attendance>,
    financialReminders: List<FinancialReminder>,
    careerOpportunities: List<CareerOpportunity>,
    documents: List<Document>,
    projects: List<Project>,
    collegeEvents: List<CollegeEvent>,
    tasks: List<Task>,
    onNavigate: (AppTab) -> Unit,
    viewModel: AppViewModel
) {
    val scrollState = rememberScrollState()
    val studentEmail by viewModel.studentEmail.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome and Time block
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome, " + studentEmail.substringBefore("@") + "!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Landed in your academic command center.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    val sdf = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
                    Text(
                        text = sdf.format(Date()),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Semester 5",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }

        // ==========================================
        // COLLEGE PORTAL INTEGRATION CENTER
        // ==========================================
        val isPortalConnected by viewModel.isPortalConnected.collectAsState()
        val portalCmsType by viewModel.portalCmsType.collectAsState()
        val portalUsername by viewModel.portalUsername.collectAsState()
        val isConnectingPortal by viewModel.isConnectingPortal.collectAsState()

        var showPortalDialog by remember { mutableStateOf(false) }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isPortalConnected) Color(0xFF003258).copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, if (isPortalConnected) Color(0xFF90CAF9).copy(alpha = 0.4f) else MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth().testTag("college_portal_widget")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isPortalConnected) Icons.Default.CloudQueue else Icons.Default.CloudSync,
                            contentDescription = "Portal Connection status",
                            tint = if (isPortalConnected) Color(0xFFB4E4A1) else Color(0xFF90CAF9),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = if (isPortalConnected) "Portal Connected (Active Gateway)" else "Sync Academic Portal",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = if (isPortalConnected) Color(0xFFB4E4A1) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (isPortalConnected) {
                        Text(
                            text = "SSL TLS 1.3 SECURE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB4E4A1),
                            modifier = Modifier
                                .background(Color(0xFF004A77), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    } else {
                        Text(
                            text = "UNLINKED",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .background(Color(0xFF1C2024), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (isPortalConnected) {
                    Text(
                        text = "Real-time sync established with " + portalCmsType + ". Live classroom records, financial balances, and assignment deadlines are linked securely under student ID: " + portalUsername + ".",
                        fontSize = 12.sp,
                        color = Color(0xFFE2E2E6)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🟢 Handshake Verified: Sync active",
                            fontSize = 11.sp,
                            color = Color(0xFFB4E4A1)
                        )
                        TextButton(
                            onClick = { viewModel.disconnectPortal() },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFFB4AB))
                        ) {
                            Text("Disconnect Gateway", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    Text(
                        text = "Sync and consolidate your Canvas, Moodle, Blackboard LMS, or Banner ERP accounts with local hardware-secured telemetry tracking.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { showPortalDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF90CAF9),
                            contentColor = Color(0xFF003258)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.align(Alignment.End).testTag("link_portal_button")
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Link, contentDescription = "Link", modifier = Modifier.size(16.dp))
                            Text("E2E Connect Portal", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (showPortalDialog) {
            var selectedLms by remember { mutableStateOf("Canvas LMS") }
            var portalUser by remember { mutableStateOf("student.academic.2026") }
            var portalPass by remember { mutableStateOf("") }
            var lmsError by remember { mutableStateOf<String?>(null) }
            val systemsList = listOf("Canvas LMS", "Moodle Portal", "Blackboard ERP", "Banner Self-Service")

            AlertDialog(
                onDismissRequest = { if (!isConnectingPortal) showPortalDialog = false },
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VpnKey, "Gateway verification", tint = Color(0xFF90CAF9))
                        Text("Connect College Gateway", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Establishes a double-authenticated SSL handshake with your college backbones. Credentials remain client-side encrypted.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text("Select collegiate portal framework:", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            systemsList.forEach { sys ->
                                val selected = selectedLms == sys
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (selected) Color(0xFF90CAF9) else Color(0xFF2F3033))
                                        .border(1.dp, if (selected) Color(0xFF90CAF9) else Color(0xFF44474E), RoundedCornerShape(8.dp))
                                        .clickable { selectedLms = sys }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = sys,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) Color(0xFF003258) else Color(0xFFC4C7C5)
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = portalUser,
                            onValueChange = { portalUser = it },
                            label = { Text("Portal Student ID / Username") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("portal_username_input")
                        )

                        OutlinedTextField(
                            value = portalPass,
                            onValueChange = { portalPass = it },
                            label = { Text("Portal Password / API Token") },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("portal_password_input")
                        )

                        if (lmsError != null) {
                            Text(lmsError ?: "", color = Color(0xFFFFB4AB), fontSize = 11.sp)
                        }

                        if (isConnectingPortal) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF1E2024), RoundedCornerShape(12.dp))
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator(color = Color(0xFF90CAF9), strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                                Text("Securing SSL Session Handshake...", fontSize = 11.sp, color = Color(0xFF90CAF9), fontWeight = FontWeight.Bold)
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Lock, "Handshake active", tint = Color(0xFFB4E4A1), modifier = Modifier.size(10.dp))
                                    Text("Generating ephemeral RSA keys...", fontSize = 9.sp, color = Color(0xFFC4C7C5))
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (portalUser.isBlank()) {
                                lmsError = "Student ID cannot be empty to establish authentic handshakes."
                            } else {
                                lmsError = null
                                viewModel.connectPortal(selectedLms, portalUser)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB4E4A1),
                            contentColor = Color(0xFF003258)
                        ),
                        enabled = !isConnectingPortal,
                        modifier = Modifier.testTag("submit_portal_link_button")
                    ) {
                        Text("Initiate Secure Sync", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showPortalDialog = false },
                        enabled = !isConnectingPortal
                    ) {
                        Text("Cancel", fontSize = 12.sp)
                    }
                },
                containerColor = Color(0xFF1C2024),
                shape = RoundedCornerShape(24.dp)
            )
        }

        LaunchedEffect(isPortalConnected) {
            if (isPortalConnected) {
                showPortalDialog = false
            }
        }

        // Quick Overview Analytics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Attendance Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(96.dp)
                    .clickable { onNavigate(AppTab.ACADEMICS) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Attendance", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                        Icon(Icons.Default.TrendingUp, "Attendance Icon", tint = SuccessColor, modifier = Modifier.size(16.dp))
                    }
                    val totalConducted = attendance.sumOf { it.conducted }
                    val totalAttended = attendance.sumOf { it.attended }
                    val overallPercentage = if (totalConducted > 0) (totalAttended.toFloat() / totalConducted * 100) else 0f
                    Text(
                        text = String.format("%.1f%%", overallPercentage),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (overallPercentage >= 75) SuccessColor else ErrorColor
                    )
                    Text(text = "$totalAttended / $totalConducted total sessions", fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }

            // Pending Deadlines Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(96.dp)
                    .clickable { onNavigate(AppTab.ACADEMICS) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Deadlines", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                        Icon(Icons.Default.HourglassBottom, "Hourglass", tint = AccentColor, modifier = Modifier.size(16.dp))
                    }
                    val pendingAssignmentsCount = assignments.count { !it.isCompleted }
                    val pendingTasksCount = tasks.count { !it.isCompleted }
                    Text(
                        text = "${pendingAssignmentsCount + pendingTasksCount}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(text = "$pendingAssignmentsCount school • $pendingTasksCount tasks", fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Placements/Career Tracker Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(96.dp)
                    .clickable { onNavigate(AppTab.CAREER) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Career Hub", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                        Icon(Icons.Default.WorkOutline, "Jobs", tint = InfoColor, modifier = Modifier.size(16.dp))
                    }
                    val careerCount = careerOpportunities.size
                    val appliedCount = careerOpportunities.count { it.status == "Applied" || it.status == "Interviewing" || it.status == "Offered" }
                    Text(
                        text = "$appliedCount/$careerCount",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "Applied pipelines", fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }

            // Documents Vault
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(96.dp)
                    .clickable { onNavigate(AppTab.VAULT) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Vault", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                        Icon(Icons.Default.LockReset, "Lock", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    }
                    val docCount = documents.size
                    Text(
                        text = "$docCount",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(text = "Scanned transcripts/IDs", fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }

        // Pinned Notices Block
        val pinnedNotices = notices.filter { it.isPinned }
        if (pinnedNotices.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "📌 Class notice updates", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    TextButton(onClick = { onNavigate(AppTab.NOTICES) }) {
                        Text("View Board")
                    }
                }
                pinnedNotices.take(2).forEach { notice ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = notice.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Badge(containerColor = MaterialTheme.colorScheme.primary) {
                                    Text(notice.category, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = notice.body,
                                fontSize = 12.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Upcoming Exams / Registrations
        if (exams.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "⏰ Roster: Exams & Registrations", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                exams.take(2).forEach { exam ->
                    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(exam.examDate))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.EventNote,
                                contentDescription = "Exam",
                                tint = ErrorColor,
                                modifier = Modifier
                                    .size(36.dp)
                                    .padding(end = 12.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(exam.subject, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(
                                    "${exam.type} • $dateStr",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            if (exam.notes.isNotBlank()) {
                                Badge(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                                    Text("Notes available", fontSize = 9.sp, modifier = Modifier.padding(4.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Fee/Scholarship Reminders Alerts
        val activeFinancials = financialReminders.filter { !it.isDone }
        if (activeFinancials.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "💸 Pending financial deadlines", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                activeFinancials.forEach { financial ->
                    val dateStr = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(financial.dueDate))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)),
                        border = BorderStroke(1.dp, ErrorColor.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = if (financial.type == "Fee") Icons.Default.Payment else Icons.Default.CardMembership,
                                    contentDescription = "Finance",
                                    tint = if (financial.type == "Fee") ErrorColor else SuccessColor,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(financial.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Due: $dateStr • ${financial.type}", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                                }
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "$${String.format("%.2f", financial.amount)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                TextButton(
                                    onClick = { viewModel.toggleFinancialReminder(financial) },
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.height(24.dp)
                                ) {
                                    Text("Mark Complete", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Project Spotlight Area
        val featuredProjects = projects.filter { it.portfolioFeatured }
        if (featuredProjects.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "✨ Digital portfolios", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                featuredProjects.take(1).forEach { project ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(project.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Icon(Icons.Default.Star, "Featured", tint = AccentColor, modifier = Modifier.size(16.dp))
                            }
                            Text(
                                "Stack: ${project.techStack}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                project.description,
                                fontSize = 12.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. NOTICES SCREEN (WITH AI notice parser)
// ==========================================
@Composable
fun NoticesAndAiScreen(notices: List<Notice>, viewModel: AppViewModel) {
    var titleInput by remember { mutableStateOf("") }
    var bodyInput by remember { mutableStateOf("") }
    var categorySelection by remember { mutableStateOf("Academics") }
    var showAddNoticeDialog by remember { mutableStateOf(false) }

    // AI Parser state
    var rawNoticeInputText by remember { mutableStateOf("") }

    val categories = listOf("Academics", "Career", "Placement", "Events", "General")
    var selectedFilterCategory by remember { mutableStateOf("All") }

    val displayNotices = if (selectedFilterCategory == "All") notices else notices.filter { it.category == selectedFilterCategory }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // AI notices parsing box (Sleek futuristic design, matching critical alert style bg-[#004A77])
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF004A77)
            ),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI Extractions",
                        tint = Color(0xFFD1E4FF)
                    )
                    Text(
                        text = "Student OS AI notice scanner",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Parse complex college circulars, syllabus announcements, or emails into actionable assignments, exams, and tasks automatically with Gemini.",
                    fontSize = 11.sp,
                    color = Color(0xFFD1E4FF).copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = rawNoticeInputText,
                    onValueChange = { rawNoticeInputText = it },
                    placeholder = { Text("Paste college newsletter, email, WhatsApp forward message, or circular text...", fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("ai_notice_input"),
                    textStyle = TextStyle(fontSize = 12.sp),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Preloaded Templates row for quick test
                Text(text = "Try preloaded circular templates:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            rawNoticeInputText = "OFFICIAL SYLLABUS ANNOUNCEMENT:\nThe Mid-semester exam for Compiler Design theory has been scheduled in exactly 5 days. Students must submit practical report files in 2 days to receive internal lab credit of 50 points."
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Exam & Assignment Report", fontSize = 10.sp)
                    }

                    Button(
                        onClick = {
                            rawNoticeInputText = "GOOGLE INTERNSHIP NOTIFICATION:\nRecruitment Office: Software Engineering Intern position is open for Winter 2026. Deadlines to register and match resumes in 14 days. Minimum requirement: 8.5 CGPA."
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Career Google Intern", fontSize = 10.sp)
                    }

                    Button(
                        onClick = {
                            rawNoticeInputText = "ACCOUNTS GENERAL LEDGER:\nTuition fee of $2,450 for Semester 5 is overdue in 12 days. Also, Merit-cum-Means scholarship application portal deadline is closing in 24 days."
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Finance & Scholarships", fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.parseDocumentWithAI(rawNoticeInputText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("run_ai_parser_btn"),
                    enabled = rawNoticeInputText.isNotBlank()
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "Parse", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Extract actionable schedules & cards with AI", fontSize = 12.sp)
                }
            }
        }

        // Filter / Header Category row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Central bulletin updates", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Button(
                onClick = { showAddNoticeDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.testTag("add_custom_notice_btn")
            ) {
                Icon(Icons.Default.Add, "Add", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Post Notice", fontSize = 11.sp)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filterOptions = listOf("All") + categories
            filterOptions.forEach { opt ->
                FilterChip(
                    selected = selectedFilterCategory == opt,
                    onClick = { selectedFilterCategory = opt },
                    label = { Text(opt, fontSize = 11.sp) }
                )
            }
        }

        // Notices list
        if (displayNotices.isEmpty()) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(1f), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Campaign, "Empty", modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No bulletin notices currently listed", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(displayNotices) { notice ->
                    var isExpanded by remember { mutableStateOf(false) }
                    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isExpanded = !isExpanded },
                        colors = CardDefaults.cardColors(
                            containerColor = if (notice.isPinned) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                        ),
                        border = BorderStroke(1.dp, if (notice.isPinned) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    if (notice.isPinned) {
                                        Icon(Icons.Default.PushPin, "Pinned", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(
                                        text = notice.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Badge(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                                    Text(
                                        notice.category,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = sdf.format(Date(notice.timestamp)),
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = notice.body,
                                fontSize = 12.sp,
                                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (isExpanded) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(
                                        onClick = { viewModel.deleteNotice(notice) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, "Delete Notice", tint = ErrorColor, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal dialog to add custom notices manually
    if (showAddNoticeDialog) {
        AlertDialog(
            onDismissRequest = { showAddNoticeDialog = false },
            title = { Text("Publish Circular", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Circular Header Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = bodyInput,
                        onValueChange = { bodyInput = it },
                        label = { Text("Circular Detailed Body") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Text("Bulletin Category", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = categorySelection == cat,
                                onClick = { categorySelection = cat },
                                label = { Text(cat, fontSize = 10.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (titleInput.isNotBlank() && bodyInput.isNotBlank()) {
                            viewModel.addNotice(titleInput, bodyInput, categorySelection, false)
                            titleInput = ""
                            bodyInput = ""
                            showAddNoticeDialog = false
                        }
                    }
                ) {
                    Text("Publish to Bulletin")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddNoticeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// ==========================================
// 3. ACADEMICS SCREEN
// ==========================================
@Composable
fun AcademicsScreen(
    assignments: List<Assignment>,
    exams: List<Exam>,
    attendance: List<Attendance>,
    viewModel: AppViewModel
) {
    var selectedAcademicTab by remember { mutableStateOf(0) } // 0: Attendance, 1: Assignments, 2: Exams

    var courseInputName by remember { mutableStateOf("") }
    var showAddCourseDialog by remember { mutableStateOf(false) }

    var assignmentTitleInput by remember { mutableStateOf("") }
    var assignmentCourseInput by remember { mutableStateOf("") }
    var assignmentDaysInput by remember { mutableStateOf("3") }
    var showAddAssignmentDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Upper Tab selection row
        TabRow(
            selectedTabIndex = selectedAcademicTab,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(selected = selectedAcademicTab == 0, onClick = { selectedAcademicTab = 0 }) {
                Text("📊 Attendance", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Tab(selected = selectedAcademicTab == 1, onClick = { selectedAcademicTab = 1 }) {
                Text("📝 Schoolwork", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Tab(selected = selectedAcademicTab == 2, onClick = { selectedAcademicTab = 2 }) {
                Text("🎓 Exams", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        when (selectedAcademicTab) {
            0 -> {
                // ATTENDANCE TAB
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Active Semester 5 Courses", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Requirement: minimum 75.0% attendance limit", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                    }
                    Button(
                        onClick = { showAddCourseDialog = true },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("add_course_btn")
                    ) {
                        Icon(Icons.Default.Add, "Add", modifier = Modifier.size(16.dp))
                        Text("Course", fontSize = 11.sp)
                    }
                }

                if (attendance.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CalendarToday, "None", modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                            Text("No courses registered yet", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(attendance) { att ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, if (att.percentage < 75f) ErrorColor.copy(alpha = 0.3f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(att.courseName, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.weight(1f))
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (att.percentage >= 75f) SuccessColor.copy(alpha = 0.15f) else ErrorColor.copy(alpha = 0.15f)
                                            )
                                        ) {
                                            Text(
                                                text = "${att.statusText} (${String.format("%.1f", att.percentage)}%)",
                                                fontWeight = FontWeight.Bold,
                                                color = if (att.percentage >= 75f) SuccessColor else ErrorColor,
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Attended: ${att.attended} / Conducted: ${att.conducted}", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                                            
                                            // Helper recommendation to maintain 75% limit
                                            val sessionsToAttend = if (att.percentage < 75f) {
                                                // (attended + x) / (conducted + x) >= 0.75 => x >= 3conducted - 4attended
                                                val x = (4 * att.attended - 3 * att.conducted)
                                                val needed = 3 * att.conducted - 4 * att.attended
                                                if (needed > 0) "Attend next $needed classes consecutively!" else "Do not miss next class!"
                                            } else {
                                                // attended / (conducted + y) >= 0.75 => y <= (4attended - 3conducted)/3
                                                val remainingMissable = (4 * att.attended - 3 * att.conducted) / 3
                                                if (remainingMissable > 0) "Safe to bunk next $remainingMissable classes." else "Do not miss next class!"
                                            }
                                            Text(sessionsToAttend, fontSize = 11.sp, color = if (att.percentage < 75f) ErrorColor else SuccessColor, fontWeight = FontWeight.Medium)
                                        }

                                        // Incremental attendance control layout
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            IconButton(
                                                onClick = { viewModel.decrementAttendance(att, true) },
                                                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                                modifier = Modifier.size(34.dp)
                                            ) {
                                                Icon(Icons.Default.Remove, "attended decrease", modifier = Modifier.size(16.dp))
                                            }
                                            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))) {
                                                Text(
                                                    text = "Class attended",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.padding(4.dp)
                                                )
                                            }
                                            IconButton(
                                                onClick = { viewModel.incrementAttendance(att, true) },
                                                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
                                                modifier = Modifier.size(34.dp)
                                            ) {
                                                Icon(Icons.Default.Add, "attended increase", tint = Color.White, modifier = Modifier.size(16.dp))
                                            }

                                            Spacer(modifier = Modifier.width(4.dp))
                                            IconButton(
                                                onClick = { viewModel.incrementAttendance(att, false) },
                                                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                                modifier = Modifier.size(34.dp)
                                            ) {
                                                Icon(Icons.Default.Cancel, "conducted increase but missed class", tint = Color.White, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // ASSIGNMENTS TAB
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Collegiate Work Project Tasks", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Button(
                        onClick = { showAddAssignmentDialog = true },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("add_assignment_btn")
                    ) {
                        Icon(Icons.Default.Add, "Add", modifier = Modifier.size(16.dp))
                        Text("Work", fontSize = 11.sp)
                    }
                }

                if (assignments.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Assignment, "No Assignments", modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                            Text("No academic milestones listed currently.", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(assignments) { assignment ->
                            val dateStr = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(Date(assignment.dueDate))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (assignment.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Checkbox(
                                            checked = assignment.isCompleted,
                                            onCheckedChange = { viewModel.toggleAssignment(assignment) },
                                            modifier = Modifier.testTag("todo_checkbox_${assignment.id}")
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = assignment.title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                textDecoration = if (assignment.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                            )
                                            Text(
                                                "${assignment.courseName} • Due: $dateStr",
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                        }
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Badge(containerColor = if (assignment.isCompleted) SuccessColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)) {
                                            Text(
                                                text = if (assignment.isCompleted) "Done" else "${assignment.points} Pts",
                                                color = if (assignment.isCompleted) SuccessColor else MaterialTheme.colorScheme.primary,
                                                fontSize = 10.sp,
                                                modifier = Modifier.padding(4.dp)
                                            )
                                        }
                                        IconButton(
                                            onClick = { viewModel.deleteAssignment(assignment) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, "Delete", tint = ErrorColor, modifier = Modifier.size(14.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // EXAMS TAB
                Text("Official Assessment Timelines", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                if (exams.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.School, "Exam", modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                            Text("No scheduled exam files listed", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(exams) { exam ->
                            val dateStr = SimpleDateFormat("EEE, dd MMM yyyy - hh:mm a", Locale.getDefault()).format(Date(exam.examDate))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(exam.subject, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Badge(containerColor = MaterialTheme.colorScheme.errorContainer) {
                                            Text(exam.type, color = ErrorColor, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                    Text(
                                        "Timeline: $dateStr",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    if (exam.notes.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                                        ) {
                                            Text(
                                                text = exam.notes,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onBackground,
                                                modifier = Modifier.padding(8.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                        TextButton(onClick = { viewModel.deleteExam(exam) }, contentPadding = PaddingValues(0.dp)) {
                                            Text("Remove Exam", color = ErrorColor, fontSize = 11.sp)
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

    // Modal dialogs
    if (showAddCourseDialog) {
        var newCourseName by remember { mutableStateOf("") }
        var initialConducted by remember { mutableStateOf("0") }
        var initialAttended by remember { mutableStateOf("0") }

        AlertDialog(
            onDismissRequest = { showAddCourseDialog = false },
            title = { Text("Register New Course", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newCourseName,
                        onValueChange = { newCourseName = it },
                        label = { Text("Course Name / Code") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = initialConducted,
                        onValueChange = { initialConducted = it },
                        label = { Text("Initial Conducted Lectures") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = initialAttended,
                        onValueChange = { initialAttended = it },
                        label = { Text("Initial Attended Lectures") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val condVal = initialConducted.toIntOrNull() ?: 0
                        val attVal = initialAttended.toIntOrNull() ?: 0
                        if (newCourseName.isNotBlank()) {
                            viewModel.addAttendanceCourse(newCourseName, attVal, condVal)
                            showAddCourseDialog = false
                        }
                    }
                ) {
                    Text("Add Course")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCourseDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showAddAssignmentDialog) {
        AlertDialog(
            onDismissRequest = { showAddAssignmentDialog = false },
            title = { Text("New Project / Work Task", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = assignmentTitleInput,
                        onValueChange = { assignmentTitleInput = it },
                        label = { Text("Task Title / Objective") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = assignmentCourseInput,
                        onValueChange = { assignmentCourseInput = it },
                        label = { Text("Host Course Subject") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = assignmentDaysInput,
                        onValueChange = { assignmentDaysInput = it },
                        label = { Text("Days from today due") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val days = assignmentDaysInput.toIntOrNull() ?: 3
                        if (assignmentTitleInput.isNotBlank() && assignmentCourseInput.isNotBlank()) {
                            viewModel.addAssignment(assignmentTitleInput, assignmentCourseInput, days, 100)
                            assignmentTitleInput = ""
                            assignmentCourseInput = ""
                            showAddAssignmentDialog = false
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddAssignmentDialog = false }) { Text("Cancel") }
            }
        )
    }
}

// ==========================================
// 4. CAREER HUB SCREEN
// ==========================================
@Composable
fun CareerHubScreen(
    careerOpportunities: List<CareerOpportunity>,
    collegeEvents: List<CollegeEvent>,
    viewModel: AppViewModel
) {
    var selectedCareerTab by remember { mutableStateOf(0) } // 0: Placement Pipeline, 1: Events/Hackathons

    var jobTitleInput by remember { mutableStateOf("") }
    var companyInput by remember { mutableStateOf("") }
    var jobTypeInput by remember { mutableStateOf("Internship") } // Internship or Placement
    var detailsInput by remember { mutableStateOf("") }
    var locationInput by remember { mutableStateOf("Remote") }
    var showAddJobDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TabRow(selectedTabIndex = selectedCareerTab, containerColor = MaterialTheme.colorScheme.background) {
            Tab(selected = selectedCareerTab == 0, onClick = { selectedCareerTab = 0 }) {
                Text("💼 Placements & Interns", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Tab(selected = selectedCareerTab == 1, onClick = { selectedCareerTab = 1 }) {
                Text("🔥 Competitions & Hackathons", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        when (selectedCareerTab) {
            0 -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Application Pipelines", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Track the states of your recruitment cycles", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                    }
                    Button(
                        onClick = { showAddJobDialog = true },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("add_job_pipeline_btn")
                    ) {
                        Icon(Icons.Default.Add, "Add", modifier = Modifier.size(16.dp))
                        Text("Pipeline", fontSize = 11.sp)
                    }
                }

                if (careerOpportunities.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No career items registered yet.", color = MaterialTheme.colorScheme.secondary)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(careerOpportunities) { opportunity ->
                            val dateStr = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date(opportunity.deadline))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(opportunity.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text("${opportunity.companyOrOrg} • ${opportunity.location}", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                                        }
                                        Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                                            Text(opportunity.type, color = MaterialTheme.colorScheme.onPrimaryContainer, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(opportunity.details, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Deadline: $dateStr", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)

                                    Spacer(modifier = Modifier.height(10.dp))
                                    // Status selector
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Pipelines State:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        val statColors = mapOf(
                                            "Open" to InfoColor,
                                            "Wishlist" to MaterialTheme.colorScheme.secondary,
                                            "Applied" to MaterialTheme.colorScheme.primary,
                                            "Interviewing" to AccentColor,
                                            "Offered" to SuccessColor,
                                            "Rejected" to ErrorColor
                                        )
                                        var mExpanded by remember { mutableStateOf(false) }
                                        Box {
                                            Button(
                                                onClick = { mExpanded = true },
                                                colors = ButtonDefaults.buttonColors(containerColor = (statColors[opportunity.status] ?: MaterialTheme.colorScheme.secondary).copy(alpha = 0.2f), contentColor = statColors[opportunity.status] ?: MaterialTheme.colorScheme.onSurface),
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                                modifier = Modifier.height(28.dp)
                                            ) {
                                                Text(opportunity.status, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                                Icon(Icons.Default.ArrowDropDown, "down", modifier = Modifier.size(16.dp))
                                            }
                                            DropdownMenu(expanded = mExpanded, onDismissRequest = { mExpanded = false }) {
                                                listOf("Wishlist", "Applied", "Interviewing", "Offered", "Rejected").forEach { s ->
                                                    DropdownMenuItem(
                                                        text = { Text(s, fontSize = 11.sp) },
                                                        onClick = {
                                                            viewModel.updateOpportunityStatus(opportunity, s)
                                                            mExpanded = false
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                        TextButton(onClick = { viewModel.deleteOpportunity(opportunity) }, contentPadding = PaddingValues(0.dp)) {
                                            Text("Remove Pipeline Project", color = ErrorColor, fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // EVENTS & COMPETITIONS TAB
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Featured Campus Events", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Join collegiate activities and industrial webinars", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                    }
                }

                if (collegeEvents.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No upcoming university circular events scheduled currently.", color = MaterialTheme.colorScheme.secondary)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(collegeEvents) { event ->
                            val dateStr = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()).format(Date(event.eventDate))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(event.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.weight(1f))
                                        Button(
                                            onClick = { viewModel.toggleEventRsvp(event) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (event.rsvped) SuccessColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                                                contentColor = if (event.rsvped) SuccessColor else MaterialTheme.colorScheme.onSurfaceVariant
                                            ),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                            modifier = Modifier.height(28.dp).testTag("rsvp_btn_${event.id}")
                                        ) {
                                            Icon(if (event.rsvped) Icons.Default.Check else Icons.Default.CalendarToday, "rsvp", modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(if (event.rsvped) "Going" else "RSVP Now", fontSize = 10.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Organizer: ${event.organizer} • Venue: ${event.venue}", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(event.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Date: $dateStr", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Job Dialog
    if (showAddJobDialog) {
        AlertDialog(
            onDismissRequest = { showAddJobDialog = false },
            title = { Text("Register Job Pipeline Goal", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = jobTitleInput,
                        onValueChange = { jobTitleInput = it },
                        label = { Text("Job Role / Title (e.g. Android Intern)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = companyInput,
                        onValueChange = { companyInput = it },
                        label = { Text("Company Host Organization") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = locationInput,
                        onValueChange = { locationInput = it },
                        label = { Text("Job Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = detailsInput,
                        onValueChange = { detailsInput = it },
                        label = { Text("Specific Job details / Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (jobTitleInput.isNotBlank() && companyInput.isNotBlank()) {
                            viewModel.addCareerOpportunity(
                                title = jobTitleInput,
                                company = companyInput,
                                type = jobTypeInput,
                                details = detailsInput,
                                location = locationInput,
                                daysFromNow = 14
                            )
                            jobTitleInput = ""
                            companyInput = ""
                            detailsInput = ""
                            showAddJobDialog = false
                        }
                    }
                ) {
                    Text("Add Pipeline")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddJobDialog = false }) { Text("Cancel") }
            }
        )
    }
}

// ==========================================
// 5. DOCUMENT VAULT SCREEN
// ==========================================
@Composable
fun DocumentVaultScreen(documents: List<Document>, viewModel: AppViewModel) {
    var docNameInput by remember { mutableStateOf("") }
    var categorySelection by remember { mutableStateOf("Resume") }
    var notesInput by remember { mutableStateOf("") }
    var showUploadDialog by remember { mutableStateOf(false) }

    val categories = listOf("Resume", "Certificate", "ID Document", "Marksheet")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Secure Document Vault", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Store, manage and preview essential credentials", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
            }
            Button(
                onClick = { showUploadDialog = true },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.testTag("upload_vault_doc_btn")
            ) {
                Icon(Icons.Default.CloudUpload, "Upload", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Upload Document", fontSize = 11.sp)
            }
        }

        if (documents.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.FolderOpen, "Empty", modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Document Vault is empty.", fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(documents) { doc ->
                    val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(doc.uploadedTimestamp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Icon(
                                        imageVector = when (doc.category) {
                                            "Resume" -> Icons.Default.Description
                                            "Certificate" -> Icons.Default.Badge
                                            "ID Document" -> Icons.Default.ContactPage
                                            else -> Icons.Default.ReceiptLong
                                        },
                                        contentDescription = "Doc Category Icon",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(doc.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Badge(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                                    Text(doc.category, color = MaterialTheme.colorScheme.onSecondaryContainer, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            if (doc.notes.isNotBlank()) {
                                Text(doc.notes, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Uploaded: $dateStr", fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary)
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Button(
                                        onClick = { /* Simulate file viewing */ },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.primary),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                        modifier = Modifier.height(28.dp)
                                    ) {
                                        Text("Preview SCAN", fontSize = 10.sp)
                                    }
                                    IconButton(
                                        onClick = { viewModel.deleteDocument(doc) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, "clear doc", tint = ErrorColor, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showUploadDialog) {
        AlertDialog(
            onDismissRequest = { showUploadDialog = false },
            title = { Text("Secure Document Upload", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = docNameInput,
                        onValueChange = { docNameInput = it },
                        label = { Text("Document File Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = notesInput,
                        onValueChange = { notesInput = it },
                        label = { Text("Special notes or Details") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Credential classification Category", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = categorySelection == cat,
                                onClick = { categorySelection = cat },
                                label = { Text(cat, fontSize = 10.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (docNameInput.isNotBlank()) {
                            viewModel.addDocument(docNameInput, categorySelection, notesInput, true)
                            docNameInput = ""
                            notesInput = ""
                            showUploadDialog = false
                        }
                    }
                ) {
                    Text("Secure Credentials")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUploadDialog = false }) { Text("Cancel") }
            }
        )
    }
}

// ==========================================
// 6. PROJECTS PORTFOLIO SCREEN
// ==========================================
@Composable
fun ProjectsPortfolioScreen(projects: List<Project>, tasks: List<Task>, viewModel: AppViewModel) {
    var prjTitle by remember { mutableStateOf("") }
    var techStackInput by remember { mutableStateOf("") }
    var prjDesc by remember { mutableStateOf("") }
    var sourceLink by remember { mutableStateOf("") }
    var isFeatured by remember { mutableStateOf(false) }
    var showProjectDialog by remember { mutableStateOf(false) }

    var selectedSection by remember { mutableStateOf(0) } // 0: Portfolios, 1: Daily Task list

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TabRow(selectedTabIndex = selectedSection, containerColor = MaterialTheme.colorScheme.background) {
            Tab(selected = selectedSection == 0, onClick = { selectedSection = 0 }) {
                Text("🔮 Portfolio Show", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Tab(selected = selectedSection == 1, onClick = { selectedSection = 1 }) {
                Text("📋 Dynamic Task OS", modifier = Modifier.padding(12.dp), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        when (selectedSection) {
            0 -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Personal Projects & Portfolios", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Demonstrate your practical technical craft", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                    }
                    Button(
                        onClick = { showProjectDialog = true },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("add_project_btn")
                    ) {
                        Icon(Icons.Default.Add, "Add Project", modifier = Modifier.size(16.dp))
                        Text("Project", fontSize = 11.sp)
                    }
                }

                if (projects.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No portfolio projects listed. Tap 'Project' to showcase your code!", color = MaterialTheme.colorScheme.secondary)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(projects) { project ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(project.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        if (project.portfolioFeatured) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Star, "Featured star", tint = AccentColor, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Featured", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AccentColor)
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Stack: ${project.techStack}", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(project.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (project.githubLink.isNotBlank()) {
                                            Text(project.githubLink, fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                                        } else {
                                            Spacer(modifier = Modifier.width(1.dp))
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            TextButton(onClick = { viewModel.deleteProject(project) }, contentPadding = PaddingValues(0.dp)) {
                                                Text("Remove", color = ErrorColor, fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // TASK OS
                var taskTitleInput by remember { mutableStateOf("") }
                var taskCatInput by remember { mutableStateOf("Academic") }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Dynamic Task Scheduler", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Academic commitments & daily campus chores", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                    }
                }

                // Inline quick task addition
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = taskTitleInput,
                        onValueChange = { taskTitleInput = it },
                        placeholder = { Text("New task objective...", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f).testTag("quick_task_input"),
                        maxLines = 1,
                        textStyle = TextStyle(fontSize = 12.sp)
                    )
                    Button(
                        onClick = {
                            if (taskTitleInput.isNotBlank()) {
                                viewModel.addTask(taskTitleInput, taskCatInput, 1)
                                taskTitleInput = ""
                            }
                        },
                        modifier = Modifier.testTag("submit_task_btn")
                    ) {
                        Text("Add", fontSize = 11.sp)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Academic", "Career", "Personal").forEach { cat ->
                        FilterChip(
                            selected = taskCatInput == cat,
                            onClick = { taskCatInput = cat },
                            label = { Text(cat, fontSize = 10.sp) }
                        )
                    }
                }

                if (tasks.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text("All habits and academic tasks checked off!", color = MaterialTheme.colorScheme.secondary)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(tasks) { task ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (task.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Checkbox(
                                            checked = task.isCompleted,
                                            onCheckedChange = { viewModel.toggleTask(task) },
                                            modifier = Modifier.testTag("task_checkbox_${task.id}")
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Text(
                                                text = task.title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                                            )
                                            Text("Category: ${task.category}", fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary)
                                        }
                                    }
                                    IconButton(
                                        onClick = { viewModel.deleteTask(task) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, "clear task", tint = ErrorColor, modifier = Modifier.size(13.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showProjectDialog) {
        AlertDialog(
            onDismissRequest = { showProjectDialog = false },
            title = { Text("Publish Portfolio Piece", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = prjTitle,
                        onValueChange = { prjTitle = it },
                        label = { Text("Project Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = techStackInput,
                        onValueChange = { techStackInput = it },
                        label = { Text("Technology Stacks (e.g. Kotlin, Compose)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = prjDesc,
                        onValueChange = { prjDesc = it },
                        label = { Text("Description & architecture details") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    OutlinedTextField(
                        value = sourceLink,
                        onValueChange = { sourceLink = it },
                        label = { Text("GitHub Source Link") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(checked = isFeatured, onCheckedChange = { isFeatured = it })
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Feature this workspace in command spotlight", fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (prjTitle.isNotBlank()) {
                            viewModel.addProject(prjTitle, techStackInput, prjDesc, sourceLink, isFeatured)
                            prjTitle = ""
                            techStackInput = ""
                            prjDesc = ""
                            sourceLink = ""
                            isFeatured = false
                            showProjectDialog = false
                        }
                    }
                ) {
                    Text("Add Workspace")
                }
            },
            dismissButton = {
                TextButton(onClick = { showProjectDialog = false }) { Text("Cancel") }
            }
        )
    }
}
