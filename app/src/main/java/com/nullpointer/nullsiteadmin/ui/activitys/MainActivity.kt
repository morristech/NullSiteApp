package com.nullpointer.nullsiteadmin.ui.activitys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.nullpointer.nullsiteadmin.ui.navigator.MainDestinations
import com.nullpointer.nullsiteadmin.ui.screens.NavGraphs
import com.nullpointer.nullsiteadmin.ui.share.NavigatorDrawer
import com.nullpointer.nullsiteadmin.ui.share.ToolbarMenu
import com.nullpointer.nullsiteadmin.ui.theme.NullSiteAdminTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NullSiteAdminTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    var titleNav by remember { mutableStateOf(MainDestinations.PersonalInfoScreen.label) }
                    val scope = rememberCoroutineScope()
                    val navController = rememberNavController()
                    val scaffoldState= rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
                    navController.addOnDestinationChangedListener { _, destination, _ ->
//                        MainDestinations.isHomeRoute(destination.route)
                        titleNav = MainDestinations.getLabel(destination.route)
                    }

                    Scaffold(
                        scaffoldState = scaffoldState,
                        topBar = {
                            ToolbarMenu(title = titleNav) {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            }
                        },
                        drawerContent = {
                            NavigatorDrawer(
                                drawerState = scaffoldState.drawerState,
                                scope = scope,
                                navController = navController
                            )
                        }
                    ) { paddingValues ->
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            navController = navController,
                            modifier = Modifier.padding(paddingValues)
                        )
                    }

                }
            }
        }
    }
}

