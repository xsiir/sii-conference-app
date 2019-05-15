package org.sienkiewicz.conferenceapp.UI;

import java.time.LocalDate;
import java.util.List;

import org.sienkiewicz.conferenceapp.scheduler.Lecture;
import org.sienkiewicz.conferenceapp.scheduler.PlanElement;
import org.sienkiewicz.conferenceapp.scheduler.SchedulerFacade;
import org.sienkiewicz.conferenceapp.scheduler.ThematicPath;
import org.sienkiewicz.conferenceapp.user.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

import io.vavr.control.Either;

@SpringUI
@Theme("valo")
public class ConferenceUI extends UI {

	private final LocalDate _FIRST_DAY = LocalDate.of(2019, 6, 1);
	private final LocalDate _SECOND_DAY = LocalDate.of(2019, 6, 2);

	private final SchedulerFacade schedulerFacade;
	private final UserFacade userFacade;
	private final TableRecord recordTile;
	private final UserDetailsWindow window;

	private VerticalLayout root;

	@Autowired
	ConferenceUI(SchedulerFacade schedulerFacade, UserFacade userFacade, TableRecord recordTile,
			UserDetailsWindow window) {
		super();
		this.schedulerFacade = schedulerFacade;
		this.userFacade = userFacade;
		this.recordTile = recordTile;
		this.window = window;
	}

	@Override
	protected void init(VaadinRequest request) {
		setupLayout();
		updateGUI();
	}

	private void setupLayout() {
		root = new VerticalLayout();
		setContent(root);
	}

	private void updateGUI() {
		root.removeAllComponents();	
		if(isLogged()) displayUserHeader();	
		else displayGuestHeader();
		
		displayDaySchedule(_FIRST_DAY);
		displayDaySchedule(_SECOND_DAY);
	}
	
	
	private void displayGuestHeader() {
		HorizontalLayout loginFormLayout = new HorizontalLayout();
		TextField loginTextField = new TextField("Enter your login...");
		Button confirmButton = new Button("Login");
		confirmButton.addClickListener(onClick -> {
			userFacade.login(loginTextField.getValue());
			updateGUI();
		});
		loginFormLayout.addComponents(loginTextField, confirmButton);
		root.addComponent(loginFormLayout);
	}

	private void displayUserHeader() {
		Label welcomeLabel = new Label(
				"Hi " + userFacade.getLoggedUserLogin() + " your adress email is: " + userFacade.getLoggedUserEmail());

		HorizontalLayout formLayout = new HorizontalLayout();

		Button logoutButton = new Button("Logout");
		logoutButton.addClickListener(click -> {
			userFacade.logout();
			updateGUI();
		});

		TextField emailTextField = new TextField("Change your email adress");
		Button confirmChangeEmailButton = new Button("Confirm");
		confirmChangeEmailButton.addClickListener(click -> {
			Either<Exception, Boolean> result = userFacade.changeUserEmail(emailTextField.getValue());
			result.onRight(action -> updateGUI());
			result.onLeft(error -> Notification.show(error.toString()).setDelayMsec(2000));
		});

		formLayout.addComponents(logoutButton, emailTextField, confirmChangeEmailButton);
		root.addComponents(welcomeLabel, formLayout);

		printLecturesList();
	}

	private void printLecturesList() {
			Label tableHeader = new Label("Your lectures: ");
			List<Lecture> lectures = userFacade.getLoggedUserLectures();
			Grid<Lecture> grid = new Grid<>();
			grid.setItems(lectures);
			grid.addColumn(Lecture::getTitle).setCaption("Name");
			grid.addColumn(Lecture::getSpeaker).setCaption("Speaker");
			grid.addColumn(Lecture::getOcuppiedSeats).setCaption("Registered people");
			grid.addColumn(lecture -> "Unassign!", new ButtonRenderer<>(click -> {
					userFacade.unassing(click.getItem().getId());
					updateGUI();
			}));
			root.addComponents(tableHeader, grid);
	}

	private void displayDaySchedule(LocalDate date) {
		List<ThematicPath> paths = readScheduleOfDate(date);
		Label day = new Label(date.toString());
		HorizontalLayout tableLayout = new HorizontalLayout();

		// For every thematic path...
		for (ThematicPath path : paths) {
			// ...create a column...
			VerticalLayout columnLayout = new VerticalLayout();
			// ...and for every plan element in this path
			for (PlanElement element : path.getPlanOfTheDay()) {
				// ...create tile with details...
				VerticalLayout tile = recordTile.getTile(element);
				// ...and if it is a Lecture, add join button...
				if (element instanceof Lecture) {
					Button button = new Button("Dolacz");
					// .. which will assign user to lecture and update page
					button.addClickListener(c -> {
						if (userFacade.isLogged()) {
							Either<Exception, Boolean> either = userFacade.assingLoggedUserToLecture(element.getId());
							either.onLeft(error -> Notification.show(error.toString()).setDelayMsec(2000));
							either.onRight(action -> updateGUI());
							updateGUI();
						} else {
							window.setLecture(element.getId());
							UI.getCurrent().addWindow(window);
							window.addCloseListener(e -> updateGUI());
						}
					});
					tile.addComponent(button);
				}
				columnLayout.addComponent(tile);
			}
			tableLayout.addComponent(columnLayout);
		}
		root.addComponents(day, tableLayout);
	}

	private List<ThematicPath> readScheduleOfDate(LocalDate date) {
		return schedulerFacade.getThematicPathByDate(date);
	}
	
	private boolean isLogged() {
		return userFacade.isLogged();
	}

}
