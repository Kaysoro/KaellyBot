package commands.model;

import discord4j.core.object.entity.Message;
import enums.Language;
import exceptions.DiscordException;
import exceptions.NotFoundDiscordException;
import exceptions.TooMuchDiscordException;
import java.util.List;

public abstract class FetchCommand extends AbstractLegacyCommand {

    protected DiscordException tooMuchServers;
    protected DiscordException notFoundServer;

    protected FetchCommand(String name, String pattern) {
        super(name, pattern);
        tooMuchServers = new TooMuchDiscordException("server", true);
        notFoundServer = new NotFoundDiscordException("server");
    }

    /**
     * Renvoie True si la liste ne contient pas un seul élément et jette une DiscordException, sinon renvoie False.
     * @param list List d'ojbets à vérifier
     * @param tooMuch Exception à jeter si la liste a plus d'un objet
     * @param notFound Exception à jeter si la liste est vide
     * @param message Message d'origine provoquant l'appel de la commande
     * @param lg Langue utilisée par la guilde
     * @param <T> Type de la liste passé en paramètre; non utilisé
     * @return True si la liste contient un seul élément; jette une DiscordException et renvoie faux le cas échéant
     */
    protected <T> boolean checkData(List<T> list, DiscordException tooMuch, DiscordException notFound, Message message, Language lg){
        if (list.size() > 1){
            tooMuch.throwException(message, this, lg);
            return true;
        }
        else if(list.isEmpty()){
            notFound.throwException(message, this, lg);
            return true;
        }
        return false;
    }
}
