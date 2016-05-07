import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

public class GetStartSec {

	public static void work(ArrayList<Long> start_secs, File device) {
		String device_path = device.getAbsolutePath();
		String cpulogs_path = device_path + "/CpuUsageLogs";
		File cpulogs_folder = new File(cpulogs_path);
		File[] cpulogs = cpulogs_folder.listFiles();
		Arrays.sort(cpulogs);
		
		long start_sec = 0;
	    int last_known_day = 0;
	    int count = 0;
		
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
			    	
			    	if(curr_time < Work.BASE_SEC) {
			    		continue;
			    	}
			    	
			    	int curr_day = (int) ((curr_time - Work.BASE_SEC)/Work.DAY_IN_SEC) + 1;
			    	
			    	if(curr_day > last_known_day) {
			    		if(count < Work.RECORD_LIMIT && start_secs.size() >= 1) {
			    			start_secs.subList(start_secs.size()-1, start_secs.size()).clear();
			    		}
			    		
			    		start_sec = getStartTime(curr_time);
			    		last_known_day = curr_day;
			    		start_secs.add(start_sec);
			    		
			    		count = 0;
			    	}
			    	
			    	count ++;
			    }
			} catch (FileNotFoundException e) {	
				//
			} catch (IOException e) {
				//
			} catch (NumberFormatException e) {
				//
			}
		}
	}
	
	
	private static long getStartTime(long time) {
		long start_time = time - (time-Work.BASE_SEC)%Work.DAY_IN_SEC;
		return start_time;
	}
}
