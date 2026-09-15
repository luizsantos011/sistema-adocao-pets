package br.com.abrigo.service;

import br.com.abrigo.domain.enums.SexoPet;
import br.com.abrigo.domain.enums.TipoPet;
import br.com.abrigo.domain.models.Endereco;
import br.com.abrigo.domain.models.Pet;
import br.com.abrigo.domain.repository.PetRepositorio;
import br.com.abrigo.domain.specification.*;

import java.util.Arrays;
import java.util.List;

public class PetServico {
    PetRepositorio petRepositorio;

    public PetServico(PetRepositorio petRepositorio) {
        this.petRepositorio = petRepositorio;
    }

    public void cadastrarPet(List<String> respostas){
        String[] nomeCompleto = extrairPartes(respostas.get(0), "\\s+");
        String nome = nomeCompleto.length > 0 ? nomeCompleto[0] : null;
        String sobrenome = nomeCompleto.length > 1
                ? String.join(" ", Arrays.copyOfRange(nomeCompleto, 1, nomeCompleto.length)) : null;
        TipoPet tipo = parseEnum(TipoPet.class, respostas.get(1));
        SexoPet sexo = parseEnum(SexoPet.class, respostas.get(2));
        Endereco endereco = parseEndereco(respostas.get(3));
        Double idade = parseIdade(respostas.get(4));
        Double pesoAproximado = parseDouble(respostas.get(5));
        String raca = respostas.get(6).isBlank() ? null : respostas.get(6);
        Pet pet = new Pet(nome, sobrenome, tipo, sexo, endereco, idade, pesoAproximado, raca);
        petRepositorio.salvar(pet);
    }

    public List<Pet> buscarPets(String tipo, String termo1, String termo2) {
        Specification<Pet> spec = new PetPorTipoSpecification(tipo)
                .and(new PetPorTermoGeralSpecification(termo1))
                .and(new PetPorTermoGeralSpecification(termo2));
        return petRepositorio.listarTodos().stream()
                .filter(spec::ehSatisfeitoPor)
                .toList();
    }

    public void atualizarPet(Pet pet, String novoEnderecoStr) {
        if (pet == null) {
            throw new IllegalArgumentException("O pet para atualização não pode ser nulo.");
        }
        if (novoEnderecoStr != null && !novoEnderecoStr.isBlank()) {
            pet.setEndereco(parseEndereco(novoEnderecoStr));
        }
        petRepositorio.atualizar(pet);
    }

    private String[] extrairPartes(String entrada, String divisor) {
        if (entrada == null || entrada.isBlank()) return new String[0];
        return entrada.trim().split(divisor);
    }

    private Endereco parseEndereco(String entrada) {
        if (entrada == null || entrada.isBlank()) return null;
        String[] partes = extrairPartes(entrada, ",");
        Integer numero = (partes.length > 0 && partes[0].trim().matches("\\d+")) ? Integer.parseInt(partes[0].trim()) : null;
        String cidade = partes.length > 1 && !partes[1].trim().isBlank() ? partes[1].trim() : null;
        String rua = partes.length > 2 && !partes[2].trim().isBlank() ? partes[2].trim() : null;
        String ondeFoiEncontrado = partes.length > 3 && !partes[3].trim().isBlank() ? partes[3].trim() : null;
        return new Endereco(numero, cidade, rua, ondeFoiEncontrado);
    }

    private <T extends Enum<T>> T parseEnum(Class<T> enumClass, String valor) {
        try {
            return Enum.valueOf(enumClass, valor.trim().toUpperCase());
        }catch (Exception e) {
            return null;
        }
    }

    private Double parseIdade(String entrada) {
        if (entrada == null || entrada.isBlank()) return null;
        String limpa = entrada.toLowerCase().trim().replaceAll(",", ".");
        if(limpa.contains("mes") || limpa.contains("mês")) {
            String apenasNumeros = limpa.replaceAll("[^0-9]", "");
            return apenasNumeros.isBlank() ? null : Double.parseDouble(apenasNumeros)/12;
        }
        String apenasNumero = limpa.replaceAll("[^0-9]", "");
        return apenasNumero.isBlank() ? null : Double.parseDouble(apenasNumero);
    }

    private Double parseDouble(String entrada) {
        if (entrada == null || entrada.isBlank()) return null;
        try {
            return Double.parseDouble(entrada.trim().replaceAll(",", "."));
        }catch (Exception e) {
            return null;
        }
    }
}