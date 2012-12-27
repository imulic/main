package controllers;

import play.*;
import play.i18n.Messages;
import play.mvc.*;
import java.util.*;
import models.*;

public class Application extends Controller {
	private static final int MAXIMUM_TRIES = 3;
	private static int     number_of_guesses = 0;
	private volatile static int   random_number = 0;

	private static int generateRandomNumber(){
		Random r = new Random();
		int random_number  = r.nextInt(20);
		random_number = random_number==0 ? 1 : random_number ;
		return random_number;
	}
	
	private static void init(){
		random_number = generateRandomNumber();
		number_of_guesses = 0;
	}
	
	static { 
	   init();
	};
	
    public static void index() {
    	System.out.println("random number generated is : "+random_number);
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
 
    	validation.required("Number", number);
    //	validation.min("Number", number, 1);
    //	validation.max("Number", number, 20);
    	
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
    	    number_of_guesses++;	
    	}
    	
    	flash.put("number_of_guesses", MAXIMUM_TRIES-number_of_guesses);
    	
    	checkGuess(number);
    	index();
    }

	/**
	 * @param number
	 */
    
	private static void checkGuess(int number) {
		if (number == random_number){
    		flash.clear();
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