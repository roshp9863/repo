package com.inn.solid.SRP;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

import com.inn.solid.SRP.Reminder;
import com.inn.solid.SRP.dao.ReminderDAO;
import com.inn.solid.SRP.printer.ReminderPrinter;
import com.inn.solid.SRP.utils.ReminderUtils;

public class App {
    public static void main(String[] args) throws FileNotFoundException {

        ReminderPrinter print = new ReminderPrinter();
        ReminderDAO reminderDao = new ReminderDAO();
        
        ReminderUtils reminderOperations = new ReminderUtils();
        
        Reminder dsa = new Reminder(1, "DSA", LocalDateTime.of(2024, 03, 24, 12, 12));
        Reminder systemDesign = new Reminder(2, "SystemDesign", LocalDateTime.of(2024, 03, 25, 12, 12));
        Reminder springBoot = new Reminder(3, "SpringBoot with microservices", LocalDateTime.of(2024, 03, 26, 12, 12));
        reminderOperations.addReminder(dsa);
        reminderOperations.addReminder(systemDesign);
        reminderOperations.addReminder(springBoot);
        print.printReminders(reminderOperations.getReminderList());
        reminderOperations.removeReminders(3);
        System.out.println("===========================================================");
        print.printReminders(reminderOperations.getReminderList());
        reminderDao.saveReminders(reminderOperations.getReminderList(),"MondayReminder");
    }

}