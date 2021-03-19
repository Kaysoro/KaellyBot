package payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Game {

    DOFUS("Dofus"            , "https://static.ankama.com/g/modules/masterpage/block/header/navbar/dofus/logo.png"      , "https://i.imgur.com/gJMIxvo.png"),
    DOFUS_TOUCH("Dofus Touch", "https://static.ankama.com/g/modules/masterpage/block/header/navbar/dofus-touch/logo.png", "https://i.imgur.com/D9ArwwF.png"),
    DOFUS_RETRO("Dofus Retro", "https://static.ankama.com/dofus/ng/modules/mmorpg/retro/new/assets/logo.png"            , "https://i.imgur.com/ipYjRAo.png");

    private final String name;

    private final String logo;

    private final String icon;
}