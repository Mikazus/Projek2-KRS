# Proyek 2 Algoritma dan Struktur Data — Sistem Rekomendasi Pengambilan Mata Kuliah

Sistem berbasis Java untuk memberikan rekomendasi pengambilan mata kuliah (MK) wajib pada semester berikutnya berdasarkan riwayat kelulusan mahasiswa, jadwal penawaran semester (Ganjil/Genap), dan graf prasyarat mata kuliah sesuai buku panduan akademik FILKOM.

---

## Anggota Kelompok

| No | Nama Lengkap | NIM | Fokus Tanggung Jawab (Rekomendasi) |
| :-: | :--- | :--- | :--- |
| 1 | **Mikhael Lucien Then** | `255150201111036` | Struktur Data (`src/strukturdata/`) — `CustomList` & `Graph` |
| 2 | **Michael Juanio Patrick** | `255150201111037` | Model & Utilitas (`src/model/`, `src/util/`) — `MataKuliah` & `FileLoader` |
| 3 | **Almer Firdaus Widjokongko** | `255150201111039` | Logika & Antarmuka (`src/service/`, `src/Main.java`) — `RekomendasiService` & Output |

*(Catatan: Penyusunan dokumen laporan `docs/Laporan_Proyek2_ASD.pdf` dan pengujian bersama dilakukan secara kolaboratif oleh seluruh anggota kelompok).*

---

## 1. Ringkasan Produk (PRD Overview)

### 1.1 Tujuan
Membangun program CLI (*Command-Line Interface*) yang membaca data mata kuliah, relasi prasyarat, serta riwayat kelulusan mahasiswa, lalu mengelompokkan mata kuliah yang ditawarkan di semester berikutnya menjadi:
1. **Daftar MK yang bisa diambil** pada semester depan.
2. **Daftar MK yang belum bisa diambil** pada semester depan beserta alasan prasyarat yang belum lulus.

### 1.2 Ruang Lingkup
* Hanya mencakup **Mata Kuliah Wajib** sesuai peta kurikulum FILKOM.
* Menggunakan **struktur data buatan sendiri** (*custom data structures*) tanpa bergantung pada *built-in collection/graph library* bawaan Java.

---

## 2. Kebutuhan Fungsional (Functional Requirements)

| ID | Fitur | Deskripsi Kebutuhan |
| :--- | :--- | :--- |
| **FR-01** | Pembacaan Data MK | Program membaca `matakuliah.csv` berpemisah titik koma (`;`) yang memuat: `kode MK`, `nama MK`, `sks`, `semester`, dan `ganjil/genap` (`Ganjil`, `Genap`, atau `Keduanya`). |
| **FR-02** | Pembacaan Prasyarat | Program membaca `prasyarat.csv` berpemisah titik koma (`;`) yang memetakan mata kuliah tujuan beserta mata kuliah prasyaratnya ke dalam struktur *Directed Graph*. |
| **FR-03** | Pembacaan Profil Mahasiswa | Program membaca file `mhs_xx.txt` berformat `key=value` untuk mengambil `semester_berikutnya` (`ganjil`/`genap`, *case-insensitive*) dan daftar kode MK `lulus` yang dipisahkan koma (`,`). |
| **FR-04** | Filter Penawaran Semester | Program hanya mengevaluasi MK yang **belum lulus** dan **ditawarkan pada semester berikutnya** (termasuk MK dengan nilai penawaran `Keduanya`). |
| **FR-05** | Validasi Prasyarat Bertingkat | Sebuah MK dapat diambil jika seluruh prasyarat langsung maupun prasyarat tidak langsung (*transitive prerequisites*) telah lulus. Jika belum, program wajib melacak dan menampilkan seluruh prasyarat yang belum lulus sebagai alasan. |
| **FR-06** | Aturan Khusus Skripsi (`SKR`) | Mata kuliah **Skripsi** hanya dapat diambil apabila **seluruh MK wajib lainnya** telah lulus, terlepas dari prasyarat graf standar. |
| **FR-07** | Tampilan Output Terstruktur | Menampilkan dua bagian output di konsol: daftar MK yang bisa diambil (bernomor, kode, nama, SKS) dan daftar MK yang belum bisa diambil beserta daftar kode prasyarat yang belum lulus. |

---

## 3. Kebutuhan Non-Fungsional & Batasan Teknis

* **Bahasa Pemrograman:** Java (JDK 17+ direkomendasikan).
* **Larangan Library Bawaan:** Dilarang menggunakan struktur data bawaan Java seperti `TreeMap`, `TreeSet`, `HashMap`, `ArrayList`, `LinkedList`, maupun library graf eksternal. Struktur data list dan graph **harus diimplementasikan sendiri dari nol**.
* **Portabilitas Path:** Pembacaan file wajib menggunakan *relative path* (misal: `data/matakuliah.csv`) agar dapat dijalankan di perangkat mana pun tanpa mengubah kode.
* **Deliverables Repository:** Seluruh source code dan laporan PDF (maksimal 3 halaman berisi detail struktur data & algoritma) harus tersimpan rapi di repository online (GitHub/GitLab) untuk dikumpulkan melalui Brone.

