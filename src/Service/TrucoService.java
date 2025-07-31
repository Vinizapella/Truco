package Service;

import Model.*;
import Util.TipoJogo;
import java.util.*;

public class TrucoService {
    private Mesa mesa;
    private int rodadasVencidasJ1;
    private int rodadasVencidasJ2;
    private boolean trucoAtivo;
    private int nivelTruco;
    private Jogador jogadorQuePediuTruco;

    public TrucoService() {
        this.rodadasVencidasJ1 = 0;
        this.rodadasVencidasJ2 = 0;
        this.trucoAtivo = false;
        this.nivelTruco = 1;
    }

    public Mesa criarMesa(TipoJogo tipoJogo) {
        mesa = new Mesa(tipoJogo);
        resetarContadores();
        return mesa;
    }

    public void adicionarJogadores(List<String> nomes) {
        for (String nome : nomes) {
            mesa.adicionarJogador(new Jogador(nome));
        }
    }

    public void iniciarPartida() {
        mesa.iniciarJogo();
        resetarContadores();
    }

    public String processarJogada(int indiceCarta) {
        try {
            Jogador jogadorAtual = mesa.getJogadorAtual();
            Carta cartaJogada = jogadorAtual.getCartas().get(indiceCarta);

            mesa.jogarCarta(indiceCarta);

            String resultado = jogadorAtual.getNome() + " jogou: " + cartaJogada;

            if (mesa.getCartasJogadas().size() == 0) {
                resultado += "\n" + avaliarVencedorMao();
            }

            return resultado;

        } catch (Exception e) {
            return "Erro ao processar jogada: " + e.getMessage();
        }
    }

    private String avaliarVencedorMao() {
        String resultado = "";

        if (rodadasVencidasJ1 >= 2) {
            resultado = "Jogador/Time 1 venceu a mão!";
            atribuirPontos(true);
        } else if (rodadasVencidasJ2 >= 2) {
            resultado = "Jogador/Time 2 venceu a mão!";
            atribuirPontos(false);
        }

        if (!resultado.isEmpty()) {
            if (!mesa.partidaAcabou()) {
                mesa.novaMao();
                resetarContadores();
                resultado += "\nNova mão iniciada!";
            } else {
                resultado += "\n🎉 JOGO ACABOU! Vencedor: " + mesa.getVencedor().getNome();
            }
        }

        return resultado;
    }

    private void atribuirPontos(boolean time1Venceu) {
        int pontos = calcularPontosTruco();

        if (mesa.getTipoJogo() == TipoJogo.DUPLA) {
            if (time1Venceu) {
                mesa.getJogadores().get(0).adicionarPontos(pontos);
            } else {
                mesa.getJogadores().get(1).adicionarPontos(pontos);
            }
        } else { // Quarteto
            if (time1Venceu) {
                mesa.getJogadores().get(0).adicionarPontos(pontos);
                mesa.getJogadores().get(2).adicionarPontos(pontos);
            } else {
                mesa.getJogadores().get(1).adicionarPontos(pontos);
                mesa.getJogadores().get(3).adicionarPontos(pontos);
            }
        }
    }

    private int calcularPontosTruco() {
        switch (nivelTruco) {
            case 1: return 1;
            case 2: return 3;
            case 3: return 6;
            case 4: return 9;
            case 5: return 12;
            default: return 1;
        }
    }

    public String pedirTruco(Jogador jogador) {
        if (nivelTruco >= 5) {
            return "Não é possível pedir truco além de 12!";
        }

        nivelTruco++;
        trucoAtivo = true;
        jogadorQuePediuTruco = jogador;

        String[] nomes = {"", "Truco", "Seis", "Nove", "Doze"};
        return jogador.getNome() + " pediu " + nomes[nivelTruco] + "!";
    }

    public String aceitarTruco() {
        trucoAtivo = false;
        return "Truco aceito! Mão vale " + calcularPontosTruco() + " pontos.";
    }

    public String rejeitarTruco() {
        int pontosGanhos = calcularPontosTruco() / 3;

        jogadorQuePediuTruco.adicionarPontos(pontosGanhos);

        String resultado = "Truco rejeitado! " + jogadorQuePediuTruco.getNome() +
                " ganhou " + pontosGanhos + " pontos.";

        if (!mesa.partidaAcabou()) {
            mesa.novaMao();
            resetarContadores();
            resultado += "\nNova mão iniciada!";
        }

        return resultado;
    }

    private void resetarContadores() {
        rodadasVencidasJ1 = 0;
        rodadasVencidasJ2 = 0;
        trucoAtivo = false;
        nivelTruco = 1;
        jogadorQuePediuTruco = null;
    }

    public String getStatusJogo() {
        if (mesa == null) return "Nenhuma mesa criada.";

        StringBuilder status = new StringBuilder();
        status.append("=== STATUS DO JOGO ===\n");
        status.append("Tipo: ").append(mesa.getTipoJogo().getDescricao()).append("\n");
        status.append("Rodada: ").append(mesa.getRodadaAtual()).append("\n");
        status.append("Pontos da mão: ").append(calcularPontosTruco()).append("\n");

        if (trucoAtivo) {
            status.append("⚡ TRUCO ATIVO! Aguardando resposta...\n");
        }

        status.append("\n=== JOGADORES ===\n");
        for (Jogador jogador : mesa.getJogadores()) {
            status.append(jogador.toString()).append("\n");
        }

        return status.toString();
    }

    public Mesa getMesa() {
        return mesa;
    }

    public boolean isTrucoAtivo() {
        return trucoAtivo;
    }

    public int getNivelTruco() {
        return nivelTruco;
    }
}