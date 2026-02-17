package com.inn.solid.SRP.validation;

import com.inn.solid.SRP.Reminder;

public class ReminderValidationChecker {
	

	public boolean validateReminder(Reminder reminder) {
		if(reminder.getReminderId()<=0 && !reminder.getNote().isEmpty() && !reminder.getNote().isBlank() && reminder.getNote()!=null) {
			return true;
		}
		return false;
	}


}
