package entity;

import java.time.LocalDateTime;

public class Notification {
    public enum NotificationType {
        POINTS_EXPIRY,      
        TIER_UPGRADE,       
        PROMOTIONAL,        
        REDEMPTION_CONFIRM, 
        POINTS_EARNED       
    }

    private String notificationId;
    private String memberId;
    private String message;
    private NotificationType type;
    private LocalDateTime sentDateTime;
    private boolean isRead;

    public Notification(String notificationId, String memberId, String message, NotificationType type) {
        this.notificationId = notificationId;
        this.memberId = memberId;
        this.message = message;
        this.type = type;
        this.sentDateTime = LocalDateTime.now();
        this.isRead = false;
    }

    public String getNotificationId(){ 
        return notificationId;
    }
    public String getMemberId(){
        return memberId; 
    }
    public String getMessage(){
        return message; 
    }
    public NotificationType getType(){
        return type; 
    }
    public LocalDateTime getSentDateTime(){
        return sentDateTime; 
    }
    public boolean isRead(){
        return isRead; 
    }
    public void markAsRead(){
        this.isRead = true; 
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (Read: %b)", 
                type, sentDateTime.toLocalDate(), message, isRead);
    }
}
