import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

public class GetSleepEnergy {
	
	public static double work(ArrayList<Long> start_secs, File device) {
		double energy = 0.0;
		long previous_time = 0;
		long total_sleep_time = 0;
		
		String device_path = device.getAbsolutePath();
		String cpulogs_path = device_path + "/CpuUsageLogs";
		File cpulogs_folder = new File(cpulogs_path);
		File[] cpulogs = cpulogs_folder.listFiles();
		Arrays.sort(cpulogs);
		
		for(File cpulog : cpulogs) {
			String line = "";
			
			try {
			    InputStream fis = new FileInputStream(cpulog);
			    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			    BufferedReader br = new BufferedReader(isr);
			 
			    while ((line = br.readLine()) != null && line.length() > 1) {
			    	String[] substrs = line.split(",");
			    	String time_str = substrs[0]; // in sec 
			    	String cpu_str = substrs[1]; // in percentage
			    	long curr_time = Long.parseLong(time_str, 10);
			    	double cpu = Double.parseDouble(cpu_str);
			    	
			    	if(Work.validTime(start_secs, curr_time)) {
			    		if(curr_time > previous_time) {
			    			// in case gap bigger than a day we % Work.DAY_IN_SEC
			    			// eg. no record after 4.1 9pm and before 9am 4.2, then 
			    			// I treat the watch is sleeping in this period
			    			if (previous_time != 0)
			    				total_sleep_time += (curr_time - previous_time)%Work.DAY_IN_SEC - 1;
			    			
			    			previous_time = curr_time;
			    		}
			    	}
			    }
			} catch (FileNotFoundException e) {	
				//
			} catch (IOException e) {
				//
			} catch (NumberFormatException e) {
				//
			}
		}
		
		energy = (total_sleep_time*Work.SLEEP_POWER)/(3.6*3.7);
		
		return energy;
	}
}

