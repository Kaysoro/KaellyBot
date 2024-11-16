# KaellyBOT

<a href="https://discordapp.com/oauth2/authorize?&client_id=202916641414184960&scope=bot"><img align="right" src="https://i.imgur.com/9R9HLqa.png" width=27%></a>

KaellyBOT aims to provide useful commands for DOFUS community (FR/EN/ES)! If you have questions, suggestions or just want to say hello, feel free to join the [discord support server](https://discord.gg/CyJCFDk) :)

## Invite KaellyBOT to your server
There is a running official instance used by 13.000+ discord servers and 660.000+ users.  
Just [click here](https://discordapp.com/oauth2/authorize?&client_id=202916641414184960&scope=bot), follow the discord instructions and that's it!

## Commands

The commands below won't work anymore sooner or later. This bot was created before slash commands arrived and the new version is heavily related to this Discord interaction system now.
That being said, the commands principle remains the same, just use `/` in Discord!
![KaellyBOT commands](https://i.imgur.com/37Ifk6o.png "KaellyBOT commands")

## Data privacy
Only Discord user IDs are stored in database to make it run.  
This data is registered when a user register himself in a book like the job book; users can be easily unregistered as described in each *book command* help.

**Collected information are and will never be used for commercial purposes.**

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
- Updates force downtime on any servers, on any features
- One monolith handles every Discord shards making it clumsy when one shard is offline time to time

All these factors push me to put this project as deprecated and to think about a new architecture.

### What's next?
The [KaellyBot organization](https://github.com/KaellyBot/) has been created as ecosystem divided into modular repositories (based on Golang and communicating through RabbitMQ), each serving a specific purpose:
* Kaelly-discord
  * Handles Discord interactions, commands, and events.

* Kaelly-encyclopedia
    * Retrieves game data like items, sets, almanax from [DofusDude API](http://dofusdu.de).

* Kaelly-configurator
  * Handles bot configurations like game server binding, webhooks notifications, etc.

* Kaelly-books
  * Handles alignment and job registries.

* Kaelly-rss
  * Retrieves RSS feeds from Ankama games websites

* Kaelly-twitter
  * Retrieves tweets from official Ankama game account

* Kaelly-metrics
  * Collects, processes, and visualizes bot metrics for insights.

... and so on. Here below the planned architecture with most of the modules.
![KaellyBOT next architecture](https://i.imgur.com/4fefjkp.png "KaellyBOT next architecture")

There is still a lot to do and the beginning of a new start, I'm working actively on it! If you want to help, feel free to join the [discord support server](https://discord.gg/CyJCFDk) and discuss! :)

### What about this repository?

Well, it won't be archived but probably used as documentation to explain KaellyBot history, tech choices and how to use it right now.

## License  
KaellyBOT is [GPL(v3) licensed](./LICENSE).

## Thank you!  

The development and the availability of KaellyBot 24/7 generate ongoing cost. Do not hesitate to help the project grow with a donation!   
[![Donate](https://www.paypalobjects.com/en_US/FR/i/btn/btn_donateCC_LG.gif)](https://www.paypal.me/kaysoro)

### Donors

- Hart69
- Eaglow
- Darkrai25
- DreamsVoid
- Siid
- Tynagmo
- Nocks
- Sacree-Sacri

## Star History

<a href="https://star-history.com/#kaysoro/kaellybot&Date">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://api.star-history.com/svg?repos=kaysoro/kaellybot&type=Date&theme=dark" />
    <source media="(prefers-color-scheme: light)" srcset="https://api.star-history.com/svg?repos=kaysoro/kaellybot&type=Date" />
    <img alt="Star History Chart" src="https://api.star-history.com/svg?repos=kaysoro/kaellybot&type=Date" />
  </picture>
</a>