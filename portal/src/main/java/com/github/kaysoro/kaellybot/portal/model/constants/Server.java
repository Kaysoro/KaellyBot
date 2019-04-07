package com.github.kaysoro.kaellybot.portal.model.constants;

public enum Server {

    AGRIDE("Agride"),
    ATCHAM("Atcham"),
    CROCABULIA("Crocabulia"),
    ECHO("Echo"),
    MERIANA("Meriana"),
    OMBRE("Ombre"),
    OTO_MUSTAM("Oto-Mustam"),
    RUBILAX("Rubilax"),
    PANDORE("Pandore"),
    USH("Ush"),
    JULITH("Julith"),
    NIDAS("Nidas"),
    MERKATOR("Merkator"),
    FURYE("Furye"),
    BRUMEN("Brumen"),
    ILYZAELLE("Ilyzaelle");

    private String name;

    Server(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
