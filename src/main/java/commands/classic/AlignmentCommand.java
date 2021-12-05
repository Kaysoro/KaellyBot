package commands.classic;

import commands.model.FetchCommand;
import data.Guild;
import data.OrderUser;
import data.ServerDofus;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import enums.City;
import enums.Language;
import enums.Order;
import exceptions.*;
import util.ServerUtils;
import util.Translator;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by steve on 08/02/2018
 */
public class AlignmentCommand extends FetchCommand {

    private DiscordException notFoundFilter;
    private DiscordException tooMuchFilters;
    private DiscordException tooMuchCities;
    private DiscordException notFoundCity;
    private DiscordException tooMuchOrders;
    private DiscordException notFoundOrder;

    public AlignmentCommand(){
        super("align", "(.*)");
        setUsableInMP(false);
        notFoundFilter = new NotFoundDiscordException("filter");
        tooMuchFilters = new TooMuchDiscordException("filter");
        tooMuchCities = new TooMuchDiscordException("city", true);
        notFoundCity = new NotFoundDiscordException("city");
        tooMuchOrders = new TooMuchDiscordException("order", true);
        notFoundOrder = new NotFoundDiscordException("order");
    }

    @Override
    public void request(MessageCreateEvent event, Message message, Matcher m, Language lg) {
        String content = m.group(1).trim();
        Optional<discord4j.core.object.entity.Guild> guild = message.getGuild().blockOptional();
        Optional<Member> user = message.getAuthorAsMember().blockOptional();

        // Initialisation du Filtre
        City city = null;
        Order order = null;

        if (guild.isPresent() && user.isPresent()){
            ServerDofus server = ServerUtils.getDofusServerFrom(Guild.getGuild(guild.get()), message.getChannel().block());

            // Consultation filtré par niveau
            if ((m = Pattern.compile(">\\s+(\\d{1,3})(\\s+.+)?").matcher(content)).matches()){
                int level = Integer.parseInt(m.group(1));
                if (m.group(2) != null) {
                    ServerUtils.ServerQuery serverQuery = ServerUtils.getServerDofusFromName(m.group(2), lg);
                    if (serverQuery.hasSucceed())
                        server = serverQuery.getServer();
                    else {
                        serverQuery.getExceptions()
                                .forEach(e -> e.throwException(message, this, lg, serverQuery.getServersFound()));
                        return;
                    }
                } else if (server == null){
                    notFoundServer.throwException(message, this, lg);
                    return;
                }

                List<EmbedCreateSpec> embeds = OrderUser.getOrdersFromLevel(guild.get().getMembers()
                        .collectList().blockOptional().orElse(Collections.emptyList()), server, level, guild.get(), lg);
                for (EmbedCreateSpec embed : embeds)
                    message.getChannel().flatMap(chan -> chan.createEmbed(embed)).subscribe();
            }
            else {
                // L'utilisateur concerné est-il l'auteur de la commande ?
                if(Pattern.compile("^<@[!|&]?\\d+>").matcher(content).find()){
                    content = content.replaceFirst("<@[!|&]?\\d+>", "").trim();
                    Optional<Snowflake> memberId = message.getUserMentionIds().stream().findFirst();
                    if (! memberId.isPresent()){
                        BasicDiscordException.USER_NEEDED.throwException(message, this, lg);
                        return;
                    }
                    user = guild.get().getMemberById(memberId.get()).blockOptional();
                }

                //Consultation des données filtrés par utilisateur
                ServerUtils.ServerQuery serverQuery = ServerUtils.getServerDofusFromName(content, lg);
                if (! serverQuery.getServersFound().isEmpty() && Pattern.compile("(.+)").matcher(content).matches()
                        || content.isEmpty()){
                    if (serverQuery.hasSucceed())
                        server = serverQuery.getServer();
                    else if (server == null) {
                        if (!content.isEmpty())
                            serverQuery.getExceptions()
                                    .forEach(e -> e.throwException(message, this, lg, serverQuery.getServersFound()));
                        else
                            notFoundServer.throwException(message, this, lg);
                        return;
                    }

                    if (user.isPresent()) {
                        List<EmbedCreateSpec> embeds = OrderUser.getOrdersFromUser(user.get(), server, lg);
                        for (EmbedCreateSpec embed : embeds)
                            message.getChannel().flatMap(chan -> chan.createEmbed(embed)).subscribe();
                    }
                }
                // Enregistrement des données
                else if((m = Pattern.compile("(\\p{L}+)\\s+(\\p{L}+)\\s+(\\d{1,3})(\\s+.+)?").matcher(content)).matches()){
                    if(user.isPresent() && message.getAuthor().isPresent() && user.get().getId().equals(message.getAuthor().get().getId())) {
                        // Parsing des données et traitement des divers exceptions
                        List<City> cities = findCity(m.group(1), lg);
                        if (checkData(cities, tooMuchCities, notFoundCity, message, lg)) return;
                        city = cities.get(0);
                        List<Order> orders = findOrder(m.group(2), lg);
                        if (checkData(orders, tooMuchOrders, notFoundOrder, message, lg)) return;
                        order = orders.get(0);
                        int level = Integer.parseInt(m.group(3));

                        if (m.group(4) != null) {
                            ServerUtils.ServerQuery query = ServerUtils.getServerDofusFromName(m.group(4), lg);
                            if (query.hasSucceed())
                                server = query.getServer();
                            else {
                                query.getExceptions()
                                        .forEach(e -> e.throwException(message, this, lg, query.getServersFound()));
                                return;
                            }
                        } else if (server == null){
                            notFoundServer.throwException(message, this, lg);
                            return;
                        }
                        if(OrderUser.containsKeys(user.get().getId().asLong(), server, city, order)) {
                            OrderUser.get(user.get().getId().asLong(), server, city, order).get(0).setLevel(level);
                            if (level != 0)
                                message.getChannel().flatMap(chan -> chan
                                        .createMessage(Translator.getLabel(lg, "align.update"))).subscribe();
                            else
                                message.getChannel().flatMap(chan -> chan
                                    .createMessage(Translator.getLabel(lg, "align.reset"))).subscribe();
                        }
                        else {
                            new OrderUser(user.get().getId().asLong(), server, city, order, level).addToDatabase();
                            if (level != 0)
                                message.getChannel().flatMap(chan -> chan
                                        .createMessage(Translator.getLabel(lg, "align.save"))).subscribe();
                            else
                                message.getChannel().flatMap(chan -> chan
                                        .createMessage(Translator.getLabel(lg, "align.no_reset"))).subscribe();
                        }
                    }
                    else
                        badUse.throwException(message, this, lg);

                }
                // Consultation filtré par cité et/ou par ordre
                else if((m = Pattern.compile("(\\p{L}+)(\\s+\\p{L}+)?(\\s+[\\p{L}|\\W]+)?").matcher(content)).matches()){
                    if (m.group(3) != null) {
                        ServerUtils.ServerQuery query = ServerUtils.getServerDofusFromName(m.group(3), lg);
                        if (query.hasSucceed())
                            server = query.getServer();
                        else {
                            query.getExceptions()
                                    .forEach(e -> e.throwException(message, this, lg, query.getServersFound()));
                            return;
                        }
                    }

                    // On a précisé à la fois une cité et un ordre
                    if (m.group(2) != null) {
                        boolean is2Server = false;
                        if (m.group(3) == null){
                            ServerUtils.ServerQuery query = ServerUtils.getServerDofusFromName(m.group(2), lg);
                            if (query.hasSucceed()) {
                                server = query.getServer();
                                is2Server = true;
                            }
                            else {
                                query.getExceptions()
                                        .forEach(e -> e.throwException(message, this, lg, query.getServersFound()));
                                return;
                            }
                        }

                        if (is2Server){
                            // Est-ce un ordre ? une cité ?
                            String value = m.group(1).trim();
                            List<City> cities = findCity(value, lg);
                            List<Order> orders = findOrder(value, lg);
                            if (cities.isEmpty() && orders.isEmpty()){
                                notFoundFilter.throwException(message, this, lg);
                                return;
                            }
                            if (cities.size() > 1 || orders.size() > 1){
                                tooMuchFilters.throwException(message, this, lg);
                                return;
                            }
                            if (cities.size() == 1) city = cities.get(0);
                            if (orders.size() == 1) order = orders.get(0);
                        }
                        else {
                            List<City> cities = findCity(m.group(1).trim(), lg);
                            if (checkData(cities, tooMuchCities, notFoundCity, message, lg)) return;
                            city = cities.get(0);
                            List<Order> orders = findOrder(m.group(2).trim(), lg);
                            if (checkData(orders, tooMuchOrders, notFoundOrder, message, lg)) return;
                            order = orders.get(0);
                        }
                    }
                    else {
                        // Is an order ? a city ?
                        List<City> cities = findCity(m.group(1).trim(), lg);
                        List<Order> orders = findOrder(m.group(1).trim(), lg);
                        if (cities.isEmpty() && orders.isEmpty()){
                            notFoundFilter.throwException(message, this, lg);
                            return;
                        }
                        if (cities.size() > 1 || orders.size() > 1){
                            tooMuchFilters.throwException(message, this, lg);
                            return;
                        }
                        if (cities.size() == 1) city = cities.get(0);
                        if (orders.size() == 1) order = orders.get(0);
                    }

                    if (server == null){
                        notFoundServer.throwException(message, this, lg);
                        return;
                    }

                    List<EmbedCreateSpec> embeds = OrderUser.getOrdersFromCityOrOrder(guild.get().getMembers()
                                    .collectList().blockOptional().orElse(Collections.emptyList()),
                            server, city, order, guild.get(), lg);
                    for (EmbedCreateSpec embed : embeds)
                        message.getChannel().flatMap(chan -> chan.createEmbed(embed)).subscribe();
                }
                else
                    badUse.throwException(message, this, lg);
            }
        }
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

    @Override
    public String help(Language lg, String prefixe) {
        return "**" + prefixe + name + "** " + Translator.getLabel(lg, "align.help");
    }

    @Override
    public String helpDetailed(Language lg, String prefixe) {
        return help(lg, prefixe)
                + "\n`" + prefixe + name + " `*`server`* : " + Translator.getLabel(lg, "align.help.detailed.1")
                + "\n`" + prefixe + name + " `*`order server`* : " + Translator.getLabel(lg, "align.help.detailed.2")
                + "\n`" + prefixe + name + " `*`> level server`* : " + Translator.getLabel(lg, "align.help.detailed.3")
                + "\n`" + prefixe + name + " `*`@user server`* : " + Translator.getLabel(lg, "align.help.detailed.4")
                + "\n`" + prefixe + name + " `*`city order level server`* : " + Translator.getLabel(lg, "align.help.detailed.5") + "\n";
    }
}