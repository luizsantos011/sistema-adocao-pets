package br.com.abrigo.domain.repository;

import br.com.abrigo.domain.models.Pet;

import java.util.List;

public interface PetRepositorio {
    void salvar(Pet pet);
    List<Pet> listarTodos();
    void atualizar(Pet pet);
}
