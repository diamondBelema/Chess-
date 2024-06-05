package com.dibe.chess.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dibe.chess.R
import com.dibe.chess.Screen
import com.dibe.chess.ui.theme.ChessTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController : NavController) {
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter =  painterResource(id = R.drawable.background) ,
            contentDescription = "Background Image" ,
            contentScale = ContentScale.FillBounds ,
            modifier = Modifier.matchParentSize()
             )
        Scaffold (
            topBar = { LargeTopAppBar(title = { Text(text = "♟ CHESS", fontSize = 50.sp) })}
                 ){ paddingValue ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
               ){
                OutlinedCard (
                    onClick = { navController.navigate(Screen.TWOPLAYERS.name) },
                    modifier = Modifier
                        .padding(top = 40.dp, start = 10.dp, end = 10.dp)
                             ){
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                           ){
                        ListItem(
                            headlineContent = { Text("Two Player", fontSize = 19.sp)},
                            leadingContent = { Text("♟vs♙", fontSize = 20.sp)} ,
                            trailingContent = {
                                IconButton(onClick = { navController.navigate(Screen.TWOPLAYERS.name) }) {
                                    Icon(Icons.Default.PlayArrow , contentDescription ="play")
                                }
                            }
                                )
                    }
                }
            }
        }
    }
}
