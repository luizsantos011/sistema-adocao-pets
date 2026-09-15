package br.com.abrigo.domain.specification;

public interface Specification<T> {
    boolean ehSatisfeitoPor (T item);

    default Specification<T> and(Specification<T> outro) {
        if(outro == null) return this;
        return item -> this.ehSatisfeitoPor(item) && outro.ehSatisfeitoPor(item);
    }
}