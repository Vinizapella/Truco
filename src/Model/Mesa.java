package Model;

import Util.TipoJogo;
import java.util.*;

public class Mesa {
    private List<Jogador> jogadores;
    private TipoJogo tipoJogo;
    private Baralho baralho;
    private int jogadorAtual;
    private int rodadaAtual;
    private List<Carta> cartasJogadas;
    private List<Jogador> ordemCartasJogadas; // Para saber quem jogou cada carta
    private boolean jogoIniciado;
    private int pontosRodada; // 1, 3, 6, 9, 12 (normal, truco, 6, 9, 12)

    public Mesa(TipoJogo tipoJogo) {
        this.tipoJogo = tipoJogo;
        this.jogadores = new ArrayList<>();
        this.baralho = new Baralho();
        this.jogadorAtual = 0;
        this.rodadaAtual = 1;
        this.cartasJogadas = new ArrayList<>();
        this.ordemCartasJogadas = new ArrayList<>();
        this.jogoIniciado = false;
        this.pontosRodada = 1;
    }

    public void adicionarJogador(Jogador jogador) {
        if (jogadores.size() >= tipoJogo.getNumeroJogadores()) {
            throw new RuntimeException("Mesa já está completa!");
        }
        jogadores.add(jogador);
    }

    public void iniciarJogo() {
        if (jogadores.size() != tipoJogo.getNumeroJogadores()) {
            throw new RuntimeException("Número de jogadores incorreto para iniciar o jogo!");
        }

        for (Jogador jogador : jogadores) {
            jogador.receberCartas(baralho.distribuirCartas(3));
        }

        jogoIniciado = true;
    }

    public void jogarCarta(int indiceCarta) {
        if (!jogoIniciado) {
            throw new RuntimeException("Jogo ainda não foi iniciado!");
        }

        Jogador jogador = jogadores.get(jogadorAtual);
        Carta cartaJogada = jogador.jogarCarta(indiceCarta);

        cartasJogadas.add(cartaJogada);
        ordemCartasJogadas.add(jogador);

        proximoJogador();

        if (cartasJogadas.size() == tipoJogo.getNumeroJogadores()) {
            avaliarRodada();
        }
    }

    private void avaliarRodada() {
        Carta cartaMaisForte = cartasJogadas.get(0);
        int indiceVencedor = 0;

        for (int i = 1; i < cartasJogadas.size(); i++) {
            if (cartasJogadas.get(i).ehMaisFortQue(cartaMaisForte)) {
                cartaMaisForte = cartasJogadas.get(i);
                indiceVencedor = i;
            }
        }

        Jogador vencedorRodada = ordemCartasJogadas.get(indiceVencedor);
        System.out.println("Rodada " + rodadaAtual + " vencida por: " + vencedorRodada.getNome());

        cartasJogadas.clear();
        ordemCartasJogadas.clear();
        rodadaAtual++;

        jogadorAtual = jogadores.indexOf(vencedorRodada);
    }

    private void proximoJogador() {
        jogadorAtual = (jogadorAtual + 1) % tipoJogo.getNumeroJogadores();
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

    public List<Jogador> getJogadores() {
        return new ArrayList<>(jogadores);
    }

    public Jogador getJogadorAtual() {
        return jogadores.get(jogadorAtual);
    }

    public int getRodadaAtual() {
        return rodadaAtual;
    }

    public List<Carta> getCartasJogadas() {
        return new ArrayList<>(cartasJogadas);
    }

    public boolean isJogoIniciado() {
        return jogoIniciado;
    }

    public int getPontosRodada() {
        return pontosRodada;
    }

    public TipoJogo getTipoJogo() {
        return tipoJogo;
    }
}