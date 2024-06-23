import json
import sys

gameDataFile = open('../game.json')
pokemonDataFile = open('../pokemon.json')

pokedex = json.load(pokemonDataFile)

gameData = json.load(gameDataFile)
game = "scarlet"
gameIndex = 0

newPokemon = [
    "Ekans",
    "Arbok",
    "Sandshrew",
    "Sandslash",
    "Cleffa",
    "Clefairy",
    "Clefable",
    "Oddish",
    "Gloom",
    "Vileplume",
    "Bellossom",
    "Vulpix",
    "Ninetales",
    "Poliwag",
    "Poliwhirl",
    "Poliwrath",
    "Politoed",
    "Bellsprout",
    "Weepinbell",
    "Victreebel",
    "Geodude",
    "Graveler",
    "Golem",
    "Munchlax",
    "Snorlax",
    "Sentret",
    "Furret",
    "Hoothoot",
    "Noctowl",
    "Spinda",
    "Ariados",
    "Aipom",
    "Ambipom",
    "Yanma",
    "Yanmega",
    "Gligar",
    "Gliscor",
    "Swinub",
    "Piloswine",
    "Mamoswine",
    "Poochyena",
    "Mightyena",
    "Seedot",
    "Nuzleaf",
    "Shiftry",
    "Lotad",
    "Lombre",
    "Ludicolo",
    "Corphish",
    "Crawdaunt",
    "Chingling",
    "Chimecho",
    "Volbeat",
    "Illumise",
    "Slugma",
    "Magcargo",
    "Feebas",
    "Milotic",
    "Duskull",
    "Dusclops",
    "Dusknoir",
    "Turtwig",
    "Grotle",
    "Torterra",
    "Piplup",
    "Prinplup",
    "Empoleon",
    "Chimchar",
    "Monferno",
    "Infernape",
    "Timburr",
    "Gurdurr",
    "Conkeldurr",
    "Sewaddle",
    "Swadloon",
    "Leavanny",
    "Litwick",
    "Lampent",
    "Chandelure",
    "Vullaby",
    "Mandibuzz",
    "Phantump",
    "Trevenant",
    "Grubbin",
    "Charjabug",
    "Vikavolt",
    "Cutiefly",
    "Ribombee",
    "Jangmo-o",
    "Hakamo-o",
    "Kommo-o",
    "Cramorant",
    "Morpeko",
    "Ursaluna",
    "Basculegion",
    "Bulbasaur",
    "Ivysaur",
    "Venusaur",
    "Squirtle",
    "Wartortle",
    "Blastoise",
    "Oddish",
    "Gloom",
    "Vileplume",
    "Tentacool",
    "Tentacruel",
    "Doduo",
    "Dodrio",
    "Seel",
    "Dewgong",
    "Exeggcute",
    "Exeggutor",
    "Tyrogue",
    "Hitmonlee",
    "Hitmonchan",
    "Hitmontop",
    "Magby",
    "Magmar",
    "Magmortar",
    "Elekid",
    "Electabuzz",
    "Electivire",
    "Rhyhorn",
    "Rhydon",
    "Rhyperior",
    "Lapras",
    "Porygon",
    "Porygon2",
    "Porygon-Z",
    "Chikorita",
    "Bayleef",
    "Meganium",
    "Totodile",
    "Croconaw",
    "Feraligatr",
    "Chinchou",
    "Lanturn",
    "Bellossom",
    "Snubbull",
    "Granbull",
    "Smeargle",
    "Horsea",
    "Seadra",
    "Kingdra",
    "Skarmory",
    "Raikou",
    "Entei",
    "Suicune",
    "Lugia",
    "Ho-Oh",
    "Treecko",
    "Grovyle",
    "Sceptile",
    "Torchic",
    "Combusken",
    "Blaziken",
    "Mudkip",
    "Marshtomp",
    "Swampert",
    "Plusle",
    "Minun",
    "Trapinch",
    "Vibrava",
    "Flygon",
    "Beldum",
    "Metang",
    "Metagross",
    "Regirock",
    "Regice",
    "Registeel",
    "Latias",
    "Latios",
    "Deoxys",
    "Cranidos",
    "Rampardos",
    "Shieldon",
    "Bastiodon",
    "Snivy",
    "Servine",
    "Serperior",
    "Tepig",
    "Pignite",
    "Emboar",
    "Blitzle",
    "Zebstrika",
    "Drilbur",
    "Excadrill",
    "Cottonee",
    "Whimsicott",
    "Scraggy",
    "Scrafty",
    "Minccino",
    "Cinccino",
    "Solosis",
    "Duosion",
    "Reuniclus",
    "Joltik",
    "Galvantula",
    "Golett",
    "Golurk",
    "Cobalion",
    "Terrakion",
    "Virizion",
    "Reshiram",
    "Zekrom",
    "Kyurem",
    "Keldeo",
    "Meloetta",
    "Espurr",
    "Meowstic",
    "Inkay",
    "Malamar",
    "Litten",
    "Torracat",
    "Incineroar",
    "Popplio",
    "Brionne",
    "Primarina",
    "Pikipek",
    "Trumbeak",
    "Toucannon",
    "Dewpider",
    "Araquanid",
    "Comfey",
    "Minior",
    "Cosmog",
    "Cosmoem",
    "Solgaleo",
    "Lunala",
    "Necrozma",
    "Alolan Geodude",
    "Alolan Graveler",
    "Alolan Golem",
    "Alolan Exeggutor",
    "Milcery",
    "Alcremie",
    "Duraludon",
    "Dipplin",
    "Poltchageist",
    "Sinistcha",
    "Okidogi",
    "Munkidori",
    "Fezandipiti",
    "Ogerpon",
    "Archaludon",
    "Hydrapple",
    "Gouging Fire",
    "Raging Bolt",
    "Iron Boulder",
    "Iron Crown",
    "Terapagos",
    "Pecharunt",
    "Walking Wake",
    "Iron Leaves",
    "Bloodmoon Ursaluna"
]