---

## 4. Struktur Folder Proyek

```text
proyek2-asd-rekomendasi-mk/
├── data/                           # Berkas dataset input (.csv & .txt)
│   ├── matakuliah.csv
│   ├── prasyarat.csv
│   ├── mhs_01.txt
│   ├── mhs_02.txt
│   └── mhs_03.txt
├── src/                            # Seluruh source code Java
│   ├── model/
│   │   └── MataKuliah.java         # Entitas data mata kuliah
│   ├── strukturdata/
│   │   ├── CustomList.java         # Implementasi Linked List buatan sendiri
│   │   └── Graph.java              # Implementasi Directed Graph prasyarat
│   ├── util/
│   │   └── FileLoader.java         # Parser untuk file .csv dan .txt
│   ├── service/
│   │   └── RekomendasiService.java # Logika rekomendasi & validasi prasyarat
│   └── Main.java                   # Entry point aplikasi & format tampilan CLI
├── docs/                           # Dokumen laporan tugas
│   └── Laporan_Proyek2_ASD.pdf     # Laporan PDF maksimal 3 halaman
├── .gitignore                      # Filter file biner/IDE agar tidak masuk Git
└── README.md                       # Dokumentasi utama & PRD proyek
```

---

## 5. Spesifikasi Detail Keseluruhan File

### 5.1 Folder `data/` (Input)
* **`matakuliah.csv`**: Berisi daftar lengkap MK wajib beserta kode, nama, bobot SKS, semester acuan, dan periode penawaran (`Ganjil`/`Genap`/`Keduanya`). Pemisah kolom: `;`.
* **`prasyarat.csv`**: Berisi pasangan relasi ketergantungan antar MK. Pemisah kolom: `;`.
* **`mhs_01.txt`, `mhs_02.txt`, `mhs_03.txt`**: Berkas uji profil mahasiswa. Baris 1 memuat `semester_berikutnya=<ganjil|genap>`, baris 2 memuat `lulus=<KODE1,KODE2,...>`.

### 5.2 Folder `src/model/`
* **`MataKuliah.java`**
  * **Tujuan:** Merepresentasikan satu objek mata kuliah.
  * **Atribut:** `kode` (`String`), `nama` (`String`), `sks` (`int`), `semester` (`int`), `penawaran` (`String`).
  * **Method Utama:** Konstruktor, *getter* untuk seluruh atribut, serta `toString()` untuk mempermudah pencetakan format output.

### 5.3 Folder `src/strukturdata/`
* **`CustomList.java`**
  * **Tujuan:** Menggantikan struktur koleksi bawaan Java (`ArrayList`/`LinkedList`/`Set`) menggunakan *Singly Linked List* generik (`CustomList<T>`).
  * **Operasi Wajib:** `add(T data)`, `addUnique(T data)`, `get(int index)`, `contains(T data)`, `size()`, dan `isEmpty()`.
* **`Graph.java`**
  * **Tujuan:** Memodelkan peta kurikulum sebagai *Directed Graph* menggunakan *Adjacency List* berbasis `CustomList`.
  * **Operasi Wajib:**
    * `addVertex(MataKuliah mk)`: Menambahkan simpul MK.
    * `addPrerequisite(String kodeMK, String kodePrasyarat)`: Menambahkan sisi berarah dari MK menuju prasyaratnya.
    * `getVertex(String kodeMK)`: Mencari simpul berdasarkan kode MK.
    * `getAllPrerequisites(String kodeMK)`: Melakukan penelusuran graf (DFS/BFS) untuk mengumpulkan seluruh prasyarat langsung maupun tidak langsung dari suatu MK tanpa duplikasi.

### 5.4 Folder `src/util/`
* **`FileLoader.java`**
  * **Tujuan:** Membaca seluruh file input menggunakan `BufferedReader` / `FileReader` dan mengubahnya ke dalam objek/struktur data buatan sendiri.
  * **Method Wajib:**
    * `loadMataKuliah(String path, Graph graph)`: Parsing `matakuliah.csv` (abaikan baris header, split `;`, trim spasi).
    * `loadPrasyarat(String path, Graph graph)`: Parsing `prasyarat.csv` (abaikan baris header, split `;`) lalu mendaftarkan *edge* ke `Graph`.
    * `loadMahasiswa(String path)`: Membaca `mhs_xx.txt` untuk mengekstrak target semester berikutnya (dinormalisasi *case-insensitive*) dan `CustomList<String>` berisi daftar kode MK lulus.

