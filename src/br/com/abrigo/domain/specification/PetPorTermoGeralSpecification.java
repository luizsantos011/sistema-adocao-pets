package br.com.abrigo.domain.specification;

import br.com.abrigo.domain.models.Pet;

public class PetPorTermoGeralSpecification implements Specification<Pet> {
    private final String termo;

    public PetPorTermoGeralSpecification(String termo) {
        this.termo = termo;
    }

    @Override
    public boolean ehSatisfeitoPor(Pet pet) {
        if (termo == null || termo.isBlank()) return true;
        boolean bateuTexto = new PetPorTextoSpecification(termo).ehSatisfeitoPor(pet);
        boolean bateuData = new PetPorDataCadastroSpecification(termo).ehSatisfeitoPor(pet);
        return bateuTexto || bateuData;
    }
}