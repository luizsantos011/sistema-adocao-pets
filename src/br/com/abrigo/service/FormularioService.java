package br.com.abrigo.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class FormularioService {
    private static final Path caminho = Path.of("formulario.txt");

    public FormularioService() throws IOException {}

    public List<String> carregarPerguntas() throws IOException {
        try (Stream<String> linhas = Files.lines(caminho, StandardCharsets.UTF_8)) {
            return linhas
                    .filter(line -> !line.isBlank())
                    .toList();
        }
    }
}