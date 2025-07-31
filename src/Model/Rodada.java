package Model;

import java.util.*;

public class Rodada {
    private List<Carta> cartasJogadas;
    private List<Jogador> ordemJogadores;
    private Jogador vencedor;
    private int numeroRodada;
    private boolean finalizada;

    public Rodada(int numeroRodada) {
        this.numeroRodada = numeroRodada;
        this.cartasJogadas = new ArrayList<>();
        this.ordemJogadores = new ArrayList<>();
        this.vencedor = null;
        this.finalizada = false;
    }

    public void adicionarJogada(Jogador jogador, Carta carta) {
        if (finalizada) {
            throw new IllegalStateException("Rodada já foi finalizada!");
        }

        if (jogador == null || carta == null) {
            throw new IllegalArgumentException("Jogador e carta não podem ser nulos!");
        }

        cartasJogadas.add(carta);
        ordemJogadores.add(jogador);
    }

    public Jogador avaliarVencedor() {
        if (cartasJogadas.isEmpty()) {
            throw new IllegalStateException("Não há cartas jogadas para avaliar!");
        }

        Carta cartaMaisForte = cartasJogadas.get(0);
        int indiceVencedor = 0;

        for (int i = 1; i < cartasJogadas.size(); i++) {
            if (cartasJogadas.get(i).ehMaisFortQue(cartaMaisForte)) {
                cartaMaisForte = cartasJogadas.get(i);
                indiceVencedor = i;
            }
        }

        vencedor = ordemJogadores.get(indiceVencedor);
        finalizada = true;

        return vencedor;
    }

    public boolean isCompleta(int totalJogadores) {
        return cartasJogadas.size() == totalJogadores;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void reset() {
        cartasJogadas.clear();
        ordemJogadores.clear();
        vencedor = null;
        finalizada = false;
    }

    public Carta getCartaJogador(Jogador jogador) {
        int indice = ordemJogadores.indexOf(jogador);
        return indice >= 0 ? cartasJogadas.get(indice) : null;
    }

    public List<Carta> getCartasJogadas() {
        return new ArrayList<>(cartasJogadas);
    }

    public List<Jogador> getOrdemJogadores() {
        return new ArrayList<>(ordemJogadores);
    }

    public Jogador getVencedor() {
        return vencedor;
    }

    public int getNumeroRodada() {
        return numeroRodada;
    }

    public int getTotalJogadas() {
        return cartasJogadas.size();
    }

    @Override
    public String toString() {
        return String.format("Rodada %d - %d jogadas - %s",
                numeroRodada,
                cartasJogadas.size(),
                finalizada ? "Vencedor: " + vencedor.getNome() : "Em andamento");
    }
}