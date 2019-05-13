package org.sienkiewicz.conferenceapp.UI;

import org.sienkiewicz.conferenceapp.scheduler.Lecture;
import org.sienkiewicz.conferenceapp.scheduler.PlanElement;
import org.sienkiewicz.conferenceapp.user.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class TableRecord {
	
	@Autowired
	private UserFacade userFacade;

	/**
	 * Method which is responsible for create suitable component 
	 * depending on type of implementation of sent interface.
	 * Every single type will contain duration and title, but if element is instance of Lecture
	 * it will contains labels as speaker, remaining seats or button to save new participant. 
	 * @param element - Interface that point to type of schedule element. 
	 * @return VertialLayout created depending on type of implementation of sent interface
	 */
	VerticalLayout getTile(PlanElement element){
	
		VerticalLayout mainLayout = new VerticalLayout();
		
		addBasicInformations(mainLayout, element);

		if(element instanceof Lecture) {
			addDetailsToLectureTile(mainLayout, element);
		}
		
		return mainLayout;
	}
	
	/**
	 * Add duration and title of plan element in a HorizontalLayout to sent layout
	 * which should be a main layout of tile.
	 * 
	 * @param layout - should be a main layout of tile
	 * @param element - interface which allow to pull information about plan element
	 */
	private void addBasicInformations(Layout layout, PlanElement element) {
		HorizontalLayout titleLayout = new HorizontalLayout();
		Label time = new Label(element.getStartTime() + " - " + element.getEndTime().toString());
		Label title = new Label(element.getTitle());
		titleLayout.addComponents(time, title);
		layout.addComponent(titleLayout);
	}
	
	/**
	 * If element is a instance of Lecture method adds to component as information as speaker,
	 * remaining seats and button to add new participant.
	 * 
	 * @param layout - should be a main layout of tile
	 * @param element - interface which allow to pull information about plan element
	 */
	private void addDetailsToLectureTile(Layout layout, PlanElement element) {
		Lecture lecture = (Lecture) element;
		Label speaker = new Label(lecture.getSpeaker());
		Label remainingSeats = new Label(lecture.getOcuppiedSeats() + "/" + lecture.get_MAX_PARTICIPANT());
		Button button = new Button("Dolacz");
		button.addClickListener(c -> userFacade.assingUserToLecture(element.getId()));
		layout.addComponents(speaker, remainingSeats, button);
	}

}
