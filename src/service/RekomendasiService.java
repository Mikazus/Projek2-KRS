package service;

import model.MataKuliah;
import strukturdata.CustomList;
import strukturdata.Graph;
import util.FileLoader.DataMahasiswa;

public class RekomendasiService {
    private Graph graph;

    // Constructor untuk menghubungkan service dengan Graph mata kuliah
    public RekomendasiService(Graph graph) {
        this.graph = graph;
    }

    // Mengecek daftar seluruh MK wajib (selain SKR) yang belum lulus
    // Digunakan untuk memvalidasi persyaratan khusus mata kuliah Skripsi (SKR)
    private CustomList<String> getMkWajibBelumLulus(CustomList<String> mkLulus) {
        CustomList<String> sisaWajib = new CustomList<>();
        CustomList<Graph.Vertex> semuaMk = graph.getAllVertices();

        for (int i = 0; i < semuaMk.size(); i++) {
            String kode = semuaMk.get(i).getMataKuliah().getKode().toUpperCase();
            if (!kode.equals("SKR") && !mkLulus.contains(kode)) {
                sisaWajib.add(kode);
            }
        }
        return sisaWajib;
    }

    // Memproses data mahasiswa dan mencetak daftar MK yang bisa diambil
    // beserta MK yang belum bisa diambil di semester depan beserta alasannya
    public void tampilkanRekomendasi(DataMahasiswa mhs) {
        CustomList<MataKuliah> bisaDiambil = new CustomList<>();
        CustomList<String> belumBisaDiambil = new CustomList<>();

        CustomList<Graph.Vertex> semuaMk = graph.getAllVertices();
        CustomList<String> mkLulus = mhs.getMkLulus();
        String semesterDepan = mhs.getSemesterBerikutnya();

        for (int i = 0; i < semuaMk.size(); i++) {
            MataKuliah mk = semuaMk.get(i).getMataKuliah();
            String kode = mk.getKode().toUpperCase();

            // Lewati MK yang sudah lulus atau tidak ditawarkan di semester berikutnya
            if (mkLulus.contains(kode) || !mk.isDitawarkan(semesterDepan)) {
                continue;
            }

            // Aturan khusus Skripsi (SKR): hanya bisa diambil jika semua MK wajib lainnya sudah lulus
            if (kode.equals("SKR")) {
                CustomList<String> sisaWajib = getMkWajibBelumLulus(mkLulus);
                if (sisaWajib.isEmpty()) {
                    bisaDiambil.add(mk);
                } else {
                    belumBisaDiambil.add("SKR: belum lulus semua MK wajib (" + sisaWajib.join(", ") + ")");
                }
                continue;
            }

            // Cek prasyarat reguler menggunakan DFS pada Graph
            CustomList<String> prasyaratKurang = graph.getPrasyaratBelumLulus(kode, mkLulus);
            if (prasyaratKurang.isEmpty()) {
                bisaDiambil.add(mk);
            } else {
                belumBisaDiambil.add(kode + ": belum lulus " + prasyaratKurang.join(", "));
            }
        }

        cetakHasil(bisaDiambil, belumBisaDiambil);
    }

    // Mencetak hasil rekomendasi ke konsol sesuai format yang diminta pada instruksi
    private void cetakHasil(CustomList<MataKuliah> bisaDiambil, CustomList<String> belumBisaDiambil) {
        System.out.println("Daftar MK yang bisa diambil:");
        if (bisaDiambil.isEmpty()) {
            System.out.println("- Tidak ada mata kuliah yang bisa diambil.");
        } else {
            for (int i = 0; i < bisaDiambil.size(); i++) {
                MataKuliah mk = bisaDiambil.get(i);
                System.out.printf("%d. %-5s %-42s %d SKS%n",
                        (i + 1), mk.getKode(), mk.getNama(), mk.getSks());
            }
        }

        System.out.println("\nDaftar MK yang belum bisa diambil semester depan:");
        if (belumBisaDiambil.isEmpty()) {
            System.out.println("- Semua mata kuliah yang ditawarkan sudah memenuhi prasyarat.");
        } else {
            for (int i = 0; i < belumBisaDiambil.size(); i++) {
                System.out.println(belumBisaDiambil.get(i));
            }
        }
    }
}
