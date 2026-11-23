package strukturData;

public class CustomList<T> {

    /**
     * Node internal untuk menyimpan data dan pointer ke elemen berikutnya.
     */
    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Constructor untuk menginisialisasi list kosong.
     */
    public CustomList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * Menambahkan elemen baru di bagian akhir list dengan kompleksitas O(1).
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Menambahkan elemen hanya jika belum ada di dalam list (mencegah duplikasi).
     */
    public void addUnique(T data) {
        if (!contains(data)) {
            add(data);
        }
    }

    /**
     * Mengecek apakah suatu elemen sudah tersimpan di dalam list.
     */
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Mengambil elemen pada posisi indeks tertentu (mulai dari 0).
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Indeks di luar batas: " + index);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Mengembalikan jumlah elemen di dalam list.
     */
    public int size() {
        return size;
    }

    /**
     * Mengecek apakah list dalam keadaan kosong.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Menggabungkan seluruh elemen list menjadi satu teks dengan pemisah tertentu (misal ", ").
     */
    public String join(String delimiter) {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data.toString());
            if (current.next != null) {
                sb.append(delimiter);
            }
            current = current.next;
        }
        return sb.toString();
    }
}