package com.inn.solid.SRP.utils;

import java.util.ArrayList;
import java.util.List;

import com.inn.solid.SRP.Reminder;
import com.inn.solid.SRP.validation.ReminderValidationChecker;

public class ReminderUtils {
	
	private List<Reminder> reminderList = new ArrayList<>();
	ReminderValidationChecker validate = new ReminderValidationChecker();
	
	public List<Reminder> getReminderList() {
		return reminderList;
	}

	public void setReminderList(List<Reminder> reminderList) {
		this.reminderList = reminderList;
	}

	public void addReminder(Reminder reminder) {
		if(validate.validateReminder(reminder))
			reminderList.add(reminder);
		else
			throw new RuntimeException("Reminder enter is not valid, please check again!!!");
	}

	public void removeReminders(int number) {
		reminderList.remove(--number);
	}


}
