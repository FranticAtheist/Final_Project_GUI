package swing;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import javax.jws.soap.InitParam;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.text.SimpleAttributeSet;

import com.fazecast.jSerialComm.SerialPort;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextArea textPanel;
	private Toolbar toolbar;
	private FormPanel formPanel;
	private PortSettings connectedPort;
	private InputStream comPortInput;
	private OutputStream comPortOutput;
	private MessageListener portListener;
	private JFileChooser fileChooser;
	private String fileName;

	public MainFrame() {

		super("Final Project");try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel. test
		}
		setLayout(new BorderLayout());
		toolbar = new Toolbar();
		textPanel = new TextArea();
		formPanel = new FormPanel();

		fileChooser = new JFileChooser();

		fileChooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					String filename = f.getName().toLowerCase();
					return filename.endsWith(".txt");
				}
			}

			@Override
			public String getDescription() {
				return "Text files (*.txt)";
			}

		});

		setJMenuBar(createMenubar());

		textPanel.setEditable(false);

		formPanel.setCheckBoxEventListener(new CheckBoxListener() {
			public void CheckBoxEventOccurred(CheckBoxEvent e) {
				textPanel.setEditable(e.isEditable());
			}
		});

		formPanel.setFormListener(new FormListener() {
			public void formEventOccurred(FormEvent e) {

				if (e.isCommand()) {
					textPanel.append(e.getText());
				} else if (e.getMode() == 0) {

					Integer typeMode = e.getMode();
					portListener.setText(e.getText());
					portListener.setMode(e.getMode());
					sendMode(typeMode);

				} else if (e.getMode() == 1) {
					portListener.setText(parseTextToCommand(textPanel.getText()));
					portListener.setMode(e.getMode());
					sendMode(e.getMode());
					// String test = parseTextToCommand(textPanel.getText());
				} else if (e.getMode() == 2) {
					Integer typeMode = e.getMode();
					portListener.setMode(e.getMode());
					portListener.setText(fileName + Utility.END_OF_TEXT + textPanel.getText());
					sendMode(typeMode);
				}

			}

		});

		toolbar.setToolbarListener(new ToolbarListener() {
			public void toolbarEventOccurred(ToolbarEvent ev) {
				connectedPort = new PortSettings(ev.getCurrentPort(), ev.getBaudrate(), ev.getStop(), ev.getParity());
				JButton clicked = ev.getClicked();

				if (clicked == toolbar.getConnectButton()) {
					if (connectedPort.getPort().openPort()) {
						connectedPort.setConnected(true);
						toolbar.getDisconnectButton().setVisible(true);
						toolbar.getConnectButton().setVisible(false);
						toolbar.getChangeSettingsButton().setVisible(true);
						connectedPort.getPort().setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT,
								SerialPort.NO_PARITY);
						connectedPort.getPort().setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
						comPortInput = connectedPort.getPort().getInputStream();
						comPortOutput = connectedPort.getPort().getOutputStream();
						try {
							comPortInput.skip(comPortInput.available());
						} catch (IOException e) {
							System.out.println(e);
							e.printStackTrace();
						}
						portListener = new MessageListener(connectedPort.getPort());
						portListener.setTextPanel(textPanel);
						System.out.println("successfully opened the port");
					} else {
						System.out.println("unable to open the port");
					}

				} else if (clicked == toolbar.getDisconnectButton()) {
					if (connectedPort.getPort().closePort()) {
						connectedPort.setConnected(false);
						toolbar.getConnectButton().setVisible(true);
						toolbar.getChangeSettingsButton().setVisible(false);
						toolbar.getDisconnectButton().setVisible(false);
						System.out.println("successfully closed the port");
					} else {
						System.out.println("unable to close the port");
					}

				} else if (clicked == toolbar.getChangeSettingsButton()) {
					StringBuilder changedSetteings = new StringBuilder();
					changedSetteings.append(Utility.uartCommand);
					changedSetteings.append(connectedPort.getBaudrate());
					changedSetteings.append(connectedPort.getStopLength());
					changedSetteings.append(connectedPort.getParityType());
					portListener.setText(changedSetteings.toString());
					portListener.setPortSettings(connectedPort);
					portListener.setChangingPortSettings(true);
					portListener.setMode(Utility.commandMode);
					sendMode(Utility.commandMode);
					
					System.out
							.println("change settings to: " + "baud: " + Utility.intBaudRates[connectedPort.getBaudrate()]
									+ " stop: " + Utility.stopLengths[connectedPort.getStopLength()] + " parity: "
									+ Utility.parityType[connectedPort.getParityType()]);
				}

			}
		});

		add(formPanel, BorderLayout.WEST);
		add(toolbar, BorderLayout.NORTH);
		add(textPanel, BorderLayout.CENTER);

		setSize(800, 600);
		setMinimumSize(new DimensionUIResource(800, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}


	private void sendMode(Integer typeMode) {
		byte[] toSend = { (byte) (typeMode.byteValue() + '0') };
		portListener.setDoneSending(false);
		portListener.setToSendBytes(toSend);
		portListener.setIndex(0);
		try {
			connectedPort.getPort().getOutputStream().write(toSend[0]);
			connectedPort.getPort().getOutputStream().flush();
			System.out.println("was sent: " + toSend[0]);
		} catch (IOException e1) {
			System.out.println(e1);
			e1.printStackTrace();
		}
	}

	private JMenuBar createMenubar() {

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");

		JMenuItem saveFileItem = new JMenuItem("Save File...");
		JMenuItem openFileItem = new JMenuItem("Open File...");
		JMenuItem exitItem = new JMenu("Exit");
		saveFileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					try {
						FileWriter writer = new FileWriter(fileChooser.getSelectedFile());
						writer.write(textPanel.getText());
						writer.close();
					} catch (IOException e1) {
						System.out.println(e1);
						e1.printStackTrace();
					}

				}
			}
		});

		openFileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
					FileReader reader = null;
					File selectedFile = fileChooser.getSelectedFile();
					fileName = selectedFile.getName();
					String filepath = selectedFile.getAbsolutePath();
					try {
						reader = new FileReader(filepath);
						System.out.println(filepath);
					} catch (FileNotFoundException e1) {
						System.out.println(e1);
						e1.printStackTrace();
					} catch (IOException e1) {
						System.out.println(e1);
						e1.printStackTrace();
					}
					BufferedReader br = new BufferedReader(reader);
					StringBuilder sb = new StringBuilder();
					String line;
					textPanel.setText("");
					try {
						while ((line = br.readLine()) != null) {
							sb.append(line).append("\n");
						}
					} catch (IOException e1) {
						System.out.println(e1);
						e1.printStackTrace();
					}
					textPanel.setText("");
					textPanel.append(sb.toString());

				}

			}

		});

		fileMenu.add(saveFileItem);
		fileMenu.add(openFileItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		menuBar.add(fileMenu);

		return menuBar;

	}

	String parseTextToCommand(String text) {
		StringBuffer command = new StringBuffer();
		for (String line : text.split("\\r?\\n")) {
			if (Arrays.asList(Utility.LedColors).contains(line)) {
				command.append("0");
				command.append(Arrays.asList(Utility.LedColors).indexOf(line));
			} else if (line.contains("delay")) {
				for (String word : line.split(" ")) {
					if (!word.contains("delay")) {
						try {
							Integer.parseInt("eueou");
						} catch (Exception e1) {

						}

						if (Integer.parseInt(word) != 0) {
							command.append("1");
							command.append(word);
							command.append(Utility.END_OF_TEXT);
						}
					}
				}
			}
		}
		return command.toString();

	}

}
