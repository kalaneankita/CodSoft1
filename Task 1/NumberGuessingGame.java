package codSoftPackage;
import java.util.Scanner;
import java.util.Random;
public class NumberGuessingGame {
public static void main(String[] args) {
	Scanner sc = new Scanner(System.in);
	Random ran = new Random();
	int min = 1;
	int max = 100;
	int maxAttempts = 10;
	int scoreOfUser =0;
	
	System.out.println("Welcome to the Game");
	
	boolean playGame = true;
	
	while(playGame) {
		
		int generatedNum = ran.nextInt(max - min +1)+min;
		int attempts =0;
		boolean guessedRight = false;
		
		System.out.println("I have chosen a number between " + min + " and  " + max + ". Guess it?");
		
		while(attempts < maxAttempts && !guessedRight) {
			System.out.print("Enter your Guess : ");
			
			int userGuess = sc.nextInt();
			attempts++;
			
			if(userGuess == generatedNum) {
				System.out.println("Congratulations! You guessed the correct number in " + attempts + " attempts.");
				scoreOfUser += 1;
				guessedRight = true;
			}else if(userGuess < generatedNum) {
				
				System.out.println("Your guess is too low.");
			}else {
				System.out.println("Your guess is too High.");
			}
		}
		if(!guessedRight) {
			System.out.println("you've reached the maximum number of attempts. The number was: " + generatedNum);
		}
		
		
	System.out.print("Do you want to play again? (y/n): ");
	String response = sc.next();
	playGame = response.equalsIgnoreCase("y");
	}
	
	System.out.println("Game over! Your total score is: " + scoreOfUser);
    sc.close();
}
}
