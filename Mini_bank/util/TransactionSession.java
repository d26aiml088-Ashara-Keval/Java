package util;

public class TransactionSession implements AutoCloseable {
    private boolean open = true;

    public boolean isOpen() {
        return open;
    }

    @Override
    public void close() {
        open = false;
    }
}
