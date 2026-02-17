package com.inn.solid.SRP;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reminder {

	private int reminderId;
	private String note;
	private LocalDateTime time;

	public Reminder() {
		super();
	}

	public Reminder(int reminderId, String note, LocalDateTime time) {
		this.reminderId = reminderId;
		this.note = note;
		this.time = time;
	}

	public int getReminderId() {
		return reminderId;
	}

	public String getNote() {
		return note;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setReminderId(int reminderId) {
		this.reminderId = reminderId;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	

	@Override
	public String toString() {
		return "Reminder [reminderId=" + reminderId + ", note=" + note + ", time=" + time + "]";
	}

}