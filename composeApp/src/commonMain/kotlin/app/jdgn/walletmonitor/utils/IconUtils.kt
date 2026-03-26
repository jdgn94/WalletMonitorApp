package app.jdgn.walletmonitor.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.vectorResource
import walletmonitor.composeapp.generated.resources.Res
import walletmonitor.composeapp.generated.resources.*

data class CustomIcon(
    val name: String,
    val icon: ImageVector,
    val category: String
)

object IconUtils {
    @Composable
    fun getCategoryIcons(): Map<String, List<CustomIcon>> {
        val icons = listOf(
            // Transport
            CustomIcon("ambulance", vectorResource(Res.drawable.category_ambulance), "Transport"),
            CustomIcon("bus", vectorResource(Res.drawable.category_bus), "Transport"),
            CustomIcon("bycicle", vectorResource(Res.drawable.category_bycicle), "Transport"),
            CustomIcon("bycicle_2", vectorResource(Res.drawable.category_bycicle_2), "Transport"),
            CustomIcon("bike", vectorResource(Res.drawable.category_bike), "Transport"),
            CustomIcon("car", vectorResource(Res.drawable.category_car), "Transport"),
            CustomIcon("car_repair", vectorResource(Res.drawable.category_car_repair), "Transport"),
            CustomIcon("cargo_bike", vectorResource(Res.drawable.category_cargo_bike), "Transport"),
            CustomIcon("delivery", vectorResource(Res.drawable.category_delivery), "Transport"),
            CustomIcon("delivery_bike", vectorResource(Res.drawable.category_delivery_bike), "Transport"),
            CustomIcon("garbage_truck", vectorResource(Res.drawable.category_garbage_truck), "Transport"),
            CustomIcon("fire_brigade", vectorResource(Res.drawable.category_fire_brigade), "Transport"),
            CustomIcon("taxi", vectorResource(Res.drawable.category_taxi), "Transport"),
            CustomIcon("plane", vectorResource(Res.drawable.category_plane), "Transport"),
            CustomIcon("siren", vectorResource(Res.drawable.category_siren), "Transport"),
            CustomIcon("parking", vectorResource(Res.drawable.category_parking), "Transport"),
            CustomIcon("tow_truck", vectorResource(Res.drawable.category_tow_truck), "Transport"),
            CustomIcon("school_bus", vectorResource(Res.drawable.category_school_bus), "Transport"),

            // Finance
            CustomIcon("bank", vectorResource(Res.drawable.category_bank), "Finance"),
            CustomIcon("cash", vectorResource(Res.drawable.category_cash), "Finance"),
            CustomIcon("contactless", vectorResource(Res.drawable.category_contactless), "Finance"),
            CustomIcon("piggy_bank", vectorResource(Res.drawable.category_piggy_bank), "Finance"),
            CustomIcon("pie_chart", vectorResource(Res.drawable.category_pie_chart), "Finance"),
            CustomIcon("market_analysis", vectorResource(Res.drawable.category_market_analysis), "Finance"),
            CustomIcon("report", vectorResource(Res.drawable.category_report), "Finance"),

            // Shopping
            CustomIcon("app_store", vectorResource(Res.drawable.category_app_store), "Shopping"),
            CustomIcon("beauty_product", vectorResource(Res.drawable.category_beauty_product), "Shopping"),
            CustomIcon("gift", vectorResource(Res.drawable.category_gift), "Shopping"),
            CustomIcon("gift_2", vectorResource(Res.drawable.category_gift_2), "Shopping"),
            CustomIcon("food_cart", vectorResource(Res.drawable.category_food_cart), "Shopping"),
            CustomIcon("grocery_cart", vectorResource(Res.drawable.category_grocery_cart), "Shopping"),
            CustomIcon("shampoo", vectorResource(Res.drawable.category_shampoo), "Shopping"),

            // Entertainment & Tech
            CustomIcon("apple_music", vectorResource(Res.drawable.category_apple_music), "Entertainment"),
            CustomIcon("apple_tv", vectorResource(Res.drawable.category_apple_tv), "Entertainment"),
            CustomIcon("cinema", vectorResource(Res.drawable.category_cinema), "Entertainment"),
            CustomIcon("cinema_screen", vectorResource(Res.drawable.category_cinema_screen), "Entertainment"),
            CustomIcon("crunchyroll", vectorResource(Res.drawable.category_crunchyroll), "Entertainment"),
            CustomIcon("deezer", vectorResource(Res.drawable.category_deezer), "Entertainment"),
            CustomIcon("disco_ball", vectorResource(Res.drawable.category_disco_ball), "Entertainment"),
            CustomIcon("epic_games", vectorResource(Res.drawable.category_epic_games), "Entertainment"),
            CustomIcon("pc", vectorResource(Res.drawable.category_pc), "Entertainment"),
            CustomIcon("gog", vectorResource(Res.drawable.category_gog), "Entertainment"),
            CustomIcon("xbox", vectorResource(Res.drawable.category_xbox), "Entertainment"),
            CustomIcon("steam", vectorResource(Res.drawable.category_steam), "Entertainment"),
            CustomIcon("netflix", vectorResource(Res.drawable.category_netflix), "Entertainment"),
            CustomIcon("spotify", vectorResource(Res.drawable.category_spotify), "Entertainment"),
            CustomIcon("theatre", vectorResource(Res.drawable.category_theatre), "Entertainment"),
            CustomIcon("joystick", vectorResource(Res.drawable.category_joystick), "Entertainment"),
            CustomIcon("joystick_2", vectorResource(Res.drawable.category_joystick_2), "Entertainment"),
            CustomIcon("nintendo", vectorResource(Res.drawable.category_nintendo), "Entertainment"),
            CustomIcon("playstation", vectorResource(Res.drawable.category_playstation), "Entertainment"),
            CustomIcon("soundcloud", vectorResource(Res.drawable.category_soundcloud), "Entertainment"),
            CustomIcon("theme_park", vectorResource(Res.drawable.category_theme_park), "Entertainment"),
            CustomIcon("smart_tv", vectorResource(Res.drawable.category_smart_tv), "Entertainment"),
            CustomIcon("streaming_tv", vectorResource(Res.drawable.category_streaming_tv), "Entertainment"),
            CustomIcon("google_play", vectorResource(Res.drawable.category_google_play), "Entertainment"),
            CustomIcon("headphones", vectorResource(Res.drawable.category_headphones), "Entertainment"),
            CustomIcon("party_hat", vectorResource(Res.drawable.category_party_hat), "Entertainment"),
            CustomIcon("party_popper", vectorResource(Res.drawable.category_party_popper), "Entertainment"),

            // Food & Drink
            CustomIcon("beer", vectorResource(Res.drawable.category_beer), "Food & Drink"),
            CustomIcon("cake", vectorResource(Res.drawable.category_cake), "Food & Drink"),
            CustomIcon("cake_slice", vectorResource(Res.drawable.category_cake_slice), "Food & Drink"),
            CustomIcon("birthday_cake", vectorResource(Res.drawable.category_birthday_cake), "Food & Drink"),
            CustomIcon("birthday_cake_2", vectorResource(Res.drawable.category_birthday_cake_2), "Food & Drink"),
            CustomIcon("diet", vectorResource(Res.drawable.category_diet), "Food & Drink"),
            CustomIcon("fast_food", vectorResource(Res.drawable.category_fast_food), "Food & Drink"),
            CustomIcon("fast_food_2", vectorResource(Res.drawable.category_fast_food_2), "Food & Drink"),
            CustomIcon("food", vectorResource(Res.drawable.category_food), "Food & Drink"),
            CustomIcon("wine", vectorResource(Res.drawable.category_wine), "Food & Drink"),
            CustomIcon("sweets", vectorResource(Res.drawable.category_sweets), "Food & Drink"),
            CustomIcon("liquor", vectorResource(Res.drawable.category_liquor), "Food & Drink"),
            CustomIcon("restaurant", vectorResource(Res.drawable.category_restaurant), "Food & Drink"),
            CustomIcon("healthy_drink", vectorResource(Res.drawable.category_healthy_drink), "Food & Drink"),
            CustomIcon("water_bottle", vectorResource(Res.drawable.category_water_bottle), "Food & Drink"),

            // Services & Health
            CustomIcon("doctor", vectorResource(Res.drawable.category_doctor), "Services"),
            CustomIcon("doctor_2", vectorResource(Res.drawable.category_doctor_2), "Services"),
            CustomIcon("barber", vectorResource(Res.drawable.category_barber), "Services"),
            CustomIcon("barber_pole", vectorResource(Res.drawable.category_barber_pole), "Services"),
            CustomIcon("customer_service", vectorResource(Res.drawable.category_customer_service), "Services"),
            CustomIcon("customer_service_2", vectorResource(Res.drawable.category_customer_service_2), "Services"),
            CustomIcon("customer_support", vectorResource(Res.drawable.category_customer_support), "Services"),
            CustomIcon("fire_extinguisher", vectorResource(Res.drawable.category_fire_extinguisher), "Services"),
            CustomIcon("hair", vectorResource(Res.drawable.category_hair), "Services"),
            CustomIcon("hairdresser", vectorResource(Res.drawable.category_hairdresser), "Services"),
            CustomIcon("mechanic", vectorResource(Res.drawable.category_mechanic), "Services"),
            CustomIcon("tool_box", vectorResource(Res.drawable.category_tool_box), "Services"),
            CustomIcon("stethoscope", vectorResource(Res.drawable.category_stethoscope), "Services"),
            CustomIcon("veterinarian", vectorResource(Res.drawable.category_veterinarian), "Services"),
            CustomIcon("judge", vectorResource(Res.drawable.category_judge), "Services"),
            CustomIcon("judge_2", vectorResource(Res.drawable.category_judge_2), "Services"),
            CustomIcon("police", vectorResource(Res.drawable.category_police), "Services"),
            CustomIcon("school", vectorResource(Res.drawable.category_school), "Services"),
            CustomIcon("pen", vectorResource(Res.drawable.category_pen), "Services"),
            CustomIcon("pencil", vectorResource(Res.drawable.category_pencil), "Services"),
            CustomIcon("graduation", vectorResource(Res.drawable.category_graduation), "Services"),

            // Others
            CustomIcon("beach", vectorResource(Res.drawable.category_beach), "Others"),
            CustomIcon("categories", vectorResource(Res.drawable.category_categories), "Others"),
            CustomIcon("christmas_tree", vectorResource(Res.drawable.category_christmas_tree), "Others"),
            CustomIcon("christmas_wreath", vectorResource(Res.drawable.category_christmas_wreath), "Others"),
            CustomIcon("electrical_energy", vectorResource(Res.drawable.category_electrical_energy), "Others"),
            CustomIcon("ethernet", vectorResource(Res.drawable.category_ethernet), "Others"),
            CustomIcon("ethernet_2", vectorResource(Res.drawable.category_ethernet_2), "Others"),
            CustomIcon("flash", vectorResource(Res.drawable.category_flash), "Others"),
            CustomIcon("genre", vectorResource(Res.drawable.category_genre), "Others"),
            CustomIcon("tent", vectorResource(Res.drawable.category_tent), "Others"),
            CustomIcon("wifi", vectorResource(Res.drawable.category_wifi), "Others"),
            CustomIcon("house", vectorResource(Res.drawable.category_house), "Others"),
            CustomIcon("global", vectorResource(Res.drawable.category_global), "Others"),
            CustomIcon("google", vectorResource(Res.drawable.category_google), "Others"),
            CustomIcon("pet_toy", vectorResource(Res.drawable.category_pet_toy), "Others"),
            CustomIcon("pet_food", vectorResource(Res.drawable.category_pet_food), "Others"),
            CustomIcon("pet_beauty", vectorResource(Res.drawable.category_pet_beauty), "Others"),
            CustomIcon("sim_card", vectorResource(Res.drawable.category_sim_card), "Others"),
            CustomIcon("microsoft", vectorResource(Res.drawable.category_microsoft), "Others"),
            CustomIcon("not_found", vectorResource(Res.drawable.category_not_found), "Others"),
            CustomIcon("water_tap", vectorResource(Res.drawable.category_water_tap), "Others"),
            CustomIcon("water", vectorResource(Res.drawable.category_water), "Others"),
            CustomIcon("water_rinse", vectorResource(Res.drawable.category_water_rinse), "Others"),
            CustomIcon("light_bulb", vectorResource(Res.drawable.category_light_bulb), "Others")
        )
        return icons.groupBy { it.category }
    }

