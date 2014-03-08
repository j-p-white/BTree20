import java.util.Comparator;


public class NaturalComparator<T extends Comparable<T>> implements Comparator<T> {

	@Override
	public int compare(T obj1, T obj2) {
		return obj1.compareTo(obj2);
	}

}
