package com.inn.interview.program;

import java.util.*;

// Custom exception for handling invalid operations
class InvalidAppointmentException extends Exception {
    public InvalidAppointmentException(String message) {
        super(message);
    }
}

// Doctor class to represent doctors
class Doctor {
    private String name;
    private String speciality;
    private Set<String> availability;

    public Doctor(String name, String speciality) {
        this.name = name;
        this.speciality = speciality;
        this.availability = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public Set<String> getAvailability() {
        return availability;
    }

    public void setAvailability(Set<String> availability) {
        this.availability = availability;
    }

    // Method to add availability
    public void addAvailability(String timeSlot) {
        availability.add(timeSlot);
    }

    // Method to remove availability
    public void removeAvailability(String timeSlot) {
        availability.remove(timeSlot);
    }
}

// Patient class to represent patients
class Patient {
    private String name;

    public Patient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

// Appointment class to represent appointments
class Appointment {
    private Patient patient;
    private Doctor doctor;
    private String time;
    private boolean waitlist;

    public Appointment(Patient patient, Doctor doctor, String time, boolean waitlist) {
        this.patient = patient;
        this.doctor = doctor;
        this.time = time;
        this.waitlist = waitlist;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getTime() {
        return time;
    }

    public boolean isWaitlist() {
        return waitlist;
    }
}

// Hospital class to manage doctors, patients, and appointments
class Hospital {
    private Map<String, Doctor> doctors;
    private List<Patient> patients;
    private List<Appointment> appointments;
    private int bookingId;

    public Hospital() {
        doctors = new HashMap<>();
        patients = new ArrayList<>();
        appointments = new ArrayList<>();
        bookingId = 1000;
    }

    // Method to register a doctor
    public void registerDoctor(String name, String speciality) {
        doctors.put(name, new Doctor(name, speciality));
        System.out.println("Welcome Dr. " + name + "!!");
    }

    // Method to mark doctor's availability
    public void markDoctorAvailability(String name, Set<String> availability) {
        Doctor doctor = doctors.get(name);
        if (doctor != null) {
            doctor.setAvailability(availability);
            System.out.println("Done Doc!");
        } else {
            System.out.println("Doctor not found!");
        }
    }

    // Method to register a patient
    public void registerPatient(String name) {
        patients.add(new Patient(name));
        System.out.println(name + " registered successfully.");
    }

    // Method to book an appointment
    public void bookAppointment(String patientName, String doctorName, String time, boolean waitlist) throws InvalidAppointmentException {
        Patient patient = getPatient(patientName);
        Doctor doctor = doctors.get(doctorName);
        if (patient != null && doctor != null) {
            if (doctor.getAvailability().contains(time)) {
                appointments.add(new Appointment(patient, doctor, time, waitlist));
                System.out.println("Booked. Booking id: " + bookingId++);
            } else {
                throw new InvalidAppointmentException("Slot not available");
            }
        } else {
            throw new InvalidAppointmentException("Patient or doctor not found!");
        }
    }

    // Method to cancel a booking
    public void cancelBooking(int bookingId) throws InvalidAppointmentException {
        for (Appointment appointment : appointments) {
            if (!appointment.isWaitlist() && appointment.getTime().equals(String.valueOf(bookingId))) {
                appointments.remove(appointment);
                System.out.println("Booking Cancelled");
                return;
            }
        }
        throw new InvalidAppointmentException("Booking not found!");
    }

    // Method to show doctor availability by speciality
    public void showAvailabilityBySpeciality(String speciality) {
        for (Doctor doctor : doctors.values()) {
            if (doctor.getSpeciality().equals(speciality)) {
                System.out.println("Dr." + doctor.getName() + ": " + doctor.getAvailability());
            }
        }
    }

    // Method to show appointments booked by a patient
    public void showAppointmentsBooked(String patientName) {
        for (Appointment appointment : appointments) {
            if (appointment.getPatient().getName().equals(patientName)) {
                System.out.println("Booking id: " + appointment.getTime() + ", Dr " +
                        appointment.getDoctor().getName() + " " + appointment.getTime());
            }
        }
    }

    // Helper method to get patient object by name
    private Patient getPatient(String name) {
        for (Patient patient : patients) {
            if (patient.getName().equals(name)) {
                return patient;
            }
        }
        return null;
    }
}

public class FlipkartHospital {
    public static void main(String[] args) {
        Hospital hospital = new Hospital();
        try {
            hospital.registerDoctor("Curious", "Cardiologist");
            hospital.markDoctorAvailability("Curious", Set.of("9:00-10:00", "12:00-13:00", "16:00-17:00"));

            hospital.registerDoctor("Dreadful", "Dermatologist");
            hospital.markDoctorAvailability("Dreadful", Set.of("9:00-10:00", "11:00-12:00", "13:00-14:00"));

            hospital.showAvailabilityBySpeciality("Cardiologist");

            hospital.registerPatient("PatientA");

            hospital.bookAppointment("PatientA", "Curious", "12:00", false);
            hospital.showAvailabilityBySpeciality("Cardiologist");

            hospital.cancelBooking(1234);
            hospital.showAvailabilityBySpeciality("Cardiologist");

            hospital.bookAppointment("PatientB", "Curious", "12:00", false);

            hospital.registerDoctor("Daring", "Dermatologist");
            hospital.markDoctorAvailability("Daring", Set.of("11:00-12:00", "14:00-15:00"));

            hospital.showAvailabilityBySpeciality("Dermatologist");

            hospital.bookAppointment("PatientF", "Daring", "11:00", false);
            hospital.bookAppointment("PatientA", "Curious", "12:00", false);
            hospital.bookAppointment("PatientF", "Curious", "9:00", false);
            hospital.bookAppointment("PatientC", "Curious", "16:00", false);

            hospital.showAvailabilityBySpeciality("Cardiologist");

            hospital.bookAppointment("PatientD", "Curious", "16:00", true);

            hospital.cancelBooking(5701);

            hospital.showAppointmentsBooked("PatientF");
        } catch (InvalidAppointmentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
