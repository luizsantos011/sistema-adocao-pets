package br.com.abrigo.domain.repository;

import java.io.IOException;
import java.util.List;

public interface FormularioRepositorio {
    List<String> carregarPerguntas() throws IOException;
}
