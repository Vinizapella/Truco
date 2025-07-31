package Model;

import java.util.*;

public class Jogador {
    private String nome;
    private List<Carta> cartas;
    private int pontos;

    public Jogador(String nome) {
        this.nome = nome;
        this.cartas = new ArrayList<>();
        this.pontos = 0;
    }

    public void receberCarta(Carta carta) {
        cartas.add(carta);
    }

    public void receberCartas(List<Carta> novasCartas) {
        cartas.addAll(novasCartas);
    }

    public Carta jogarCarta(int indice) {
        if (indice < 0 || indice >= cartas.size()) {
            throw new IndexOutOfBoundsException("Índice de carta inválido!");
        }
        return cartas.remove(indice);
    }

    public boolean temCartas() {
        return !cartas.isEmpty();
    }

    public int numeroDeCartas() {
        return cartas.size();
    }

    public void limparMao() {
        cartas.clear();
    }

    public void adicionarPontos(int pontosGanhos) {
        this.pontos += pontosGanhos;
    }

    public void zerarPontos() {
        this.pontos = 0;
    }

    public String getNome() {
        return nome;
    }

    public List<Carta> getCartas() {
        return new ArrayList<>(cartas); // Retorna uma cópia para segurança
    }

    public int getPontos() {
        return pontos;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome + " (" + pontos + " pontos) - " + cartas.size() + " cartas";
    }
}