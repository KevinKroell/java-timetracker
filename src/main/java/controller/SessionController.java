package main.java.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.event.DocumentEvent;

import main.java.model.CustomTableModel;
import main.java.model.IModel;
import main.java.model.Regex;
import main.java.model.SessionModel;
import main.java.model.StaticActions;
import main.java.model.User;
import main.java.view.IView;
import main.java.view.SessionView;

public class SessionController implements IController {

	private JDialog frame;
	private int editedSessionID = 0;
	private SessionModel editModel;
	private SessionView editView;
	
	private SessionModel sessionModel;
	private SessionView sessionView;
	private CustomTableModel tableData;
	private DatabaseController db = DatabaseController.getInstance();

	
	/**
	 * Creates a new CustomTableModel for hour entries and sets header titles.
     * Calls method to query needed data and sets renderer properties for table.
     * It also calls the methods to fill dropdowns with information.
	 * 
     * @author Leander
     * 
     */
	// Constructor
	@SuppressWarnings("deprecation")
	public SessionController() {
		
		this.tableData = new CustomTableModel(new String[] {
				"#",
				"Datum",
				"Projekt",
				"Kunde",
				"Leistung",
				"Beschreibung",
				"Start",
				"Ende",
				"Dauer",
				"Pause"
			});
		
		queryData();
		this.sessionModel = new SessionModel();
		this.sessionView = new SessionView(this);

		this.sessionModel.addObserver(this.sessionView);

		actionLoadProjects();
		actionLoadServices();
		actionLoadClients();
		actionLoadProjectsNewEntry();
		actionLoadServicesNewEntry();
		sumDuration();
	}

