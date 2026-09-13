package br.com.abrigo.infrastructure;

import br.com.abrigo.domain.models.Pet;
import br.com.abrigo.domain.repository.PetRepositorio;

public class PetRepositorioArquivo implements PetRepositorio {

    public PetRepositorioArquivo() {}

    @Override
    public void salvar(Pet pet) {
    }
}
