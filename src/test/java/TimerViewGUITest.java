package test.java;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.formdev.flatlaf.FlatDarkLaf;

import main.java.controller.DatabaseController;
import main.java.controller.TimerHourController;
import main.java.view.TimerView;

import static org.junit.Assert.assertNotNull;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JPanelFixture;

class TimerViewGUITest {
	private FrameFixture window;

	@BeforeAll
	public static void setUpOnce() {
		FailOnThreadViolationRepaintManager.install();
	}

	@BeforeEach
	public void setUp() {
		DatabaseController dbc = new DatabaseController("sa", "");
		dbc.initializeDB();
		FlatDarkLaf.setup();
		TimerView frame = GuiActionRunner.execute(() -> new TimerView(new TimerHourController()));
		window = new FrameFixture(frame);
		window.show(); // shows the frame to test
	}

	@AfterEach
	public void tearDown() {
		// After each test case we close all windows, deallocate Mouse keys etc.
		window.cleanUp();
	}

	@Test
	void testAllButtons() {
		window.button("btnStart").click();
		window.button("btnPause").click();
		window.button("btnStop").click();
		window.button("btnSave").click();
		window.button("btnReset").click();
		window.button("btnLoadProjects").click();
	}

	@Test
	void testInsertComment() {
		window.textBox("textFieldComment").enterText("This is a comment");
		window.textBox("textFieldComment").deleteText();
	}

	@Test
	void testProjectPanel() {
		window.panel("projectPanel").isEnabled();
	}

	@Test
	void testAllGuiElements() {
		JButtonFixture btnStart = window.button("btnStart");
		assertNotNull(btnStart);
		window.button("btnStart").click();
		
		JButtonFixture btnPause = window.button("btnPause");
		assertNotNull(btnPause);
		window.button("btnPause").click();
		
		JButtonFixture btnStop = window.button("btnStop");
		assertNotNull(btnStop);
		window.button("btnStop").click();
		
		JButtonFixture btnSave = window.button("btnSave");
		assertNotNull(btnSave);
		window.button("btnSave").click();
		
		JButtonFixture btnReset = window.button("btnReset");
		assertNotNull(btnReset);
		window.button("btnReset").click();
		
		JButtonFixture btnLoadProjects = window.button("btnLoadProjects");
		assertNotNull(btnLoadProjects);
		window.button("btnLoadProjects").click();
		
		JPanelFixture projectPanel = window.panel("projectPanel");
		assertNotNull(projectPanel);
		window.panel("projectPanel").isEnabled();
	}
}
