/*Main method
 */
import java.io.*;
import java.util.*;

import javax.swing.*;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
public class ReaderInitiater 
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Let's track a stock! Please enter a ticker: ");
		String ticker = in.nextLine();
		Reader2 r = new Reader2(ticker);
		r.webToArray();
		r.writeToFile();
		Grapher1 g = new Grapher1(ticker);
		g.getChart();
		g.getData();
		//g.addData();
		
		SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(g.getChart());
    JFrame jf = sw.displayChart();
    
    while(jf.isShowing()) 
    
		{
			try
			{
				r.webToArray();
				r.writeToFile();
				g.getData();
				g.updateData();
				sw.repaintChart();
				Thread.sleep(600000);
			}
      catch (Exception e)
      {
      	System.out.print(e);
      }
	}
}
}