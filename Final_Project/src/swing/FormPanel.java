package swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.InsetsUIResource;

public class FormPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel nameLabel;
	private JLabel commandLabel;
	private JTextField nameField;
	private JTextField commandField;
	private JButton sendBtn;
	private FormListener formListener;
	private CheckBoxListener checkBoxListener;
	private JList<String> modeList;
	private JList<String> commandList;
	private JCheckBox editCommandCheck;
	private JTextArea chatArea;
	private JLabel chatLabel;
	private JLabel enableEditCommand;
	private JLabel colorLabel;
	private JComboBox<String> colorCombo;
	private JButton createComButton;
	private JFileChooser sendFileChooser;
	private JComboBox<String> delayCombo;
	private JLabel delayLabel;

	private JRadioButton evenRadio;
	private JRadioButton oddRadio;
	private JRadioButton noneRadio;
	private ButtonGroup parityGroup;
	private StringBuffer buffer;
	private JLabel limitLabel;

	public FormPanel() {
		Dimension dim = getPreferredSize();
		dim.width = 500;
		setPreferredSize(dim);

		nameLabel = new JLabel("Name: ");
		commandLabel = new JLabel("command: ");
		nameField = new JTextField(10);
		commandField = new JTextField(10);
		commandField.setPreferredSize(new Dimension(20, 20));
		modeList = new JList<String>();
		commandList = new JList<String>();
		editCommandCheck = new JCheckBox();
		createComButton = new JButton("Add Command");
		delayCombo = new JComboBox<String>();
		delayCombo.setEditable(true);
		delayCombo.setVisible(false);
		delayLabel = new JLabel("Delay: ");
		delayLabel.setVisible(false);
		buffer = new StringBuffer();
		limitLabel = new JLabel("invalid delay");
		limitLabel.setVisible(false);
		
		sendFileChooser = new JFileChooser();
		sendFileChooser.setVisible(false);
		sendFileChooser.setFileFilter(new FileFilter() {
		
		

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
		

		chatArea = new JTextArea();
		chatArea.setWrapStyleWord(true);
		chatArea.setLineWrap(true);
		chatArea.setPreferredSize(new Dimension(120, 150));
		chatLabel = new JLabel("chat: ");
		enableEditCommand = new JLabel("edit command: ");
		colorLabel = new JLabel("Color: ");
		colorCombo = new JComboBox<>();

		evenRadio = new JRadioButton("even");
		oddRadio = new JRadioButton("odd");
		noneRadio = new JRadioButton("none");
		parityGroup = new ButtonGroup();
		evenRadio.setActionCommand("even");
		oddRadio.setActionCommand("odd");
		noneRadio.setActionCommand("none");

		editCommandCheck.setVisible(false);
		enableEditCommand.setVisible(false);
		colorCombo.setVisible(false);
		colorLabel.setVisible(false);
		commandLabel.setVisible(false);
		commandList.setVisible(false);
		createComButton.setVisible(false);

		// set up parity
		parityGroup.add(evenRadio);
		parityGroup.add(oddRadio);
		parityGroup.add(noneRadio);

		editCommandCheck.addActionListener(new ActionListener() {// need to edit
																	// this

			public void actionPerformed(ActionEvent e) {
				boolean isTicked = editCommandCheck.isSelected();
				CheckBoxEvent ev = new CheckBoxEvent(this, isTicked);
				if(checkBoxListener != null) {
					checkBoxListener.CheckBoxEventOccurred(ev);
				}

			}
		});
		// set up type box
		DefaultListModel<String> modeModel = new DefaultListModel<String>();
		for (String mode : Utility.modes) {
			modeModel.addElement(mode);
		}

		modeList.setModel(modeModel);

		// set up color combo
		DefaultComboBoxModel<String> colorModel = new DefaultComboBoxModel<>();
		for (String color : Utility.LedColors) {
			colorModel.addElement(color);
		}

		colorCombo.setModel(colorModel);

		modeList.setPreferredSize(new Dimension(120, 80));
		modeList.setBorder(BorderFactory.createEtchedBorder());
		modeList.setSelectedIndex(0);

		// set up command listener
		createComButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FormEvent ev = null;
				if (commandList.getSelectedIndex() == 0) {
					ev = new FormEvent(this, 0, (String) colorCombo.getSelectedItem() + "\n", true);
				} else if (commandList.getSelectedIndex() == 1 & delayCombo.getSelectedItem() != null && Integer.parseInt((String) delayCombo.getSelectedItem()) > 0 & Integer.parseInt((String) delayCombo.getSelectedItem())<=9999 ) {
					ev = new FormEvent(this, 0, (String) ("delay " + delayCombo.getSelectedItem()+ "\n"), true);
					limitLabel.setVisible(false);
				}else if (commandList.getSelectedIndex() == 1 & (delayCombo.getSelectedItem() == null || Integer.parseInt((String) delayCombo.getSelectedItem()) < 0 | Integer.parseInt((String) delayCombo.getSelectedItem())>=9999)) {
					limitLabel.setVisible(true);
				}
				if (formListener != null & ev != null) {
					formListener.formEventOccurred(ev);

				}
			}
		});


		modeList.getSelectionModel().addListSelectionListener(new ListSelectionListener() { // changes
																							// form
																							// panel
																							// layout

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int currentMode = modeList.getSelectedIndex();
				switch (currentMode) {
					case 0 :
						commandLabel.setVisible(false);
						commandList.setVisible(false);
						chatArea.setVisible(true);
						chatLabel.setVisible(true);
						editCommandCheck.setVisible(false);
						enableEditCommand.setVisible(false);
						colorCombo.setVisible(false);
						colorLabel.setVisible(false);
						createComButton.setVisible(false);
						sendFileChooser.setVisible(false);
						delayCombo.setVisible(false);
						delayLabel.setVisible(false);

						break;
					case 1 :
						commandLabel.setVisible(true);
						commandList.setVisible(true);
						chatArea.setVisible(false);
						chatLabel.setVisible(false);
						editCommandCheck.setVisible(true);
						enableEditCommand.setVisible(true);
						colorCombo.setVisible(true);
						colorLabel.setVisible(true);
						createComButton.setVisible(true);
						sendFileChooser.setVisible(false);
						delayCombo.setVisible(true);
						delayLabel.setVisible(true);

						break;
					case 2 :
						commandLabel.setVisible(false);
						commandList.setVisible(false);
						chatArea.setVisible(false);
						chatLabel.setVisible(false);
						editCommandCheck.setVisible(false);
						enableEditCommand.setVisible(false);
						colorCombo.setVisible(false);
						colorLabel.setVisible(false);
						createComButton.setVisible(false);
						sendFileChooser.setVisible(false);
						delayCombo.setVisible(false);
						delayLabel.setVisible(false);
				}

			}
		});

		// set up combo box
		DefaultComboBoxModel<String> empModel = new DefaultComboBoxModel<String>();
		empModel.addElement("led");
		empModel.addElement("delay");
		commandList.setModel(empModel);
		commandList.setSelectedIndex(0);
		commandList.setPreferredSize(new Dimension(100, 66));
		commandList.setBorder(BorderFactory.createEmptyBorder());

		sendBtn = new JButton("Send");
		sendBtn.setPreferredSize(new Dimension(80, 20));

		sendBtn.addActionListener(new ActionListener() { // creates event when
															// "ok" is clicked
			public void actionPerformed(ActionEvent e) {

				int modeCat = modeList.getSelectedIndex();
				String chatText = chatArea.getText();
				FormEvent ev = null;
				switch (modeCat) {
					case 0 : // chat
						ev = new FormEvent(this, modeCat, chatText, false);
						break;
					case 1 : // command
						ev = new FormEvent(this, modeCat, chatText, false);
						break;
					case 2 : // file
						ev = new FormEvent(this, modeCat, chatText, false);
						break;
					case 3 : // file name
						ev = new FormEvent(this, modeCat, chatText, false);
						break;
				}

				if (formListener != null & ev != null) {
					formListener.formEventOccurred(ev);
				}
			}
		});

		Border innerBorder = BorderFactory.createTitledBorder("controls");
		Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		layoutComponents();

	}

	public void layoutComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		////////////////////// first row ////////////////////////

		gc.weighty = 0.5;
		gc.weightx = 1;

		gc.gridy++;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new InsetsUIResource(25, 0, 0, 0);
		add(new JLabel("Mode: "), gc);

		gc.gridx = 1;

		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new InsetsUIResource(0, 0, 0, 0);
		add(modeList, gc);

		////////////////////// next row ////////////////////////
		gc.weighty = 0.2;
		gc.weightx = 1;

		gc.gridy++;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new InsetsUIResource(10, 0, 0, 5);
		add(commandLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new InsetsUIResource(0, 0, 0, 0);
		add(commandList, gc);

		////////////////////// next row ////////////////////////
		gc.weighty = 0.2;
		gc.weightx = 1;

		gc.gridy++;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new InsetsUIResource(10, 0, 0, 5);
		add(colorLabel, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new InsetsUIResource(0, 0, 0, 0);
		add(colorCombo, gc);

		gc.gridx = 2;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new InsetsUIResource(10, 0, 0, 5);
		add(delayLabel, gc);

		gc.gridx = 3;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new InsetsUIResource(0, 0, 0, 0);
		add(delayCombo, gc);


		////////////////////// next row ////////////////////////
		gc.weighty = 0.2;
		gc.weightx = 1;

		gc.gridy++;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new InsetsUIResource(10, 0, 0, 5);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new InsetsUIResource(0, 0, 0, 0);
		add(createComButton, gc);
		
		gc.gridx = 2;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new InsetsUIResource(0, 0, 0, 0);
		add(limitLabel, gc);

		////////////////////// next row ////////////////////////
		gc.weighty = 0.2;
		gc.weightx = 1;

		gc.gridy++;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new InsetsUIResource(10, 0, 0, 5);
		add(enableEditCommand, gc);

		gc.gridx = 1;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.insets = new InsetsUIResource(0, 0, 0, 0);
		add(editCommandCheck, gc);

		////////////////////// next row ////////////////////////
		gc.weighty = 0.2;
		gc.weightx = 1;

		gc.gridy++;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new InsetsUIResource(10, 0, 0, 5);
		add(chatLabel, gc);

		////////////////////// next row ////////////////////////
		gc.weighty = 0.2;

		gc.gridy++;

		gc.gridx = 0;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridwidth = 3;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.insets = new InsetsUIResource(0, 0, 0, 0);
		add(chatArea, gc);

		////////////////////// next row ////////////////////////
		gc.weighty = 2;
		gc.weightx = 0.0;
		gc.ipady = 5;
		gc.gridx = 0;
		gc.gridy++;
		gc.gridwidth = 3;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.insets = new InsetsUIResource(0, 0, 0, 0);
		add(sendFileChooser, gc);
		////////////////////// next row ////////////////////////
		gc.weighty = 0.5;
		gc.weightx = 0.0;
		gc.ipady = 5;
		gc.gridx = 0;
		gc.gridy++;
		gc.gridwidth = 3;
		gc.anchor = GridBagConstraints.CENTER;
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.insets = new InsetsUIResource(0, 0, 0, 0);
		add(sendBtn, gc);
	}

	public void setFormListener(FormListener listener) {
		this.formListener = listener;
	}
	public void setCheckBoxEventListener(CheckBoxListener listener) {
		this.checkBoxListener = listener;
	}
	
}
