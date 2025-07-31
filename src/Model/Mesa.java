package Model;

import Util.TipoJogo;
import java.util.*;

public class Mesa {
    private List<Jogador> jogadores;
    private TipoJogo tipoJogo;
    private Baralho baralho;
    private int jogadorAtual;
    private boolean jogoIniciado;
    private int rodadaAtual;
    private int pontosRodada;
    private List<Carta> cartasJogadas;
    private List<Jogador> ordemCartasJogadas;

    public Mesa(TipoJogo tipoJogo) {
        this.tipoJogo = tipoJogo;
        this.jogadores = new ArrayList<>();
        this.baralho = new Baralho();
        this.jogadorAtual = 0;
        this.jogoIniciado = false;
        this.rodadaAtual = 1;
        this.pontosRodada = 1;
        this.cartasJogadas = new ArrayList<>();
        this.ordemCartasJogadas = new ArrayList<>();
    }

    public void adicionarJogador(Jogador jogador) {
        if (jogadores.size() >= tipoJogo.getNumeroJogadores()) {
            throw new RuntimeException("Mesa já está completa!");
        }
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador não pode ser nulo!");
        }
        jogadores.add(jogador);
    }

    public void iniciarJogo() {
        if (jogadores.size() != tipoJogo.getNumeroJogadores()) {
            throw new RuntimeException("Número de jogadores incorreto para iniciar o jogo!");
        }

        for (Jogador jogador : jogadores) {
            jogador.limparMao();
            jogador.receberCartas(baralho.distribuirCartas(3));
        }

        jogoIniciado = true;
    }

    public void proximoJogador() {
        jogadorAtual = (jogadorAtual + 1) % tipoJogo.getNumeroJogadores();
    }

    public void setJogadorAtual(Jogador jogador) {
        int indice = jogadores.indexOf(jogador);
        if (indice >= 0) {
            jogadorAtual = indice;
        }
    }

    public void novaMao() {
        baralho.reiniciar();
        rodadaAtual = 1;
        pontosRodada = 1;
        cartasJogadas.clear();
        ordemCartasJogadas.clear();

        for (Jogador jogador : jogadores) {
            jogador.limparMao();
        }

        iniciarJogo();
    }

    public boolean estaPronta() {
        return jogadores.size() == tipoJogo.getNumeroJogadores() && jogoIniciado;
    }

    public boolean partidaAcabou() {
        return jogadores.stream().anyMatch(j -> j.getPontos() >= 12);
    }

    public Jogador getVencedor() {
        return jogadores.stream()
                .filter(j -> j.getPontos() >= 12)
                .findFirst()
                .orElse(null);
    }

    public List<Jogador> getJogadores() {
        return new ArrayList<>(jogadores);
    }

    public Jogador getJogadorAtual() {
        if (jogadores.isEmpty()) {
            return null;
        }
        return jogadores.get(jogadorAtual);
    }

    public int getIndiceJogadorAtual() {
        return jogadorAtual;
    }

    public TipoJogo getTipoJogo() {
        return tipoJogo;
    }

    public boolean isJogoIniciado() {
        return jogoIniciado;
    }

    public Baralho getBaralho() {
        return baralho;
    }

    public int getRodadaAtual() {
        return rodadaAtual;
    }

    public int getPontosRodada() {
        return pontosRodada;
    }

    public List<Carta> getCartasJogadas() {
        return new ArrayList<>(cartasJogadas);
    }

    public List<Jogador> getOrdemCartasJogadas() {
        return new ArrayList<>(ordemCartasJogadas);
    }

    @Override
    public String toString() {
        return String.format("Mesa %s - %d/%d jogadores - %s",
                tipoJogo.getDescricao(),
                jogadores.size(),
                tipoJogo.getNumeroJogadores(),
                jogoIniciado ? "Jogo iniciado" : "Aguardando");
    }
}
