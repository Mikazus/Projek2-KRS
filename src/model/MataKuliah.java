package model;

public class MataKuliah {
    private String kode;
    private String nama;
    private int sks;
    private int semester;
    private String jenisSemester; // "Ganjil", "Genap", atau "Keduanya"

    // Constructor untuk membuat objek MataKuliah baru
    public MataKuliah(String kode, String nama, int sks, int semester, String jenisSemester) {
        this.kode = kode;
        this.nama = nama;
        this.sks = sks;
        this.semester = semester;
        this.jenisSemester = jenisSemester;
    }

    // Mengembalikan kode mata kuliah (contoh: "ASD")
    public String getKode() {
        return kode;
    }

    // Mengembalikan nama lengkap mata kuliah
    public String getNama() {
        return nama;
    }

    // Mengembalikan bobot SKS mata kuliah
    public int getSks() {
        return sks;
    }

    // Mengembalikan semester rekomendasi mata kuliah
    public int getSemester() {
        return semester;
    }

    // Mengembalikan jenis penawaran semester ("Ganjil", "Genap", atau "Keduanya")
    public String getJenisSemester() {
        return jenisSemester;
    }

    // Mengecek apakah mata kuliah ini ditawarkan pada semester berikutnya
    // MK dengan jenis "Keduanya" (seperti Skripsi) selalu ditawarkan di Ganjil maupun Genap
    public boolean isDitawarkan(String semesterBerikutnya) {
        return jenisSemester.equalsIgnoreCase("Keduanya")
                || jenisSemester.equalsIgnoreCase(semesterBerikutnya);
    }
}