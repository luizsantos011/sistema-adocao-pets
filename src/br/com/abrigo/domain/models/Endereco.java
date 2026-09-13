package br.com.abrigo.domain.models;

public class Endereco {
    private Integer numero;
    private String cidade;
    private String rua;
    private static final String NAO_INFORMADO = "Não informado";

    public Endereco(Integer numero, String cidade, String rua) {
        this.numero = numero;
        this.cidade = cidade;
        this.rua = rua;
    }

    public String getNumero() {
        return (numero == null)  ? NAO_INFORMADO : numero.toString();
    }

    public String getCidade() {
        return cidade;
    }

    public String getRua() {
        return rua;
    }
}
