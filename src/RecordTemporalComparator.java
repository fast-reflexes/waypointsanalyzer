package wpchallenge;

import java.util.Comparator;

public class RecordTemporalComparator implements Comparator<Record> {
	
	public int compare(Record r1, Record r2) {
		return r1.getTime().compareTo(r2.getTime());
	}
	
	public boolean equals(Object o) {
		if(o instanceof RecordTemporalComparator) return true;
		else return false;
	}

}