package evs.rest.demo.domain;

public class RackUtil {
	public static boolean compare(Object a, Object b) {
		if(a == null && b == null)
			return true;
		if(a == null)
			return false;
		return a.equals(b);
	}
}
