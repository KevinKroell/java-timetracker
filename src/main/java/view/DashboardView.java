package main.java.view;

import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Observable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.java.controller.DashboardController;
import main.java.controller.DashboardHourListController;
import main.java.controller.DashboardProjectListController;
import main.java.controller.LayoutManager;
import main.java.controller.NewProjectController;
import main.java.controller.TimerHourController;
import main.java.model.StaticActions;
import main.java.model.User;

import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class DashboardView implements IView {

	private static final long serialVersionUID = 1L;

	private JPanel timerPanel;
	private JPanel dashbPanel;
	private TimerHourController timerHourController;
	private NewProjectController newProjectController;
	private DashboardHourListController hourListController;
	private DashboardProjectListController projectListController;

	private JButton btnProjectShowMore;
	private JButton btnSessionShowMore;

	public DashboardView(DashboardController dashboardController) {
		timerHourController = dashboardController.getTimerHourController();
		newProjectController = dashboardController.getNewProjectController();
		hourListController = dashboardController.getDashboardHourListController();
		projectListController = dashboardController.getDashboardProjectListController();

		dashbPanel = new JPanel();
		dashbPanel.setName("dashboardMainPane");
		dashbPanel.setBounds(0, 0, 1490, 960);
		dashbPanel.setBackground(new Color(47, 48, 52));

		dashbPanel.setLayout(null);

		JLabel lblDashboard = new JLabel("Dashboard\r\n");
		lblDashboard.setForeground(Color.WHITE);
		lblDashboard.setBounds(10, 60, 76, 24);
		dashbPanel.add(lblDashboard);

		JPanel productPanel = new JPanel();
		productPanel.setName("dashboardProductivityPane");
		productPanel.setBackground(new Color(31, 32, 33));
		productPanel.setBounds(10, 699, 355, 250);
		JLabel productImage = new JLabel(LayoutManager.getImageIcon(LayoutManager.IMAGE_PRODUCTIVITY, 230, 199));
		productImage.setBounds(10, 40, 335, 199);
		productImage.setVisible(true);
		productPanel.add(productImage);
		dashbPanel.add(productPanel);
		productPanel.setLayout(null);

		JLabel lblProductivity = new JLabel("Produktivit\u00E4t");
		lblProductivity.setForeground(Color.WHITE);
		lblProductivity.setBounds(10, 11, 102, 24);
		productPanel.add(lblProductivity);

		timerPanel = new JPanel();
		timerPanel.setName("dashboardTimerPane");
		timerPanel.setBackground(new Color(31, 32, 33));
		timerPanel.setBounds(10, 87, 354, 600);
		dashbPanel.add(timerPanel);
		TimerView timerView_1 = timerHourController.getTimerView();
		timerPanel.setLayout(null);
		timerPanel.add(timerView_1.getContentPanel());

		JPanel projectPanel = new JPanel();
		projectPanel.setName("dashboardProjectsPane");
		projectPanel.setLayout(null);
		projectPanel.setBackground(new Color(31, 32, 33));
		projectPanel.setBounds(373, 87, 811, 424);

		dashbPanel.add(projectPanel);

		JLabel lblCurrentProjects = new JLabel("Aktuelle Projekte\r\n");
		lblCurrentProjects.setName("lblCurrentProjects");
		lblCurrentProjects.setForeground(Color.WHITE);
		lblCurrentProjects.setBounds(10, 11, 105, 24);
		projectPanel.add(lblCurrentProjects);

		btnProjectShowMore = new JButton("mehr anzeigen");
		btnProjectShowMore.setActionCommand(StaticActions.ACTION_MENU_PROJECTS);
		btnProjectShowMore.setName("btnProjectShowMore");
		btnProjectShowMore.setForeground(Color.ORANGE);
		btnProjectShowMore.setBounds(631, 390, 170, 23);
		projectPanel.add(btnProjectShowMore);
		btnProjectShowMore.setOpaque(false);
		btnProjectShowMore.setContentAreaFilled(false);
		btnProjectShowMore.setBorderPainted(false);
		btnProjectShowMore.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnProjectShowMore.setForeground(Color.WHITE);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnProjectShowMore.setForeground(Color.ORANGE);
			}
		});

		JPanel panelProjectList = new JPanel();
		panelProjectList.setLayout(new BoxLayout(panelProjectList, BoxLayout.X_AXIS));
		panelProjectList.setName("projectListPanel");
		panelProjectList.add(projectListController.getView().getScrollPane());
		panelProjectList.setBackground(new Color(35, 36, 38));
		panelProjectList.setBounds(10, 68, 791, 314);
		projectPanel.add(panelProjectList);

		JPanel sessionPanel = new JPanel();
		sessionPanel.setName("dashboardLastSessionsPane");
		sessionPanel.setLayout(null);
		sessionPanel.setBackground(new Color(31, 32, 33));
		sessionPanel.setBounds(375, 522, 721, 427);
		dashbPanel.add(sessionPanel);

		JLabel lblLastSessions = new JLabel("Letzte Sitzungen\r\n");
		lblLastSessions.setName("lblLastSessions");
		lblLastSessions.setForeground(Color.WHITE);
		lblLastSessions.setBounds(10, 11, 102, 24);
		sessionPanel.add(lblLastSessions);

		btnSessionShowMore = new JButton("mehr anzeigen");
		btnSessionShowMore.setActionCommand(StaticActions.ACTION_MENU_SESSIONS);
		btnSessionShowMore.setName("btnSessionShowMore");
		btnSessionShowMore.setOpaque(false);
		btnSessionShowMore.setForeground(Color.ORANGE);
		btnSessionShowMore.setContentAreaFilled(false);
		btnSessionShowMore.setBorderPainted(false);
		btnSessionShowMore.setBounds(541, 397, 170, 23);
		sessionPanel.add(btnSessionShowMore);
		btnSessionShowMore.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				btnSessionShowMore.setForeground(Color.WHITE);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				btnSessionShowMore.setForeground(Color.ORANGE);
			}
		});

		JPanel panelHourList = new JPanel();
		panelHourList.setName("hourListPanel");
		panelHourList.setLayout(new BoxLayout(panelHourList, BoxLayout.X_AXIS));
		panelHourList.add(hourListController.getView().getScrollPane());
		panelHourList.setBackground(new Color(35, 36, 38));
		panelHourList.setBounds(10, 68, 701, 324);
		sessionPanel.add(panelHourList);

		JPanel timelinePanel = new JPanel();
		timelinePanel.setName("dashboardTimelinePane");
		//timelinePanel.setLayout(null);
		timelinePanel.setBackground(new Color(31, 32, 33));
		timelinePanel.setBounds(1106, 522, 365, 171);
		timelinePanel.setLayout(null);
		JLabel timelineImage = new JLabel(LayoutManager.getImageIcon(LayoutManager.IMAGE_TIMELINE, 345, 120));
		timelineImage.setBounds(10, 40, 345, 120);
		timelineImage.setVisible(true);
		timelinePanel.add(timelineImage);
		dashbPanel.add(timelinePanel);

		JLabel lblTimeline = new JLabel("Timeline");
		lblTimeline.setForeground(Color.WHITE);
		lblTimeline.setBounds(10, 11, 102, 24);
		timelinePanel.add(lblTimeline);

		JPanel upcomingPanel = new JPanel();
		upcomingPanel.setName("dashboardActivityPane");
		upcomingPanel.setLayout(null);
		upcomingPanel.setBackground(new Color(31, 32, 33));
		upcomingPanel.setBounds(1194, 87, 277, 424);
		dashbPanel.add(upcomingPanel);

		JLabel lblUpcoming = new JLabel("Anstehende\r\n");
		lblUpcoming.setForeground(Color.WHITE);
		lblUpcoming.setBounds(10, 11, 75, 28);
		upcomingPanel.add(lblUpcoming);

		JLabel lblActivities = new JLabel("Aktivitäten");
		lblActivities.setForeground(Color.WHITE);
		lblActivities.setBounds(10, 30, 75, 28);
		upcomingPanel.add(lblActivities);
		
		JTextPane textPane = new JTextPane();
		textPane.setText(
				"Heute, 10:00 Uhr:\t Präsentation \n\n"
				+ "Heute, 10:00 Uhr:\t Telefonat \n\n"
				+ "Heute, 12:00 Uhr:\t Mittagessen KD \n\n"
				+ "Heute, 16:00 Uhr:\t Briefing \n\n"
				+ "Morgen, 08:00 Uhr:\t Außentermin \n\n"
				+ "Morgen, 10:00 Uhr:\t MA Gespräch \n\n"
				+ "Morgen, 17:00 Uhr:\t Review \n\n"
				+ "24.02., 10:00 Uhr:\t Fortbildung \n\n"
				+ "25.02., 10:00 Uhr:\t Fortbildung");
		textPane.setBackground(new Color(31, 32, 33));
		textPane.setBounds(10, 69, 257, 344);
		upcomingPanel.add(textPane);

		JPanel newprojectPanel = new JPanel();
		newprojectPanel.setName("dashboardNewProjectPane");
		newprojectPanel.setLayout(null);
		newprojectPanel.setBackground(new Color(31, 32, 33));
		newprojectPanel.setBounds(1106, 704, 365, 245);
		dashbPanel.add(newprojectPanel);
		NewProjectView newProjectView = newProjectController.getNewProjectView();
		newprojectPanel.add(newProjectView.getContentPanel());
		
		JLabel lblGreeting = new JLabel("Hallo, " + User.getUser().getName());
		lblGreeting.setForeground(Color.ORANGE);
		lblGreeting.setFont(LayoutManager.getFont("dinNeuzeitGrotesk_regular").deriveFont(54.0f));
		lblGreeting.setBounds(375, 18, 809, 65);
		dashbPanel.add(lblGreeting);
		
		JLabel lblToday = new JLabel("Heute: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		lblToday.setHorizontalAlignment(SwingConstants.RIGHT);
		lblToday.setForeground(Color.ORANGE);
		lblToday.setBounds(1194, 43, 277, 40);
		dashbPanel.add(lblToday);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public void update(Observable o, Object arg) {
	}

	public JPanel getDashbPanel() {
		return dashbPanel;
	}

	public void setDashbPanel(JPanel dashbPanel) {
		this.dashbPanel = dashbPanel;
	}

	public JPanel getTimerPanel() {
		return timerPanel;
	}

	public void setTimerPanel(JPanel timerPanel) {
		this.timerPanel = timerPanel;
	}

	public JButton getBtnProjectShowMore() {
		return btnProjectShowMore;
	}

	public JButton getBtnSessionShowMore() {
		return btnSessionShowMore;
	}
}