    @Composable
    fun getIconByName(name: String): ImageVector? {
        return when (name) {
            "ambulance" -> vectorResource(Res.drawable.category_ambulance)
            "app_store" -> vectorResource(Res.drawable.category_app_store)
            "apple_music" -> vectorResource(Res.drawable.category_apple_music)
            "apple_tv" -> vectorResource(Res.drawable.category_apple_tv)
            "bank" -> vectorResource(Res.drawable.category_bank)
            "barber" -> vectorResource(Res.drawable.category_barber)
            "barber_pole" -> vectorResource(Res.drawable.category_barber_pole)
            "beach" -> vectorResource(Res.drawable.category_beach)
            "beauty_product" -> vectorResource(Res.drawable.category_beauty_product)
            "beer" -> vectorResource(Res.drawable.category_beer)
            "bike" -> vectorResource(Res.drawable.category_bike)
            "birthday_cake" -> vectorResource(Res.drawable.category_birthday_cake)
            "birthday_cake_2" -> vectorResource(Res.drawable.category_birthday_cake_2)
            "bus" -> vectorResource(Res.drawable.category_bus)
            "bycicle" -> vectorResource(Res.drawable.category_bycicle)
            "bycicle_2" -> vectorResource(Res.drawable.category_bycicle_2)
            "cake" -> vectorResource(Res.drawable.category_cake)
            "cake_slice" -> vectorResource(Res.drawable.category_cake_slice)
            "car" -> vectorResource(Res.drawable.category_car)
            "car_repair" -> vectorResource(Res.drawable.category_car_repair)
            "cargo_bike" -> vectorResource(Res.drawable.category_cargo_bike)
            "cash" -> vectorResource(Res.drawable.category_cash)
            "categories" -> vectorResource(Res.drawable.category_categories)
            "christmas_tree" -> vectorResource(Res.drawable.category_christmas_tree)
            "christmas_wreath" -> vectorResource(Res.drawable.category_christmas_wreath)
            "cinema" -> vectorResource(Res.drawable.category_cinema)
            "cinema_screen" -> vectorResource(Res.drawable.category_cinema_screen)
            "contactless" -> vectorResource(Res.drawable.category_contactless)
            "crunchyroll" -> vectorResource(Res.drawable.category_crunchyroll)
            "customer_service" -> vectorResource(Res.drawable.category_customer_service)
            "customer_service_2" -> vectorResource(Res.drawable.category_customer_service_2)
            "customer_support" -> vectorResource(Res.drawable.category_customer_support)
            "deezer" -> vectorResource(Res.drawable.category_deezer)
            "delivery" -> vectorResource(Res.drawable.category_delivery)
            "delivery_bike" -> vectorResource(Res.drawable.category_delivery_bike)
            "diet" -> vectorResource(Res.drawable.category_diet)
            "disco_ball" -> vectorResource(Res.drawable.category_disco_ball)
            "doctor" -> vectorResource(Res.drawable.category_doctor)
            "doctor_2" -> vectorResource(Res.drawable.category_doctor_2)
            "electrical_energy" -> vectorResource(Res.drawable.category_electrical_energy)
            "epic_games" -> vectorResource(Res.drawable.category_epic_games)
            "ethernet" -> vectorResource(Res.drawable.category_ethernet)
            "ethernet_2" -> vectorResource(Res.drawable.category_ethernet_2)
            "fast_food" -> vectorResource(Res.drawable.category_fast_food)
            "fast_food_2" -> vectorResource(Res.drawable.category_fast_food_2)
            "fire_brigade" -> vectorResource(Res.drawable.category_fire_brigade)
            "fire_extinguisher" -> vectorResource(Res.drawable.category_fire_extinguisher)
            "flash" -> vectorResource(Res.drawable.category_flash)
            "food" -> vectorResource(Res.drawable.category_food)
            "food_cart" -> vectorResource(Res.drawable.category_food_cart)
            "garbage_truck" -> vectorResource(Res.drawable.category_garbage_truck)
            "genre" -> vectorResource(Res.drawable.category_genre)
            "gift" -> vectorResource(Res.drawable.category_gift)
            "gift_2" -> vectorResource(Res.drawable.category_gift_2)
            "pc" -> vectorResource(Res.drawable.category_pc)
            "gog" -> vectorResource(Res.drawable.category_gog)
            "pen" -> vectorResource(Res.drawable.category_pen)
            "hair" -> vectorResource(Res.drawable.category_hair)
            "taxi" -> vectorResource(Res.drawable.category_taxi)
            "tent" -> vectorResource(Res.drawable.category_tent)
            "wifi" -> vectorResource(Res.drawable.category_wifi)
            "wine" -> vectorResource(Res.drawable.category_wine)
            "xbox" -> vectorResource(Res.drawable.category_xbox)
            "house" -> vectorResource(Res.drawable.category_house)
            "judge" -> vectorResource(Res.drawable.category_judge)
            "plane" -> vectorResource(Res.drawable.category_plane)
            "siren" -> vectorResource(Res.drawable.category_siren)
            "steam" -> vectorResource(Res.drawable.category_steam)
            "water" -> vectorResource(Res.drawable.category_water)
            "global" -> vectorResource(Res.drawable.category_global)
            "google" -> vectorResource(Res.drawable.category_google)
            "liquor" -> vectorResource(Res.drawable.category_liquor)
            "pencil" -> vectorResource(Res.drawable.category_pencil)
            "police" -> vectorResource(Res.drawable.category_police)
            "report" -> vectorResource(Res.drawable.category_report)
            "school" -> vectorResource(Res.drawable.category_school)
            "sweets" -> vectorResource(Res.drawable.category_sweets)
            "judge_2" -> vectorResource(Res.drawable.category_judge_2)
            "netflix" -> vectorResource(Res.drawable.category_netflix)
            "parking" -> vectorResource(Res.drawable.category_parking)
            "pet_toy" -> vectorResource(Res.drawable.category_pet_toy)
            "shampoo" -> vectorResource(Res.drawable.category_shampoo)
            "spotify" -> vectorResource(Res.drawable.category_spotify)
            "theatre" -> vectorResource(Res.drawable.category_theatre)
            "joystick" -> vectorResource(Res.drawable.category_joystick)
            "mechanic" -> vectorResource(Res.drawable.category_mechanic)
            "nintendo" -> vectorResource(Res.drawable.category_nintendo)
            "pet_food" -> vectorResource(Res.drawable.category_pet_food)
            "sim_card" -> vectorResource(Res.drawable.category_sim_card)
            "smart_tv" -> vectorResource(Res.drawable.category_smart_tv)
            "tool_box" -> vectorResource(Res.drawable.category_tool_box)
            "microsoft" -> vectorResource(Res.drawable.category_microsoft)
            "not_found" -> vectorResource(Res.drawable.category_not_found)
            "party_hat" -> vectorResource(Res.drawable.category_party_hat)
            "pie_chart" -> vectorResource(Res.drawable.category_pie_chart)
            "tow_truck" -> vectorResource(Res.drawable.category_tow_truck)
            "water_tap" -> vectorResource(Res.drawable.category_water_tap)
            "graduation" -> vectorResource(Res.drawable.category_graduation)
            "headphones" -> vectorResource(Res.drawable.category_headphones)
            "joystick_2" -> vectorResource(Res.drawable.category_joystick_2)
            "light_bulb" -> vectorResource(Res.drawable.category_light_bulb)
            "pet_beauty" -> vectorResource(Res.drawable.category_pet_beauty)
            "piggy_bank" -> vectorResource(Res.drawable.category_piggy_bank)
            "restaurant" -> vectorResource(Res.drawable.category_restaurant)
            "school_bus" -> vectorResource(Res.drawable.category_school_bus)
            "soundcloud" -> vectorResource(Res.drawable.category_soundcloud)
            "theme_park" -> vectorResource(Res.drawable.category_theme_park)
            "google_play" -> vectorResource(Res.drawable.category_google_play)
            "hairdresser" -> vectorResource(Res.drawable.category_hairdresser)
            "playstation" -> vectorResource(Res.drawable.category_playstation)
            "stethoscope" -> vectorResource(Res.drawable.category_stethoscope)
            "water_rinse" -> vectorResource(Res.drawable.category_water_rinse)
            "grocery_cart" -> vectorResource(Res.drawable.category_grocery_cart)
            "party_popper" -> vectorResource(Res.drawable.category_party_popper)
            "streaming_tv" -> vectorResource(Res.drawable.category_streaming_tv)
            "veterinarian" -> vectorResource(Res.drawable.category_veterinarian)
            "water_bottle" -> vectorResource(Res.drawable.category_water_bottle)
            "healthy_drink" -> vectorResource(Res.drawable.category_healthy_drink)
            "market_analysis" -> vectorResource(Res.drawable.category_market_analysis)
            else -> null
        }
    }
}
