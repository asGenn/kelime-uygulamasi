package com.example.kelime_uygulamasi.repository;

import java.util.List;

public class deneme {
    private String kelime;
    private String kelimeAnlam;

    public deneme(String kelime, String kelimeAnlam) {
        this.kelime = kelime;
        this.kelimeAnlam = kelimeAnlam;
    }

    public String getKelime() {
        return kelime;
    }

    public void setKelime(String kelime) {
        this.kelime = kelime;
    }

    public String getKelimeAnlam() {
        return kelimeAnlam;
    }

    public void setKelimeAnlam(String kelimeAnlam) {
        this.kelimeAnlam = kelimeAnlam;
    }
}
