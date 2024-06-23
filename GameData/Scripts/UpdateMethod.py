import json
import sys

# TODO
# Add version exclusive pokemon

pokemonDataFile = open('../pokemon.json')
gameDataFile = open('../game.json')
methodDataFile = open('../method.json')

pokedex = json.load(pokemonDataFile)

methodId = 15
methodData = json.load(methodDataFile)
currentMethodData = methodData[methodId]

gameData = json.load(gameDataFile)
tableName = "table-" + str(methodId)

newPokemon = [
    "Ekans",
    "Arbok",
    "Pikachu",
    "Sandshrew",
    "Sandslash",
    "Clefairy",
    "Vulpix",
    "Mankey",
    "Primeape",
    "Growlithe",
    "Poliwag",
    "Poliwhirl",
    "Bellsprout",
    "Weepinbell",
    "Geodude",
    "Graveler",
    "Gastly",
    "Haunter",
    "Gengar",
    "Koffing",
    "Magikarp",
    "Gyarados",
    "Sentret",
    "Furret",
    "Hoothoot",
    "Noctowl",
    "Spinarak",
    "Ariados",
    "Pichu",
    "Cleffa",
    "Sudowoodo",
    "Aipom",
    "Yanma",
    "Wooper",
    "Quagsire",
    "Dunsparce",
    "Gligar",
    "Heracross",
    "Sneasel",
    "Slugma",
    "Swinub",
    "Piloswine",
    "Houndour",
    "Houndoom",
    "Stantler",
    "Poochyena",
    "Mightyena",
    "Lotad",
    "Lombre",
    "Seedot",
    "Nuzleaf",
    "Ralts",
    "Kirlia",
    "Gardevoir",
    "Surskit",
    "Masquerain",
    "Nosepass",
    "Volbeat",
    "Illumise",
    "Spoink",
    "Barboach",
    "Whiscash",
    "Corphish",
    "Crawdaunt",
    "Feebas",
    "Duskull",
    "Dusclops",
    "Chimecho",
    "Snorunt",
    "Starly",
    "Staravia",
    "Staraptor",
    "Kricketot",
    "Kricketune",
    "Shinx",
    "Luxio",
    "Luxray",
    "Pachirisu",
    "Chingling",
    "Bronzor",
    "Bronzong",
    "Bonsly",
    "Gible",
    "Gabite",
    "Munchlax",
    "Riolu",
    "Lucario",
    "Gallade",
    "Timburr",
    "Gurdurr",
    "Sewaddle",
    "Swadloon",
    "Leavanny",
    "Petilil",
    "Basculin",
    "Ducklett",
    "Tynamo",
    "Eelektrik",
    "Litwick",
    "Lampent",
    "Mienfoo",
    "Pawniard",
    "Bisharp",
    "Vullaby",
    "Carbink",
    "Goomy",
    "Sliggoo",
    "Phantump",
    "Trevenant",
    "Noibat",
    "Grubbin",
    "Charjabug",
    "Oricorio",
    "Cutiefly",
    "Ribombee",
    "Rockruff",
    "Lycanroc",
    "Mudbray",
    "Fomantis",
    "Lurantis",
    "Salandit",
    "Mimikyu",
    "Jangmo-o",
    "Hakamo-o",
    "Skwovet",
    "Greedent",
    "Chewtle",
    "Drednaw",
    "Applin",
    "Flapple",
    "Appletun",
    "Cramorant",
    "Arrokuda",
    "Barraskewda",
    "Hatenna",
    "Hattrem",
    "Hatterene",
    "Impidimp",
    "Morgrem",
    "Grimmsnarl",
    "Indeedee",
    "Morpeko",
    "Dudunsparce",
    "Orthworm",
    "Tandemaus",
    "Bombirdier",
    "Glimmet",
    "Toedscool",
    "Toedscruel",
    "Kingambit",
    "Annihilape",
    "Poltchageist",
    "Bulbasaur",
    "Charmander",
    "Squirtle",
    "Oddish",
    "Gloom",
    "Vileplume",
    "Venonat",
    "Venomoth",
    "Diglett",
    "Dugtrio",
    "Tentacool",
    "Tentacruel",
    "Slowpoke",
    "Magnemite",
    "Magneton",
    "Doduo",
    "Dodrio",
    "Seel",
    "Dewgong",
    "Grimer",
    "Muk",
    "Exeggcute",
    "Exeggutor",
    "Hitmonlee",
    "Hitmonchan",
    "Rhyhorn",
    "Rhydon",
    "Chansey",
    "Horsea",
    "Seadra",
    "Scyther",
    "Electabuzz",
    "Magmar",
    "Tauros",
    "Lapras",
    "Porygon",
    "Chikorita",
    "Cyndaquil",
    "Totodile",
    "Chinchou",
    "Lanturn",
    "Bellossom",
    "Girafarig",
    "Snubbull",
    "Granbull",
    "Qwilfish",
    "Scizor",
    "Skarmory",
    "Smeargle",
    "Tyrogue",
    "Hitmontop",
    "Elekid",
    "Magby",
    "Blissey",
    "Treecko",
    "Torchic",
    "Mudkip",
    "Slakoth",
    "Vigoroth",
    "Slaking",
    "Plusle",
    "Minun",
    "Numel",
    "Camerupt",
    "Torkoal",
    "Trapinch",
    "Vibrava",
    "Flygon",
    "Swablu",
    "Altaria",
    "Zangoose",
    "Seviper",
    "Luvdisc",
    "Beldum",
    "Metang",
    "Turtwig",
    "Chimchar",
    "Piplup",
    "Cranidos",
    "Shieldon",
    "Happiny",
    "Finneon",
    "Lumineon",
    "Snover",
    "Abomasnow",
    "Rotom",
    "Snivy",
    "Tepig",
    "Oshawott",
    "Blitzle",
    "Zebstrika",
    "Drilbur",
    "Excadrill",
    "Cottonee",
    "Whimsicott",
    "Sandile",
    "Krokorok",
    "Krookodile",
    "Scraggy",
    "Scrafty",
    "Minccino",
    "Cinccino",
    "Gothita",
    "Gothorita",
    "Gothitelle",
    "Solosis",
    "Duosion",
    "Reuniclus",
    "Deerling",
    "Sawsbuck",
    "Alomomola",
    "Joltik",
    "Galvantula",
    "Eelektross",
    "Axew",
    "Fraxure",
    "Cubchoo",
    "Beartic",
    "Golett",
    "Golurk",
    "Rufflet",
    "Braviary",
    "Mandibuzz",
    "Chespin",
    "Fennekin",
    "Froakie",
    "Fletchling",
    "Fletchinder",
    "Talonflame",
    "Litleo",
    "Pyroar",
    "Espurr",
    "Meowstic",
    "Inkay",
    "Rowlet",
    "Litten",
    "Popplio",
    "Pikipek",
    "Trumbeak",
    "Toucannon",
    "Crabrawler",
    "Crabominable",
    "Dewpider",
    "Araquanid",
    "Comfey",
    "Minior",
    "Bruxish",
    "Grookey",
    "Scorbunny",
    "Sobble",
    "Sinistea",
    "Milcery",
    "Duraludon",
    "Kleavor",
    "Rellor",
    "Rabsca",
    "Farigiraf"
]

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

for pokemon in newPokemon:
    if pokemon in currentMethodData['pokemon']:
        continue
    currentMethodData['pokemon'].append(pokemon)

currentMethodData['pokemon'].sort()

for game in currentMethodData['games']:
    gameData[game][tableName] = currentMethodData['pokemon']
    print(gameData[game]['name'], "has been updated")

json_save = json.dumps(gameData, indent=2)
with open('../game.json', 'w') as outfile:
    outfile.write(json_save)

methodData[methodId] = currentMethodData
json_save = json.dumps(methodData, indent=2)
with open('../method.json', 'w') as outfile:
    outfile.write(json_save)

pokemonDataFile.close()
gameDataFile.close()
methodDataFile.close()
