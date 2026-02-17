package com.inn.solid.SRP.dao;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import com.inn.solid.SRP.Reminder;

public class ReminderDAO {
	
	public void saveReminders(List<Reminder> reminderList, String filename) throws FileNotFoundException {
		//from java7 onwards no need to close the printstream try automatically close this
		try (PrintStream writter = new PrintStream(
				"/Users/roshanipatel/Documents/Development/GIT/staging/repo/JAVA/com/inn/solid/SRP/Reminders/"
						+ filename)) {
			writter.println("THIS IS MY REMINDERS");

			for (Reminder reminder : reminderList) {
				writter.println(reminder);
			}
		}
	}


}
