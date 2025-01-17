package main.java.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.PatternSyntaxException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.java.controller.DatePicker;
import main.java.controller.ProjectController;
import main.java.model.ProjectModel;
import main.java.model.SessionModel;
import main.java.model.StaticActions;
import main.java.view.SessionView.DateCellRenderer;

@SuppressWarnings("deprecation")
public class ProjectView implements IView {

	private static final long serialVersionUID = 1L;
	private JPanel projectPanel;
	private JPanel contentPane; // Container
	JTabbedPane tabbedPane;
	private JTable table;
	private TableRowSorter<TableModel> sorter;
	private JComboBox<String> comboBoxProject = new JComboBox<String>();
	private JComboBox<String> comboBoxClient = new JComboBox<String>();
	private JComboBox<String> comboBoxClientNewP;
	private JTextField textFieldProjectName;
	private JTextField textFieldStartDate;
	private JTextField textFieldEndDate;
	private JTextField textFieldFrom;
	private JTextField textFieldTo;
	private JTextField textFieldClientName;
	private JTextField textFieldContact;
	private JTextField textFieldTelephone;
	private JTextField textFieldMobile;
	private JTextField textFieldStreet;
	private JTextField textFieldHouseNumber;
	private JTextField textFieldZip;
	private JTextField textFieldCity;
	private JTextField textFieldCountry;
	private JTextField textFieldNewService;
	private JTextField textFieldInternalRate;
	private JTextField textFieldExternalRate;
	private JLabel lblErrorClient;
	private JLabel lblErrorService;
	private JLabel lblErrorProject;
	private ProjectController projectController;
	private DefaultTableCellRenderer dateRenderer;

	public class DateCellRenderer extends DefaultTableCellRenderer{

		private static final long serialVersionUID = 1L;

