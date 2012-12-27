package controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GuessingService {
	// Singleton
	private static GuessingService instance;
	private GuessingService(){
		
	}
	public static GuessingService getInstance(String sessionId) throws Exception{
		GuessingService gs;
		if (instance==null){
			gs = new GuessingService();
			if (sessionId == null) {
				throw new Exception("Session id can not be null. Something is wrong !");
			}
			gs.setSessionId(sessionId);
			gs.initializeRandomMap(randomMap);
			gs.initializeTriesMap(triesMap);
		}
		else gs = instance;
		return gs;
	}
	
	// Data
	
	private String sessionId;
	private static Map<String,Integer> triesMap = new HashMap<String,Integer>();
	private static Map<String,Integer> randomMap = new HashMap<String, Integer>();
	
	private static int generateRandomNumber(){
		Random r = new Random();
		int random_number  = r.nextInt(20);
		random_number = random_number==0 ? 1 : random_number ;
		return random_number;
	}
	
    private void initializeTriesMap(Map tMap){
		if (tMap.containsKey(sessionId)) {
			tMap.remove(sessionId);
			tMap.put(sessionId, 0);
		} else
			tMap.put(sessionId, 0);
    }
    
    private void initializeRandomMap(Map rMap){

		if (rMap.containsKey(sessionId)){
			rMap.remove(sessionId);
			rMap.put(sessionId,  generateRandomNumber());
		}
		else rMap.put(sessionId, generateRandomNumber());
    }
    
	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getNumberOfTriesForSession() {
		return  triesMap.get(this.sessionId);
	}

	public Integer getRandomNumberForSession() {
		return randomMap.get(sessionId);
	}
	
	public void updateTries(int number_of_guesses){
		triesMap.remove(sessionId);
		triesMap.put(sessionId, number_of_guesses);
	}
}
