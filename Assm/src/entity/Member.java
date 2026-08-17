//Member.java - Entity Class
package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member {
    private String memberId;          // 8-digit unique ID
    private Guest guest;
    private String tier;              // Silver, Gold, Platinum, Diamond
    private int points;
    private int lifetimePoints;
    private LocalDate joinDate;
    private LocalDate pointsExpiryDate;
    private LocalDate lastActivityDate;
    private List<String> redemptionHistory;
    private List<String> promotionHistory;
    private int stayCount;
    private double totalSpent;
    private String preferredRoomType;
    private List<String> preferences;

    public Member() {
        this.tier ="Silver";
        this.points = 0;
        this.lifetimePoints = 0;
        this.joinDate = LocalDate.now();
        this.pointsExpiryDate = LocalDate.now().plusYears(1);
        this.lastActivityDate = LocalDate.now();
        this.redemptionHistory = new ArrayList<>();
        this.promotionHistory = new ArrayList<>();
        this.stayCount = 0;
        this.totalSpent = 0.0;
        this.preferences = new ArrayList<>();
        this.preferredRoomType = "Standard";
    }

    public Member(Guest guest) {
        this.memberId = "M" + System.currentTimeMillis();
        this.guest = guest;
        this.tier = "Silver";
        this.points = 100;     //welcome bonus
        this.lifetimePoints = 100;
        this.joinDate = LocalDate.now();
        this.pointsExpiryDate = LocalDate.now().plusYears(1);
        this.lastActivityDate = LocalDate.now();
        this.redemptionHistory = new ArrayList<>();
        this.promotionHistory = new ArrayList<>();
        this.stayCount = 0;
        this.totalSpent = 0.0;
        this.preferences = new ArrayList<>();
        this.preferredRoomType = "Standard";
        
    }
}