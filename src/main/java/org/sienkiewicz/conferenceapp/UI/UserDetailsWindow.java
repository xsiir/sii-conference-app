package org.sienkiewicz.conferenceapp.UI;

import javax.annotation.PostConstruct;

import org.sienkiewicz.conferenceapp.user.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import io.vavr.control.Either;

@SpringUI
public class UserDetailsWindow extends Window {

	@Autowired
	private UserFacade userFacade;

	private Long lectureId;

	UserDetailsWindow() {
		super();

	}

	@PostConstruct
	public void init() {
		Label welcomeLabel = new Label("Enter your details! " + lectureId);
		Button closeButton = new Button("Close");
		Button saveButton = new Button("Save");
		TextField loginTextField = new TextField("Login");
		TextField emailTextFiedl = new TextField("Email");

		closeButton.addClickListener((e) -> close());

		saveButton.addClickListener((e) -> {
			Either<Exception, Boolean> result = userFacade.assignNewUser(loginTextField.getValue(),
					emailTextFiedl.getValue(), this.lectureId);

			result.onLeft(error -> Notification.show(error.toString()).setDelayMsec(2000));

			result.onRight(x -> {
				close();
				Notification.show("Assgined succesfuly!").setDelayMsec(2000);
			});

		});
		VerticalLayout windowContent = new VerticalLayout();
		windowContent.setMargin(true);
		setContent(windowContent);
		windowContent.addComponent(welcomeLabel);
		windowContent.addComponents(loginTextField, emailTextFiedl, closeButton, saveButton);
		setModal(true);
	}

	public void setLecture(Long id) {
		this.lectureId = id;

	}
}
