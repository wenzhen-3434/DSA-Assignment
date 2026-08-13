//Member.java - Entity Class
package entity;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;

public class Member implements Comparable<Member> {
    private String memberId;          // 8-digit unique ID
    private String icPassportNo;
    private String name;
    private String email;
    private String phoneNumber;
    private String tier;              // Silver, Gold, Platinum, Diamond
    private int loyaltyPoint;
    private LocalDate joinDate;
    private LocalDate pointExpiryDate;
    private boolean notificationEnable;

    // Loyalty tier thresholds
    public static final int SILVER_THRESHOLD = 0;
    public static final int GOLD_THRESHOLD = 5000;
    public static final int PLATINUM_THRESHOLD = 15000;
    public static final int DIAMOND_THRESHOLD = 30000;

    // Points expiration period (months)
    public static final int POINT_EXPIRY_MONTHS = 12;

    // Constructor for new member
    public Member(String memberId, String name, String email, String phoneNumber, String icPassportNo, String tier, int points, LocalDate joinDate) {
        this.memberId = memberId;
        this.name = name;
        this.icPassportNo = icPassportNo;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.tier = tier;
        this.loyaltyPoint = points;
        this.joinDate = joinDate;
        this.pointExpiryDate = joinDate.plusMonths(POINT_EXPIRY_MONTHS);
        this.notificationEnable = true;
    }

    //Add points and check for tier upgrade
    public boolean addPoints(int points) {
        if (points <= 0) {
            return false;
        }

        this.loyaltyPoint += points;
        updatePointsExpiry();
        checkUpdateTier();
        return true;
    }

    //Redeem points
    public boolean redeemPoints(int points) {
        if (points <= 0 || points > this.loyaltyPoint) {
            return false;
        }

        this.loyaltyPoint -= points;
        return true;
    }

    //Check and update tier based on points
    private void checkUpdateTier() {
        String newTier = calculateTier(this.loyaltyPoint);
        if (!newTier.equals(this.tier)) {
            this.tier = newTier;
        }
    }

    // Calculate tier based on points
    public static String calculateTier(int points) {
        if (points >= DIAMOND_THRESHOLD) {
            return "Diamond";
        } else if (points >= PLATINUM_THRESHOLD) {
            return "Platinum";
        } else if (points >= GOLD_THRESHOLD) {
            return "Gold";
        } else {
            return "Silver";
        }
    }

    // Update points expiry date (extend by 12 months from current date)
    private void updatePointsExpiry() {
        this.pointExpiryDate = LocalDate.now().plusMonths(POINT_EXPIRY_MONTHS);
    }

    /**
     * Check if points are expiring soon (within 30 days)
     */
    public boolean isPointsExpiringSoon() {
        long daysUntilExpiry = LocalDate.now().until(pointExpiryDate, ChronoUnit.DAYS);
        return daysUntilExpiry <= 30 && daysUntilExpiry >= 0;
    }

    //Check if points expired
    public boolean hasPointsExpired() {
        return LocalDate.now().isAfter(pointExpiryDate) && loyaltyPoint > 0;
    }

    //getter setter
    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTier() {
        return tier;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoint;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public LocalDate getPointsExpiryDate() {
        return pointExpiryDate;
    }

    public boolean isNotificationsEnabled() {
        return notificationEnable;
    }

    public void setNotificationsEnabled(boolean enabled) {
        this.notificationEnable = enabled;
    }

    public String getIcPassportNo() {
        return icPassportNo;
    }

    public void setIcPassportNo(String icPassportNo) {
        this.icPassportNo = icPassportNo;
    }

    // Compares this member with another member by memberId for sorting collections.
    @Override
    public int compareTo(Member other) {
        if (other == null) {
            return 1;
        }
        if (this.memberId == null && other.memberId == null) {
            return 0;
        }
        if (this.memberId == null) {
            return -1;
        }
        if (other.memberId == null) {
            return 1;
        }
        return this.memberId.compareTo(other.memberId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return memberId != null && memberId.equals(member.memberId);
    }

    @Override
    public int hashCode() {
        return memberId != null ? memberId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Member[ID=%s, Name=%s, Tier=%s, Points=%d, Expiry=%s]",
                memberId, name, tier, loyaltyPoint, pointExpiryDate);
    }
}