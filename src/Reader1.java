/*Reads all the content on a stock's instraday price log and writes it into a text file
 * Note: Google Finance only updates as the page is manually refreshed
 */
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.io.*;

public class Reader1 
{
	private ArrayList<String> rawFileLines = new ArrayList<String>(); //Stores lines of file content
	private ArrayList<String> splitSource = new ArrayList<String>(); //Stores lines webpage content 
  private String ticker = ""; //stores user inputed ticker 
  private Long lastUnix;
  private Long startUnix = 0L;
  
  public Reader1(String stockTicker)
  {
  	ticker = stockTicker;
  }
	
//Transfers all the relevant price data from google finance to  text file
	public void collect() 
  {
		/*Connect to Google Finance */
		try
		{	
      URL url = new URL("https://www.google.com/finance/getprices?i=60&p=1d&f=d,o,h,l,c,v&df=cpct&q=" + ticker);
      URLConnection urlConn = url.openConnection();
      InputStreamReader inputStreamReader = new InputStreamReader(urlConn.getInputStream());
      BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

      String fileName = ticker + ".txt"; 
      //FILE -----> ARRAYLIST
      //If the text file already exists, copy all file content into an ArrayList and get the last time stamp
      try(Scanner file = new Scanner(new FileReader(fileName))) 
      {
      	//Copy all file content into an ArrayList
      	while (file.hasNextLine()) 
        {
         rawFileLines.add(file.nextLine()); 
        }   
      	String[] lastLine = rawFileLines.get(-2).split(",");
      	lastUnix = Long.parseLong(lastLine[0]);
      	System.out.println(lastLine);
      	
       }
      
      catch (Exception e) 
      {
      	
      	File newFile = new File("src/" + fileName);
      	newFile.createNewFile();
      	System.out.println("no");
        lastUnix = 0L;
      } 
      
      //WEB -----> ARRAYLIST -----> FILE
      /*If the text file doesn't exist yet, we make one. If it does, we grab it.*/
      
      
      //BufferedWriter saveFile = new BufferedWriter(new FileWriter(new File(fileName), true )); //"false" = replace, not amend to file. 
      FileWriter saveFile = new FileWriter(fileName, true ); 
      /*Skip first 7 lines of the webpage content because they contain irrelevant data */
      int i = 0;
      while(i < 7)
      {
      	bufferedReader.readLine();  
      	i = i + 1;
      }	
      
     /*If splitSource already had previous data, wipe it out + start anew. */
      if (splitSource.size() > 0) 
      {
      	splitSource.clear();
      }
      String s = "";
      /*Read relevant webpage lines + put them into splitSource*/
      while ((s = bufferedReader.readLine()) != null)
      {
      	splitSource.add(s);
      }
    
      /*Process lines of data + write them into the text file*/
      for (String eachLine : splitSource) 
      {
      	String[] splitLine = eachLine.split(","); // {time, close, high, low, open, volume}
       /*Each relevant line of data *should* contain 6 chunks data separated 
        * by commas, so we just make sure it is. */
      	if (splitLine.length == 6)
      	{
      		/* IF 1ST LINE OF DAY, apply special treatment. */
      		if (splitLine[0].contains("a")) 
      		{
      			startUnix = Long.parseLong(splitLine[0].substring(1));//Record the first time stamp of the day
        		
      			/* Unlike Yahoo Finance (which no longer exists currently), Google Finance always puts 
        		 * 'a' in front the first time stamp of the day, so we want everything after 'a' */
  					String lineToWrite = eachLine.substring(1) + "\n";
  						
  					/*Write the processed line of data onto the text file*/
  					if (startUnix > lastUnix)
  					{
  						saveFile.write(lineToWrite);		
  						saveFile.flush();
  					}
           }
      		
      		/* If NOT the first line of the day, we check if we need to write it. all we have to do is reassign 
      		 * the time stamps. 
      		 * 
      		 * Google finance does not include any unix time stamps for ticks 
      		 * after the very first one; rather, it just labels them as the minutes 
      		 * (which are not in Unix time!) passed after the market 
      		 * opens: 1, 2, 3 .... 390. (390 marks 4:00 pm). 
      		 * So in order to assign dates to every price, we have use the first 
      		 * (and only) time stamp to calculate the others, which is why we must 
      		 * record the first time stamp of the day. */
      		else 
      		{
      			/*Reassign the time stamp as startTime + minutes, in Unix Time as a String. */
      		
      			Long currentTime = startUnix + Long.parseLong(splitLine[0]) * 60L;
      			splitLine[0] = currentTime + ""; 
      			String lineToWrite = splitLine[0] + eachLine.substring(eachLine.indexOf(",")) + "\n";
      			if (currentTime > lastUnix)
      			{
      				
      			saveFile.write(lineToWrite); //write it into the file	
      			saveFile.flush();
      			}
           }
      	}
      }
      //close the file    
      saveFile.close();      
     }
    catch (Exception e)
		{
    	System.out.print(e);
    }
	}
}

      

