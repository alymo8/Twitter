import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Twitter {
	//initialize the 4 tables in the constuctor (4th is to count effectif of the word)
	//call addtweet on each tweet
	//put all the stopwords from the arraylist in the table

	//addtweet:
	//add the tweet to the author table
	//addtweet to date table
	//addtweet to the 4th table
	//for each tweet get the message and get words from it 
	//for each word check if it's a stopword 
	// if not a sw then add to the table
	//make sure not to count a word two times

	MyHashTable<String, ArrayList<Tweet>> wa;
	MyHashTable<String, ArrayList<Tweet>> wd;
	MyHashTable<String, Integer> ws;
	MyHashTable<String, Tweet> lt;
	static LinkedList<User> users;
	static User currentUser;



	// O(n+m) where n is the number of tweets, and m the number of stopWords
	public Twitter(ArrayList<Tweet> tweets, ArrayList<String> stopWords, LinkedList<User> users) {

		this.wa = new MyHashTable<String, ArrayList<Tweet>>(7);
		this.wd = new MyHashTable<String, ArrayList<Tweet>>(7);
		this.ws = new MyHashTable<String, Integer>(7);
		this.lt = new MyHashTable<String, Tweet>(1);
		Twitter.users=users;
		//		if(tweets!=new ArrayList<Tweet>()) {
		//		for(Tweet tweet : tweets) {					 
		//			addTweet(tweet);
		//		}
		//		}
		//		if(stopWords!=new ArrayList<String>()) {
		//		for(String word: stopWords) {
		//			ws.put(word.toLowerCase(), 0);
		//		}
		//		}
	}


	static Twitter twitter = new Twitter(null,null,null);

	public static Twitter getTwitter() {

		return twitter;

	}


	public void addUser(User user) {
		users.add(user);
	}

	public LinkedList<User> getUsers() {
		return users;
	}


	/**
	 * Add Tweet t to this Twitter
	 * O(1)
	 */	
	public void addTweet(Tweet t) {

		ArrayList<Tweet> tweetsByAuthor = wa.get(t.getAuthor());
		String d = t.getDateAndTime().substring(0, 10);
		ArrayList<Tweet> tweetsByDate = wd.get(d);

		//	ArrayList<>
		if (tweetsByAuthor == null) {
			// there is no pair in wa that contains the given key
			// -> add the pair to wa
			// -> put with key and and an array list containing t
			//tweetsByAuthor.add(t);
			ArrayList<Tweet> n = new ArrayList<Tweet>(1);
			n.add(t);
			wa.put(t.getAuthor(), n);
			lt.put(t.getAuthor(), t);
		} 
		else {
			// add t to tweetsByAuthor			
			wa.get(t.getAuthor()).add(t);
			if((t.getDateAndTime()).compareTo(lt.get(t.getAuthor()).getDateAndTime())>0)
				lt.put(t.getAuthor(), t);
		}
		if(tweetsByDate==null) {	
			ArrayList<Tweet> n = new ArrayList<Tweet>(1);
			n.add(t);
			wd.put(d, n);
		}
		else {
			wd.get(d).add(t);
		}
	}	




	/**
	 * Search this Twitter for the latest Tweet of a given author.
	 * If there are no tweets from the given author, then the 
	 * method returns null. 
	 * O(1)  
	 */
	public Tweet latestTweetByAuthor(String author) {
		if(lt.get(author)!=null) return lt.get(author);
		else {

			for(int i = 0; i< wa.get(author).size()-1; i++) {
				if(wa.get(author).get(i).getDateAndTime().compareTo(wa.get(author).get(i+1).getDateAndTime())<0) {
					return wa.get(author).get(i+1);
				}
				else return  wa.get(author).get(i);
			}
		}
		return null;
	}





	/**
	 * Search this Twitter for Tweets by `date' and return an 
	 * ArrayList of all such Tweets. If there are no tweets on 
	 * the given date, then the method returns null.
	 * O(1)
	 */
	public ArrayList<Tweet> tweetsByDate(String date) {

		String d = date.substring(0, 10);
		if(wd.get(d)!=null) {
			return wd.get(d);
		}
		return null;
	}



	/**
	 * Returns an ArrayList of words (that are not stop words!) that
	 * appear in the tweets. The words should be ordered from most 
	 * frequent to least frequent by counting in how many tweet messages
	 * the words appear. Note that if a word appears more than once
	 * in the same tweet, it should be counted only once. 
	 */
	public ArrayList<String> trendingTopics() {
		MyHashTable<String, Integer>  c = new MyHashTable<String, Integer>(7);
		ArrayList<ArrayList<Tweet>> tweets = wd.values();
		for(ArrayList<Tweet> list: tweets) {
			for(Tweet tweet : list) {
				MyHashTable<String, Integer> is = new MyHashTable<String, Integer>(7);
				ArrayList<String> words = getWords(tweet.getMessage());    			
				for(String word: words) {
					String w = word.toLowerCase();
					if(ws.get(w)==null) {
						if(is.get(w)==null) {
							is.put(w, 0);
							Integer old = c.get(w);
							if(c.get(w)==null) old = 0;
							c.put(w, old+1);    						
						}
					}
				}
			}
		}
		return MyHashTable.fastSort(c);



	}



	/**
	 * A helper method you can use to obtain an ArrayList of words from a 
	 * String, separating them based on apostrophes and space characters. 
	 * All character that are not letters from the English alphabet are ignored. 
	 */
	private static ArrayList<String> getWords(String msg) {


		ArrayList<String> words = new ArrayList<String>();
		BreakIterator breakIterator = BreakIterator.getWordInstance();
		breakIterator.setText(msg);
		int lastIndex = breakIterator.first();
		while (BreakIterator.DONE != lastIndex) {
			int firstIndex = lastIndex;
			lastIndex = breakIterator.next();
			if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(msg.charAt(firstIndex))) {
				words.add(msg.substring(firstIndex, lastIndex));
			}
		}

		return words;

	}

	public static User getCurrentUser() {
		return currentUser;
	}


	public static void setCurrentUser(User currentUser) {
		Twitter.currentUser = currentUser;
	}


	public static void login(String username, String password) {
		if(!User.hasWithUsername(username) || !User.getWithUsername(username).getPassword().equals(password)) {
			throw new IllegalArgumentException("Username or passsword is not correct");
		}
		else {
			Twitter.setCurrentUser(User.getWithUsername(username));
		}

	}

	
	public static void signUp(String username, String password, String confirmpass) {
		if(!User.hasWithUsername(username) && password.equals(confirmpass)){
			User newUser = new User(username, password);
			if(Twitter.users==null) {
				Twitter.users = new LinkedList<User>();
				Twitter.users.add(newUser);
			}
		}
		else throw new IllegalArgumentException("Username already exists or passwords don't match");

	}

	public static boolean isAStopWord(String word) throws FileNotFoundException {
		Scanner input=new Scanner(new File("src/stopWords.txt"));
		input.useDelimiter("\n"); //delimiter is one or more spaces
		while(input.hasNext()){
			String s = input.next();
			if(word.equals(s)) {
				return true;
			}
			
		}
		return false;
	}
}
