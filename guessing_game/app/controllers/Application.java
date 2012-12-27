package controllers;

import play.*;
import play.i18n.Messages;
import play.mvc.*;
import java.util.*;
import java.util.Map.Entry;

import models.*;

public class Application extends Controller {
	private static final int MAXIMUM_TRIES = 3;

	private static GuessingService guessingService; 

	private static void init(){
		String sessionId = session.getId().toString();
		try { // well I need to check some stuff specially sessionId if that is wrong we are in trouble
			guessingService = GuessingService.getInstance(sessionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("sessionid = "+sessionId);
	}
	
	static { 
	   init();
	};
	
    public static void index() {
    	System.out.println("random number generated is : "+guessingService.getRandomNumberForSession());
        render();
    }
    
    public static void startagain(){
    	init(); 
    	render();  
    }
    
    public static void yougotit(){
    	init();
    	render();
    }
   
    public static void guess(int number){

    	int number_of_guesses = guessingService.getNumberOfTriesForSession();
    	int random_number = guessingService.getRandomNumberForSession(); 
    	
    	if (number<=0 || number>20){
    		flash.put("error", "Invalid input, enter value between 1 and 20");
    		index();
    	}
    	
    	if (validation.hasErrors()){
    		params.flash(); // Add http parameters to flash scope
    		validation.keep(); // keep the errors for next request
    		index();
    		for (play.data.validation.Error error : validation.errors()){
    			System.out.println(error.message());
    		}
    	}
    	
        // Some debug info   
    	
    	System.out.println("random number:  "+random_number);
    	System.out.println("you have guessed: "+number);
    	System.out.println("number of guesses: "+number_of_guesses);
    	
    	if (number_of_guesses>=2) {
    		flash.put("you_loose","Correct number was "+random_number);
    		checkGuess(number);
    		startagain();
    	} 
    	else
    	{
    	  guessingService.updateTries(++number_of_guesses);	    
    	}
    	
    	flash.put("number_of_guesses", MAXIMUM_TRIES-guessingService.getNumberOfTriesForSession());
    	
    	checkGuess(number);
    	index();
    }

    
	private static void checkGuess(int number) {
		int random_number = guessingService.getRandomNumberForSession();
		if (number == random_number){
    		flash.put("correct", Messages.get("app.correct"));
    		yougotit(); 
    	} else if (number<random_number){
    		flash.put("error", "too low" );
    	}
    	else if (number>random_number){
    		flash.put("error", "too high" );
    	}
	} 
}