package commands;

import sx.blah.discord.handle.obj.IMessage;

import java.util.regex.Matcher;

/**
 * Created by steve on 14/07/2016.
 */
public interface Command {
    String getName();
    String getPattern();
    Matcher getMatcher(IMessage message);
    String getPrefix(IMessage message);
    boolean request(IMessage message);

    /**
     * Is the command usable in MP?
     * @return True if it can be used in MP, else false.
     */
    boolean isUsableInMP();

    /**
     * Is the command only usable by admins ?
     * @return True if it only can be used by admin, else false.
     */
    boolean isAdmin();

    /**
     * Is the command available by the admin ?
     * @return true is the command is available, else false.
     */
    boolean isPublic();

    /**
     * Change the command scope
     * @param isPublic is command available or not
     */
    void setPublic(boolean isPublic);

    /**
     * Change the command scope in MP
     * @param isUsableInMP is command available in MP or not
     */
    void setUsableInMP(boolean isUsableInMP);

    /**
     * Change the command scope for admin user
     * @param isAdmin is command only available for admins or not
     */
    void setAdmin(boolean isAdmin);

    /**
     * @param prefixe Prefixe for command
     * @return Short description of the command
     */
    String help(String prefixe);

    /**
     * @param prefixe Prefixe for command
     * @return Detailed description of the command
     */
    String helpDetailed(String prefixe);
}
