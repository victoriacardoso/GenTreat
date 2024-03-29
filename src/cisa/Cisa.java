package cisa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;

import database.DatabaseConnection;
import screen.ProjectLog;

public class Cisa {
	private String idproject;
	private String assemblyOutput;
	private Integer genomeSize;

	public String getIdproject() {
		return idproject;
	}

	public String getAssemblyOutput() {
		return assemblyOutput;
	}

	public int getGenomeSize() {
		return genomeSize;
	}

	public void runMerge(int idproject) {
		try {
			Statement statement;
			String cmmd;
			ResultSet resulSet;
			cmmd = "SELECT * FROM parameter WHERE idproject=" + idproject + ";";
			statement = DatabaseConnection.connect.createStatement();
			resulSet = statement.executeQuery(cmmd);

			while (resulSet.next()) {

				String assemblyOutput = resulSet.getString("output");

				File cisaFolder = new File(assemblyOutput + "/CISA");
				if (!cisaFolder.exists()) {
					cisaFolder.mkdir();
				}

				String cpAssemblySpades = "cp " + assemblyOutput + "/spades-assembly/scaffolds.fasta" + " "
						+ assemblyOutput + "/CISA/";
				CommandLine cpAssembly1 = CommandLine.parse(cpAssemblySpades);
				DefaultExecutor cpExecutor = new DefaultExecutor();
				cpExecutor.execute(cpAssembly1);

				String cpAssemblyMegahit = "cp " + assemblyOutput + "/megahit-assembly/final.contigs.fa" + " "
						+ assemblyOutput + "/CISA/";
				CommandLine cpAssembly2 = CommandLine.parse(cpAssemblyMegahit);
				DefaultExecutor cpExecutor2 = new DefaultExecutor();
				cpExecutor2.execute(cpAssembly2);

				BufferedWriter bw = new BufferedWriter(new FileWriter(assemblyOutput + "/CISA/Merge.config"));
				bw.write("count=2");
				bw.newLine();
				bw.write("data=" + assemblyOutput + "/CISA/scaffolds.fasta" + ",title=Contig1");
				bw.newLine();
				bw.write("data=" + assemblyOutput + "/CISA/final.contigs.fa" + ",title=Contig2");
				bw.newLine();
				bw.write("min_length=100");
				bw.newLine();
				bw.write("Master_file=" + assemblyOutput + "/CISA/Merged.ctg.fa");

				bw.flush();
				bw.close();

				String mergeCommand = "python2 " + "lib/CISA1.3/Merge.py " + assemblyOutput + "/CISA/Merge.config";

				PreparedStatement statmnt = null;
				statmnt = DatabaseConnection.connect.prepareStatement(
						"UPDATE project SET status =  'Running CISA' WHERE project.idproject=" + idproject + ";");
				statmnt.executeUpdate();

				Process p = Runtime.getRuntime().exec(mergeCommand);

				BufferedReader br;
				String linha;

				br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				PrintWriter pw = new PrintWriter(new FileWriter(assemblyOutput + "/log.txt", true));

				while ((linha = br.readLine()) != null) {
					pw.println(linha);
				}

				p.waitFor();
				pw.close();
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public void runCisa(int idproject) {
		try {
			Statement statement;
			String cmmd;
			ResultSet resulSet;
			cmmd = "SELECT * FROM parameter WHERE idproject=" + idproject + ";";
			statement = DatabaseConnection.connect.createStatement();
			resulSet = statement.executeQuery(cmmd);

			while (resulSet.next()) {
				String assemblyOutput = resulSet.getString("output");
				String genomeSize = resulSet.getString("cisaGenomeSize");

				BufferedWriter bw = new BufferedWriter(new FileWriter(assemblyOutput + "/CISA/CISA.config"));
				bw.write("genome=" + genomeSize);
				bw.newLine();
				bw.write("infile=" + assemblyOutput + "/CISA/Merged.ctg.fa");
				bw.newLine();
				bw.write("outfile=" + assemblyOutput + "/CISA/cisa.ctg.fa");
				bw.newLine();
				bw.write("nucmer=/usr/bin/nucmer");
				bw.newLine();
				bw.write("R2_Gap=0.95");
				bw.newLine();
				bw.write("CISA=lib/CISA1.3");
				bw.newLine();
				bw.write("makeblastdb=/usr/bin/makeblastdb");
				bw.newLine();
				bw.write("blastn=/usr/bin/blastn");

				bw.flush();
				bw.close();

				String cisaCommand = "python2 " + "lib/CISA1.3/CISA.py " + assemblyOutput + "/CISA/CISA.config";
				Process p = Runtime.getRuntime().exec(cisaCommand);

				BufferedReader br;
				String linha;

				br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				PrintWriter pw = new PrintWriter(new FileWriter(assemblyOutput + "/log.txt", true));

				while ((linha = br.readLine()) != null) {
					pw.println(linha);
				}
				pw.close();
				p.waitFor();

				br.close();

				PreparedStatement preparedStmt = null;
				String result_assembly = assemblyOutput + "/CISA/cisa.ctg.fa";

				new File(result_assembly).renameTo(new File(assemblyOutput + "/CISA/cisa.fasta"));

				preparedStmt = DatabaseConnection.connect.prepareStatement("UPDATE organism SET result_cisa= '"
						+ assemblyOutput + "/CISA/cisa.fasta" + "' WHERE idproject=" + idproject + ";");
				preparedStmt.executeUpdate();

				checkCisaFile(idproject, assemblyOutput);

				PreparedStatement stmt = null;
				stmt = DatabaseConnection.connect.prepareStatement(
						"UPDATE project SET status =  'Complete CISA' WHERE project.idproject=" + idproject + ";");
				stmt.executeUpdate();
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public void checkCisaFile(int idproject, String output) {
		try {
			Statement stm = null;
			String cmmdo;
			ResultSet rst;
			cmmdo = "SELECT * FROM organism WHERE idproject =" + idproject + ";";
			stm = DatabaseConnection.connect.createStatement();
			rst = stm.executeQuery(cmmdo);

			while (rst.next()) {
				String result_assembly = rst.getString("result_cisa");

				File a = new File(result_assembly);
				if (a.exists()) {
					if (a.length() == 0) {
						File cisaDirectory = new File(output + "/CISA");
						FileUtils.deleteDirectory(cisaDirectory);
						runMerge(idproject);
						runCisa(idproject);
					}

				} else {
					ProjectLog.getDialog().setTitle("Error");
					ProjectLog.getLblYouCanCheck().setText("Something went wrong with CISA.");
					ProjectLog.getEmptyField().setText("Please, check the CISA log file!");
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
