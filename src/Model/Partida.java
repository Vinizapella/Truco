package Model;

import Util.TipoJogo;
import java.util.*;

public class Partida {
    private List<Rodada> rodadas;
    private Placar placar;
    private int maoAtual;
    private boolean partidaFinalizada;
    private Jogador primeiroJogador;
    private TipoJogo tipoJogo;
    private List<Jogador> jogadores;
    private Rodada rodadaAtual;
    private int rodadasVencidasTime1;
    private int rodadasVencidasTime2;

    public Partida(TipoJogo tipoJogo) {
        this.tipoJogo = tipoJogo;
        this.rodadas = new ArrayList<>();
        this.placar = new Placar(tipoJogo);
        this.maoAtual = 1;
        this.partidaFinalizada = false;
        this.jogadores = new ArrayList<>();
        this.rodadasVencidasTime1 = 0;
        this.rodadasVencidasTime2 = 0;
    }

    public void adicionarJogadores(List<Jogador> jogadores) {
        if (jogadores.size() != tipoJogo.getNumeroJogadores()) {
            throw new IllegalArgumentException("Número incorreto de jogadores!");
        }

        this.jogadores = new ArrayList<>(jogadores);

        for (Jogador jogador : jogadores) {
            placar.adicionarJogador(jogador);
        }

        this.primeiroJogador = jogadores.get(0);
    }

    public void iniciarNovaMao() {
        rodadas.clear();
        rodadaAtual = null;
        rodadasVencidasTime1 = 0;
        rodadasVencidasTime2 = 0;

        iniciarNovaRodada();
    }

    public void iniciarNovaRodada() {
        int numeroRodada = rodadas.size() + 1;
        rodadaAtual = new Rodada(numeroRodada);
        rodadas.add(rodadaAtual);
    }

    public void adicionarJogada(Jogador jogador, Carta carta) {
        if (partidaFinalizada) {
            throw new IllegalStateException("Partida já foi finalizada!");
        }

        if (rodadaAtual == null) {
            iniciarNovaRodada();
        }

        rodadaAtual.adicionarJogada(jogador, carta);

        if (rodadaAtual.isCompleta(tipoJogo.getNumeroJogadores())) {
            finalizarRodadaAtual();
        }
    }

    private void finalizarRodadaAtual() {
        Jogador vencedorRodada = rodadaAtual.avaliarVencedor();

        if (tipoJogo == TipoJogo.DUPLA) {
            if (vencedorRodada == jogadores.get(0)) {
                rodadasVencidasTime1++;
            } else {
                rodadasVencidasTime2++;
            }
        } else {
            if (vencedorRodada == jogadores.get(0) || vencedorRodada == jogadores.get(2)) {
                rodadasVencidasTime1++;
            } else {
                rodadasVencidasTime2++;
            }
        }

        if (rodadasVencidasTime1 >= 2 || rodadasVencidasTime2 >= 2) {
            finalizarMao();
        }

        primeiroJogador = vencedorRodada;
    }

    private void finalizarMao() {
        boolean time1Venceu = rodadasVencidasTime1 >= 2;

        if (tipoJogo == TipoJogo.DUPLA) {
            if (time1Venceu) {
                placar.adicionarPontos(jogadores.get(0), 1);
            } else {
                placar.adicionarPontos(jogadores.get(1), 1);
            }
        } else {
            if (time1Venceu) {
                placar.adicionarPontos(jogadores.get(0), 1);
                placar.adicionarPontos(jogadores.get(2), 1);
            } else {
                placar.adicionarPontos(jogadores.get(1), 1);
                placar.adicionarPontos(jogadores.get(3), 1);
            }
        }

        maoAtual++;

        if (placar.temVencedor()) {
            partidaFinalizada = true;
        }
    }

    public void atribuirPontos(int pontos) {
        boolean time1Venceu = rodadasVencidasTime1 >= 2;

        if (tipoJogo == TipoJogo.DUPLA) {
            if (time1Venceu) {
                placar.adicionarPontos(jogadores.get(0), pontos);
            } else {
                placar.adicionarPontos(jogadores.get(1), pontos);
            }
        } else {
            if (time1Venceu) {
                placar.adicionarPontos(jogadores.get(0), pontos);
                placar.adicionarPontos(jogadores.get(2), pontos);
            } else {
                placar.adicionarPontos(jogadores.get(1), pontos);
                placar.adicionarPontos(jogadores.get(3), pontos);
            }
        }

        if (placar.temVencedor()) {
            partidaFinalizada = true;
        }
    }

    public boolean maoAcabou() {
        return rodadasVencidasTime1 >= 2 || rodadasVencidasTime2 >= 2;
    }

    public List<Jogador> getVencedoresMao() {
        List<Jogador> vencedores = new ArrayList<>();

        if (!maoAcabou()) {
            return vencedores;
        }

        boolean time1Venceu = rodadasVencidasTime1 >= 2;

        if (tipoJogo == TipoJogo.DUPLA) {
            vencedores.add(time1Venceu ? jogadores.get(0) : jogadores.get(1));
        } else {
            if (time1Venceu) {
                vencedores.add(jogadores.get(0));
                vencedores.add(jogadores.get(2));
            } else {
                vencedores.add(jogadores.get(1));
                vencedores.add(jogadores.get(3));
            }
        }

        return vencedores;
    }

    public List<Rodada> getRodadas() {
        return new ArrayList<>(rodadas);
    }

    public Placar getPlacar() {
        return placar;
    }

    public int getMaoAtual() {
        return maoAtual;
    }

    public boolean isPartidaFinalizada() {
        return partidaFinalizada;
    }

    public Jogador getPrimeiroJogador() {
        return primeiroJogador;
    }

    public List<Jogador> getJogadores() {
        return new ArrayList<>(jogadores);
    }

    public Rodada getRodadaAtual() {
        return rodadaAtual;
    }

    public int getRodadasVencidasTime1() {
        return rodadasVencidasTime1;
    }

    public int getRodadasVencidasTime2() {
        return rodadasVencidasTime2;
    }

    public Jogador getVencedorPartida() {
        return placar.getVencedor();
    }

    @Override
    public String toString() {
        return String.format("Partida %s - Mão %d - %s",
                tipoJogo.getDescricao(),
                maoAtual,
                partidaFinalizada ? "Finalizada" : "Em andamento");
    }
}
