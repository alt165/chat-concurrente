import java.util.Deque;
import java.util.LinkedList;

public class MessageHistory {
    private final Deque<String> history;
    private final int maxSize;

    public MessageHistory(int maxSize) {
        this.maxSize = maxSize;
        this.history = new LinkedList<>();
    }

    public synchronized void addMessage(String message) {
        if (history.size() == maxSize) {
            history.removeFirst();
        }
        history.addLast(message);
    }

    public synchronized Iterable<String> getHistory() {
        return new LinkedList<>(history);
    }

    public synchronized boolean isEmpty() {
        return history.isEmpty();
    }
}
