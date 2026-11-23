import java.io.IOException;
import service.RekomendasiService;
import strukturData.Graph;
import util.FileLoader;
import util.FileLoader.DataMahasiswa;

public class main{

    /**
     * Method utama untuk memuat ketiga file input dan menjalankan sistem rekomendasi MK.
     */
    public static void main(String[] args) {
        String pathMataKuliah = "data/matakuliah.csv";
        String pathPrasyarat = "data/prasyarat.csv";
        // Default menggunakan mhs_01.txt, atau bisa diganti lewat argumen terminal
        String pathMahasiswa = (args.length > 0) ? args[0] : "data/mhs_01.txt";

        Graph graph = new Graph();

        try {
            FileLoader.loadMataKuliah(pathMataKuliah, graph);
            FileLoader.loadPrasyarat(pathPrasyarat, graph);
            DataMahasiswa dataMhs = FileLoader.loadMahasiswa(pathMahasiswa);

            System.out.println("File Mahasiswa      : " + pathMahasiswa);
            System.out.println("Semester Berikutnya : " + dataMhs.getSemesterBerikutnya());
            System.out.println("============================================================");

            RekomendasiService service = new RekomendasiService(graph);
            service.tampilkanRekomendasi(dataMhs);

        } catch (IOException e) {
            System.err.println("Gagal membaca file input: " + e.getMessage());
        }
    }
}