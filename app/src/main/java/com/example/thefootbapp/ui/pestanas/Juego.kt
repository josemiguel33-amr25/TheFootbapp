package com.example.thefootbapp.ui.pestanas


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.thefootbapp.ui.datos.FootBappViewModel

@Composable
fun PantallaJuego(
    viewModel: FootBappViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val jugador = uiState.jugadorAdivinar
    var textoUsuario by remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.size(300.dp).padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (jugador != null) {
                    Text(
                        text = "PISTAS",
                        style = MaterialTheme.typography.displayLarge,
                        textAlign = TextAlign.Center
                    )

                    if (uiState.intentosFallidos >= 1) {
                        Text(
                            text = "Edad: ${jugador.age}",
                            style = MaterialTheme.typography.displayMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (uiState.intentosFallidos >= 2) {
                        Text(
                            text = "Nacionalidad: ${jugador.nationality}",
                            style = MaterialTheme.typography.displayMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (uiState.intentosFallidos >= 3) {
                        Text(
                            text = "Posición: ${jugador.position}",
                            style = MaterialTheme.typography.displayMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (uiState.intentosFallidos >= 4) {
                        AsyncImage(
                            model = jugador.photo,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )
                    }

                    if (uiState.intentosFallidos == 0) {
                        Text(
                            text = "¿Quien es este jugador?",
                            style = MaterialTheme.typography.displayMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            value = textoUsuario,
            onValueChange = { textoUsuario = it },
            label = { Text("Nombre del jugador") },
            singleLine = true,
            modifier = Modifier.padding(horizontal = 32.dp).fillMaxWidth(),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFFF4E00),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.size(16.dp))

        Button(
            onClick = {
                viewModel.comprobarIntento(textoUsuario)
                textoUsuario = ""
            },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF4E00)
            )
        ) {
            Text("COMPROBAR")
        }

        if (uiState.juegoTerminado) {
            Text(
                text = "¡Perdiste! Era ${jugador?.name}",
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
            TextButton(onClick = { viewModel.iniciarJuego() }) {
                Text("REINTENTAR", color = Color.White)
            }
        }
    }
}