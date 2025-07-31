package Model;

import Util.TipoJogo;
import java.util.*;

public class Placar {
    private Map<Jogador, Integer> pontos;
    private TipoJogo tipoJogo;
    private List<String> historicoPartidas;
    private static final int PONTOS_VITORIA = 12;

    public Placar(TipoJogo tipoJogo) {
        this.tipoJogo = tipoJogo;
        this.pontos = new HashMap<>();
        this.historicoPartidas = new ArrayList<>();
    }

    public void adicionarJogador(Jogador jogador) {
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador não pode ser nulo!");
        }
        pontos.put(jogador, 0);
    }

    public void adicionarPontos(Jogador jogador, int pontosGanhos) {
        if (jogador == null) {
            throw new IllegalArgumentException("Jogador não pode ser nulo!");
        }

        if (pontosGanhos < 0) {
            throw new IllegalArgumentException("Pontos não podem ser negativos!");
        }

        int pontosAtuais = pontos.getOrDefault(jogador, 0);
        pontos.put(jogador, pontosAtuais + pontosGanhos);

        historicoPartidas.add(String.format("%s ganhou %d pontos",
                jogador.getNome(), pontosGanhos));
    }

    public void adicionarPontosTime(List<Jogador> jogadores, List<Jogador> time, int pontosGanhos) {
        if (tipoJogo != TipoJogo.QUARTETO) {
            throw new IllegalStateException("Método apenas para quarteto!");
        }

        for (Jogador jogador : time) {
            if (jogadores.contains(jogador)) {
                adicionarPontos(jogador, pontosGanhos);
            }
        }
    }

    public int getPontos(Jogador jogador) {
        return pontos.getOrDefault(jogador, 0);
    }

    public int getPontosTime1(List<Jogador> jogadores) {
        if (tipoJogo != TipoJogo.QUARTETO) {
            throw new IllegalStateException("Método apenas para quarteto!");
        }

        if (jogadores.size() < 3) {
            return 0;
        }

        return getPontos(jogadores.get(0));
    }

    public int getPontosTime2(List<Jogador> jogadores) {
        if (tipoJogo != TipoJogo.QUARTETO) {
            throw new IllegalStateException("Método apenas para quarteto!");
        }

        if (jogadores.size() < 2) {
            return 0;
        }

        return getPontos(jogadores.get(1));
    }

    public boolean temVencedor() {
        return pontos.values().stream().anyMatch(p -> p >= PONTOS_VITORIA);
    }

    public Jogador getVencedor() {
        return pontos.entrySet().stream()
                .filter(entry -> entry.getValue() >= PONTOS_VITORIA)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public List<Jogador> getVencedores() {
        return pontos.entrySet().stream()
                .filter(entry -> entry.getValue() >= PONTOS_VITORIA)
                .map(Map.Entry::getKey)
                .collect(ArrayList::new, (list, player) -> list.add(player), ArrayList::addAll);
    }

    public void resetar() {
        pontos.replaceAll((jogador, pontos) -> 0);
        historicoPartidas.clear();
    }

    public void zerarTodos() {
        pontos.clear();
        historicoPartidas.clear();
    }

    public String getPlacarFormatado(List<Jogador> jogadores) {
        StringBuilder sb = new StringBuilder();

        if (tipoJogo == TipoJogo.DUPLA) {
            sb.append(String.format("%s: %d x %d :%s",
                    jogadores.get(0).getNome(),
                    getPontos(jogadores.get(0)),
                    getPontos(jogadores.get(1)),
                    jogadores.get(1).getNome()));
        } else {
            sb.append(String.format("Time 1 (%s & %s): %d\n",
                    jogadores.get(0).getNome(),
                    jogadores.get(2).getNome(),
                    getPontosTime1(jogadores)));
            sb.append(String.format("Time 2 (%s & %s): %d",
                    jogadores.get(1).getNome(),
                    jogadores.get(3).getNome(),
                    getPontosTime2(jogadores)));
        }

        return sb.toString();
    }

    public Map<Jogador, Integer> getTodosPontos() {
        return new HashMap<>(pontos);
    }

    public List<String> getHistoricoPartidas() {
        return new ArrayList<>(historicoPartidas);
    }

    public TipoJogo getTipoJogo() {
        return tipoJogo;
    }

    @Override
    public String toString() {
        return String.format("Placar %s - %d jogadores registrados",
                tipoJogo.getDescricao(), pontos.size());
    }
}