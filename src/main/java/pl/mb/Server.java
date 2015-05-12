package pl.mb;

public class Server {

    private int capacity;

    public Server(int capacity) {
        this.capacity = capacity;
    }

    public int capacity() {
        return capacity;
    }

    public Percent load() {
        return Percent.zero();
    }
}
