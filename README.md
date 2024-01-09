<h1 align=center>Party Crackers</h1>

A simple mini-plugin that gives players party crackers that can be thrown onto the ground and explode into a burst of particles after a few seconds and then drop random reward item(s).

## Commands

- cracker give <cracker-name> <amount> -> Gives the sender an x amount, or 1 if not defined, of the specified cracker. (crackers.give)

## Parameters and Configuration
Each cracker has their own YML file with the following 7 parameteres / sections:

### Identification
* Identifier -> The cracker's unique ID, used for commands and general data manipulation.
* Display Name -> The cracker's display name, can be used for GUIs and to represent the cracker visually.
* Display Item -> The cracker's item that's given to the player, can be used for GUIs and visual presentations as well.
### Animations
* Explosion time -> Time for the explosion to happen in ticks.
* Explosion particles -> A list of possible particles that can be used when the cracker explodes
* Explosion sounds -> A list of sounds that can be used when the cracker explodes.

### Rewards
* Rewards consist of 4 types, [command, message, items, money] these types each do different things.
* You can have multiple rewards of different types, just be sure to define their ID.
* Rewards are ran in the same order as the configuration.
* Everything specific is explained in the default cracker's configuration.

## Other

* when a cracker is opened it sends an HTTP Post request to the defined URL on the config. That request contains the player's id, cracker's id and the time the player opened the cracker.
