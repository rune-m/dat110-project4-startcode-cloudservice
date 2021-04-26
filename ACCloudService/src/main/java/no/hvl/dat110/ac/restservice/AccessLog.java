package no.hvl.dat110.ac.restservice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;

public class AccessLog {
	
	// atomic integer used to obtain identifiers for each access entry
	private AtomicInteger cid;
	protected ConcurrentHashMap<Integer, AccessEntry> log;
	
	public AccessLog () {
		this.log = new ConcurrentHashMap<Integer,AccessEntry>();
		cid = new AtomicInteger(0);
	}

	// TODO: add an access entry to the log for the provided message and return assigned id
	public int add(String message) {
		
		int id = cid.incrementAndGet();

		log.put(id, new AccessEntry(id, message));
		
		return id;
	}
		
	// TODO: retrieve a specific access entry from the log
	public AccessEntry get(int id) {
		
		return log.get(id);
		
	}
	
	// TODO: clear the access entry log
	public void clear() {

		log.clear();

	}
	
	// TODO: return JSON representation of the access log
	public String toJson () {
    	
		List<AccessEntry> allEntries = new ArrayList<>();
		log.forEach((k,v) -> {
			allEntries.add(v);
		});
    	
    	return new Gson().toJson(allEntries);
    }
}
