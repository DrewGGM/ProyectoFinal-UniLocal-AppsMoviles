package com.example.primeraplicacionprueba.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.primeraplicacionprueba.model.CreatePlaceState
import com.example.primeraplicacionprueba.model.Place
import com.example.primeraplicacionprueba.model.PlaceType
import com.example.primeraplicacionprueba.model.Location
import com.example.primeraplicacionprueba.model.Day
import com.example.primeraplicacionprueba.model.PlaceStatus
import com.example.primeraplicacionprueba.model.Review
import com.example.primeraplicacionprueba.model.Shedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class PlacesViewModel : ViewModel() {

    private val _places = MutableStateFlow(emptyList<Place>())
    val places: StateFlow<List<Place>> = _places.asStateFlow()

    // Estado para creación de lugares
    private val _createPlaceState = MutableStateFlow(CreatePlaceState())
    val createPlaceState: StateFlow<CreatePlaceState> = _createPlaceState.asStateFlow()
    
    // Reference to ReviewViewModel for getting reviews
    private var reviewViewModel: ReviewViewModel? = null

    init {
        loadPlaces()
    }
    
    fun setReviewViewModel(reviewViewModel: ReviewViewModel) {
        this.reviewViewModel = reviewViewModel
    }
    
    fun getReviewsForPlace(placeId: String): List<Review> {
        return reviewViewModel?.getReviewsByPlace(placeId) ?: emptyList()
    }
    
    fun getAverageRatingForPlace(placeId: String): Float {
        val reviews = getReviewsForPlace(placeId)
        return if (reviews.isNotEmpty()) {
            reviews.map { it.rating }.average().toFloat()
        } else 0f
    }
    
    fun getReviewCountForPlace(placeId: String): Int {
        return getReviewsForPlace(placeId).size
    }

    fun loadPlaces() {
        if (_places.value.isNotEmpty()) return
        _places.value = listOf(
            Place(
                id = "p1",
                title = "Café Central",
                imagenes = listOf(
                    "https://cdn.pixabay.com/photo/2016/11/18/19/13/buildings-1836478_1280.jpg",
                    "https://cdn.pixabay.com/photo/2022/07/14/00/36/cafe-7320242_1280.jpg"
                ),
                description = "Un café acogedor con postres artesanales y ambiente relajado perfecto para trabajar o estudiar.",
                phones = listOf("+57 300 111 2233", "+57 6 123 4567"),
                type = PlaceType.CAFE,
                shedule = listOf(
                    Shedule(
                        Day.MONDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(20, 0)
                    ),
                    Shedule(
                        Day.TUESDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(20, 0)
                    ),
                    Shedule(
                        Day.WEDNESDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(20, 0)
                    ),
                    Shedule(
                        Day.THURSDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(20, 0)
                    ),
                    Shedule(
                        Day.FRIDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(20, 0)
                    ),
                    Shedule(
                        Day.SATURDAY,
                        LocalTime.of(9, 0),
                        LocalTime.of(18, 0)
                    )
                ),
                location = Location(latitude = 4.8143, longitude = -75.6946),
                adress = "Calle 10 #12-34, Centro",
                website = "www.cafecentral.com",
                email = "info@cafecentral.com",
                socialMedia = "@cafecentral_med",
                reviews = listOf(
                    Review(
                        "r1",
                        "1",
                        "Juan Camilo",
                        "p1",
                        5,
                        "Una experiencia increíble. La comida deliciosa y el servicio excepcional. ¡Totalmente recomendado!",
                        LocalDateTime.now().minusDays(2)
                    ),
                    Review(
                        "r2",
                        "2",
                        "Andrew",
                        "p1",
                        4,
                        "Gran lugar para pasar la tarde. El café es excelente y el ambiente muy acogedor. Volveré pronto.",
                        LocalDateTime.now().minusDays(5)
                    )
                ),
                city = "Armenia",
                neighborhood = "Centro",
                priceRange = "$$",
                amenities = listOf(
                    "WiFi Gratis",
                    "Aire Acondicionado",
                    "Terraza",
                    "Estacionamiento"
                ),
                isVerified = true,
                ownerId = "1",
                viewCount = 1250,
                favoriteCount = 89,
                createdDate = LocalDate.now(),
            ),
            Place(
                id = "p2",
                title = "La Parrilla Q",
                imagenes = listOf(
                    "https://image-tc.galaxy.tf/wiwebp-4n7ax7h9txbjrk6qbi3pf3tup/restaurante-vedana-ananda-hotel-boutique-cartagena-restaurante-1-11zon.webp?width=1920"
                ),
                description = "Restaurante especializado en carnes a la brasa con más de 15 años de experiencia.",
                phones = listOf("+57 310 555 8899"),
                type = PlaceType.RESTAURANT,
                shedule = listOf(
                    Shedule(
                        Day.MONDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.TUESDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.WEDNESDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.THURSDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.FRIDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.SATURDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.SUNDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(22, 0)
                    )
                ),
                location = Location(latitude = 4.5353, longitude = -75.6750),
                adress = "Av. Bolívar 45-67",
                website = "www.laparrillaq.com",
                email = "reservas@laparrillaq.com",
                socialMedia = "@laparrillaq",
                reviews = listOf(
                    Review(
                        "r3",
                        "1",
                        "Juan Camilo",
                        "p2",
                        5,
                        "Las mejores carnes de la ciudad. El ambiente es perfecto para una cena romántica.",
                        LocalDateTime.now().minusDays(1)
                    ),
                    Review(
                        "r4",
                        "2",
                        "Andrew",
                        "p2",
                        4,
                        "Excelente calidad de carne y buen servicio. Un poco caro pero vale la pena.",
                        LocalDateTime.now().minusDays(3)
                    ),
                    Review(
                        "r5",
                        "1",
                        "Juan Camilo",
                        "p2",
                        3,
                        "Buen lugar pero la espera fue muy larga. La comida estuvo bien.",
                        LocalDateTime.now().minusDays(7)
                    )
                ),
                city = "Pereira",
                neighborhood = "El Poblado",
                priceRange = "$$$",
                amenities = listOf(
                    "Reservas",
                    "Delivery",
                    "Terraza",
                    "Bar",
                    "Estacionamiento Valet"
                ),
                isVerified = true,
                ownerId = "2",
                viewCount = 890,
                favoriteCount = 156,
                createdDate = LocalDate.now(),
            ),
            // Más lugares de Armenia
            Place(
                id = "p3",
                title = "Museo del Oro Quimbaya",
                imagenes = listOf(
                    "https://fincasejecafetero.com/wp-content/uploads/2021/02/destino_museo_quimbaya.jpg"
                ),
                description = "Museo que alberga una de las colecciones de orfebrería prehispánica más importantes del país.",
                phones = listOf("+57 6 745 8161"),
                type = PlaceType.MUSEUM,
                shedule = listOf(
                    Shedule(
                        Day.TUESDAY,
                        LocalTime.of(9, 0),
                        LocalTime.of(17, 0)
                    ),
                    Shedule(
                        Day.WEDNESDAY,
                        LocalTime.of(9, 0),
                        LocalTime.of(17, 0)
                    ),
                    Shedule(
                        Day.THURSDAY,
                        LocalTime.of(9, 0),
                        LocalTime.of(17, 0)
                    ),
                    Shedule(
                        Day.FRIDAY,
                        LocalTime.of(9, 0),
                        LocalTime.of(17, 0)
                    ),
                    Shedule(
                        Day.SATURDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(16, 0)
                    ),
                    Shedule(
                        Day.SUNDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(16, 0)
                    )
                ),
                location = Location(latitude = 4.8143, longitude = -75.6946),
                adress = "Av. Bolívar 40N-80",
                website = "www.banrepcultural.org/armenia",
                email = "armenia@banrepcultural.org",
                socialMedia = "@museodeloro",
                reviews = listOf(
                    Review(
                        "r6",
                        "3",
                        "María González",
                        "p3",
                        5,
                        "Un museo increíble con una colección impresionante. Muy educativo y bien organizado.",
                        LocalDateTime.now().minusDays(4)
                    ),
                    Review(
                        "r7",
                        "4",
                        "Carlos Ruiz",
                        "p3",
                        4,
                        "Excelente lugar para conocer la cultura prehispánica. La entrada es gratuita.",
                        LocalDateTime.now().minusDays(6)
                    )
                ),
                city = "Armenia",
                neighborhood = "Centro",
                priceRange = "Gratis",
                amenities = listOf(
                    "Entrada Gratuita",
                    "Guías",
                    "Tienda de Souvenirs",
                    "Estacionamiento"
                ),
                isVerified = true,
                ownerId = "3",
                viewCount = 2100,
                favoriteCount = 234,
                createdDate = LocalDate.now(),
            ),
            Place(
                id = "p4",
                title = "Parque de la Vida",
                imagenes = listOf(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSpXddd6C-4g1upI3rcMpQFgqh5FUi7ryMZg&s"
                ),
                description = "Parque ecológico con senderos, lagos artificiales y áreas de recreación familiar.",
                phones = listOf("+57 6 745 1234"),
                type = PlaceType.PARK,
                shedule = listOf(
                    Shedule(
                        Day.MONDAY,
                        LocalTime.of(6, 0),
                        LocalTime.of(18, 0)
                    ),
                    Shedule(
                        Day.TUESDAY,
                        LocalTime.of(6, 0),
                        LocalTime.of(18, 0)
                    ),
                    Shedule(
                        Day.WEDNESDAY,
                        LocalTime.of(6, 0),
                        LocalTime.of(18, 0)
                    ),
                    Shedule(
                        Day.THURSDAY,
                        LocalTime.of(6, 0),
                        LocalTime.of(18, 0)
                    ),
                    Shedule(
                        Day.FRIDAY,
                        LocalTime.of(6, 0),
                        LocalTime.of(18, 0)
                    ),
                    Shedule(
                        Day.SATURDAY,
                        LocalTime.of(6, 0),
                        LocalTime.of(19, 0)
                    ),
                    Shedule(Day.SUNDAY, LocalTime.of(6, 0), LocalTime.of(19, 0))
                ),
                location = Location(latitude = 4.8200, longitude = -75.6800),
                adress = "Carrera 14 #23-45",
                website = "www.parquedelavida.com",
                email = "info@parquedelavida.com",
                socialMedia = "@parquedelavida",
                reviews = listOf(
                    Review(
                        "r8",
                        "5",
                        "Ana Martínez",
                        "p4",
                        5,
                        "Perfecto para caminar y hacer ejercicio. Muy bien mantenido.",
                        LocalDateTime.now().minusDays(2)
                    ),
                    Review(
                        "r9",
                        "6",
                        "Luis Pérez",
                        "p4",
                        4,
                        "Excelente lugar para pasar en familia. Los niños se divierten mucho.",
                        LocalDateTime.now().minusDays(5)
                    )
                ),
                city = "Armenia",
                neighborhood = "La Castellana",
                priceRange = "Gratis",
                amenities = listOf("Senderos", "Lagos", "Área de Juegos", "Ciclovía", "Baños"),
                isVerified = true,
                ownerId = "4",
                viewCount = 1800,
                favoriteCount = 189,
                createdDate = LocalDate.now(),
            ),
            Place(
                id = "p5",
                title = "Centro Comercial Portal del Quindío",
                imagenes = listOf(
                    "https://www.naturalezaydescanso.net/wp-content/uploads/2020/04/principal-1024x499-1.png"
                ),
                description = "Centro comercial con múltiples tiendas, restaurantes, cine y entretenimiento.",
                phones = listOf("+57 6 745 5678"),
                type = PlaceType.SHOPPING,
                shedule = listOf(
                    Shedule(
                        Day.MONDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(21, 0)
                    ),
                    Shedule(
                        Day.TUESDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(21, 0)
                    ),
                    Shedule(
                        Day.WEDNESDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(21, 0)
                    ),
                    Shedule(
                        Day.THURSDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(21, 0)
                    ),
                    Shedule(
                        Day.FRIDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.SATURDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.SUNDAY,
                        LocalTime.of(11, 0),
                        LocalTime.of(21, 0)
                    )
                ),
                location = Location(latitude = 4.8100, longitude = -75.7000),
                adress = "Carrera 19 #14-25",
                website = "www.portaldelquindio.com",
                email = "info@portaldelquindio.com",
                socialMedia = "@portaldelquindio",
                reviews = listOf(
                    Review(
                        "r10",
                        "7",
                        "Patricia López",
                        "p5",
                        4,
                        "Buen centro comercial con variedad de tiendas. El estacionamiento es amplio.",
                        LocalDateTime.now().minusDays(3)
                    ),
                    Review(
                        "r11",
                        "8",
                        "Roberto Silva",
                        "p5",
                        3,
                        "Muchas opciones de comida y entretenimiento. A veces está muy lleno.",
                        LocalDateTime.now().minusDays(7)
                    )
                ),
                city = "Armenia",
                neighborhood = "Centro",
                priceRange = "$$",
                amenities = listOf(
                    "Estacionamiento",
                    "Cine",
                    "Comida Rápida",
                    "Tiendas",
                    "Cajeros"
                ),
                isVerified = true,
                ownerId = "5",
                viewCount = 3200,
                favoriteCount = 456,
                createdDate = LocalDate.now(),
            ),
            // Más lugares de Pereira
            Place(
                id = "p6",
                title = "Termales de Santa Rosa de Cabal",
                imagenes = listOf(
                    "https://caracol.com.co/resizer/v2/IWP6WRFIN5CDVIHVPH5GROISU4.png?auth=5efba1ff758da254af29bed7a46ddb18e91f94e5324e3f01c6c3d321c7437a8c&width=768&quality=70&smart=true"
                ),
                description = "Termales naturales con aguas termales medicinales y hermosos paisajes de montaña.",
                phones = listOf("+57 6 364 1234"),
                type = PlaceType.OTHER,
                shedule = listOf(
                    Shedule(
                        Day.MONDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(18, 0)
                    ),
                    Shedule(
                        Day.TUESDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(18, 0)
                    ),
                    Shedule(
                        Day.WEDNESDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(18, 0)
                    ),
                    Shedule(
                        Day.THURSDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(18, 0)
                    ),
                    Shedule(
                        Day.FRIDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(18, 0)
                    ),
                    Shedule(
                        Day.SATURDAY,
                        LocalTime.of(7, 0),
                        LocalTime.of(19, 0)
                    ),
                    Shedule(Day.SUNDAY, LocalTime.of(7, 0), LocalTime.of(19, 0))
                ),
                location = Location(latitude = 4.5353, longitude = -75.6750),
                adress = "Vía Santa Rosa de Cabal",
                website = "www.termalessantarosa.com",
                email = "reservas@termalessantarosa.com",
                socialMedia = "@termalessantarosa",
                reviews = listOf(
                    Review(
                        "r12",
                        "9",
                        "Isabel Torres",
                        "p6",
                        5,
                        "Una experiencia relajante única. Las aguas termales son increíbles.",
                        LocalDateTime.now().minusDays(1)
                    ),
                    Review(
                        "r13",
                        "10",
                        "Miguel Herrera",
                        "p6",
                        4,
                        "Muy relajante, pero hay que llegar temprano para evitar multitudes.",
                        LocalDateTime.now().minusDays(4)
                    )
                ),
                city = "Pereira",
                neighborhood = "Santa Rosa de Cabal",
                priceRange = "$$$",
                amenities = listOf(
                    "Aguas Termales",
                    "Restaurante",
                    "Hospedaje",
                    "Senderos",
                    "Estacionamiento"
                ),
                isVerified = true,
                ownerId = "6",
                viewCount = 2800,
                favoriteCount = 567,
                createdDate = LocalDate.now(),
            ),
            Place(
                id = "p7",
                title = "Centro Comercial Pereira Plaza",
                imagenes = listOf(
                    "https://www.eldiario.com.co/wp-content/uploads/2020/08/8fc91574-7a5e-486c-92d1-acd9c334fe2e.jpg"
                ),
                description = "Centro comercial moderno con tiendas de marcas reconocidas, restaurantes y entretenimiento.",
                phones = listOf("+57 6 315 1234"),
                type = PlaceType.SHOPPING,
                shedule = listOf(
                    Shedule(
                        Day.MONDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(21, 0)
                    ),
                    Shedule(
                        Day.TUESDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(21, 0)
                    ),
                    Shedule(
                        Day.WEDNESDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(21, 0)
                    ),
                    Shedule(
                        Day.THURSDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(21, 0)
                    ),
                    Shedule(
                        Day.FRIDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.SATURDAY,
                        LocalTime.of(10, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.SUNDAY,
                        LocalTime.of(11, 0),
                        LocalTime.of(21, 0)
                    )
                ),
                location = Location(latitude = 4.5353, longitude = -75.6750),
                adress = "Carrera 7 #30-20",
                website = "www.pereiraplaza.com",
                email = "info@pereiraplaza.com",
                socialMedia = "@pereiraplaza",
                reviews = listOf(
                    Review(
                        "r14",
                        "11",
                        "Carmen Vargas",
                        "p7",
                        4,
                        "Excelente centro comercial con muchas opciones de compras y comida.",
                        LocalDateTime.now().minusDays(2)
                    ),
                    Review(
                        "r15",
                        "12",
                        "Fernando Castro",
                        "p7",
                        5,
                        "Muy moderno y bien organizado. El estacionamiento es cómodo.",
                        LocalDateTime.now().minusDays(6)
                    )
                ),
                city = "Pereira",
                neighborhood = "Centro",
                priceRange = "$$",
                amenities = listOf(
                    "Estacionamiento",
                    "Cine",
                    "Restaurantes",
                    "Tiendas",
                    "Cajeros",
                    "WiFi"
                ),
                isVerified = true,
                ownerId = "7",
                viewCount = 4100,
                favoriteCount = 678,
                createdDate = LocalDate.now(),
            ),
            Place(
                id = "p8",
                title = "Parque Olaya Herrera",
                imagenes = listOf(
                    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSpXddd6C-4g1upI3rcMpQFgqh5FUi7ryMZg&s"
                ),
                description = "Parque urbano con áreas verdes, juegos infantiles y espacios para deportes.",
                phones = listOf("+57 6 315 5678"),
                type = PlaceType.PARK,
                shedule = listOf(
                    Shedule(
                        Day.MONDAY,
                        LocalTime.of(6, 0),
                        LocalTime.of(20, 0)
                    ),
                    Shedule(
                        Day.TUESDAY,
                        LocalTime.of(6, 0),
                        LocalTime.of(20, 0)
                    ),
                    Shedule(
                        Day.WEDNESDAY,
                        LocalTime.of(6, 0),
                        LocalTime.of(20, 0)
                    ),
                    Shedule(
                        Day.THURSDAY,
                        LocalTime.of(6, 0),
                        LocalTime.of(20, 0)
                    ),
                    Shedule(
                        Day.FRIDAY,
                        LocalTime.of(6, 0),
                        LocalTime.of(20, 0)
                    ),
                    Shedule(
                        Day.SATURDAY,
                        LocalTime.of(6, 0),
                        LocalTime.of(21, 0)
                    ),
                    Shedule(Day.SUNDAY, LocalTime.of(6, 0), LocalTime.of(21, 0))
                ),
                location = Location(latitude = 4.5353, longitude = -75.6750),
                adress = "Carrera 13 #15-30",
                website = "www.parqueolayaherrera.com",
                email = "info@parqueolayaherrera.com",
                socialMedia = "@parqueolayaherrera",
                reviews = listOf(
                    Review(
                        "r16",
                        "13",
                        "Gloria Morales",
                        "p8",
                        4,
                        "Buen parque para hacer ejercicio y pasar tiempo en familia.",
                        LocalDateTime.now().minusDays(3)
                    ),
                    Review(
                        "r17",
                        "14",
                        "Jorge Ramírez",
                        "p8",
                        5,
                        "Excelente lugar para correr y hacer deporte. Muy bien mantenido.",
                        LocalDateTime.now().minusDays(8)
                    )
                ),
                city = "Pereira",
                neighborhood = "Centro",
                priceRange = "Gratis",
                amenities = listOf(
                    "Área de Juegos",
                    "Canchas Deportivas",
                    "Ciclovía",
                    "Baños",
                    "Iluminación"
                ),
                isVerified = true,
                ownerId = "8",
                viewCount = 1900,
                favoriteCount = 234,
                createdDate = LocalDate.now(),
            ),
            Place(
                id = "p9",
                title = "Restaurante El Fogón",
                imagenes = listOf(
                    "https://images.pexels.com/photos/262047/pexels-photo-262047.jpeg"
                ),
                description = "Restaurante tradicional colombiano especializado en comida típica de la región cafetera.",
                phones = listOf("+57 6 315 9999"),
                type = PlaceType.RESTAURANT,
                shedule = listOf(
                    Shedule(
                        Day.MONDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.TUESDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.WEDNESDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.THURSDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(22, 0)
                    ),
                    Shedule(
                        Day.FRIDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(23, 0)
                    ),
                    Shedule(
                        Day.SATURDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(23, 0)
                    ),
                    Shedule(
                        Day.SUNDAY,
                        LocalTime.of(12, 0),
                        LocalTime.of(22, 0)
                    )
                ),
                location = Location(latitude = 4.5353, longitude = -75.6750),
                adress = "Calle 20 #8-45",
                website = "www.elfogonpereira.com",
                email = "reservas@elfogonpereira.com",
                socialMedia = "@elfogonpereira",
                reviews = listOf(
                    Review(
                        "r18",
                        "15",
                        "Sandra Jiménez",
                        "p9",
                        5,
                        "La mejor comida típica de Pereira. Los platos son auténticos y deliciosos.",
                        LocalDateTime.now().minusDays(1)
                    ),
                    Review(
                        "r19",
                        "16",
                        "Diego Mendoza",
                        "p9",
                        4,
                        "Excelente ambiente y comida tradicional. Muy recomendado para turistas.",
                        LocalDateTime.now().minusDays(5)
                    )
                ),
                city = "Pereira",
                neighborhood = "Centro",
                priceRange = "$$",
                amenities = listOf(
                    "Comida Típica",
                    "Reservas",
                    "Delivery",
                    "Terraza",
                    "Estacionamiento"
                ),
                isVerified = true,
                ownerId = "9",
                viewCount = 1500,
                favoriteCount = 345,
                createdDate = LocalDate.now(),
                placeStatus = PlaceStatus.APPROVED
            ),
            // Lugares pendientes para moderación
            Place(
                id = "p10",
                title = "Panadería El Trigal",
                imagenes = listOf("https://cdn.pixabay.com/photo/2016/11/18/19/13/buildings-1836478_1280.jpg"),
                description = "Panadería tradicional con productos artesanales",
                phones = listOf("+57 300 111 2233"),
                type = PlaceType.OTHER,
                shedule = listOf(
                    Shedule(Day.MONDAY, LocalTime.of(6, 0), LocalTime.of(18, 0)),
                    Shedule(Day.TUESDAY, LocalTime.of(6, 0), LocalTime.of(18, 0)),
                    Shedule(Day.WEDNESDAY, LocalTime.of(6, 0), LocalTime.of(18, 0)),
                    Shedule(Day.THURSDAY, LocalTime.of(6, 0), LocalTime.of(18, 0)),
                    Shedule(Day.FRIDAY, LocalTime.of(6, 0), LocalTime.of(18, 0)),
                    Shedule(Day.SATURDAY, LocalTime.of(6, 0), LocalTime.of(16, 0))
                ),
                location = Location(latitude = 4.8143, longitude = -75.6946),
                adress = "Calle 15 #20-45, Centro",
                website = null,
                email = null,
                socialMedia = null,
                reviews = emptyList(),
                city = "Pereira",
                neighborhood = "Centro",
                priceRange = "$$",
                amenities = listOf("Panadería", "Bebidas", "Para llevar"),
                isVerified = false,
                ownerId = "3",
                viewCount = 0,
                favoriteCount = 0,
                createdDate = LocalDate.now(),
                placeStatus = PlaceStatus.PENDING
            ),
            Place(
                id = "p11",
                title = "Tienda de Café Expresso",
                imagenes = listOf("https://cdn.pixabay.com/photo/2022/07/14/00/36/cafe-7320242_1280.jpg"),
                description = "Café especializado en bebidas expresso",
                phones = listOf("+57 300 222 3344"),
                type = PlaceType.CAFE,
                shedule = listOf(
                    Shedule(
                        Day.MONDAY,
                        LocalTime.of(7, 0),
                        LocalTime.of(19, 0)
                    ),
                    Shedule(
                        Day.TUESDAY,
                        LocalTime.of(7, 0),
                        LocalTime.of(19, 0)
                    ),
                    Shedule(
                        Day.WEDNESDAY,
                        LocalTime.of(7, 0),
                        LocalTime.of(19, 0)
                    ),
                    Shedule(
                        Day.THURSDAY,
                        LocalTime.of(7, 0),
                        LocalTime.of(19, 0)
                    ),
                    Shedule(
                        Day.FRIDAY,
                        LocalTime.of(7, 0),
                        LocalTime.of(19, 0)
                    ),
                    Shedule(
                        Day.SATURDAY,
                        LocalTime.of(8, 0),
                        LocalTime.of(17, 0)
                    )
                ),
                location = Location(latitude = 4.8143, longitude = -75.6946),
                adress = "Carrera 8 #12-30, El Poblado",
                website = null,
                email = null,
                socialMedia = null,
                reviews = emptyList(),
                city = "Pereira",
                neighborhood = "El Poblado",
                priceRange = "$$",
                amenities = listOf("Café Especial", "Postres", "Terraza"),
                isVerified = false,
                ownerId = "4",
                viewCount = 0,
                favoriteCount = 0,
                createdDate = LocalDate.now(),
                placeStatus = PlaceStatus.PENDING,
            )
        )
    }

    fun create(place: Place) {
        _places.value = _places.value + place
    }

    fun filtrarportitulotypo(query: String): List<Place> {
        return _places.value.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.type.toString().contains(query, ignoreCase = true)
        }

    }

    fun findById(id: String): Place? {
        return _places.value.find { it.id == id }
    }

    fun findByTitle(title: String): Place? {
        return _places.value.find { it.title.equals(title, ignoreCase = true) }
    }

    fun filtrarPlaceByTile(title: String): List<Place> {
        return _places.value.filter { it.title.contains(title, ignoreCase = true) }
    }


    fun findByType(type: String): List<Place> {
        return _places.value.filter { it.type.toString().contains(type, ignoreCase = true) }
    }

    fun findPlacesByOwnerId(ownerId: String): List<Place> {
        if (ownerId.isNotEmpty()) {
            return _places.value.filter { it.ownerId == ownerId }
        } else {
            return emptyList()
        }
    }

    fun findMostPopularPlacesInCity(city: String): List<Place> {
        return _places.value.filter { it.city.equals(city, ignoreCase = true) }
            .sortedByDescending { it.favoriteCount }
            .take(10)
    }

    // Métodos para moderación
    fun getPendingPlaces(): List<Place> {
        return _places.value.filter { it.placeStatus == PlaceStatus.PENDING }
    }

    fun getApprovedPlacesToday(): List<Place> {
        // Por simplicidad, retornamos todos los aprobados
        // En una implementación real, filtrarías por fecha
        return _places.value.filter { it.placeStatus == PlaceStatus.APPROVED }
    }

    fun getReportedPlaces(): List<Place> {
        // Por simplicidad, retornamos lugares con pocas reseñas como "reportados"
        return _places.value.filter { it.reviews.size < 2 }
    }

    fun getProblematicPlaces(): List<Place> {
        // Por simplicidad, retornamos lugares sin verificar
        return _places.value.filter { !it.isVerified }
    }

    fun approvePlace(placeId: String) {
        println("DEBUG: approvePlace llamado con ID: $placeId")
        val currentPlaces = _places.value.toMutableList()
        val placeIndex = currentPlaces.indexOfFirst { it.id == placeId }
        
        if (placeIndex != -1) {
            val place = currentPlaces[placeIndex]
            // Crear una nueva instancia del lugar con el estado actualizado
            val updatedPlace = place.copy(placeStatus = PlaceStatus.APPROVED)
            currentPlaces[placeIndex] = updatedPlace
            println("DEBUG: Cambiando estado de ${place.title} a APPROVED")
            
            // Crear una nueva lista para forzar la notificación del StateFlow
            _places.value = currentPlaces.toList()
        }
        
        println("DEBUG: Lugares pendientes después de aprobar: ${_places.value.filter { it.placeStatus == PlaceStatus.PENDING }.size}")
    }

    fun rejectPlace(placeId: String) {
        println("DEBUG: rejectPlace llamado con ID: $placeId")
        val currentPlaces = _places.value.toMutableList()
        val placeIndex = currentPlaces.indexOfFirst { it.id == placeId }
        
        if (placeIndex != -1) {
            val place = currentPlaces[placeIndex]
            // Crear una nueva instancia del lugar con el estado actualizado
            val updatedPlace = place.copy(placeStatus = PlaceStatus.REJECTED)
            currentPlaces[placeIndex] = updatedPlace
            println("DEBUG: Cambiando estado de ${place.title} a REJECTED")
            
            // Crear una nueva lista para forzar la notificación del StateFlow
            _places.value = currentPlaces.toList()
        }
        
        println("DEBUG: Lugares pendientes después de rechazar: ${_places.value.filter { it.placeStatus == PlaceStatus.PENDING }.size}")
    }

    fun deletePlace(placeId: String) {
        _places.value = _places.value.filter { it.id != placeId }
    }

    fun updatePlace(updatedPlace: Place) {
        _places.value = _places.value.map { place ->
            if (place.id == updatedPlace.id) updatedPlace else place
        }
    }

    // ===== Métodos para creación/edición de lugares =====

    // Step 1: Actualizar información básica
    fun updateBasicInfo(title: String, description: String, type: PlaceType?) {
        _createPlaceState.value = _createPlaceState.value.copy(
            title = title,
            description = description,
            type = type
        )
    }

    // Step 2: Actualizar contacto y ubicación
    fun updateContactInfo(
        phone: String,
        website: String?,
        socialMedia: String?,
        address: String = "",
        city: String = "",
        neighborhood: String = ""
    ) {
        _createPlaceState.value = _createPlaceState.value.copy(
            phones = if (phone.isNotBlank()) listOf(phone) else emptyList(),
            website = website?.takeIf { it.isNotBlank() },
            socialMedia = socialMedia?.takeIf { it.isNotBlank() },
            address = address,
            city = city,
            neighborhood = neighborhood
        )
    }

    fun updateLocation(location: Location) {
        _createPlaceState.value = _createPlaceState.value.copy(location = location)
    }

    // Step 3: Actualizar horarios
    fun updateSchedule(schedule: List<Shedule>) {
        _createPlaceState.value = _createPlaceState.value.copy(schedule = schedule)
    }

    // Step 4: Actualizar imágenes
    fun updateImages(images: List<String>) {
        _createPlaceState.value = _createPlaceState.value.copy(images = images)
    }

    fun addImage(imageUrl: String) {
        _createPlaceState.value = _createPlaceState.value.copy(
            images = _createPlaceState.value.images + imageUrl
        )
    }

    fun removeImage(imageUrl: String) {
        _createPlaceState.value = _createPlaceState.value.copy(
            images = _createPlaceState.value.images.filter { it != imageUrl }
        )
    }

    // Validaciones
    fun isStep1Valid(): Boolean {
        val state = _createPlaceState.value
        return state.title.isNotBlank() &&
               state.description.isNotBlank() &&
               state.type != null
    }

    fun isStep2Valid(): Boolean {
        val state = _createPlaceState.value
        return state.phones.isNotEmpty() ||
               state.website != null ||
               state.socialMedia != null
    }

    fun isStep3Valid(): Boolean {
        return _createPlaceState.value.schedule.isNotEmpty()
    }

    fun isStep4Valid(): Boolean {
        return _createPlaceState.value.images.isNotEmpty()
    }

    // Reiniciar estado de creación
    fun resetCreateState() {
        _createPlaceState.value = CreatePlaceState()
    }

    // Cargar un lugar existente en el estado de edición
    fun loadPlaceForEdit(placeId: String) {
        val place = findById(placeId)
        place?.let {
            _createPlaceState.value = CreatePlaceState(
                title = it.title,
                description = it.description,
                type = it.type,
                phones = it.phones,
                website = it.website,
                socialMedia = it.socialMedia,
                location = it.location,
                address = it.adress,
                city = it.city,
                neighborhood = it.neighborhood,
                schedule = it.shedule,
                images = it.imagenes
            )
        }
    }
}