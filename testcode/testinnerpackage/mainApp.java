package testcode.testinnerpackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class mainApp {
	public static void main(String[] args){
		
		StackADT<String> Stack = new ArrayStack();
		
		boolean running = true;
		String input;
		
		while(running){
			
			System.out.println("Please choose an option.");
			System.out.println("[1 - Place String][2 - Remove top][3 - Examine top][4 - Check if empty][5 - Check size][6 - Quit]");
			
			//input = System.console().readLine();
			
			switch("1"){
			case "1" : 	System.out.println("Please enter string to be place on stack.");
						Stack.push("test");
						System.out.println("String has been placed on the stack.");
						break;
			case "2" :  Stack.pop();
						System.out.println("Top value has been removed from the stack.");
						break;
			case "3" :  System.out.println("Top value on stack = " + Stack.top());
						break;
			case "4" :  if(Stack.isEmpty()){
							System.out.println("Stack is empty.");
							}
						else{
							System.out.println("Stack is not empty.");
							}
						break;
			case "5" :  if(Stack.isEmpty()){
							System.out.println("Stack is empty.");
						}
						else{
							System.out.println("There are " + Stack.size() + "items in the stack.");
						}
						break;
			case "6" :  System.out.println("Good Bye!");
						running = false;
						break;
										
			}
		}
		
	}
}
