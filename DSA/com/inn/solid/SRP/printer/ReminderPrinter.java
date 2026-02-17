package com.inn.solid.SRP.printer;

import java.util.List;
import com.inn.solid.SRP.Reminder;

public class ReminderPrinter {
	
	public void printReminders(List<Reminder> reminderList) {
		reminderList.forEach(reminder -> System.out.println(reminder));
	}


}
