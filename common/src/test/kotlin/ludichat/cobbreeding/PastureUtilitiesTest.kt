package ludichat.cobbreeding

//import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
//import com.cobblemon.mod.common.api.pokemon.egg.EggGroup
//import com.cobblemon.mod.common.api.pokemon.evolution.PreEvolution
//import com.cobblemon.mod.common.block.entity.PokemonPastureBlockEntity
//import com.cobblemon.mod.common.pokemon.Gender
//import com.cobblemon.mod.common.pokemon.Pokemon
//import com.cobblemon.mod.common.pokemon.Species
//import io.mockk.*
//import io.mockk.junit5.MockKExtension
//import ludichat.cobbreeding.PastureUtilities.getPokemon
//import ludichat.cobbreeding.PastureUtilities.getPossibleEggs
//import org.junit.jupiter.api.*
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.extension.ExtendWith
//
//@ExtendWith(MockKExtension::class)
//@MockKExtension.ConfirmVerification
//internal class PastureUtilitiesTest {
//    companion object {
//        init {
//            // Hack to mock PokemonSpecies::getByName
//            try {
//                net.minecraft.Bootstrap.initialize()
//            } catch (_: Throwable) {
//            }
//        }
//
//        @JvmStatic
//        @BeforeAll
//        fun beforeTests() {
//            // Hack to mock PokemonSpecies::getByName
//            mockkConstructor(PokemonSpecies::class)
//            every { PokemonSpecies } just awaits
//            mockkObject(PokemonSpecies)
//        }
//
//        @JvmStatic
//        @AfterAll
//        fun afterTests() {
//            // Clean up hack to mock PokemonSpecies::getByName
//            unmockkObject(PokemonSpecies)
//            unmockkConstructor(PokemonSpecies::class)
//        }
//    }
//
//    @Nested
//    @ExtendWith(MockKExtension::class)
//    @MockKExtension.ConfirmVerification
//    inner class DittoTests {
//        @Test
//        fun `Ditto can breed with Manaphy to get Phione eggs`() {
//            val phioneSpecies = mockk<Species>()
//
//            val manaphySpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.WATER_1, EggGroup.FAIRY)
//            }
//            val manaphy = mockk<Pokemon> {
//                every { species } returns manaphySpecies
//            }
//
//            val dittoSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.DITTO)
//            }
//            val ditto = mockk<Pokemon> {
//                every { species } returns dittoSpecies
//            }
//
//            every { PokemonSpecies.getByName("manaphy") } returns manaphySpecies
//            every { PokemonSpecies.getByName("phione") } returns phioneSpecies
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(manaphy, ditto)),
//                getPossibleEggs(listOf(ditto, manaphy))
//            ).map { { assertEquals(hashMapOf(Pair(phioneSpecies, listOf(Pair(manaphy, ditto)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                manaphySpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                manaphy.equals(manaphy)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                ditto.equals(ditto)
//
//                manaphy.species
//                manaphySpecies.eggGroups
//
//                ditto.species
//                dittoSpecies.eggGroups
//            }
//
//            checkUnnecessaryStub(
//                phioneSpecies,
//                manaphySpecies,
//                manaphy,
//
//                dittoSpecies,
//                ditto,
//            )
//        }
//
//        @Test
//        fun `Ditto can breed with Phione`() {
//            val manaphySpecies = mockk<Species>()
//
//            val phioneSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.WATER_1, EggGroup.FAIRY)
//                every { preEvolution } returns null
//            }
//            val phione = mockk<Pokemon> {
//                every { species } returns phioneSpecies
//            }
//
//            val dittoSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.DITTO)
//            }
//            val ditto = mockk<Pokemon> {
//                every { species } returns dittoSpecies
//            }
//
//            every { PokemonSpecies.getByName("manaphy") } returns manaphySpecies
//            every { PokemonSpecies.getByName("phione") } returns phioneSpecies
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(phione, ditto)),
//                getPossibleEggs(listOf(ditto, phione))
//            ).map { { assertEquals(hashMapOf(Pair(phioneSpecies, listOf(Pair(phione, ditto)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                phioneSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                phione.equals(phione)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                ditto.equals(ditto)
//
//                phione.species
//                phioneSpecies.eggGroups
//                phioneSpecies.preEvolution
//
//                ditto.species
//                dittoSpecies.eggGroups
//            }
//
//            checkUnnecessaryStub(
//                manaphySpecies,
//                phioneSpecies,
//                phione,
//
//                dittoSpecies,
//                ditto,
//            )
//        }
//
//        @Test
//        fun `Ditto can't breed with Ditto`() {
//            val manaphySpecies = mockk<Species>()
//            every { PokemonSpecies.getByName("manaphy") } returns manaphySpecies
//
//            val dittoSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.DITTO)
//            }
//            val ditto1 = mockk<Pokemon> {
//                every { species } returns dittoSpecies
//            }
//            val ditto2 = mockk<Pokemon> {
//                every { species } returns dittoSpecies
//            }
//
//            assertTrue(
//                getPossibleEggs(
//                    listOf(
//                        ditto1,
//                        ditto2
//                    )
//                ).isEmpty()
//            )
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                dittoSpecies.equals(manaphySpecies)
//
//                ditto1.species
//                ditto2.species
//                dittoSpecies.eggGroups
//            }
//
//            checkUnnecessaryStub(
//                manaphySpecies,
//                dittoSpecies,
//                ditto1,
//                ditto2,
//            )
//        }
//
//        @Test
//        fun `Ditto can't breed with unbreedable pokemon`() {
//            val undiscoveredEggGroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.UNDISCOVERED)
//            }
//            val pokemon = mockk<Pokemon> {
//                every { species } returns undiscoveredEggGroupSpecies
//            }
//
//            val dittoSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.DITTO)
//            }
//            val ditto = mockk<Pokemon> {
//                every { species } returns dittoSpecies
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemon, ditto)),
//                getPossibleEggs(listOf(ditto, pokemon))
//            ).map { { assertTrue(it.isEmpty()) } })
//
//            verify {
//                pokemon.species
//                undiscoveredEggGroupSpecies.eggGroups
//
//                ditto.species
//                dittoSpecies.eggGroups
//            }
//
//            checkUnnecessaryStub(
//                undiscoveredEggGroupSpecies,
//                pokemon,
//
//                dittoSpecies,
//                ditto,
//            )
//        }
//
//        @Test
//        fun `Ditto can breed with any other egg group`() {
//            val manaphySpecies = mockk<Species>()
//            every { PokemonSpecies.getByName("manaphy") } returns manaphySpecies
//
//            val monsterGroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER)
//                every { preEvolution } returns null
//            }
//            val monsterGroupPokemon = mockk<Pokemon> {
//                every { species } returns monsterGroupSpecies
//            }
//
//            val water1GroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.WATER_1)
//                every { preEvolution } returns null
//            }
//            val water1GroupPokemon = mockk<Pokemon> {
//                every { species } returns water1GroupSpecies
//            }
//
//            val water2GroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.WATER_2)
//                every { preEvolution } returns null
//            }
//            val water2GroupPokemon = mockk<Pokemon> {
//                every { species } returns water2GroupSpecies
//            }
//
//            val water3GroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.WATER_3)
//                every { preEvolution } returns null
//            }
//            val water3GroupPokemon = mockk<Pokemon> {
//                every { species } returns water3GroupSpecies
//            }
//
//            val bugGroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.BUG)
//                every { preEvolution } returns null
//            }
//            val bugGroupPokemon = mockk<Pokemon> {
//                every { species } returns bugGroupSpecies
//            }
//
//            val flyingGroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.FLYING)
//                every { preEvolution } returns null
//            }
//            val flyingGroupPokemon = mockk<Pokemon> {
//                every { species } returns flyingGroupSpecies
//            }
//
//            val fieldGroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.FIELD)
//                every { preEvolution } returns null
//            }
//            val fieldGroupPokemon = mockk<Pokemon> {
//                every { species } returns fieldGroupSpecies
//            }
//
//            val fairyGroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.FAIRY)
//                every { preEvolution } returns null
//            }
//            val fairyGroupPokemon = mockk<Pokemon> {
//                every { species } returns fairyGroupSpecies
//            }
//
//            val grassGroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.GRASS)
//                every { preEvolution } returns null
//            }
//            val grassGroupPokemon = mockk<Pokemon> {
//                every { species } returns grassGroupSpecies
//            }
//
//            val humanGroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.HUMAN_LIKE)
//                every { preEvolution } returns null
//            }
//            val humanGroupPokemon = mockk<Pokemon> {
//                every { species } returns humanGroupSpecies
//            }
//
//            val mineralGroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MINERAL)
//                every { preEvolution } returns null
//            }
//            val mineralGroupPokemon = mockk<Pokemon> {
//                every { species } returns mineralGroupSpecies
//            }
//
//            val amorphousGroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.AMORPHOUS)
//                every { preEvolution } returns null
//            }
//            val amorphousGroupPokemon = mockk<Pokemon> {
//                every { species } returns amorphousGroupSpecies
//            }
//
//            val dragonGroupSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.DRAGON)
//                every { preEvolution } returns null
//            }
//            val dragonGroupPokemon = mockk<Pokemon> {
//                every { species } returns dragonGroupSpecies
//            }
//
//            val dittoSpecies = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.DITTO)
//            }
//            val ditto = mockk<Pokemon> {
//                every { species } returns dittoSpecies
//            }
//
//            assertAll(
//                listOf(
//                    listOf(
//                        getPossibleEggs(listOf(monsterGroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, monsterGroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(monsterGroupSpecies, listOf(Pair(monsterGroupPokemon, ditto)))).entries, it) } },
//                    listOf(
//                        getPossibleEggs(listOf(water1GroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, water1GroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(water1GroupSpecies, listOf(Pair(water1GroupPokemon, ditto)))).entries, it) } },
//                    listOf(
//                        getPossibleEggs(listOf(water2GroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, water2GroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(water2GroupSpecies, listOf(Pair(water2GroupPokemon, ditto)))).entries, it) } },
//                    listOf(
//                        getPossibleEggs(listOf(water3GroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, water3GroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(water3GroupSpecies, listOf(Pair(water3GroupPokemon, ditto)))).entries, it) } },
//                    listOf(
//                        getPossibleEggs(listOf(bugGroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, bugGroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(bugGroupSpecies, listOf(Pair(bugGroupPokemon, ditto)))).entries, it) } },
//                    listOf(
//                        getPossibleEggs(listOf(flyingGroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, flyingGroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(flyingGroupSpecies, listOf(Pair(flyingGroupPokemon, ditto)))).entries, it) } },
//                    listOf(
//                        getPossibleEggs(listOf(fieldGroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, fieldGroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(fieldGroupSpecies, listOf(Pair(fieldGroupPokemon, ditto)))).entries, it) } },
//                    listOf(
//                        getPossibleEggs(listOf(fairyGroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, fairyGroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(fairyGroupSpecies, listOf(Pair(fairyGroupPokemon, ditto)))).entries, it) } },
//                    listOf(
//                        getPossibleEggs(listOf(grassGroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, grassGroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(grassGroupSpecies, listOf(Pair(grassGroupPokemon, ditto)))).entries, it) } },
//                    listOf(
//                        getPossibleEggs(listOf(humanGroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, humanGroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(humanGroupSpecies, listOf(Pair(humanGroupPokemon, ditto)))).entries, it) } },
//                    listOf(
//                        getPossibleEggs(listOf(mineralGroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, mineralGroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(mineralGroupSpecies, listOf(Pair(mineralGroupPokemon, ditto)))).entries, it) } },
//                    listOf(
//                        getPossibleEggs(listOf(amorphousGroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, amorphousGroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(amorphousGroupSpecies, listOf(Pair(amorphousGroupPokemon, ditto)))).entries, it) } },
//                    listOf(
//                        getPossibleEggs(listOf(dragonGroupPokemon, ditto)),
//                        getPossibleEggs(listOf(ditto, dragonGroupPokemon))
//                    ).map { { assertEquals(hashMapOf(Pair(dragonGroupSpecies, listOf(Pair(dragonGroupPokemon, ditto)))).entries, it) } },
//                ).flatten()
//            )
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                monsterGroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                monsterGroupPokemon.equals(monsterGroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                water1GroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                water1GroupPokemon.equals(water1GroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                water2GroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                water2GroupPokemon.equals(water2GroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                water3GroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                water3GroupPokemon.equals(water3GroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                bugGroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                bugGroupPokemon.equals(bugGroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                flyingGroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                flyingGroupPokemon.equals(flyingGroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                fieldGroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                fieldGroupPokemon.equals(fieldGroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                fairyGroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                fairyGroupPokemon.equals(fairyGroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                grassGroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                grassGroupPokemon.equals(grassGroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                humanGroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                humanGroupPokemon.equals(humanGroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                mineralGroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                mineralGroupPokemon.equals(mineralGroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                amorphousGroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                amorphousGroupPokemon.equals(amorphousGroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                dragonGroupSpecies.equals(manaphySpecies)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                dragonGroupPokemon.equals(dragonGroupPokemon)
//
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                ditto.equals(ditto)
//
//                monsterGroupPokemon.species
//                monsterGroupSpecies.eggGroups
//                monsterGroupSpecies.preEvolution
//
//                water1GroupPokemon.species
//                water1GroupSpecies.eggGroups
//                water1GroupSpecies.preEvolution
//
//                water2GroupPokemon.species
//                water2GroupSpecies.eggGroups
//                water2GroupSpecies.preEvolution
//
//                water3GroupPokemon.species
//                water3GroupSpecies.eggGroups
//                water3GroupSpecies.preEvolution
//
//                bugGroupPokemon.species
//                bugGroupSpecies.eggGroups
//                bugGroupSpecies.preEvolution
//
//                flyingGroupPokemon.species
//                flyingGroupSpecies.eggGroups
//                flyingGroupSpecies.preEvolution
//
//                fieldGroupPokemon.species
//                fieldGroupSpecies.eggGroups
//                fieldGroupSpecies.preEvolution
//
//                fairyGroupPokemon.species
//                fairyGroupSpecies.eggGroups
//                fairyGroupSpecies.preEvolution
//
//                grassGroupPokemon.species
//                grassGroupSpecies.eggGroups
//                grassGroupSpecies.preEvolution
//
//                humanGroupPokemon.species
//                humanGroupSpecies.eggGroups
//                humanGroupSpecies.preEvolution
//
//                mineralGroupPokemon.species
//                mineralGroupSpecies.eggGroups
//                mineralGroupSpecies.preEvolution
//
//                amorphousGroupPokemon.species
//                amorphousGroupSpecies.eggGroups
//                amorphousGroupSpecies.preEvolution
//
//                dragonGroupPokemon.species
//                dragonGroupSpecies.eggGroups
//                dragonGroupSpecies.preEvolution
//
//                ditto.species
//                dittoSpecies.eggGroups
//            }
//
//            checkUnnecessaryStub(
//                manaphySpecies,
//
//                monsterGroupSpecies,
//                monsterGroupPokemon,
//
//                water1GroupSpecies,
//                water1GroupPokemon,
//
//                water2GroupSpecies,
//                water2GroupPokemon,
//
//                water3GroupSpecies,
//                water3GroupPokemon,
//
//                bugGroupSpecies,
//                bugGroupPokemon,
//
//                flyingGroupSpecies,
//                flyingGroupPokemon,
//
//                fieldGroupSpecies,
//                fieldGroupPokemon,
//
//                fairyGroupSpecies,
//                fairyGroupPokemon,
//
//                grassGroupSpecies,
//                grassGroupPokemon,
//
//                humanGroupSpecies,
//                humanGroupPokemon,
//
//                mineralGroupSpecies,
//                mineralGroupPokemon,
//
//                amorphousGroupSpecies,
//                amorphousGroupPokemon,
//
//                dragonGroupSpecies,
//                dragonGroupPokemon,
//
//                dittoSpecies,
//                ditto,
//            )
//        }
//    }
//
//    @Nested
//    @ExtendWith(MockKExtension::class)
//    @MockKExtension.ConfirmVerification
//    inner class SimpleEggGroupTests {
//        @Test
//        fun `Male and female monster egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female water1 egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.WATER_1)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.WATER_1)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female water2 egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.WATER_2)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.WATER_2)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female water3 egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.WATER_3)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.WATER_3)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female bug egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.BUG)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.BUG)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female flying egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.FLYING)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.FLYING)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female field egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.FIELD)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.FIELD)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female fairy egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.FAIRY)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.FAIRY)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female grass egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.GRASS)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.GRASS)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female human-like egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.HUMAN_LIKE)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.HUMAN_LIKE)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female mineral egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MINERAL)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MINERAL)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female amorphous egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.AMORPHOUS)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.AMORPHOUS)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female dragon egg group pokemon can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.DRAGON)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.DRAGON)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Two male pokemon cannot breed together`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER)
//            }
//            val pokemonM1 = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER)
//            }
//            val pokemonM2 = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.MALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM1, pokemonM2)),
//                getPossibleEggs(listOf(pokemonM2, pokemonM1))
//            ).map { { assertTrue(it.isEmpty()) } })
//
//            verify {
//                pokemonM1.gender
//                pokemonM1.species
//                species1.eggGroups
//
//                pokemonM2.gender
//                pokemonM2.species
//                species2.eggGroups
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM1,
//
//                species2,
//                pokemonM2,
//            )
//        }
//
//        @Test
//        fun `Two female pokemon cannot breed together`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER)
//            }
//            val pokemonF1 = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.FEMALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER)
//            }
//            val pokemonF2 = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonF1, pokemonF2)),
//                getPossibleEggs(listOf(pokemonF2, pokemonF1))
//            ).map { { assertTrue(it.isEmpty()) } })
//
//            verify {
//                pokemonF1.gender
//                pokemonF1.species
//                species1.eggGroups
//
//                pokemonF2.gender
//                pokemonF2.species
//                species2.eggGroups
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonF1,
//
//                species2,
//                pokemonF2,
//            )
//        }
//
//        @Test
//        fun `Undiscovered egg group pokemon cannot breed with others`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER)
//            }
//            val pokemon1 = mockk<Pokemon> {
//                every { species } returns species1
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.UNDISCOVERED)
//            }
//            val pokemon2 = mockk<Pokemon> {
//                every { species } returns species2
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemon1, pokemon2)),
//                getPossibleEggs(listOf(pokemon2, pokemon1))
//            ).map { { assertTrue(it.isEmpty()) } })
//
//            verify {
//                pokemon1.species
//                species1.eggGroups
//
//                pokemon2.species
//                species2.eggGroups
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemon1,
//
//                species2,
//                pokemon2,
//            )
//        }
//
//        @Test
//        fun `Undiscovered egg group pokemon cannot breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.UNDISCOVERED)
//            }
//            val pokemon1 = mockk<Pokemon> {
//                every { species } returns species1
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.UNDISCOVERED)
//            }
//            val pokemon2 = mockk<Pokemon> {
//                every { species } returns species2
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemon1, pokemon2)),
//                getPossibleEggs(listOf(pokemon2, pokemon1))
//            ).map { { assertTrue(it.isEmpty()) } })
//
//            verify {
//                pokemon1.species
//                species1.eggGroups
//
//                pokemon2.species
//                species2.eggGroups
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemon1,
//
//                species2,
//                pokemon2,
//            )
//        }
//
//        @Test
//        fun `Evolved species lay eggs of pre-evolved species`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { preEvolution } returns null
//            }
//            val preEvolve1 = mockk<PreEvolution> {
//                every { species } returns species2
//            }
//            val speciesEvolved1 = mockk<Species> {
//                every { preEvolution } returns preEvolve1
//            }
//            val preEvolve2 = mockk<PreEvolution> {
//                every { species } returns speciesEvolved1
//            }
//            val speciesEvolved2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER)
//                every { preEvolution } returns preEvolve2
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns speciesEvolved2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.preEvolution
//                preEvolve1.species
//                speciesEvolved1.preEvolution
//                preEvolve2.species
//                speciesEvolved2.eggGroups
//                speciesEvolved2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                preEvolve1,
//                speciesEvolved1,
//                preEvolve2,
//                speciesEvolved2,
//                pokemonF,
//            )
//        }
//    }
//
//    @Nested
//    @ExtendWith(MockKExtension::class)
//    @MockKExtension.ConfirmVerification
//    inner class MixedEggGroupsTest {
//        @Test
//        fun `Male and female pokemon with an egg group in common can breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER, EggGroup.WATER_1)
//            }
//            val pokemonM = mockk<Pokemon> {
//                every { species } returns species1
//                every { gender } returns Gender.MALE
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER, EggGroup.WATER_2)
//                every { preEvolution } returns null
//            }
//            val pokemonF = mockk<Pokemon> {
//                every { species } returns species2
//                every { gender } returns Gender.FEMALE
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemonM, pokemonF)),
//                getPossibleEggs(listOf(pokemonF, pokemonM))
//            ).map { { assertEquals(hashMapOf(Pair(species2, listOf(Pair(pokemonM, pokemonF)))).entries, it) } })
//
//            verify {
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonM.equals(pokemonM)
//                @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//                pokemonF.equals(pokemonF)
//
//                pokemonM.gender
//                pokemonM.species
//                species1.eggGroups
//
//                pokemonF.gender
//                pokemonF.species
//                species2.eggGroups
//                species2.preEvolution
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemonM,
//
//                species2,
//                pokemonF,
//            )
//        }
//
//        @Test
//        fun `Male and female pokemon with no egg group in common cannot breed with each other`() {
//            val species1 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.MONSTER, EggGroup.WATER_1)
//            }
//            val pokemon1 = mockk<Pokemon> {
//                every { species } returns species1
//            }
//
//            val species2 = mockk<Species> {
//                every { eggGroups } returns hashSetOf(EggGroup.WATER_2, EggGroup.WATER_3)
//            }
//            val pokemon2 = mockk<Pokemon> {
//                every { species } returns species2
//            }
//
//            assertAll(listOf(
//                getPossibleEggs(listOf(pokemon1, pokemon2)),
//                getPossibleEggs(listOf(pokemon2, pokemon1))
//            ).map { { assertTrue(it.isEmpty()) } })
//
//            verify {
//                pokemon1.species
//                species1.eggGroups
//
//                pokemon2.species
//                species2.eggGroups
//            }
//
//            checkUnnecessaryStub(
//                species1,
//                pokemon1,
//
//                species2,
//                pokemon2,
//            )
//        }
//    }
//
//    @Test
//    fun `getPokemon() gets pokemon from a tethered pokemon list`() {
//        val pokemon1 = mockk<Pokemon>()
//        val pokemon2 = mockk<Pokemon>()
//        val pokemon3 = mockk<Pokemon>()
//
//        val tethering1 = mockk<PokemonPastureBlockEntity.Tethering> {
//            every { getPokemon() } returns pokemon1
//        }
//        val tethering2 = mockk<PokemonPastureBlockEntity.Tethering> {
//            every { getPokemon() } returns pokemon2
//        }
//        val tethering3 = mockk<PokemonPastureBlockEntity.Tethering> {
//            every { getPokemon() } returns pokemon3
//        }
//
//        val pokemonList = listOf(pokemon1, pokemon2, pokemon3)
//        val tetheringList = listOf(tethering1, tethering2, tethering3)
//
//        assertEquals(pokemonList, tetheringList.getPokemon())
//
//        verify {
//            @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//            pokemon1.equals(pokemon1)
//
//            @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//            pokemon2.equals(pokemon2)
//
//            @Suppress("UnusedEquals", "ReplaceCallWithBinaryOperator")
//            pokemon3.equals(pokemon3)
//
//            tethering1.getPokemon()
//            tethering2.getPokemon()
//            tethering3.getPokemon()
//        }
//
//        checkUnnecessaryStub(
//            pokemon1,
//            pokemon2,
//            pokemon3,
//
//            tethering1,
//            tethering2,
//            tethering3
//        )
//    }
//}
