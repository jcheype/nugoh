package nugoh.service.twitter;


import java.util.Map;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterService {
    private TwitterFactory twitterFactory = new TwitterFactory();
    private Twitter twitter;
    private String username;
    private String password;

    private String data_key = "data";

    public void setPassword(String password) {
	this.password = password;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public void setData_key(String data_key) {
	this.data_key = data_key;
    }

    public void init() {
	twitter = twitterFactory.getInstance(username, password);
    }

    public void run(Map<String, Object> context) throws TwitterException {
	twitter.updateStatus((String) context.get(data_key));
    }
}