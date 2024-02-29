package com.example.reportedengue;

public class DataClass {
    private String dataNombre;
    private String dataApellido;
    private String dataCelular;
    private String dataEmail;
    private String uploadDirec;

    private Double uploadLat;
    private Double uploadLong;

    public String getDataNombre() {
        return dataNombre;
    }

    public String getDataApellido() {
        return dataApellido;
    }

    public String getDataCelular() {
        return dataCelular;
    }

    public String getDataEmail() {
        return dataEmail;
    }

    public String getUploadDirec() {
        return uploadDirec;
    }

    public Double getUploadLat() {
        return uploadLat;
    }

    public Double getUploadLong() {
        return uploadLong;
    }

    public DataClass(String dataNombre, String dataApellido, String dataCelular, String dataEmail, String uploadDirec, Double uploadLat, Double uploadLong) {
        this.dataNombre = dataNombre;
        this.dataApellido = dataApellido;
        this.dataCelular = dataCelular;
        this.dataEmail = dataEmail;
        this.uploadDirec = uploadDirec;
        this.uploadLat = uploadLat;
        this.uploadLong = uploadLong;
    }
}
