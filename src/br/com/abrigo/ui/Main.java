package br.com.abrigo.ui;

import br.com.abrigo.domain.repository.PetRepositorio;
import br.com.abrigo.infrastructure.FormularioRepositorioArquivo;
import br.com.abrigo.infrastructure.PetRepositorioArquivo;
import br.com.abrigo.service.PetServico;

public class Main {
    public static void main(String[] args) {
        try{
            FormularioRepositorioArquivo formularioRepositorioArquivo = new FormularioRepositorioArquivo();
            PetRepositorio petRepositorio = new PetRepositorioArquivo();
            PetServico petServico = new PetServico(petRepositorio);
            MenuConsole mc = new MenuConsole(formularioRepositorioArquivo, petServico);

            mc.carregarMenu();
        }catch (Exception e) {
            System.out.println("Erro ao iniciar o programa: " + e.getMessage());
        }
    }
}