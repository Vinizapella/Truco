package Util;

public enum ValorCarta {
    QUATRO("4", 1),
    CINCO("5", 2),
    SEIS("6", 3),
    SETE("7", 4),
    DAMA("Q", 5),
    VALETE("J", 6),
    REI("K", 7),
    AS("A", 8),
    DOIS("2", 9),
    TRES("3", 10);

    private final String simbolo;
    private final int forca;

    ValorCarta(String simbolo, int forca) {
        this.simbolo = simbolo;
        this.forca = forca;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public int getForca() {
        return forca;
    }

    public boolean ehMaisFortQue(ValorCarta outro) {
        return this.forca > outro.forca;
    }

    public boolean mesmaForca(ValorCarta outro) {
        return this.forca == outro.forca;
    }

    @Override
    public String toString() {
        return simbolo;
    }
}
