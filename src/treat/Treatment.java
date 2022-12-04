package treat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.exec.ExecuteException;

import screen.ProjectLog;

public class Treatment {
	public void treatRead(String read1, String read2, String output) throws ExecuteException, IOException, InterruptedException {
		String command = "lib/bbmap/repair.sh in1=" + read1 + " in2=" + read2 + " out1=" + output + "1_treated.fastq"
				+ " out2=" + output + "2_treated.fastq repair";
		Process p = Runtime.getRuntime().exec(command);

		BufferedReader br;
		String linha;

		br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		PrintWriter pw= new PrintWriter(new FileWriter(output + "/log.txt", true));

		while ((linha= br.readLine()) != null) {
			pw.println(linha);
		}
		pw.close();
		p.waitFor();
		
	}
	public void checkTreatedFile(String treated1, String treated2, String read1, String read2, String output,
			Treatment treatment) throws InterruptedException, ExecuteException, IOException {
		File a = new File(treated1);
		File a1 = new File(treated2);
		Thread.sleep(10000);
		if (a.exists() && a1.exists()) {
			if (a.length() == 0 && a1.length() == 0) {
				treatment.treatRead(read1, read2, output);

			}
		} else {
			ProjectLog.getDialog().setTitle("Error");
			ProjectLog.getLblYouCanCheck().setText("Something went wrong treating the files.");
			ProjectLog.getEmptyField().setText("Please, check the log file!");
			ProjectLog.getLblThankYouFor().setText("");
			ProjectLog.getDialog().setVisible(true);
			System.in.read();
		}

	}
}