	/**
	 * Queries needed data for displaying hour entry information.
	 * Formats everything to Strings except start and end times.
	 * This information is stored as actual "Date" object, for 
	 * making it possible to filter for time periods.
	 * 
	 * @author Leander
	 */
	public void queryData() {
		ArrayList<Object> result = db.query(
				"SELECT hour_entry.h_id, entry_date, project.name, customer.company, service.name, hour_entry.description, start_time, end_time, time_minutes, pause_minutes "
				+ "FROM hour_entry "
				+ "LEFT JOIN service ON hour_entry.s_id = service.s_id "
				+ "LEFT JOIN project ON hour_entry.p_id = project.p_id "
				+ "LEFT JOIN customer ON project.c_id = customer.c_id "
				+ "WHERE u_id = " + User.getUser().getU_id()
				+ " ORDER BY h_id DESC;");
		Object[][] resultArray = new Object[result.size()][10];
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		formatter.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		
		for (int i = 0; i < result.size(); i++) {
			for (int j = 0; j < 10; j++) {
				ArrayList<Object> row = (ArrayList<Object>) result.get(i);
				String value = row.get(j).toString();
				// If column is for start or end time
				if (j == 6 || j == 7) {
					String[] split = value.split("\\.");
					value = split[0] + ".000";
					java.util.Date date = null;
					try {
						date = formatter.parse(value);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					resultArray[i][j] = date;
				// else just convert to String 
				} else {
					if (j == 0) {
						value = String.format("%1$5s", value).replace(' ', '0');
					} else if (j == 1) {
						value = LocalDate.parse(value)
								.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
					} else if (j == 8 || j == 9) {
						String hours = (Integer.parseInt(value) / 60)+"";
						String minutes = (Integer.parseInt(value) % 60)+"";
						value = ("00" + hours).substring(hours.length()) + ":" + ("00" + minutes).substring(minutes.length()) + " h";
					}
					resultArray[i][j] = value;
				}
			}
		}
		this.tableData.setData(resultArray);		
	}
	
	public void actionLoadProjects() {
		this.sessionModel.setProjectSet(false);
		this.sessionModel.retrieveProjects();
	}

	public void actionLoadServices() {
		this.sessionModel.setServiceSet(false);
		this.sessionModel.retrieveServices();
	}

	public void actionLoadClients() {
		this.sessionModel.setClientSet(false);
		this.sessionModel.retrieveClients();
	}
	
	public void actionLoadProjectsNewEntry() {
		this.sessionModel.setProjectNewEntrySet(false);
		this.sessionModel.retrieveProjectsNewEntry();
	}

	public void actionLoadServicesNewEntry() {
		this.sessionModel.setServiceNewEntrySet(false);
		this.sessionModel.retrieveServicesNewEntry();
	}

	public SessionView getSessionView() {
		return sessionView;
	}

	/**
	 * Reads data given by user to filter with.
	 * If filters are filled out, this method adds them to a List of Row Filters.
	 * It then applies these filters with an AND operation on the table data.
	 * <p>
	 * <b>Filters:</b><br>
	 * Projectname<br>
	 * Client<br>
	 * Service<br>
	 * From (Date)<br>
	 * To (Date)<br>
	 * @author Leander
	 */
	public void actionFilterEntries() {
		String projectFilter = "";
		String clientFilter = "";
		String serviceFilter = "";
		String startFilter = sessionView.getTextFieldFrom().getText();
		String endFilter = sessionView.getTextFieldTo().getText();

		List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		formatter.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		
		if (sessionView.getComboBoxProject().getItemCount() > 0) {
			projectFilter = sessionView.getComboBoxProject().getSelectedItem().toString();
			if (!projectFilter.equals("")) {
				filters.add(RowFilter.regexFilter("^" + projectFilter + "$", 2));
			}
		}
		if (sessionView.getComboBoxClient().getItemCount() > 0) {
			clientFilter = sessionView.getComboBoxClient().getSelectedItem().toString();
			if (!clientFilter.equals("")) {
				filters.add(RowFilter.regexFilter("^" + clientFilter + "$", 3));
			}
		}
		if (sessionView.getComboBoxService().getItemCount() > 0) {
			serviceFilter = sessionView.getComboBoxService().getSelectedItem().toString();
			if (!serviceFilter.equals("")) {
				filters.add(RowFilter.regexFilter("^" + serviceFilter + "$", 4));
			}
		}
		if (!startFilter.equals("bitte Datum w�hlen...") && !startFilter.equals("")) {
			String start = startFilter.split(" ", 1)[0].replace(".", "-");
			java.util.Date startDate = null;
			try {
				startDate = formatter.parse(start);
				System.out.println("Filter from: " + startDate);
			} catch (ParseException e) {
				System.out.println("Error while parsing Date: " + start);
			}
			if (startDate != null)
				new Date(startDate.getTime() - (1000 * 60 * 60 * 24)); // subtract one day, so it is included
				filters.add(RowFilter.dateFilter(ComparisonType.AFTER, startDate, 6));
		}
		if (!endFilter.equals("bitte Datum w�hlen...") && !endFilter.equals("")) {
			String end = endFilter.split(" ", 1)[0].replace(".", "-");
			java.util.Date endDate = null;
			try {
				endDate = formatter.parse(end);
				System.out.println("Filter to: " + endDate);
			} catch (ParseException e) {
				System.out.println("Error while parsing Date: " + end);
			}
			if (endDate != null)
				endDate = new Date(endDate.getTime() + (1000 * 60 * 60 * 24)); // add one day, so it is included
			filters.add(RowFilter.dateFilter(ComparisonType.BEFORE, endDate, 7));
		}

		if (filters.size() > 0) {
			sessionView.getSorter().setRowFilter(RowFilter.andFilter(filters));
		} else {
			sessionView.getSorter().setRowFilter(null);
		}

	}

	public CustomTableModel getTableModel() {
		return tableData;
	}

	/**
	 * Sets view to read userinput data from depending on given argument.
	 * Reads userinput from view, validates it and then updates or inserts data
	 * to database (also depending on boolean argument).
	 * <p>
	 * If this method is called through an action fired by an "edit view", it updates
	 * the data in the database (argument = true). <br>
	 * If this method is called through an action fired by the "new entry" view 
	 * (normal tabbed pane) it inserts new data (new entry is being created).
	 * @param editingExistentEntry Set 'true' if existent hour entry data should be
	 * edited and not being newly created.
	 * @author Leander
	 */
	public void actionSaveEntry(boolean editingExistentEntry) {
		SessionView view = null;
		if (editingExistentEntry) {
			view = this.editView;
		} else {
			view = this.sessionView;
		}
		
		SessionModel model = null;
		if (editingExistentEntry) {
			model = this.editModel;
		} else {
			model = this.sessionModel;
		}
		
		
		view.getLblErrorMessageNewEntry().setText("");
		
		// Get information from input
		if (view.getDropDownProjectName().getItemCount() < 1) {
			view.getLblErrorMessageNewEntry().setText("Bitte w�hlen/erstellen Sie zuerst ein Projekt.");
			return;
		} 
		if (view.getDropDownService().getItemCount() < 1) {
			view.getLblErrorMessageNewEntry().setText("Bitte w�hlen/erstellen Sie zuerst eine Leistung.");
			return;
		}
		int projectDropdownIndex = view.getDropDownProjectName().getSelectedIndex();
		int projectID = (int) model.getProjectListNewEntry().get(projectDropdownIndex).get(0);

		int serrviceDropdownIndex = view.getDropDownService().getSelectedIndex();
		int serviceID = (int) model.getServiceListNewEntry().get(serrviceDropdownIndex).get(0);

		String dateString = view.getTextFieldDate().getText();
		
		String from = view.getTextFieldStart().getText();
		String to = view.getTextFieldEnd().getText();
		String pause = view.getTextFieldPause().getText();
		String comment = view.getTextFieldComment().getText();
		
		
		// Validation
		boolean validDate = Regex.validate(dateString, Regex.VALID_DATE_FORMAT_DD_MM_YYYY);
		boolean validFrom = Regex.validate(from, Regex.VALID_TIME_FORMAT_HH_MM);
		boolean validTo = Regex.validate(to, Regex.VALID_TIME_FORMAT_HH_MM);
		boolean validPause = Regex.validate(pause, Regex.VALID_TIME_FORMAT_HH_MM) || pause.equals("");
		
		// With successfull validation convert inputs and write to database
		if (validDate && validFrom && validTo && validPause) {
			
			java.sql.Date date = createDateFromString(dateString);
			
			// if start greater than end another day has started and a day needs to be added
			int additionalDay = 0;
			if (to.compareTo(from) < 0) {
				additionalDay = 1;
			}
			
			LocalDateTime start = createLocalDateTime(date, from);
			LocalDateTime end = createLocalDateTime(date, to).plusDays(additionalDay);
			
			int pauseMinutes = calculateMinutesFromHourFormat(pause);
			int durationMinutes = calculateMinutesBetweenLocalDateTimes(start, end) - pauseMinutes;
			
			if (durationMinutes < 0){
				view.getLblErrorMessageNewEntry().setText("Fehler: Pause ist l�nger als Dauer.");
				return;
			}
			
			if (editingExistentEntry) {
				db.run("UPDATE hour_entry SET "
						+ "entry_date = '" + date + "',"
						+ "description = '" + comment + "',"
						+ "start_time = '" + start + "',"
						+ "end_time = '" + end + "',"
						+ "time_minutes = '" + durationMinutes + "',"
						+ "pause_minutes = '" + pauseMinutes + "',"
						+ "p_id = '" + projectID + "',"
						+ "s_id = '" + serviceID + "' "
						+ "WHERE h_id = '" + editedSessionID + "' "
						+ "AND u_id = '" + User.getUser().getU_id() + "';");
			} else {
				db.run("INSERT INTO hour_entry(entry_date,description,start_time,end_time,time_minutes,pause_minutes,p_id,s_id,u_id) VALUES("
						+ "'" + date + "'," 
						+ "'" + comment + "'," 
						+ "'" + start + "'," 
						+ "'" + end + "'," 
						+ "'" + durationMinutes + "'," // productive time (pauseMinutes is subtracted)
						+ "'" + pauseMinutes + "',"
						+ "'" + projectID + "',"
						+ "'" + serviceID + "',"
						+ "'" + User.getUser().getU_id() + "');");	
			}
			
			// Empty input fields
			view.getTextFieldStart().setText("");
			view.getTextFieldEnd().setText("");
			view.getTextFieldPause().setText("");
			view.getTextFieldComment().setText("");
		} else {
			String errorDate = "";
			String errorFrom = "";
			String errorTo = "";
			String errorPause = "";
			
			if (!validDate)
				errorDate = "'Datum' ";
			if (!validFrom)
				errorFrom = "'Von' ";
			if (!validTo)
				errorTo = "'Bis' ";
			if (!validPause)
				errorPause = "'Pause'";
			
			String error = "Fehler in folgenden Feldern: " + errorDate + errorFrom + errorTo + errorPause;
			view.getLblErrorMessageNewEntry().setText(error);
		}
		
	}

	/**
	 * Creates a LocalDateTime object from Date object for day and a String 
	 * representing a time in format HH:mm
	 * 
	 * @param date Date object containing the wanted day.
	 * @param time String in HH:mm format.
	 * @return Parsed LocalDateTime
	 * @author Leander
	 */
	private LocalDateTime createLocalDateTime(java.sql.Date date, String time) {
		time = time.replace(".", ":");
		LocalDate dateFrom = date.toLocalDate();
		LocalTime timeFrom = LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm"));
		LocalDateTime start = LocalDateTime.of(dateFrom, timeFrom);
		return start;
	}
	
	/**
	 * Creates a Date object from a given String in format dd-MM-yyyy.
	 * @param dd_MM_yyyy String in format dd-MM-yyyy
	 * @return Date object
	 * @author Leander
	 */
	private java.sql.Date createDateFromString(String dd_MM_yyyy) {
		String[] splitDate = dd_MM_yyyy.replace(".", "-").split("-");
 		return java.sql.Date.valueOf(splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0]);
	}
	
