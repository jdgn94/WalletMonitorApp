package app.jdgn.walletmonitor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun DiagonalBanner(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.error,
    textColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .size(width = 300.dp, height = 50.dp) // Tamaño más ajustado para la esquina
            .offset(x = 22.dp, y = (-22).dp)
            .zIndex(10f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .rotate(45f)
                .background(backgroundColor, MaterialTheme.shapes.medium)
                .fillMaxWidth(), // Altura de cinta más delgada
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 9.sp, // Tamaño de fuente ligeramente menor para que quepa bien
                fontWeight = FontWeight.Black,
                maxLines = 1
            )
        }
    }
}
