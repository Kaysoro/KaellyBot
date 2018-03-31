package commands.classic;

import commands.model.AbstractCommand;
import data.Guild;
import data.OrderUser;
import data.ServerDofus;
import enums.City;
import enums.Language;
import enums.Order;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import util.Message;
import util.Quadruple;
import util.Translator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 08/02/2018
 */
public class AlignmentCommand extends AbstractCommand {

    private final static Logger LOG = LoggerFactory.getLogger(AlignmentCommand.class);
    private DiscordException notFoundFilter;
    private DiscordException tooMuchCities;
    private DiscordException notFoundCity;
    private DiscordException tooMuchOrders;
    private DiscordException notFoundOrder;
    private DiscordException tooMuchServers;
    private DiscordException notFoundServer;
    private DiscordException notFoundGuildServer;
    private DiscordException badUse;
    private DiscordException noEnoughRights;

    public AlignmentCommand(){
        super("align", "(.*)");
        setUsableInMP(false);
        notFoundFilter = new NotFoundDiscordException("filter");
        tooMuchCities = new TooMuchDiscordException("city", true);
        notFoundCity = new NotFoundDiscordException("city");
        tooMuchOrders = new TooMuchDiscordException("order", true);
        notFoundOrder = new NotFoundDiscordException("order");
        tooMuchServers = new TooMuchDiscordException("server", true);
        notFoundServer = new NotFoundDiscordException("server");
        notFoundGuildServer = new NotFoundDiscordException("guild_server");
        noEnoughRights = new BasicDiscordException("exception.basic.no_enough_rights");
        badUse = new BadUseCommandDiscordException();
    }

    @Override
    public boolean request(IMessage message) {
        if (super.request(message)) {
            Language lg = Translator.getLanguageFrom(message.getChannel());
            Matcher m = getMatcher(message);
            m.matches();
            String content = m.group(1).trim();

            // Initialisation du Filtre
            City city;
            Order order;
            IUser user = message.getAuthor();
            ServerDofus server = Guild.getGuild(message.getGuild()).getServerDofus();
            List<ServerDofus> servers;

            // Consultation filtré par niveau
            if ((m = Pattern.compile(">\\s+(\\d{1,3})(\\s+.+)?").matcher(content)).matches()){
                Message.sendText(message.getChannel(), "Consultation filtré par niveau");
                //TODO
            }
            else {
                // L'utilisateur concerné est-il l'auteur de la commande ?
                if(Pattern.compile("^<@[!|&]?\\d+>").matcher(content).find()){
                    content = content.replaceFirst("<@[!|&]?\\d+>", "").trim();
                    user = message.getMentions().get(0);
                }

                //Consultation des données filtrés par utilisateur
                if (!(servers = findServer(content)).isEmpty() && (m = Pattern.compile("(.+)").matcher(content)).matches()
                        || content.isEmpty()){
                    Message.sendText(message.getChannel(), "Consultation des données utilisateur");
                    //TODO
                }
                // Enregistrement des données
                else if((m = Pattern.compile("(\\p{L}+)\\s+(\\p{L}+)\\s+(\\d{1,3})(\\s+.+)?").matcher(content)).matches()){
                    if(user == message.getAuthor() || user != message.getAuthor() && isUserHasEnoughRights(message)) {
                        // Parsing des données et traitement des divers exceptions
                        List<City> cities = findCity(m.group(1), lg);
                        if (! checkData(cities, tooMuchCities, notFoundCity, message, lg)) return false;
                        city = cities.get(0);
                        List<Order> orders = findOrder(m.group(2), lg);
                        if (! checkData(orders, tooMuchOrders, notFoundOrder, message, lg)) return false;
                        order = orders.get(0);
                        int level = Integer.parseInt(m.group(3));

                        if (m.group(4) != null) {
                            servers = findServer(m.group(4));
                            if (!checkData(servers, tooMuchServers, notFoundServer, message, lg)) return false;
                            server = servers.get(0);
                        } else if (server == null){
                            notFoundGuildServer.throwException(message, this, lg);
                            return false;
                        }
                        if(OrderUser.getOrders().containsKey(Quadruple.of(user.getLongID(), server, city, order)))
                            OrderUser.getOrders().get(Quadruple.of(user.getLongID(), server, city, order)).setLevel(level);
                        else
                            new OrderUser(user.getLongID(), server, city, order, level).addToDatabase();

                        if (level != 0)
                            Message.sendText(message.getChannel(), "align.save");
                        else
                            Message.sendText(message.getChannel(), "align.reset");
                    }
                    else
                        noEnoughRights.throwException(message, this, lg);

                }
                // Consultation filtré par cité et/ou par ordre
                else if((m = Pattern.compile("(\\p{L}+)(\\s+\\p{L}+)?(\\s+[\\p{L}|\\W]+)?").matcher(content)).matches()){
                    Message.sendText(message.getChannel(), "Consultation filtré par cité et/ou par ordre");
                    //TODO
                }
                else
                    badUse.throwException(message, this, lg);
            }
        }
        return false;
    }

