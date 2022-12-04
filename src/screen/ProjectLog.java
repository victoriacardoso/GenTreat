package screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import database.DatabaseConnection;
import log.Log;
import log.Status;
import log.Tools;
import prokka.Prokka;

public class ProjectLog {
	private static JPanel panelProjectLog;
	private static JTable tableLog;
	private static JScrollPane scrollPaneLog;
	private static String status;
	private static DefaultTableModel dft;
	private static JFrame frame;
	private static JMenu mnProject;
	private static String idRunAgain;
	private static int idInicio;
	private static JDialog dialog;
	private static JLabel emptyField;
	private static JLabel lblYouCanCheck;
	private static JLabel lblThankYouFor;

	private static String[] yesNoOptions = { "Yes", "No" };

	public static int getIdInicio() {
		return idInicio;
	}

	public static void setIdInicio(int idInicio) {
		ProjectLog.idInicio = idInicio;
	}

	public static JDialog getDialog() {
		return dialog;
	}

	public static JLabel getEmptyField() {
		return emptyField;
	}

	public String getId() {
		return idRunAgain;
	}

	public void setId(String id) {
		ProjectLog.idRunAgain = id;
	}

	public JFrame getFrame() {
		return frame;
	}

	public static String getStatus() {
		return status;
	}

	public static void setStatus(String status) {
		ProjectLog.status = status;
	}

	public static JLabel getLblYouCanCheck() {
		return lblYouCanCheck;
	}

	public static JLabel getLblThankYouFor() {
		return lblThankYouFor;
	}

