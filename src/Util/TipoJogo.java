package Util;

public enum TipoJogo {
    DUPLA(2, "Dupla (2 jogadores)"),
    QUARTETO(4, "Quarteto (4 jogadores)");

    private final int numeroJogadores;
    private final String descricao;

    TipoJogo(int numeroJogadores, String descricao) {
        this.numeroJogadores = numeroJogadores;
        this.descricao = descricao;
    }

    public int getNumeroJogadores() {
        return numeroJogadores;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}