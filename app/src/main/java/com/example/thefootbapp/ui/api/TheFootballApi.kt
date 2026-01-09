package com.example.thefootbapp.ui.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val URL = "https://v3.football.api-sports.io/"
//Esto lo ponemos porque mi api manda muchos datos y yo solo especifico en los objetos alguno por lo tanto si ve campos json no especificados explota
private val jsonConfig = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
    .baseUrl(URL)
    .build()

interface FootballApiService {
    @GET("fixtures")
    suspend fun getFixtures(
        @Header("x-rapidapi-key") apiKey: String,
        @Query("league") idLiga: Int,
        @Query("season") temporadaAnio: Int
    ): RespuestaApi

    @GET("fixtures/statistics")
    suspend fun getEstadisticas(
        @Header("x-rapidapi-key") apiKey: String,
        @Query("fixture") partidoId: Int
    ): RespuestaEstadisticas

    @GET("players/profiles")
    suspend fun getPerfilJugador(
        @Header("x-rapidapi-key") apiKey: String,
        @Query("player") id: Int
    ): RespuestaJugador

    @GET("venues")
    suspend fun getEstadioPorId(
        @Header("x-rapidapi-key") apiKey: String,
        @Query("id") idEstadio: Int
    ): RespuestaEstadio

}

object FootballApi {
    val retrofitService : FootballApiService by lazy {
        retrofit.create(FootballApiService::class.java)
    }
}


//Objetos para la serializacion
@Serializable
data class RespuestaApi(
    @SerialName("response")
    val response: List<DatosPartidos>
)

@Serializable
data class DatosPartidos (
    @SerialName("fixture")
    val informacion: DetallesFixture,

    @SerialName("teams")
    val equipos: EquiposCompitiendo,

    @SerialName("goals")
    val goles: MarcadorGoles
)

@Serializable
data class DetallesFixture(
    @SerialName("id")
    val partidoId: Int,

    @SerialName("date")
    val fechaIso: String,

    @SerialName("venue")
    val estadio: Estadio? = null
)

@Serializable
data class Estadio(
    @SerialName("name") val nombreEstadio: String? = null,
    @SerialName("city") val ciudad: String? = null
)

@Serializable
data class EquiposCompitiendo(
    @SerialName("home")
    val local: DatosEquipo,

    @SerialName("away")
    val visitante: DatosEquipo
)

@Serializable
data class DatosEquipo(
    @SerialName("id")
    val idEquipo: Int,

    @SerialName("name")
    val nombre: String,

    @SerialName("logo")
    val escudoUrl: String
)


@Serializable
data class MarcadorGoles( //pongo interrogacion porque si el partido ha quedado 0 0 va a devolver null la api
    @SerialName("home")
    val golesLocal: Int? = 0,

    @SerialName("away")
    val golesVisitante: Int? = 0
)


//estadisticas para los partidos expandidos

@Serializable
data class RespuestaEstadisticas(
    @SerialName("response")
    val response: List<EstadisticasPorEquipo>
)

@Serializable
data class EstadisticasPorEquipo(
    @SerialName("team")
    val equipo: DatosEquipo,

    @SerialName("statistics")
    val estadisticas: List<ItemEstadistica>
)

@Serializable
data class ItemEstadistica(
    @SerialName("type")
    val tipo: String, //  por ejemplo posesion o tiros

    @SerialName("value")
    val valor: JsonElement? // Usamos JsonElemen porque la api me da algunas veces numero otras string
)


//Juego (Solo lo uso para los juegos)

@Serializable
data class RespuestaJugador(
    @SerialName("response")
    val response: List<JugadorPerfilTotalidad>
)

@Serializable
data class JugadorPerfilTotalidad(
    @SerialName("player")
    val player: DatosJugador
)
 // Solo guardo los datos para el juego y ademas el json que me dan tiene pocos datos, pero no puedo llamar tantas veces a la api
@Serializable
data class DatosJugador(
    val id: Int,
    val name: String,
    val age: Int,
    val nationality: String,
    val position: String,
    val photo: String
)


//Para estadios aleatorios

@Serializable
data class RespuestaEstadio(
    @SerialName("response") val response: List<DatosEstadio>
)

@Serializable
data class DatosEstadio(
    @SerialName("name")
    val nombre: String,

    @SerialName("address")
    val direccion: String,

    @SerialName("city")
    val ciudad: String,

    @SerialName("country")
    val pais: String,

    @SerialName("capacity")
    val capacidad: Int,

    @SerialName("image")
    val imagen: String
)
