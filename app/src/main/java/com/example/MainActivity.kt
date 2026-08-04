package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.ui.components.NavigationDrawerContent
import com.example.ui.screens.BackupRestoreScreen
import com.example.ui.screens.EditorScreen
import com.example.ui.screens.GroupsScreen
import com.example.ui.screens.HiddenNotesScreen
import com.example.ui.screens.NotesListScreen
import com.example.ui.screens.SettingsAboutScreen
import com.example.ui.viewmodel.Screen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TrashScreen
import com.example.ui.theme.KabyolokorTheme
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

import com.example.ui.font.BengaliFonts

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BengaliFonts.init(this)
        enableEdgeToEdge()

        setContent {
            val isDarkPref by mainViewModel.isDarkMode.collectAsState()
            val systemInDark = isSystemInDarkTheme()
            val effectiveDark = isDarkPref ?: systemInDark

            KabyolokorTheme(darkTheme = effectiveDark) {
                MainAppContent(
                    viewModel = mainViewModel,
                    isDarkMode = effectiveDark
                )
            }
        }
    }
}

@Composable
fun MainAppContent(
    viewModel: MainViewModel,
    isDarkMode: Boolean
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val exportAllPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        if (uri != null) {
            viewModel.exportAllNotesToPdfToUri(context, uri)
        }
    }

    // Back button handling
    BackHandler(enabled = currentScreen !is Screen.NotesList && currentScreen !is Screen.Splash) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else {
            viewModel.navigateTo(Screen.NotesList)
        }
    }

    if (currentScreen is Screen.Splash) {
        SplashScreen(
            onSplashFinished = {
                viewModel.navigateTo(Screen.NotesList)
            }
        )
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = currentScreen !is Screen.Editor,
            drawerContent = {
                NavigationDrawerContent(
                    currentScreen = currentScreen,
                    isDarkMode = isDarkMode,
                    onNavigate = { targetScreen ->
                        viewModel.navigateTo(targetScreen)
                    },
                    onToggleDarkMode = { isDark ->
                        viewModel.toggleDarkMode(isDark)
                    },
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    },
                    onExportAllPdf = {
                        val dateStr = SimpleDateFormat("dd_MMM_yyyy", Locale("bn", "BD")).format(Date())
                        exportAllPdfLauncher.launch("Kabyoloko_All_Notes_$dateStr.pdf")
                    }
                )
            }
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                modifier = Modifier.fillMaxSize(),
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    is Screen.Splash -> {
                        SplashScreen(
                            onSplashFinished = {
                                viewModel.navigateTo(Screen.NotesList)
                            }
                        )
                    }

                    is Screen.NotesList, is Screen.PinnedNotes -> {
                        NotesListScreen(
                            viewModel = viewModel,
                            onOpenDrawer = { scope.launch { drawerState.open() } },
                            onOpenEditor = { noteId ->
                                viewModel.navigateTo(Screen.Editor(noteId))
                            }
                        )
                    }

                    is Screen.Editor -> {
                        EditorScreen(
                            noteId = screen.noteId,
                            mainViewModel = viewModel,
                            onBack = {
                                viewModel.navigateTo(Screen.NotesList)
                            }
                        )
                    }

                    is Screen.Groups -> {
                        GroupsScreen(
                            viewModel = viewModel,
                            onOpenDrawer = { scope.launch { drawerState.open() } },
                            onBack = { viewModel.navigateTo(Screen.NotesList) },
                            onOpenEditor = { noteId ->
                                viewModel.navigateTo(Screen.Editor(noteId))
                            }
                        )
                    }

                    is Screen.HiddenNotes -> {
                        HiddenNotesScreen(
                            viewModel = viewModel,
                            onOpenDrawer = { scope.launch { drawerState.open() } },
                            onBack = { viewModel.navigateTo(Screen.NotesList) },
                            onOpenEditor = { noteId ->
                                viewModel.navigateTo(Screen.Editor(noteId))
                            }
                        )
                    }

                    is Screen.Trash -> {
                        TrashScreen(
                            viewModel = viewModel,
                            onOpenDrawer = { scope.launch { drawerState.open() } },
                            onBack = { viewModel.navigateTo(Screen.NotesList) }
                        )
                    }

                    is Screen.BackupRestore -> {
                        BackupRestoreScreen(
                            viewModel = viewModel,
                            onOpenDrawer = { scope.launch { drawerState.open() } },
                            onBack = { viewModel.navigateTo(Screen.NotesList) }
                        )
                    }

                    is Screen.SettingsAbout -> {
                        SettingsAboutScreen(
                            viewModel = viewModel,
                            onOpenDrawer = { scope.launch { drawerState.open() } },
                            onBack = { viewModel.navigateTo(Screen.NotesList) }
                        )
                    }
                }
            }
        }
    }
}
