package my.self.bsmg.mgr.tool;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 1L;
    private static int MAX_ENTRIES = 1024;

    public int getMaxEntries() {
        return MAX_ENTRIES;
    }

    public void setMaxEntries(int max_size) {
        MAX_ENTRIES = max_size;
    }

    public LRUMap() {
        super(MAX_ENTRIES, 0.75f, true);
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > MAX_ENTRIES;
    }
}
