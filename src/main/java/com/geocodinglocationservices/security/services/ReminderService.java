package com.geocodinglocationservices.security.services;

import com.geocodinglocationservices.Service.UserFcmTokenService;
import com.geocodinglocationservices.controllers.NotificationController;
import com.geocodinglocationservices.models.MedicationDetail;
import com.geocodinglocationservices.models.MedicineInvoice;
import com.geocodinglocationservices.models.Order;
import com.geocodinglocationservices.payload.response.NotificationMessage;
import com.geocodinglocationservices.repository.InvoiceRepository;
import com.geocodinglocationservices.repository.OrderRepo;
import com.stripe.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReminderService {
    @Autowired
    private OrderRepo orderRepository;

    @Autowired
    private NotificationController notificationController;
    @Autowired
    private UserFcmTokenService userFcmTokenService;
    private Set<Long> usersToNotify = ConcurrentHashMap.newKeySet();

//    @Autowired
//    private MobileNotificationService mobileNotificationService; // Service to send mobile push notifications
    @Scheduled(fixedRate = 3600000)
    public void checkAndSendRemindersForUser() {
       // System.out.println("checkAndSendRemindersForUser triggered at: " + LocalDateTime.now());
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            MedicineInvoice invoice = order.getInvoice();

            if (invoice == null) {
                System.out.println("Invoice is null for Order ID: " + order.getId());
                continue;
            }

            List<MedicationDetail> medications = invoice.getMedications();

            for (MedicationDetail medication : medications) {
                if (medication != null && shouldSendReminder(medication, order)) {
                  //  System.out.println("Adding user ID " + order.getCustomer().getId() + " to notification list.");
                    usersToNotify.add(order.getCustomer().getId());
                }
            }
        }
        System.out.println(usersToNotify);
        //sendNotificationsToUsers();
    }

    private boolean shouldSendReminder(MedicationDetail medication, Order order) {
        if (medication.getMedicationDosage() == null || medication.getDays() == null) {
            System.out.println("Medication dosage or days is null for Medication ID: " + order.getId());
            return false;
        }

        int dosagePerDay;
        int daysOfMedication;
        try {
           // dosagePerDay = Integer.parseInt(medication.getMedicationDosage());
            daysOfMedication = Integer.parseInt(medication.getDays());
        } catch (NumberFormatException e) {
            return false;
        }

        Timestamp startDate = order.getOrderDate();
        if (startDate == null) {
            System.out.println("Order date is null for Order ID: " + order.getId());
            return false;
        }

        long daysSinceOrder = ChronoUnit.DAYS.between(startDate.toLocalDateTime().toLocalDate(), LocalDate.now());
        int daysLeft = daysOfMedication - (int) daysSinceOrder;
      //  System.out.println("Order ID: " + order.getId() + " | Days since order: " + daysSinceOrder + " | Days left: " + daysLeft);
        return daysLeft <= 3;
    }
    private void sendNotificationsToUsers() {
        for (Long userId : usersToNotify) {
            // Fetch the user's FCM token
            String fcmToken = userFcmTokenService.getFcmTokenByUserId(userId);

            if (fcmToken != null && !fcmToken.isEmpty()) {
                NotificationMessage message = new NotificationMessage();
                message.setMessage("It's time to reorder your medication!");
                message.setReminderTime(LocalDateTime.now());

                // Send notification using the FCM token
                notificationController.sendMedicationReminder(fcmToken, message);
            }

            // Remove the user from the notification list after sending the notification
            removeUserFromNotificationList(userId);
        }
    }

    public boolean shouldNotifyUser(Long userId) {
        System.out.println(usersToNotify);
        return usersToNotify.contains(userId);
    }

    public void removeUserFromNotificationList(Long userId) {
        usersToNotify.remove(userId);
    }


}
