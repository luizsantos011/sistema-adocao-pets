package br.com.abrigo.domain.specification;

import br.com.abrigo.domain.models.Pet;

public class PetPorTextoSpecification implements Specification<Pet> {
    private final String termo;

    public PetPorTextoSpecification(String termo) {
        this.termo = termo;
    }

    @Override
    public boolean ehSatisfeitoPor(Pet pet) {
        if(termo == null || termo.isBlank()) return true;
        String t = termo.trim().toLowerCase();
        String dadosPet = (pet.getNome() + " " + pet.getRaca() + " " +
                (pet.getEndereco() != null ? pet.getEndereco().paraFormatoArquivo() : "") + " " +
                pet.getIdade() + " " + pet.getPesoAproximado()).toLowerCase();
        return dadosPet.contains(t);
    }
}
