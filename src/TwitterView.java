import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TwitterView extends Application{

	Stage window;
	static Scene homeS;
	//	Twitter twitter = new Twitter(null,null,null);


	@Override
	public void start(Stage stage) throws Exception {

		window = stage;
		File home = new File("src/home.fxml");
		String homepath = home.getAbsolutePath();

		Parent root = FXMLLoader.load(new File(homepath).toURI().toURL());
		homeS = new Scene(root, 500, 650);

		window.setTitle("FXML Welcome");
		window.setScene(homeS);
		window.show();
	}

	//	public Twitter getTwitter() {
	////		if(twitter==null) 	return new Twitter(null, null, null);
	////		else
	//			return twitter;
	//		
	//	}



	public void switchScene(File file, Stage window) throws MalformedURLException, IOException {
		String homepath = file.getAbsolutePath();

		Parent root = FXMLLoader.load(new File(homepath).toURI().toURL());
		homeS = new Scene(root, 500, 650);

		window.setTitle("FXML Welcome");
		window.setScene(homeS);
	}

	public void popupError(String text) {
		Alert box = new Alert(AlertType.ERROR);
		box.setHeaderText(null);
		box.setContentText(text);
		box.show();
	}

	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	void goSignUp(ActionEvent event) throws MalformedURLException, IOException {
		window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		File home = new File("src/signUp.fxml");
		switchScene(home, window);
	}

	@FXML
	void tryLogin(ActionEvent event) throws MalformedURLException, IOException {
		try {
			Twitter.login(username.getText(), password.getText());
			window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			File home = new File("src/feed.fxml");
			switchScene(home, window);
		}
		catch(IllegalArgumentException e) {
			popupError(e.getMessage());
		}
	}

	@FXML
	private PasswordField confirmpass;

	@FXML
	void trySignUp(ActionEvent event) throws MalformedURLException, IOException {
		try {
			Twitter.signUp(username.getText(), password.getText(), confirmpass.getText());
			window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			popupSuccess("Signed Up successfully");
			File home = new File("src/home.fxml");

			switchScene(home, window);
		}
		catch(IllegalArgumentException e) {
			popupError(e.getMessage());
		}
	}

	private void popupSuccess(String string) {
		Alert box = new Alert(AlertType.CONFIRMATION);
		box.setHeaderText(null);
		box.setContentText(string);
		box.show();
	}	

	@FXML
	private TextArea text;

	@FXML
	void goHome(ActionEvent event) throws MalformedURLException, IOException {
		window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		File home = new File("src/home.fxml");
		switchScene(home, window);
	}

	@FXML
	void tryFeed(ActionEvent event) throws MalformedURLException, IOException {
		window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		File home = new File("src/feed.fxml");
		switchScene(home, window);
		//text.setText(value);
	}

	@FXML
	private TextField textt;


	@FXML
	void tryTrending(ActionEvent event) throws MalformedURLException, IOException {
		//	text.setText();
		window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		File home = new File("src/trending.fxml");
		switchScene(home, window);
		String trends = "";

		////		Twitter.getTwitter();



	}

	@FXML
	void doTrending(ActionEvent event) throws FileNotFoundException {
		ArrayList<String> topics = new ArrayList<String>();
		topics= Twitter.getTwitter().trendingTopics();
		String trends = "\n";
		for(int i =0; i<topics.size() && i<5; i++) {
			if(!Twitter.isAStopWord(topics.get(i))) {
				trends=trends + topics.get(i) + " \n";
			}
		}

		text.setText("Trending topics\n" + trends);
		System.out.println(trends);
	}



	@FXML
	void tryPost(ActionEvent event) throws MalformedURLException, IOException {
		//Tweet tweet = new Tweet(Twitter.getCurrentUser().getUsername(), );
		window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		File home = new File("src/post.fxml");
		switchScene(home, window);
	}

	@FXML
	private TextArea postContext;

	@FXML
	void doPost(ActionEvent event) {

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());    	
		Tweet tweet = new Tweet(Twitter.getCurrentUser().getUsername(), timeStamp, postContext.getText());
		Twitter t = Twitter.getTwitter();
		Twitter.getTwitter().addTweet(tweet);
		Alert box = new Alert(AlertType.CONFIRMATION);
		box.setHeaderText(null);
		box.setContentText("Tweet posted");
		box.show();

	}


	@FXML
	private TextField searchContext;

	@FXML
	void doSearch(ActionEvent event) {
		if(searchContext.getText().contains("-")) {
			if(Twitter.getTwitter().tweetsByDate(searchContext.getText()) != null) {
				String allText = "";

				for(int i=0; i<Twitter.getTwitter().tweetsByDate(searchContext.getText()).size();i++) {
					allText+="\t\t" +Twitter.getTwitter().tweetsByDate(searchContext.getText()).get(i).getMessage() + "\n"
							+ "\n";
				}
				String date = "\n\tThe tweets posted on " + searchContext.getText() + " are:\n\n";
				text.setText(date  + allText);
			}
		}
		else {
			if(Twitter.getTwitter().latestTweetByAuthor(searchContext.getText()) != null) {
				String allText = "";
				Tweet t = Twitter.getTwitter().latestTweetByAuthor(searchContext.getText());
				//for(int i=0; i<Twitter.getTwitter().latestTweetByAuthor(searchContext.getText()).getMessage();i++) {
				allText+=Twitter.getTwitter().latestTweetByAuthor(searchContext.getText()).getMessage();
				//}
				String lastTweet = "\n\tThe last tweet by " + searchContext.getText() + " is:\n\n";
				text.setText(lastTweet + "\t" + allText + "\ton " + Twitter.getTwitter().latestTweetByAuthor(searchContext.getText()).getDateAndTime() + "\n\n");
			}
		}
	}
}
