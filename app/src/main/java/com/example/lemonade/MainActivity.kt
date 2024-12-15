package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeTheme {
                LemonadeApp()
            }
        }
    }
}

@Composable
fun LemonadeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color(0xFFFFA500),  // Bright orange for primary color
            background = Color(0xFFFFF44F),  // Bright, lemony yellow background
            onBackground = Color.Black  // Black text
        )
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LemonadeApp() {
    var step by remember { mutableIntStateOf(1) }
    var squeezeCount by remember { mutableIntStateOf((2..4).random()) }
    var currentSqueeze by remember { mutableIntStateOf(0) }

    val content = when (step) {
        1 -> LemonadeStep(
            text = stringResource(R.string.tap_lemon_tree),
            imageRes = R.drawable.lemon_tree,
            contentDesc = stringResource(R.string.desc_lemon_tree)
        ) {
            step = 2
        }

        2 -> LemonadeStep(
            text = stringResource(R.string.tap_lemon),
            imageRes = R.drawable.lemon_squeeze,
            contentDesc = stringResource(R.string.desc_lemon)
        ) {
            currentSqueeze++
            if (currentSqueeze >= squeezeCount) {
                step = 3
                currentSqueeze = 0
            }
        }

        3 -> LemonadeStep(
            text = stringResource(R.string.tap_lemonade),
            imageRes = R.drawable.lemon_drink,
            contentDesc = stringResource(R.string.desc_glass_of_lemonade)
        ) {
            step = 4
        }

        4 -> LemonadeStep(
            text = stringResource(R.string.tap_empty_glass),
            imageRes = R.drawable.lemon_restart,
            contentDesc = stringResource(R.string.desc_empty_glass)
        ) {
            step = 1
            squeezeCount = (2..4).random()
        }

        else -> LemonadeStep("", 0, "") {}
    }

    LemonadeScreen(
        text = content.text,
        imageRes = content.imageRes,
        contentDescription = content.contentDesc,
        onImageClick = content.onImageClick
    )
}

data class LemonadeStep(
    val text: String,
    val imageRes: Int,
    val contentDesc: String,
    val onImageClick: () -> Unit
)

@Composable
fun LemonadeScreen(
    text: String,
    imageRes: Int,
    contentDescription: String,
    onImageClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.9f else 1f, label = "image-scale")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Fond blanc pour tout l'écran
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF44F)) // Fond jaune pour le titre
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.app_name), // Titre principal
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                color = Color.Black // Texte noir
            )
        }

        // Contenu centré (texte + image)
        Column(
            Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .background(Color.White), // Fond blanc pour le contenu
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image avec effet de clic
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(250.dp)
                    .scale(scale)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFFFE4E1))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false, color = Color(0xFFFFA500)),
                        onClick = {
                            isPressed = true
                            onImageClick()
                            isPressed = false
                        }
                    )
            )

            // Texte de l'étape actuelle
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}