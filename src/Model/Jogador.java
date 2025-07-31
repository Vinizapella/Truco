package Model;

import java.util.*;


public class Jogador {
    private String nome;
    private List<Carta> cartas;
    private int pontos;

    public Jogador(String nome) {
        setNome(nome);
        this.cartas = new ArrayList<>();
        this.pontos = 0;
    }

    public void receberCarta(Carta carta) {
        if (carta == null) {
            throw new IllegalArgumentException("Carta não pode ser nula!");
        }
        cartas.add(carta);
    }

    public void receberCartas(List<Carta> novasCartas) {
        if (novasCartas == null) {
            throw new IllegalArgumentException("Lista de cartas não pode ser nula!");
        }

        for (Carta carta : novasCartas) {
            receberCarta(carta);
        }
    }

    public Carta jogarCarta(int indice) {
        validarIndiceCartas(indice);

        if (!temCartas()) {
            throw new IllegalStateException("Jogador não tem cartas para jogar!");
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

    public List<Carta> getCartas() {
        return new ArrayList<>(cartas);
    }

    public void adicionarPontos(int pontosGanhos) {
        if (pontosGanhos < 0) {
            throw new IllegalArgumentException("Pontos não podem ser negativos!");
        }
        this.pontos += pontosGanhos;
    }

    public void removerPontos(int pontosRemovidos) {
        if (pontosRemovidos < 0) {
            throw new IllegalArgumentException("Pontos a remover não podem ser negativos!");
        }

        this.pontos = Math.max(0, this.pontos - pontosRemovidos);
    }

    public void zerarPontos() {
        this.pontos = 0;
    }

    public int getPontos() {
        return pontos;
    }

    private void validarIndiceCartas(int indice) {
        if (indice < 0 || indice >= cartas.size()) {
            throw new IndexOutOfBoundsException(
                    String.format("Índice inválido: %d. Cartas disponíveis: 0 a %d",
                            indice, cartas.size() - 1)
            );
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do jogador não pode ser vazio!");
        }

        if (nome.trim().length() > 20) {
            throw new IllegalArgumentException("Nome muito longo! Máximo 20 caracteres.");
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome.trim();
    }

    @Override
    public String toString() {
        return String.format("%s (%d pontos) - %d cartas",
                nome, pontos, cartas.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Jogador jogador = (Jogador) obj;
        return nome.equals(jogador.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}