package br.com.abrigo.domain.models;

import br.com.abrigo.domain.enums.SexoPet;
import br.com.abrigo.domain.enums.TipoPet;

import java.text.Normalizer;
import java.util.regex.*;

public class Pet{
    private String nome;
    private String sobrenome;
    private SexoPet sexo;
    private TipoPet tipo;
    private Endereco endereco;
    private Double idade;
    private Double pesoAproximado;
    private String raca;
    private static final String NAO_INFORMADO = "Não informado";

    public Pet(String nome, String sobrenome, TipoPet tipoPet, SexoPet sexoPet, Endereco endereco,
               Double idade, Double pesoAproximado, String raca) {
        this.nome = validarNome(nome);
        this.sobrenome = validarSobrenome(sobrenome);
        this.sexo = sexoPet;
        this.tipo = tipoPet;
        this.endereco = endereco;
        this.idade = validarIdade(idade);
        this.pesoAproximado = validarPeso(pesoAproximado);
        this.raca = validarRaca(raca);
    }

    private String validarNome(String nome) {
        if (nome == null || nome.isBlank()) return NAO_INFORMADO;
        String nomeNormalizado = Normalizer.normalize(nome, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        if (!nomeNormalizado.matches("^[A-Za-z\\s]+$")) throw new IllegalArgumentException("Nome inválido");
        return nome;
    }
    private String validarSobrenome(String sobrenome) {
        if (sobrenome == null || sobrenome.isBlank()) return NAO_INFORMADO;
        String sobrenomeNormalizado = Normalizer.normalize(sobrenome, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        if (!sobrenomeNormalizado.matches("^[A-Za-z\\s]+$")) throw new IllegalArgumentException("Sobrenome inválido");
        return sobrenome;
    }
    private Double validarPeso(Double peso) {
        if (peso == null) return null;
        if (peso.isNaN()) throw new IllegalArgumentException("Peso inválido");
        if (peso < 0.5 || peso > 60) throw new IllegalArgumentException("Peso inválido");
        return peso;
    }
    private Double validarIdade(Double idade) {
        if (idade == null) return null;
        if (idade.isNaN()) throw new IllegalArgumentException("Idade inválida");
        if (idade < 0 || idade > 20) throw new IllegalArgumentException("Idade inválida");
        return idade;
    }
    private String validarRaca(String raca) {
        if (raca == null || raca.isBlank()) return NAO_INFORMADO;
        String racaNormalizada = Normalizer.normalize(raca, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        if (!racaNormalizada.matches("^[A-Za-z\\s]+$")) throw new IllegalArgumentException("Raça inválida");
        return raca;
    }

    public String getNome() {
        return nome;
    }

    public SexoPet getSexo() {
        return sexo;
    }

    public TipoPet getTipo() {
        return tipo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public String getIdade() {
        return (idade == null) ? NAO_INFORMADO : idade.toString();
    }

    public String getPesoAproximado() {
        return (pesoAproximado == null) ? NAO_INFORMADO : pesoAproximado.toString();
    }

    public String getRaca() {
        return raca;
    }
}
