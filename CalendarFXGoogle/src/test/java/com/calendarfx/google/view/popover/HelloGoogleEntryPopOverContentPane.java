/**
 * Copyright (C) 2015, 2016 Dirk Lemmermann Software & Consulting (dlsc.com) 
 * 
 * This file is part of CalendarFX.
 */

package com.calendarfx.google.view.popover;

import com.calendarfx.google.model.GoogleEntry;
import com.calendarfx.model.Calendar;
import com.calendarfx.view.CalendarView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Sample for the google entry pop over.
 *
 * Created by gdiaz on 13/01/2017.
 */
public class HelloGoogleEntryPopOverContentPane extends Application {

	@Override
	public void start (Stage primaryStage) throws Exception {
		Calendar calendar = new Calendar();
		calendar.setName("Google Calendar");
		calendar.setStyle(Calendar.Style.STYLE2);

		GoogleEntry entry = new GoogleEntry();
		entry.setTitle("Google Entry");
		entry.setCalendar(calendar);
		entry.setLocation("Bogota");

		ObservableList<Calendar> allCalendars = FXCollections.observableArrayList(calendar);

		GoogleEntryPopOverContentPane pane = new GoogleEntryPopOverContentPane(entry, allCalendars);

		primaryStage.setTitle("Google Calendar");
		Scene scene = new Scene(pane, 400, 600);
		scene.getStylesheets().add(CalendarView.class.getResource("calendar.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.centerOnScreen();
		primaryStage.show();
	}
}
