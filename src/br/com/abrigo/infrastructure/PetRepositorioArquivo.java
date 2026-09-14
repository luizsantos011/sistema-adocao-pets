package br.com.abrigo.infrastructure;

import br.com.abrigo.domain.enums.SexoPet;
import br.com.abrigo.domain.enums.TipoPet;
import br.com.abrigo.domain.models.Endereco;
import br.com.abrigo.domain.models.Pet;
import br.com.abrigo.domain.repository.PetRepositorio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PetRepositorioArquivo implements PetRepositorio {
    private static final String PASTA_DESTINO = "petsCadastrados";

    @Override
    public void salvar(Pet pet) {
        try {
            Path pasta = Path.of(PASTA_DESTINO);
            if (!Files.exists(pasta)) {
                Files.createDirectories(pasta);
            }
            String dataEHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
            String nomeCompleto = pet.getNome() + (pet.getSobrenome() != null ? pet.getSobrenome() : "");
            String nomeFormatado = nomeCompleto.replaceAll("\\s+", "").toUpperCase();
            String nomeArquivo = String.format("%s-%s.TXT", dataEHora, nomeFormatado);
            Path caminhoArquivo = pasta.resolve(nomeArquivo);
            Files.writeString(caminhoArquivo, pet.paraFormatoArquivo(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o pet: " + e.getMessage());
        }
    }

    @Override
    public List<Pet> listarTodos() {
        List<Pet> pets = new ArrayList<>();
        Path pasta = Path.of(PASTA_DESTINO);
        if (!Files.exists(pasta)) return pets;
        try (var stream = Files.list(pasta)) {
            stream.filter(p -> p.toString().toUpperCase().endsWith(".TXT"))
                    .forEach(caminho -> {
                        try {
                            List<String> linhas = Files.readAllLines(caminho);
                            if (linhas.size() >= 7) {
                                Pet pet = converterLinhasParaPet(linhas);
                                if (pet != null) {
                                    pets.add(pet);
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Erro ao carregar arquivo: " + caminho.getFileName());
                        }
                    });
        } catch (IOException e) {
            System.err.println("Erro ao ler diretório de pets: " + e.getMessage());
        }
        return pets;
    }

    private Pet converterLinhasParaPet(List<String> linhas) {
        try {
            List<String> limpas = linhas.stream()
                    .map(l -> l.replaceFirst("^\\d+\\s*-\\s*", "").trim())
                    .toList();
            String nomeBruto = limpas.get(0).replaceAll("(?i)não informado|n/i", "").trim();
            String nome = nomeBruto.isEmpty() ? "Não informado" : nomeBruto;
            String sobrenome = null;
            TipoPet tipo = parseEnum(TipoPet.class, limpas.get(1));
            SexoPet sexo = parseEnum(SexoPet.class, limpas.get(2));
            Endereco endereco = parseEndereco(limpas.get(3));
            Double idade = parseIdade(limpas.get(4));
            Double peso = parseDouble(limpas.get(5));
            String raca = limpas.get(6).isBlank() || limpas.get(6).equalsIgnoreCase("não informado") ? "N/I" : limpas.get(6);
            return new Pet(nome, sobrenome, tipo, sexo, endereco, idade, peso, raca);
        } catch (Exception e) {
            return null;
        }
    }

    private Endereco parseEndereco(String entrada) {
        if (entrada == null || entrada.isBlank()) return null;
        String[] partes = entrada.trim().split(",");
        String cidade = partes.length > 0 && !partes[0].trim().isBlank() ? partes[0].trim() : null;
        Integer numero = (partes.length > 1 && partes[1].trim().matches("\\d+")) ? Integer.parseInt(partes[1].trim()) : null;
        String rua = partes.length > 2 && !partes[2].trim().isBlank() ? partes[2].trim() : null;
        String onde = partes.length > 3 && !partes[3].trim().isBlank() ? partes[3].trim() : null;
        return new Endereco(numero, cidade, rua, onde);
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumClass, String valor) {
        try {
            return Enum.valueOf(enumClass, valor.trim().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private Double parseIdade(String entrada) {
        if (entrada == null || entrada.isBlank()) return null;
        String limpa = entrada.toLowerCase().trim().replaceAll(",", ".");
        double valorCalculado;
        if (limpa.contains("mes") || limpa.contains("mês")) {
            String apenasNumeros = limpa.replaceAll("[^0-9.]", "");
            if (apenasNumeros.isBlank()) return null;
            valorCalculado = Double.parseDouble(apenasNumeros) / 12.0;
        } else {
            String apenasNumero = limpa.replaceAll("[^0-9.]", "");
            if (apenasNumero.isBlank()) return null;
            valorCalculado = Double.parseDouble(apenasNumero);
        }
        return Math.round(valorCalculado * 10.0) / 10.0;
    }

    private Double parseDouble(String entrada) {
        if (entrada == null || entrada.isBlank()) return null;
        String apenasNumero = entrada.trim().replaceAll(",", ".").replaceAll("[^0-9.]", "");
        try {
            if (apenasNumero.isBlank()) return null;
            double valor = Double.parseDouble(apenasNumero);
            return Math.round(valor * 10.0) / 10.0;
        } catch (Exception e) {
            return null;
        }
    }
}