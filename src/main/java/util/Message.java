package util;

import data.Constants;
import enums.Language;
import exceptions.MissingPermissionDiscordException;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by steve on 14/11/2016.
 */
public class Message {

    private static final MissingPermissionDiscordException missingPermission = new MissingPermissionDiscordException();

    public static void sendText(IChannel channel, String content){
        Language lg = Translator.getLanguageFrom(channel);
        RequestBuffer.request(() -> {
            try {
                new MessageBuilder(ClientConfig.DISCORD())
                        .withChannel(channel)
                        .withContent(content)
                        .build();
            } catch(RateLimitException e){
                LoggerFactory.getLogger(Message.class).warn(e.getMessage(),e );
                throw e;
            } catch (DiscordException e){
                Reporter.report(e, channel.isPrivate()? null : channel.getGuild(), channel);
                LoggerFactory.getLogger(Message.class).error(e.getMessage(), e);
            } catch(MissingPermissionsException e){
                LoggerFactory.getLogger(Message.class).warn(Constants.name
                        + " n'a pas les permissions pour appliquer cette requête.");
                missingPermission.throwException(channel, lg, e);
            } catch(Exception e){
                Reporter.report(e, channel.isPrivate()? null : channel.getGuild(), channel);
                LoggerFactory.getLogger(Message.class).error(e.getMessage(),e );
            }
        });
    }

    public static void sendImage(IChannel channel, BufferedImage image, String imageName) {
        Language lg = Translator.getLanguageFrom(channel);
        RequestBuffer.request(() -> {
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(image, "png", os);
                InputStream is = new ByteArrayInputStream(os.toByteArray());
                new MessageBuilder(ClientConfig.DISCORD())
                        .withChannel(channel)
                        .withFile(is, imageName)
                        .build();
            } catch (RateLimitException e) {
                LoggerFactory.getLogger(Message.class).warn(e.getMessage(), e);
                throw e;
            } catch (DiscordException e) {
                Reporter.report(e, channel.isPrivate() ? null : channel.getGuild(), channel);
                LoggerFactory.getLogger(Message.class).error(e.getMessage(), e);
            } catch (MissingPermissionsException e) {
                LoggerFactory.getLogger(Message.class).warn(Constants.name
                        + " n'a pas les permissions pour appliquer cette requête.");
                missingPermission.throwException(channel, lg, e);
            } catch (Exception e) {
                Reporter.report(e, channel.isPrivate() ? null : channel.getGuild(), channel);
                LoggerFactory.getLogger(Message.class).error(e.getMessage(), e);
            }
        });
    }

    public static void sendFile(IChannel channel, InputStream file, String fileName) {
        Language lg = Translator.getLanguageFrom(channel);
        RequestBuffer.request(() -> {
            try {
                new MessageBuilder(ClientConfig.DISCORD())
                        .withChannel(channel)
                        .withFile(file, fileName)
                        .build();
            } catch (RateLimitException e) {
                LoggerFactory.getLogger(Message.class).warn(e.getMessage(), e);
                throw e;
            } catch (DiscordException e) {
                Reporter.report(e, channel.isPrivate() ? null : channel.getGuild(), channel);
                LoggerFactory.getLogger(Message.class).error(e.getMessage(), e);
            } catch (MissingPermissionsException e) {
                LoggerFactory.getLogger(Message.class).warn(Constants.name
                        + " n'a pas les permissions pour appliquer cette requête.");
                missingPermission.throwException(channel, lg, e);
            } catch (Exception e) {
                Reporter.report(e, channel.isPrivate() ? null : channel.getGuild(), channel);
                LoggerFactory.getLogger(Message.class).error(e.getMessage(), e);
            }
        });
    }

    public static void sendEmbed(IChannel channel, EmbedObject content){
        Language lg = Translator.getLanguageFrom(channel);
        RequestBuffer.request(() -> {
            try {
                new MessageBuilder(ClientConfig.DISCORD())
                        .withChannel(channel)
                        .withEmbed(content)
                        .build();
            } catch(RateLimitException e){
                LoggerFactory.getLogger(Message.class).warn(e.getMessage(), e);
                throw e;
            } catch (DiscordException e){
                Reporter.report(e, channel.isPrivate()? null : channel.getGuild(), channel);
                LoggerFactory.getLogger(Message.class).error(e.getMessage(), e);
            } catch(MissingPermissionsException e){
                LoggerFactory.getLogger(Message.class).warn(Constants.name
                        + " n'a pas les permissions pour appliquer cette requête.");
                missingPermission.throwException(channel, lg, e);
            } catch(Exception e){
                Reporter.report(e, channel.isPrivate()? null : channel.getGuild(), channel);
                LoggerFactory.getLogger(Message.class).error(e.getMessage(), e);
            }
        });
    }
}