    /**
     * Renvoie True si la liste contient un seul élément, sinon jette une DiscordException puis renvoie False.
     * @param list List d'ojbets à vérifier
     * @param tooMuch Exception à jeter si la liste a plus d'un objet
     * @param notFound Exception à jeter si la liste est vide
     * @param message Message d'origine provoquant l'appel de la commande
     * @param lg Langue utilisée par la guilde
     * @param <T> Type de la liste passé en paramètre; non utilisé
     * @return True si la liste contient un seul élément; jette une DiscordException et renvoie faux le cas échéant
     */
    private <T> boolean checkData(List<T> list, DiscordException tooMuch, DiscordException notFound, IMessage message, Language lg){
        if (list.size() > 1){
            tooMuch.throwException(message, this, lg);
            return false;
        }
        else if(list.isEmpty()){
            notFound.throwException(message, this, lg);
            return false;
        }
        return true;
    }

    /**
     * Retourne une liste de cités dont le nom commence par celui passé en paramètre; les caractèrs spéciaux et la
     * casse sont ignorés.
     * @param value Nom partielle ou complet d'une cité d'alignement
     * @param lg Langue utilisée par la guilde
     * @return Liste de cités corrspondant à value
     */
    private List<City> findCity(String value, Language lg){
        List<City> result = new ArrayList<>();
        value = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("\\W+", "").toLowerCase().trim();

        if (! value.isEmpty())
            for(City city : City.values())
                if (Normalizer.normalize(city.getLabel(lg), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .replaceAll("\\W+", "").toLowerCase().trim().startsWith(value))
                    result.add(city);
        return result;
    }

    /**
     * Retourne une liste d'ordres dont le nom commence par celui passé en paramètre; les caractèrs spéciaux et la
     * casse sont ignorés.
     * @param value Nom partielle ou complet d'un ordre d'alignement
     * @param lg Langue utilisée par la guilde
     * @return Liste d'ordres corrspondant à value
     */
    private List<Order> findOrder(String value, Language lg){
        List<Order> result = new ArrayList<>();
        value = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("\\W+", "").toLowerCase().trim();
        if (!value.isEmpty())
            for(Order order : Order.values())
                if (Normalizer.normalize(order.getLabel(lg), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .replaceAll("\\W+", "").toLowerCase().trim().startsWith(value))
                    result.add(order);
        return result;
    }

    /**
     * Retourne une liste de serveurs dont le nom commence par celui passé en paramètre; les caractèrs spéciaux et la
     * casse sont ignorés.
     * @param value Nom partielle ou complet d'un serveur dofus
     * @return Liste de serveurs corrspondant à value
     */
    private List<ServerDofus> findServer(String value){
        List<ServerDofus> result = new ArrayList<>();
        value = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("\\W+", "").toLowerCase().trim();
        if (! value.isEmpty())
            for(ServerDofus serverDofus : ServerDofus.getServersDofus())
                if (Normalizer.normalize(serverDofus.getName(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                        .replaceAll("\\W+", "").toLowerCase().trim().startsWith(value))
                    result.add(serverDofus);
        return result;
    }

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "align.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n" + prefixe + "`"  + name + " `*`server`* : " + Translator.getLabel(lg, "align.help.detailed.1")
                + "\n" + prefixe + "`"  + name + " `*`order server`* : " + Translator.getLabel(lg, "align.help.detailed.2")
                + "\n" + prefixe + "`"  + name + " `*`> level server`* : " + Translator.getLabel(lg, "align.help.detailed.3")
                + "\n" + prefixe + "`"  + name + " `*`@user server`* : " + Translator.getLabel(lg, "align.help.detailed.4")
                + "\n" + prefixe + "`"  + name + " `*`city order level server`* : " + Translator.getLabel(lg, "align.help.detailed.5")
                + "\n" + prefixe + "`"  + name + " `*`@user city order level server`* : " + Translator.getLabel(lg, "align.help.detailed.6")+ "\n";
    }
}
