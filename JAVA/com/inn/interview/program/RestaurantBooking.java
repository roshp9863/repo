package com.inn.interview.program;

     
 import java.time.DayOfWeek;
 import java.time.Instant;
 import java.util.*;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 import java.util.concurrent.ThreadLocalRandom;
 import java.util.stream.Collectors;
     
     class Restraunt{
     
         int id;
         String name;
         int noOfTable;
         List<RestrauntSchedule> schedules;
     
         List<RestrauntTables> tables;
     }
     
     class RestrauntSchedule {
     
         int id;
     
         int restrauntId;
         int dayOfWeek;
         int fromTime;
         int endTime;
     
     }
     
     class RestrauntTables {
     
         int tableSlotId;
         int restrauntId;
         String name;
         Date date;
         int fromTime;
         int endTime;
         int noOfTableLeft;
     
     }
     
     class Booking{
     

		String bookingId;
         String userId;
         int restrauntId;
         Date bookedAt;
         BookingStatus bookingStatus;
         int fromTime;
         int endTime;
     
         int tableSlotId;
         
         @Override
		public String toString() {
			return "Booking [bookingId=" + bookingId + ", userId=" + userId + ", restrauntId=" + restrauntId
					+ ", bookedAt=" + bookedAt + ", bookingStatus=" + bookingStatus + ", fromTime=" + fromTime
					+ ", endTime=" + endTime + ", tableSlotId=" + tableSlotId + "]";
		}

     }
     
     enum BookingStatus {
         RESERVED,
         CONFIRMED,
         CANCELLED
     }
     
     enum Weekday {
     
         MONDAY,
         TUESDAY,
         WEDNESDAY,
         THRUSDAY,
         FRIDAY,
         SATURDAY
     
     }
     
     class RestrauntTableScheduler {
     
         RestrauntService restrauntService;
         final int daysToSchedule;
     
         final int slotDuration;
     
         RestrauntTableScheduler(int daysToSchedule, int slotDuration) {
             this.daysToSchedule = daysToSchedule;
             this.slotDuration = slotDuration;
         }
     
         public List<RestrauntTables> createScheduleForARestraunt(Restraunt restraunt){
             List<RestrauntTables> restrauntTables = new ArrayList<>();
             for (int i =0; i<daysToSchedule; i++){
                 Calendar c = Calendar.getInstance();
                 Date todayDate = Date.from(Instant.now());
                 c.setTime(todayDate);
                 c.add(Calendar.DATE,i);
                 int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                 List<RestrauntSchedule> dayOfWeekSchedules = restraunt.schedules.stream().filter(s -> s.dayOfWeek == dayOfWeek)
                         .collect(Collectors.toList());
                 for(RestrauntSchedule schedule : dayOfWeekSchedules){
                     //from time -> endtime
                     int startTime = schedule.fromTime;
                     int endTime = startTime + (slotDuration * 100);
                     while (endTime<=schedule.endTime) {
                         RestrauntTables restrauntTable = new RestrauntTables();
                         restrauntTable.tableSlotId = ThreadLocalRandom.current().nextInt(1000);
                         restrauntTable.date = todayDate;
                         restrauntTable.restrauntId = restraunt.id;
                         restrauntTable.noOfTableLeft = restraunt.noOfTable;
                         restrauntTable.fromTime = startTime;
                         restrauntTable.endTime = endTime;
                         restrauntTable.name = restraunt.name;
                         startTime = endTime;
                         endTime = startTime + (slotDuration * 100);
                         restrauntTables.add(restrauntTable);
                     }
                 }
             }
             return restrauntTables;
         }
     
     }
     
     class RestrauntService {
     
         final Map<Integer,Restraunt> restraunts;
     
         final RestrauntTableScheduler restrauntTableScheduler;
     
         List<RestrauntTables> restrauntTables;
     
         public RestrauntService(RestrauntTableScheduler restrauntTableScheduler) {
             this.restrauntTableScheduler = restrauntTableScheduler;
             this.restraunts = new HashMap<>();
             restrauntTables = new ArrayList<>();
         }
     
         Restraunt registerRestraunt(final String name, final int tables, final List<RestrauntSchedule> schedules){
     
             Restraunt restraunt = new Restraunt();
             restraunt.id = ThreadLocalRandom.current().nextInt(100,1000);
             restraunt.name = name;
             restraunt.noOfTable = tables;
             restraunt.schedules = schedules;
             restraunts.put(restraunt.id,restraunt);
             restrauntTables.addAll(restrauntTableScheduler.createScheduleForARestraunt(restraunt));
             return restraunt;
         }
     
         List<RestrauntTables> searchRestraunt(String name, Date on, final int fromTime, final int endTime){
     
             return restrauntTables.stream().filter( rt -> rt.name.contains(name))
                     .filter(st -> st.date.equals(on))
                     .filter(st -> st.fromTime >= fromTime)
                     .filter(st -> st.endTime <= endTime).collect(Collectors.toList());
     
         }
     
         List<RestrauntTables> searchRestraunt(final int restrauntId, final Date on, final int fromTime, final int endTime){
     
             List<RestrauntTables> result = restrauntTables.stream()
                     .filter(st -> st.restrauntId == restrauntId)
                     //.filter(st -> st.date.getDate() == on.getDate())
                     .filter(st -> st.fromTime >= fromTime)
                     .filter(st -> st.endTime <= endTime).collect(Collectors.toList());
             return result;
         }
     
     }
     
     class BookingService {
     
         final RestrauntService restrauntService;
     
         final Map<String, Booking> bookings;
     
         BookingService(RestrauntService restrauntService) {
             this.restrauntService = restrauntService;
             this.bookings = new HashMap<>();
         }
     
         Booking bookTable(final String userId, final int restrauntId, final Date on, final int fromTime, final int endTime){
             List<RestrauntTables> result = restrauntService.searchRestraunt(restrauntId, on, fromTime, endTime);
             Optional<RestrauntTables> optional = result.stream()
                     .filter(st -> st.fromTime >= fromTime)
                     .filter(st -> st.endTime <= endTime ).findAny();
     
             if(!optional.isPresent())
                 throw new RuntimeException("No table available for give date");
     
             RestrauntTables tables = optional.get();
             synchronized (tables){
                 if(tables.noOfTableLeft>0) {
                     Booking newBooking = new Booking();
                     newBooking.bookingId = UUID.randomUUID().toString();
                     newBooking.userId = userId;
                     newBooking.bookedAt = Date.from(Instant.now());
                     newBooking.bookingStatus = BookingStatus.RESERVED;
                     newBooking.restrauntId = tables.restrauntId;
                     newBooking.fromTime = tables.fromTime;
                     newBooking.endTime = tables.endTime;
                     newBooking.tableSlotId = tables.tableSlotId;
                     --tables.noOfTableLeft;
                     bookings.put(newBooking.bookingId,newBooking);
                     return newBooking;
                 }else
                     throw new RuntimeException("TableSlots are full for the given time. Please select any other slot");
             }
         }
     
         Booking cancelBooking(final String bookingId){
     
             validateBookingId(bookingId);
             Booking booking = bookings.get(bookingId);
             if(BookingStatus.CANCELLED.equals(booking.bookingStatus))
                 throw new RuntimeException("Booking already cancelled");
     
             Optional<RestrauntTables> optional = restrauntService.restrauntTables.stream()
                     .filter(rt -> rt.tableSlotId == booking.tableSlotId).findAny();
             if(!optional.isPresent())
                 throw new RuntimeException("Invalid booking to cancel : " + bookingId);
             RestrauntTables restrauntTable = optional.get();
             synchronized (restrauntTable){
                 ++restrauntTable.noOfTableLeft;
             }
             booking.bookingStatus = BookingStatus.CANCELLED;
             return booking;
         }
     
         Booking confirmBooking(final String bookingId){
             validateBookingId(bookingId);
             Booking booking = bookings.get(bookingId);
             booking.bookingStatus = BookingStatus.CONFIRMED;
             return booking;
         }
     
         private void validateBookingId(final String bookingId){
             if(!bookings.containsKey(bookingId))
                 throw new RuntimeException("No booking found with bookingId :" + bookingId);
         }
     
     }
     
     
     
     public class RestaurantBooking {
     
         public static void main(String[] args){
     
             RestrauntTableScheduler tableScheduler = new RestrauntTableScheduler(5,1);
     
            RestrauntService restrauntService = new RestrauntService(tableScheduler);
            tableScheduler.restrauntService = restrauntService;
            List<RestrauntSchedule> restrauntSchedules = new ArrayList<>();
            RestrauntSchedule schedule = new RestrauntSchedule();
            schedule.dayOfWeek = DayOfWeek.WEDNESDAY.getValue()+1;
            schedule.fromTime = 1000;
            schedule.endTime = 1300;
            schedule.id = 1;
            restrauntSchedules.add(schedule);
     
             schedule = new RestrauntSchedule();
             schedule.dayOfWeek = DayOfWeek.WEDNESDAY.getValue()+1;
             schedule.fromTime = 1800;
             schedule.endTime = 2400;
             schedule.id = 2;
             restrauntSchedules.add(schedule);
     
            Restraunt restraunt = restrauntService.registerRestraunt("Dhaba",1,restrauntSchedules);
     
            BookingService bookingService = new BookingService(restrauntService);
            int[] startTimings = new int[]{ 1000, 1100, 1200, 1300, 1700, 1800, 1900, 2000, 2100, 2200, 2300, 2400 };
            int size = startTimings.length;
     
             ExecutorService executorService = Executors.newFixedThreadPool(5);
             for(int i=0; i< 15; i++) {
                 executorService.submit(new Runnable() {
                     @Override
                     public void run() {
                         int startTime = startTimings[ThreadLocalRandom.current().nextInt(0, size - 1)];
                         Booking booking = new Booking();
                         booking.fromTime = startTime;
                         booking.endTime = startTime+100;
                         booking.restrauntId = restraunt.id;
                         booking.userId = "nickpro";
                         try {
                             Thread.sleep(startTime);
                              booking = bookingService.bookTable("nickpro", restraunt.id, Date.from(Instant.now()), startTime, startTime + 100);
                             System.out.println("Booking Done :" + booking.fromTime + "-" + booking.endTime);
                             booking = bookingService.cancelBooking(booking.bookingId);
                             System.out.println("Booking Canc :" + booking.fromTime + "-" + booking.endTime);
                         } catch (Exception ex) {
                             System.out.println("Booking fail :" + booking.fromTime + "-" + booking.endTime+ "-" + ex.getMessage());
                         }
                     }
                 });
             }
             executorService.shutdown();
         }
     
     }