import json
import sys

dataFile = open('../pokemon.json')

pokedex = json.load(dataFile)
pokedexLength = len(pokedex)
newPokemon = [
    {
        "generation": 9,
        "name": "Walking Wake",
        "breedable": False
    },
    {
        "generation": 9,
        "name": "Iron Leaves",
        "breedable": False
    },
    {
        "generation": 9,
        "name": "Bloodmoon Ursaluna",
        "breedable": True
    },
    {
        "generation": 9,
        "name": "Dipplin",
        "breedable": True,
        "family": ["Applin", "Dipplin", "Hydrapple"]
    },
    {
        "generation": 9,
        "name": "Poltchageist",
        "breedable": True,
        "family": ["Poltchageist", "Sinistcha"]
    },
    {
        "generation": 9,
        "name": "Sinistcha",
        "breedable": True,
        "family": ["Poltchageist", "Sinistcha"]
    },
    {
        "generation": 9,
        "name": "Okidogi",
        "breedable": False
    },
    {
        "generation": 9,
        "name": "Munkidori",
        "breedable": False
    },
    {
        "generation": 9,
        "name": "Fezandipiti",
        "breedable": False
    },
    {
        "generation": 9,
        "name": "Ogerpon",
        "breedable": False,
        "forms": ["Teal Mask", "Wellspring Mask", "Hearthflame Mask", "Cornerstone Mask"]
    },
    {
        "generation": 9,
        "name": "Archaludon",
        "breedable": True,
        "family": ["Duraludon", "Archaludon"]
    },
    {
        "generation": 9,
        "name": "Hydrapple",
        "breedable": True,
        "family": ["Applin", "Dipplin", "Hydrapple"]
    },
    {
        "generation": 9,
        "name": "Gouging Fire",
        "breedable": False
    },
    {
        "generation": 9,
        "name": "Raging Bolt",
        "breedable": False
    },
    {
        "generation": 9,
        "name": "Iron Boulder",
        "breedable": False
    },
    {
        "generation": 9,
        "name": "Iron Crown",
        "breedable": False
    },
    {
        "generation": 9,
        "name": "Terapagos",
        "breedable": False,
        "forms": ["Normal", "Terastal"]
    },
    {
        "generation": 9,
        "name": "Pecharunt",
        "breedable": False
    },
]


def findPokemonIndex(pokemonName):
    for index, pokemon in enumerate(pokedex):
        if pokemonName == pokemon['name']:
            return index
    print("Could not find pokemon in pokedex")


def checkMerge(new, old):
    for i, member in enumerate(old):
        if new[i] != old[i]:
            if i == 0 and new[i + 1] != old[i]:
                break
            return False
    return True


def updateFamily(new, old):
    if isinstance(old[0], list):
        mergedFlag = False
        for i, arr in enumerate(old):
            if checkMerge(new, arr):
                old[i] = new
                mergedFlag = True
                break
        if not mergedFlag:
            old.append(new)
    else:
        if checkMerge(new, old):
            old = new
        else:
            newArr = []
            newArr.append(old)
            newArr.append(new)
            old = newArr

    return old


# add pokemon to dex with only name
for pokemon in newPokemon:
    pokedex.append({'name': pokemon['name']})

# add the rest of the data to the pokedex
for pokedexIndex in range(pokedexLength, len(pokedex)):
    pokemonData = newPokemon[pokedexIndex - pokedexLength]
    for key in pokemonData:
        if key == 'name':
            continue
        if key == 'family':
            for i, member in enumerate(pokemonData['family']):
                pokemonData['family'][i] = findPokemonIndex(member)
        pokedex[pokedexIndex][key] = pokemonData[key]

# update pokemon families from previous entries
for pokemon in newPokemon:
    if 'family' in pokemon:
        for member in pokemon['family']:
            if member > pokedexLength - 1:
                continue

            newTree = pokemon['family']
            if 'family' in pokedex[member]:
                oldTree = pokedex[member]['family']
            else:
                oldTree = [member]

            if newTree == oldTree:
                continue

            print(pokedex[member]['name'] + "'s new family tree is:", end=" ")
            pokedex[member]['family'] = updateFamily(newTree, oldTree)
            print(pokedex[member]['family'])

confirm = input("Does this look correct? (y/n)\n").lower() == 'y'

if not confirm:
    sys.exit()

json_save = json.dumps(pokedex, indent=2)

with open('../pokemon.json', 'w') as outfile:
    outfile.write(json_save)

print("New pokemon have been added")

dataFile.close()
