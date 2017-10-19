/**
 * Copyright (C) 2015, 2016 Dirk Lemmermann Software & Consulting (dlsc.com) 
 * 
 * This file is part of CalendarFX.
 */

package com.calendarfx.view;

import impl.com.calendarfx.view.CalendarSelectorSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Skin;

import com.calendarfx.model.Calendar;

/**
 * A control for choosing a calendar from a list of calendars.
 * <p/>
 * <center><img src="doc-files/calendar-selector.png"></center>
 */
public class CalendarSelector extends CalendarFXControl {

	/**
	 * Constructs a new selector.
	 */
	public CalendarSelector() {
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return new CalendarSelectorSkin(this);
	}

	private final ObservableList<Calendar> calendars = FXCollections.observableArrayList();

	/**
	 * The available list of calendars from which the user can select.
	 *
	 * @return the list of calendars
	 */
	public final ObservableList<Calendar> getCalendars() {
		return calendars;
	}

	/*
	 * Support for value.
	 */
	private final ObjectProperty<Calendar> calendar = new SimpleObjectProperty<>(this, "calendar"); //$NON-NLS-1$

	/**
	 * A property used to store the current value of the control, the currently
	 * selected calendar.
	 *
	 * @return the selected calendar
	 */
	public final ObjectProperty<Calendar> calendarProperty() {
		return calendar;
	}

	/**
	 * Sets the value of {@link #calendarProperty()}.
	 *
	 * @param calendar the selected calendar
	 */
	public final void setCalendar(Calendar calendar) {
		calendarProperty().set(calendar);
	}

	/**
	 * Returns the value of {@link #calendarProperty()}.
	 *
	 * @return the currently selected calendar
	 */
	public final Calendar getCalendar() {
		return calendarProperty().get();
	}
}
