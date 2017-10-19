/**
 * Copyright (C) 2015, 2016 Dirk Lemmermann Software & Consulting (dlsc.com) 
 * 
 * This file is part of CalendarFX.
 */

package impl.com.calendarfx.view;

import com.calendarfx.model.Calendar;
import com.calendarfx.view.CalendarHeaderView;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener.Change;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("javadoc")
public class CalendarHeaderViewSkin extends SkinBase<CalendarHeaderView> {

    private GridPane pane;

    private PseudoClass FIRST_COLUMN = PseudoClass.getPseudoClass("first"); //$NON-NLS-1$
    private PseudoClass MIDDLE_COLUMN = PseudoClass.getPseudoClass("middle"); //$NON-NLS-1$
    private PseudoClass LAST_COLUMN = PseudoClass.getPseudoClass("last"); //$NON-NLS-1$

    /**
     * Constructs a new header view skin.
     *
     * @param view
     *            the control to skin
     */
    public CalendarHeaderViewSkin(CalendarHeaderView view) {
        super(view);

        view.setFocusTraversable(false);

        pane = new GridPane();
        getChildren().add(pane);

        InvalidationListener updateBackgroundsListener = evt -> updateColumns();
        view.numberOfDaysProperty().addListener(updateBackgroundsListener);

        updateColumns();

        InvalidationListener calendarVisibilityListener = it -> updateColumns();

        for (Calendar calendar : getSkinnable().getCalendars()) {
            view.getCalendarVisibilityProperty(calendar).addListener(calendarVisibilityListener);
        }

        getSkinnable().getCalendars()
                .addListener((Change<? extends Calendar> change) -> {
                    while (change.next()) {
                        if (change.wasAdded()) {
                            for (Calendar calendar : change.getAddedSubList()) {
                                view.getCalendarVisibilityProperty(calendar).addListener(calendarVisibilityListener);
                            }
                        } else if (change.wasRemoved()) {
                            for (Calendar calendar : change.getRemoved()) {
                                view.getCalendarVisibilityProperty(calendar).removeListener(calendarVisibilityListener);
                            }
                        }
                    }

                    updateColumns();
                });

        view.visibleProperty().addListener(calendarVisibilityListener);
    }

    private void updateColumns() {
        pane.getChildren().clear();

        final CalendarHeaderView headerView = getSkinnable();
        if (headerView.isVisible()) {
            Callback<Calendar, Node> factory = headerView.getCellFactory();

            List<ColumnConstraints> weekConstraints = new ArrayList<>();

            int numberOfDays = headerView.getNumberOfDays();
            for (int i = 0; i < numberOfDays; i++) {
                ColumnConstraints con = new ColumnConstraints();
                con.setPercentWidth((double) 100 / (double) numberOfDays);
                weekConstraints.add(con);

                GridPane dayGridPane = new GridPane();
                dayGridPane.setMaxWidth(Double.MAX_VALUE);
                GridPane.setHgrow(dayGridPane, Priority.ALWAYS);
                GridPane.setVgrow(dayGridPane, Priority.ALWAYS);
                GridPane.setFillHeight(dayGridPane, true);
                GridPane.setFillWidth(dayGridPane, true);

                List<ColumnConstraints> dayConstraints = new ArrayList<>();

                /*
                 * We only care about visible calendars.
                 */
                List<Calendar> calendars = headerView.getCalendars().stream()
                        .filter(headerView::isCalendarVisible)
                        .collect(Collectors.toList());

                int calendarCount = calendars.size();

                double columnWidth = (double) 100 / (double) calendarCount;

                for (int j = 0; j < calendarCount; j++) {
                    Calendar calendar = calendars.get(j);

                    ColumnConstraints calendarConstraint = new ColumnConstraints();
                    calendarConstraint.setPercentWidth(columnWidth);
                    calendarConstraint.setFillWidth(true);
                    calendarConstraint.setHgrow(Priority.ALWAYS);
                    dayConstraints.add(calendarConstraint);

                    Node calendarLabel = factory.call(calendar);

                    calendarLabel.getStyleClass().addAll(
                            "default-style-calendar-header", //$NON-NLS-1$
                            calendar.getStyle() + "-calendar-header"); //$NON-NLS-1$
                    dayGridPane.add(calendarLabel, j, 0);

                    GridPane.setHgrow(calendarLabel, Priority.ALWAYS);
                    GridPane.setVgrow(calendarLabel, Priority.ALWAYS);
                    GridPane.setFillHeight(calendarLabel, true);
                    GridPane.setFillWidth(calendarLabel, true);

                    calendarLabel.pseudoClassStateChanged(FIRST_COLUMN, j == 0);
                    calendarLabel.pseudoClassStateChanged(MIDDLE_COLUMN,
                            j > 0 && j < calendarCount - 1);
                    calendarLabel.pseudoClassStateChanged(LAST_COLUMN,
                            j == calendarCount - 1);
                }

                dayGridPane.getColumnConstraints().setAll(dayConstraints);

                pane.add(dayGridPane, i, 0);
            }

            pane.getColumnConstraints().setAll(weekConstraints);
        }
    }
}
