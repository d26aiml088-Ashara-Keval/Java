package Lab_08;

class MyResource implements AutoCloseable {
    MyResource() {
        System.out.println("Resource opened.");
    }

    void performTask() throws Exception {
        throw new Exception("Original task error.");
    }

    @Override
    public void close() {
        System.out.println("Resource closed.");
    }
}

public class ResourceDemo {
    public static void main(String[] args) {
        try (MyResource resource = new MyResource()) {
            resource.performTask();
        } catch (Exception e) {
            System.out.println("Caught: " + e.getMessage());
        }
    }
}