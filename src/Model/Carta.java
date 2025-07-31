package Model;

import Util.Naipe;
import Util.ValorCarta;

public class Carta {
    private final ValorCarta valor;
    private final Naipe naipe;

    public Carta(ValorCarta valor, Naipe naipe) {
        this.valor = valor;
        this.naipe = naipe;
    }


    public boolean ehMaisFortQue(Carta outraCarta) {
        return this.valor.ehMaisFortQue(outraCarta.valor);
    }

    public boolean mesmaForca(Carta outraCarta) {
        return this.valor.mesmaForca(outraCarta.valor);
    }

    public ValorCarta getValor() {
        return valor;
    }

    public Naipe getNaipe() {
        return naipe;
    }

    public int getForca() {
        return valor.getForca();
    }

    @Override
    public String toString() {
        return valor.getSimbolo() + naipe.getSimbolo();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Carta carta = (Carta) obj;
        return valor == carta.valor && naipe == carta.naipe;
    }

    @Override
    public int hashCode() {
        return valor.hashCode() + naipe.hashCode();
    }
}