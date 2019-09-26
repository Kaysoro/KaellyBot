# KaellyBOT [![Build Status](https://travis-ci.org/Kaysoro/KaellyBot.svg?branch=DevTouch)](https://travis-ci.org/Kaysoro/KaellyBot) [![pipeline status](https://gitlab.com/Kaysoro/KaellyBot/badges/DevTouch/pipeline.svg)](https://gitlab.com/Kaysoro/KaellyBot/commits/DevTouch) [![Known Vulnerabilities](https://snyk.io/test/github/kaysoro/kaellybot/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/kaysoro/kaellybot?targetFile=pom.xml) [![Coverage Status](https://coveralls.io/repos/github/Kaysoro/KaellyBot/badge.svg?branch=DevTouch)](https://coveralls.io/github/Kaysoro/KaellyBot?branch=DevTouch)
KaellyTOUCH est destinée à fournir des commandes utiles à la communauté de DOFUS TOUCH (FR/EN)! L'intégralité de son code est libre d'accès. Si vous avez des questions, des suggestions ou que vous souhaitez juste passer un coucou, rejoignez le serveur discord du support : [![Support Server Invite](https://img.shields.io/badge/Join-KaellyBOT%20Support-7289DA.svg?style=flat)](https://discord.gg/CyJCFDk)

## Ajoutez KaellyTOUCH à votre serveur : [![Official Kaelly Invite](https://img.shields.io/badge/Add-KaellyTOUCH-0199FE.svg?style=flat)](https://discordapp.com/oauth2/authorize?&client_id=393925392618094612&scope=bot)

## Commandes

### Help
Cette commande est destinée à expliciter les commandes de Kaelly avec des exemples d'utilisation.   
`!help` explique de manière succincte chaque commande.  
`!help command` explique de façon détaillée la commande spécifiée.  

### Align  
Cette commande renvoie l'annuaire des alignés.  
`!align server` : affiche vos alignements (serveur dofus optionnel sur la commande si celui-ci est précisé pour le serveur discord).  
`!align order server` : affiche les alignés d'un ordre spécifique ou d'une cité spécifique.  
`!align > level server` : affiche les alignés ayant un niveau supérieur ou égal à celui précisé.  
`!align @user server` : affiche les alignements de l'utilisateur précisé.  
`!align city order level server` : enregistre l'alignement précisé en paramètre. Indiquer 0 pour le niveau supprime l'alignement.  
### Alliance
Affiche la page d'une alliance.
`!alliance nom` : donne la page d'alliance associée au nom. Celui-ci doit être exact.
`!alliance nom -serv server` : est à utiliser lorsque le nom ne suffit pas pour déterminer la fiche d'une alliance.

### Almanax
Donne des informations relatives à l'Almanax.  
`!almanax` : donne le bonus et l'offrande du jour actuel.  
`!almanax jj/mm/aaaa` : Donne le bonus et l'offrande du jour spécifié.  
`!almanax +days` : donne la liste des bonus et offrandes des jours à venir (jusqu'à 9 jours).  

### Almanax-auto
Poste l'almanax du jour à compter de minuit; nécessite le droit de gérer le serveur.  
`!almanax-auto true` : poste quotidiennement l'almanax du jour. Fonctionne aussi avec `on` et `0`.  
`!almanax-auto false` : ne poste plus l'almanax dans le salon. Fonctionne aussi avec `off` et `1`.  

### About
Informations relatives à Kaelly.  
`!about` : donne des informations sur KaellyTOUCH et un moyen d'obtenir de l'aide.

### Command
Autorise ou non l'utilisation d'une commande de Kaelly.  
`!cmd command true` : autorise l'utilisation de la commande. Fonctionne aussi avec `on` et `0`.  
`!cmd command false` : interdit l'utilisation de la commande. Fonctionne aussi avec `off` et `1`.  

### Donate  
`!donate` donne un lien Paypal pour soutenir l'équipe de développement !  

### Dist
Renvoie le transport le plus proche à vol d'oiseau.  
`!dist [POS, POS]` : renvoie le transport le plus proche de la position indiquée à vol d'oiseau.  

### Guild
Affiche la page d'une guilde.
`!guild name` : donne la page d'une guilde associée au nom. Celui-ci doit être exact.
`!guild name -serv server` : est à utiliser lorsque le nom ne suffit pas pour déterminer la fiche d'une guilde.

### Invite
`!invite` donne le lien d'invitation pour le bot et pour le serveur de support.  
### Item
Renvoie les statistiques d'un item du jeu Dofus.  
`!item item` : renvoie les statistiques de l'item spécifié : son nom peut être approximatif s'il est suffisamment précis.  
`!item -more item` : renvoie les statistiques détaillés de l'item spécifiée.  

### Job  
Gère un annuaire d'artisans.   
`!job server` : affiche vos métiers (serveur dofus optionnel sur la commande si celui-ci est précisé pour le serveur discord).  
`!job @user server` : affiche les métiers de l'utilisateur précisé.  
`!jobj ob1 job2 job3 server` : affiche les métiers précisés en paramètre.  
`!job >l evel job1 job2 job3 server` : affiche les métiers précisés en paramètre, avec un filtre supplémentaire sur le niveau.  
`!job job1, job2 job3 level server` : enregistre les métiers précisés en paramètre. Indiquer 0 pour le niveau supprime les métiers.  
`!job -all level server` : enregistre tous les métiers. Indiquer 0 pour le niveau supprime les métiers.  
`!job -list` : affiche tous les métiers disponibles avec l'orthographe reconnue par le bot.  

### Lang
Change la langue utilisée pour communiquer avec Kaelly parmi la liste suivante : FR, EN, SP.  
`!lang` : renseigne la langue du serveur et du salon.    
`!lang lang` : met à jour la langue du serveur.    
`!lang -channel lang` : met à jour la langue du salon (prioritaire au serveur).

### Monster
Renvoie les statistiques d'un monstre du jeu Dofus.  
`!monster monster` : renvoie les statistiques du monstre spécifié : son nom peut être approximatif s'il est suffisamment précis.
`!monster -more monster` : renvoie les statistiques détaillés du monstre spécifié.

### Ping
`!ping` mesure le temps de réponse.  

### Prefix
Change le préfixe utilisé pour invoquer une commande. Niveau modérateur minimum requis.  
`!prefix prefix` : change le préfixe par celui passé en paramètre. 3 maximum.  

### Random
Commande tirant des valeurs aléatoires.  
`!rdm` : tire une valeur entre Vrai et Faux.  
`!rdm number` : tire une valeur entre 0 et nombre.
`!rdm value1 value2 ...` : tire une valeur parmi celles spécifiées en paramètre.

### Resource
Renvoie les statistiques d'une ressource du jeu Dofus.  
`!resource resource` : renvoie les statistiques de la ressource spécifiée : son nom peut être approximatif s'il est suffisamment précis.    
`!resource -more resource` : renvoie les statistiques détaillés de la ressource spécifiée.

### RSS
Autorise ou non Kaelly à poster du contenu RSS dans un salon.  
`!rss true` : poste les news à partir du flux RSS de Dofus.com. Fonctionne aussi avec `on` et `0`.  
`!rss false` : ne poste plus les flux RSS dans le salon. Fonctionne aussi avec `off` et `1`.  

### Server
Permet de déterminer à quel serveur Dofus correspond ce serveur Discord.  
`!server` : affiche le serveur Dofus rattaché au serveur Discord.  
`!server -list` : affiche l'ensemble des serveurs Dofus.  
`!server server` : permet de déterminer à quel serveur Dofus correspond ce serveur Discord.  
`!server -reset` : permet de se détacher d'un quelconque serveur Dofus.  

### Set
Renvoie les statistiques d'une panoplie du jeu Dofus.  
`!set set` : renvoie les statistiques de la panoplie spécifiée : son nom peut être approximatif s'il est suffisamment précis.    
`!set -more set` : renvoie les statistiques détaillés de la panoplie spécifiée.  

### Sound
Joue des sons dans un salon vocal.  
`!sound` : joue un son au hasard, parmi une liste prédéfinie.  
`!sound sound` : joue le son spécifié.  
`!sound -leave` : quitte le salon vocal.

### Tuto
Renvoie le tutoriel correspondant à la recherche effectuée..  
`!tuto search` : renvoie le tutoriel correspondant à la recherche effectuée sur dofuspourlesnoobs : quête, donjon... Son nom peut être approximatif s'il est suffisamment précis.  

### Twitter
Autorise ou non Kaelly à poster des tweets dans un salon.  
`!twitter true` : poste les tweets de Dofusfr. Fonctionne aussi avec `on` et `0`.  
`!twitter false` : ne poste plus les tweets dans le salon. Fonctionne aussi avec `off` et `1`.  

### Whois
Affiche la page personnelle d'un joueur.  
`!whois pseudo` : donne la page personnelle associée au pseudo. Celui-ci doit être exact.
`!whois pseudo server` : est à utiliser lorsque le pseudo ne suffit pas pour déterminer la fiche d'un personnage.
`!whois -more pseudo server` : affiche les caractéristiques du personnage si elles sont disponibles.  


# Participer au projet (Développeur uniquement)

Section réservée aux développeurs. Si vous souhaitez ajouter Kaelly, il existe un lien d'invitation plus haut. Pour proposer des suggestions ou remonter des bugs, utiliser le serveur support. Le projet est ouvert au pulls request : https://github.com/Kaysoro/KaellyBot/pulls.


## Créer votre Bot

### Obtenir un token Discord

1. Connectez-vous sur le [site developers discord](https://discordapp.com/developers/applications/me).
2. Dans Application > MyApp : Cliquez sur New App.
3. Renseignez le nom du Bot, une brève description et une icône.
4. Une fois créé, Cliquez sur "Create a bot user".
5. Cochez la case "Public bot" si vous souhaitez qu'il soit accessible par n'importe qui.
6. Récupérez le client ID et le token, ils vous serviront à ajouter votre bot sur un serveur et à le mettre en route.

Ne diffusez pas votre token, il permettrait à n'importe qui de modifier votre bot.

### Obtenir un lien d'invitation

Vous avez créé votre bot sur le site de Discord et vous souhaitez l'inviter sur un serveur.
Récupérez le client ID de votre bot et placez-le à la place de CLIENT_ID dans l'url suivante :
https://discordapp.com/oauth2/authorize?&client_id=CLIENT_ID&scope=bot

### Connecter le bot à Discord

Maintenant que vous avez déclaré votre application sur la plateforme Discord, il suffit d'ajouter le token précédemment copié dans le fichier [config.properties](https://github.com/Kaysoro/KaellyBot/blob/DevTouch/config.properties), de telle sorte que : discord.token=TOKEN.

Avant de continuer, il faut ajouter la base de données *bdd.sqlite* à la racine du dossier; celle-ci est en libre téléchargement sur le serveur de support KaellyBOT, dans le salon \#trucs_utiles.

#### Créer le .jar pour démarrer le bot :

Installer *Maven* sur votre machine puis exécutez les commandes suivantes :
- `cd VotreProjet`
- `mvn clean compile`
- `mvn assembly:single`

#### Démarrer le .jar :

- En double-cliquant dessus (Ne prémunit pas d'éventuelles erreurs).
- En l'exécutant depuis la console : `java -jar jarfile` (permet aussi d'avoir les logs du bot).

#### Heberger le .jar :

Kaelly peut être lancée depuis n'importe quelle distribution : elle est compatible Windows/Linux/MacOS. Un serveur VPS est nécessaire pour son autonomie (sans quoi, il devra tourner sur votre machine personnelle); il pourra fonctionner indépendamment en utilisant des commandes telles que `screen` ou encore `tmux` sur Linux, par exemple.

## Licence
Ce projet est sous licence GPL(v3).

## Merci !
Le développement et l'accessibilité de Kaelly 24/7 génèrent des frais en continu. N'hésitez pas à participer à son maintien par le biais des dons !
[![Donate](https://www.paypalobjects.com/fr_FR/FR/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=89WTL4LXRZK98)
