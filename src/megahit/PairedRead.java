package megahit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;

import database.DatabaseConnection;
import screen.ProjectLog;
import treat.Treatment;

public class PairedRead {
	private String paired1;
	private String paired2;
	private String idorganism;

	public String getPaired1() {
		return paired1;
	}

	public void setPaired1(String paired1) {
		this.paired1 = paired1;
	}

	public String getPaired2() {
		return paired2;
	}

	public void setPaired2(String paired2) {
		this.paired2 = paired2;
	}

	public String getIdorganism() {
		return idorganism;
	}

	public void runMegahit(int idproject) {
		try {
			Statement st = null;
			String cmd;
			ResultSet rs;
			cmd = "SELECT * FROM organism WHERE idproject=" + idproject
					+ " AND paired1 is not null AND paired2 is not null;";
			st = DatabaseConnection.connect.createStatement();
			rs = st.executeQuery(cmd);
			while (rs.next()) {
				String paired1 = rs.getString("paired1");
				String paired2 = rs.getString("paired2");
				setPaired1(paired1);
				setPaired2(paired2);
			}
			Statement statement;
			String cmmd;
			ResultSet resulSet;
			cmmd = "SELECT * FROM parameter WHERE idproject=" + idproject + ";";
			statement = DatabaseConnection.connect.createStatement();
			resulSet = statement.executeQuery(cmmd);
			String output = "";
			String orientation = "";
			String mem_flag = "";
			String min_count = "";
			String megahit_kmers = "";

			String forwardReads;
			String reverseReads;

			while (resulSet.next()) {
				output = resulSet.getString("output");
				orientation = resulSet.getString("orientation");
				mem_flag = resulSet.getString("mem_flag");
				min_count = resulSet.getString("min_count");
				megahit_kmers = resulSet.getString("megahit_kmers");
			}
			forwardReads = "-1";
			reverseReads = "-2";

			PreparedStatement sta = null;
			sta = DatabaseConnection.connect.prepareStatement(
					"UPDATE project SET status =  'Running Megahit' WHERE project.idproject=" + idproject + ";");
			sta.executeUpdate();

			Treatment treatment = new Treatment();
			treatment.treatRead(getPaired1(), getPaired2(), output + "/");
			Thread.sleep(120000);
			treatment.checkTreatedFile(output + "/1_treated.fastq", output + "/2_treated.fastq", getPaired1(), getPaired2(),
					output, treatment);

			switch (orientation) {

			case "fr":
				String assemblyCommand = "/opt/megahit/bin/megahit " + forwardReads + " " + output + "/1_treated.fastq"
						+ " " + reverseReads + " " + output + "/2_treated.fastq" + " --mem-flag " + mem_flag
						+ " --min-count " + min_count + " --k-list " + megahit_kmers + " -o " + output
						+ "/megahit-assembly";

				Process p = Runtime.getRuntime().exec(assemblyCommand);
				BufferedReader br;
				String linha;

				br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				PrintWriter pw = new PrintWriter(new FileWriter(output + "/log.txt", true));

				while ((linha = br.readLine()) != null) {
					pw.println(linha);
				}

				p.waitFor();
				pw.close();

				break;
			case "rf":
				String assemblyCmmd = "/opt/megahit/bin/megahit " + forwardReads + " " + output + "/2_treated.fastq"
						+ " " + reverseReads + " " + output + "/1_treated.fastq" + " -o " + output
						+ "/megahit-assembly";

				Process p2 = Runtime.getRuntime().exec(assemblyCmmd);

				br = new BufferedReader(new InputStreamReader(p2.getErrorStream()));
				pw = new PrintWriter(new FileWriter(output + "/log.txt", true));

				while ((linha = br.readLine()) != null) {
					pw.println(linha);
				}

				p2.waitFor();
				pw.close();
				break;

			}
			checkMegahitFile(idproject);

			PreparedStatement stat = null;
			stat = DatabaseConnection.connect.prepareStatement(
					"UPDATE project SET status =  'Complete Megahit' WHERE project.idproject=" + idproject + ";");
			stat.executeUpdate();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public void checkMegahitFile(int idproject) {
		try {

			Statement statement;
			String cmmd;
			ResultSet resulSet;
			cmmd = "SELECT * FROM parameter WHERE idproject=" + idproject + ";";
			statement = DatabaseConnection.connect.createStatement();
			resulSet = statement.executeQuery(cmmd);
			while (resulSet.next()) {
				String output = resulSet.getString("output");
				File a = new File(output + "/megahit-assembly/final.contigs.fa");
				Thread.sleep(10000);
				if (a.exists()) {
					if (a.length() == 0) {
						File megahitDirectory = new File(output + "/megahit-assembly");
						FileUtils.deleteDirectory(megahitDirectory);
						runMegahit(idproject);
					}

				} else {
					ProjectLog.getDialog().setTitle("Error");
					ProjectLog.getLblYouCanCheck().setText("Something went wrong with Megahit.");
					ProjectLog.getEmptyField().setText("Please, check the Megahit log file!");
					ProjectLog.getLblThankYouFor().setText("");
					ProjectLog.getDialog().setVisible(true);
					System.in.read();
				}
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	
}
