package org.sienkiewicz.conferenceapp.UI;

import java.time.LocalDate;
import java.util.List;

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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("valo")
public class ConferenceUI extends UI {
	
	private final SchedulerFacade schedulerFacade;
	private final UserFacade userFacade;
	private final TableRecord recordTile;
	
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
		addLoginForm();
		displayDaySchedule(_FIRST_DAY);
		displayDaySchedule(_SECOND_DAY);
	}

	private void setupLayout() {
		root = new VerticalLayout();
		setContent(root);
		
	}

	private void addLoginForm() {
		HorizontalLayout formLayout = new HorizontalLayout();
		TextField textField = new TextField("");
		Button confirmButton = new Button("Confirm");
		confirmButton.addClickListener(c -> userFacade.login(textField.getValue()));
		formLayout.addComponents(textField, confirmButton);

		root.addComponent(formLayout);
		
		
	}

	private void displayDaySchedule(LocalDate date) {
		List<ThematicPath> paths = readScheduleOfDate(date);
		Label day = new Label(date.toString());
		HorizontalLayout tableLayout = new HorizontalLayout();
		
		//For every thematic path...
		for(ThematicPath path : paths) {
			//...create a column...
			VerticalLayout columnLayout = new VerticalLayout();
			//...and display schedule of this path
			for(PlanElement element : path.getPlanOfTheDay()) {
				columnLayout.addComponent(recordTile.getTile(element));
			}
			tableLayout.addComponent(columnLayout);
		}
		root.addComponents(day, tableLayout);
	}
	
	private List<ThematicPath> readScheduleOfDate(LocalDate date){
		return schedulerFacade.getThematicPathByDate(date);
	}

}
