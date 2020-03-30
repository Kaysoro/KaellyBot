package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import util.Translator;

import java.awt.*;
import java.text.Normalizer;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Dimension {

    ENUTROSOR("dimension.enutrosor", "https://i.imgur.com/ssMAcx3.png", new Color(255, 255, 104)),
    SRAMBAD  ("dimension.srambad"  , "https://i.imgur.com/jzpizTm.png", new Color(47, 66, 104)),
    XELORIUM ("dimension.xelorium" , "https://i.imgur.com/vfQhS5D.png", new Color(110, 81, 105)),
    ECAFLIPUS("dimension.ecaflipus", "https://i.imgur.com/sLK4FmQ.png", new Color(205, 216, 158));

    private String key;

    private String image;

    private Color color;

    public String getLabel(Language lang){
        return Translator.getLabel(lang, getKey());
    }

    public static Dimension valueOfCaseUnsensitive(String dimensionName, Language lang){
        final String DIMENSION_NAME = Normalizer.normalize(dimensionName, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase()
                .replaceAll("\\W+", "");
        return Stream.of(Dimension.values())
                .filter(dim -> Normalizer.normalize(dim.getLabel(lang), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase()
                        .replaceAll("\\W+", "").startsWith(DIMENSION_NAME))
                .findFirst().orElseThrow(NullPointerException::new);
    }
}