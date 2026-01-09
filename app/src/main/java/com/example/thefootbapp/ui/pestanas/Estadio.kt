package com.example.thefootbapp.ui.pestanas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.thefootbapp.ui.datos.FootBappViewModel

@Composable
fun PantallaEstadios(
    viewModel: FootBappViewModel,
    tamanioVentana: WindowWidthSizeClass
) {
    val uiState by viewModel.uiState.collectAsState()
    val estadio = uiState.estadio

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(if (tamanioVentana == WindowWidthSizeClass.Expanded) 0.8f else 0.9f)
                .padding(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            if (uiState.cargandoDatos) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color(0xFFFF4E00))
                }
            } else if (estadio != null) {
                if (tamanioVentana == WindowWidthSizeClass.Expanded) {
                    Row(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AsyncImage(
                            model = estadio.imagen,
                            contentDescription = null,
                            modifier = Modifier.size(300.dp).padding(16.dp)
                        )
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = estadio.nombre,
                                style = MaterialTheme.typography.displayMedium,
                                color = Color(0xFFFF4E00)
                            )
                            Text(text = "Dirección: ${estadio.direccion}", color = Color.White)
                            Text(text = "Ciudad: ${estadio.ciudad}", color = Color.White)
                            Text(text = "País: ${estadio.pais}", color = Color.White)
                            Text(text = "Capacidad: ${estadio.capacidad} personas", color = Color.Gray)
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = estadio.imagen,
                            contentDescription = null,
                            modifier = Modifier.size(200.dp).padding(8.dp)
                        )
                        Text(
                            text = estadio.nombre,
                            style = MaterialTheme.typography.displayMedium,
                            color = Color(0xFFFF4E00),
                            textAlign = TextAlign.Center
                        )
                        Text(text = "Dirección: ${estadio.direccion}", color = Color.White)
                        Text(text = "Ciudad: ${estadio.ciudad}", color = Color.White)
                        Text(text = "País: ${estadio.pais}", color = Color.White)
                        Text(text = "Capacidad: ${estadio.capacidad} personas", color = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        Button(
            onClick = { viewModel.obtenerEstadioAleatorio() },
            colors = buttonColors(
                containerColor = Color(0xFFFF4E00)
            )
        ) {
            Text("VER OTRO ESTADIO")
        }
    }
}