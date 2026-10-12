package org.ith.model;

import java.util.Objects;

public class Contribuyente {

    private Integer doc;
    private String razonSocial;
    private Integer dv;
    private String ruc;
    private String estado;
    private Boolean esPersonaJuridica;
    private Boolean esEntidadPublica;

    public Contribuyente() {}

    public Contribuyente(
            Integer doc,
            String razonSocial,
            Integer dv,
            String ruc,
            String estado,
            Boolean esPersonaJuridica,
            Boolean esEntidadPublica) {
        this.doc = doc;
        this.razonSocial = razonSocial;
        this.dv = dv;
        this.ruc = ruc;
        this.estado = estado;
        this.esPersonaJuridica = esPersonaJuridica;
        this.esEntidadPublica = esEntidadPublica;
    }

    public Integer getDoc() {
        return doc;
    }

    public void setDoc(Integer doc) {
        this.doc = doc;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getEsPersonaJuridica() {
        return esPersonaJuridica;
    }

    public void setEsPersonaJuridica(Boolean esPersonaJuridica) {
        this.esPersonaJuridica = esPersonaJuridica;
    }

    public Boolean getEsEntidadPublica() {
        return esEntidadPublica;
    }

    public void setEsEntidadPublica(Boolean esEntidadPublica) {
        this.esEntidadPublica = esEntidadPublica;
    }

    @Override
    public String toString() {
        return "Contribuyente{" + "doc="
                + doc + ", razonSocial='"
                + razonSocial + '\'' + ", dv="
                + dv + ", ruc='"
                + ruc + '\'' + ", estado='"
                + estado + '\'' + ", esPersonaJuridica="
                + esPersonaJuridica + ", esEntidadPublica="
                + esEntidadPublica + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contribuyente that)) return false;

        return Objects.equals(doc, that.doc)
                && Objects.equals(razonSocial, that.razonSocial)
                && Objects.equals(dv, that.dv)
                && Objects.equals(ruc, that.ruc)
                && Objects.equals(estado, that.estado)
                && Objects.equals(esPersonaJuridica, that.esPersonaJuridica)
                && Objects.equals(esEntidadPublica, that.esEntidadPublica);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doc, razonSocial, dv, ruc, estado, esPersonaJuridica, esEntidadPublica);
    }
}
