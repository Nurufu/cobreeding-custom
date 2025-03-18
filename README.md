# Cobbreeding

A side-mod for Cobblemon to add Pokémon breeding early.

## Functionality

This mod extends the pasture block's functionality to enable compatible Pokémon in the pasture to lay eggs!
Just place your Pokémon to roam in the pasture and come back later to maybe find an egg in the nesting area on the bottom of the pasture block.

### Implemented features

Here is the list of features included in this mod regarding breeding. Marked features work a bit differently than in pokemon games of the main serie.

- Compatible pokemon breed if let in the pasture together, even with different owners.
- IVs inheritance, including function of Power items and Destiny Knot.
- Nature inheritance, including function of the Everstone.
- Form inheritance(1).
- Ability inheritance(2).
- Eggmoves inheritance. Every move the parents ever learned are included, so no need to equip them in their move set(3).
- Pokeball inheritance.
- Shiny hunting using methods similar to Masuda or Gen2 shiny breeding.
- Abilities reducing hatch time by half

(1). Regional forms are directly inherited from the parents rather than checking the biome.\
(2). Small chance to randomly get the pokemon's hidden ability! A parent with its hidden ability can transfer it to its eggs normally.\
(3). As Light Ball isn't implemented, Pichu gets Volt Tackle without any requirement.

Other features include :
- Eggs spawning in the pasture block when pokemon successfully bred.
- Being able to pick up an egg in the bottom of a pasture block with an emplty hand.
- Keeping pokemon eggs in the inventory that will hatch with time passing (timer is stopped if the egg is not in a player inventory).
- Mirror Herb teaching Egg Moves every time the block rolls for an egg.

### Configurable options

- eggCheckTicks  (Default : 12000) : How many ticks between each attempts to spawn an egg in a Pasture Block.
- eggCheckChance (Default : 0.5) : How likely an attempt to spawn an egg succeeds.
- eggHatchMultiplier (Default : 1.0) : Controls how quickly eggs hatch. At 1.0, an egg from a pokemon having an egg cycle of 20 takes about 10 minutes to hatch. The lower this value is, the faster the egg hatches.
- shinyMethod (Default to "masuda") :
    - "disabled" : Pokemon will hatch with a normal shiny rate.
    - "masuda" : If the two pokemon making an egg belong to different trainers, the shiny rate will be multiplied by shinyMultiplier.
    - "crystal" : Each shiny parent multiplies the shiny rate for the egg by shinyMultiplier.
    - "crystal masuda" : Combines both crystal and masuda methods.
- shinyMultiplier (default to 4.0) : Multiplies the shiny rate of the egg depending of the shinyMethod that has been set.
- hiddenAbilitiesEnabled (Default : true) : The offspring has a slight chance to obtain its hidden ability even if not inherited
- forcedAbilitiesEnabled (Default : false) : If the mother/non-Ditto has a forced ability, so will the offspring
- dittoAndDittoRandomEgg (Default : false) : Breeding two Ditto will result in a random offspring
- dittoAndDittoAllowLegendary (Default : false) : Allow legendary and mythical from Ditto random breeding

## Known issues

- Mooshtanks gives birth to regular Miltanks