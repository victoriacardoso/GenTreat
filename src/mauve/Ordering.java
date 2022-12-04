package mauve;

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

public class Ordering {
	public void OrderContigs(int id) throws SQLException, IOException {
		String result_cisa = "";
		String reference = "";
		Statement stmt = null;
		String cmmdo;
		ResultSet result;
		cmmdo = "SELECT * FROM organism WHERE idproject =" + id + ";";
		stmt = DatabaseConnection.connect.createStatement();
		result = stmt.executeQuery(cmmdo);
		while (result.next()) {
			result_cisa = result.getString("result_cisa");
			reference = result.getString("reference");
		}
		String output = "";

		Statement statement;
		String cmmd;
		ResultSet resulSet;

		cmmd = "SELECT * FROM parameter WHERE idproject=" + id + ";";
		statement = DatabaseConnection.connect.createStatement();
		resulSet = statement.executeQuery(cmmd);
		while (resulSet.next()) {
			output = resulSet.getString("output");
		}
		String path = "";
		String pt[] = result_cisa.split("/");

		new File(output + "/Mauve").mkdir();

		String command1 = "bash lib/mauv.sh " + output + "/Mauve" + " " + reference + " " + result_cisa;
		try {
			PreparedStatement stmtOrder = null;
			stmtOrder = DatabaseConnection.connect.prepareStatement(
					"UPDATE project SET status =  'Running Mauve' WHERE project.idproject=" + id + ";");
			stmtOrder.executeUpdate();

			Process p = Runtime.getRuntime().exec(command1);
			BufferedReader br;
			String linha;

			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			PrintWriter pw = new PrintWriter(new FileWriter(output + "/Mauve/" + "log.txt"));
			PrintWriter pw2 = new PrintWriter(new FileWriter(output + "/log.txt", true));

			while ((linha = br.readLine()) != null) {
				pw.println(linha);
				pw2.println(linha);

			}
			p.waitFor();
			pw.close();
			pw2.close();
			
			br.close();
			
			Result ge = new Result();
			path = ge.GetResult(output + "/Mauve/" + "log.txt");

			PreparedStatement preparedStmt = null;
			String ordered_file = output + "/Mauve/" + "alignment" + path.trim() + "/" + pt[pt.length - 1];

			new File(ordered_file).renameTo(new File(output + "/Mauve" + "/mauve.fasta"));

			FileUtils.copyFile(new File(output + "/Mauve" + "/mauve.fasta"), new File(output + "/GenTreat.fasta"));

			preparedStmt = DatabaseConnection.connect.prepareStatement("UPDATE organism SET ordered_file= '" + output
					+ "/Mauve/mauve.fasta" + "' WHERE idproject=" + id + ";");
			preparedStmt.executeUpdate();

			checkFileMauve(id, output);

			PreparedStatement stmtOrderFinish = null;
			stmtOrderFinish = DatabaseConnection.connect
					.prepareStatement("UPDATE project SET status =  'Complete Mauve' WHERE project.idproject=" + id + ";");
			stmtOrderFinish.executeUpdate();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

		

	}

	public void checkFileMauve(int id, String output) throws SQLException, IOException {

		File a = new File(output + "/Mauve/mauve.fasta");

		if (a.exists()) {
			if (a.length() == 0) {
				FileUtils.deleteDirectory(new File(output + "/Mauve"));
				OrderContigs(id);
			}

		} else {
			ProjectLog.getDialog().setTitle("Error");
			ProjectLog.getLblYouCanCheck().setText("Something went wrong with Mauve.");
			ProjectLog.getEmptyField().setText("Please, check the Mauve log file!");
			ProjectLog.getLblThankYouFor().setText("");
			ProjectLog.getDialog().setVisible(true);
			System.in.read();
		}
	}
}
