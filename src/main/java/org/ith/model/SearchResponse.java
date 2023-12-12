package org.ith.model;

import java.util.List;
import java.util.Objects;

public class SearchResponse {

    private List<Contribuyente> contribuyentes;
    private Integer paginas;

    public SearchResponse() {
    }

    public SearchResponse(List<Contribuyente> contribuyentes, Integer paginas) {
        this.contribuyentes = contribuyentes;
        this.paginas = paginas;
    }

    public List<Contribuyente> getContribuyentes() {
        return contribuyentes;
    }

    public void setContribuyentes(List<Contribuyente> contribuyentes) {
        this.contribuyentes = contribuyentes;
    }

    public Integer getPaginas() {
        return paginas;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "contribuyentes=" + contribuyentes +
                ", paginas=" + paginas +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchResponse that)) return false;

        if (!Objects.equals(contribuyentes, that.contribuyentes))
            return false;
        return Objects.equals(paginas, that.paginas);
    }

    @Override
    public int hashCode() {
        int result = contribuyentes != null ? contribuyentes.hashCode() : 0;
        result = 31 * result + (paginas != null ? paginas.hashCode() : 0);
        return result;
    }

}
