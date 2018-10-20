/*Pulls stock prices and places them into a text file */
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.io.*;
public class Reader2
{
	
  private ArrayList<String> splitSource = new ArrayList<String>(); //Stores lines webpage content 
  private String ticker = ""; //stores user inputed ticker 
  private Long startUnix = 0L;
  private String fileName = "";
  private PrintWriter f;

public Reader2(String stockTicker)
{
	ticker = stockTicker;
	fileName =  ticker + ".txt";
	
	try //Prepare text file
	{
		File file = new File(fileName);
		f  = new PrintWriter(new FileWriter(file, false)); //False allows the file to be overwritten later
	}
	catch (IOException e)
	{
		System.out.print(e);
	}
}

/*WEBPAGE -----> ARRAY */
  public void webToArray()
  {
  	try
  	{
  		URL url = new URL("https://www.google.com/finance/getprices?i=60&p=1d&f=d,o,h,l,c,v&df=cpct&q=" + ticker);
  		URLConnection urlConn = url.openConnection();
  		InputStreamReader inputStreamReader = new InputStreamReader(urlConn.getInputStream());
  		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
  		
    	int i = 0;
      while(i < 7)
      {
      	bufferedReader.readLine();  
      	i = i + 1;
      }	
      
      if (splitSource.size() > 0) 
      {
      	splitSource.clear();
      }
      
      String s = "";
      while ((s = bufferedReader.readLine()) != null)
      {
      	splitSource.add(s);
      }
  	}
  	catch (Exception e)
  	{
  		e.printStackTrace();
  		System.out.print("Could not reach page.");
  	}
  }
  
  /*ARRAY ----> FILE */
  public void writeToFile()
  {
  	try
  	{
  		for (String eachLine : splitSource) 
  		{
  			String[] splitLine = eachLine.split(","); 
  			if (splitLine.length == 6)
  			{
  				if (splitLine[0].charAt(0) == 'a') 
  				{
  					startUnix = Long.parseLong(splitLine[0].substring(1));//Record the first time stamp of the day
  					String lineToWrite = eachLine.substring(1) + "\n"; 					  
  					f.write(lineToWrite);	//Overwrite the file and write the line to it
  				}
    		/* If NOT the first line of the day, we check if we need to write it. 
    		 * All we have to do is reassign the time stamps. 
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
    			f.write(lineToWrite); //write the line to it
        }
    	}
    }
    //close the file    
  	f.flush();
    f.close();      
   }
  catch (Exception e)
	{
  	System.out.print(e);
  }
 }
}
