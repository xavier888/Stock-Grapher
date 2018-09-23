/*Graphs the prices from the text file */
import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.SwingWorker;
import org.knowm.xchart.*;
import org.knowm.xchart.style.markers.*;

 
/**
 * Creates a real-time chart using SwingWorker
 */
public class Grapher1
{
 
  private XYChart chart;
  private String ticker = "";
  /* The data */
  private ArrayList<Date> dates = new ArrayList<Date>();  //x-axis
	private ArrayList<Double> closes = new ArrayList<Double>();  //y-axis
	private ArrayList<Double> highs = new ArrayList<Double>(); //y-axis
	private ArrayList<Double> lows = new ArrayList<Double>(); //y-axis
	private ArrayList<Double> opens = new ArrayList<Double>(); //y-axis
  
  public Grapher1(String stockTicker)
  {
  ticker = stockTicker;
  }
  
  public XYChart getChart() //Prepares the graph
	{
		chart = new XYChartBuilder().width(1000).height(600).title("Intraday Prices for " + ticker).
				xAxisTitle("Time").yAxisTitle("Price").build();
		
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setChartBackgroundColor(Color.WHITE);
		chart.getStyler().setPlotBackgroundColor(Color.BLACK);
		chart.getStyler().setPlotGridLinesColor(Color.GRAY);
		
		
		return chart;
	}
 
  public void getData()
  {
  	try 
		{
			if (dates.size() > 0)
			{
				dates.clear();
			}
			if (closes.size() > 0)
			{
				closes.clear();
			}
			if (highs.size() > 0)
			{
				highs.clear();
			}
			if (lows.size() > 0)
			{
				lows.clear();
			}
			if (opens.size() > 0)
			{
				opens.clear();
			}
			
			String fileName = ticker + ".txt";
			
			/*Open the text file. */
			Scanner file = new Scanner(new FileReader(fileName)); 
			
			/*Copy all the prices from the text and paste them into their arrays */
			while (file.hasNextLine()) 
			{
				String[] elements = file.nextLine().split(",");
				
				Date date = new Date(Long.parseLong(elements[0])*1000L);
				double close = Double.parseDouble(elements[1]);
				double high = Double.parseDouble(elements[2]);
				double low = Double.parseDouble(elements[3]);
				double open = Double.parseDouble(elements[4]);
				
				/*Fill arrays with data */
				dates.add(date);
				closes.add(close);
				highs.add(high);
				lows.add(low);
				opens.add(open);
			}
			
			addData();
		}
    catch (Exception e) 
		{
    	e.printStackTrace();
    	System.out.print(e);
    }	
  }
  
  public void addData()
  {
  		chart.addSeries("Close", dates, closes).setMarker(SeriesMarkers.NONE).setLineWidth(3);
  		chart.addSeries("High", dates, highs).setMarker(SeriesMarkers.NONE).setLineWidth(3);
  		chart.addSeries("Low", dates, lows).setMarker(SeriesMarkers.NONE).setLineWidth(3);
  		chart.addSeries("Open", dates, opens).setMarker(SeriesMarkers.NONE).setLineWidth(3);
  }
  
  public void updateData()
  {
  	chart.updateXYSeries("Close", dates, closes, null);
		chart.updateXYSeries("High", dates, highs, null);
		chart.updateXYSeries("Low", dates, lows, null);
		chart.updateXYSeries("Open", dates, opens, null);
  }
}
