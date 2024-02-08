package mc.ultimatecore.helper.utils;

public record Tuple<K, V>(K key, V value) {

    public static <K, V> Tuple<K, V> from(K key, V value) {
        return new Tuple<>(key, value);
    }
}
