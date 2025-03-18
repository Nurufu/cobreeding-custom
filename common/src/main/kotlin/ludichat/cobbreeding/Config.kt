package ludichat.cobbreeding

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class Config(
    @EncodeDefault val eggCheckTicks: Int = Defaults.EGG_CHECK_TICKS,
    @EncodeDefault val eggCheckChance: Float = Defaults.EGG_CHECK_CHANCE,
    @EncodeDefault val eggHatchMultiplier: Float = Defaults.EGG_HATCH_MULTIPLIER,
    @EncodeDefault val shinyMethod: String = Defaults.SHINY_METHOD,
    @EncodeDefault val shinyMultiplier: Float = Defaults.SHINY_MULTIPLIER,
    @EncodeDefault val hiddenAbilitiesEnabled: Boolean = Defaults.HIDDEN_ABILITIES_ENABLED,
    @EncodeDefault val forcedAbilitiesEnabled: Boolean = Defaults.FORCED_ABILITIES_ENABLED,
    @EncodeDefault val dittoAndDittoRandomEgg: Boolean = Defaults.DITTO_AND_DITTO_RANDOM_EGG,
    @EncodeDefault val dittoAndDittoAllowLegendary: Boolean = Defaults.DITTO_AND_DITTO_ALLOW_LEGENDARY,
) {
    object Defaults {
        const val EGG_CHECK_TICKS = 12000
        const val EGG_CHECK_CHANCE = 0.5F
        const val EGG_HATCH_MULTIPLIER = 1.0F
        const val SHINY_METHOD = "masuda"
        const val SHINY_MULTIPLIER = 4.0F
        const val HIDDEN_ABILITIES_ENABLED = true
        const val FORCED_ABILITIES_ENABLED = false
        const val DITTO_AND_DITTO_RANDOM_EGG = false
        const val DITTO_AND_DITTO_ALLOW_LEGENDARY = false
    }

    init {
        require(eggCheckTicks > 0) { "Amount of ticks between each egg check has to be greater than 0." }
        require(eggCheckChance in 0.0..1.0) { "Egg chance has to be between 0 and 1." }
        require(eggHatchMultiplier > 0) { "Egg hatching speed multiplier has to be greater than 0." }
    }
}