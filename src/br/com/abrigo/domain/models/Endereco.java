package br.com.abrigo.domain.models;

public class Endereco {
    private Integer numero;
    private String cidade;
    private String rua;
    private String ondeFoiEncontrado;
    private static final String NAO_INFORMADO = "Não informado";

    public Endereco(Integer numero, String cidade, String rua,  String ondeFoiEncontrado) {
        this.numero = numero;
        this.cidade = cidade;
        this.rua = rua;
        this.ondeFoiEncontrado = ondeFoiEncontrado;
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

    public String getOndeFoiEncontrado() {
        return ondeFoiEncontrado;
    }

    public String toString() {
        return numero+cidade+rua+ondeFoiEncontrado;
    }

    public String paraFormatoArquivo() {
        return String.format("%s, %s, %s",
                rua != null ? rua : NAO_INFORMADO,
                getNumero(),
                cidade != null ? cidade : NAO_INFORMADO);
    }
}
