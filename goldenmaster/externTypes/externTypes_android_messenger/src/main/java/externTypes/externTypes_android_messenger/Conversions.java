package externTypes.externTypes_android_messenger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility methods for converting between primitive arrays (used by Bundle/Parcel/JNI)
 * and List types (used by the Java API surface).
 */
public final class Conversions {

    private Conversions() {}

    // --- toList overloads (array → List) ---

    public static List<Boolean> toList(boolean[] arr) {
        if (arr == null) return new ArrayList<>();
        List<Boolean> list = new ArrayList<>(arr.length);
        for (boolean v : arr) list.add(v);
        return list;
    }

    public static List<Integer> toList(int[] arr) {
        if (arr == null) return new ArrayList<>();
        List<Integer> list = new ArrayList<>(arr.length);
        for (int v : arr) list.add(v);
        return list;
    }

    public static List<Long> toList(long[] arr) {
        if (arr == null) return new ArrayList<>();
        List<Long> list = new ArrayList<>(arr.length);
        for (long v : arr) list.add(v);
        return list;
    }

    public static List<Float> toList(float[] arr) {
        if (arr == null) return new ArrayList<>();
        List<Float> list = new ArrayList<>(arr.length);
        for (float v : arr) list.add(v);
        return list;
    }

    public static List<Double> toList(double[] arr) {
        if (arr == null) return new ArrayList<>();
        List<Double> list = new ArrayList<>(arr.length);
        for (double v : arr) list.add(v);
        return list;
    }

    public static List<String> toList(String[] arr) {
        if (arr == null) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(arr));
    }

    public static <T> List<T> toList(T[] arr) {
        if (arr == null) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(arr));
    }

    // --- toArray overloads (List → array) ---

    public static boolean[] toArray(List<Boolean> list, boolean[] hint) {
        if (list == null) return null;
        boolean[] arr = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    public static int[] toArray(List<Integer> list, int[] hint) {
        if (list == null) return null;
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    public static long[] toArray(List<Long> list, long[] hint) {
        if (list == null) return null;
        long[] arr = new long[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    public static float[] toArray(List<Float> list, float[] hint) {
        if (list == null) return null;
        float[] arr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    public static double[] toArray(List<Double> list, double[] hint) {
        if (list == null) return null;
        double[] arr = new double[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }

    public static String[] toArray(List<String> list, String[] hint) {
        if (list == null) return null;
        return list.toArray(new String[0]);
    }

    public static <T> T[] toArray(List<T> list, T[] hint) {
        if (list == null) return null;
        return list.toArray(hint);
    }
}
