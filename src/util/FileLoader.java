package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import model.MataKuliah;
import strukturdata.CustomList;
import strukturdata.Graph;

public class FileLoader {

    /**
     * Kelas penampung data input mahasiswa dari file mhs_xx.txt.
     */
    public static class DataMahasiswa {
        private String semesterBerikutnya;
        private CustomList<String> mkLulus;

        /**
         * Constructor untuk menyimpan semester berikutnya dan daftar kode MK yang sudah lulus.
         */
        public DataMahasiswa(String semesterBerikutnya, CustomList<String> mkLulus) {
            this.semesterBerikutnya = semesterBerikutnya;
            this.mkLulus = mkLulus;
        }

        /**
         * Mengembalikan target semester berikutnya ("ganjil" atau "genap").
         */
        public String getSemesterBerikutnya() {
            return semesterBerikutnya;
        }

        /**
         * Mengembalikan daftar kode mata kuliah yang telah lulus (huruf kapital).
         */
        public CustomList<String> getMkLulus() {
            return mkLulus;
        }
    }

    /**
     * Membaca file matakuliah.csv (pemisah titik koma ';') dan memasukkannya ke dalam Graph.
     */
    public static void loadMataKuliah(String filePath, Graph graph) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Lewati baris header pertama

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] kolom = line.split(";");
                if (kolom.length >= 5) {
                    String kode = kolom[0].trim().toUpperCase();
                    String nama = kolom[1].trim();
                    int sks = Integer.parseInt(kolom[2].trim());
                    int semester = Integer.parseInt(kolom[3].trim());
                    String jenisSemester = kolom[4].trim();

                    graph.addVertex(new MataKuliah(kode, nama, sks, semester, jenisSemester));
                }
            }
        }
    }

    /**
     * Membaca file prasyarat.csv (pemisah titik koma ';') dan menghubungkan edge prasyarat di Graph.
     */
    public static void loadPrasyarat(String filePath, Graph graph) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Lewati baris header pertama

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] kolom = line.split(";");
                if (kolom.length >= 2) {
                    String kodePrasyarat = kolom[0].trim().toUpperCase();
                    String kodeTujuan = kolom[1].trim().toUpperCase();
                    graph.addPrasyarat(kodePrasyarat, kodeTujuan);
                }
            }
        }
    }

    /**
     * Membaca file mhs_xx.txt berformat key=value untuk mendapatkan semester berikutnya dan MK lulus.
     */
    public static DataMahasiswa loadMahasiswa(String filePath) throws IOException {
        String semesterBerikutnya = "";
        CustomList<String> mkLulus = new CustomList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || !line.contains("=")) continue;

                String[] parts = line.split("=", 2);
                String key = parts[0].trim().toLowerCase();
                String value = parts[1].trim();

                if (key.equals("semester_berikutnya")) {
                    semesterBerikutnya = value;
                } else if (key.equals("lulus") && !value.isEmpty()) {
                    String[] daftarKode = value.split(",");
                    for (String kode : daftarKode) {
                        String bersih = kode.trim().toUpperCase();
                        if (!bersih.isEmpty()) {
                            mkLulus.addUnique(bersih);
                        }
                    }
                }
            }
        }

        return new DataMahasiswa(semesterBerikutnya, mkLulus);
    }
}
