package br.com.abrigo.domain.specification;

import br.com.abrigo.domain.models.Pet;

public class PetPorTipoSpecification implements Specification<Pet> {
    private final String tipo;

    public PetPorTipoSpecification(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean ehSatisfeitoPor(Pet pet) {
        return pet.getTipo() != null && pet.getTipo().name().equalsIgnoreCase(tipo);
    }
}