	public void openScreen() {
		frame = new JFrame();
		frame.setBounds(100, 100, 654, 408);
		frame.setVisible(true);
		frame.setTitle("Project Log");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				if (JOptionPane.showOptionDialog(null, "Do you want to exit the program?", "Attention",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNoOptions,
						null) == JOptionPane.YES_OPTION) {
					System.exit(0);
				} else {
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

				}

			};
		});

		panelProjectLog = new JPanel();
		panelProjectLog.setBounds(130, 167, 160, 158);
		panelProjectLog.setLayout(null);
		frame.getContentPane().add(panelProjectLog);

		JPanel panelLog = new JPanel();
		panelLog.setBounds(65, 72, 461, 151);
		panelLog.setLayout(null);
		panelProjectLog.add(panelLog);

		JLabel lblProjectLog = new JLabel("Project Log");
		lblProjectLog.setBounds(12, 12, 230, 24);
		lblProjectLog.setFont(new Font("Dialog", Font.BOLD, 16));
		panelProjectLog.add(lblProjectLog);

		tableLog = new JTable();
		tableLog.setCellSelectionEnabled(true);
		tableLog.setShowVerticalLines(false);
		tableLog.setShowHorizontalLines(false);
		tableLog.setShowGrid(false);
		tableLog.setBackground(Color.WHITE);
		tableLog.setEnabled(false);

		dft = new DefaultTableModel();
		dft = (DefaultTableModel) tableLog.getModel();
		dft.addColumn("Status");
		dft.addColumn("Started Date");
		dft.addColumn("Finished Date");

		scrollPaneLog = new JScrollPane(tableLog);
		scrollPaneLog.setBounds(new Rectangle(0, 12, 452, 127));
		scrollPaneLog.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPaneLog.setEnabled(false);
		panelLog.add(scrollPaneLog);
		scrollPaneLog.setViewportView(tableLog);

		dialog = new JDialog(frame, "Complete Process");
		dialog.setBounds(450, 300, 537, 171);
		dialog.setResizable(false);
		dialog.getContentPane().setLayout(null);
		dialog.setLocationRelativeTo(null);

		JDialog enabledDialog = new JDialog(frame, "Attention");
		enabledDialog.setBounds(450, 300, 537, 171);
		enabledDialog.setResizable(false);
		enabledDialog.getContentPane().setLayout(null);
		enabledDialog.setLocationRelativeTo(null);

		JLabel enabledField = new JLabel("You cannot create a new project while there is one running!");
		enabledField.setBounds(0, 36, 527, 25);
		enabledField.setHorizontalAlignment(SwingConstants.CENTER);
		enabledDialog.getContentPane().add(enabledField);

		emptyField = new JLabel("");
		emptyField.setHorizontalAlignment(SwingConstants.CENTER);
		emptyField.setBounds(0, 36, 527, 25);
		dialog.getContentPane().add(emptyField);

		JButton btnOkAttention = new JButton("OK");
		btnOkAttention.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enabledDialog.dispose();

			}
		});
		btnOkAttention.setBounds(224, 100, 81, 19);
		enabledDialog.getContentPane().add(btnOkAttention);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		mnProject = new JMenu("Project");
		mnProject.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(mnProject.isEnabled()) {
					enabledDialog.setVisible(false);

				}
				else {
					enabledDialog.setVisible(true);
				}

			}
		});
		menuBar.add(mnProject);
		mnProject.setEnabled(false);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialog.dispose();

				try {
					Home home = new Home();
					Home.updateTable();
					home.getFrm_Home().setVisible(true);
			
					frame.dispose();

				} catch (Exception e) {
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
				}

			}
		});
		btnOk.setBounds(224, 100, 81, 19);
		dialog.getContentPane().add(btnOk);

		lblYouCanCheck = new JLabel("All done. You can check the results in this folder:");
		lblYouCanCheck.setBounds(124, 26, 360, 15);
		dialog.getContentPane().add(lblYouCanCheck);

		lblThankYouFor = new JLabel("Thank you for using GenTreat!");
		lblThankYouFor.setBounds(153, 73, 247, 15);
		dialog.getContentPane().add(lblThankYouFor);

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Input input = new Input();
				input.getFrame().setVisible(true);
				getFrame().dispose();
			}
		});
		btnBack.setBounds(22, 304, 78, 31);

	
	

		JMenuItem mntmCreate = new JMenuItem("Create");
		mntmCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Home home = new Home();
					home.getDialog().setVisible(true);
					frame.dispose();
				} catch (Exception e) {
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
				}
			}
		});
		mnProject.add(mntmCreate);

		JMenu mnAbout = new JMenu("About");
		mnAbout.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					Home home = new Home();
					home.getDialog2().setVisible(true);
				} catch (Exception ex) {
					System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
				}

			}
		});

		menuBar.add(mnAbout);

	}

	public static Runnable t1 = new Runnable() {

		@Override
		public void run() {
			try {

				if (idRunAgain != null) {
					Statement statement;
					String cmmd;
					ResultSet resulSet;
					cmmd = "SELECT * FROM parameter WHERE idproject=" + idRunAgain + ";";
					statement = DatabaseConnection.connect.createStatement();
					resulSet = statement.executeQuery(cmmd);

					while (resulSet.next()) {
						String output = resulSet.getString("output");

						File directory = new File(output);
						directory.mkdir();

						String projectName = null;
						Statement stmt = null;
						String command;
						ResultSet resultName;
						command = "SELECT * FROM project WHERE idproject=" + idRunAgain + ";";
						stmt = DatabaseConnection.connect.createStatement();
						resultName = stmt.executeQuery(command);

						while (resultName.next()) {
							projectName = resultName.getString("name");
						}
						System.out.println("Starting project: " + projectName);

						runTools(Integer.parseInt(idRunAgain));
					}
				} else {

					String projectName = null;
					Statement stmt = null;
					String command;
					ResultSet resultName;
					command = "SELECT * FROM project WHERE idproject=" + idInicio + ";";
					stmt = DatabaseConnection.connect.createStatement();
					resultName = stmt.executeQuery(command);

					projectName = resultName.getString("name");

					System.out.println("Starting project: " + projectName);

					runTools(idInicio);

				}

			} catch (Exception e) {
				e.printStackTrace();
				// System.err.println(e.getClass().getName() + ": " + e.getMessage());

			}
		}
	};

	public static Runnable t2 = new Runnable() {

		@Override
		public void run() {
			try {
				if (idRunAgain != null) {
					String order = "";
					Statement statement;
					String cmmd;
					ResultSet resulSet;
					cmmd = "SELECT * FROM parameter WHERE idproject=" + idRunAgain + ";";
					statement = DatabaseConnection.connect.createStatement();
					resulSet = statement.executeQuery(cmmd);

					while (resulSet.next()) {
						order = resulSet.getString("ordination");
					}
					if (order.equals("1")) {
						startLogProject(Integer.parseInt(idRunAgain), 0, 1, 2, 3, 4);
					} else {
						startLogProject(Integer.parseInt(idRunAgain), 0, 1, 2, 0, 3);
					}
				} else {
					String order = null;
					Statement statement;
					String cmmd;
					ResultSet resulSet;
					cmmd = "SELECT * FROM parameter WHERE idproject=" + idInicio + ";";
					statement = DatabaseConnection.connect.createStatement();
					resulSet = statement.executeQuery(cmmd);

					while (resulSet.next()) {
						order = resulSet.getString("ordination");

						if (order.equals("1")) {
							startLogProject(idInicio, 0, 1, 2, 3, 4);
						} else {
							startLogProject(idInicio, 0, 1, 2, 0, 3);
						}
					}
				}

			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}
	};
	public static Runnable t3 = new Runnable() {

		@Override
		public void run() {
			try {
				String singleRead = null;
				String output = "";
				String order = null;
				Tools tools = new Tools();

				CustomPanel customPanel = new CustomPanel();
				panelProjectLog.add(customPanel);
				customPanel.setBounds(236, 229, 125, 96);

				Statement stm = null;
				String cmmdo;
				ResultSet rst;
				cmmdo = "SELECT * FROM organism WHERE idproject=" + idRunAgain + ";";
				stm = DatabaseConnection.connect.createStatement();
				rst = stm.executeQuery(cmmdo);

				Statement statement;
				String cmmd;
				ResultSet resulSet;
				cmmd = "SELECT * FROM parameter WHERE idproject=" + idRunAgain + ";";
				statement = DatabaseConnection.connect.createStatement();
				resulSet = statement.executeQuery(cmmd);

				while (rst.next()) {
					singleRead = rst.getString("single");
				}
				while (resulSet.next()) {
					order = resulSet.getString("ordination");
					output = resulSet.getString("output");
				}
				String projectName = null;
				Statement stmt = null;
				String command;
				ResultSet resultName;
				command = "SELECT * FROM project WHERE idproject=" + idRunAgain + ";";
				stmt = DatabaseConnection.connect.createStatement();
				resultName = stmt.executeQuery(command);

				while (resultName.next()) {
					projectName = resultName.getString("name");
				}
				System.out.println("Resuming project: " + projectName);

				if (order.equals("1")) {
					if ((singleRead != null)) {
						customPanel.addProgress(20);
						tools.runSingleSPAdes(Integer.parseInt(idRunAgain), customPanel, 20);

						Thread.sleep(10000);

					} else {
						customPanel.addProgress(20);
						tools.runPareadSPAdes(Integer.parseInt(idRunAgain), customPanel, 20);

						Thread.sleep(10000);
					}
					tools.runCisa(Integer.parseInt(idRunAgain), customPanel, 20);
					Thread.sleep(10000);
					tools.runMauve(Integer.parseInt(idRunAgain), customPanel, 20);
					Thread.sleep(10000);
					tools.runProkka(Integer.parseInt(idRunAgain), customPanel, 20);
				} else {
					if ((singleRead != null)) {
						customPanel.addProgress(25);

						tools.runSingleSPAdes(Integer.parseInt(idRunAgain), customPanel, 25);

						Thread.sleep(10000);

					} else {
						customPanel.addProgress(25);
						tools.runPareadSPAdes(Integer.parseInt(idRunAgain), customPanel, 25);

						Thread.sleep(10000);
					}
					tools.runCisa(Integer.parseInt(idRunAgain), customPanel, 25);
					Thread.sleep(10000);
					tools.runProkka(Integer.parseInt(idRunAgain), customPanel, 25);
				}
				output = new File(output).getName();
				emptyField.setText(output);
				dialog.setVisible(true);
				mnProject.setEnabled(true);
				System.out.println("All done. Thank you for using GenTreat!");
				System.out.println("------------------------------------------");

			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}
	};
	public static Runnable t4 = new Runnable() {

		@Override
		public void run() {
			try {
				CustomPanel customPanel = new CustomPanel();
				panelProjectLog.add(customPanel);
				customPanel.setBounds(236, 229, 125, 96);

				String order = null;
				String output = "";
				Tools tools = new Tools();

				Statement statement;
				String cmmd;
				ResultSet resulSet;
				cmmd = "SELECT * FROM parameter WHERE idproject=" + idRunAgain + ";";
				statement = DatabaseConnection.connect.createStatement();
				resulSet = statement.executeQuery(cmmd);

				while (resulSet.next()) {
					order = resulSet.getString("ordination");
					output = resulSet.getString("output");

				}
				resulSet.close();
				String projectName = null;
				Statement stmt = null;
				String cmd;
				ResultSet resultName;
				cmd = "SELECT * FROM project WHERE idproject=" + idRunAgain + ";";
				stmt = DatabaseConnection.connect.createStatement();
				resultName = stmt.executeQuery(cmd);

				while (resultName.next()) {
					projectName = resultName.getString("name");
				}
				resultName.close();
				System.out.println("Resuming project: " + projectName);

				if (order.equals("1")) {
					customPanel.addProgress(40);
					tools.runCisa(Integer.parseInt(idRunAgain), customPanel, 20);

					Thread.sleep(5000);

					tools.runMauve(Integer.parseInt(idRunAgain), customPanel, 20);
					System.out.println("Aqui");
					Thread.sleep(10000);

					tools.runProkka(Integer.parseInt(idRunAgain), customPanel, 20);
				} else {
					customPanel.addProgress(50);
					tools.runCisa(Integer.parseInt(idRunAgain), customPanel, 25);

					Thread.sleep(10000);

					tools.runProkka(Integer.parseInt(idRunAgain), customPanel, 25);
				}
				Statement st = null;
				String command;
				ResultSet resultSet;
				command = "SELECT * FROM parameter WHERE idproject =" + idRunAgain + ";";
				st = DatabaseConnection.connect.createStatement();
				resultSet = st.executeQuery(command);

				resultSet.close();
				mnProject.setEnabled(true);
				output = new File(output).getName();
				emptyField.setText(output);
				dialog.setVisible(true);
				mnProject.setEnabled(true);
				System.out.println("All done. Thank you for using GenTreat!");
				System.out.println("------------------------------------------");
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}
	};
	public static Runnable t5 = new Runnable() {

		@Override
		public void run() {
			try {

				CustomPanel customPanel = new CustomPanel();
				panelProjectLog.add(customPanel);
				customPanel.setBounds(236, 229, 125, 96);

				String order = null;
				Tools tools = new Tools();

				Statement statement;
				String cmmd;
				ResultSet resulSet;
				cmmd = "SELECT * FROM parameter WHERE idproject=" + idRunAgain + ";";
				statement = DatabaseConnection.connect.createStatement();
				resulSet = statement.executeQuery(cmmd);

				String projectName = null;
				Statement stmt = null;
				String command;
				ResultSet resultName;
				command = "SELECT * FROM project WHERE idproject=" + idRunAgain + ";";
				stmt = DatabaseConnection.connect.createStatement();
				resultName = stmt.executeQuery(command);

				while (resultName.next()) {
					projectName = resultName.getString("name");
				}
				resultName.close();
				
				System.out.println("Resuming project: " + projectName);

				while (resulSet.next()) {
					order = resulSet.getString("ordination");
				}
				resulSet.close();
				if (order.equals("1")) {
					customPanel.addProgress(60);
					tools.runMauve(Integer.parseInt(idRunAgain), customPanel, 20);
					Thread.sleep(10000);
					tools.runProkka(Integer.parseInt(idRunAgain), customPanel, 20);
				} else {
					tools.runProkka(Integer.parseInt(idRunAgain), customPanel, 25);
					Thread.sleep(10000);
				}
				mnProject.setEnabled(true);
				emptyField.setText(projectName);
				dialog.setVisible(true);
				System.out.println("All done. Thank you for using GenTreat!");
				System.out.println("------------------------------------------");

			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}
	};
	public static Runnable t7 = new Runnable() {

		@Override
		public void run() {
			Status status = new Status();

			try {
				String order = null;

				Statement statement;
				String cmmd;
				ResultSet resulSet;
				cmmd = "SELECT * FROM parameter WHERE idproject=" + idRunAgain + ";";
				statement = DatabaseConnection.connect.createStatement();
				resulSet = statement.executeQuery(cmmd);

				while (resulSet.next()) {
					order = resulSet.getString("ordination");
				}
				resulSet.close();
				if (order.equals("1")) {
					if (status.checkStatus(Integer.parseInt(idRunAgain)).equals("Running SPAdes")
							|| status.checkStatus(Integer.parseInt(idRunAgain)).equals("Complete Megahit")) {
						startLogProject(Integer.parseInt(idRunAgain), 0, 0, 1, 2, 3);
					}
					if (status.checkStatus(Integer.parseInt(idRunAgain)).equals("Running CISA")
							|| status.checkStatus(Integer.parseInt(idRunAgain)).equals("Complete SPAdes")) {
						startLogProject(Integer.parseInt(idRunAgain), 0, 0, 0, 1, 2);
					}
					if (status.checkStatus(Integer.parseInt(idRunAgain)).equals("Running Mauve")
							|| status.checkStatus(Integer.parseInt(idRunAgain)).equals("Complete CISA")) {
						startLogProject(Integer.parseInt(idRunAgain), 0, 0, 0, 0, 1);
					}
					if (status.checkStatus(Integer.parseInt(idRunAgain)).equals("Running Prokka")
							|| status.checkStatus(Integer.parseInt(idRunAgain)).equals("Complete Mauve")) {
						startLogProject(Integer.parseInt(idRunAgain), 0, 0, 0, 0, 0);
					}
				} else {
					if (status.checkStatus(Integer.parseInt(idRunAgain)).equals("Running SPAdes")
							|| status.checkStatus(Integer.parseInt(idRunAgain)).equals("Complete Megahit")) {
						startLogProject(Integer.parseInt(idRunAgain), 0, 0, 1, 0, 2);
					}
					if (status.checkStatus(Integer.parseInt(idRunAgain)).equals("Running CISA")
							|| status.checkStatus(Integer.parseInt(idRunAgain)).equals("Complete SPAdes")) {
						startLogProject(Integer.parseInt(idRunAgain), 0, 0, 0, 0, 1);
					}
					if (status.checkStatus(Integer.parseInt(idRunAgain)).equals("Running Prokka")
							|| status.checkStatus(Integer.parseInt(idRunAgain)).equals("Complete CISA")) {
						startLogProject(Integer.parseInt(idRunAgain), 0, 0, 0, 0, 0);
					}
				}

			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}

		}
	};
	public static Runnable t8 = new Runnable() {

		@Override
		public void run() {
			try {
				Thread.sleep(8000);

				CustomPanel customPanel = new CustomPanel();
				panelProjectLog.add(customPanel);
				customPanel.setBounds(236, 229, 125, 96);

				String order;
				String output;

				Prokka prokka = new Prokka();

				String projectName = null;
				Statement stmt = null;
				String command;
				ResultSet resultName;
				command = "SELECT * FROM project WHERE idproject=" + idRunAgain + ";";
				stmt = DatabaseConnection.connect.createStatement();
				resultName = stmt.executeQuery(command);

				Statement statement;
				String cmmd;
				ResultSet resulSet;
				cmmd = "SELECT * FROM parameter WHERE idproject=" + idRunAgain + ";";
				statement = DatabaseConnection.connect.createStatement();
				resulSet = statement.executeQuery(cmmd);

				while (resulSet.next()) {
					order = resulSet.getString("ordination");
					output = resulSet.getString("output");

					while (resultName.next()) {
						projectName = resultName.getString("name");
						System.out.println("Resuming project: " + projectName);
					}
					resultName.close();
					if (order.equals("1")) {
						customPanel.addProgress(80);
						prokka.runProkka(Integer.parseInt(idRunAgain));
						customPanel.addProgress(20);

					} else {
						customPanel.addProgress(75);
						prokka.runProkka(Integer.parseInt(idRunAgain));
						customPanel.addProgress(25);

					}
					mnProject.setEnabled(true);
					output = new File(output).getName();
					emptyField.setText(projectName);
					dialog.setVisible(true);
					System.out.println("All done. Thank you for using GenTreat!");
					System.out.println("------------------------------------------");

				}
				resulSet.close();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}
	};

	public static void startLogProject(int idproject, int rowMegahit, int rowSpades, int rowCisa, int rowMauve,
			int rowProkka) {
		try {
			Thread.sleep(5000);

			Status status = new Status();
			String checkStatus = status.checkStatus(idproject);

			if (checkStatus.equals("Running Megahit")) {
				Log log = new Log();
				log.runMegahitLog(dft);

				while (status.checkStatus(idproject).equals("Running Megahit")) {
					Thread.sleep(30000);
				}
				Date date0 = new Date();
				DateFormat dateFormat1 = DateFormat.getInstance();
				String finishedDate = dateFormat1.format(date0);
				dft.setValueAt("Complete Megahit", rowMegahit, 0);
				dft.setValueAt(finishedDate, rowMegahit, 2);
				Thread.sleep(30000);
			}

			if (status.checkStatus(idproject).equals("Running SPAdes")) {
				Log log = new Log();
				log.runSpadesLog(dft);

				while (status.checkStatus(idproject).equals("Running SPAdes")) {
					Thread.sleep(30000);
				}
				Date date00 = new Date();
				DateFormat dateFormat00 = DateFormat.getInstance();
				String finishedDateSpades = dateFormat00.format(date00);
				dft.setValueAt("Complete Spades", rowSpades, 0);
				dft.setValueAt(finishedDateSpades, rowSpades, 2);
				Thread.sleep(30000);
			}
			if (status.checkStatus(idproject).equals("Running CISA")) {
				Log log = new Log();
				log.runCisaLog(dft);

				while (status.checkStatus(idproject).equals("Running CISA")) {
					Thread.sleep(30000);

				}

				Date date000 = new Date();
				DateFormat df = DateFormat.getInstance();
				String finishedDateCisa = df.format(date000);
				dft.setValueAt("Complete CISA", rowCisa, 0);
				dft.setValueAt(finishedDateCisa, rowCisa, 2);
				Thread.sleep(10000);
			}

			if (status.checkStatus(idproject).equals("Running Mauve")) {
				Log log = new Log();
				log.runMauveLog(dft);

				while (status.checkStatus(idproject).equals("Running Mauve")) {
					Thread.sleep(30000);
				}

				Date date0000 = new Date();
				DateFormat df0 = DateFormat.getInstance();
				String finishedDateRagoo = df0.format(date0000);
				dft.setValueAt("Complete Mauve", rowMauve, 0);
				dft.setValueAt(finishedDateRagoo, rowMauve, 2);
				Thread.sleep(30000);
			}

			if (status.checkStatus(idproject).equals("Running Prokka")) {
				Log log = new Log();
				log.runProkkaLog(dft);

				while (status.checkStatus(idproject).equals("Running Prokka")) {
					Thread.sleep(20000);
				}

				Date date00000 = new Date();
				DateFormat df00 = DateFormat.getInstance();
				String finishedDateProkka = df00.format(date00000);
				dft.setValueAt("Complete Prokka", rowProkka, 0);
				dft.setValueAt(finishedDateProkka, rowProkka, 2);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void runTools(int idproject) {
		try {
			String singleRead = null;
			String order = "";
			String output = "";
			Tools tools = new Tools();

			CustomPanel customPanel = new CustomPanel();
			panelProjectLog.add(customPanel);
			customPanel.setBounds(236, 229, 125, 96);

			Statement stm = null;
			String cmmdo;
			ResultSet rst;
			cmmdo = "SELECT * FROM organism WHERE idproject=" + idproject + ";";
			stm = DatabaseConnection.connect.createStatement();
			rst = stm.executeQuery(cmmdo);

			Statement statement;
			String cmmd;
			ResultSet resulSet;
			cmmd = "SELECT * FROM parameter WHERE idproject=" + idproject + ";";
			statement = DatabaseConnection.connect.createStatement();
			resulSet = statement.executeQuery(cmmd);

			while (rst.next()) {
				singleRead = rst.getString("single");
			}
			rst.close();

			while (resulSet.next()) {
				order = resulSet.getString("ordination");
				output = resulSet.getString("output");
			}
			resulSet.close();
			stm.close();
			statement.close();

			if (order.equals("1")) {
				if ((singleRead != null)) {
					tools.runSingleMegahit(idproject, customPanel, 20);

					Thread.sleep(10000);

					tools.runSingleSPAdes(idproject, customPanel, 20);

					Thread.sleep(10000);

				} else {
					tools.runPareadMegahit(idproject, customPanel, 20);

					Thread.sleep(10000);

					tools.runPareadSPAdes(idproject, customPanel, 20);

					Thread.sleep(10000);
				}

				tools.runCisa(idproject, customPanel, 20);
				Thread.sleep(5000);

				tools.runMauve(idproject, customPanel, 20);
				Thread.sleep(5000);

				tools.runProkka(idproject, customPanel, 20);
			} else {
				if ((singleRead != null)) {
					tools.runSingleMegahit(idproject, customPanel, 25);

					Thread.sleep(20000);

					tools.runSingleSPAdes(idproject, customPanel, 25);

					Thread.sleep(10000);

				} else {
					tools.runPareadMegahit(idproject, customPanel, 25);

					Thread.sleep(10000);

					tools.runPareadSPAdes(idproject, customPanel, 25);

					Thread.sleep(10000);
				}
				tools.runCisa(idproject, customPanel, 25);

				Thread.sleep(10000);

				tools.runProkka(idproject, customPanel, 25);
			}

			String projectName = null;
			Statement stmt = null;
			String command;
			ResultSet resultName;
			command = "SELECT * FROM project WHERE idproject=" + idRunAgain + ";";
			stmt = DatabaseConnection.connect.createStatement();
			resultName = stmt.executeQuery(command);

			while (resultName.next()) {
				projectName = resultName.getString("name");
				System.out.println("Resuming project: " + projectName);
			}
			resultName.close();
			mnProject.setEnabled(true);
			output = new File(output).getName();

			emptyField.setText(output);
			dialog.setVisible(true);
			System.out.println("All done. Thank you for using GenTreat!");
			System.out.println("------------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
