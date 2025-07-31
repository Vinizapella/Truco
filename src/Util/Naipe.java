package Util;

public enum Naipe {
    COPAS("♥", "Copas"),
    ESPADAS("♠", "Espadas"),
    OUROS("♦", "Ouros"),
    PAUS("♣", "Paus");

    private final String simbolo;
    private final String nome;

    Naipe(String simbolo, String nome) {
        this.simbolo = simbolo;
        this.nome = nome;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return simbolo;
    }
}
