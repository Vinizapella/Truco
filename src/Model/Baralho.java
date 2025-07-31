package Model;

import Util.Naipe;
import java.util.*;

public class Baralho {
    private List<Carta> cartas;
    private final String[] valores = {"4", "5", "6", "7", "Q", "J", "K", "A", "2", "3"};

    public Baralho() {
        criarBaralho();
        embaralhar();
    }

    private void criarBaralho() {
        cartas = new ArrayList<>();

        for (Naipe naipe : Naipe.values()) {
            for (String valor : valores) {
                cartas.add(new Carta(valor, naipe));
            }
        }
    }

    public void embaralhar() {
        Collections.shuffle(cartas);
    }

    public Carta distribuirCarta() {
        if (cartas.isEmpty()) {
            throw new RuntimeException("Baralho vazio! Não há mais cartas para distribuir.");
        }
        return cartas.remove(cartas.size() - 1);
    }

    public List<Carta> distribuirCartas(int quantidade) {
        List<Carta> cartasDistribuidas = new ArrayList<>();

        for (int i = 0; i < quantidade; i++) {
            cartasDistribuidas.add(distribuirCarta());
        }

        return cartasDistribuidas;
    }

    public int cartasRestantes() {
        return cartas.size();
    }

    public boolean isEmpty() {
        return cartas.isEmpty();
    }

    public void reiniciar() {
        criarBaralho();
        embaralhar();
    }
}
