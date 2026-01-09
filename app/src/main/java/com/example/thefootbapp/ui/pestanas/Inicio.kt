package com.example.thefootbapp.ui.pestanas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.thefootbapp.R


@Composable
fun Inicio(
    clickBiblioteca: (Int) -> Unit,
    clickJuego: () -> Unit,
    clickEstadio: () -> Unit,
    tamanioVentana: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    //Esto lo usamos porque en las tablet se va de lado y no mostraba un texto, además tampoco mostraba el cuarto boton
    val paddingInferior = if (tamanioVentana == WindowWidthSizeClass.Expanded) 20.dp else 120.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp, bottom = paddingInferior),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = if (tamanioVentana == WindowWidthSizeClass.Expanded) 20.dp else 40.dp)
        ) {
            Text(
                text = "Bienvenido a",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "TheFootbapp",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "tu aplicación",
                style = MaterialTheme.typography.displayLarge,
            )
            Text(
                text = "de fútbol",
                style = MaterialTheme.typography.displayLarge,
                color = Color(0xFFFF4E00)
            )
        }

        if (tamanioVentana == WindowWidthSizeClass.Expanded) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    BotonMenuPrincipal(
                        texto = R.string.premier,
                        idImagen = R.drawable.premierleague_logo,
                        onClick = { clickBiblioteca(39) },
                        modifier = Modifier.width(300.dp)
                    )
                    BotonMenuPrincipal(
                        texto = R.string.liga_espanola,
                        idImagen = R.drawable.laligalogo,
                        onClick = { clickBiblioteca(140) },
                        modifier = Modifier.width(300.dp)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    BotonMenuPrincipal(
                        texto = R.string.juego_1,
                        idImagen = R.drawable.jugadorgenerico,
                        onClick = clickJuego,
                        modifier = Modifier.width(300.dp)
                    )
                    BotonMenuPrincipal(
                        texto = R.string.estadio,
                        idImagen = R.drawable.estadio,
                        onClick = clickEstadio,
                        modifier = Modifier.width(300.dp)
                    )
                }
            }
        } else {
            BotonMenuPrincipal(
                texto = R.string.premier,
                idImagen = R.drawable.premierleague_logo,
                onClick = { clickBiblioteca(39) }
            )
            BotonMenuPrincipal(
                texto = R.string.liga_espanola,
                idImagen = R.drawable.laligalogo,
                onClick = { clickBiblioteca(140) }
            )
            BotonMenuPrincipal(
                texto = R.string.juego_1,
                idImagen = R.drawable.jugadorgenerico,
                onClick = clickJuego
            )
            BotonMenuPrincipal(
                texto = R.string.estadio,
                idImagen = R.drawable.estadio,
                onClick = clickEstadio
            )
        }
    }
}

@Composable
fun BotonMenuPrincipal(
    texto: Int,
    idImagen: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(0.8f)
            .padding(bottom = 16.dp)
            .height(60.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = idImagen),
                contentDescription = null, //null porque en cada llamada le paso una imagen distinta
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = stringResource(texto),
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}