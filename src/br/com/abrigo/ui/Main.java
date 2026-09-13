package br.com.abrigo.ui;

import br.com.abrigo.infrastructure.FormularioRepositorioArquivo;

public class Main {
    public static void main(String[] args) {
        try{
            FormularioRepositorioArquivo formularioRepositorioArquivo = new FormularioRepositorioArquivo();
            MenuConsole mc = new MenuConsole(formularioRepositorioArquivo);

            mc.carregarMenu();
        }catch (Exception e) {
            System.out.println("Erro ao iniciar o programa: " + e.getMessage());
        }
    }
}