package com.example.gnammy.ui.screens.gnamdetails

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gnammy.ui.composables.RecipeCardBig

@Composable
@Preview
fun GnamDetailsScreen(navController: NavHostController? = null, modifier: Modifier? = null) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        RecipeCardBig(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recipe:",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "This is a sample recipe text to simulate a recipe description. It can be replaced with the actual recipe details.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                modifier = Modifier.padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                onClick = {
                    // TODO Handle search action
                }
            ) {
                Text("Rimuovi dai preferiti")
            }
            Button(
                onClick = {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "This is a sample recipe text to simulate a recipe description.")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(intent, "Save Recipe to Notes"))
                }
            ) {
                Text("Salva nelle note")
            }
        }
    }
}