for i, data in enumerate(gameData):
    if data['name'].lower() == game.lower():
        game = data
        gameIndex = i
        break

continueFlag = True
for i, pokemon in enumerate(newPokemon):
    for dexNum, check in enumerate(pokedex):
        if check['name'] != pokemon:
            if dexNum == len(pokedex) - 1:
                print(pokemon, "is misspelled")
                continueFlag = False
            else:
                continue
        newPokemon[i] = dexNum
        break

if not continueFlag:
    sys.exit()

game['pokedex'] += newPokemon
game['pokedex'].sort()

unbreedables = [[242, 'Raikou'], [243, 'Entei'], [244, 'Suicune'], [248, 'Lugia'], [249, 'Ho-Oh'], [376, 'Regirock'], [377, 'Regice'], [378, 'Registeel'], [379, 'Latias'], [380, 'Latios'], [385, 'Deoxys'], [637, 'Cobalion'], [638, 'Terrakion'], [639, 'Virizion'], [642, 'Reshiram'],
                [643, 'Zekrom'], [645, 'Kyurem'], [646, 'Keldeo'], [647, 'Meloetta'], [788, 'Cosmog'], [789, 'Cosmoem'], [790, 'Solgaleo'], [791, 'Lunala'], [799, 'Necrozma'], [1070, 'Okidogi'], [1071, 'Munkidori'], [1072, 'Fezandipiti'], [1073, 'Ogerpon'], [1076, 'Gouging Fire'],
                [1077, 'Raging Bolt'], [1080, 'Terapagos'], [1081, 'Pecharunt'], [1064, 'Walking Wake'], [1066, 'Bloodmoon Ursaluna']]

if len(unbreedables) == 0:
    for pokemon in newPokemon:
        if not pokedex[pokemon]['breedable']:
            unbreedables.append([pokemon, pokedex[pokemon]['name']])
    print("The following pokemon aren't breedable. Please sort out the ones that aren't catchable in " + game['name'] + " and copy paste the result into the 'breedable' array")
    print(unbreedables)
    sys.exit()
else:
    for pokemon in unbreedables:
        game['unbreedables'].append(pokemon[0])
    game['unbreedables'].sort()

gameData[gameIndex] = game

json_save = json.dumps(gameData, indent=2)

with open('../game.json', 'w') as outfile:
    outfile.write(json_save)

print(game['name'], "has been updated")

gameDataFile.close()
pokemonDataFile.close()
