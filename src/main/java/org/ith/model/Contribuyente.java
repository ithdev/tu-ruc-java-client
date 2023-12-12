package org.ith.model;

import java.util.Objects;

public class Contribuyente {

    private Integer ruc;
    private String razonSocial;
    private Integer dv;

    public Contribuyente() {
    }

    public Contribuyente(Integer ruc, String razonSocial, Integer dv) {
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.dv = dv;
    }

    public Integer getRuc() {
        return ruc;
    }

    public void setRuc(Integer ruc) {
        this.ruc = ruc;
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
                "ruc=" + ruc +
                ", razonSocial='" + razonSocial + '\'' +
                ", dv=" + dv +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contribuyente that)) return false;

        if (!Objects.equals(ruc, that.ruc)) return false;
        if (!Objects.equals(razonSocial, that.razonSocial)) return false;
        return Objects.equals(dv, that.dv);
    }

    @Override
    public int hashCode() {
        int result = ruc != null ? ruc.hashCode() : 0;
        result = 31 * result + (razonSocial != null ? razonSocial.hashCode() : 0);
        result = 31 * result + (dv != null ? dv.hashCode() : 0);
        return result;
    }

}
