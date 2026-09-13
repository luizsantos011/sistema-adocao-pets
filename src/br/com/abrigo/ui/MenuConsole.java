package br.com.abrigo.ui;

import br.com.abrigo.domain.repository.FormularioRepositorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuConsole {
    FormularioRepositorio formularioRepositorioArquivo;
    Scanner sc = new Scanner(System.in);

    public MenuConsole(FormularioRepositorio formularioRepositorioArquivo) {
        this.formularioRepositorioArquivo = formularioRepositorioArquivo;
    }

    public void carregarMenu () {
        int opcao = 0;
        do{
            exibirOpcoes();
            String entrada = sc.nextLine();

            try {
                opcao = Integer.parseInt(entrada);
                processarOpcao(opcao);
            }catch(NumberFormatException e) {
                System.out.println("\nOpção inválida. Digite apenas números inteiros.\n");
                opcao = 0;
            }
        }while(opcao!=6);
    }

    private void exibirOpcoes() {
        System.out.println("----- MENU PRINCIPAL -----");
        System.out.println("1 - Cadastrar um novo pet");
        System.out.println("2 - Alterar os dados do pet cadastrado");
        System.out.println("3 - Deletar um pet cadastrado");
        System.out.println("4 - Listar todos os pets cadastrados");
        System.out.println("5 - Listar pets por algum critério (idade, nome, raça)");
        System.out.println("6 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> {
                try{
                    List<String> respostas = new ArrayList<>();
                    System.out.print("\n");
                    for(String p : formularioRepositorioArquivo.carregarPerguntas()) {
                        System.out.println(p);
                        String resposta = sc.nextLine();
                        respostas.add(resposta);
                    }
                    System.out.print("\n");
                }catch(IOException e) {
                    System.out.println("Erro ao carregar o formulário: " + e.getMessage());
                }
            }
            case 2 -> System.out.println("\nAlterar os dados do pet cadastrado");
            case 3 -> System.out.println("\nDeletar os pets cadastrados");
            case 4 -> System.out.println("\nListar todos os pets cadastrados");
            case 5 -> System.out.println("\nListar pet por algum critério");
            case 6 -> System.out.println("\nFinalizando programa...");
            default -> {
                if(opcao <= 0){
                    System.out.println("\nOpção inválida. Digite apenas números inteiros positivos.\n");
                }else if(opcao > 6){
                    System.out.println("\nOpção inválida. Digite apenas números inteiros entre 1 e 6.\n");
                }
            }
        }
    }
}
