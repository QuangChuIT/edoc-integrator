package com.bkav.edoc.converter.entity;

import java.io.Serializable;
import java.util.List;

public class Medicalbill implements Serializable {
    private String date;
    private String doctorName;
    private String doctorId;
    private List<String> medicineInfo;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public List<String> getMedicineInfo() {
        return medicineInfo;
    }

    public void setMedicineInfo(List<String> medicineInfo) {
        this.medicineInfo = medicineInfo;
    }
}
