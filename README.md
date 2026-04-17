# ThirdSpareMain - The ThirdSpare Server Plugin

Gradle & 1.18 Fork of ThirdSpareMain (ThirdSparePlugin). It was simpler to fork code and set up a new project clean 
using Gradle instead of Maven - As suggested by the PaperMC community as of Minecraft 1.17.
___
This plugin is a personal "and the kitchen sink" type of plugin to be used on any of our "Thirdspare" servers. The 
plugin aims to replace an essential, economy, backpack, and friend system plugins. It has a goal of being easy to read
(that is yet to come lol) and in the future being more modular.


Currently the plugin is built against ***Paper 1.21.10 R01 Snapshot***

## TO-DO
___
- Code cleanup - It is needed to go through and clean up repeated code, as well as make it more readable
- Create permissions for existing commands
- Convert Java code to Kotlin

# Configuration Notes

### Chat Channel Configuration
Inside of `ThirdSpareMain\data`, a `channels.json` file, which is where channels will be generated on first load and contain all custom channel data such
as the name, the prefix and the color assigned. 
Attached below is an example of the channel list array inside of `channels.json`
> ### Character to Colour options for channel config
> 'G' -> Green  
> 'C' -> Aqua  
> 'B' -> Blue  
> 'Y' -> Yellow   
> 'R' -> Red/Light Red  
> 'M' -> Magenta/Light Purple    

   
   
```
{
  "channelList": [
    {
      "name": "GLOBAL",
      "prefix": "G",
      "color": "G"
    },
    {
      "name": "TRADE",
      "prefix": "T",
      "color": "R"
    }
  ]
}
```