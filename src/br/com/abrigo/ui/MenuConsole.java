package br.com.abrigo.ui;

import br.com.abrigo.domain.models.Pet;
import br.com.abrigo.domain.repository.FormularioRepositorio;
import br.com.abrigo.service.PetServico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MenuConsole {
    private static final String VERDE = "\u001B[32m";
    private static final String RESET = "\u001B[0m";
    private final FormularioRepositorio formularioRepositorioArquivo;
    private final PetServico petServico;
    private final Scanner sc = new Scanner(System.in);

    public MenuConsole(FormularioRepositorio formularioRepositorioArquivo, PetServico petServico) {
        this.formularioRepositorioArquivo = formularioRepositorioArquivo;
        this.petServico = petServico;
    }

    public void carregarMenu() {
        int opcao = 0;
        do {
            exibirOpcoes();
            String entrada = sc.nextLine();

            try {
                opcao = Integer.parseInt(entrada);
                processarOpcao(opcao);
            } catch (NumberFormatException e) {
                System.out.println("\nOpção inválida. Digite apenas números inteiros.\n");
                opcao = 0;
            }
        } while (opcao != 6);
    }

    private void exibirOpcoes() {
        System.out.println("===== MENU PRINCIPAL =====");
        System.out.println("1 - Cadastrar um novo pet");
        System.out.println("2 - Alterar os dados do pet cadastrado");
        System.out.println("3 - Deletar um pet cadastrado");
        System.out.println("4 - Listar todos os pets cadastrados");
        System.out.println("5 - Listar pets por algum critério(idade, nome, raça)");
        System.out.println("6 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void processarOpcao(int opcao) {
        switch (opcao) {
            case 1 -> cadastrarPet();
            case 2 -> System.out.println("\nAlterar os dados do pet cadastrado");
            case 3 -> System.out.println("\nDeletar os pets cadastrados");
            case 4 -> System.out.println("\nListar todos os pets cadastrados");
            case 5 -> buscarPetsPorCriterios();
            case 6 -> System.out.println("\nFinalizando programa...");
            default -> {
                if (opcao <= 0) {
                    System.out.println("\nOpção inválida. Digite apenas números inteiros positivos.\n");
                } else if (opcao > 6) {
                    System.out.println("\nOpção inválida. Digite apenas números inteiros entre 1 e 6.\n");
                }
            }
        }
    }

    private void cadastrarPet() {
        try {
            List<String> respostas = new ArrayList<>();
            System.out.print("\n");
            for (String p : formularioRepositorioArquivo.carregarPerguntas()) {
                System.out.println(p);
                String resposta = sc.nextLine();
                respostas.add(resposta);
            }
            petServico.cadastrarPet(respostas);
            System.out.print("\n");
        } catch (IOException e) {
            System.out.println("Erro ao carregar o formulário: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erro ao cadastrar o pet: " + e.getMessage());
        }
    }

    private void buscarPetsPorCriterios() {
        System.out.println("\n===== BUSCA DE PETS =====");
        System.out.print("Informe o TIPO do pet (Cachorro/Gato): ");
        String tipo = sc.nextLine().trim();
        System.out.println("\nVocê pode adicionar até mais 2 critérios(ou aperte ENTER para ignorar):");
        System.out.print("Qual termo deseja buscar no 1º critério? ");
        String termo1 = sc.nextLine().trim();
        String termo2 = "";
        if (!termo1.isEmpty()) {
            System.out.print("Qual termo deseja buscar no 2º critério extra? ");
            termo2 = sc.nextLine().trim();
        }
        List<Pet> encontrados = petServico.buscarPets(tipo, termo1, termo2);
        if (encontrados.isEmpty()) {
            System.out.println("\nNenhum pet encontrado com os critérios informados.\n");
            return;
        }
        System.out.println("\n===== PETS ENCONTRADOS =====");
        for (int i = 0; i < encontrados.size(); i++) {
            Pet p = encontrados.get(i);
            String nome = destacarTermos(p.getNome(), tipo, termo1, termo2);
            String tipoPet = destacarTermos(p.getTipo() != null ? p.getTipo().toString() : "N/I", tipo, termo1, termo2);
            String sexo = destacarTermos(p.getSexo() != null ? p.getSexo().toString() : "N/I", tipo, termo1, termo2);
            String endereco = destacarTermos((p.getEndereco() != null) ? p.getEndereco().paraFormatoArquivo() : "N/I", tipo, termo1, termo2);
            String idade = destacarTermos((p.getIdade() != null) ? p.getIdade() + " anos" : "N/I", tipo, termo1, termo2);
            String peso = destacarTermos((p.getPesoAproximado() != null) ? p.getPesoAproximado() + " kg" : "N/I", tipo, termo1, termo2);
            String raca = destacarTermos((p.getRaca() != null) ? p.getRaca() : "N/I", tipo, termo1, termo2);
            System.out.println((i + 1) + ". " + nome + " | " +
                    tipoPet + " | " + sexo + " | " + endereco + " | " +
                    idade + " | " + peso + " | " + raca);
        }
        System.out.println();
    }

    private String destacarTermos(String texto, String... termos) {
        if (texto == null || texto.isBlank()) {
            return texto;
        }
        String resultado = texto;
        for (String termo : termos) {
            if (termo != null && !termo.isBlank()) {
                resultado = resultado.replaceAll("(?i)" + Pattern.quote(termo), VERDE + "$0" + RESET);
            }
        }
        return resultado;
    }
}