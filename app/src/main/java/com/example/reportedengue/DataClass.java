package com.example.reportedengue;

public class DataClass {
    private String dataTitle;
    private String dataDesc;
    private String dataLang;
    private String dataDescr;

    private String uploadDirec;

    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public String getDataLang() {
        return dataLang;
    }

    public String getDataDescr() {
        return dataDescr;
    }

    public String getUploadDirec() {
        return uploadDirec;
    }

    public DataClass(String dataTitle, String dataDesc, String dataLang, String dataDescr, String uploadDirec) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataLang = dataLang;
        this.dataDescr = dataDescr;
        this.uploadDirec = uploadDirec;
    }
}
