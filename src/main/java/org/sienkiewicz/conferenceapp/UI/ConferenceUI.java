package org.sienkiewicz.conferenceapp.UI;

import java.time.LocalDate;
import java.util.List;

import javax.swing.Action;
import javax.swing.WindowConstants;

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
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;

import io.vavr.control.Either;

@SpringUI
@Theme("valo")
public class ConferenceUI extends UI {

	private final SchedulerFacade schedulerFacade;
	private final UserFacade userFacade;
	private final TableRecord recordTile;

	@Autowired
	private UserDetailsWindow window;

	private final LocalDate _FIRST_DAY = LocalDate.of(2019, 6, 1);
	private final LocalDate _SECOND_DAY = LocalDate.of(2019, 6, 2);

	private VerticalLayout root;

	@Autowired
	ConferenceUI(SchedulerFacade schedulerFacade, UserFacade userFacade, TableRecord recordTile) {
		super();
		this.schedulerFacade = schedulerFacade;
		this.userFacade = userFacade;
		this.recordTile = recordTile;
	}

	@Override
	protected void init(VaadinRequest request) {
		setupLayout();
		userFacade.testRegister();
		displayHeadernForm();
		printLecturesIfLogged();
		displayDaySchedule(_FIRST_DAY);
		displayDaySchedule(_SECOND_DAY);
	}

	private void setupLayout() {
		root = new VerticalLayout();
		setContent(root);
	}

	private void printLecturesIfLogged() {
		if (userFacade.isLogged()) {
			Label label = new Label("Twoje wyklady na ktore jestes zapisany to: ");
			List<Lecture> lectures = userFacade.getLoggedUserLectures();
			Grid<Lecture> grid = new Grid<>();
			grid.setItems(lectures);
			grid.addColumn(Lecture::getTitle).setCaption("Nazwa");
			grid.addColumn(Lecture::getSpeaker).setCaption("Speaker");
			grid.addColumn(Lecture::getOcuppiedSeats).setCaption("Miejsca");
			grid.addColumn(lecture -> "Wypisz!", new ButtonRenderer<>(click -> {
				Notification.show("JUZ NIE BEDZIESZ NA PRZYJECIU O ID " + click.getItem().getId());
			}));
			root.addComponents(label, grid);

		}
	}

	private void displayHeadernForm() {
		HorizontalLayout formLayout = new HorizontalLayout();

		if (!userFacade.isLogged()) {
			TextField textField = new TextField("Wprowadz swoj login...");
			Button confirmButton = new Button("Zaloguj");
			confirmButton.addClickListener(c -> {
				userFacade.login(textField.getValue());
				update();
			});
			formLayout.addComponents(textField, confirmButton);
		} else {
			Button confirmButton = new Button("Wyloguj");
			confirmButton.addClickListener(c -> {
				userFacade.logout();
				update();
			});
			formLayout.addComponent(confirmButton);
		}

		root.addComponent(formLayout);

	}

	private void update() {
		root.removeAllComponents();
		displayHeadernForm();
		printLecturesIfLogged();
		displayDaySchedule(_FIRST_DAY);
		displayDaySchedule(_SECOND_DAY);

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
							either.onRight(action -> update());
							update();
						} else {
							window.setLecture(element.getId());
							UI.getCurrent().addWindow(window);
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

}
