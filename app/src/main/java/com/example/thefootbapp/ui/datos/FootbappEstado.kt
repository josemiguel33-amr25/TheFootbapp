package com.example.thefootbapp.ui.datos

import com.example.thefootbapp.ui.api.DatosEstadio
import com.example.thefootbapp.ui.api.DatosJugador
import com.example.thefootbapp.ui.api.DatosPartidos
import com.example.thefootbapp.ui.api.EstadisticasPorEquipo

data class FootbappEstado (
    // Partidos
    val temporadaAnio: Int = 2023,
    val liga: Int = 0,
    val listaPartidosJugados: List<DatosPartidos> = emptyList(),

    //Estadisticas partidos  esto es importante porque sin el mapa tendría que cada vez que expando un partido volver a pedirle a la api los partidos y me fundiría los creditos además que obviamente no es eficiente
    val mapaEstadisticas: Map<Int, List<EstadisticasPorEquipo>> = emptyMap(),

    //Para Adivina El Jugador
    val idJugador: Int = 0,
    val jugadorAdivinar: DatosJugador? = null, //El objeto que devuelve la api
    val intentosFallidos: Int = 0,
    val juegoTerminado: Boolean = false,
    val textoIntroducido: String = "",


    //Estadio ID
    val idEstadio: Int = 0,
    val estadio: DatosEstadio? = null,
    //Variables para la api
    val cargandoDatos: Boolean = true
)