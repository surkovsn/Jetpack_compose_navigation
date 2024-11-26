package ru.surkov_sn.jetpackcomposenavigation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.surkov_sn.jetpackcomposenavigation.ui.theme.JetpackComposeNavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            App(navController = navController) { innerPadding ->
                NavHost(
                    navController,
                    startDestination = NavRoutes.Home.route,
                    modifier = Modifier.padding(innerPadding)
                ){
                    composable(NavRoutes.Home.route) { Home() }
                    composable(NavRoutes.Shedule.route) { Shedule() }
                    composable(NavRoutes.Journal.route) { Journal() }
                    composable(NavRoutes.RecordBook.route) { RecordBook() }
                    composable(NavRoutes.Menu.route) { Menu() }
                }
            }


        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun App(navController: NavController,
            content: @Composable (PaddingValues) ->Unit
    ) {
        JetpackComposeNavigationTheme {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = { TopBarNavigation(scrollBehavior) },
                bottomBar = {
                    BottomNavigationBar(navController)
                },
                content = content
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController){
    NavigationBar{
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route
        val context = LocalContext.current
        NavBarItems.BarItems.forEach { navItems->
            NavigationBarItem(
                selected = currentRoute ==navItems.route,
                onClick = {
                    when(navItems.route){
                        NavRoutes.Menu.route -> {
                            Toast.makeText(context,"Открываем меню",Toast.LENGTH_SHORT).show()
                        }
                        else ->{
                            navController.navigate(navItems.route) {
                                popUpTo(navController.graph.findStartDestination().id) {saveState = true}
                                launchSingleTop = true
                                restoreState = true
                            }
                        }

                    }

                },
                icon = {Icon(imageVector = navItems.image, contentDescription = navItems.title)},
                label = {
                    Text(text = navItems.title)
                },
            )

        }
    }
}

object NavBarItems{
    val BarItems = listOf(
        BarItem(
            title = "Главный",
            image = Icons.Filled.Home,
            route = "home"
        ),
        BarItem(
            title = "Расписание",
            image = Icons.Filled.DateRange,
            route = "shedule"
        ),
        BarItem(
            title = "Журнал",
            image = Icons.Filled.Info,
            route = "journal"
        ),
        BarItem(
            title = "Зачетка",
            image = Icons.Filled.Share,
            route = "recordbook"
        )
        ,
        BarItem(
            title = "Еще",
            image = Icons.Filled.MoreVert,
            route = "menu"
        )
    )
}

data class BarItem(
    val title:String,
    val image: ImageVector,
    val route:String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarNavigation(scrollBehavior:TopAppBarScrollBehavior){
    CenterAlignedTopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = { Text(
            text ="Главный",
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            fontSize = 20.sp

        )},
        navigationIcon = {
            IconButton(
                onClick = {}
            ) {
                Icon(

                    imageVector = ImageVector.vectorResource(R.drawable.settings),
                    contentDescription = "settings"
                )
            }
        },
        actions = {
            IconButton(
                onClick = {}

            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notifications",
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun Home(){
    Text("Home Screen", fontSize = 30.sp)
}
@Composable
fun Shedule(){
    Text("Shedule Screen", fontSize = 30.sp)
}
@Composable
fun Journal(){
    Text("Journal Screen", fontSize = 30.sp)
}

@Composable
fun RecordBook(){
    Text("RecordBook Screen", fontSize = 30.sp)
}

@Composable
fun Menu(){
    Text("Menu Screen", fontSize = 30.sp)
}

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Shedule : NavRoutes("shedule")
    object Journal : NavRoutes("journal")
    object RecordBook : NavRoutes("recordbook")
    object Menu : NavRoutes("menu")
}



