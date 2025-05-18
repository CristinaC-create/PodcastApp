package com.cristinacabral.podcastapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cristinacabral.podcastapp.ui.theme.PodcastAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PodcastAppTheme {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(navController)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("podcastList") { PodcastListScreen() }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0288D1), Color(0xFF121212))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.podcast_image),
                contentDescription = "App Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("The Podcast App", fontSize = 30.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to The Podcast App", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate("podcastList") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
        ) {
            Text("Sign In", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { navController.navigate("podcastList") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("Continue as Guest")
        }
    }
}

// --- Podcast List Code ---
data class Podcast(
    val title: String,
    val description: String,
    val websiteUrl: String,
    val audioUrl: String,
    @DrawableRes val imageRes: Int,
)

val samplePodcasts = listOf(
    Podcast("The Daily", "News from NYT", "https://nytimes.com", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3", R.drawable.the_daily),
    Podcast("99% Invisible", "Design & architecture", "https://99percentinvisible.org", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3", R.drawable.invincible),
    Podcast("SmartLess", "Celebrity interviews", "https://www.smartless.com", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3", R.drawable.smartless),
    Podcast("Science Vs", "Debunking fads with science", "https://gimletmedia.com/shows/science-vs", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3", R.drawable.science_vs),
    Podcast("Stuff You Should Know", "How stuff works explained", "https://stuffyoushouldknow.com", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3", R.drawable.stuff_you),
    Podcast("Crime Junkie", "True crime stories told weekly.", "https://crimejunkiepodcast.com", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3", R.drawable.crime_junkie),
    Podcast("The Joe Rogan Experience", "Long-form conversations with guests", "https://open.spotify.com/show/4rOoJ6Egrf8K2IrywzwOMk", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3", R.drawable.joe_rogan),
    Podcast("The Diary of a CEO", "Conversations on life, business & mental health", "https://www.diaryofaceo.com", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3", R.drawable.diary_ceo)
)

@Composable
fun PodcastListScreen() {
    var searchQuery by remember { mutableStateOf("") }
    val filteredPodcasts = samplePodcasts.filter {
        it.title.contains(searchQuery, ignoreCase = true)
    }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "\uD83C\uDFA7 Featured Podcasts",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search podcasts", color = Color.White) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        filteredPodcasts.forEach { podcast ->
            PodcastCard(podcast) {
                val intent = Intent(Intent.ACTION_VIEW, podcast.websiteUrl.toUri())
                context.startActivity(intent)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun PodcastCard(podcast: Podcast, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = podcast.imageRes),
                contentDescription = podcast.title,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(podcast.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                Text(podcast.description, fontSize = 14.sp, color = Color.LightGray)
            }
        }
    }
}
