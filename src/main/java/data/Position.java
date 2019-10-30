package data;

import discord4j.core.spec.EmbedCreateSpec;
import enums.Language;
import enums.Transport;
import org.apache.commons.lang3.time.DateUtils;
import util.*;

import java.util.Calendar;
import java.util.Date;

public class Position implements Embedded {

    private final static long LIMIT = 172800000;
    
    private Portal portal;
    private ServerDofus server;
    private Coordinate coordinate;
    private int utilisation;
    private long creation;
    private long lastUpdate;
    private String creationSource;
    private String updateSource;
    private Transport zaap;
    private Transport transportLimited;

    public Position(Portal portal, ServerDofus server, Coordinate coordinate, int utilisation, long creation,
                    long lastUpdate, String creationSource, String updateSource,
                    Transport zaap, Transport transportLimited) {
        this.portal = portal;
        this.server = server;
        this.coordinate = coordinate;
        this.utilisation = utilisation;
        this.creation = creation;
        this.lastUpdate = lastUpdate;
        this.creationSource = creationSource;
        this.updateSource = updateSource;
        this.zaap = zaap;
        this.transportLimited = transportLimited;
    }

    public boolean isValid(){
        return System.currentTimeMillis() - creation <= LIMIT
                && !coordinate.isNull();
    }

    @Override
    public void decorateEmbedObject(EmbedCreateSpec spec, Language lg) {
            spec.setTitle(portal.getName())
            .setColor(portal.getColor())
            .setThumbnail(portal.getUrl());

        if (! isValid()) {
            coordinate = new Coordinate();
            utilisation = -1;
            spec.setDescription(Translator.getLabel(lg, "portal.unknown"));
        } else {
            spec.addField(Translator.getLabel(lg, "portal.position"), "**" + coordinate + "**", true);
            if (utilisation != -1)
                spec.addField(Translator.getLabel(lg, "portal.utilisation.title"), utilisation
                        + " " + Translator.getLabel(lg, "portal.utilisation.desc")
                        + (utilisation > 1 ? "s" : ""), true);
            if (transportLimited != null)
                spec.addField(Translator.getLabel(lg, "portal.private_zaap"), transportLimited.toDiscordString(lg), false);
            spec.addField(Translator.getLabel(lg, "portal.zaap"), zaap.toDiscordString(lg), false)
                .setFooter(getDateInformation(creationSource, updateSource, lg), null);
        }
    }

    @Override
    public void decorateMoreEmbedObject(EmbedCreateSpec spec, Language lg) {
        decorateEmbedObject(spec, lg);
    }
    
    private String getDateInformation(String creationSource, String updateSource, Language lg){
        StringBuilder st = new StringBuilder(Translator.getLabel(lg, "portal.date.added")).append(" ");

        long deltaCreation = System.currentTimeMillis() - creation;
        st.append(getLabelTimeAgo(lg, deltaCreation)).append(" ").append(Translator.getLabel(lg, "portal.date.by"))
                .append(" ").append(creationSource);

        if (lastUpdate != -1 && ! DateUtils.truncatedEquals(new Date(creation), new Date(lastUpdate), Calendar.SECOND)){
            long deltaUpdate = System.currentTimeMillis() - lastUpdate;
            st.append(" - ").append(Translator.getLabel(lg, "portal.date.edited")).append(" ")
                    .append(getLabelTimeAgo(lg, deltaUpdate)).append(" ").append(Translator.getLabel(lg, "portal.date.by"))
                    .append(" ").append(updateSource);
        }
        return st.toString();
    }

    private String getLabelTimeAgo(Language lg, long time){
        if (time < DateUtils.MILLIS_PER_MINUTE)
            return Translator.getLabel(lg, "portal.date.now");
        else if (time < DateUtils.MILLIS_PER_HOUR)
            return Translator.getLabel(lg, "portal.date.minutes_ago")
                    .replace("{time}", String.valueOf(time / DateUtils.MILLIS_PER_MINUTE));
        else
            return Translator.getLabel(lg, "portal.date.hours_ago")
                    .replace("{time}", String.valueOf(time / DateUtils.MILLIS_PER_HOUR));
    }

    public Portal getPortal(){
        return portal;
    }

    public ServerDofus getServer() {
        return server;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getUtilisation() {
        return utilisation;
    }

    public long getCreation() {
        return creation;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public String getCreationSource() {
        return creationSource;
    }

    public String getUpdateSource() {
        return updateSource;
    }

    public Transport getZaap() {
        return zaap;
    }

    public Transport getTransportLimited() {
        return transportLimited;
    }
}
