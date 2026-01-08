package CollectionFramework;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCacheImpl {
    public static void main(String[] args) {
        LinkedHashMap<Integer,String> lru = new LinkedHashMap<>(10,0.5f,true){
            protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
                return this.size()>10;
            }
        };



    }
}
