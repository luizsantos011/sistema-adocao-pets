package br.com.abrigo.domain.specification;

import br.com.abrigo.domain.models.Pet;

public class PetPorDataCadastroSpecification implements Specification<Pet> {
    private final String termoData;

    public PetPorDataCadastroSpecification(String termoData) {
        this.termoData = termoData;
    }

    @Override
    public boolean ehSatisfeitoPor(Pet pet) {
        if (termoData == null || termoData.isBlank()) return true;
        String dataNormalizada = normalizarDataEntrada(termoData.trim());
        if (dataNormalizada == null || pet.getDataCadastro() == null) return false;
        return pet.getDataCadastro().trim().equals(dataNormalizada.trim());
    }

    private String normalizarDataEntrada(String entrada) {
        String limpo = entrada.replaceAll("[^0-9]", "");
        if (entrada.contains("/") || entrada.contains("-")) {
            String[] partes = entrada.split("[/-]");
            if (partes.length == 2) {
                int p1 = Integer.parseInt(partes[0].trim());
                int p2 = Integer.parseInt(partes[1].trim());
                if (partes[0].trim().length() <= 2 && partes[1].trim().length() == 4) {
                    return String.format("%02d/%04d", p1, p2);
                }
                if (partes[0].trim().length() == 4 && partes[1].trim().length() <= 2) {
                    return String.format("%02d/%04d", p2, p1);
                }
            }
        }
        if (limpo.length() == 6) {
            int m = Integer.parseInt(limpo.substring(0, 2));
            int a = Integer.parseInt(limpo.substring(2));
            return String.format("%02d/%04d", m, a);
        }
        return null;
    }
}