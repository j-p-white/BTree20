
import java.util.Comparator;
/*
 * Classname:BasicBST
 * 
 * Version information: 1
 *
 * Date: 2/9/2014
 * 
 * Copyright notice: none
 */
class NaturalComparator<T extends Comparable<T>> implements Comparator<T>
	{
		public int compare(T a,T b){
			return a.compareTo(b);
		}
		public boolean equals(Object obj){
			return (obj!=null) && (obj instanceof NaturalComparator);
		}
	}// end comparator class