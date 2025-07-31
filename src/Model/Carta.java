package Model;

import Util.Naipe;

public class Carta {
    private final String valor;
    private final Naipe naipe;
    private final int forca; // Força da carta na hierarquia do truco

    public Carta(String valor, Naipe naipe) {
        this.valor = valor;
        this.naipe = naipe;
        this.forca = calcularForca(valor);
    }

    private int calcularForca(String valor) {
        switch (valor) {
            case "4": return 1;
            case "5": return 2;
            case "6": return 3;
            case "7": return 4;
            case "Q": return 5;
            case "J": return 6;
            case "K": return 7;
            case "A": return 8;
            case "2": return 9;
            case "3": return 10;
            default: return 0;
        }
    }

    public boolean ehMaisFortQue(Carta outraCarta) {
        return this.forca > outraCarta.forca;
    }

    public boolean mesmaForca(Carta outraCarta) {
        return this.forca == outraCarta.forca;
    }

    public String getValor() {
        return valor;
    }

    public Naipe getNaipe() {
        return naipe;
    }

    public int getForca() {
        return forca;
    }

    @Override
    public String toString() {
        return valor + naipe.getSimbolo();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Carta carta = (Carta) obj;
        return valor.equals(carta.valor) && naipe == carta.naipe;
    }

    @Override
    public int hashCode() {
        return valor.hashCode() + naipe.hashCode();
    }
}