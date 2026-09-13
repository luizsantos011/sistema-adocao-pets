package br.com.abrigo.infrastructure;

import br.com.abrigo.domain.models.Pet;
import br.com.abrigo.domain.repository.PetRepositorio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class PetRepositorioArquivo implements PetRepositorio {
    private static final String PASTA_DESTINO = "petsCadastrados";

    public PetRepositorioArquivo() {}

    @Override
    public void salvar(Pet pet) {
        try {
            Path pasta = Path.of(PASTA_DESTINO);
            if(!Files.exists(pasta)) {
                Files.createDirectories(pasta);
            }
            String dataEHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm"));
            String nomeArquivo = String.format("%s_%s_%s.txt",
                    dataEHora,
                    pet.getNome().toUpperCase(),
                    pet.getSobrenome().toUpperCase());
            Path caminhoArquivo = pasta.resolve(nomeArquivo);
            String nomeFormatado = (pet.getNome() + (pet.getSobrenome() != null ? pet.getSobrenome() : ""))
                    .replaceAll("\\s+", "")
                    .toUpperCase();
            Files.writeString(caminhoArquivo, pet.paraFormatoArquivo(), StandardOpenOption.CREATE_NEW);
        }catch (IOException e) {
            System.out.println("Erro ao salvar o pet: " + e.getMessage());
        }
    }
}
