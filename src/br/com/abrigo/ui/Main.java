package br.com.abrigo.ui;

import br.com.abrigo.infrastructure.FileFormularioRepository;

public class Main {
    public static void main(String[] args) {
        try{
            FileFormularioRepository fileFormularioRepository = new FileFormularioRepository();
            MenuConsole mc = new MenuConsole(fileFormularioRepository);

            mc.carregarMenu();
        }catch (Exception e) {
            System.out.println("Erro ao iniciar o programa: " + e.getMessage());
        }
    }
}