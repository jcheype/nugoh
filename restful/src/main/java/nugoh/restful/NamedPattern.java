package nugoh.restful;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: mush
 * Date: Dec 16, 2009
 * Time: 1:22:38 AM
 */
public class NamedPattern {
    private final String REGEX = "\\{(\\w+)\\}";

    private final List<String> patternKey = new ArrayList();
    private final Pattern pattern;
    private final Pattern namePattern = Pattern.compile(REGEX);
    private final String uri;

    private String replaceParam(String uri) {
        StringBuffer myStringBuffer = new StringBuffer();
        Matcher myMatcher = namePattern.matcher(uri);
        while (myMatcher.find()) {
            patternKey.add(myMatcher.group(1));
            myMatcher.appendReplacement(myStringBuffer, "([^/]*)");
        }
        myMatcher.appendTail(myStringBuffer);
        return myStringBuffer.toString();
    }

    public NamedPattern(String uri) {
        this.uri = uri;
        pattern = Pattern.compile(replaceParam(uri));
    }

    public Map<String, Object> getMap(String uri){
        Map<String, Object> result = new HashMap<String, Object>();
        Matcher matcher = pattern.matcher(uri);
        if(matcher.find()){
            for(int i = 0 ; i<matcher.groupCount(); i++){
                result.put(patternKey.get(i), matcher.group(i+1));
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return uri;    //To change body of overridden methods use File | Settings | File Templates.
    }
}
