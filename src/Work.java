import java.awt.List;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Work {
	
	public static long BASE_SEC = 1458360000; // Local Time March 19th 00:00, data starts here
	public static long DAY_IN_SEC = 86400;
	public static long RECORD_LIMIT = 500; // if day has less than 500 record, do not calculate it
	public static double SLEEP_POWER = 23.5;

	public static void main(String[] args) {
		String root_path = "/home/xingliu/Work/server_backup/data_new";
		File root_dir = new File(root_path);
		File[] devices = root_dir.listFiles();
		Arrays.sort(devices);
		
		for(File device : devices) {
			//get the start second of the day which we calculate E of this device
			ArrayList<Long> start_secs = new ArrayList<Long>();
			GetStartSec.work(start_secs, device);
			
			double sleep_e = GetSleepEnergy.work(start_secs, device);
			System.out.println(sleep_e);
		}
	}
	
	public static boolean validTime(ArrayList<Long> start_secs, long time) {
		for(long sec : start_secs) {
			if(time >= sec && time <= sec+DAY_IN_SEC-1) {
				return true;
			} 
		}
		
		return false;
	}
}
