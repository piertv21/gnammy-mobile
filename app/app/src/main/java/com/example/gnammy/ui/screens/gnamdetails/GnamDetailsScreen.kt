package com.example.gnammy.ui.screens.gnamdetails

import android.content.Intent
import android.net.Uri
import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gnammy.ui.composables.ImageWithPlaceholder
import com.example.gnammy.ui.composables.Size

@Composable
fun OverlappingColumn(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.1, to = 1.0) overlapFactor: Float = 0.5f,
    content: @Composable () -> Unit,
) {
    val measurePolicy = overlappingColumnMeasurePolicy(overlapFactor)
    Layout(
        measurePolicy = measurePolicy,
        content = content,
        modifier = modifier
    )
}

fun overlappingColumnMeasurePolicy(overlapFactor: Float) = MeasurePolicy { measurables, constraints ->
    val placeables = measurables.map { measurable -> measurable.measure(constraints) }
    val width = placeables.maxOf { it.width }
    val height = (placeables.subList(1, placeables.size).sumOf { it.height } * overlapFactor + placeables[0].height).toInt()
    layout(width, height) {
        var yPos = 0
        for (placeable in placeables) {
            placeable.placeRelative(0, yPos, 0f)
            yPos += (placeable.height * overlapFactor).toInt()
        }
    }
}

@Composable
@Preview
fun GnamDetailsScreen(navController: NavHostController? = null, modifier: Modifier? = null) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val propicUri = Uri.parse("https://budgetbytes.com/wp-content/uploads/2022/07/Beth-2022-3-60x60.jpg")
    val imageUri = Uri.parse("https://studiousguy.com/wp-content/uploads/2021/05/Cheese-Slice.jpg")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 16.dp, 16.dp)
            .verticalScroll(scrollState)
    ) {

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(10.dp)
        )
        {
            Text(
                text = "Spicy Sriracha Noodles",
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Start,
                style =  MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                ImageWithPlaceholder(
                    uri = propicUri,
                    size = Size.Sm,
                    description = "propic",
                    modifier = Modifier
                        .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
                        .size(30.dp)
                        .clip(shape = CircleShape)
                        .align(Alignment.CenterVertically)
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = " Beth, Budget Bytes",
                    color = MaterialTheme.colorScheme.background,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                Text(
                    text = " - 24/05/2024",
                    color = MaterialTheme.colorScheme.background,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium.copy(),
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }

        }

        Spacer(modifier = Modifier.height(10.dp))

        ImageWithPlaceholder(
                uri = imageUri,
                size = Size.Lg,
                description = "Recipe Image",
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(20.dp))
            )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxHeight(1f / 3f)
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = MaterialTheme.colorScheme.primary)
                .padding(10.dp)
        ) {
            Text(
                text = "Oh WOW. It should be illegal for noodles to be this easy AND this delicious. These spicy sriracha noodles are my new favorite quick fix! They only take about 15 minutes to make, they’re totally rich, flavorful, and SUPER SPICY. Like, “burn a hole through your stomach” spicy. Call me crazy, but sometimes I want that. No, I crave that. So, this one goes out to all of you heat seekers!",
                color = MaterialTheme.colorScheme.background,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically),
                overflow = TextOverflow.Ellipsis
            )
        }



        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Recipe:",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "4 oz. lo mein noodles  (\$1.30)\n" +
                    "2 Tbsp butter  (\$0.20)\n" +
                    "1/4 tsp crushed red pepper  (\$0.02)\n" +
                    "2 large eggs (\$0.42)\n" +
                    "1 Tbsp brown sugar (\$0.08)\n" +
                    "1 Tbsp soy sauce  (\$0.06)\n" +
                    "2 Tbsp sriracha (\$0.22)\n" +
                    "1 handful fresh cilantro (optional) (\$0.10)\n" +
                    "1 green onion, sliced  (\$0.10)\n" +
                    "\n" +
                    "Prepare the sauce for the noodles. In a small bowl, stir together the brown sugar, soy sauce, and sriracha. Set the sauce aside.\n" +
                    "Bring  a pot of water to a boil for the noodles. Once boiling, add the noodles  and boil until tender. Drain the noodles in a colander.\n" +
                    "While waiting for the water to boil, crack two eggs into a bowl then whisk lightly.\n" +
                    "Heat  the butter in a skillet over medium heat, then add the eggs and crushed  pepper and lightly scramble the eggs. Avoid over cooking the eggs.\n" +
                    "Once  the noodles have drained, add them to the skillet with the eggs, then  drizzle the sauce over top. Toss the noodles and eggs to coat in the  sauce.\n" +
                    "Top the noodles with fresh cilantro and sliced green onion, then serve.",
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

@Composable
fun RecipeCardBig(modifier: Modifier) {

    Column(modifier = modifier
        .clip(RoundedCornerShape(20.dp))
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        .padding(10.dp)
        .fillMaxSize()
        .clip(RoundedCornerShape(20.dp))
    )
    {
        // titleBar




    }
}

