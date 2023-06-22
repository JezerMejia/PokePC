package com.jezerm.pokepc.navigation

import android.os.Build
import android.view.WindowInsetsController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jezerm.pokepc.entities.Item
import com.jezerm.pokepc.entities.Recipe
import com.jezerm.pokepc.ui.components.BorderedButton
import com.jezerm.pokepc.ui.components.TextShadow
import com.jezerm.pokepc.ui.modifiers.insetBorder
import com.jezerm.pokepc.ui.modifiers.outsetBorder
import com.jezerm.pokepc.ui.theme.PokePCTheme

@Composable
fun RecipeGrid(recipe: Recipe) {
    val ingredients = ArrayList<Pair<Item, Int>>()

    for (i in 1..9) {
        val (item, pos) = recipe.items.find { v -> v.second == i } ?: Pair(Item.AIR, i)
        ingredients.add(item to pos)
    }

    LazyVerticalGrid(
        modifier = Modifier
            .widthIn(100.dp, 164.dp)
            .heightIn(100.dp, 164.dp),
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        userScrollEnabled = false
    ) {
        items(ingredients, key = { c -> c.second }) { (item, position) ->
            val imageBitmap = ImageBitmap.imageResource(item.image)
            Surface(color = Color(139, 139, 139)) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clip(RectangleShape)
                        .insetBorder(lightSize = 4.dp, darkSize = 4.dp, borderPadding = 0.dp)
                        .padding(4.dp),
                    bitmap = imageBitmap,
                    filterQuality = FilterQuality.None,
                    contentDescription = item.value,
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.Center
                )
            }
        }
    }
}

@Composable
fun RecipeContainer(recipe: Recipe) {
    val grayColor = Color(198, 198, 198)

    Card(
        modifier = Modifier
            .clip(RectangleShape)
            .outsetBorder(lightSize = 8.dp, darkSize = 10.dp, borderPadding = 0.dp),
        shape = RectangleShape,
        backgroundColor = grayColor
    ) {
        Column(
            Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RecipeGrid(recipe)
            TextShadow(text = recipe.result.value, fontWeight = FontWeight.Bold)
            val imageBitmap = ImageBitmap.imageResource(recipe.result.image)
            Surface(color = Color(139, 139, 139)) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RectangleShape)
                        .insetBorder(lightSize = 4.dp, darkSize = 4.dp, borderPadding = 0.dp)
                        .padding(6.dp),
                    bitmap = imageBitmap,
                    filterQuality = FilterQuality.None,
                    contentDescription = recipe.result.value,
                    contentScale = ContentScale.FillWidth,
                    alignment = Alignment.Center
                )
            }
        }
    }
}

fun LazyGridScope.RecipeCards() {
    val goalRecipies: List<Recipe> = listOf(
        Recipe.CAKE,
        Recipe.BEACON,
    )
    val basicRecipes: List<Recipe> = listOf(
        Recipe.STICK,
        Recipe.BUCKET,
        Recipe.IRON_PICKAXE
    )

    item(span = { GridItemSpan(2) }) {
        TextShadow(
            modifier = Modifier.fillMaxWidth(),
            text = "Objetivos",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4
        )
    }
    items(goalRecipies, key = { c -> c.result }) { recipe ->
        RecipeContainer(recipe)
    }
    item(span = { GridItemSpan(2) }) {
        TextShadow(
            modifier = Modifier.fillMaxWidth(),
            text = "Recetas básicas",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h4
        )
    }
    items(basicRecipes, key = { c -> c.result }) { recipe ->
        RecipeContainer(recipe)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeView(controller: NavHostController) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            systemUiController.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        systemUiController.setStatusBarColor(color = Color(0, 0, 0, 100))
        onDispose { }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            Surface(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0, 0, 0, 100))
                    .displayCutoutPadding()
            ) {
            }
        },
        bottomBar = {
            Surface(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0, 0, 0, 100))
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp, top = 8.dp),
            ) {
                BorderedButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        controller.navigate("poke")
                    }
                ) {
                    TextShadow(text = "Abrir AR", style = MaterialTheme.typography.button)
                }
            }
        },
        backgroundColor = Color.Transparent,
        contentColor = MaterialTheme.colors.onSurface,
    ) { innerPadding ->
        Surface(Modifier.padding(innerPadding), color = Color.Transparent) {
            Column(
                Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    userScrollEnabled = true
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextShadow(text = "ARCraft", style = MaterialTheme.typography.h1)
                            TextShadow(
                                text = "Minecraft en Realidad Aumentada",
                                style = MaterialTheme.typography.h3,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    item(span = { GridItemSpan(2) }) {
                        BorderedButton(
                            modifier = Modifier.padding(bottom = 12.dp),
                            onClick = {
                                controller.navigate("howto")
                            }
                        ) {
                            TextShadow(text = "¿Cómo se juega?", fontWeight = FontWeight.Bold)
                        }
                    }

                    RecipeCards()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    PokePCTheme {
        HomeView(rememberNavController())
    }
}