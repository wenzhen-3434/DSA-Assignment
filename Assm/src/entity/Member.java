package entity;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;

public class Member {
    private String memberId;    //8 digit
    private String icPassportNo;
    private String name;
    private String email;
    private String phoneNumber;
    private String tier;     //Silver,gold,platinum,diamond
    private int loyaltyPoint;
    private LocalDate joinDate;
    private LocalDate pointExpiryDate;
    private boolean notificationEnable;

    //loyalty tier threshold
    public static final int SILVER_THRESHOLD = 0;
    public static final int GOLD_THRESHOLD = 5000;
    public static final int PLATINUM_THRESHOLD = 15000;
    public static final int DIAMOND_THRESHOLD = 30000;

    //Points expiration period(month)
    public static final int POINT_EXPITY_MONTHS = 12;

    public Member(String memberId, String name, String email, String phoneNumber, String icPassportNo, String tier, int points, LocalDate joinDate) {
        this.memberId = memberId;
        this.name = name;
        this.icPassportNo = icPassportNo;
        this.email = email;
        this.tier = tier;
        this.loyaltyPoint = points;
        this.joinDate = joinDate;
        this.pointExpiryDate = joinDate.plusMonths(POINT_EXPITY_MONTHS);
        this.notificationEnable = true;
    }

    //add points and check for tier upgrade
    public boolean addPoints(int points){
        if(points <= 0){
            return false;
        }

        this.loyaltyPoint += points;
        updatePointsExpiry();
        checkUpdateTier();
        return true;
    }

    //redeem points
    public boolean redeemPoints(int points){
        if(points <= 0 || points > this.loyaltyPoint){
            return false;
        }

        this.loyaltyPoint -= points;
        return true;
    }
}