	/**
	 * Calculates the minutes between two given dates (LocalDateTime) 
	 * rounded to minutes.
	 * @param start LocalDateTime of start
	 * @param end LocalDateTime of end
	 * @return Integer representing the amount of minutes between dates.
	 * @author Leander
	 */
	private int calculateMinutesBetweenLocalDateTimes(LocalDateTime start, LocalDateTime end) {
		return Math.round(Duration.between(start, end).getSeconds() / 60);
	}
	
	/**
	 * Calculates minutes from a String in format HH:mm.
	 * @param pause String in time format HH:mm
	 * @return Integer representing the amount of minutes.
	 * @author Leander
	 */
	private int calculateMinutesFromHourFormat(String pause) {
		if (Regex.validate(pause, Regex.VALID_TIME_FORMAT_HH_MM)) {
			String[] split = pause.split(":");
			int hours = Integer.parseInt(split[0]);
			int minutes = Integer.parseInt(split[1]);
			return hours * 60 + minutes;
		} else {
			return 0;
		}
	}
	
	// ActionListener method
	@Override
	public void actionPerformed(ActionEvent e) {
		String event = e.getActionCommand();
		System.out.println("ACTION: " + event.toString());

		if (event.equalsIgnoreCase(StaticActions.ACTION_TIMER_SAVE)) {
			ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			executorService.schedule(() -> {
				queryData();
				sessionView.getSorter().setRowFilter(null);
				sumDuration();
			}, 1, TimeUnit.SECONDS);
			
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_OVERVIEW_LOAD)) {
			actionLoadProjects();
			actionLoadServices();
			actionLoadClients();
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_OVERVIEW_SEARCH)) {
			queryData();
			if (sessionView.getHourEntryTable().getRowCount() > 0) {
				actionFilterEntries();
				sumDuration();				
			}
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_OVERVIEW_RESET)) {
			actionLoadProjects();
			actionLoadServices();
			actionLoadClients();
			queryData();
			sessionView.getSorter().setRowFilter(null);
			sumDuration();
			resetInputFieldsOverview();
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_OVERVIEW_SET_PROJECT)) {
			this.sessionModel.setProjectSet(true);
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_OVERVIEW_SET_CLIENT)) {
			this.sessionModel.setClientSet(true);
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_OVERVIEW_SET_SERVICE)) {
			this.sessionModel.setServiceSet(true);
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_OVERVIEW_EDIT_PROJECT)) {
			openEditDialog();			
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_OVERVIEW_DELETE_PROJECT)) {
			deleteSelectedMethod();
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_NEW_SAVE)) {
			actionSaveEntry(false);
			queryData();
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_NEW_RESET)) {
			actionLoadProjectsNewEntry();
			actionLoadServicesNewEntry();
			resetFieldsNewEntry();
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_NEW_SET_PROJECT)) {
			this.sessionModel.setProjectNewEntrySet(true);
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_NEW_SET_SERVICE)) {
			this.sessionModel.setServiceNewEntrySet(true);
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_EDIT_SAVE)) {
			actionSaveEntry(true);
			queryData();
			this.frame.dispose();
		}
		if (event.equalsIgnoreCase(StaticActions.ACTION_SESSION_EDIT_ABORT)) {
			this.frame.dispose();
		}
	}

	/**
	 * Gets all visible duration values from column "Dauer" 
	 * and calculates a sum. <br>
	 * This sum is then displayed under the JTable.
	 */
	private void sumDuration() {
		System.out.println(Integer.parseInt("00"));
		int total = 0;
		for(int i = 0; i < sessionView.getHourEntryTable().getRowCount(); i++){
			String[] values = ((String) sessionView.getHourEntryTable().getValueAt(i, 8)).replace(" h", "").split(":");
			int hours = Integer.parseInt(values[0]);
	        int minutes = Integer.parseInt(values[1]);
	        total += hours*60;
	        total += minutes;
	    }
		String durationSum = String.format("%1$2s", total/60).replace(' ', '0') + ":" + String.format("%1$2s", total%60).replace(' ', '0') + " Std.";
		sessionView.getLblDurationSum().setText("Summe Arbeitszeit: " + durationSum);
	}
	
	/**
	 * Resets all fields in the "new entry" tab to their defaults.
	 * @author Leander
	 */
	private void resetFieldsNewEntry() {
		sessionView.getTextFieldDate().setText("bitte Datum w�hlen...");
		sessionView.getTextFieldStart().setText("");
		sessionView.getTextFieldEnd().setText("");
		sessionView.getTextFieldPause().setText("");
		sessionView.getTextFieldComment().setText("");
		sessionView.getLblErrorMessageNewEntry().setText("");
	}

	/**
	 * Reads information from selected table row and asks for confirmation.
	 * When confirmed, this method deletes the selected hour entry from 
	 * database and re-queries data.
	 * 
	 * @author Leander
	 */
	private void deleteSelectedMethod() {
		int row = sessionView.getHourEntryTable().getSelectedRow();
		if (row > -1) {
			int sessionID = Integer.parseInt((String) sessionView.getHourEntryTable().getValueAt(row, 0));
			int input = JOptionPane.showConfirmDialog(null, 
		            "Sind Sie sicher, dass Sie den ausgew�hlten Eintrag l�schen m�chten?", "L�schen best�tigen", 
		            JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		    // -1=abortX, 0=ok, 2=cancel
		    
			if (input == 0) {
				db.run("DELETE FROM hour_entry WHERE h_id = " + sessionID + " AND u_id = " + User.getUser().getU_id() + ";");
				queryData();
			}
		}
	}

	/**
	 * Opens edit dialog for selected hour entry if a table row is selected.
	 * <p>
	 * Reads data from the selected table row and places it intro 
	 * the user input fields (pre fillout).<br>
	 * <p>
	 * The view is generated from the input form panel defined in the class
	 * 'SessionView'.<br>
	 * To work slightly different, this method changes the fired actions and
	 * the appearance of the buttons in this view.
	 * @author Leander
	 */
	private void openEditDialog() {
		int row = sessionView.getHourEntryTable().getSelectedRow();
		if (row > -1) {			
			DateFormat df = new SimpleDateFormat("HH:mm");
			
			// Read values from TableModel
			editedSessionID = Integer.parseInt((String) sessionView.getHourEntryTable().getValueAt(row, 0));
			String project = (String) sessionView.getHourEntryTable().getValueAt(row, 2);
			String service = (String) sessionView.getHourEntryTable().getValueAt(row, 4);
			String date = (String) sessionView.getHourEntryTable().getValueAt(row, 1);
			String from = df.format((java.util.Date) sessionView.getHourEntryTable().getValueAt(row, 6));
			String to = df.format((java.util.Date) sessionView.getHourEntryTable().getValueAt(row, 7));
			String pause = ((String) sessionView.getHourEntryTable().getValueAt(row, 9)).split(" ")[0];
			String comment = (String) sessionView.getHourEntryTable().getValueAt(row, 5);
			
			// Instanciate new view and new model, but keep this controller
			editView = new SessionView(this);
			editModel = new SessionModel();
			editModel.addObserver(editView);
			editModel.retrieveProjectsNewEntry();
			editModel.retrieveServicesNewEntry();
			
			// Pre-fillout input fields
			editView.getDropDownProjectName().setSelectedItem(project);
			editView.getDropDownService().setSelectedItem(service);
			editView.getTextFieldDate().setText(date);
			editView.getTextFieldStart().setText(from);
			editView.getTextFieldEnd().setText(to);
			editView.getTextFieldPause().setText(pause);
			editView.getTextFieldComment().setText(comment);
						
			// Change button-behaviour and texts
			editView.getBtnSaveEntry().setActionCommand(StaticActions.ACTION_SESSION_EDIT_SAVE);
			editView.getBtnResetEntry().setText("Abbrechen");
			editView.getBtnResetEntry().setActionCommand(StaticActions.ACTION_SESSION_EDIT_ABORT);
			
			// Setup new JDialog
			frame = new JDialog(new JFrame(), "Eintrag bearbeiten", true);
			frame.getContentPane().add(editView.getPanelInputForm()); // Just load Input-Panel
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			frame.setVisible(true);				
		}
	}

	/**
	 * Resets als input field to their default values.
	 * @author Leander
	 */
	private void resetInputFieldsOverview() {
		sessionView.sortTableDescendingDate();
		sessionView.getTextFieldFrom().setText("bitte Datum w�hlen...");
		sessionView.getTextFieldTo().setText("bitte Datum w�hlen...");
		sessionView.getLblErrorMessageNewEntry().setText("");
		sessionView.getBtnDeleteEntry().setVisible(false);
		sessionView.getBtnEditEntry().setVisible(false);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {

	}

	@Override
	public void removeUpdate(DocumentEvent e) {

	}

	@Override
	public void changedUpdate(DocumentEvent e) {

	}

	@Override
	public IModel getModel() {
		return null;
	}

	@Override
	public IView getView() {
		return null;
	}
}
