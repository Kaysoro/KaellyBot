# KaellyBOT

<a href="https://discordapp.com/oauth2/authorize?&client_id=202916641414184960&scope=bot"><img align="right" src="https://i.imgur.com/9R9HLqa.png" width=27%></a>

[![Build Status](https://travis-ci.org/Kaysoro/KaellyBot.svg?branch=master)](https://travis-ci.org/Kaysoro/KaellyBot)
[![pipeline status](https://gitlab.com/Kaysoro/KaellyBot/badges/master/pipeline.svg)](https://gitlab.com/Kaysoro/KaellyBot/commits/master)
[![Known Vulnerabilities](https://snyk.io/test/github/kaysoro/kaellybot/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/kaysoro/kaellybot?targetFile=pom.xml)
[![Coverage Status](https://coveralls.io/repos/github/Kaysoro/KaellyBot/badge.svg?branch=master)](https://coveralls.io/github/Kaysoro/KaellyBot?branch=master)

KaellyBOT aims to provide useful commands for DOFUS community (FR/EN/ES)! If you have questions, suggestions or just want to say hello, feel free to join the [discord support server](https://discord.gg/CyJCFDk) :)

## Invite KaellyBOT to your server
There is a running official instance used by 10.000+ discord servers and 540.000+ users.  
Just [click here](https://discordapp.com/oauth2/authorize?&client_id=202916641414184960&scope=bot), follow the discord instructions and that's it!

## Commands
![KaellyBOT commands](https://i.imgur.com/37Ifk6o.png "KaellyBOT commands")

## Data privacy
Only Discord user IDs are stored in database to make it run.  
This data is registered when a user register himself in a book like the job book; users can be easily unregistered as described in each *book command* help.

**Collected informations are and will never be used for commercial purposes.**

## Developer section
This section is dedicated for developers. If you're not, please use the official instance described above.  
*Note: this repository is considered as deprecated: you can find some explanations just below.*

### Current structure
- Java 8 project based on Discord4J V3.2.1
- Built with Maven 
- Commands detection with regex
- Store data into SQLite database
- Start it with `java -jar Kaellybot.jar`

### Limitations
With the time and the growing usage, a lot of new problems appear:
- The use of file database is limited and adapted for small bots
- No smart caching for stored data
- No use of Reactor project to optimize the performances
- Permissions needed to send a message are not well checked
- Commands arguments are not well divided: all behaviour described in the command class
- Almanax, RSS and Twitter events are not well managed for large bots (it breaks very often)
- Usage of tmux/screen to host it
- Some of these previous limitations require more RAM and so increase the cost to host it

All these factors push me to put this project as deprecated and to think about a new architecture.

### What's next?
The [KaellyBot organization](https://github.com/KaellyBot/) has been created to contain several projects like:
- [Kaelly-core](https://github.com/KaellyBot/Kaelly-core) is the equivalent of this one with the following structure:
    - Java 15 project based on SpringBoot V2.X and Discord4J V3.1.X.
    - Built with Maven
    - Dockerized with Jib
    - Massive use of Reactor
    - Massive use of Annotations to describe the commands
    - Massive use of CDI: everything is injectable
    - Commands arguments detection with regex
    - Store data into MongoDB database
    - Microservices and dedicated libraries built for it
    - This version is still in development and is partially used by the official instance ([DofusRoom](https://www.dofusroom.com/) feature is the only one available).
- [Kaelly-portals](https://github.com/KaellyBot/Kaelly-portals) is a SpringBoot microservice to manage the portals positions from different sources like [dofus-portals.fr](https://dofus-portals.fr/). It is currently used by the official instance.
- [Kaelly-commons](https://github.com/KaellyBot/Kaelly-commons) is a SpringBoot library to bring some commons entities or tools, currently used by Kaelly-core and Kaelly-portals.
- [Kaelly-dashboard](https://github.com/KaellyBot/Kaelly-dashboard) is a first draft of a dashboard website (Node/Express/DiscordJS) to configure KaellyBot on your server. This project is paused, not prior and not used. The chosen technologies are not totally defined and will probably change in the future.
- [Kaelly-environment](https://github.com/KaellyBot/Kaelly-environment) is the place to store some docker-compose files and automate easily the launch of KaellyBot (partially or entirely) on a new machine.

There is still a lot to do, but I'm working actively on it! If you wanna help, feel free to join the [discord support server](https://discord.gg/CyJCFDk) and discuss! :)

## License  
KaellyBOT is [GPL(v3) licensed](./LICENSE).

## Partners  
[JetBrains](https://www.jetbrains.com/?from=KaellyBot) supports this project since the beginning by providing us its products!  
[![JetBrains logo](https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/JetBrains_Logo_2016.svg/100px-JetBrains_Logo_2016.svg.png)](https://www.jetbrains.com/?from=KaellyBot)  

## Thank you!  
The development and the availability of KaellyBot 24/7 generate ongoing cost. Do not hesitate to help the project grow with a donation!   
[![Donate](https://www.paypalobjects.com/en_US/FR/i/btn/btn_donateCC_LG.gif)](https://www.paypal.me/kaysoro)

### Donators
- Hart69#0001
- Elder-Master#7684
- Darkrai#8780
- DreamsVoid#8802
- ! Siid !#0001
- Tynagmo