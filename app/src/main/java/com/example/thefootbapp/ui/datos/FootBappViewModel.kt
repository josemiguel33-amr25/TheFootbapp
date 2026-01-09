package com.example.thefootbapp.ui.datos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thefootbapp.ui.api.FootballApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FootBappViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FootbappEstado())
    val uiState: StateFlow<FootbappEstado> = _uiState.asStateFlow()


    // Función para actualizar la liga seleccionada en los botones
    fun actualizarLigaSeleccionada(nuevaLigaId: Int) {
        _uiState.update { estadoActual ->
            estadoActual.copy(liga = nuevaLigaId)
        }
    }


    //Esto para activar y desactivar el icono de cargar
    fun cargandoApi() {
        _uiState.update { it.copy(cargandoDatos = true) }
    }

    fun apiCargada() {
        _uiState.update { it.copy(cargandoDatos = false) }
    }

    //Esto para actaulizar el anio
    fun actualizarTemporada(anio: Int) {
        _uiState.update { it.copy(temporadaAnio = anio) }
    }

    fun actualizarLiga(idLiga: Int) {
        _uiState.update { it.copy(liga = idLiga) }
    }

    //Esta funcion la uso porque si yo me meto en la premier cargo partidos voy para atras y me meto en la liga, me saldrán los de la premier hasta que cambie de año
    fun limpiarLista() {
        _uiState.update { it.copy(
            listaPartidosJugados = emptyList()
        )}
    }

    fun anioSeleccionado(anio: Int) {
        actualizarTemporada(anio)
        cargandoApi()
        limpiarLista()

        //DEPUARCION
        Log.d("depuradora", "Iniciando búsqueda${_uiState.value.liga} $anio")
    viewModelScope.launch {
        try {
            val respuesta = FootballApi.retrofitService.getFixtures(
                apiKey = "9d56011ec0f4148724e3dcefaf86c061",
                idLiga = _uiState.value.liga,
                temporadaAnio = anio
            )

            apiCargada()
            _uiState.update { estadoActual ->
                estadoActual.copy(
                    listaPartidosJugados = respuesta.response
                )
            }

        } catch (e: Exception) {
            //Si hay un error paramos de cargar y mando un error a consola
            apiCargada()
            Log.e("depuradora", "${e.message}")
            Log.e("depuradora", "${e.cause}")
            //println("ERROR CARGANDO DATOS")
        }
    }
    }

    fun cargarEstadisticas(partidoId: Int) {
        // lo que explico en el estado
        if (_uiState.value.mapaEstadisticas.containsKey(partidoId)) return

        viewModelScope.launch {
            try {
                val respuesta = FootballApi.retrofitService.getEstadisticas(
                    apiKey = "9d56011ec0f4148724e3dcefaf86c061",
                    partidoId = partidoId
                )

                _uiState.update { estado -> // por lo que se ve en kotlin si usas mapa + a crea un mapa nuevo con todo lo anterior mas lo nuevo
                    estado.copy(
                        mapaEstadisticas = estado.mapaEstadisticas + (partidoId to respuesta.response)
                    )
                }

            } catch (e: Exception) {
                Log.e("depuradora", "${e.message}")
            }
        }
    }

    fun iniciarJuego() {
        _uiState.update { it.copy(
            jugadorAdivinar = null,
            intentosFallidos = 0,
            juegoTerminado = false
        )}

        val idAzar = (1..10000).random()

        viewModelScope.launch {
            try { // copia y pega de las mismas llamadas solo cambiamos parametros
                val respuesta = FootballApi.retrofitService.getPerfilJugador(
                    apiKey = "9d56011ec0f4148724e3dcefaf86c061",
                    id = idAzar
                )

                if (respuesta.response.isNotEmpty()) {
                    _uiState.update { it.copy(
                        jugadorAdivinar = respuesta.response[0].player
                    )}
                }
            } catch (e: Exception) {
                println("ERROR FATAL")
            }
        }
    }

    fun comprobarIntento(nombreIntroducido: String) {
        val jugador = _uiState.value.jugadorAdivinar ?: return

        if (nombreIntroducido.equals(jugador.name, ignoreCase = true)) {
            iniciarJuego()
        } else {
            val nuevosIntentos = _uiState.value.intentosFallidos + 1

            if (nuevosIntentos >= 5) {
                _uiState.update { it.copy(juegoTerminado = true) }
            } else {
                _uiState.update { it.copy(intentosFallidos = nuevosIntentos) }
            }
        }
    }

    fun obtenerEstadioAleatorio() {
        cargandoApi()

        viewModelScope.launch {
            try {
                val idAzar = (1..10000).random()
                val respuesta = FootballApi.retrofitService.getEstadioPorId(
                    apiKey = "9d56011ec0f4148724e3dcefaf86c061",
                    idEstadio = idAzar
                )

                if (respuesta.response.isNotEmpty()) {
                    _uiState.update { it.copy(
                        estadio = respuesta.response[0]
                    )}
                    apiCargada()
                } else {
                    obtenerEstadioAleatorio()
                }
            } catch (e: Exception) {
                apiCargada()
                Log.e("depuradora", "Error estadio: ${e.message}")
            }
        }
    }


}