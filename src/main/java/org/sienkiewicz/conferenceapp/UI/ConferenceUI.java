package org.sienkiewicz.conferenceapp.UI;

import java.time.LocalDate;
import java.util.List;

import org.sienkiewicz.conferenceapp.scheduler.Lecture;
import org.sienkiewicz.conferenceapp.scheduler.PlanElement;
import org.sienkiewicz.conferenceapp.scheduler.SchedulerFacade;
import org.sienkiewicz.conferenceapp.scheduler.ThematicPath;import org.sienkiewicz.conferenceapp.user.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SpringUI
@Theme("valo")
public class ConferenceUI extends UI {
	
	private VerticalLayout root;
	
	@Autowired
	private SchedulerFacade facade;
	
	@Autowired
	private UserFacade userFacade;
	
	@Autowired
	private TableRecord tile;
	
	@Override
	protected void init(VaadinRequest request) {
		setupLayout();
		addLoginForm();
		addTable();
		
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

	private void addTable() {
		List<ThematicPath> paths = facade.getThematicPathByDate(LocalDate.of(2019, 6, 1));
		HorizontalLayout tableLayout = new HorizontalLayout();
		for(ThematicPath path : paths) {
			VerticalLayout columnLayout = new VerticalLayout();
			for(PlanElement element : path.getPlanOfTheDay()) {
				columnLayout.addComponent(tile.getTile(element));
			}
			tableLayout.addComponent(columnLayout);
		}
		root.addComponent(tableLayout);
	}

}
