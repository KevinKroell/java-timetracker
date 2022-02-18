package main.java.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.regex.PatternSyntaxException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import main.java.controller.DatePicker;
import main.java.controller.SessionController;
import main.java.model.SessionModel;
import main.java.model.StaticActions;
import javax.swing.SwingConstants;

public class SessionView implements IView {

	private JPanel sessionPanel;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane; // Container
	JTabbedPane tabbedPane;
	private JTable table;
	private TableRowSorter<TableModel> sorter;
	private JComboBox<String> comboBoxProject = new JComboBox<String>();
	private JComboBox<String> comboBoxService = new JComboBox<String>();
	private JComboBox<String> comboBoxClient = new JComboBox<String>();
	private JComboBox<String> dropDownProjectName;
	private JComboBox<String> dropDownService;
	private JTextField textFieldDate;
	private JTextField textFieldFrom;
	private JTextField textFieldTo;
	private JTextField textFieldStart;
	private JTextField textFieldEnd;
	private JTextField textFieldPause;
	private JTextField textFieldComment;


	public SessionView(SessionController sessionController) {
		sessionPanel = new JPanel();
		sessionPanel.setName("dashboardMainPane");
		sessionPanel.setBounds(0, 0, 1490, 1060);
		sessionPanel.setBackground(new Color(47,48,52));
		sessionPanel.setLayout(null);
		
		JPanel sessionPanel1 = new JPanel();
		sessionPanel1.setBounds(10, 87, 1470, 944);
		sessionPanel1.setName("dashboardTimerPane");
		sessionPanel1.setBackground(new Color(31,32,33));
		sessionPanel.add(sessionPanel1);
		sessionPanel1.setLayout(null);
		
		
		
		JLabel lblNewLabel = new JLabel("Sitzungen");
		lblNewLabel.setBounds(10, 60, 134, 24);
		sessionPanel.add(lblNewLabel);
		lblNewLabel.setForeground(Color.WHITE);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(15, 15, 1400, 909);
		sessionPanel1.add(tabbedPane);

		JPanel panelHourEntryOverview = new JPanel();
		panelHourEntryOverview.setName("panelHourEntryOverview");
		tabbedPane.addTab("Session �bersicht", null, panelHourEntryOverview, null);
		SpringLayout sl_panelHourEntryOverview = new SpringLayout();
		sl_panelHourEntryOverview.putConstraint(SpringLayout.EAST, comboBoxProject, -900, SpringLayout.EAST,
				panelHourEntryOverview);
		panelHourEntryOverview.setLayout(sl_panelHourEntryOverview);

		// Titel Label
		JLabel lblSessionOverviewHeadline = new JLabel("Session \u00DCbersicht");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, lblSessionOverviewHeadline, 10, SpringLayout.WEST,
				panelHourEntryOverview);
		lblSessionOverviewHeadline.setName("lblHeadTitel");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, lblSessionOverviewHeadline, 10, SpringLayout.NORTH,
				panelHourEntryOverview);
		panelHourEntryOverview.add(lblSessionOverviewHeadline);
		lblSessionOverviewHeadline.setFont(new Font("Tahoma", Font.BOLD, 18));

		// Tabel Frame
		JScrollPane scrollPaneTable = new JScrollPane();
		sl_panelHourEntryOverview.putConstraint(SpringLayout.EAST, scrollPaneTable, -10, SpringLayout.EAST,
				panelHourEntryOverview);
		scrollPaneTable.setName("scrollPaneTable");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, scrollPaneTable, -541, SpringLayout.SOUTH,
				panelHourEntryOverview);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, scrollPaneTable, 10, SpringLayout.WEST,
				panelHourEntryOverview);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.SOUTH, scrollPaneTable, -35, SpringLayout.SOUTH,
				panelHourEntryOverview);
		panelHourEntryOverview.add(scrollPaneTable);
		
		// Creating the table
		table = new JTable(sessionController.getTableModel());
		sessionController.getTableModel().addTableModelListener(table); // add JTable as listener for data changes
		int columnCount = table.getColumnModel().getColumnCount(); // get columncount for following for-loop
		for (int i = 0; i < columnCount; i++) {
			table.getColumnModel().getColumn(i).setHeaderValue(sessionController.getTableModel().getColumnNames()[i]); // set headers manually, since columnames dont refresh?
		}
		scrollPaneTable.setViewportView(table);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		leftRenderer.setHorizontalAlignment(JLabel.LEFT);
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		table.setAutoCreateRowSorter(true);
		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(1).setPreferredWidth(240);
		table.getColumnModel().getColumn(1).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(2).setPreferredWidth(120);
		table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(3).setPreferredWidth(240);
		table.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);
		table.getColumnModel().getColumn(4).setPreferredWidth(120);
		table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(5).setPreferredWidth(120);
		table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(6).setPreferredWidth(70);
		table.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
		sorter = new TableRowSorter<>(table.getModel());
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		table.setRowSorter(sorter);

		// Projects Label
		JLabel lblProjectEntry = new JLabel("Projekt:");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, comboBoxProject, -4, SpringLayout.NORTH,
				lblProjectEntry);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, lblProjectEntry, 120, SpringLayout.NORTH, panelHourEntryOverview);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, lblProjectEntry, 150, SpringLayout.WEST, panelHourEntryOverview);
		lblProjectEntry.setName("lblProjects");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, comboBoxProject, 22, SpringLayout.EAST, lblProjectEntry);
		panelHourEntryOverview.add(lblProjectEntry);
		panelHourEntryOverview.add(comboBoxProject);

		// Projects DropDown
		comboBoxProject.setName("comboBoxProject");
		comboBoxProject.setPreferredSize(new Dimension(300, 20));
		comboBoxProject.setAlignmentX(0.0f);
		comboBoxProject.addActionListener(sessionController);
		comboBoxProject.setActionCommand(StaticActions.ACTION_SESSION_OVERVIEW_SET_PROJECT);
		lblProjectEntry.setLabelFor(comboBoxProject);

		// refresh button projects
		JButton btnLoadProjects = new JButton("\u21BB");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, btnLoadProjects, 0, SpringLayout.NORTH,
				comboBoxProject);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, btnLoadProjects, 10, SpringLayout.EAST,
				comboBoxProject);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.SOUTH, btnLoadProjects, 0, SpringLayout.SOUTH, comboBoxProject);
		btnLoadProjects.setName("btnLoadProjects");
		btnLoadProjects.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 14));
		panelHourEntryOverview.add(btnLoadProjects);
		btnLoadProjects.addActionListener(sessionController);
		btnLoadProjects.setActionCommand(StaticActions.ACTION_SESSION_OVERVIEW_LOAD);

		// Services Label
		JLabel lblServiceEntry = new JLabel("Leistung:");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, lblServiceEntry, 0, SpringLayout.WEST, lblProjectEntry);
		lblServiceEntry.setName("lblService");
		panelHourEntryOverview.add(lblServiceEntry);

		// Services DropDown
		comboBoxService = new JComboBox<String>();
		comboBoxService.setName("comboBoxService");
		comboBoxService.setActionCommand(StaticActions.ACTION_SESSION_OVERVIEW_SET_SERVICE);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, comboBoxService, -4, SpringLayout.NORTH,
				lblServiceEntry);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, comboBoxService, 0, SpringLayout.WEST,
				comboBoxProject);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.EAST, comboBoxService, 0, SpringLayout.EAST,
				comboBoxProject);
		panelHourEntryOverview.add(comboBoxService);

		// Time Frame Label
		JLabel lblTimeFrame = new JLabel("Zeitraum");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, lblTimeFrame, 40, SpringLayout.NORTH,
				lblServiceEntry);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, lblTimeFrame, 0, SpringLayout.WEST, lblServiceEntry);
		lblTimeFrame.setName("lblTimeFrame");
		panelHourEntryOverview.add(lblTimeFrame);

		// search button
		JButton btnApplyFilter = new JButton("Suchen...");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.EAST, btnApplyFilter, -500, SpringLayout.EAST,
				scrollPaneTable);
		btnApplyFilter.setName("btnApplyFilter");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.SOUTH, btnApplyFilter, -19, SpringLayout.NORTH,
				scrollPaneTable);
		panelHourEntryOverview.add(btnApplyFilter);
		btnApplyFilter.addActionListener(sessionController);
		btnApplyFilter.setActionCommand(StaticActions.ACTION_SESSION_OVERVIEW_SEARCH);

		// input start date
		textFieldFrom = new JTextField();
		textFieldFrom.setEditable(false);
		textFieldFrom.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, textFieldFrom, 0, SpringLayout.WEST, comboBoxService);
		textFieldFrom.setName("textFieldFrom");
		panelHourEntryOverview.add(textFieldFrom);
		textFieldFrom.setColumns(20);

		// input start date label
		JLabel lblFrom = new JLabel("von:");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, lblFrom, 20, SpringLayout.NORTH, lblTimeFrame);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.EAST, lblFrom, -10, SpringLayout.WEST, textFieldFrom);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, textFieldFrom, -4, SpringLayout.NORTH,
				lblFrom);
		lblFrom.setName("lblFrom");
		panelHourEntryOverview.add(lblFrom);

		// input end date label
		JLabel lblTo = new JLabel("bis:");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, lblTo, 20, SpringLayout.SOUTH, lblFrom);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, lblTo, 0, SpringLayout.WEST, lblFrom);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.EAST, lblTo, 0, SpringLayout.EAST, lblFrom);
		lblTo.setName("lblTo");
		panelHourEntryOverview.add(lblTo);

		// input end date
		textFieldTo = new JTextField();
		textFieldTo.setEditable(false);
		textFieldTo.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, textFieldTo, -4, SpringLayout.NORTH, lblTo);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, textFieldTo, 0, SpringLayout.WEST,
				textFieldFrom);
		textFieldTo.setName("textFieldTo");
		panelHourEntryOverview.add(textFieldTo);
		textFieldTo.setColumns(10);

		JButton btnSetStartDate = new JButton("...");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.EAST, btnSetStartDate, 0, SpringLayout.EAST, comboBoxService);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.EAST, textFieldFrom, -10, SpringLayout.WEST, btnSetStartDate);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, btnSetStartDate, 0, SpringLayout.NORTH,
				textFieldFrom);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.SOUTH, btnSetStartDate, 0, SpringLayout.SOUTH, textFieldFrom);
		btnSetStartDate.setName("btnSetStartDate");
		panelHourEntryOverview.add(btnSetStartDate);

		JButton btnSetEndDate = new JButton("...");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.EAST, btnSetEndDate, 0, SpringLayout.EAST,
				comboBoxService);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.EAST, textFieldTo, -10, SpringLayout.WEST, btnSetEndDate);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, btnSetEndDate, 0, SpringLayout.NORTH,
				textFieldTo);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.SOUTH, btnSetEndDate, 0, SpringLayout.SOUTH, textFieldTo);
		btnSetEndDate.setName("btnSetEndDate");
		panelHourEntryOverview.add(btnSetEndDate);

		////////////////////////////////////
		///// Second Tab / new project /////
		////////////////////////////////////
		JPanel panelNewHourEntry = new JPanel();
		panelNewHourEntry.setName("panelNewHourEntry");
		tabbedPane.setName("tabbedPaneNewHourEntry");
		tabbedPane.addTab("Neuer Zeiteintrag", null, panelNewHourEntry, null);
		SpringLayout slPanelNewHourEntry = new SpringLayout();
		panelNewHourEntry.setLayout(slPanelNewHourEntry);

		JLabel lblNewHourEntryHeadline = new JLabel("Neuer Eintrag");
		lblNewHourEntryHeadline.setName("lblNewHourEntryHeadline");
		slPanelNewHourEntry.putConstraint(SpringLayout.NORTH, lblNewHourEntryHeadline, 10, SpringLayout.NORTH,
				panelNewHourEntry);
		slPanelNewHourEntry.putConstraint(SpringLayout.WEST, lblNewHourEntryHeadline, 10, SpringLayout.WEST,
				panelNewHourEntry);
		lblNewHourEntryHeadline.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelNewHourEntry.add(lblNewHourEntryHeadline);

		JPanel panel_input_form = new JPanel();
		slPanelNewHourEntry.putConstraint(SpringLayout.EAST, panel_input_form, -370, SpringLayout.EAST,
				panelNewHourEntry);
		panel_input_form.setName("panel_input_form");
		slPanelNewHourEntry.putConstraint(SpringLayout.NORTH, panel_input_form, 80, SpringLayout.NORTH,
				panelNewHourEntry);
		slPanelNewHourEntry.putConstraint(SpringLayout.WEST, panel_input_form, 150, SpringLayout.WEST,
				panelNewHourEntry);
		slPanelNewHourEntry.putConstraint(SpringLayout.SOUTH, panel_input_form, -333, SpringLayout.SOUTH,
				panelNewHourEntry);
		panelNewHourEntry.add(panel_input_form);
		SpringLayout sl_panel_input_form = new SpringLayout();
		panel_input_form.setLayout(sl_panel_input_form);

		JLabel lblProject = new JLabel("Projekt:");
		lblProject.setName("lblProject");
		panel_input_form.add(lblProject);

		dropDownProjectName = new JComboBox<String>();
		dropDownProjectName.setName("dropDownProjectName");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, lblProject, 3, SpringLayout.NORTH,
				dropDownProjectName);
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, dropDownProjectName, 10, SpringLayout.NORTH,
				panel_input_form);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, dropDownProjectName, -10, SpringLayout.EAST,
				panel_input_form);
		panel_input_form.add(dropDownProjectName);
		
		JLabel lblServiceName = new JLabel("Leistung:");
		sl_panel_input_form.putConstraint(SpringLayout.WEST, lblServiceName, 10, SpringLayout.WEST, panel_input_form);
		lblServiceName.setName("lblServiceName");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, lblServiceName, 20, SpringLayout.SOUTH, lblProject);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, lblProject, 0, SpringLayout.WEST, lblServiceName);
		panel_input_form.add(lblServiceName);

		JLabel lblDate = new JLabel("Datum:");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, lblDate, 20, SpringLayout.SOUTH, lblServiceName);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, lblDate, 10, SpringLayout.WEST, panel_input_form);
		lblDate.setName("lblDate");
		panel_input_form.add(lblDate);

		JLabel lblStart = new JLabel("Von:");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, lblStart, 20, SpringLayout.SOUTH, lblDate);
		lblStart.setName("lblStart");
		panel_input_form.add(lblStart);

		dropDownService = new JComboBox<String>();
		dropDownService.setName("dropDownService");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, dropDownService, 14, SpringLayout.SOUTH,
				dropDownProjectName);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, dropDownService, 0, SpringLayout.EAST,
				dropDownProjectName);
		panel_input_form.add(dropDownService);

		textFieldDate = new JTextField(20);
		textFieldDate.setName("textFieldDate");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, textFieldDate, -3, SpringLayout.NORTH,
				lblDate);
		panel_input_form.add(textFieldDate);

		JButton btnSetEntryDate = new JButton("...");
		btnSetEntryDate.setName("btnSetDate");
		sl_panel_input_form.putConstraint(SpringLayout.EAST, textFieldDate, -6, SpringLayout.WEST,
				btnSetEntryDate);
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, btnSetEntryDate, -4, SpringLayout.NORTH, lblDate);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, btnSetEntryDate, -33, SpringLayout.EAST,
				dropDownProjectName);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, btnSetEntryDate, 0, SpringLayout.EAST,
				dropDownProjectName);
		panel_input_form.add(btnSetEntryDate);

		textFieldStart = new JTextField(5);
		textFieldStart.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, lblStart, -5, SpringLayout.WEST, textFieldStart);
		textFieldStart.setName("textFieldEndDate");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, textFieldStart, -3, SpringLayout.NORTH, lblStart);
		panel_input_form.add(textFieldStart);
		lblProject.setName("lblProject");

		// Save Button
		JButton btnSaveEntry = new JButton("Speichern");
		sl_panel_input_form.putConstraint(SpringLayout.WEST, btnSaveEntry, 190, SpringLayout.WEST, panel_input_form);
		btnSaveEntry.setName("btnSaveHourEntry");
		btnSaveEntry.addActionListener(sessionController);
		btnSaveEntry.setActionCommand(StaticActions.ACTION_SESSION_NEW_SAVE);
		panel_input_form.add(btnSaveEntry);
		
		JButton btnResetEntry = new JButton("Reset");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, btnResetEntry, 0, SpringLayout.NORTH, btnSaveEntry);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, btnResetEntry, -10, SpringLayout.WEST, btnSaveEntry);
		btnResetEntry.setName("btnResetInputFields");
		btnResetEntry.setActionCommand(StaticActions.ACTION_SESSION_OVERVIEW_RESET);
		panel_input_form.add(btnResetEntry);
		
		JLabel lblEnd = new JLabel("Bis:");
		lblEnd.setHorizontalAlignment(SwingConstants.RIGHT);
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, lblEnd, 0, SpringLayout.NORTH, lblStart);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, lblEnd, 10, SpringLayout.EAST, textFieldStart);
		lblEnd.setName("lblEnd");
		panel_input_form.add(lblEnd);
		
		textFieldEnd = new JTextField(5);
		textFieldEnd.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, lblEnd, -5, SpringLayout.WEST, textFieldEnd);
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, textFieldEnd, -3, SpringLayout.NORTH, lblEnd);
		textFieldEnd.setName("textFieldEndDate");
		panel_input_form.add(textFieldEnd);
		
		JLabel lblPause = new JLabel("Pause:");
		sl_panel_input_form.putConstraint(SpringLayout.EAST, textFieldEnd, -20, SpringLayout.WEST, lblPause);
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, lblPause, 0, SpringLayout.NORTH, lblEnd);
		lblPause.setName("lblPause");
		panel_input_form.add(lblPause);
		
		textFieldPause = new JTextField(5);
		textFieldPause.setHorizontalAlignment(SwingConstants.CENTER);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, lblPause, -5, SpringLayout.WEST, textFieldPause);
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, textFieldPause, 0, SpringLayout.NORTH, textFieldEnd);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, textFieldPause, 0, SpringLayout.EAST, btnSetEntryDate);
		textFieldPause.setName("textFieldEndDate");
		panel_input_form.add(textFieldPause);
		
		JLabel lblComment = new JLabel("Kommentar:");
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, lblComment, 20, SpringLayout.SOUTH, lblStart);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, lblComment, 10, SpringLayout.WEST, panel_input_form);
		lblComment.setName("lblComment");
		panel_input_form.add(lblComment);
		
		textFieldComment = new JTextField(20);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, textFieldStart, 0, SpringLayout.WEST,
				textFieldComment);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, dropDownProjectName, 0, SpringLayout.WEST,
				textFieldComment);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, dropDownService, 0, SpringLayout.WEST, textFieldComment);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, textFieldDate, 0, SpringLayout.WEST,
				textFieldComment);
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, textFieldComment, -3, SpringLayout.NORTH, lblComment);
		sl_panel_input_form.putConstraint(SpringLayout.WEST, textFieldComment, 5, SpringLayout.EAST, lblComment);
		sl_panel_input_form.putConstraint(SpringLayout.NORTH, btnSaveEntry, 43, SpringLayout.SOUTH, textFieldComment);
		sl_panel_input_form.putConstraint(SpringLayout.EAST, textFieldComment, 0, SpringLayout.EAST, textFieldPause);
		textFieldComment.setName("textFieldComment");
		panel_input_form.add(textFieldComment);

		// Date Popup
		final JFrame popupFrame = new JFrame();
		popupFrame.setName("popupFrame");
		btnSetEntryDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				textFieldDate.setText(new DatePicker(popupFrame).setPickedDate().replace("-", "."));
				System.out.print(textFieldDate.getText());
			}
		});
		

		// Reset project table
		JButton btnResetFilter = new JButton("Reset");
		btnResetFilter.setName("btnResetFilter");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, btnResetFilter, 0, SpringLayout.NORTH,
				btnApplyFilter);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.EAST, btnResetFilter, -10, SpringLayout.WEST,
				btnApplyFilter);
		btnResetFilter.setName("btnResetFilter");
		btnResetFilter.addActionListener(sessionController);
		btnResetFilter.setActionCommand(StaticActions.ACTION_SESSION_OVERVIEW_RESET);
		panelHourEntryOverview.add(btnResetFilter);
		
		JLabel lblClient = new JLabel("Kunde:");
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, lblServiceEntry, 40, SpringLayout.NORTH, lblClient);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, lblClient, 40, SpringLayout.NORTH, lblProjectEntry);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, lblClient, 0, SpringLayout.WEST, lblProjectEntry);
		lblClient.setName("lblClient");
		panelHourEntryOverview.add(lblClient);
		
		comboBoxClient = new JComboBox<String>();
		sl_panelHourEntryOverview.putConstraint(SpringLayout.NORTH, comboBoxClient, -4, SpringLayout.NORTH, lblClient);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.WEST, comboBoxClient, 0, SpringLayout.WEST, comboBoxProject);
		sl_panelHourEntryOverview.putConstraint(SpringLayout.EAST, comboBoxClient, 0, SpringLayout.EAST, comboBoxProject);
		comboBoxClient.setName("comboBoxClient");
		comboBoxClient.setActionCommand(StaticActions.ACTION_SESSION_OVERVIEW_SET_CLIENT);
		panelHourEntryOverview.add(comboBoxClient);
		btnSetStartDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				textFieldFrom.setText(new DatePicker(popupFrame).setPickedDate().replace("-", "."));
				System.out.print(textFieldFrom.getText());
			}
		});
		btnSetEndDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				textFieldTo.setText(new DatePicker(popupFrame).setPickedDate().replace("-", "."));
			}
		});

	}
	
	public JPanel getSessionPanel() {
		return sessionPanel;
	}

	public void setSessionPanel(JPanel sessionPanel) {
	this.sessionPanel = sessionPanel;
	}
	
	public JComboBox<String> getComboBoxProject() {
		return comboBoxProject;
	}

	public void setComboBoxProject(JComboBox<String> comboBox) {
		this.comboBoxProject = comboBox;
	}
	
	public JComboBox<String> getComboBoxService() {
		return comboBoxService;
	}

	public void setComboBoxService(JComboBox<String> comboBox) {
		this.comboBoxService = comboBox;
	}
	
	public JComboBox<String> getComboBoxClient() {
		return comboBoxClient;
	}

	public void setComboBoxClient(JComboBox<String> comboBox) {
		this.comboBoxClient = comboBox;
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

	public void setHourEntryTable(JTable table) {
		this.table = table;
	}

	public String getNewProjectName() {
		String projectName = null;
		if (dropDownProjectName.getSelectedItem() != null) {
			projectName = dropDownProjectName.getSelectedItem().toString();
		}
		return projectName;
	}

	public Date getNewStartDate() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		java.sql.Date startDate = new Date(System.currentTimeMillis());
		java.util.Date inputStartDate;
		try {
			inputStartDate = format.parse(textFieldDate.getText());
		} catch (ParseException e) { // start date set to actual date in case nothing was entered
			inputStartDate = new Date(System.currentTimeMillis());
			// e.printStackTrace();
		}
		startDate.setTime(inputStartDate.getTime());
		return startDate;
	}

	public Date getNewEndDate() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		java.sql.Date endDate = new Date(System.currentTimeMillis());
		java.util.Date inputEndDate;
		try {
			inputEndDate = format.parse(textFieldStart.getText());
		} catch (ParseException e) { // end date set to actual date in case nothing was entered
			inputEndDate = new Date(System.currentTimeMillis());
			// e.printStackTrace();
		}
		endDate.setTime(inputEndDate.getTime());
		return endDate;
	}

	public JTextField getTextFieldFrom() {
		return textFieldFrom;
	}

	public JTextField getTextFieldTo() {
		return textFieldTo;
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof SessionModel) {
			ArrayList<String> projectNames = new ArrayList<>();
			projectNames.add(""); // Empty entry for filtering "nothing"
			((SessionModel) arg).getProjectList().forEach(project -> {
				projectNames.add(project.get(1).toString());
			});
			this.comboBoxProject.setModel(new DefaultComboBoxModel(projectNames.toArray()));
			System.out.println("Projects loaded into ComboBox");
		}
		if (arg instanceof SessionModel && ((SessionModel) arg).getClientList() != null) {
			ArrayList<String> clientNames = new ArrayList<>();
			clientNames.add(""); // Empty entry for filtering "nothing"
			((SessionModel) arg).getClientList().forEach(client -> {
				clientNames.add(client.get(1).toString());
			});
			this.comboBoxClient.setModel(new DefaultComboBoxModel(clientNames.toArray()));
			System.out.println("Clients loaded into ComboBox");
		}
		
		if (arg instanceof SessionModel && ((SessionModel) arg).getServiceList() != null) {
			ArrayList<String> serviceNames = new ArrayList<>();
			serviceNames.add(""); // Empty entry for filtering "nothing"
			((SessionModel) arg).getServiceList().forEach(service -> {
				serviceNames.add(service.get(1).toString());
			});
			this.comboBoxService.setModel(new DefaultComboBoxModel(serviceNames.toArray()));
			System.out.println("Services loaded into ComboBox");
		}
		
	}

	public void setTab(int i) {
		tabbedPane.setSelectedIndex(i);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public JPanel getContentPane() {
		return sessionPanel;
	}

	public void setContentPane(JPanel contentPane) {
		this.sessionPanel = contentPane;
	}
}
