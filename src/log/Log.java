package log;

import java.text.DateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableModel;

public class Log {
	public void runMegahitLog(DefaultTableModel dft) throws InterruptedException {

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getInstance();
		String startDate = dateFormat.format(date);

		dft.addRow(new Object[] { "Running Megahit", startDate, null });
	}

	public void runSpadesLog(DefaultTableModel dft) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getInstance();
		String startDate = dateFormat.format(date);

		dft.addRow(new Object[] { "Running SPades", startDate, null });
	}

	public void runCisaLog(DefaultTableModel dft) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getInstance();
		String startDate = dateFormat.format(date);

		dft.addRow(new Object[] { "Running CISA", startDate, null });
	}

	public void runMauveLog(DefaultTableModel dft) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getInstance();
		String startDate = dateFormat.format(date);

		dft.addRow(new Object[] { "Running Mauve", startDate, null });
	}

	public void runProkkaLog(DefaultTableModel dft) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getInstance();
		String startDate = dateFormat.format(date);

		dft.addRow(new Object[] { "Running Prokka", startDate, null });

	}

}
