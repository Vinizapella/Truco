package view;

import Model.*;
import Service.TrucoService;
import Util.TipoJogo;
import java.util.*;


public class MenuConsole {
    private Scanner scanner;
    private TrucoService trucoService;

    public MenuConsole() {
        scanner = new Scanner(System.in);
        trucoService = new TrucoService();
    }

    public void iniciar() {
        mostrarBanner();

        while (true) {
            mostrarMenuPrincipal();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    iniciarNovoJogo();
                    break;
                case 2:
                    mostrarRegras();
                    break;
                case 3:
                    System.out.println("👋 Obrigado por jogar!");
                    return;
                default:
                    System.out.println("❌ Opção inválida!");
            }
        }
    }

    private void mostrarBanner() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    🃏 JOGO DE TRUCO 🃏                    ║");
        System.out.println("║                                                          ║");
        System.out.println("║            ♠️ ♥️ ♦️ ♣️  BEM-VINDO!  ♣️ ♦️ ♥️ ♠️            ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void mostrarMenuPrincipal() {
        System.out.println("┌─────────────────────────────────────┐");
        System.out.println("│            MENU PRINCIPAL           │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│  1. 🎮 Iniciar Novo Jogo           │");
        System.out.println("│  2. 📋 Ver Regras                  │");
        System.out.println("│  3. 🚪 Sair                        │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    private void iniciarNovoJogo() {
        limparTela();
        mostrarTiposDeJogo();

        TipoJogo tipoJogo = escolherTipoJogo();
        List<String> nomes = coletarNomesJogadores(tipoJogo);

        trucoService.criarMesa(tipoJogo);
        trucoService.adicionarJogadores(nomes);
        trucoService.iniciarPartida();

        executarJogo();
    }

    private void mostrarTiposDeJogo() {
        System.out.println("┌─────────────────────────────────────┐");
        System.out.println("│         TIPO DE JOGO                │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│  1. 👥 Dupla (2 jogadores)         │");
        System.out.println("│  2. 👥👥 Quarteto (4 jogadores)     │");
        System.out.println("└─────────────────────────────────────┘");
    }

    private TipoJogo escolherTipoJogo() {
        while (true) {
            System.out.print("Escolha o tipo de jogo: ");
            int opcao = lerOpcao();

            switch (opcao) {
                case 1: return TipoJogo.DUPLA;
                case 2: return TipoJogo.QUARTETO;
                default: System.out.println("❌ Opção inválida!");
            }
        }
    }

    private List<String> coletarNomesJogadores(TipoJogo tipoJogo) {
        List<String> nomes = new ArrayList<>();

        System.out.println("\n📝 Digite os nomes dos jogadores:");
        for (int i = 1; i <= tipoJogo.getNumeroJogadores(); i++) {
            System.out.print("Jogador " + i + ": ");
            String nome = scanner.nextLine().trim();
            if (nome.isEmpty()) nome = "Jogador " + i;
            nomes.add(nome);
        }

        return nomes;
    }

    private void executarJogo() {
        while (!trucoService.getMesa().partidaAcabou()) {
            mostrarEstadoJogo();

            if (trucoService.isTrucoAtivo()) {
                processarTruco();
            } else {
                processarJogadaNormal();
            }

            aguardarProximoJogador();
        }

        mostrarVencedor();
    }

    private void mostrarEstadoJogo() {
        limparTela();
        Mesa mesa = trucoService.getMesa();

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    🃏 JOGO DE TRUCO 🃏                    ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.printf("║  Rodada: %d/3  │  Pontos da mão: %d  │  Tipo: %s   ║%n",
                mesa.getRodadaAtual(),
                calcularPontosMao(),
                mesa.getTipoJogo() == TipoJogo.DUPLA ? "Dupla" : "Quarteto");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        mostrarPlacar();

        mostrarCartasNaMesa();

        mostrarCartasJogadorAtual();
    }

    private void mostrarPlacar() {
        Mesa mesa = trucoService.getMesa();

        System.out.println("┌─────────────────── PLACAR ───────────────────┐");

        if (mesa.getTipoJogo() == TipoJogo.DUPLA) {
            Jogador j1 = mesa.getJogadores().get(0);
            Jogador j2 = mesa.getJogadores().get(1);

            System.out.printf("│  %-20s %2d x %-2d %20s │%n",
                    j1.getNome(), j1.getPontos(), j2.getPontos(), j2.getNome());
        } else {
            List<Jogador> jogadores = mesa.getJogadores();
            System.out.printf("│  Time 1: %-15s %-15s │%n",
                    jogadores.get(0).getNome(), jogadores.get(2).getNome());
            System.out.printf("│          %2d pontos                          │%n",
                    jogadores.get(0).getPontos());
            System.out.println("│                                             │");
            System.out.printf("│  Time 2: %-15s %-15s │%n",
                    jogadores.get(1).getNome(), jogadores.get(3).getNome());
            System.out.printf("│          %2d pontos                          │%n",
                    jogadores.get(1).getPontos());
        }

        System.out.println("└─────────────────────────────────────────────┘");
        System.out.println();
    }

    private void mostrarCartasNaMesa() {
        List<Carta> cartasNaMesa = trucoService.getMesa().getCartasJogadas();

        if (!cartasNaMesa.isEmpty()) {
            System.out.println("┌─────────── CARTAS NA MESA ───────────┐");
            System.out.print("│  ");

            for (int i = 0; i < cartasNaMesa.size(); i++) {
                System.out.printf("[%s]  ", cartasNaMesa.get(i));
            }

            for (int i = cartasNaMesa.size(); i < 4; i++) {
                System.out.print("[  ]  ");
            }

            System.out.println("  │");
            System.out.println("└───────────────────────────────────────┘");
            System.out.println();
        }
    }

    private void mostrarCartasJogadorAtual() {
        Jogador jogadorAtual = trucoService.getMesa().getJogadorAtual();
        List<Carta> cartas = jogadorAtual.getCartas();

        System.out.println("┌──────────── SUA VEZ: " + jogadorAtual.getNome() + " ────────────┐");
        System.out.println("│                                                          │");
        System.out.print("│    ");

        for (int i = 0; i < cartas.size(); i++) {
            System.out.printf("(%d)[%s]   ", i + 1, cartas.get(i));
        }

        for (int i = cartas.size(); i < 3; i++) {
            System.out.print("      ");
        }

        System.out.println("    │");
        System.out.println("│                                                          │");
        System.out.println("└──────────────────────────────────────────────────────────┘");
        System.out.println();
    }

    private void processarJogadaNormal() {
        Jogador jogadorAtual = trucoService.getMesa().getJogadorAtual();

        System.out.println("┌─────────────── OPÇÕES ───────────────┐");
        System.out.println("│  1-3. Jogar carta (número da carta) │");
        System.out.println("│  4. 🔥 Pedir Truco                  │");
        System.out.println("└─────────────────────────────────────┘");

        System.out.print("Sua escolha: ");
        int opcao = lerOpcao();

        if (opcao >= 1 && opcao <= 3 && opcao <= jogadorAtual.getCartas().size()) {
            String resultado = trucoService.processarJogada(opcao - 1);
            System.out.println("\n" + resultado);
        } else if (opcao == 4) {
            String resultado = trucoService.pedirTruco(jogadorAtual);
            System.out.println("\n🔥 " + resultado);
        } else {
            System.out.println("❌ Opção inválida!");
        }
    }

    private void processarTruco() {
        System.out.println("🔥 TRUCO ATIVO! O que você vai fazer?");
        System.out.println("┌─────────────── RESPOSTA AO TRUCO ───────────────┐");
        System.out.println("│  1. ✅ Aceitar                                 │");
        System.out.println("│  2. 🔥 Aumentar (Seis/Nove/Doze)              │");
        System.out.println("│  3. 🏃 Correr (desistir)                       │");
        System.out.println("└─────────────────────────────────────────────────┘");

        System.out.print("Sua escolha: ");
        int opcao = lerOpcao();

        switch (opcao) {
            case 1:
                System.out.println("✅ " + trucoService.aceitarTruco());
                break;
            case 2:
                String resultado = trucoService.pedirTruco(trucoService.getMesa().getJogadorAtual());
                System.out.println("🔥 " + resultado);
                break;
            case 3:
                System.out.println("🏃 " + trucoService.rejeitarTruco());
                break;
            default:
                System.out.println("❌ Opção inválida!");
        }
    }

    private void aguardarProximoJogador() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔄 Pressione ENTER para continuar para o próximo jogador...");
        System.out.println("   (Vire a tela para o próximo jogador!)");
        System.out.println("=".repeat(60));
        scanner.nextLine();
    }

    private void mostrarVencedor() {
        limparTela();
        Jogador vencedor = trucoService.getMesa().getVencedor();

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    🎉 PARABÉNS! 🎉                        ║");
        System.out.println("║                                                          ║");
        System.out.printf("║              🏆 VENCEDOR: %-20s 🏆              ║%n", vencedor.getNome());
        System.out.printf("║                   %d PONTOS                              ║%n", vencedor.getPontos());
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");

        System.out.println("\nPressione ENTER para voltar ao menu...");
        scanner.nextLine();
    }

    private void mostrarRegras() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                    📋 REGRAS DO TRUCO                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("🃏 HIERARQUIA DAS CARTAS (da menor para maior):");
        System.out.println("   4, 5, 6, 7, Q(Dama), J(Valete), K(Rei), A(Ás), 2, 3");
        System.out.println();
        System.out.println("🎯 OBJETIVO:");
        System.out.println("   • Ser o primeiro a fazer 12 pontos");
        System.out.println("   • Cada mão vale inicialmente 1 ponto");
        System.out.println();
        System.out.println("🔥 SISTEMA DE TRUCO:");
        System.out.println("   • Normal: 1 ponto");
        System.out.println("   • Truco: 3 pontos");
        System.out.println("   • Seis: 6 pontos");
        System.out.println("   • Nove: 9 pontos");
        System.out.println("   • Doze: 12 pontos");
        System.out.println();
        System.out.println("⚡ COMO JOGAR:");
        System.out.println("   • Cada jogador recebe 3 cartas");
        System.out.println("   • Melhor de 3 rodadas ganha a mão");
        System.out.println("   • Quem ganha a rodada começa a próxima");
        System.out.println();
        System.out.println("Pressione ENTER para voltar...");
        scanner.nextLine();
    }

    private void limparTela() {
        // Limpa a tela (funciona na maioria dos terminais)
        System.out.print("\033[2J\033[H");
    }

    private int lerOpcao() {
        try {
            int opcao = Integer.parseInt(scanner.nextLine().trim());
            return opcao;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int calcularPontosMao() {
        switch (trucoService.getNivelTruco()) {
            case 1: return 1;
            case 2: return 3;
            case 3: return 6;
            case 4: return 9;
            case 5: return 12;
            default: return 1;
        }
    }
}