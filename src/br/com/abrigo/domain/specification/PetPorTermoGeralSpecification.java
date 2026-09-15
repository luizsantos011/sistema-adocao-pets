package br.com.abrigo.domain.specification;

import br.com.abrigo.domain.models.Pet;

public class PetPorTermoGeralSpecification implements Specification<Pet> {
    private final String termo;

    public PetPorTermoGeralSpecification(String termo) {
        this.termo = termo != null ? termo.trim().toLowerCase() : "";
    }

    @Override
    public boolean ehSatisfeitoPor(Pet pet) {
        if (termo.isEmpty()) return true;
        if (pet == null) return false;

        String nome = pet.getNome() != null ? pet.getNome().toLowerCase() : "";
        String sobrenome = pet.getSobrenome() != null ? pet.getSobrenome().toLowerCase() : "";
        String raca = pet.getRaca() != null ? pet.getRaca().toLowerCase() : "";
        String idade = pet.getIdade() != null ? pet.getIdade().toString() : "";
        String peso = pet.getPesoAproximado() != null ? pet.getPesoAproximado().toString() : "";
        String endereco = pet.getEndereco() != null ? pet.getEndereco().paraFormatoArquivo().toLowerCase() : "";

        return nome.contains(termo)
                || sobrenome.contains(termo)
                || raca.contains(termo)
                || idade.contains(termo)
                || peso.contains(termo)
                || endereco.contains(termo);
    }
}