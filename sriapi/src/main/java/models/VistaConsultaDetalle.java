package models;

public class VistaConsultaDetalle {
    private String numeroRuc;
    private String razonSocial;
    private String estadoContribuyenteRuc;
    private String actividadEconomicaPrincipal;
    private String tipoContribuyente;
    private String regimen;
    private String categoria;

    // Getters y setters
    // Constructor

    public VistaConsultaDetalle(String numeroRuc, String razonSocial, String estadoContribuyenteRuc, String actividadEconomicaPrincipal, String tipoContribuyente, String regimen, String categoria) {
        this.numeroRuc = numeroRuc;
        this.razonSocial = razonSocial;
        this.estadoContribuyenteRuc = estadoContribuyenteRuc;
        this.actividadEconomicaPrincipal = actividadEconomicaPrincipal;
        this.tipoContribuyente = tipoContribuyente;
        this.regimen = regimen;
        this.categoria = categoria;
    }

    public String getNumeroRuc() {
        return numeroRuc;
    }

    public void setNumeroRuc(String numeroRuc) {
        this.numeroRuc = numeroRuc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getEstadoContribuyenteRuc() {
        return estadoContribuyenteRuc;
    }

    public void setEstadoContribuyenteRuc(String estadoContribuyenteRuc) {
        this.estadoContribuyenteRuc = estadoContribuyenteRuc;
    }

    public String getActividadEconomicaPrincipal() {
        return actividadEconomicaPrincipal;
    }

    public void setActividadEconomicaPrincipal(String actividadEconomicaPrincipal) {
        this.actividadEconomicaPrincipal = actividadEconomicaPrincipal;
    }

    public String getTipoContribuyente() {
        return tipoContribuyente;
    }

    public void setTipoContribuyente(String tipoContribuyente) {
        this.tipoContribuyente = tipoContribuyente;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
