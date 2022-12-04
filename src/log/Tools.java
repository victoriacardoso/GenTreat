package log;

import java.io.IOException;
import java.sql.SQLException;

import cisa.Cisa;
import mauve.Ordering;
import megahit.PairedRead;
import prokka.Prokka;
import screen.CustomPanel;
import spades.SingleRead;

public class Tools {
	public void runSingleMegahit(int idproject, CustomPanel customPanel, int progress) {
		megahit.SingleRead megahitSingle = new megahit.SingleRead();
		megahitSingle.runMegahit(idproject);
		customPanel.addProgress(progress);
	}

	public void runPareadMegahit(int idproject, CustomPanel customPanel, int progress) {
		PairedRead paired = new PairedRead();
		paired.runMegahit(idproject);
		customPanel.addProgress(progress);
	}

	public void runSingleSPAdes(int idproject, CustomPanel customPanel, int progress) {
		SingleRead single = new SingleRead();
		single.runSpades(idproject);
		customPanel.addProgress(progress);
	}

	public void runPareadSPAdes(int idproject, CustomPanel customPanel, int progress) {
		spades.PairedRead pairedspades = new spades.PairedRead();
		pairedspades.runSpades(idproject);
		customPanel.addProgress(progress);
	}

	public void runCisa(int idproject, CustomPanel customPanel, int progress) {
		Cisa cisa = new Cisa();
		cisa.runMerge(idproject);
		cisa.runCisa(idproject);
		customPanel.addProgress(progress);
	}

	public void runMauve(int idproject, CustomPanel customPanel, int progress) throws SQLException, IOException {
		Ordering ordering = new Ordering();
		ordering.OrderContigs(idproject);
		customPanel.addProgress(progress);
	}

	public void runProkka(int idproject, CustomPanel customPanel, int progress)
			throws SQLException, IOException, InterruptedException {
		Prokka prokka = new Prokka();
		prokka.runProkka(idproject);
		Thread.sleep(5000);
		customPanel.addProgress(progress);
	}
}