		public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (value instanceof java.util.Date) {
				// Use SimpleDateFormat class to get a formatted String from Date object.
				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy"); 
				String strDate = dateFormat.format(value);
				this.setText(strDate);
			}
			return this;
		}
	}
	
	public ProjectView(ProjectController projectController) {
		this.projectController = projectController;
		
		projectPanel = new JPanel();
		projectPanel.setName("projectPanelMainPane");
		projectPanel.setBounds(0, 0, 1490, 960);
		projectPanel.setBackground(new Color(47, 48, 52));
		projectPanel.setLayout(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // top, left, bottom, right
		contentPane.setBounds(10, 87, 1470, 862);
		contentPane.setBackground(new Color(31, 32, 33));
		projectPanel.add(contentPane);

		JLabel lblNewLabel = new JLabel("Projekte");
		lblNewLabel.setBounds(10, 60, 134, 24);
		lblNewLabel.setForeground(Color.WHITE);
		projectPanel.add(lblNewLabel);
		contentPane.setLayout(null);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setName("projectViewTabbedPane");
		tabbedPane.setBounds(15, 15, 910, 830);
		contentPane.add(tabbedPane);

		JPanel panel_project_overview = new JPanel();
		panel_project_overview.setName("panel_project_overview");
		tabbedPane.addTab("Projektübersicht", null, panel_project_overview, null);
		SpringLayout sl_panel_project_overview = new SpringLayout();
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, comboBoxProject, 58, SpringLayout.NORTH, panel_project_overview);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, comboBoxProject, 245, SpringLayout.WEST, panel_project_overview);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, comboBoxProject, -434, SpringLayout.EAST, panel_project_overview);
		panel_project_overview.setLayout(sl_panel_project_overview);

		////////////////////////////////////
		/// First Tab / project overview ///
		////////////////////////////////////
		
		// Titel Label
		JLabel lblHeadTitel = new JLabel("Projektübersicht");
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, lblHeadTitel, 10, SpringLayout.NORTH, panel_project_overview);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, lblHeadTitel, 10, SpringLayout.WEST, panel_project_overview);
		lblHeadTitel.setName("lblHeadTitel");
		panel_project_overview.add(lblHeadTitel);
		lblHeadTitel.setFont(new Font("Tahoma", Font.BOLD, 18));

		// Tabel Frame
		JScrollPane scrollPaneTable = new JScrollPane();
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, scrollPaneTable, 164, SpringLayout.NORTH, panel_project_overview);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, scrollPaneTable, 10, SpringLayout.WEST, panel_project_overview);
		sl_panel_project_overview.putConstraint(SpringLayout.SOUTH, scrollPaneTable, -21, SpringLayout.SOUTH, panel_project_overview);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, scrollPaneTable, 895, SpringLayout.WEST,
				panel_project_overview);
		scrollPaneTable.setName("scrollPaneTable");
		panel_project_overview.add(scrollPaneTable);
		
		// create table
		table = new JTable();
		updateTable(projectController);
		scrollPaneTable.setViewportView(table);

		dateRenderer = new DateCellRenderer();
		dateRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		table.getColumnModel().getColumn(2).setPreferredWidth(120);
		table.getColumnModel().getColumn(2).setCellRenderer(dateRenderer);
		table.getColumnModel().getColumn(3).setPreferredWidth(120);
		table.getColumnModel().getColumn(3).setCellRenderer(dateRenderer);
		
		
		// Projects Label
		JLabel lblProjects = new JLabel("Projekt Name:");
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, lblProjects, 3, SpringLayout.NORTH, comboBoxProject);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, lblProjects, -50, SpringLayout.WEST, comboBoxProject);
		lblProjects.setName("lblProjects");
		panel_project_overview.add(lblProjects);
		panel_project_overview.add(comboBoxProject);

		// Projects DropDown
		comboBoxProject.setName("comboBoxProject");
		comboBoxProject.setPreferredSize(new Dimension(300, 20));
		comboBoxProject.setAlignmentX(0.0f);
		comboBoxProject.addActionListener(projectController);
		comboBoxProject.setActionCommand(StaticActions.ACTION_SET_PROJECT);
		lblProjects.setLabelFor(comboBoxProject);

		// refresh button projects
		JButton btnLoadProjects = new JButton("\u21BB");
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, btnLoadProjects, -5, SpringLayout.NORTH, lblProjects);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, btnLoadProjects, 14, SpringLayout.EAST, comboBoxProject);
		sl_panel_project_overview.putConstraint(SpringLayout.SOUTH, btnLoadProjects, 4, SpringLayout.SOUTH, lblProjects);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, btnLoadProjects, -390, SpringLayout.EAST, panel_project_overview);
		btnLoadProjects.setName("btnLoadProjects");
		btnLoadProjects.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 14));
		panel_project_overview.add(btnLoadProjects);
		btnLoadProjects.addActionListener(projectController);
		btnLoadProjects.setActionCommand(StaticActions.ACTION_LOAD_PROJECTS);

		// Client Label
		JLabel lblClient = new JLabel("Kunde:");
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, lblClient, 0, SpringLayout.WEST, lblProjects);
		lblClient.setName("lblService");
		panel_project_overview.add(lblClient);

		// Client DropDown
		comboBoxClient = new JComboBox<String>();
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, lblClient, 4, SpringLayout.NORTH, comboBoxClient);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, lblClient, -50, SpringLayout.WEST, comboBoxClient);
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, comboBoxClient, 10, SpringLayout.SOUTH, comboBoxProject);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, comboBoxClient, 0, SpringLayout.EAST,
				comboBoxProject);
		comboBoxClient.setName("comboBoxService");
		comboBoxClient.setActionCommand(StaticActions.ACTION_PROJECT_SET_CLIENT);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, comboBoxClient, 0, SpringLayout.WEST,
				comboBoxProject);
		panel_project_overview.add(comboBoxClient);

		// Time Frame Label
		JLabel lblTimeFrame = new JLabel("Zeitraum:");
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, lblTimeFrame, 16, SpringLayout.SOUTH, lblClient);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, lblTimeFrame, 0, SpringLayout.WEST, lblProjects);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, lblTimeFrame, 0, SpringLayout.EAST, lblProjects);
		lblTimeFrame.setName("lblTimeFrame");
		panel_project_overview.add(lblTimeFrame);

		// search button
		JButton btnSearchButton = new JButton("Suchen...");
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, btnSearchButton, -11, SpringLayout.NORTH, lblTimeFrame);
		sl_panel_project_overview.putConstraint(SpringLayout.SOUTH, btnSearchButton, -17, SpringLayout.NORTH, scrollPaneTable);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, btnSearchButton, 0, SpringLayout.EAST, scrollPaneTable);
		btnSearchButton.setName("btnSearchButton");
		panel_project_overview.add(btnSearchButton);
		btnSearchButton.addActionListener(projectController);
		btnSearchButton.setActionCommand(StaticActions.ACTION_SEARCH_PROJECTS);

		// input start date
		textFieldFrom = new JTextField();
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, textFieldFrom, -3, SpringLayout.NORTH, lblTimeFrame);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, textFieldFrom, 0, SpringLayout.WEST, comboBoxProject);
		textFieldFrom.setName("textFieldFrom");
		panel_project_overview.add(textFieldFrom);
		textFieldFrom.setColumns(10);

		// input start date label
		JLabel lblFrom = new JLabel("von:");
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, lblFrom, -666, SpringLayout.EAST, panel_project_overview);
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, lblFrom, 0, SpringLayout.NORTH, lblTimeFrame);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, lblFrom, 6, SpringLayout.EAST, lblTimeFrame);
		lblFrom.setName("lblFrom");
		panel_project_overview.add(lblFrom);

		// input end date label
		JLabel lblTo = new JLabel("bis:");
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, lblTo, 0, SpringLayout.NORTH, lblTimeFrame);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, lblTo, -513, SpringLayout.EAST, panel_project_overview);
		lblTo.setName("lblTo");
		panel_project_overview.add(lblTo);

		// input end date
		textFieldTo = new JTextField();
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, textFieldTo, -3, SpringLayout.NORTH, lblTimeFrame);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, textFieldTo, 6, SpringLayout.EAST, lblTo);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, textFieldTo, 0, SpringLayout.EAST, comboBoxProject);
		textFieldTo.setName("textFieldTo");
		panel_project_overview.add(textFieldTo);
		textFieldTo.setColumns(10);
		// DatePicker Start Date (search)
		JButton btnSetStartDate_1 = new JButton("...");
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, btnSetStartDate_1, -549, SpringLayout.EAST, panel_project_overview);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, lblTo, 6, SpringLayout.EAST, btnSetStartDate_1);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, textFieldFrom, -6, SpringLayout.WEST, btnSetStartDate_1);
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, btnSetStartDate_1, -4, SpringLayout.NORTH, lblTimeFrame);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, btnSetStartDate_1, 330, SpringLayout.WEST, panel_project_overview);
		btnSetStartDate_1.setName("btnSetStartDate_1");
		panel_project_overview.add(btnSetStartDate_1);

		JButton btnSetStartDate_1_1 = new JButton("...");
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, btnSetStartDate_1_1, -4, SpringLayout.NORTH, lblTimeFrame);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, btnSetStartDate_1_1, 18, SpringLayout.EAST, textFieldTo);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, btnSetStartDate_1_1, 0, SpringLayout.EAST, btnLoadProjects);
		btnSetStartDate_1_1.setName("btnSetStartDate_1_1");
		panel_project_overview.add(btnSetStartDate_1_1);
		
		// Date Popup
		final JFrame popupFrame = new JFrame();
		popupFrame.setName("popupFrame");
		btnSetStartDate_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				textFieldFrom.setText(new DatePicker(popupFrame).setPickedDate());
				System.out.print(textFieldFrom.getText());
			}
		});
		btnSetStartDate_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				textFieldTo.setText(new DatePicker(popupFrame).setPickedDate());
			}
		});

		////////////////////////////////////
		///// Second Tab / new project /////
		////////////////////////////////////

		JPanel panel_new_project = new JPanel();
		panel_new_project.setName("panel_new_project");
		tabbedPane.setName("tabbedPane_panel_new_project"); // added
		tabbedPane.addTab("Neues Projekt", null, panel_new_project, null);
		SpringLayout sl_panel_new_project = new SpringLayout();
		panel_new_project.setLayout(sl_panel_new_project);

		JLabel lblNewProjectHead = new JLabel("Neues Projekt:");
		lblNewProjectHead.setName("lblNewProjectHead");
		sl_panel_new_project.putConstraint(SpringLayout.NORTH, lblNewProjectHead, 10, SpringLayout.NORTH,
				panel_new_project);
		sl_panel_new_project.putConstraint(SpringLayout.WEST, lblNewProjectHead, 10, SpringLayout.WEST,
				panel_new_project);
		lblNewProjectHead.setFont(new Font("Tahoma", Font.BOLD, 18));
		panel_new_project.add(lblNewProjectHead);

		JPanel panel_input_form = new JPanel();
		panel_input_form.setName("panel_input_form");
		sl_panel_new_project.putConstraint(SpringLayout.NORTH, panel_input_form, 80, SpringLayout.NORTH,
				panel_new_project);
		sl_panel_new_project.putConstraint(SpringLayout.WEST, panel_input_form, 150, SpringLayout.WEST,
				panel_new_project);
		sl_panel_new_project.putConstraint(SpringLayout.SOUTH, panel_input_form, -333, SpringLayout.SOUTH,
				panel_new_project);
		sl_panel_new_project.putConstraint(SpringLayout.EAST, panel_input_form, -425, SpringLayout.EAST,
				panel_new_project);
		panel_new_project.add(panel_input_form);
		SpringLayout sl_panel_input_form = new SpringLayout();
		panel_input_form.setLayout(sl_panel_input_form);

		JLabel lblNewLabel_18 = new JLabel("Projekt Name:");
		lblNewLabel_18.setName("lblNewLabel_18");
		sl_panel_input_form.putConstraint(SpringLayout.EAST, lblNewLabel_18, 120, SpringLayout.WEST, panel_input_form);
		panel_input_form.add(lblNewLabel_18);

		textFieldProjectName = new JTextField();
		textFieldProjectName.setName("textFieldProjectName");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, lblNewLabel_18, 3, SpringLayout.NORTH,
				textFieldProjectName);
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, textFieldProjectName, 10, SpringLayout.NORTH,
				panel_input_form);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, textFieldProjectName, 144, SpringLayout.WEST,
				panel_input_form);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, textFieldProjectName, -10, SpringLayout.EAST,
				panel_input_form);
		panel_input_form.add(textFieldProjectName);
		textFieldProjectName.setColumns(10);

		JLabel lblNewLabel_19 = new JLabel("Kunde:");
		lblNewLabel_19.setName("lblNewLabel_19");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, lblNewLabel_19, 20, SpringLayout.SOUTH, lblNewLabel_18);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, lblNewLabel_18, 0, SpringLayout.WEST, lblNewLabel_19);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, lblNewLabel_19, 20, SpringLayout.WEST, panel_input_form);
		panel_input_form.add(lblNewLabel_19);

		JLabel lblNewLabel_20 = new JLabel("Start Datum:");
		lblNewLabel_20.setName("lblNewLabel_20");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, lblNewLabel_20, 21, SpringLayout.SOUTH, lblNewLabel_19);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, lblNewLabel_20, 20, SpringLayout.WEST, panel_input_form);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, lblNewLabel_20, -210, SpringLayout.EAST, panel_input_form);
		panel_input_form.add(lblNewLabel_20);

		JLabel lblNewLabel_21 = new JLabel("End Datum:");
		lblNewLabel_21.setName("lblNewLabel_21");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, lblNewLabel_21, 21, SpringLayout.SOUTH, lblNewLabel_20);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, lblNewLabel_21, 20, SpringLayout.WEST, panel_input_form);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, lblNewLabel_21, -210, SpringLayout.EAST, panel_input_form);
		panel_input_form.add(lblNewLabel_21);

		// Error Label
		lblErrorProject = new JLabel();
		lblErrorProject.setName("lblErrorProject");
		lblErrorProject.setForeground(new Color(255, 140, 0));
		lblErrorProject.setVisible(false);
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, lblErrorProject, 21, SpringLayout.SOUTH, lblNewLabel_21);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, lblErrorProject, 150, SpringLayout.WEST, panel_input_form);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, lblErrorProject, 100, SpringLayout.EAST, panel_input_form);
		panel_input_form.add(lblErrorProject);
		
		comboBoxClientNewP = new JComboBox<String>();
		comboBoxClientNewP.setName("textFieldClient");
		comboBoxClientNewP.setActionCommand(StaticActions.ACTION_SET_CLIENT_NEWP);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, lblNewLabel_19, -24, SpringLayout.WEST, comboBoxClientNewP);
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, comboBoxClientNewP, 14, SpringLayout.SOUTH,
				textFieldProjectName);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, comboBoxClientNewP, 144, SpringLayout.WEST, panel_input_form);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, comboBoxClientNewP, 0, SpringLayout.EAST,
				textFieldProjectName);
		panel_input_form.add(comboBoxClientNewP);

		// start date input field
		textFieldStartDate = new JTextField(20);
		textFieldStartDate.setName("textFieldStartDate");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, textFieldStartDate, -3, SpringLayout.NORTH,
				lblNewLabel_20);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, textFieldStartDate, 0, SpringLayout.WEST,
				textFieldProjectName);
		panel_input_form.add(textFieldStartDate);
		// start date DatePicker Button 
		JButton btnSetStartDate = new JButton("...");
		btnSetStartDate.setName("btnSetStartDate");
		sl_panel_input_form.putConstraint(SpringLayout.EAST, textFieldStartDate, -6, SpringLayout.WEST,
				btnSetStartDate);
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, btnSetStartDate, -4, SpringLayout.NORTH, lblNewLabel_20);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, btnSetStartDate, -33, SpringLayout.EAST,
				textFieldProjectName);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, btnSetStartDate, 0, SpringLayout.EAST,
				textFieldProjectName);
		panel_input_form.add(btnSetStartDate);
		// end date input field
		textFieldEndDate = new JTextField(20);
		textFieldEndDate.setName("textFieldEndDate");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, textFieldEndDate, -3, SpringLayout.NORTH, lblNewLabel_21);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, textFieldEndDate, 0, SpringLayout.WEST, textFieldProjectName);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, textFieldEndDate, 0, SpringLayout.EAST, textFieldStartDate);
		panel_input_form.add(textFieldEndDate);
		// end date DatePicker Button 
		JButton btnSetEndDate = new JButton("...");
		lblNewLabel_18.setName("lblNewLabel_18");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, btnSetEndDate, -4, SpringLayout.NORTH, lblNewLabel_21);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, btnSetEndDate, 0, SpringLayout.WEST, btnSetStartDate);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, btnSetEndDate, -10, SpringLayout.EAST, panel_input_form);

		panel_input_form.add(btnSetEndDate);

		// Save Button
		JButton btnSaveProject = new JButton("Speichern");
		btnSaveProject.setName("btnSaveProject");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, btnSaveProject, 43, SpringLayout.SOUTH, textFieldEndDate);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, btnSaveProject, 0, SpringLayout.WEST,
				textFieldProjectName);
		btnSaveProject.addActionListener(projectController);
		btnSaveProject.setActionCommand(StaticActions.ACTION_SAVE_PROJECT);
		panel_input_form.add(btnSaveProject);


		// Reset project table
		JButton btnResetProjects = new JButton("\u21BB");
		sl_panel_project_overview.putConstraint(SpringLayout.NORTH, btnResetProjects, 0, SpringLayout.NORTH, btnSearchButton);
		sl_panel_project_overview.putConstraint(SpringLayout.WEST, btnResetProjects, 257, SpringLayout.EAST, btnSetStartDate_1_1);
		sl_panel_project_overview.putConstraint(SpringLayout.SOUTH, btnResetProjects, -17, SpringLayout.NORTH, scrollPaneTable);
		sl_panel_project_overview.putConstraint(SpringLayout.EAST, btnResetProjects, -6, SpringLayout.WEST, btnSearchButton);
		btnResetProjects.setName("btnResetProjects");
		btnResetProjects.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 14));
		btnResetProjects.addActionListener(projectController);
		btnResetProjects.setActionCommand(StaticActions.ACTION_RESET_PROJECTS);
		panel_project_overview.add(btnResetProjects);

		btnSetStartDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				// popupFrame.setLocation(getMousePosition());
				textFieldStartDate.setText(new DatePicker(popupFrame).setPickedDate());
				System.out.print(textFieldStartDate.getText());
			}
		});
		comboBoxProject.addActionListener(projectController);
		comboBoxProject.setActionCommand(StaticActions.ACTION_SET_PROJECT);

		
		btnSetEndDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				textFieldEndDate.setText(new DatePicker(popupFrame).setPickedDate());
			}
		});
		////////////////////////////////////
		///// third tab / new client ///////
		////////////////////////////////////

		createTab3();

		////////////////////////////////////
		///// fourth tab / service /////////
		////////////////////////////////////

		createTab4();

	}

	// Getter Setter
	public JPanel getProjectPanel() {
		return projectPanel;
	}

	public JComboBox getComboBox() {
		return comboBoxProject;
	}

	public void setComboBox(JComboBox comboBox) {
		this.comboBoxProject = comboBox;
	}

	public JTable getProjectTable() {
		JTable table = this.table;
		return table;
	}

	public void setProjectTable(JTable table) {
		this.table = table;
	}

	public String getNewProjectName() {
		return textFieldProjectName.getText();
	}

	// Added Getters and Setters (Kevin)
	
	public boolean getLblErrorVisible() {
		return lblErrorClient.isVisible();
	}

	public JLabel getLblErrorProject() {
		return lblErrorProject;
	}

	public JLabel getLblErrorClient() {
		return lblErrorClient;
	}

	public void setLblErrorClient(JLabel lblErrorClient) {
		this.lblErrorClient = lblErrorClient;
	}

	public JLabel getLblErrorService() {
		return lblErrorService;
	}

	public void setLblErrorVisible(boolean b) {
		lblErrorClient.setVisible(b);
	}

	public String getTextFieldClientName() {
		return textFieldClientName.getText();
	}

	public String getTextFieldContact() {
		return textFieldContact.getText();
	}

	public String getTextFieldTelephone() {
		return textFieldTelephone.getText();
	}

	public String getTextFieldMobile() {
		return textFieldMobile.getText();
	}

	public String getTextFieldStreet() {
		return textFieldStreet.getText();
	}

	public String getTextFieldHouseNumber() {
		return textFieldHouseNumber.getText();
	}

	public String getTextFieldZip() {
		return textFieldZip.getText();
	}

	public String getTextFieldCity() {
		return textFieldCity.getText();
	}

	public String getTextFieldCountry() {
		return textFieldCountry.getText();
	}
	
	public JTextField getFieldClientName() {
		return textFieldClientName;
	}

	public JTextField getFieldContact() {
		return textFieldContact;
	}

	public JTextField getFieldTelephone() {
		return textFieldTelephone;
	}

	public JTextField getFieldMobile() {
		return textFieldMobile;
	}

	public JTextField getFieldStreet() {
		return textFieldStreet;
	}

	public JTextField getFieldHouseNumber() {
		return textFieldHouseNumber;
	}

	public JTextField getFieldZip() {
		return textFieldZip;
	}

	public JTextField getFieldCity() {
		return textFieldCity;
	}

	public JTextField getFieldCountry() {
		return textFieldCountry;
	}

	public String getTextFieldNewService() {
		return textFieldNewService.getText();
	}

	public String getTextFieldInternalRate() {
		return textFieldInternalRate.getText();
	}

	public String getTextFieldExternalRate() {
		return textFieldExternalRate.getText();
	}

	public void setTextFieldEndDate(JTextField textFieldEndDate) {
		this.textFieldEndDate = textFieldEndDate;
	}

	public void setTextFieldClientName(JTextField textFieldClientName) {
		this.textFieldClientName = textFieldClientName;
	}

	public void setTextFieldContact(JTextField textFieldContact) {
		this.textFieldContact = textFieldContact;
	}

	public void setTextFieldTelephone(JTextField textFieldTelephone) {
		this.textFieldTelephone = textFieldTelephone;
	}

	public void setTextFieldMobile(JTextField textFieldMobile) {
		this.textFieldMobile = textFieldMobile;
	}

	public void setTextFieldStreet(JTextField textFieldStreet) {
		this.textFieldStreet = textFieldStreet;
	}

	public void setTextFieldHouseNumber(JTextField textFieldHouseNumber) {
		this.textFieldHouseNumber = textFieldHouseNumber;
	}

	public void setTextFieldZip(JTextField textFieldZip) {
		this.textFieldZip = textFieldZip;
	}

	public void setTextFieldCity(JTextField textFieldCity) {
		this.textFieldCity = textFieldCity;
	}

	public void setTextFieldCountry(JTextField textFieldCountry) {
		this.textFieldCountry = textFieldCountry;
	}

	public void setTextFieldNewService(JTextField textFieldNewService) {
		this.textFieldNewService = textFieldNewService;
	}

	public void setTextFieldInternalRate(JTextField textFieldInternalRate) {
		this.textFieldInternalRate = textFieldInternalRate;
	}

	public void setTextFieldExternalRate(JTextField textFieldExternalRate) {
		this.textFieldExternalRate = textFieldExternalRate;
	}

	/**
	 * Creates elements of the third tab in "Projekte"
	 */
	private void createTab3() {
		// HEAD //

		JPanel panelNewClient = new JPanel();
		panelNewClient.setName("panelNewClient");
		tabbedPane.setName("tabbedPanePanelNewClient"); // added
		tabbedPane.addTab("Neuer Kunde", null, panelNewClient, null);
		SpringLayout slPanelNewClient = new SpringLayout();
		panelNewClient.setLayout(slPanelNewClient);

		JLabel lblNewCustomer = new JLabel("Neuer Kunde:");
		lblNewCustomer.setName("lblNewCustomer");
		slPanelNewClient.putConstraint(SpringLayout.NORTH, lblNewCustomer, 10, SpringLayout.NORTH, panelNewClient);
		slPanelNewClient.putConstraint(SpringLayout.WEST, lblNewCustomer, 10, SpringLayout.WEST, panelNewClient);
		lblNewCustomer.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelNewClient.add(lblNewCustomer);

		JPanel panelInputFormCustomer = new JPanel();
		slPanelNewClient.putConstraint(SpringLayout.SOUTH, panelInputFormCustomer, -200, SpringLayout.SOUTH,
				panelNewClient);
		panelInputFormCustomer.setName("panelInputFormCustomer");
		slPanelNewClient.putConstraint(SpringLayout.NORTH, panelInputFormCustomer, 80, SpringLayout.NORTH,
				panelNewClient);
		slPanelNewClient.putConstraint(SpringLayout.WEST, panelInputFormCustomer, 150, SpringLayout.WEST,
				panelNewClient);
		slPanelNewClient.putConstraint(SpringLayout.EAST, panelInputFormCustomer, -425, SpringLayout.EAST,
				panelNewClient);
		panelNewClient.add(panelInputFormCustomer);
		SpringLayout slPanelInputFormCustomer = new SpringLayout();
		panelInputFormCustomer.setLayout(slPanelInputFormCustomer);

		// LABELS //

		JLabel lblClientName = new JLabel("Firma:");
		lblClientName.setName("lblCustomerName");
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, lblClientName, 120, SpringLayout.WEST,
				panelInputFormCustomer);
		panelInputFormCustomer.add(lblClientName);

		JLabel lblContactPerson = new JLabel("Ansprechpartner:");
		lblContactPerson.setName("lblContactPerson");
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, lblContactPerson, 20, SpringLayout.SOUTH,
				lblClientName);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblClientName, 0, SpringLayout.WEST,
				lblContactPerson);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblContactPerson, 20, SpringLayout.WEST,
				panelInputFormCustomer);
		panelInputFormCustomer.add(lblContactPerson);

		JLabel lblTelephone = new JLabel("Telefon:");
		lblTelephone.setName("lblTelephone");
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, lblTelephone, 20, SpringLayout.SOUTH,
				lblContactPerson);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblContactPerson, 0, SpringLayout.WEST, lblTelephone);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblTelephone, 20, SpringLayout.WEST,
				panelInputFormCustomer);
		panelInputFormCustomer.add(lblTelephone);

		JLabel lblMobile = new JLabel("Handy:");
		lblMobile.setName("lblMobile");
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, lblMobile, 20, SpringLayout.SOUTH, lblTelephone);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblTelephone, 0, SpringLayout.WEST, lblMobile);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblMobile, 20, SpringLayout.WEST,
				panelInputFormCustomer);
		panelInputFormCustomer.add(lblMobile);

		JLabel lblStreet = new JLabel("Straße:");
		lblStreet.setName("lblStreet");
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, lblStreet, 20, SpringLayout.SOUTH, lblMobile);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblMobile, 0, SpringLayout.WEST, lblStreet);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblStreet, 20, SpringLayout.WEST,
				panelInputFormCustomer);
		panelInputFormCustomer.add(lblStreet);

		JLabel lblHouseNumber = new JLabel("Hausnummer:");
		lblHouseNumber.setName("lblHouseNumber");
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, lblHouseNumber, 20, SpringLayout.SOUTH, lblStreet);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblStreet, 0, SpringLayout.WEST, lblHouseNumber);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblHouseNumber, 20, SpringLayout.WEST,
				panelInputFormCustomer);
		panelInputFormCustomer.add(lblHouseNumber);

		JLabel lblZip = new JLabel("PLZ:");
		lblZip.setName("lblZip");
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, lblZip, 20, SpringLayout.SOUTH, lblHouseNumber);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblHouseNumber, 0, SpringLayout.WEST, lblZip);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblZip, 20, SpringLayout.WEST,
				panelInputFormCustomer);
		panelInputFormCustomer.add(lblZip);

		JLabel lblCity = new JLabel("Ort:");
		lblCity.setName("lblCity");
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, lblCity, 20, SpringLayout.SOUTH, lblZip);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblZip, 0, SpringLayout.WEST, lblCity);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblCity, 20, SpringLayout.WEST,
				panelInputFormCustomer);
		panelInputFormCustomer.add(lblCity);

		JLabel lblCountry = new JLabel("Land:");
		lblCountry.setName("lblCountry");
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, lblCountry, 20, SpringLayout.SOUTH, lblCity);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblCity, 0, SpringLayout.WEST, lblCountry);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblCountry, 20, SpringLayout.WEST,
				panelInputFormCustomer);
		panelInputFormCustomer.add(lblCountry);

		lblErrorClient = new JLabel();
		lblErrorClient.setName("lblErrorClient");
		lblErrorClient.setForeground(new Color(255, 140, 0));
		lblErrorClient.setVisible(false);
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, lblErrorClient, 20, SpringLayout.SOUTH, lblCountry);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblCountry, 0, SpringLayout.WEST, lblErrorClient);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, lblErrorClient, 20, SpringLayout.WEST,
				panelInputFormCustomer);
		panelInputFormCustomer.add(lblErrorClient);

		// TEXT FIELDS //

		textFieldClientName = new JTextField();
		textFieldClientName.setName("textFieldClientName");
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, lblClientName, 3, SpringLayout.NORTH,
				textFieldClientName);
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, textFieldClientName, 10, SpringLayout.NORTH,
				panelInputFormCustomer);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, textFieldClientName, 144, SpringLayout.WEST,
				panelInputFormCustomer);
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, textFieldClientName, -10, SpringLayout.EAST,
				panelInputFormCustomer);
		panelInputFormCustomer.add(textFieldClientName);
		textFieldClientName.setColumns(10);

		textFieldContact = new JTextField();
		textFieldContact.setName("textFieldContact");
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, lblContactPerson, -24, SpringLayout.WEST,
				textFieldContact);
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, textFieldContact, 14, SpringLayout.SOUTH,
				textFieldClientName);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, textFieldContact, 144, SpringLayout.WEST,
				panelInputFormCustomer);
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, textFieldContact, 0, SpringLayout.EAST,
				textFieldClientName);
		panelInputFormCustomer.add(textFieldContact);
		textFieldContact.setColumns(10);

		textFieldTelephone = new JTextField();
		textFieldTelephone.setName("textFieldTelephone");
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, lblTelephone, -24, SpringLayout.WEST,
				textFieldTelephone);
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, textFieldTelephone, 14, SpringLayout.SOUTH,
				textFieldContact);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, textFieldTelephone, 144, SpringLayout.WEST,
				panelInputFormCustomer);
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, textFieldTelephone, 0, SpringLayout.EAST,
				textFieldContact);
		panelInputFormCustomer.add(textFieldTelephone);
		textFieldTelephone.setColumns(10);

		textFieldMobile = new JTextField();
		textFieldMobile.setName("textFieldMobile");
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, lblMobile, -24, SpringLayout.WEST, textFieldMobile);
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, textFieldMobile, 14, SpringLayout.SOUTH,
				textFieldTelephone);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, textFieldMobile, 144, SpringLayout.WEST,
				panelInputFormCustomer);
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, textFieldMobile, 0, SpringLayout.EAST,
				textFieldTelephone);
		panelInputFormCustomer.add(textFieldMobile);
		textFieldMobile.setColumns(10);

		textFieldStreet = new JTextField();
		textFieldStreet.setName("textFieldStreet");
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, lblStreet, -24, SpringLayout.WEST, textFieldStreet);
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, textFieldStreet, 14, SpringLayout.SOUTH,
				textFieldMobile);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, textFieldStreet, 144, SpringLayout.WEST,
				panelInputFormCustomer);
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, textFieldStreet, 0, SpringLayout.EAST,
				textFieldMobile);
		panelInputFormCustomer.add(textFieldStreet);
		textFieldStreet.setColumns(10);

		textFieldHouseNumber = new JTextField();
		textFieldHouseNumber.setName("textFieldHouseNumber");
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, lblHouseNumber, -24, SpringLayout.WEST,
				textFieldHouseNumber);
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, textFieldHouseNumber, 14, SpringLayout.SOUTH,
				textFieldStreet);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, textFieldHouseNumber, 144, SpringLayout.WEST,
				panelInputFormCustomer);
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, textFieldHouseNumber, 0, SpringLayout.EAST,
				textFieldStreet);
		panelInputFormCustomer.add(textFieldHouseNumber);
		textFieldHouseNumber.setColumns(10);

		textFieldZip = new JTextField();
		textFieldZip.setName("textFieldZip");
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, lblZip, -24, SpringLayout.WEST, textFieldZip);
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, textFieldZip, 14, SpringLayout.SOUTH,
				textFieldHouseNumber);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, textFieldZip, 144, SpringLayout.WEST,
				panelInputFormCustomer);
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, textFieldZip, 0, SpringLayout.EAST,
				textFieldHouseNumber);
		panelInputFormCustomer.add(textFieldZip);
		textFieldZip.setColumns(10);

		textFieldCity = new JTextField();
		textFieldCity.setName("textFieldCity");
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, lblCity, -24, SpringLayout.WEST, textFieldCity);
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, textFieldCity, 14, SpringLayout.SOUTH, textFieldZip);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, textFieldCity, 144, SpringLayout.WEST,
				panelInputFormCustomer);
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, textFieldCity, 0, SpringLayout.EAST, textFieldZip);
		panelInputFormCustomer.add(textFieldCity);
		textFieldCity.setColumns(10);

		textFieldCountry = new JTextField();
		textFieldCountry.setName("textFieldCountry");
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, lblCity, -24, SpringLayout.WEST, textFieldCountry);
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, textFieldCountry, 14, SpringLayout.SOUTH,
				textFieldCity);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, textFieldCountry, 144, SpringLayout.WEST,
				panelInputFormCustomer);
		slPanelInputFormCustomer.putConstraint(SpringLayout.EAST, textFieldCountry, 0, SpringLayout.EAST,
				textFieldCity);
		panelInputFormCustomer.add(textFieldCountry);
		textFieldCountry.setColumns(10);

		// Save Button
		JButton btnSaveCustomer = new JButton("Speichern");
		slPanelInputFormCustomer.putConstraint(SpringLayout.NORTH, btnSaveCustomer, 50, SpringLayout.SOUTH,
				textFieldCountry);
		slPanelInputFormCustomer.putConstraint(SpringLayout.WEST, btnSaveCustomer, 120, SpringLayout.WEST,
				panelInputFormCustomer);
		btnSaveCustomer.setName("btnSaveCustomer");
		btnSaveCustomer.addActionListener(projectController);
		btnSaveCustomer.setActionCommand(StaticActions.ACTION_SAVE_CUSTOMER);
		panelInputFormCustomer.add(btnSaveCustomer);
	}

	/**
	 * Creates elements for the fourth tab in "Projekte"
	 * @author kevin
	 */
	private void createTab4() {

		// HEAD //
		JPanel panelService = new JPanel();
		panelService.setName("panelService");
		tabbedPane.setName("tabbedPanePanelService"); // added
		tabbedPane.addTab("Leistungen", null, panelService, null);
		SpringLayout slPanelService = new SpringLayout();
		panelService.setLayout(slPanelService);

		JLabel lblServiceHead = new JLabel("Neue Leistung anlegen:");
		lblServiceHead.setName("lblServiceHead");
		slPanelService.putConstraint(SpringLayout.NORTH, lblServiceHead, 10, SpringLayout.NORTH, panelService);
		slPanelService.putConstraint(SpringLayout.WEST, lblServiceHead, 10, SpringLayout.WEST, panelService);
		lblServiceHead.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelService.add(lblServiceHead);

		JPanel panelInputFormService = new JPanel();
		panelInputFormService.setName("panelInputFormCustomer");
		slPanelService.putConstraint(SpringLayout.NORTH, panelInputFormService, 80, SpringLayout.NORTH, panelService);
		slPanelService.putConstraint(SpringLayout.WEST, panelInputFormService, 150, SpringLayout.WEST, panelService);
		slPanelService.putConstraint(SpringLayout.SOUTH, panelInputFormService, -333, SpringLayout.SOUTH, panelService);
		slPanelService.putConstraint(SpringLayout.EAST, panelInputFormService, -425, SpringLayout.EAST, panelService);
		panelService.add(panelInputFormService);
		SpringLayout slPanelInputFormService = new SpringLayout();
		panelInputFormService.setLayout(slPanelInputFormService);

		// LABELS //
		JLabel lblNewService = new JLabel("Leistung:");
		lblNewService.setName("lblNewService");
		slPanelInputFormService.putConstraint(SpringLayout.EAST, lblNewService, 120, SpringLayout.WEST,
				panelInputFormService);
		panelInputFormService.add(lblNewService);

		JLabel lblInternalRate = new JLabel("Interner Satz:");
		lblInternalRate.setName("lblInternalRate");
		slPanelInputFormService.putConstraint(SpringLayout.NORTH, lblInternalRate, 20, SpringLayout.SOUTH,
				lblNewService);
		slPanelInputFormService.putConstraint(SpringLayout.WEST, lblNewService, 0, SpringLayout.WEST, lblInternalRate);
		slPanelInputFormService.putConstraint(SpringLayout.WEST, lblInternalRate, 20, SpringLayout.WEST,
				panelInputFormService);
		panelInputFormService.add(lblInternalRate);

		JLabel lblExternalRate = new JLabel("Externer Satz:");
		lblExternalRate.setName("lblExternalRate");
		slPanelInputFormService.putConstraint(SpringLayout.NORTH, lblExternalRate, 20, SpringLayout.SOUTH,
				lblInternalRate);
		slPanelInputFormService.putConstraint(SpringLayout.WEST, lblInternalRate, 0, SpringLayout.WEST,
				lblExternalRate);
		slPanelInputFormService.putConstraint(SpringLayout.WEST, lblExternalRate, 20, SpringLayout.WEST,
				panelInputFormService);
		panelInputFormService.add(lblExternalRate);

		// ErrorLabel
		lblErrorService = new JLabel();
		lblErrorService.setName("lblErrorService");
		lblErrorService.setForeground(new Color(255, 140, 0));
		lblErrorService.setVisible(false);
		slPanelInputFormService.putConstraint(SpringLayout.NORTH, lblErrorService, 20, SpringLayout.SOUTH,
				lblExternalRate);
		slPanelInputFormService.putConstraint(SpringLayout.WEST, lblExternalRate, 0, SpringLayout.WEST,
				lblErrorService);
		slPanelInputFormService.putConstraint(SpringLayout.WEST, lblErrorService, 20, SpringLayout.WEST,
				panelInputFormService);
		panelInputFormService.add(lblErrorService);

		// TEXT FIELDS //
		textFieldNewService = new JTextField();
		textFieldNewService.setName("textFieldNewService");
		slPanelInputFormService.putConstraint(SpringLayout.NORTH, lblNewService, 3, SpringLayout.NORTH,
				textFieldNewService);
		slPanelInputFormService.putConstraint(SpringLayout.NORTH, textFieldNewService, 10, SpringLayout.NORTH,
				panelInputFormService);
		slPanelInputFormService.putConstraint(SpringLayout.WEST, textFieldNewService, 144, SpringLayout.WEST,
				panelInputFormService);
		slPanelInputFormService.putConstraint(SpringLayout.EAST, textFieldNewService, -10, SpringLayout.EAST,
				panelInputFormService);
		panelInputFormService.add(textFieldNewService);
		textFieldNewService.setColumns(10);

		textFieldInternalRate = new JTextField();
		textFieldInternalRate.setName("textFieldInternalRate");
		slPanelInputFormService.putConstraint(SpringLayout.EAST, lblInternalRate, -24, SpringLayout.WEST,
				textFieldInternalRate);
		slPanelInputFormService.putConstraint(SpringLayout.NORTH, textFieldInternalRate, 14, SpringLayout.SOUTH,
				textFieldNewService);
		slPanelInputFormService.putConstraint(SpringLayout.WEST, textFieldInternalRate, 144, SpringLayout.WEST,
				panelInputFormService);
		slPanelInputFormService.putConstraint(SpringLayout.EAST, textFieldInternalRate, 0, SpringLayout.EAST,
				textFieldNewService);
		panelInputFormService.add(textFieldInternalRate);
		textFieldInternalRate.setColumns(10);

		textFieldExternalRate = new JTextField();
		textFieldExternalRate.setName("textFieldExternalRate");
		slPanelInputFormService.putConstraint(SpringLayout.EAST, lblExternalRate, -24, SpringLayout.WEST,
				textFieldExternalRate);
		slPanelInputFormService.putConstraint(SpringLayout.NORTH, textFieldExternalRate, 14, SpringLayout.SOUTH,
				textFieldInternalRate);
		slPanelInputFormService.putConstraint(SpringLayout.WEST, textFieldExternalRate, 144, SpringLayout.WEST,
				panelInputFormService);
		slPanelInputFormService.putConstraint(SpringLayout.EAST, textFieldExternalRate, 0, SpringLayout.EAST,
				textFieldInternalRate);
		panelInputFormService.add(textFieldExternalRate);
		textFieldExternalRate.setColumns(10);

		// BUTTONS //

		// Save Button
		JButton btnSaveService = new JButton("Speichern");
		btnSaveService.setName("btnSaveService");
		slPanelInputFormService.putConstraint(SpringLayout.NORTH, btnSaveService, 43, SpringLayout.SOUTH,
				textFieldExternalRate);
		slPanelInputFormService.putConstraint(SpringLayout.WEST, btnSaveService, 0, SpringLayout.WEST,
				textFieldExternalRate);
		btnSaveService.addActionListener(projectController);
		btnSaveService.setActionCommand(StaticActions.ACTION_SAVE_SERVICE);
		panelInputFormService.add(btnSaveService);
	}

	/**
	 * Displays an error Label for wrong inputs
	 * @author kevin
	 * @param lbl: label that will be modified
	 * @param message: error message that will be shown
	 * @param duration: duration in milliseconds
	 */
	public void showErrorMessage(JLabel lbl, String message, long duration) {
		lbl.setText(message);
		if (!getLblErrorVisible()) {
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					lbl.setVisible(false);
					setLblErrorVisible(false);
				}
			};
			timer.schedule(task, duration);
		}
		lbl.setVisible(true);
	}
	/**
	 * Returns the user input (start date) converted to sql.Date format
	 * @author Mo
	 */
	public Date getNewStartDate() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		java.sql.Date startDate = new Date(System.currentTimeMillis());
		java.util.Date inputStartDate;
		try {
			inputStartDate = format.parse(textFieldStartDate.getText());
		} catch (ParseException e) { // start date set to actual date in case nothing was entered
			inputStartDate = new Date(System.currentTimeMillis());
			// e.printStackTrace();
		}
		startDate.setTime(inputStartDate.getTime());
		return startDate;
	}
	/**
	 * Returns the user input (end date) converted to sql.Date format
	 * @author Mo
	 */
	public Date getNewEndDate() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		java.sql.Date endDate = new Date(System.currentTimeMillis());
		java.util.Date inputEndDate;
		try {
			inputEndDate = format.parse(textFieldEndDate.getText());
		} catch (ParseException e) { // end date set to actual date in case nothing was entered
			inputEndDate = new Date(System.currentTimeMillis());
			// e.printStackTrace();
		}
		endDate.setTime(inputEndDate.getTime());
		return endDate;
	}
	public int getClientID() {
		// TODO: get client id
		return 1;
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof ProjectModel) {
			ArrayList<String> projectNames = new ArrayList<>();
			projectNames.add("");
			((ProjectModel) arg).getProjectList().forEach(project -> {
				projectNames.add(project.get(1).toString());
			});
			this.comboBoxProject.setModel(new DefaultComboBoxModel(projectNames.toArray()));
			System.out.println("Projects loaded into ProjectView.");
		}
		if (arg instanceof ProjectModel && ((ProjectModel) arg).getClientList() != null) {
			ArrayList<String> clientNames = new ArrayList<>();
			clientNames.add(""); // Empty entry for filtering "nothing"
			((ProjectModel) arg).getClientList().forEach(client -> {
				clientNames.add(client.get(1).toString());
			});
			this.comboBoxClient.setModel(new DefaultComboBoxModel(clientNames.toArray()));
			System.out.println("Client loaded into Project View");
		}
		if (arg instanceof ProjectModel && ((ProjectModel) arg).getClientListNewP() != null) {
			ArrayList<String> clientNames = new ArrayList<>();
			((ProjectModel) arg).getClientListNewP().forEach(client -> {
				clientNames.add(client.get(1).toString());
			});
			this.comboBoxClientNewP.setModel(new DefaultComboBoxModel(clientNames.toArray()));
			System.out.println("Client loaded into Project View");
		}
	}

	/**
	 * Updates JTable Object
	 * @author Mo
	 * @param arg: ProjectController
	 */
	public void updateTable(Object arg) {
		if (arg instanceof ProjectController) {

			table.setModel(new DefaultTableModel(((ProjectController) arg).getTableModel(),
					new String[] { "#", "Projektname", "Start", "Ende", "Status", "Kunde"}

			));
			table.getColumnModel().getColumn(0).setPreferredWidth(8);
			table.getColumnModel().getColumn(1).setPreferredWidth(41);
			table.getColumnModel().getColumn(2).setPreferredWidth(44);
			table.getColumnModel().getColumn(4).setPreferredWidth(52);
			table.getColumnModel().getColumn(5).setPreferredWidth(115);
			table.setAutoCreateRowSorter(true);
			
			table.getColumnModel().getColumn(2).setCellRenderer(dateRenderer);
			table.getColumnModel().getColumn(3).setCellRenderer(dateRenderer);
			System.out.println("Update project View");
		}
		sorter = new TableRowSorter<>(table.getModel());
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		int columnIndexToSort = 0;
		sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		table.setRowSorter(sorter);

	}

	public void setTab(int i) {
		tabbedPane.setSelectedIndex(i);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}

	public JTextField getTextFieldFrom() {
		return textFieldFrom;
	}

	public JTextField getTextFieldTo() {
		return textFieldTo;
	}
	
	public JComboBox<String> getComboBoxClient() {
		return comboBoxClient;
	}

	public void setComboBoxClient(JComboBox<String> comboBox) {
		this.comboBoxClient = comboBox;
	}

	public JComboBox<String> getComboBoxClientNewP() {
		return comboBoxClientNewP;
	}
	
	public TableRowSorter<TableModel> getSorter() {
		return sorter;
	}

	public void setSorter(TableRowSorter<TableModel> sorter) {
		this.sorter = sorter;
	}

	public JTable getHourEntryTable() {
		JTable table = this.table;
		return table;
	}

}