### 5.5 Folder `src/service/`
* **`RekomendasiService.java`**
  * **Tujuan:** Menjalankan seluruh aturan bisnis sistem rekomendasi.
  * **Alur Logika:**
    1. Melakukan iterasi pada seluruh vertex `MataKuliah` di dalam `Graph`.
    2. Melewati MK yang sudah ada di daftar `lulus`.
    3. Memeriksa kesesuaian `penawaran` MK dengan `semester_berikutnya` (termasuk kondisi `Keduanya`).
    4. Jika kode MK adalah `SKR` (Skripsi), periksa apakah seluruh MK wajib selain `SKR` sudah ada di daftar `lulus`.
    5. Untuk MK lainnya, panggil `graph.getAllPrerequisites(kodeMK)` dan periksa prasyarat mana saja yang belum ada di daftar `lulus`.
    6. Jika prasyarat belum lulus kosong → masukkan ke daftar **Bisa Diambil**. Jika ada → masukkan ke daftar **Belum Bisa Diambil** beserta daftar kode prasyarat yang kurang.

### 5.6 `src/Main.java`
* **Tujuan:** Titik masuk (*entry point*) program.
* **Fungsi:**
  * Menentukan path file `data/matakuliah.csv`, `data/prasyarat.csv`, dan file mahasiswa (dapat menerima argumen CLI atau pilihan menu sederhana untuk `mhs_01.txt` s.d. `mhs_03.txt`).
  * Memanggil `FileLoader` untuk membangun `Graph` dan memuat data mahasiswa.
  * Memanggil `RekomendasiService` dan mencetak hasil akhir ke terminal sesuai format pada instruksi.

### 5.7 Folder `docs/` & Konfigurasi Root
* **`docs/Laporan_Proyek2_ASD.pdf`**: Laporan maksimal **3 halaman** mencakup:
  1. Detail struktur data yang dirancang (`CustomList` & `Graph` beserta kompleksitasnya).
  2. Detail algoritma yang digunakan (pembacaan data, penelusuran prasyarat DFS/BFS, aturan Skripsi).
  3. Cuplikan hasil uji coba pada `mhs_01.txt`, `mhs_02.txt`, dan `mhs_03.txt`.
* **`.gitignore`**: Mengabaikan folder hasil kompilasi (`bin/`, `out/`), file biner (`*.class`), serta konfigurasi lokal IDE (`.vscode/`, `.idea/`, `.DS_Store`).

---

## 6. Ekspektasi Format Output

Contoh keluaran program untuk kasus uji `mhs_01.txt`:

```text
Daftar MK yang bisa diambil:
1. SO   - Sistem Operasi (5 SKS)
2. AL   - Aljabar Linear (2 SKS)
3. PLAN - Pemrograman Lanjut (5 SKS)
4. BING - Bahasa Inggris (2 SKS)
5. BD   - Basis Data (3 SKS)
6. EP   - Etika Profesi (2 SKS)
7. MD   - Matematika Diskret (2 SKS)
8. KWN  - Kewarganegaraan (2 SKS)
9. KWU  - Kewirausahaan (2 SKS)
10. PANC - Pancasila (2 SKS)

Daftar MK yang belum bisa diambil semester depan:
PAW  : belum lulus SO, IMK
DAA  : belum lulus ASD
KI   : belum lulus SO, JK
KALJ : belum lulus AL, KA
SKR  : belum lulus semua MK wajib
```

---

## 7. Cara Menjalankan Program

Pastikan **Java Development Kit (JDK)** sudah terpasang di perangkat Anda.

### 1. Clone Repository
```bash
git clone <url-repository-anda>
cd proyek2-asd-rekomendasi-mk
```

### 2. Kompilasi Source Code
**Linux / macOS / Git Bash:**
```bash
mkdir -p bin
javac -d bin src/model/*.java src/strukturdata/*.java src/util/*.java src/service/*.java src/Main.java
```

**Windows (Command Prompt / PowerShell):**
```powershell
mkdir bin
javac -d bin src\model\*.java src\strukturdata\*.java src\util\*.java src\service\*.java src\Main.java
```

### 3. Jalankan Program
```bash
java -cp bin Main
```
*(Atau jika mendukung argumen file mahasiswa secara langsung: `java -cp bin Main data/mhs_01.txt`)*

---

## 8. Daftar Periksa Pengumpulan (Checklist)

- [ ] Seluruh struktur data dibuat mandiri tanpa `TreeMap`, `TreeSet`, atau library graph bawaan Java.
- [ ] Program teruji dengan benar pada `mhs_01.txt`, `mhs_02.txt`, dan `mhs_03.txt`.
- [ ] Aturan prasyarat bertingkat dan aturan khusus Skripsi (`SKR`) berjalan akurat.
- [ ] Laporan `docs/Laporan_Proyek2_ASD.pdf` selesai (maksimal 3 halaman).
- [ ] Seluruh file terunggah ke repository online dan link dikumpulkan di Brone.