# Battleground
Random Kits minecraft server.\
Battleground.minehut.gg

### Building
- Tested on:
  - Maven 3.9.6
  - Java 21.0.3
  - Paper 1.21.1
  - LuckPerms 5.4.139
- Clone the repo
- Install LuckPerms in server/plugins
- build with `mvn clean install`
- I use `mvn clean install; cd server; ./start.bat; cd ..` in Intellij on Windows
- plugin is automatically built to server/plugins folder
- MongoDB required, put connection string in server/plugins/Battleground/config.yml
- Database name is "kits" with a "player" collection

### Testing
- It's easy to test since the test server is included in this repo, check the build script from earlier that I use to build and start in one command
- **Create an Enchantment**:
  - I recommend just copying another enchantment and editing it, make sure it's in the proper folder in enchantments/enchantments/[rarity]/[type]
  - Make sure you add the enchantment in the list in enchantments/Enchantments.kt
  - Make sure to use the custom entity damage by entity event, and use event.attacker and event.victim
  - Check the Tiny enchantment in enchantments/legendary/armor for an example of an attribute modifier
  - Test with /enchant
- **Create an Ability**:
  - Same thing as enchantments, but it's in the abilities package and the class is slightly different
  - Test with /ability
- **For any events for specific enchants or abilities, put them in the same file, check the common/Jump ability for an example.**
- Commands are similar. Make sure to register them in commands/Commands.kt

### Contributing
- Suggestions or Pull Requests are welcomed, just try to follow the way I design things. Checkout the Utilities package for my extension functions and other utilities, Example:
``` 
  player.mongoPlayer().kills += 1
  
  Utilities.async {
      Bukkit.broadcast("This runs asyncronously".miniMessage()) 
  }
  
  Utilities.whileLoop({ !player.isDead }, { 
      player.miniMessage("Hello") 
  }, 20)
  
  Utilities.after({ 
      player.miniMessage("This runs after 1 second.") 
  }, 20)
  ```