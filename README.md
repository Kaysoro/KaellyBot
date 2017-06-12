# KaellyBOT [![Build Status](https://travis-ci.org/Kaysoro/KaellyBot.svg?branch=master)](https://travis-ci.org/Kaysoro/KaellyBot) [![Dependency Status](https://www.versioneye.com/user/projects/587eb975452b8300313609ee/badge.svg?style=flat)](https://www.versioneye.com/user/projects/587eb975452b8300313609ee) [![Coverage Status](https://coveralls.io/repos/github/Kaysoro/KaellyBot/badge.svg?branch=master)](https://coveralls.io/github/Kaysoro/KaellyBot?branch=master)
KaellyBOT est destinée à fournir des commandes utiles à la communauté de DOFUS ! L'intégralité de son code est libre d'accès. Si vous avez des questions, des suggestions ou que vous souhaitez juste passer un coucou, rejoignez le discord : [![Support Server Invite](https://img.shields.io/badge/Join-KaellyBOT%20Support-7289DA.svg?style=flat)](https://discord.gg/CyJCFDk)

## Ajoutez KaellyBOT à votre serveur : [![Official Kaelly Invite](https://img.shields.io/badge/Add-KaellyBOT-0199FE.svg?style=flat)](https://discordapp.com/oauth2/authorize?&client_id=202916641414184960&scope=bot)

## Commandes

La commande `!help` explique chaque commande.
La commande `!help command` explique de façon détaillée la commande spécifiée.

### Almanax

`!almanax` : Donne le bonus et l'offrande du jour actuel.
`!almanax jj/mm/aaaa` : Donne le bonus et l'offrande du jour spécifié.

### About

`!about` : Donne des informations sur KaellyBOT et un moyen d'obtenir de l'aide.

### Item

`!item` : Renvoit les statistiques d'un item du jeu Dofus.

`!item item` : Renvoit les statistiques de l'item spécifié : son nom peut être approximatif s'il est suffisemment précis. A noter que les items inférieurs au niveau 50 sont exclus.

### Job

`!job` : Renvoit la liste des métiers du jeu Dofus.
`!job métier` : Renvoit l'annuaire des artisans pour ce métier.
`!job métier niveau` : Vous ajoute à l'annuaire du métier correspondant. Si vous indiquez 0, vous êtes supprimé de l'annuaire pour ce métier.
`!job -allniveau` : Vous ajoute à l'annuaire pour tous les métiers correspondants. Si vous indiquez 0, vous êtes supprimé de chaque annuaire.

### Map

`!map` : Tire au hasard une carte du Goultarminator ou bien parmi celles spécifiées en paramètre.

`!map map1 map2 ...` : Tire une carte parmi celles spécifiées en paramètre. Nombres romains ou numériques uniquement.

### Pos (Dimensions)

`!pos` : Donne la position de tous les portails de dimension.

`!pos dimension` : Donne la position du portail de la dimension désirée.
`!pos dimension[POS, POS]` : Met à jour la position du portail de la dimension spécifiée.
`!pos dimension[POS, POS]nombre d'utilisations` : Met à jour la position et le nombre d'utilisation de la dimension spécifiée.
`!pos -resetdimension` : Supprime les informations de la dimension spécifiée.

### Prefixe

`!prefixe` : Change le préfixe utilisé pour invoquer une commande. Niveau modérateur minimum requis.

`!prefixe prefixe` : Change le préfixe par celui passé en paramètre. 3 maximum.

### Random

`!rdm` : Tire une valeur entre Vrai et Faux.
`!rdm nombre` : Tire une valeur entre 0 et nombre.
`!rdm valeur1 valeur2 ...` : Tire une valeur parmi celles spécifiées en paramètre.

### Right

`!right` : Donne le niveau d'administration de l'auteur de la requête.
`!right @pseudo` : Donne le niveau d'administration de l'utilisateur ou d'un groupe spécifié.
`!right @pseudo niveau` : Change le niveau d'administration d'un utilisateur ou d'un groupe spécifié.

### RSS

`!rss true` : Poste les news à partir du flux RSS de Dofus.com. Fonctionne aussi avec `on` et `0`.
`!rss false` : Ne poste plus les flux RSS dans le salon. Fonctionne aussi avec `off` et `1`.

### Rule34 (NSFW)

`!rule34 tag1 tag2 ...` : Poste du contenu sexuellement explicite (NSFW) avec les tag précisés.

### Sound

`!sound` : Donne la liste des sons pouvant être joués par le bot.
`!sound sound` : Joue le son spécifié.

### Twitter

`!twitter` : Poste les tweets de Dofusfr dans un salon; nécessite un niveau d'administration 2 (Modérateur) minimum.

`!twitter true` : Poste les tweets de Dofusfr. Fonctionne aussi avec `on` et `0`.
`!twitter false` : Ne poste plus les tweets dans le salon. Fonctionne aussi avec `off` et `1`.

### Whois

`!whois *pseudo*` : Donne la page personnelle associée au pseudo. Celui-ci doit être exact.
`!whois *pseudo serveur*` : Est à utiliser lorsque le pseudo ne suffit pas pour déterminer la fiche d'un personnage.
