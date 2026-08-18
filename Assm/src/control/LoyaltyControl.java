package control;

import adt.LinkedList;
import adt.LinkedListInterface;
import entity.Guest;
import entity.Member;
import entity.Notification;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoyaltyControl {
    private LinkedListInterface<Member> members;
    private LinkedListInterface<Notification> notifications;

    public LoyaltyControl() {
        this.members = new LinkedList<>();
        this.notifications = new LinkedList<>();
    }

    //member registration

    public Member registerMember(String guestName, String gender, String phone, String email, String icPassport, String preferredRoomType) {
        if (guestName == null || guestName.isEmpty()) {
            System.out.println("❌ Name is required.");
            return null;
        }
        
        if (email == null || email.isEmpty()) {
            System.out.println("❌ Email is required.");
            return null;
        }

        if (findMemberByEmail(email) != null) {
            System.out.println("❌ A member with email " + email + " already exists.");
            return null;
        }

        String guestId = "G" + System.currentTimeMillis();
        Guest guest = new Guest(guestId, guestName, gender, phone, email, icPassport);
        
        Member member = Member.registerNewMember(guest);
        if (member == null) {
            System.out.println("❌ Failed to create member.");
            return null;
        }
        
        if (preferredRoomType != null && !preferredRoomType.isEmpty()) {
            member.setPreferredRoomType(preferredRoomType);
        }
        
        members.add(member);
        sendWelcomeNotification(member);

        System.out.println("✅ Member registered successfully!");
        System.out.println("   📋 Member ID: " + member.getMemberId());
        System.out.println("   👤 Guest: " + guest.getGuestName());
        System.out.println("   ⭐ Tier: " + member.getTier());
        System.out.println("   💰 Welcome Points: " + member.getPoints());
        
        return member;
    }

    public Member registerMember(Guest guest) {
        if (guest == null) {
            System.out.println("❌ Cannot register: Guest is null");
            return null;
        }

        if (findMemberByEmail(guest.getEmail()) != null) {
            System.out.println("❌ Guest is already a member: " + guest.getEmail());
            return null;
        }

        Member member = Member.registerNewMember(guest);
        if (member == null) {
            System.out.println("❌ Failed to create member.");
            return null;
        }
        
        members.add(member);
        sendWelcomeNotification(member);

        System.out.println("✅ Member registered successfully!");
        System.out.println("   📋 Member ID: " + member.getMemberId());
        System.out.println("   👤 Guest: " + guest.getGuestName());
        System.out.println("   ⭐ Tier: " + member.getTier());
        System.out.println("   💰 Welcome Points: " + member.getPoints());
        
        return member;
    }

    //search member

    public Member findMember(String memberId) {
        if (memberId == null) return null;
        
        for (int i = 1; i <= members.numberOfEntries(); i++) {
            Member member = members.getEntry(i);
            if (member != null && memberId.equals(member.getMemberId())) {
                return member;
            }
        }
        return null;
    }

    public Member findMemberByEmail(String email) {
        if (email == null) return null;
        
        for (int i = 1; i <= members.numberOfEntries(); i++) {
            Member member = members.getEntry(i);
            if (member != null && member.getGuest() != null) {
                if (email.equalsIgnoreCase(member.getGuest().getEmail())) {
                    return member;
                }
            }
        }
        return null;
    }

    public Member findMemberByName(String name) {
        if (name == null) return null;
        
        for (int i = 1; i <= members.numberOfEntries(); i++) {
            Member member = members.getEntry(i);
            if (member != null && member.getGuest() != null) {
                if (member.getGuest().getGuestName().equalsIgnoreCase(name)) {
                    return member;
                }
            }
        }
        return null;
    }

    //member manage

    public List<Member> getAllMembers() {
        List<Member> result = new ArrayList<>();
        for (int i = 1; i <= members.numberOfEntries(); i++) {
            Member member = members.getEntry(i);
            if (member != null && member.isActive()) {
                result.add(member);
            }
        }
        return result;
    }

    public List<Member> getMembersByTier(String tier) {
        List<Member> result = new ArrayList<>();
        for (int i = 1; i <= members.numberOfEntries(); i++) {
            Member member = members.getEntry(i);
            if (member != null && member.isActive() && member.getTier().equals(tier)) {
                result.add(member);
            }
        }
        return result;
    }

    public int getMemberCount() {
        return members.numberOfEntries();
    }

    //points manage

    public boolean addPoints(String memberId, int points) {
        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("❌ Member not found.");
            return false;
        }

        if (points <= 0) {
            System.out.println("❌ Points must be positive.");
            return false;
        }

        String oldTier = member.getTier();
        member.addPoints(points);
        String newTier = member.getTier();

        sendPointsNotification(member, points);

        if (!oldTier.equals(newTier)) {
            sendTierUpgradeNotification(member, oldTier, newTier);
        }

        System.out.println("✅ Added " + points + " points to " + member.getGuest().getGuestName());
        System.out.println("   Current points: " + member.getPoints() + " | Tier: " + member.getTier());
        return true;
    }

    public boolean addStayPoints(String memberId, double amount) {
        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("❌ Member not found.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("❌ Amount must be positive.");
            return false;
        }

        String oldTier = member.getTier();
        member.addStay(amount);
        String newTier = member.getTier();

        int pointsEarned = (int)(amount * 10);
        sendStayNotification(member, amount, pointsEarned);

        if (!oldTier.equals(newTier)) {
            sendTierUpgradeNotification(member, oldTier, newTier);
        }

        System.out.println("✅ Stay recorded for " + member.getGuest().getGuestName());
        System.out.println("   Points earned: " + pointsEarned + " | Total stays: " + member.getStayCount());
        return true;
    }

    //redemption

    public boolean redeemPoints(String memberId, int points, String redemptionItem) {
        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("❌ Member not found.");
            return false;
        }

        if (points <= 0) {
            System.out.println("❌ Points must be positive.");
            return false;
        }

        if (member.processRedemption(redemptionItem, points)) {
            sendRedemptionSuccessNotification(member, points, redemptionItem);
            System.out.println("✅ Points redeemed successfully!");
            System.out.println("   Remaining points: " + member.getPoints());
            return true;
        } else {
            sendRedemptionFailureNotification(member, points, redemptionItem);
            System.out.println("❌ Insufficient points. Available: " + member.getPoints());
            return false;
        }
    }

    public String requestRedemption(String memberId, String item, int pointsRequired) {
        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("❌ Member not found.");
            return null;
        }

        if (pointsRequired <= 0) {
            System.out.println("❌ Points must be positive.");
            return null;
        }

        if (member.requestRedemption(item, pointsRequired)) {
            String requestId = "RQ" + System.currentTimeMillis();
            sendRedemptionRequestNotification(member, item, pointsRequired, requestId);
            System.out.println("✅ Redemption request submitted!");
            System.out.println("   Request ID: " + requestId);
            return requestId;
        } else {
            System.out.println("❌ Insufficient points for redemption request.");
            return null;
        }
    }

    //tier

    public boolean upgradeMemberTier(String memberId) {
        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("❌ Member not found.");
            return false;
        }

        String oldTier = member.getTier();
        if (member.upgradeTier()) {
            String newTier = member.getTier();
            sendTierUpgradeNotification(member, oldTier, newTier);
            System.out.println("✅ Member upgraded from " + oldTier + " to " + newTier + "!");
            return true;
        } else {
            String nextTier = member.getNextTier();
            System.out.println("❌ Cannot upgrade. " + nextTier);
            return false;
        }
    }

    public String getTierUpgradeInfo(String memberId) {
        Member member = findMember(memberId);
        if (member != null) {
            return member.getNextTier();
        }
        return "Member not found.";
    }

    // notification

    private void sendWelcomeNotification(Member member) {
        String welcomeMessage = String.format(
            "Welcome to TARUMT Resorts Loyalty Program, %s!\n" +
            "You have been registered as a %s member with %d bonus points.\n" +
            "Earn points with every stay and enjoy exclusive benefits!\n\n" +
            "Quick Tips:\n" +
            "• Earn 10 points for every $1 spent\n" +
            "• Redeem points for room upgrades, dining, and more\n" +
            "• Progress through tiers: Silver → Gold → Platinum → Diamond → Elite",
            member.getGuest().getGuestName(), member.getTier(), member.getPoints()
        );
        
        Notification welcomeNotif = new Notification(
            member.getMemberId(),
            "🎊 Welcome to TARUMT Resorts!",
            welcomeMessage,
            "Welcome",
            LocalDate.now()
        );
        notifications.add(welcomeNotif);
    }

    private void sendPointsNotification(Member member, int points) {
        Notification pointsNotif = new Notification(
            member.getMemberId(),
            "✨ Points Added! ✨",
            "You have earned " + points + " points. Total: " + member.getPoints() + " points.",
            "Points",
            LocalDate.now()
        );
        notifications.add(pointsNotif);
    }

    private void sendStayNotification(Member member, double amount, int pointsEarned) {
        Notification stayNotif = new Notification(
            member.getMemberId(),
            "🏨 Stay Points Earned!",
            "You earned " + pointsEarned + " points for your recent stay of $" + amount + ".",
            "Points",
            LocalDate.now()
        );
        notifications.add(stayNotif);
    }

    private void sendTierUpgradeNotification(Member member, String oldTier, String newTier) {
        String message = String.format(
            "🎉 Congratulations %s! You have been upgraded from %s to %s tier! 🎉\n" +
            "New benefits:\n" +
            getTierBenefits(newTier),
            member.getGuest().getGuestName(), oldTier, newTier
        );
        
        Notification upgradeNotif = new Notification(
            member.getMemberId(),
            "🎉 TIER UPGRADE! 🎉",
            message,
            "Tier_Upgrade",
            LocalDate.now()
        );
        notifications.add(upgradeNotif);
        member.getPromotionHistory().add("Tier Upgrade: " + oldTier + " → " + newTier + " on " + LocalDate.now());
    }

    private void sendRedemptionSuccessNotification(Member member, int points, String item) {
        Notification redemptionNotif = new Notification(
            member.getMemberId(),
            "✅ Redemption Successful!",
            "You have redeemed " + points + " points for: " + item + ". Remaining points: " + member.getPoints(),
            "Redemption",
            LocalDate.now()
        );
        notifications.add(redemptionNotif);
    }

    private void sendRedemptionFailureNotification(Member member, int points, String item) {
        Notification insuffNotif = new Notification(
            member.getMemberId(),
            "❌ Insufficient Points",
            "You have " + member.getPoints() + " points but need " + points + " points for: " + item,
            "Redemption",
            LocalDate.now()
        );
        notifications.add(insuffNotif);
    }

    private void sendRedemptionRequestNotification(Member member, String item, int points, String requestId) {
        Notification requestNotif = new Notification(
            member.getMemberId(),
            "📝 Redemption Request Submitted",
            "Your request to redeem " + points + " points for '" + item + 
            "' has been submitted. Request ID: " + requestId,
            "Redemption_Request",
            LocalDate.now()
        );
        notifications.add(requestNotif);
    }

    private String getTierBenefits(String tier) {
        switch (tier) {
            case "Elite":
                return "  • 50% off room upgrades\n  • Free airport transfer\n  • Complimentary spa access\n  • Priority check-in/check-out";
            case "Diamond":
                return "  • 30% off room upgrades\n  • Free breakfast buffet\n  • Late check-out until 6pm";
            case "Platinum":
                return "  • 20% off room upgrades\n  • Free welcome drink\n  • Early check-in from 10am";
            case "Gold":
                return "  • 15% off room upgrades\n  • Free room upgrade (subject to availability)";
            default:
                return "  • 10% off room upgrades\n  • Welcome amenity on arrival";
        }
    }

    //expiry check

    public void checkExpiringPoints() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║        POINTS EXPIRY ALERTS         ║");
        System.out.println("╚══════════════════════════════════════╝");
        
        LocalDate now = LocalDate.now();
        boolean hasExpiring = false;
        
        for (int i = 1; i <= members.numberOfEntries(); i++) {
            Member member = members.getEntry(i);
            if (member != null && member.isActive()) {
                LocalDate expiryDate = member.getPointsExpiryDate();
                
                if (expiryDate.minusMonths(1).isBefore(now) && expiryDate.isAfter(now)) {
                    long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(now, expiryDate);
                    System.out.printf("⚠️ %-20s: %d points expire in %d days\n", 
                        member.getGuest().getGuestName(), member.getPoints(), daysUntilExpiry);
                    hasExpiring = true;
                    sendExpiryNotification(member, daysUntilExpiry);
                }
            }
        }
        
        if (!hasExpiring) {
            System.out.println("✅ No points expiring within the next month.");
        }
        System.out.println("════════════════════════════════════════\n");
    }

    private void sendExpiryNotification(Member member, long daysUntilExpiry) {
        String message = String.format(
            "⚠️ POINTS EXPIRY ALERT ⚠️\n" +
            "Your %d points will expire in %d days.\n\n" +
            "Use your points before they expire for:\n" +
            "• Room upgrades\n" +
            "• Dining experiences\n" +
            "• Spa treatments\n" +
            "• And more!",
            member.getPoints(), daysUntilExpiry
        );
        
        Notification expiryNotif = new Notification(
            member.getMemberId(),
            "⚠️ Points Expiring Soon!",
            message,
            "Expiry_Alert",
            LocalDate.now()
        );
        notifications.add(expiryNotif);
    }

    //promotions

    public List<String> getMemberPromotions(String memberId) {
        Member member = findMember(memberId);
        if (member != null) {
            return member.getPersonalizedPromotions();
        }
        return new ArrayList<>();
    }

    public void sendBulkPromotion(String promotion, String tierFilter) {
        int sentCount = 0;
        
        for (int i = 1; i <= members.numberOfEntries(); i++) {
            Member member = members.getEntry(i);
            if (member != null && member.isActive()) {
                if (tierFilter.equals("All") || member.getTier().equals(tierFilter)) {
                    Notification promoNotif = new Notification(
                        member.getMemberId(),
                        "🎁 Special Promotion For You!",
                        promotion,
                        "Promotion",
                        LocalDate.now()
                    );
                    notifications.add(promoNotif);
                    member.getPromotionHistory().add("Promotion sent: " + promotion + " on " + LocalDate.now());
                    sentCount++;
                }
            }
        }
        
        System.out.println("✅ Bulk promotion sent to " + sentCount + " members.");
    }

    //notification manage

    public List<Notification> getNotificationsForMember(String memberId) {
        List<Notification> memberNotifs = new ArrayList<>();
        for (int i = 1; i <= notifications.numberOfEntries(); i++) {
            Notification notif = notifications.getEntry(i);
            if (notif != null && notif.getMemberId().equals(memberId)) {
                memberNotifs.add(notif);
            }
        }
        return memberNotifs;
    }

    public List<Notification> getUnreadNotifications(String memberId) {
        List<Notification> unread = new ArrayList<>();
        for (int i = 1; i <= notifications.numberOfEntries(); i++) {
            Notification notif = notifications.getEntry(i);
            if (notif != null && notif.getMemberId().equals(memberId) && !notif.isRead()) {
                unread.add(notif);
            }
        }
        return unread;
    }

    public void markAllNotificationsAsRead(String memberId) {
        for (int i = 1; i <= notifications.numberOfEntries(); i++) {
            Notification notif = notifications.getEntry(i);
            if (notif != null && notif.getMemberId().equals(memberId)) {
                notif.setRead(true);
            }
        }
    }

    public int getUnreadNotificationCount(String memberId) {
        return getUnreadNotifications(memberId).size();
    }

    //reports

    public void generateMemberProfileReport(String memberId) {
        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("❌ Member not found.");
            return;
        }

        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    MEMBER PROFILE REPORT                     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        
        System.out.println("\n📋 PERSONAL INFORMATION:");
        System.out.println("   Member ID: " + member.getMemberId());
        System.out.println("   Guest Name: " + member.getGuest().getGuestName());
        System.out.println("   Email: " + member.getGuest().getEmail());
        System.out.println("   Contact: " + member.getGuest().getPhoneNumber());
        System.out.println("   Joined: " + member.getJoinDate());
        System.out.println("   Status: " + (member.isActive() ? "✅ Active" : "❌ Inactive"));
        
        System.out.println("\n🏆 MEMBERSHIP DETAILS:");
        System.out.println("   Current Tier: " + member.getTier());
        System.out.println("   Next Tier: " + member.getNextTier());
        System.out.println("   Points: " + member.getPoints() + " (Lifetime: " + member.getLifetimePoints() + ")");
        System.out.println("   Points Expiry: " + member.getPointsExpiryDate());
        System.out.println("   Last Activity: " + member.getLastActivityDate());
        
        System.out.println("\n📊 STAY STATISTICS:");
        System.out.println("   Total Stays: " + member.getStayCount());
        System.out.println("   Total Spent: $" + String.format("%.2f", member.getTotalSpent()));
        System.out.println("   Preferred Room: " + member.getPreferredRoomType());
        
        System.out.println("\n🎁 PERSONALIZED PROMOTIONS:");
        List<String> promotions = member.getPersonalizedPromotions();
        if (promotions.isEmpty()) {
            System.out.println("   No promotions available.");
        } else {
            for (String promo : promotions) {
                System.out.println("   • " + promo);
            }
        }
        
        System.out.println("\n📜 REDEMPTION HISTORY:");
        List<String> history = member.getRedemptionHistory();
        if (history.isEmpty()) {
            System.out.println("   No redemptions yet.");
        } else {
            for (String record : history) {
                System.out.println("   • " + record);
            }
        }
        
        System.out.println("\n📬 NOTIFICATIONS:");
        List<Notification> notifs = getNotificationsForMember(memberId);
        if (notifs.isEmpty()) {
            System.out.println("   No notifications.");
        } else {
            int unread = getUnreadNotificationCount(memberId);
            System.out.println("   Unread: " + unread + " | Total: " + notifs.size());
            for (Notification notif : notifs) {
                System.out.println("   " + notif);
            }
        }
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗\n");
    }

    public void generateLoyaltyReport() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    LOYALTY PROGRAM REPORT                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        
        int totalMembers = members.numberOfEntries();
        int activeMembers = getAllMembers().size();
        
        System.out.println("\n📊 OVERALL STATISTICS:");
        System.out.println("   Total Members: " + totalMembers);
        System.out.println("   Active Members: " + activeMembers);
        System.out.println("   Inactive Members: " + (totalMembers - activeMembers));
        
        int totalPoints = 0;
        int totalStays = 0;
        double totalSpent = 0.0;
        
        for (int i = 1; i <= members.numberOfEntries(); i++) {
            Member m = members.getEntry(i);
            if (m != null) {
                totalPoints += m.getPoints();
                totalStays += m.getStayCount();
                totalSpent += m.getTotalSpent();
            }
        }
        
        System.out.println("   Total Points: " + totalPoints);
        System.out.println("   Total Stays: " + totalStays);
        System.out.println("   Total Spent: $" + String.format("%.2f", totalSpent));
        
        System.out.println("\n🏆 MEMBERS BY TIER:");
        String[] tiers = {"Silver", "Gold", "Platinum", "Diamond", "Elite"};
        for (String tier : tiers) {
            List<Member> tierMembers = getMembersByTier(tier);
            int tierPoints = 0;
            for (Member m : tierMembers) {
                tierPoints += m.getPoints();
            }
            System.out.printf("   %-10s: %3d members (Total Points: %6d)\n", 
                tier, tierMembers.size(), tierPoints);
        }
        
        System.out.println("\n📬 RECENT NOTIFICATIONS (Last 10):");
        int count = 0;
        for (int i = notifications.numberOfEntries(); i >= 1 && count < 10; i--) {
            Notification notif = notifications.getEntry(i);
            if (notif != null) {
                System.out.println("   " + notif);
                count++;
            }
        }
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗\n");
    }

    public void displayMemberNotifications(String memberId) {
        List<Notification> memberNotifs = getNotificationsForMember(memberId);
        int unread = getUnreadNotificationCount(memberId);
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    MEMBER NOTIFICATIONS                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        
        if (memberNotifs.isEmpty()) {
            System.out.println("   No notifications for this member.");
        } else {
            System.out.println("   Unread: " + unread + " | Total: " + memberNotifs.size());
            System.out.println("   " + "-".repeat(50));
            for (Notification notif : memberNotifs) {
                System.out.println("   " + notif);
            }
        }
        System.out.println("╔═══════════════════════════════════════════════════════════════╗\n");
    }
}