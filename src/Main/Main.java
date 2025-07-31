package Main;

import view.MenuConsole;

public class Main {

    public static void main(String[] args) {
        try {
            MenuConsole menu = new MenuConsole();
            menu.iniciar();

        } catch (Exception e) {
            System.err.println("❌ Erro fatal ao executar o jogo:");
            System.err.println("   " + e.getMessage());
            e.printStackTrace();
        }
    }
}