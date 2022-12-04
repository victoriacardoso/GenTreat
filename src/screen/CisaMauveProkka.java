package screen;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import database.DatabaseConnection;
import java.awt.Font;

public class CisaMauveProkka {
	private JFrame frmParameters;

	private JTextField textField_GenomeSize;
	private JTextField textField_MinLength;
	private JTextField textField_R2Gap;
	private JDialog dialogEmpty;
	private static String[] yesNoOptions = { "Yes", "No" };
	private JTextField txtGenus;

	public CisaMauveProkka() {
		initialize();
	}

	private void initialize() {
		frmParameters = new JFrame();
		frmParameters.setResizable(false);
		frmParameters.setTitle("Parameters");
		frmParameters.setBounds(100, 100, 654, 408);
		frmParameters.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmParameters.setLocationRelativeTo(null);
		frmParameters.getContentPane().setLayout(null);
		frmParameters.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				if (JOptionPane.showOptionDialog(null, "Do you want to exit the program?", "Attention",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNoOptions,
						null) == JOptionPane.OK_OPTION) {
					try {
						int idproject = 0;
						Statement st = null;
						st = DatabaseConnection.connect.createStatement();
						idproject = st.executeQuery("select last_insert_rowid()").getInt(1);

						Statement stmt_delfield = DatabaseConnection.connect.createStatement();
						String cmdd = "DELETE FROM parameter WHERE idproject=" + idproject + ";";
						stmt_delfield.executeUpdate(cmdd);

						System.exit(0);
					} catch (Exception e) {
						System.err.println(e.getClass().getName() + ": " + e.getMessage());
					}
				} else {
					frmParameters.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

				}

			};
		});

		JMenuBar menuBar = new JMenuBar();
		frmParameters.setJMenuBar(menuBar);

		JMenu mnProject = new JMenu("Project");
		menuBar.add(mnProject);

		JMenuItem mntmCreate = new JMenuItem("Create");
		mntmCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Home home;
				try {
					home = new Home();
					home.getDialog().setVisible(true);

				} catch (Exception e) {
					// TODO Auto-generated catch block
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

		JPanel panelCisa = new JPanel();
		panelCisa.setLayout(null);
		panelCisa.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "CISA Parameters",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panelCisa.setBounds(12, 28, 616, 80);
		frmParameters.getContentPane().add(panelCisa);

		JLabel lblGenomeSize = new JLabel("Genome size");
		lblGenomeSize.setBounds(368, 25, 112, 15);
		panelCisa.add(lblGenomeSize);

		textField_GenomeSize = new JTextField();
		textField_GenomeSize.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String caracteres = "0987654321";
				if (!caracteres.contains(e.getKeyChar() + "")) {
					e.consume();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
					try {
						if (e.isControlDown()
								&& !clipboard.getData(DataFlavor.stringFlavor).toString().matches("[0-9]*")) {
							e.consume();

						}
					} catch (UnsupportedFlavorException | IOException e1) {
						System.err.println(e1.getClass().getName() + ": " + e1.getMessage());
					}
				}

			}
		});
		textField_GenomeSize.setColumns(10);
		textField_GenomeSize.setBounds(470, 23, 70, 19);
		panelCisa.add(textField_GenomeSize);

		JLabel lblMinimumLength = new JLabel("Minimum length");
		lblMinimumLength.setBounds(12, 25, 112, 15);
		panelCisa.add(lblMinimumLength);

		textField_MinLength = new JTextField();
		textField_MinLength.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String caracteres = "0987654321";

				if (!caracteres.contains(e.getKeyChar() + "")) {
					e.consume();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
					try {
						if (e.isControlDown()
								&& !clipboard.getData(DataFlavor.stringFlavor).toString().matches("[0-9]*")) {
							e.consume();

						}
					} catch (UnsupportedFlavorException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		textField_MinLength.setText("100");
		textField_MinLength.setColumns(10);
		textField_MinLength.setBounds(129, 25, 70, 19);
		panelCisa.add(textField_MinLength);

		JLabel lblGenomeSize_1 = new JLabel("R2_Gap");
		lblGenomeSize_1.setBounds(12, 52, 112, 15);
		panelCisa.add(lblGenomeSize_1);

		textField_R2Gap = new JTextField();
		textField_R2Gap.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String caracteres = "0987654321.";

				if (!caracteres.contains(e.getKeyChar() + "")) {
					e.consume();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
					try {
						if (e.isControlDown()
								&& !clipboard.getData(DataFlavor.stringFlavor).toString().matches("[\\.0-9]*")) {
							e.consume();

						}
					} catch (UnsupportedFlavorException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		textField_R2Gap.setText("0.95");
		textField_R2Gap.setColumns(10);
		textField_R2Gap.setBounds(129, 52, 70, 19);
		panelCisa.add(textField_R2Gap);

		JLabel lblAmountOfBase = new JLabel("bp");
		lblAmountOfBase.setFont(new Font("Dialog", Font.BOLD, 12));
		lblAmountOfBase.setBounds(544, 23, 24, 19);
		panelCisa.add(lblAmountOfBase);

		JPanel panelProkka = new JPanel();
		panelProkka.setLayout(null);
		panelProkka.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Prokka Parameters",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panelProkka.setBounds(12, 184, 616, 89);
		frmParameters.getContentPane().add(panelProkka);

		JLabel lblGenusName = new JLabel("Genus Name");
		lblGenusName.setBounds(12, 50, 127, 15);
		panelProkka.add(lblGenusName);

		txtGenus = new JTextField();
		txtGenus.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isAlphabetic(e.getKeyChar())) {
					e.consume();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
					try {
						if (e.isControlDown()
								&& !clipboard.getData(DataFlavor.stringFlavor).toString().matches("[a-zA-Z]*")) {
							e.consume();

						}
					} catch (UnsupportedFlavorException | IOException e1) {
						e1.printStackTrace();
					}
				}

			}

		});
		txtGenus.setText("Genus");
		txtGenus.setBounds(114, 48, 114, 19);
		panelProkka.add(txtGenus);
		txtGenus.setColumns(10);

		JSpinner spinnerCpu = new JSpinner();
		spinnerCpu.setModel(new SpinnerNumberModel(8, null, null, 1));
		SpinnerNumberModel snm = new SpinnerNumberModel();
		snm.setValue(8);
		spinnerCpu.setBounds(461, 50, 48, 20);
		panelProkka.add(spinnerCpu);

		JLabel lblCpu = new JLabel("CPU");
		lblCpu.setBounds(408, 52, 70, 15);
		panelProkka.add(lblCpu);

		dialogEmpty = new JDialog(frmParameters, "Error");
		dialogEmpty.setBounds(450, 300, 400, 150);
		dialogEmpty.setResizable(false);
		dialogEmpty.getContentPane().setLayout(null);
		dialogEmpty.setLocationRelativeTo(null);

		JLabel emptyField = new JLabel("There are empty fields!");
		emptyField.setBounds(104, 24, 184, 30);
		dialogEmpty.getContentPane().add(emptyField);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogEmpty.dispose();
			}
		});
		btnOk.setBounds(154, 66, 66, 19);
		dialogEmpty.getContentPane().add(btnOk);

		JPanel panelMauve = new JPanel();
		panelMauve.setBounds(12, 124, 616, 48);
		frmParameters.getContentPane().add(panelMauve);
		panelMauve.setLayout(null);
		panelMauve.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Mauve", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));

		JRadioButton rdbtnYes = new JRadioButton("Yes");
		JRadioButton rdbtnNo = new JRadioButton("No");

		rdbtnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnYes.setSelected(false);
				try {
					int idproject = 0;
					Statement st = null;
					st = DatabaseConnection.connect.createStatement();
					idproject = st.executeQuery("select last_insert_rowid()").getInt(1);

					PreparedStatement stat = null;
					stat = DatabaseConnection.connect
							.prepareStatement("UPDATE parameter SET ordination=0" + " WHERE idproject=" + idproject);
					stat.executeUpdate();
				} catch (Exception e) {
					System.err.println(e.getClass().getName() + ":" + e.getMessage());
				}
			}
		});

		rdbtnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rdbtnNo.setSelected(false);
				try {
					int idproject = 0;
					Statement st = null;
					st = DatabaseConnection.connect.createStatement();
					idproject = st.executeQuery("select last_insert_rowid()").getInt(1);

					PreparedStatement stat = null;
					stat = DatabaseConnection.connect
							.prepareStatement("UPDATE parameter SET ordination=1" + " WHERE idproject=" + idproject);
					stat.executeUpdate();
				} catch (Exception e) {
					System.err.println(e.getClass().getName() + ":" + e.getMessage());
				}
			}
		});
		rdbtnYes.setBounds(340, 21, 56, 23);
		panelMauve.add(rdbtnYes);

		rdbtnNo.setBounds(400, 21, 56, 23);
		panelMauve.add(rdbtnNo);

		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String orientation = null;
					String ordination = null;

					String cisaMinLength = getTextField_MinLength().getText();
					String cisaGenomeSize = getTextField_GenomeSize().getText();
					String cisaR2Gap = getTextField_R2Gap().getText();

					String genusName = txtGenus.getText();
					String cpu = spinnerCpu.getValue().toString();

					int idproject = 0;
					Statement st = null;
					st = DatabaseConnection.connect.createStatement();
					idproject = st.executeQuery("select last_insert_rowid()").getInt(1);

					if (!cisaR2Gap.isBlank() && !cisaMinLength.isBlank() && !cisaGenomeSize.isBlank()
							&& !genusName.isBlank() && !cpu.isBlank()
							&& (rdbtnNo.isSelected() || rdbtnYes.isSelected())) {
						PreparedStatement stat = null;
						stat = DatabaseConnection.connect
								.prepareStatement("UPDATE parameter SET  genusName='" + genusName + "', cisaMinLength="
										+ cisaMinLength + "," + "cisaGenomeSize=" + cisaGenomeSize + ", cisaR2Gap="
										+ cisaR2Gap + ", cpu =" + cpu + " WHERE idproject=" + idproject);
						stat.executeUpdate();

						String cmmd;
						ResultSet resulSet;
						cmmd = "SELECT * FROM parameter WHERE idproject=" + idproject + ";";
						Statement statement = DatabaseConnection.connect.createStatement();
						resulSet = statement.executeQuery(cmmd);

						while (resulSet.next()) {
							orientation = resulSet.getString("orientation");
							ordination = resulSet.getString("ordination");
						}
						Input input = new Input();
						if (ordination.equals("0")) {
							input.getPanel().setVisible(false);
						}

						if (orientation == null) {
							input.getTxtNoFileSelected_single().setVisible(true);
							input.getButton_chooseFileSingleRead().setVisible(true);

						}
						if (orientation != null) {
							input.getPanel_pairedReadFileInput().setVisible(true);
							input.getLblRead1().setVisible(true);
							input.getLblRead2().setVisible(true);
							input.getTxtNoFileSelected_paired1().setVisible(true);
							input.getTxtNoFileSelected_paired2().setVisible(true);
							input.getButton_chooseRead1().setVisible(true);
							input.getButton_chooseRead2().setVisible(true);
						}

						input.getFrame().setVisible(true);
						getFrame().dispose();

					} else {
						dialogEmpty.setVisible(true);
					}
				} catch (Exception e) {
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
				}

			}
		});
		btnNext.setBounds(550, 304, 78, 31);
		frmParameters.getContentPane().add(btnNext);

		JButton button = new JButton("Back");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SpadesAndMegahit spadesParametersScreen = new SpadesAndMegahit();
				spadesParametersScreen.getfrm_SpadesAndMegahit().setVisible(true);
				getFrame().dispose();
			}
		});
		button.setBounds(26, 303, 72, 32);
		frmParameters.getContentPane().add(button);

		JLabel lblDoYouWant = new JLabel("Do you want to order the assembly result?");
		lblDoYouWant.setBounds(12, 25, 316, 15);
		panelMauve.add(lblDoYouWant);

	}

	public JFrame getFrame() {
		return frmParameters;
	}

	public JTextField getTextField_GenomeSize() {
		return textField_GenomeSize;
	}

	public JTextField getTextField_MinLength() {
		return textField_MinLength;
	}

	public JTextField getTextField_R2Gap() {
		return textField_R2Gap;
	}
}
