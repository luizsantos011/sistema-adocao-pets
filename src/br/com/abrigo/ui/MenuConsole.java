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
            case 2 -> alterarPet();
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

    private List<Pet> buscarPetsPorCriterios() {
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
            return encontrados;
        }

        System.out.println("\n===== PETS ENCONTRADOS =====");
        for (int i = 0; i < encontrados.size(); i++) {
            Pet p = encontrados.get(i);
            String nomeCompleto = p.getNome() + (p.getSobrenome() != null && !p.getSobrenome().isBlank() ? " " + p.getSobrenome() : "");
            String nome = destacarTermos(nomeCompleto, tipo, termo1, termo2);
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
        return encontrados;
    }

    private void alterarPet() {
        List<Pet> encontrados;
        do {
            encontrados = buscarPetsPorCriterios();
            if (encontrados.isEmpty()) {
                return;
            }
            System.out.print("Escolha o número do pet que deseja alterar: ");
            String entrada = sc.nextLine().trim();
            try {
                int indice = Integer.parseInt(entrada) - 1;
                if (indice >= 0 && indice < encontrados.size()) {
                    Pet petSelecionado = encontrados.get(indice);
                    executarFormularioAlteracao(petSelecionado);
                    return;
                }
                System.out.println("\nNúmero inválido! Tente a busca novamente.\n");
            } catch (NumberFormatException e) {
                System.out.println("\nEntrada inválida! Digite apenas números.\n");
            }
        } while (true);
    }

    private void executarFormularioAlteracao(Pet pet) {
        System.out.println("\n--- ALTERANDO DADOS DO PET: " + pet.getNome() + " ---");
        System.out.println("(Aperte ENTER para manter o valor atual)\n");
        System.out.print("Novo Nome (" + pet.getNome() + "): ");
        String nome = sc.nextLine().trim();
        if (!nome.isEmpty()) pet.setNome(nome);
        System.out.print("Novo Sobrenome (" + (pet.getSobrenome() != null ? pet.getSobrenome() : "Não informado") + "): ");
        String sobrenome = sc.nextLine().trim();
        if (!sobrenome.isEmpty()) pet.setSobrenome(sobrenome);
        String endAtual = (pet.getEndereco() != null) ? pet.getEndereco().paraFormatoArquivo() : "Não informado";
        System.out.print("Novo Endereço [numero, cidade, rua, ondeFoiEncontrado] (" + endAtual + "): ");
        String novoEnderecoStr = sc.nextLine().trim();
        System.out.print("Nova Idade (" + (pet.getIdade() != null ? pet.getIdade() : "N/I") + "): ");
        String idade = sc.nextLine().trim();
        if (!idade.isEmpty()) pet.setIdade(Double.parseDouble(idade));
        System.out.print("Novo Peso (" + (pet.getPesoAproximado() != null ? pet.getPesoAproximado() : "N/I") + "): ");
        String peso = sc.nextLine().trim();
        if (!peso.isEmpty()) pet.setPesoAproximado(Double.parseDouble(peso));
        System.out.print("Nova Raça (" + (pet.getRaca() != null ? pet.getRaca() : "N/I") + "): ");
        String raca = sc.nextLine().trim();
        if (!raca.isEmpty()) pet.setRaca(raca);
        petServico.atualizarPet(pet, novoEnderecoStr);
        System.out.println("\nPet alterado com sucesso!\n");
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