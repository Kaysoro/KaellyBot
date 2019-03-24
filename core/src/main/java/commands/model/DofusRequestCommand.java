package commands.model;

import enums.Language;
import exceptions.DiscordException;
import exceptions.ExceptionManager;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sx.blah.discord.handle.obj.IMessage;
import util.JSoupManager;
import util.Requestable;
import util.Translator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class DofusRequestCommand extends AbstractCommand {

    protected DofusRequestCommand(String name, String pattern) {
        super(name, pattern);
    }

    protected List<Requestable> getListRequestableFrom(String url, IMessage message, DiscordException notFound){
        List<Requestable> result = new ArrayList<>();
        Language lg = Translator.getLanguageFrom(message.getChannel());
        try {
            Document doc = JSoupManager.getDocument(url);
            Elements elems = doc.getElementsByClass("ak-bg-odd");
            elems.addAll(doc.getElementsByClass("ak-bg-even"));

            for (Element element : elems)
                result.add(new Requestable(element.child(1).text(),
                        element.child(1).select("a").attr("href")));

        } catch(IOException e){
            ExceptionManager.manageIOException(e, message, this, lg, notFound);
            return new ArrayList<>();
        }  catch (Exception e) {
            ExceptionManager.manageException(e, message, this, lg);
            return new ArrayList<>();
        }

        return result;
    }
}
