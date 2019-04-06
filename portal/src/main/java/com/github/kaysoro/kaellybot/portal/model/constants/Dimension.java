package com.github.kaysoro.kaellybot.portal.model.constants;

public enum Dimension {

    ENUTROSOR("Enutrosor"), SRAMBAD("Srambad"), XELORIUM("Xélorium"), ECAFLIPUS("Ecaflipus");

    private String name;

    Dimension(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
