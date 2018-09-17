import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

//The game is itself simple so I decided to put it all in one class, can be made modular if needed
public class HangmanGame {

    //function to count distinct chars so we know when user wins
    public static int countDistinctChars(String str){
        boolean[] isCharPresent = new boolean[Character.MAX_VALUE];
        for (int i = 0; i < str.length(); i++) {
            isCharPresent[str.charAt(i)] = true;
        }

        int counter = 0;
        for (int i = 0; i < isCharPresent.length; i++) {
            if (isCharPresent[i]){
                counter++;
            }
        }
        return counter;
    }
    //function which represents a single game
    //returns 1 point if game won or 0 if lost
    public static int play(HashMap<String, ArrayList<String>> dict){
        Scanner userIn = new Scanner(System.in);
        int mistakes = 0;
        System.out.println("Please choose a category:");
        for(String key : dict.keySet())
            System.out.println(key);
        String topic;
        do{
            System.out.print(">");
            topic = userIn.nextLine();
            if(!dict.containsKey(topic))
                System.out.println("Please enter one of the listed topics.");
        }while(!dict.containsKey(topic));

        Random rand = new Random();
        int wordNum = rand.nextInt(dict.get(topic).size());
        String phrase = dict.get(topic).get(wordNum);
        HashMap<Character,String> hiddenWord = new HashMap<>();
        //mask out word
        for(int i = 0 ; i < phrase.length() ; i++)
            if(phrase.charAt(i) == ' ')
                hiddenWord.put(' ',"  ");
            else if(phrase.charAt(i) == '-')
                hiddenWord.put('-',"-");
            else
                hiddenWord.put(phrase.charAt(i), "_");
        //clear phrase from everything but letters so we can count distinct ones
        String phraseCleaned =  phrase.replaceAll("[^\\p{L}]", "");
        int pointsToWin = countDistinctChars(phraseCleaned);
        int points = 0;
        //loop to represent single turn in game
        while(mistakes < 10){
            System.out.println("Attempts left: " + (10 - mistakes));
            System.out.print("Current word/phrase:");
            for(int i = 0 ; i < phrase.length() ; i++)
                System.out.print(hiddenWord.get(phrase.charAt(i)) + ' ');
            System.out.println();
            System.out.println("Please enter a letter:");
            char in;
            do{
                System.out.print("> ");
                in = userIn.next().charAt(0);
                if(!(in >= 'A' && in <= 'Z') && !(in >= 'a' && in <= 'z'))
                    System.out.println("Enter a latin letter, please.");
            }while((!(in >= 'A' && in <= 'Z') && !(in >= 'a' && in <= 'z')));
            char upper = Character.toUpperCase(in);
            char lower = Character.toLowerCase(upper);
            //remove point if point added twice for upper and lowercase letters

            if(hiddenWord.containsKey(upper) && hiddenWord.get(upper).equalsIgnoreCase("_")){
                hiddenWord.put(upper,upper + "");
                points++;
            }
            if(hiddenWord.containsKey(lower) && hiddenWord.get(lower).equalsIgnoreCase("_")){
                hiddenWord.put(lower,lower + "");
                points++;
            }
            if(!hiddenWord.containsKey(lower) && !hiddenWord.containsKey(upper)){
                mistakes++;
                System.out.println("The word/phrase doesn't have this letter.");
            }

            if(points == pointsToWin){
                System.out.println("Congratulations you have revealed the word/phrase:");
                for(int i = 0 ; i < phrase.length() ; i++)
                    System.out.print(hiddenWord.get(phrase.charAt(i)) + ' ');
                System.out.println();
                return 1; // return one point
            }

        }
        return 0;
    }

    public static void main(String[] args){
        try {
            BufferedReader br = new BufferedReader(new FileReader("input.txt"));
            //using hashmap with topics for O(1) access when num of topics grows towards infinity
            HashMap<String, ArrayList<String>> dict = new HashMap<>();
            String currentTopic = " ";
            String line = br.readLine();
            // loop over file to create dictionary
            while(line != null){
                line = line.trim(); // deal with spaces on sides
                //avoid crazy character
                if(line.startsWith('\uFEFF' + ""))
                    line = line.substring(1);
                if(line.startsWith("_")){
                    currentTopic = line.substring(1);
                    dict.put(currentTopic, new ArrayList<>());
                }
                else
                    dict.get(currentTopic).add(line);
                line = br.readLine();
            }
            int count = 0;
            while(play(dict) > 0){
                System.out.println("Current score: " + ++count);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Please supply provide correct dictionary name.");
        } catch (IOException e) {
            System.out.println("Problem when reading from file.");
        }
    }
}
