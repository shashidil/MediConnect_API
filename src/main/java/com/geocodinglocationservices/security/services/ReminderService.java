package com.geocodinglocationservices.security.services;

import com.geocodinglocationservices.Service.AuthService;
import com.geocodinglocationservices.Service.UserFcmTokenService;
import com.geocodinglocationservices.controllers.NotificationController;
import com.geocodinglocationservices.models.MedicationDetail;
import com.geocodinglocationservices.models.MedicineInvoice;
import com.geocodinglocationservices.models.Order;
import com.geocodinglocationservices.models.User;
import com.geocodinglocationservices.payload.response.NotificationMessage;
import com.geocodinglocationservices.repository.InvoiceRepository;
import com.geocodinglocationservices.repository.OrderRepo;
import com.geocodinglocationservices.repository.UserRepository;
import com.stripe.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReminderService {
    @Autowired
    private OrderRepo orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserFcmTokenService userFcmTokenService;
    private Map<Long, List<String>> usersToNotify = new ConcurrentHashMap<>();


    //    @Autowired
//    private MobileNotificationService mobileNotificationService; // Service to send mobile push notifications
    @Scheduled(fixedRate = 3600000)
    public void checkAndSendRemindersForUser() {
    List<Order> orders = orderRepository.findAll();

    for (Order order : orders) {
        MedicineInvoice invoice = order.getInvoice();

        if (invoice == null) {
            System.out.println("Invoice is null for Order ID: " + order.getId());
            continue;
        }

        List<MedicationDetail> medications = invoice.getMedications();

        for (MedicationDetail medication : medications) {
            Long userId = order.getCustomer().getId();

            if (medication != null && shouldSendReminder(medication, order)) {
                // Notify before medication finishes
                sendNotificationsToUsers(userId, medication.getMedicationName());
                usersToNotify.computeIfAbsent(userId, k -> new ArrayList<>()).add(medication.getMedicationName());
            } else if (medication != null && shouldSendAfterFinish(medication, order)) {
                // Notify after medication finishes
                sendNotificationsToUsers(userId, medication.getMedicationName());
                usersToNotify.computeIfAbsent(userId, k -> new ArrayList<>()).add(medication.getMedicationName());
            }
        }
    }
    System.out.println(usersToNotify);
}


    private boolean shouldSendReminder(MedicationDetail medication, Order order) {
        if (medication.getMedicationDosage() == null || medication.getDays() == null) {
            System.out.println("Medication dosage or days is null for Medication ID: " + order.getId());
            return false;
        }

        int daysOfMedication;
        try {
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

        return daysLeft <= 3;
    }

    private boolean shouldSendAfterFinish(MedicationDetail medication, Order order) {
        int daysOfMedication;
        try {
            daysOfMedication = Integer.parseInt(medication.getDays());
        } catch (NumberFormatException e) {
            return false;
        }

        Timestamp startDate = order.getOrderDate();
        long daysSinceOrder = ChronoUnit.DAYS.between(startDate.toLocalDateTime().toLocalDate(), LocalDate.now());
        int daysLeft = daysOfMedication - (int) daysSinceOrder;

        // Notify if the medication has finished (daysLeft is less than 0)
        return daysLeft < 0;
    }


    private void sendNotificationsToUsers(Long userId, String medicationName) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            String userToken = user.get().getDeviceToken();
            if (userToken != null && !userToken.isEmpty()) {
                // Send the custom message as notification
                notificationService.sendReorderNotification(userToken, medicationName);
            } else {
                System.out.println("No device token found for User ID: " + userId);
            }
        }
    }



    public boolean shouldNotifyUser(Long userId) {
        return usersToNotify.containsKey(userId);
    }

    public List<String> getMedicationsForUser(Long userId) {
        return usersToNotify.getOrDefault(userId, new ArrayList<>());
    }

    public void removeUserFromNotificationList(Long userId) {
        usersToNotify.remove(userId);
    }


}
