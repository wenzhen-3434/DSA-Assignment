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
        guest.setMembership("Silver");
    }

    //-- points management --
    //add points
    public void addPoints(int points) {
        this.points += points;
        this.lifetimePoints += points;
        this.lastActivityDate = LocalDate.now();
        updateTier();
    }
    
    //redeem points
    public boolean redeemPoints(int points) {
        if(this.points >= points){
            this.points -= points;
            this.lastActivityDate = LocalDate.now();
            return true;
        }
        return false;
    }

    //tier progression
    private void updateTier() {
       // String oldTier = this.tier;

        //tier thresholds based on lifetime points
        if(lifetimePoints >= 10000) {
            this.tier = "Diamond";
        }else if(lifetimePoints >= 5000){
            this.tier = "Platinum";
        }else if(lifetimePoints >= 2000){
            this.tier = "Gold";
        }else{
            this.tier = "Silver";
        }

        if(guest != null){
            guest.setMembership(this.tier);
        }
    }

    public boolean upgradeTier() {
        String oldTier = this.tier;
        int currentIndex = getTierIndex(this.tier);
        int maxIndex = getTierIndex("Diamond");
        
        if (currentIndex < maxIndex) {
            String newTier = getTierByIndex(currentIndex + 1);
            int requiredPoints = getTierThreshold(newTier);
            if (lifetimePoints >= requiredPoints) {
                this.tier = newTier;
                if (guest != null) {
                    guest.setMembership(newTier);
                }
                return true;
            }
        }
        return false;
    }

    private int getTierIndex(String tier) {
        String[] tiers = {"Silver", "Gold", "Platinum", "Diamond"};
        for (int i = 0; i < tiers.length; i++) {
            if (tiers[i].equals(tier)) {
                return i;
            }
        }
        return 0;
    }

     private String getTierByIndex(int index) {
        String[] tiers = {"Silver", "Gold", "Platinum", "Diamond"};
        if (index >= 0 && index < tiers.length) {
            return tiers[index];
        }
        return tiers[0];
    }

    private int getTierThreshold(String tier) {
        switch (tier) {
            case "Gold": return 2000;
            case "Platinum": return 5000;
            case "Diamond": return 10000;
            default: return 0;
        }
    }

    //Personalized Promotions
    public List<String> getPersonalizedPromotions() {
        List<String> promotions = new ArrayList<>();

        //tier-based promotions
        switch(this.tier){
        case "Diamond":
                promotions.add("💎 DIAMOND EXCLUSIVE: 30% off room upgrades");
                promotions.add("💎 DIAMOND EXCLUSIVE: Free breakfast buffet");
                promotions.add("💎 DIAMOND EXCLUSIVE: Late check-out until 6pm");
                break;

        case "Platinum":
                promotions.add("⭐ PLATINUM EXCLUSIVE: 20% off room upgrades");
                promotions.add("⭐ PLATINUM EXCLUSIVE: Free welcome drink");
                promotions.add("⭐ PLATINUM EXCLUSIVE: Early check-in from 10am");
                break;

        case "Gold":
                promotions.add("🏅 GOLD EXCLUSIVE: 15% off room upgrades");
                promotions.add("🏅 GOLD EXCLUSIVE: Free room upgrade (subject to availability)");
                break;
        
        default:
            promotions.add("🥈 SILVER EXCLUSIVE: 10% off room upgrades");
            promotions.add("🥈 SILVER EXCLUSIVE: Welcome amenity on arrival");
        }

        // Points-based promotions
        if (points >= 5000) {
            promotions.add("🎯 Redeem 5000 points for a FREE night stay!");
        } else if (points >= 3000) {
            promotions.add("🎯 Redeem 3000 points for a complimentary dinner!");
        } else if (points >= 1000) {
            promotions.add("🎯 Redeem 1000 points for a free room upgrade!");
        }

         // Stay-based promotions
        if (stayCount >= 10) {
            promotions.add("🌟 Loyalty Reward: One free night after 10 stays!");
        } else if (stayCount >= 5) {
            promotions.add("🌟 Stay 5 more nights to earn a free night reward!");
        }

        // Points expiry promotion
        if (pointsExpiryDate.minusMonths(1).isBefore(LocalDate.now())) {
            promotions.add("⏰ Use your points before they expire on " + pointsExpiryDate);
        }

        return promotions;
    }

    // Redemption Processing
    public boolean processRedemption(String item, int pointsRequired) {
        if (redeemPoints(pointsRequired)) {
            String redemptionRecord = String.format("%s (%d points) on %s - Status: COMPLETED", 
                item, pointsRequired, LocalDate.now());
            redemptionHistory.add(redemptionRecord);
            return true;
        }
        return false;
    }

    public boolean requestRedemption(String item, int pointsRequired) {
        if (points >= pointsRequired) {
            String requestRecord = String.format("%s (%d points) on %s - Status: PENDING APPROVAL", 
                item, pointsRequired, LocalDate.now());
            redemptionHistory.add(requestRecord);
            return true;
        }
        return false;
    }

    public void approveRedemptionRequest(String requestId) {
        for (int i = 0; i < redemptionHistory.size(); i++) {
            String record = redemptionHistory.get(i);
            if (record.contains(requestId) && record.contains("PENDING APPROVAL")) {
                redemptionHistory.set(i, record.replace("PENDING APPROVAL", "APPROVED"));
                break;
            }
        }
    }

    public void rejectRedemptionRequest(String requestId) {
        for (int i = 0; i < redemptionHistory.size(); i++) {
            String record = redemptionHistory.get(i);
            if (record.contains(requestId) && record.contains("PENDING APPROVAL")) {
                redemptionHistory.set(i, record.replace("PENDING APPROVAL", "REJECTED"));
                String[] parts = record.split("\\(");
                if (parts.length > 1) {
                    String pointsStr = parts[1].replace(" points)", "").trim();
                    try {
                        int refundPoints = Integer.parseInt(pointsStr);
                        this.points += refundPoints;
                    } catch (NumberFormatException e) {
                        // ignore parsing error
                    }
                }
                break;
            }
        }
    }

    //getter setter
    public String getMemberId(){
        return memberId;
    }
    public void setMembetId(String memberId){
        this.memberId = memberId;
    }

    public Guest getGuest(){
        return guest;
    }
    public void setGuest(Guest guest){
        this.guest = guest;
    }

    public String getTier(){
        return tier;
    }
    public void setTier(String tier){
        this.tier = tier;
        if(guest != null){
            guest.setMembership(tier);
        }
    }

    public int getPoints(){ 
        return points; 
    }
    public void setPoints(int points){ 
        this.points = points; 
    }
    
    public int getLifetimePoints(){
        return lifetimePoints; 
    }
    public void setLifetimePoints(int lifetimePoints){
        this.lifetimePoints = lifetimePoints; 
    }

    public LocalDate getJoinDate(){ 
        return joinDate; 
    }
    public void setJoinDate(LocalDate joinDate){ 
        this.joinDate = joinDate; 
    }

    public LocalDate getPointsExpiryDate(){ 
        return pointsExpiryDate; 
    }
    public void setPointsExpiryDate(LocalDate pointsExpiryDate){ 
        this.pointsExpiryDate = pointsExpiryDate; 
    }

    public LocalDate getLastActivityDate(){ 
        return lastActivityDate; 
    }
    public void setLastActivityDate(LocalDate lastActivityDate){ 
        this.lastActivityDate = lastActivityDate; 
    }

    public List<String> getRedemptionHistory(){ 
        return redemptionHistory; 
    }
    public void setRedemptionHistory(List<String> redemptionHistory){ 
        this.redemptionHistory = redemptionHistory; 
    }
    
    public List<String> getPromotionHistory(){ 
        return promotionHistory; 
    }
    public void setPromotionHistory(List<String> promotionHistory){ 
        this.promotionHistory = promotionHistory; 
    }
    
    public int getStayCount(){ 
        return stayCount; 
    }
    public void setStayCount(int stayCount){ 
        this.stayCount = stayCount; 
    }
    
    public double getTotalSpent(){ 
        return totalSpent; 
    }
    public void setTotalSpent(double totalSpent){ 
        this.totalSpent = totalSpent; 
    }
    
    public String getPreferredRoomType(){ 
        return preferredRoomType; 
    }
    public void setPreferredRoomType(String preferredRoomType){ 
        this.preferredRoomType = preferredRoomType; 
    }

    public List<String> getPreferences(){ 
        return preferences; 
    }
    public void setPreferences(List<String> preferences){
        this.preferences = preferences; 
    }

    public void addStay(double amountSpent) {
        this.stayCount++;
        this.totalSpent += amountSpent;
        int pointsEarned = (int)(amountSpent * 10);
        addPoints(pointsEarned);
        this.lastActivityDate = LocalDate.now();
    }

    public String getNextTier() {
        int currentIndex = getTierIndex(this.tier);
        if (currentIndex < 4) {
            String nextTier = getTierByIndex(currentIndex + 1);
            int requiredPoints = getTierThreshold(nextTier);
            return nextTier + " (Need " + (requiredPoints - lifetimePoints) + " more lifetime points)";
        }
        return "Maximum tier reached (Diamond)";
    }

    @Override
    public String toString() {
        return String.format("Member ID: %s, Guest: %s, Tier: %s, Points: %d (%d lifetime), Stays: %d, Spent: $%.2f",
                memberId, guest.getGuestName(), tier, points, lifetimePoints, stayCount, totalSpent);
    }
}