package org.ith.model;

import java.util.Objects;

public class Contribuyente {

    private String ruc;
    private String razonSocial;
    private Integer dv;
    private Integer doc;

    public Contribuyente() {
    }

    public Contribuyente(String ruc, String razonSocial, Integer dv, Integer doc) {
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.dv = dv;
        this.doc = doc;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public Integer getDoc() {
        return doc;
    }

    public void setDoc(Integer doc) {
        this.doc = doc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Integer getDv() {
        return dv;
    }

    public void setDv(Integer dv) {
        this.dv = dv;
    }

    @Override
    public String toString() {
        return "Contribuyente{" +
                "ruc='" + ruc + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", dv=" + dv +
                ", doc=" + doc +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contribuyente that)) return false;

        if (!Objects.equals(ruc, that.ruc)) return false;
        if (!Objects.equals(razonSocial, that.razonSocial)) return false;
        if (!Objects.equals(dv, that.dv)) return false;
        return Objects.equals(doc, that.doc);
    }

    @Override
    public int hashCode() {
        int result = ruc != null ? ruc.hashCode() : 0;
        result = 31 * result + (razonSocial != null ? razonSocial.hashCode() : 0);
        result = 31 * result + (dv != null ? dv.hashCode() : 0);
        result = 31 * result + (doc != null ? doc.hashCode() : 0);
        return result;
    }

}
