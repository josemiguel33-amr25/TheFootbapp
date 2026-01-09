package com.example.thefootbapp.ui.pestanas

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.thefootbapp.R
import com.example.thefootbapp.ui.datos.FootBappViewModel


enum class Pantalla(@StringRes val titulo: Int) {
    Inicio(titulo = R.string.app_name),
    Biblioteca(titulo = R.string.biblioteca),
    Partidos(titulo = R.string.partidos),
    Estadio(titulo = R.string.mas_info),
    Juego(titulo = R.string.juego_1),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    modifier: Modifier = Modifier,
    pantallaActual: Pantalla,
    navegarDelante: () -> Unit,
    navegarAtras: Boolean
    ) {
    CenterAlignedTopAppBar(
        title = {Text(stringResource(pantallaActual.titulo), style = MaterialTheme.typography.displayLarge) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(0xFFF5F5F5),
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier,
        navigationIcon = {
            if (navegarAtras) { //solo quiero que salga el logo cuando es la pantalla de inicio sino sale la flechita para atras
                IconButton(onClick = navegarDelante) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.atras)
                    )
                }
            } else {
                Image(
                    painter = painterResource(R.drawable.logomejorado),
                    contentDescription = "Logo de TheFootbapp",
                    modifier = Modifier.size(104.dp).padding(start = 24.dp)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheFootbapp(
    tamanioVentana: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val viewModel: FootBappViewModel = viewModel()
    val pilaPantallas by navController.currentBackStackEntryAsState()
    val pantallaActual = Pantalla.valueOf(pilaPantallas?.destination?.route ?: Pantalla.Inicio.name)


    Scaffold(
        topBar = {
            BarraSuperior(
                pantallaActual = pantallaActual,
                navegarDelante = { navController.navigateUp()},
                navegarAtras = navController.previousBackStackEntry != null
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = Pantalla.Inicio.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable (route = Pantalla.Inicio.name) {
                Inicio(
                    clickBiblioteca = { idLiga ->
                        viewModel.actualizarLiga(idLiga)
                        viewModel.limpiarLista()
                        navController.navigate(Pantalla.Biblioteca.name)
                    },
                    clickJuego = {
                        viewModel.iniciarJuego()
                        navController.navigate(Pantalla.Juego.name)
                    },
                    clickEstadio = {
                        viewModel.obtenerEstadioAleatorio()
                        navController.navigate(Pantalla.Estadio.name)
                    },
                    modifier = Modifier.fillMaxSize(),
                    tamanioVentana = tamanioVentana
                )
            }
            composable (route = Pantalla.Biblioteca.name) {
                BibliotecaPrincipal(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize(),
                    tamanioVentana = tamanioVentana
                )
            }
            composable (route = Pantalla.Juego.name) {
                PantallaJuego(
                    viewModel = viewModel
                )
            }
            composable (route = Pantalla.Estadio.name) {
                PantallaEstadios(
                    viewModel = viewModel,
                    tamanioVentana = tamanioVentana
                )
            }
        }
    }
}
