package prokka;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;

import database.DatabaseConnection;
import screen.ProjectLog;

public class Prokka {
	public void runProkka(int idproject) throws SQLException, InterruptedException, IOException {
		String ordered_file = null;
		String result_cisa = null;
		String single = null;
		String paired = null;
		String locusTag = null;
		String output = null;
		String order = null;
		String command = null;
		String cpu = null, genus_name = null, kingdom = null, genCode = null;

		Statement stmt = null;
		String cmmdo;
		ResultSet result;
		cmmdo = "SELECT * FROM organism WHERE idproject =" + idproject + ";";
		stmt = DatabaseConnection.connect.createStatement();
		result = stmt.executeQuery(cmmdo);

		while (result.next()) {
			ordered_file = result.getString("ordered_file");
			result_cisa = result.getString("result_cisa");
			single = result.getString("single");
			paired = result.getString("paired1");
		}
		if (single != null) {
			locusTag = defineLocusTag(single);
		} else {
			locusTag = defineLocusTag(paired);
		}

		Statement statement;
		String cmmd;
		ResultSet resulSet;
		cmmd = "SELECT * FROM parameter WHERE idproject=" + idproject + ";";
		statement = DatabaseConnection.connect.createStatement();
		resulSet = statement.executeQuery(cmmd);
		while (resulSet.next()) {
			output = resulSet.getString("output");
			order = resulSet.getString("ordination");
			genus_name = resulSet.getString("genusName");
			cpu = resulSet.getString("cpu");
			kingdom = resulSet.getString("kingdom");
			genCode = resulSet.getString("genCode");
		}

		File prokkaFolder = new File(output + "/Prokka");
		if (prokkaFolder.exists()) {
			FileUtils.deleteDirectory(prokkaFolder);
		}

		Runtime runTime = Runtime.getRuntime();

		PreparedStatement st = null;
		st = DatabaseConnection.connect.prepareStatement(
				"UPDATE project SET status =  'Running Prokka' WHERE project.idproject=" + idproject + ";");
		st.executeUpdate();

		if (order.equals("1")) {
			command = "/opt/prokka/bin/prokka --locustag " + locusTag + " --prefix  " + locusTag + " --genus "
					+ genus_name + " --cpus " + cpu + " --kingdom " + kingdom + " --gcode " + genCode + " --outdir "
					+ output + "/Prokka " + ordered_file;
		} else {
			command = "/opt/prokka/bin/prokka --locustag " + locusTag + " --prefix " + locusTag + " --genus "
					+ genus_name + " --cpus " + cpu + " --kingdom " + kingdom + " --gcode " + genCode + " --outdir "
					+ output + "/Prokka " + result_cisa;
		}
		Process p = runTime.exec(command);

		BufferedReader br;
		String linha;

		br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		PrintWriter pw = new PrintWriter(new FileWriter(output + "/log.txt", true));

		while ((linha = br.readLine()) != null) {
			pw.println(linha);
		}
		pw.close();

		p.waitFor();

		checkProkkaFile(idproject);

		PreparedStatement st2 = null;
		st2 = DatabaseConnection.connect.prepareStatement(
				"UPDATE project SET status =  'Complete process' WHERE project.idproject=" + idproject + ";");
		st2.executeUpdate();

	}

	public String defineLocusTag(String input) {
		String[] name = input.split("/");
		String[] locusTag = name[name.length - 1].split("[._]");

		return locusTag[0];
	}

	public void checkProkkaFile(int idproject)
			throws NumberFormatException, SQLException, InterruptedException, IOException {
		Statement statement;
		String cmmd;
		ResultSet resulSet;
		cmmd = "SELECT * FROM parameter WHERE idproject=" + idproject + ";";
		statement = DatabaseConnection.connect.createStatement();
		resulSet = statement.executeQuery(cmmd);

		String output = resulSet.getString("output");

		File a = new File(output + "/Prokka/");
		Thread.sleep(10000);
		if (a.exists()) {
			if (a.length() == 0) {
				File prokkaDirectory = new File(output + "/Prokka");
				FileUtils.deleteDirectory(prokkaDirectory);
				runProkka(idproject);
			}

		} else {
			ProjectLog.getDialog().setTitle("Error");
			ProjectLog.getLblYouCanCheck().setText("Something went wrong with Prokka.");
			ProjectLog.getEmptyField().setText("Please, check the Prokka log file!");
			ProjectLog.getLblThankYouFor().setText("");
			ProjectLog.getDialog().setVisible(true);
			System.in.read();
		}
	}
}
