package com.example.thefootbapp.ui.pestanas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.thefootbapp.ui.datos.FootBappViewModel
import com.example.thefootbapp.ui.api.DatosPartidos
import com.example.thefootbapp.ui.api.EstadisticasPorEquipo

@Composable
fun BibliotecaPrincipal(
    viewModel: FootBappViewModel,
    tamanioVentana: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        SelectorTemporada(
            temporadaActual = uiState.temporadaAnio,
            onTemporadaElegida = { anio ->
                viewModel.anioSeleccionado(anio)
            }
        )

        if (uiState.listaPartidosJugados.isEmpty() && !uiState.cargandoDatos) { // esto es el mensaje de inicio por asi decirlo hasta que no se escoge año y la api carga no se quita
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 40.dp)
            ) {
                Text(
                    text = "Selecciona un año",
                    style = MaterialTheme.typography.displayLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "para ver",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "los fixtures",
                    style = MaterialTheme.typography.displayLarge,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFFF4E00)
                )
            }
        }

        if (uiState.cargandoDatos) { // el circulito de carga
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFFF4E00))
            }
        } else if (uiState.listaPartidosJugados.isNotEmpty()) {
            if (tamanioVentana == WindowWidthSizeClass.Expanded) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(uiState.listaPartidosJugados) { partido ->
                        TarjetaPartido(
                            partido = partido,
                            estadisticas = uiState.mapaEstadisticas,
                            clickTarjeta = { viewModel.cargarEstadisticas(partido.informacion.partidoId) }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(uiState.listaPartidosJugados) { partido ->
                        TarjetaPartido(
                            partido,
                            uiState.mapaEstadisticas,
                            clickTarjeta = { viewModel.cargarEstadisticas(partido.informacion.partidoId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectorTemporada(
    temporadaActual: Int,
    onTemporadaElegida: (Int) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    // Lista de las últimas temporadas estas son las que me dejaba la api gratuitamente
    val anios = listOf(2024, 2023, 2022)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = { expandido = !expandido },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1A1A1A),
                contentColor = Color.White
            )
        ) {
            Text(text = "Temporada: $temporadaActual")
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            anios.forEach { anio ->
                DropdownMenuItem(
                    text = { Text(text = anio.toString()) },
                    onClick = {
                        onTemporadaElegida(anio)
                        expandido = false
                    }
                )
            }
        }
    }
}

@Composable
fun TarjetaPartido(
    partido: DatosPartidos,
    estadisticas: Map<Int, List<EstadisticasPorEquipo>>,
    clickTarjeta: (Int) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    val estadisticasDelPartido = estadisticas[partido.informacion.partidoId]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = {
            expandido = !expandido //esto es para las estadisticas, comprobamos si es null para no tener que llamar a la informacion otra vez
            if (expandido && estadisticasDelPartido == null) {
                clickTarjeta(partido.informacion.partidoId)
            }
        },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Local
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    AsyncImage(
                        model = partido.equipos.local.escudoUrl,
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                    Text(
                        text = partido.equipos.local.nombre,
                        color = Color.White,
                        fontSize = 12.sp, maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                }

                // REsultado ejemplo 1-0 + estadio
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                )
                {
                    Text(
                        text = "${partido.goles.golesLocal ?: "-"} - ${partido.goles.golesVisitante ?: "-"}",
                        color = Color(0xFFFF4E00),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = partido.informacion.estadio?.nombreEstadio ?: "Estadio",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                }

                // Visitante
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                )
                {
                    AsyncImage(
                        model = partido.equipos.visitante.escudoUrl,
                        contentDescription = null,
                        modifier = Modifier.size(45.dp)
                    )
                    Text(
                        text = partido.equipos.visitante.nombre,
                        color = Color.White, fontSize = 12.sp,
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                }
            }
            if (expandido) {
                HorizontalDivider(
                    color = Color.Gray.copy(alpha = 0.3f),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp
                )

                if (estadisticasDelPartido == null) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFFF4E00),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    EstadisticasDetalle(estadisticasDelPartido)
                }
            }
        }
    }
}

@Composable
fun EstadisticasDetalle(estadisticasLista: List<EstadisticasPorEquipo>) {
    // si por alguna remota casualidad no hay datos del partido pues no se muestra nada porque no se me ocurre como tratar este caso, de todos modos creo que es imposible que no te de datos de un partido
    if (estadisticasLista.size < 2) return

    val LocalEstadisticas = estadisticasLista[0].estadisticas
    val VisitanteEstadisticas = estadisticasLista[1].estadisticas

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        //bulce para coger todas las estadisticas que nos quedan
        LocalEstadisticas.forEachIndexed { indice, parametroEstadistica ->
        //buscamos el campo con el mismo nombre que el de local (tipo es un atributo del objeto que tenemos declarado en el archivo api
            val equipoVisitante = VisitanteEstadisticas.find { it.tipo == parametroEstadistica.tipo }


            val valorIzquierda = parametroEstadistica.valor.toString().replace("\"", "").replace("null", "0")
            val valorDerecha = equipoVisitante?.valor.toString().replace("\"", "").replace("null", "0")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Datos local por tanto izquierda
                Text(
                    text = valorIzquierda,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                // Estadistica en ingles porque la api los devuelve asi
                Text(
                    text = parametroEstadistica.tipo,
                    modifier = Modifier.weight(2f),
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                // Datos visitante por tanto derecha
                Text(
                    text = valorDerecha,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Línea como la que usamos arriba (Linea separadora como bien dice el nombre)
            HorizontalDivider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.2f))
        }
    }
}




//FUNCION GENERICA LA USE DE MODELO, estaba en la web de android NO LA USO
@Composable
fun MinimalDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Option 1") },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Option 2") },
                onClick = { /* Do something... */ }
            )
        }
    }
}
