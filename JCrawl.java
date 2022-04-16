/**
 * The Class JCrawl to handle web crawling.
 * 
 * @author Andy Zollner, Zollner Solutions LLC
 * @param startURL		URL to start crawling
 * @param breakpoint	breakpoint for program, number of URLS to scrape
 * @return
 * @exception e		print stacktrace
 * @see
 * @since
 * @serial
 * @deprecated
 */
 
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
 
public class JCrawl {
	
	private Queue<String> urlQueue;
	private List<String> visitedURLs;
		
	public JCrawl() {
		urlQueue = new LinkedList<>();
		visitedURLs = new ArrayList<>();
	}
	
	public void crawl(String startURL, int breakpoint) {
		urlQueue.add(startURL);
		visitedURLs.add(startURL);
		
		while(!urlQueue.isEmpty()) {
			// remove url from queue and begin crawling
			String str = urlQueue.remove();
			String htmlTagged = "";
			
			// use try-catch to build and check url
			try{
				
				// use BufferedReader to build url string
				URL url = new URL(str);
				BufferedReader urlIn = new BufferedReader(new InputStreamReader(url.openStream()));
				String inLine = urlIn.readLine();
				
				// read each line and concat to htmlTagged
				while (inLine != null) {
					htmlTagged += inLine;
					inLine = urlIn.readLine();
				}
				
				// close stream
				urlIn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// TODO: refactor?
			// regex to match url
			String urlRegex = "(www|http:|https:)+[^\\s]+[\\w]";
			Pattern pattern = Pattern.compile(urlRegex);
			Matcher matcher = pattern.matcher(htmlTagged);
			
			// breakpoint exits outermost loop per BFS
			breakpoint = getBreakpoint(breakpoint, matcher);
			if (breakpoint == 0) break;
			
			
		}
	}
	
	private int getBreakpoint(int breakpoint, Matcher matcher) {
		while(matcher.find()){
			String actualURL = matcher.group();

			if(!visitedURLs.contains(actualURL)){
				visitedURLs.add(actualURL);
				System.out.println("Website found with URL " + actualURL);
				urlQueue.add(actualURL);
			}

			// exit the loop if it reaches the breakpoint.
			if(breakpoint == 0) break;
			breakpoint--;
		}
		return breakpoint;
	}
	
	public static void main(String[] args) {
		JCrawl crawler = new JCrawl();
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter a URL: ");
		String startURL = sc.nextLine();
		System.out.println("Enter number of links to crawl: ");
		int breakpoint = sc.nextInt();
		crawler.crawl(startURL, breakpoint);
	}
}
