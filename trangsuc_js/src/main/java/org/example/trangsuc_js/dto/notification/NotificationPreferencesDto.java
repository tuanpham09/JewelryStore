package org.example.trangsuc_js.dto.notification;

import lombok.Data;

@Data
public class NotificationPreferencesDto {
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean pushNotifications;
    private Boolean orderStatusUpdates;
    private Boolean promotionNotifications;
    private Boolean priceDropAlerts;
    private Boolean systemAnnouncements;
    private Boolean reviewReminders;
}
