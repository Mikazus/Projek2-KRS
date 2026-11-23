package strukturData;

import model.MataKuliah;

public class Graph {

    /**
     * Merepresentasikan satu simpul (vertex) mata kuliah beserta daftar prasyarat langsungnya.
     */
    public static class Vertex {
        private MataKuliah mataKuliah;
        private CustomList<Vertex> prasyaratList;

        /**
         * Constructor vertex untuk satu objek MataKuliah.
         */
        public Vertex(MataKuliah mataKuliah) {
            this.mataKuliah = mataKuliah;
            this.prasyaratList = new CustomList<>();
        }

        /**
         * Mengembalikan objek MataKuliah pada vertex ini.
         */
        public MataKuliah getMataKuliah() {
            return mataKuliah;
        }

        /**
         * Mengembalikan daftar vertex prasyarat langsung dari mata kuliah ini.
         */
        public CustomList<Vertex> getPrasyaratList() {
            return prasyaratList;
        }
    }

    private CustomList<Vertex> vertices;

    /**
     * Constructor untuk menginisialisasi graph kosong.
     */
    public Graph() {
        this.vertices = new CustomList<>();
    }

    /**
     * Menambahkan mata kuliah baru sebagai vertex ke dalam graph.
     */
    public void addVertex(MataKuliah mk) {
        if (findVertex(mk.getKode()) == null) {
            vertices.add(new Vertex(mk));
        }
    }

    /**
     * Mencari vertex berdasarkan kode mata kuliah (tidak membedakan huruf besar/kecil).
     */
    public Vertex findVertex(String kode) {
        for (int i = 0; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            if (v.getMataKuliah().getKode().equalsIgnoreCase(kode)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Menambahkan sisi berarah (edge) bahwa kodeTujuan membutuhkan kodePrasyarat.
     */
    public void addPrasyarat(String kodePrasyarat, String kodeTujuan) {
        Vertex vPrasyarat = findVertex(kodePrasyarat);
        Vertex vTujuan = findVertex(kodeTujuan);

        if (vPrasyarat != null && vTujuan != null) {
            vTujuan.getPrasyaratList().addUnique(vPrasyarat);
        }
    }

    /**
     * Mengembalikan seluruh vertex mata kuliah yang tersimpan di dalam graph.
     */
    public CustomList<Vertex> getAllVertices() {
        return vertices;
    }

    /**
     * Mengumpulkan seluruh kode prasyarat (langsung maupun berantai) yang belum lulus
     * untuk suatu mata kuliah tertentu menggunakan penelusuran DFS.
     */
    public CustomList<String> getPrasyaratBelumLulus(String kodeMk, CustomList<String> mkLulus) {
        CustomList<String> belumLulus = new CustomList<>();
        CustomList<String> visited = new CustomList<>();

        Vertex target = findVertex(kodeMk);
        if (target != null) {
            dfsPrasyarat(target, mkLulus, visited, belumLulus);
        }
        return belumLulus;
    }

    /**
     * Helper rekursif (Post-Order DFS) untuk menelusuri rantai prasyarat dari yang paling dasar.
     * Contoh: PAW -> IMK -> SO menghasilkan urutan "SO, IMK" jika keduanya belum lulus.
     */
    private void dfsPrasyarat(Vertex current, CustomList<String> mkLulus,
                              CustomList<String> visited, CustomList<String> belumLulus) {
        CustomList<Vertex> prasyarat = current.getPrasyaratList();

        for (int i = 0; i < prasyarat.size(); i++) {
            Vertex p = prasyarat.get(i);
            String kodeP = p.getMataKuliah().getKode().toUpperCase();

            // Jika prasyarat belum pernah dikunjungi dan belum lulus, telusuri lebih dalam dulu
            if (!visited.contains(kodeP) && !mkLulus.contains(kodeP)) {
                visited.add(kodeP);
                dfsPrasyarat(p, mkLulus, visited, belumLulus);
                belumLulus.addUnique(kodeP);
            }
        }
    }
}