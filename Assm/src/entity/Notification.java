package entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Notification {

    private String notificationId;
    private String memberId;
    private String title;
    private String message;
    private String type;    //welcome, points, redemption,tier_upgrade, expiry_alert, promotion,system
    private LocalDate date;
    private boolean isRead;
    private String priority; //low, medium, high, urgent
    private String actionUrl;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Notification() {
        this.isRead = false;
        this.priority = "Medium";
    }

    public Notification(String memberId, String title, String message, String type, LocalDate date) {
        this.notificationId = "N" + System.currentTimeMillis() + (int)(Math.random() * 1000);
        this.memberId = memberId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.date = date;
        this.isRead = false;
        this.priority = determinePriority(type);
        this.actionUrl = "";
    }

    private String determinePriority(String type) {
        switch (type) {
            case "Tier_Upgrade":
            case "Expiry_Alert":
                return "High";
            case "Redemption_Approved":
                return "High";
            case "Redemption":
            case "Redemption_Request":
                return "Medium";
            case "Promotion":
                return "Medium";
            case "Welcome":
            case "Points":
                return "Low";
            default:
                return "Medium";
        }
    }

    //getter setter
    public String getNotificationId() {
        return notificationId; 
    }
    public void setNotificationId(String notificationId) { 
        this.notificationId = notificationId; 
    }
    
    public String getMemberId() { 
        return memberId;
    }
    public void setMemberId(String memberId) { 
        this.memberId = memberId; 
    }
    
    public String getTitle() { 
        return title; 
    }
    public void setTitle(String title) { 
        this.title = title; 
    }
    
    public String getMessage() { 
        return message; 
    }
    public void setMessage(String message) {
        this.message = message; 
    }
    
    public String getType() { 
        return type; 
    }
    public void setType(String type) { 
        this.type = type; 
    }
    
    public LocalDate getDate() { 
        return date; 
    }
    public void setDate(LocalDate date) { 
        this.date = date; 
    }

    public boolean isRead() { 
        return isRead; 
    }
    public void setRead(boolean read) { 
        isRead = read; 
    }
    
    public String getPriority() { 
        return priority; 
    }
    public void setPriority(String priority) { 
        this.priority = priority; 
    }
    
    public String getActionUrl() { 
        return actionUrl; 
    }
    public void setActionUrl(String actionUrl) { 
        this.actionUrl = actionUrl; 
    }

    public String getPriorityEmoji() {
        switch (priority) {
            case "Urgent": return "🔴";
            case "High": return "🟠";
            case "Medium": return "🟡";
            case "Low": return "🟢";
            default: return "⚪";
        }
    }

     @Override
    public String toString() {
        String status = isRead ? "✓" : "●";
        String priorityEmoji = getPriorityEmoji();
        return String.format("%s %s [%s] %s: %s (Sent: %s)",
                status, priorityEmoji, type, title, message, date.format(dateFormatter));
    }
}