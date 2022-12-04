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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FileUtils;

import database.DatabaseConnection;
import net.proteanit.sql.DbUtils;

public class Home {

	private JFrame frm_Home;
	private JDialog dialogCreateProject;
	private JDialog dialogProjectEmpty;
	private JDialog dialogAbout;
	public static String selectedProject;
	public static Integer row;

	private JPanel panelExistingProjects;
	private static File checkExist;
	private static String[] yesNoOptions = { "Yes", "No" };
	private DefaultTableModel dft;
	public static JTable table;
	private JScrollPane scrollPaneExistingProjects;

	DatabaseConnection connect = new DatabaseConnection();

	public JDialog getDialog() {
		return dialogCreateProject;
	}

	public JFrame getFrm_Home() {
		return frm_Home;
	}

	public JDialog getDialog2() {
		return dialogAbout;
	}

	public Home() throws SQLException {
		initialize();
		existingProjects();
		DatabaseConnection.connect.close();
		connect.connectDatabase();

	}

	public void initialize() {
		frm_Home = new JFrame();
		frm_Home.setResizable(false);
		frm_Home.setTitle("GenTreat - a tool to perform automated hybrid assembly");
		frm_Home.setBounds(100, 100, 654, 430);
		frm_Home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm_Home.getContentPane().setLayout(null);
		frm_Home.setLocationRelativeTo(null);
		frm_Home.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				if (JOptionPane.showOptionDialog(null, "Do you want to exit the program?", "Attention",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNoOptions,
						null) == JOptionPane.OK_OPTION) {
					System.exit(0);
				} else {
					frm_Home.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

				}

			};
		});

		JLabel lblGentreat = new JLabel(new ImageIcon("pictures/GenTreat.png"));
		lblGentreat.setFont(new Font("Dialog", Font.BOLD, 18));
		lblGentreat.setForeground(Color.BLACK);
		lblGentreat.setBounds(154, 13, 329, 109);
		frm_Home.getContentPane().add(lblGentreat);

		dialogProjectEmpty = new JDialog(frm_Home, "Error");
		dialogProjectEmpty.setBounds(450, 300, 400, 150);
		dialogProjectEmpty.setResizable(false);
		dialogProjectEmpty.getContentPane().setLayout(null);
		dialogProjectEmpty.setLocationRelativeTo(null);
		JLabel fieldEmpty = new JLabel("Please, insert the project name");
		fieldEmpty.setBounds(99, 30, 236, 30);
		dialogProjectEmpty.getContentPane().add(fieldEmpty);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogProjectEmpty.dispose();
			}
		});
		btnOk.setBounds(176, 66, 66, 19);
		dialogProjectEmpty.getContentPane().add(btnOk);

		dialogCreateProject = new JDialog(frm_Home, "Create Project");
		dialogCreateProject.setBounds(450, 300, 400, 150);
		dialogCreateProject.setResizable(false);
		dialogCreateProject.setLocationRelativeTo(null);
		dialogCreateProject.getContentPane().setLayout(null);

		JLabel lblProjectName = new JLabel("Project Name");
		lblProjectName.setBounds(30, 32, 141, 20);
		dialogCreateProject.getContentPane().add(lblProjectName);

		JTextField textField_projectName = new JTextField();
		textField_projectName.setBounds(132, 33, 242, 19);
		textField_projectName.setColumns(10);
		dialogCreateProject.getContentPane().add(textField_projectName);

		panelExistingProjects = new JPanel();
		panelExistingProjects.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Existing Projects",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panelExistingProjects.setBounds(62, 134, 498, 162);
		frm_Home.getContentPane().add(panelExistingProjects);
		panelExistingProjects.setLayout(null);

		dialogAbout = new JDialog(frm_Home);
		dialogAbout.setTitle("About");
		dialogAbout.setBounds(420, 300, 474, 200);
		dialogAbout.setResizable(false);
		dialogAbout.getContentPane().setLayout(null);
		dialogAbout.setLocationRelativeTo(null);

		JLabel lblBIOD = new JLabel("BIOD Research Group");
		lblBIOD.setBounds(188, 60, 151, 15);
		dialogAbout.getContentPane().add(lblBIOD);

		JLabel logoBIOD = new JLabel(new ImageIcon("pictures/BIOD-en.png"));
		logoBIOD.setLocation(390, 12);
		logoBIOD.setHorizontalAlignment(SwingConstants.RIGHT);
		logoBIOD.setVerticalTextPosition(SwingConstants.BOTTOM);
		logoBIOD.setSize(77, 139);
		dialogAbout.getContentPane().add(logoBIOD);

		JLabel logoGenTreat = new JLabel(new ImageIcon("pictures/GenTreat-2.png"));
		logoGenTreat.setLocation(12, 12);
		logoGenTreat.setHorizontalAlignment(SwingConstants.RIGHT);
		logoGenTreat.setVerticalTextPosition(SwingConstants.BOTTOM);
		logoGenTreat.setSize(130, 139);
		dialogAbout.getContentPane().add(logoGenTreat);

		JLabel lblFederalUniversityOf = new JLabel("Federal University of Pará (UFPA)");
		lblFederalUniversityOf.setBounds(144, 74, 242, 15);
		dialogAbout.getContentPane().add(lblFederalUniversityOf);

		JLabel lblContact = new JLabel("E-mail: biodresearch@gmail.com");
		lblContact.setBounds(154, 101, 228, 15);
		dialogAbout.getContentPane().add(lblContact);

		JLabel lblGenTreat = new JLabel("GenTreat");
		lblGenTreat.setFont(new Font("Dialog", Font.BOLD, 20));
		lblGenTreat.setBounds(212, 0, 127, 75);
		dialogAbout.getContentPane().add(lblGenTreat);

		JLabel lblSite = new JLabel("Site: http://biod.ufpa.br/");
		lblSite.setBounds(188, 119, 178, 15);
		dialogAbout.getContentPane().add(lblSite);

		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String projectName = textField_projectName.getText();
				if (projectName.isBlank()) {
					dialogProjectEmpty.setVisible(true);

				} else {

					dialogCreateProject.dispose();
					getFrm_Home().dispose();
					SpadesAndMegahit spadesScreen = new SpadesAndMegahit();
					spadesScreen.getfrm_SpadesAndMegahit().setVisible(true);

					connect.insertProject(projectName);
					connect.insertParameters(null, null, null, null, null, null, null, null, null, null, null, null,
							null, null, "1");

				}

			}
		});
		btnNext.setBounds(296, 67, 78, 20);
		dialogCreateProject.getContentPane().add(btnNext);

		JMenuBar menuBar = new JMenuBar();
		frm_Home.setJMenuBar(menuBar);

		JMenu mnProject = new JMenu("Project");
		menuBar.add(mnProject);

		JMenuItem mntmCreate = new JMenuItem("Create");
		mntmCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogCreateProject.setVisible(true);
			}
		});
		mnProject.add(mntmCreate);

		JMenu mnAbout = new JMenu("About");
		mnAbout.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dialogAbout.setVisible(true);

			}
		});

		menuBar.add(mnAbout);

	}

	public void existingProjects() {
		JDialog dialogAttention = new JDialog(frm_Home, "Attention");
		dialogAttention.setBounds(450, 300, 430, 150);
		dialogAttention.getContentPane().setLayout(null);
		dialogAttention.setLocationRelativeTo(null);

		JLabel lblDoYouWant = new JLabel("Do you want to overwrite the files or create a new folder?");
		lblDoYouWant.setBounds(12, 44, 423, 25);
		dialogAttention.getContentPane().add(lblDoYouWant);

		JButton btnOverwrite = new JButton("Overwrite");
		btnOverwrite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Statement st = null;
					String cmd;
					ResultSet resultSet = null;
					cmd = "SELECT * FROM parameter WHERE idproject=" + selectedProject + ";";
					st = DatabaseConnection.connect.createStatement();
					resultSet = st.executeQuery(cmd);

					Statement statement;
					String cmmd;
					ResultSet resulSet = null;
					statement = DatabaseConnection.connect.createStatement();
					cmmd = "SELECT * FROM project WHERE idproject=" + selectedProject + ";";
					resulSet = statement.executeQuery(cmmd);

					String output = resultSet.getString("output");

					if (resulSet.getString("status").equals("Complete process")) {
						File folderToDelete = new File(output);
						FileUtils.deleteDirectory(folderToDelete);
						ProjectLog log = new ProjectLog();
						log.openScreen();
						log.setId(selectedProject);
						new Thread(ProjectLog.t1).start();
						new Thread(ProjectLog.t2).start();
						frm_Home.dispose();
					}

				} catch (Exception e) {
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
				}

			}
		});
		btnOverwrite.setBounds(22, 81, 117, 25);
		dialogAttention.getContentPane().add(btnOverwrite);

		JDialog dialogFolderName = new JDialog(frm_Home, "Folder Name");
		dialogFolderName.setBounds(450, 300, 430, 150);
		dialogFolderName.getContentPane().setLayout(null);
		dialogFolderName.setLocationRelativeTo(null);

		JLabel lblInsertTheFolder = new JLabel("Insert the folder name");
		lblInsertTheFolder.setBounds(114, 25, 171, 15);
		dialogFolderName.getContentPane().add(lblInsertTheFolder);

		JTextField textField = new JTextField();
		textField.setBounds(114, 52, 166, 19);
		dialogFolderName.getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblFolderAlreadyExists = new JLabel("Folder already exists!");
		lblFolderAlreadyExists.setForeground(Color.RED);
		lblFolderAlreadyExists.setFont(new Font("Dialog", Font.BOLD, 10));
		lblFolderAlreadyExists.setBounds(12, 98, 128, 15);
		dialogFolderName.getContentPane().add(lblFolderAlreadyExists);
		lblFolderAlreadyExists.setVisible(false);

		JButton btnNewButton2 = new JButton("OK");
		btnNewButton2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				PreparedStatement preparedStmt = null;
				try {
					String folderName = textField.getText();

					Statement st;
					String cmd;
					ResultSet rs = null;
					st = DatabaseConnection.connect.createStatement();
					cmd = "SELECT * FROM parameter WHERE idproject=" + selectedProject + ";";
					rs = st.executeQuery(cmd);

					String output = rs.getString("output");
					File outputFolder = new File(output);
					checkExist = new File(outputFolder.getParent() + "/" + folderName);
					if (checkExist.exists()) {
						lblFolderAlreadyExists.setVisible(true);
					}

					else {

						lblFolderAlreadyExists.setVisible(false);

						preparedStmt = DatabaseConnection.connect
								.prepareStatement("UPDATE parameter SET output= '" + checkExist.getParent() + "/"
										+ folderName + "' WHERE idproject=" + selectedProject + ";");
						preparedStmt.executeUpdate();

						Statement statement;
						String cmmd;
						ResultSet resulSet = null;
						statement = DatabaseConnection.connect.createStatement();
						cmmd = "SELECT * FROM project WHERE idproject=" + selectedProject + ";";
						resulSet = statement.executeQuery(cmmd);

						if (resulSet.getString("status").equals("Complete process")) {
							ProjectLog projectLog = new ProjectLog();
							projectLog.openScreen();
							projectLog.setId(selectedProject);
							new Thread(ProjectLog.t1).start();
							new Thread(ProjectLog.t2).start();
							frm_Home.dispose();
						}
					}

				} catch (Exception e) {
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
				}

			}

		});
		btnNewButton2.setBounds(158, 76, 71, 25);
		dialogFolderName.getContentPane().add(btnNewButton2);

		JButton btnNewButton = new JButton("New Folder");
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dialogFolderName.setVisible(true);

			}
		});
		btnNewButton.setBounds(275, 81, 117, 25);
		dialogAttention.getContentPane().add(btnNewButton);

		JLabel lblThereIsAlready = new JLabel("There is already a folder with the name of this project!");
		lblThereIsAlready.setBounds(22, 27, 421, 15);
		dialogAttention.getContentPane().add(lblThereIsAlready);

		JButton btnRunAgain = new JButton("Run Again");
		btnRunAgain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ProjectLog run = new ProjectLog();
				try {
					Statement statement;
					String cmmd;
					ResultSet resulSet = null;
					statement = DatabaseConnection.connect.createStatement();
					cmmd = "SELECT * FROM project WHERE idproject=" + selectedProject + ";";
					resulSet = statement.executeQuery(cmmd);

					Statement st = null;
					String cmd;
					ResultSet resultSet = null;
					cmd = "SELECT * FROM parameter WHERE idproject=" + selectedProject + ";";
					st = DatabaseConnection.connect.createStatement();
					resultSet = st.executeQuery(cmd);

					File folderOutput = new File(resultSet.getString("output"));
					if (resulSet.getString("status").equals("Complete process")) {

						if (folderOutput.exists()) {
							dialogAttention.setVisible(true);

						} else {
							run.setId(selectedProject);
							run.openScreen();
							new Thread(ProjectLog.t1).start();
							new Thread(ProjectLog.t2).start();
							frm_Home.dispose();
						}

					} else {

						if (folderOutput.exists()) {
							String output = resultSet.getString("output");
							if (resulSet.getString("status").equals("Running Megahit")) {
								File folderToDelete = new File(output);
								FileUtils.deleteDirectory(folderToDelete);
								ProjectLog log = new ProjectLog();
								log.setId(selectedProject);
								log.openScreen();
								runAgain(resulSet.getString("status"));
								frm_Home.dispose();
							}
							if (resulSet.getString("status").equals("Running SPAdes")
									|| resulSet.getString("status").equals("Complete Megahit")) {
								File folderToDelete = new File(output + "/spades-assembly");
								FileUtils.deleteDirectory(folderToDelete);
								ProjectLog log = new ProjectLog();
								log.setId(selectedProject);
								log.openScreen();
								runAgain(resulSet.getString("status"));
								frm_Home.dispose();
							}

							if (resulSet.getString("status").equals("Running CISA")
									|| resulSet.getString("status").equals("Complete SPAdes")) {
								File folderToDelete = new File(output + "/CISA");
								FileUtils.deleteDirectory(folderToDelete);
								ProjectLog log = new ProjectLog();
								log.setId(selectedProject);
								log.openScreen();
								runAgain(resulSet.getString("status"));
								frm_Home.dispose();
							}
							if (resulSet.getString("status").equals("Running Mauve")
									|| resulSet.getString("status").equals("Complete CISA")) {
								File folderToDelete = new File(output + "/Mauve");
								FileUtils.deleteDirectory(folderToDelete);
								File folderToDelete2 = new File(output + "/GenTreat");
								FileUtils.deleteDirectory(folderToDelete2);
								ProjectLog log = new ProjectLog();
								log.setId(selectedProject);
								log.openScreen();
								runAgain(resulSet.getString("status"));
								frm_Home.dispose();
							}
							if (resulSet.getString("status").equals("Running Prokka")
									|| resulSet.getString("status").equals("Complete Mauve")) {
								File folderToDelete = new File(output + "/Prokka");
								FileUtils.deleteDirectory(folderToDelete);
								ProjectLog log = new ProjectLog();
								log.setId(selectedProject);
								log.openScreen();
								runAgain(resulSet.getString("status"));
								frm_Home.dispose();
							}

						} else {
							run.setId(selectedProject);
							run.openScreen();
							runAgain(resulSet.getString("status"));
							frm_Home.dispose();
						}
					}

				} catch (Exception e) {
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
				}
			}
		});
		btnRunAgain.setBounds(245, 125, 117, 25);
		btnRunAgain.setVisible(false);
		panelExistingProjects.add(btnRunAgain);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					PreparedStatement stmt_delfield;
					String cmdd = "DELETE FROM project WHERE idproject=" + selectedProject + ";";
					stmt_delfield = DatabaseConnection.connect.prepareStatement(cmdd);
					stmt_delfield.execute();

					PreparedStatement st;
					String cmd = "DELETE FROM parameter WHERE idproject=" + selectedProject + ";";
					st = DatabaseConnection.connect.prepareStatement(cmd);
					st.execute();

					PreparedStatement stmt;
					String cd = "DELETE FROM organism WHERE idproject=" + selectedProject + ";";
					stmt = DatabaseConnection.connect.prepareStatement(cd);
					stmt.execute();

					updateTable();

					btnDelete.setEnabled(false);
					btnRunAgain.setEnabled(false);
				} catch (Exception e0) {
					System.err.println(e0.getClass().getName() + ": " + e0.getMessage());
				}

			}
		});
		btnDelete.setBounds(369, 125, 117, 25);
		btnDelete.setVisible(false);
		panelExistingProjects.add(btnDelete);

		table = new JTable();
		table.setFocusTraversalKeysEnabled(false);
		table.setFocusable(false);
		table.setEnabled(true);
		table.setDefaultEditor(Object.class, null);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);

		dft = new DefaultTableModel();
		dft = (DefaultTableModel) table.getModel();
		dft.addColumn("id");
		dft.addColumn("Name");
		dft.addColumn("Status");
		dft.addColumn("Creation Date");

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				int valor = (int) table.getValueAt(table.rowAtPoint(e.getPoint()), 0);
				selectedProject = Integer.toString(valor);
				btnDelete.setVisible(true);
				btnDelete.setEnabled(true);

				try {
					String status = (String) table.getValueAt(table.rowAtPoint(e.getPoint()), 2);
					if (status.equals("Complete process")) {
						btnRunAgain.setVisible(true);
						btnRunAgain.setEnabled(true);
						btnRunAgain.setText("Run Again");

					} else {
						btnRunAgain.setVisible(true);
						btnRunAgain.setEnabled(true);
						btnRunAgain.setText("Continue");

					}

				} catch (Exception ex) {
					System.err.println(ex.getClass().getName() + ": " + ex.getMessage());
				}

			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(221);
		table.getColumnModel().getColumn(2).setPreferredWidth(163);
		table.getColumnModel().getColumn(3).setPreferredWidth(163);

		scrollPaneExistingProjects = new JScrollPane(table);
		scrollPaneExistingProjects.setBounds(new Rectangle(12, 23, 474, 90));
		scrollPaneExistingProjects.setBorder(new EmptyBorder(0, 0, 0, 0));
		scrollPaneExistingProjects.setEnabled(false);
		panelExistingProjects.add(scrollPaneExistingProjects);
		scrollPaneExistingProjects.setViewportView(table);

	}

	public static void runAgain(String status) {
		if (status.equals("Running Megahit")) {
			new Thread(ProjectLog.t1).start();
			new Thread(ProjectLog.t2).start();
		}
		if (status.equals("Complete Megahit") || status.equals("Running SPAdes")) {
			new Thread(ProjectLog.t7).start();
			new Thread(ProjectLog.t3).start();
		}
		if (status.equals("Complete SPAdes") || status.equals("Running CISA")) {
			new Thread(ProjectLog.t4).start();
			new Thread(ProjectLog.t7).start();
		}
		if (status.equals("Complete CISA") || status.equals("Running Mauve")) {
			new Thread(ProjectLog.t7).start();
			new Thread(ProjectLog.t5).start();
		}
		if (status.equals("Complete Mauve") || status.equals("Running Prokka")) {
			new Thread(ProjectLog.t7).start();
			new Thread(ProjectLog.t8).start();
		}
	}

	public static void updateTable() {

		try {
			PreparedStatement statement;
			String cmmd;
			ResultSet resulSet = null;
			cmmd = "SELECT * FROM project;";
			statement = DatabaseConnection.connect.prepareStatement(cmmd);
			resulSet = statement.executeQuery();

			table.setModel(DbUtils.resultSetToTableModel(resulSet));
			statement.close();
			resulSet.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	public static void main(String[] args) throws SQLException {
		DatabaseConnection connect = new DatabaseConnection();
		connect.start();
		connect.checkFieldsDataBase();
		Home home = new Home();
		updateTable();
		home.getFrm_Home().setVisible(true);

	}
}
