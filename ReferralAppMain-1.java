import java.time.LocalDate;
          import java.time.YearMonth;
          import java.util.*;

           class User {
               private int userId;


     private String name, email, referralCode;
     private int points;
     private LocalDate createdAt;

     public User(int userId, String name, String email) {
       this.userId = userId;
       this.name = name;
       this.email = email;
       this.points = 0;
       this.referralCode = "";
       this.createdAt = LocalDate.now();
     }

     public int getUserId() { return userId; }
     public String getName() { return name; }
     public String getEmail() { return email; }
     public String getReferralCode() { return referralCode; }
     public int getPoints() { return points; }
     public LocalDate getCreatedAt() { return createdAt; }

     public void setName(String name) { this.name = name; }
     public void setEmail(String email) { this.email = email; }
     public void setReferralCode(String code) { referralCode = code; }

     public void addPoints(int p) {
       if (p > 0) points += p;
     }

     public boolean deductPoints(int p) {
       if (p <= 0 || points < p) return false;
       points -= p;
       return true;
     }

     public void displayUser() {
       System.out.println(userId + " | " + name + " | " + email
             + " | Code: " + referralCode + " | Points: " + points);
     }
 }

 class Referral {
    private int refId;
    private User inviter;
    private String inviteeEmail, status;
    private int bonus;
    private LocalDate createdAt;

     public Referral(int id, User inviter, String email, int bonus) {
       this.refId = id;
       this.inviter = inviter;
       this.inviteeEmail = email;
       this.bonus = bonus;


         this.status = "PENDING";
         this.createdAt = LocalDate.now();
     }

     public int getRefId() { return refId; }
     public User getInviter() { return inviter; }
     public String getInviteeEmail() { return inviteeEmail; }
     public String getStatus() { return status; }
     public int getBonus() { return bonus; }
     public LocalDate getCreatedAt() { return createdAt; }

     public void setStatus(String status) { this.status = status; }

     public boolean isPending() {
       return status.equals("PENDING");
     }

     public void displayReferral() {
       System.out.println(refId + " | " + inviter.getName()
             + " | " + inviteeEmail + " | " + status
             + " | Bonus: " + bonus);
     }
 }

 class Reward {
    private int rewardId;
    private String name, category;
    private int pointsRequired, inventory;
    private boolean active;

     public Reward(int id, String name, int points, int inventory,
              String category) {
       this.rewardId = id;
       this.name = name;
       this.pointsRequired = points;
       this.inventory = inventory;
       this.category = category;
       this.active = true;
     }

     public int getRewardId() { return rewardId; }
     public String getName() { return name; }
     public int getPointsRequired() { return pointsRequired; }
     public int getInventory() { return inventory; }
     public String getCategory() { return category; }
     public boolean isActive() { return active; }

     public void setName(String name) { this.name = name; }

     public void setPointsRequired(int p) {
       if (p > 0) pointsRequired = p;
     }


     public void setActive(boolean active) {
       this.active = active;
     }

     public boolean isAvailable() {
       return active && inventory > 0;
     }

     public boolean consumeInventory() {
       if (!isAvailable()) return false;
       inventory--;
       return true;
     }

     public void restoreInventory() {
       inventory++;
     }

     public void deliver(User user) {
       System.out.println("Reward delivered to " + user.getName()
             + ": " + name);
     }

     public void displayReward() {
       System.out.println(rewardId + " | " + name
             + " | Points: " + pointsRequired
             + " | Stock: " + inventory);
     }
 }

 class VoucherReward extends Reward {
    private String voucherCode, expiryDate;
    private int discountPercent;
    private boolean onlineOnly;

     public VoucherReward(int id, String name, int points, int inventory,
                  String code, int discount, String expiry,
                  boolean onlineOnly) {
       super(id, name, points, inventory, "VOUCHER");
       this.voucherCode = code;
       this.discountPercent = discount;
       this.expiryDate = expiry;
       this.onlineOnly = onlineOnly;
     }

     public String getVoucherCode() { return voucherCode; }
     public int getDiscountPercent() { return discountPercent; }
     public String getExpiryDate() { return expiryDate; }
     public boolean isOnlineOnly() { return onlineOnly; }

     @Override


     public boolean consumeInventory() {
       return super.consumeInventory();
     }

     @Override
     public void deliver(User user) {
       System.out.println("Voucher sent to " + user.getEmail()
             + " | Code: " + voucherCode
             + " | Discount: " + discountPercent + "%"
             + " | Expiry: " + expiryDate);
     }
 }

 class GiftReward extends Reward {
    private String deliveryAddress, packaging;
    private double weightKg;
    private boolean fragile;

     public GiftReward(int id, String name, int points, int inventory,
                 String address, double weight, String packaging,
                 boolean fragile) {
       super(id, name, points, inventory, "GIFT");
       this.deliveryAddress = address;
       this.weightKg = weight;
       this.packaging = packaging;
       this.fragile = fragile;
     }

     public String getDeliveryAddress() { return deliveryAddress; }
     public String getPackaging() { return packaging; }
     public double getWeightKg() { return weightKg; }
     public boolean isFragile() { return fragile; }

     @Override
     public boolean consumeInventory() {
       return super.consumeInventory();
     }

     @Override
     public void deliver(User user) {
       System.out.println("Gift shipped to " + user.getName()
             + " | Address: " + deliveryAddress
             + " | Packaging: " + packaging
             + " | Weight: " + weightKg + " kg");
     }
 }

 class ReferralService {
    private Map<Integer, User> users = new LinkedHashMap<>();
    private Map<String, User> usersByEmail = new HashMap<>();
    private Map<String, User> usersByCode = new HashMap<>();
    private List<Referral> referrals = new ArrayList<>();


  private Map<Integer, Reward> rewards = new LinkedHashMap<>();
  private int nextReferralId;
  private int pointsPerReferral;
  private String serviceName;

  public ReferralService(String name, int bonus) {
    serviceName = name;
    pointsPerReferral = bonus;
    nextReferralId = 1;
  }

  public String getServiceName() { return serviceName; }
  public int getPointsPerReferral() { return pointsPerReferral; }
  public int getUserCount() { return users.size(); }
  public int getReferralCount() { return referrals.size(); }
  public int getRewardCount() { return rewards.size(); }

  public void setPointsPerReferral(int p) {
    if (p > 0) pointsPerReferral = p;
  }

  public String generateCode(User user) {
    String base = user.getName().replaceAll("[^A-Za-z0-9]", "")
          .toUpperCase();

      if (base.isEmpty()) base = "USER";

      String code;
      do {
         code = base + "-" + UUID.randomUUID()
              .toString().substring(0, 6).toUpperCase();
      } while (usersByCode.containsKey(code));

      return code;
  }

  public void registerUser(User user) {
    String email = user.getEmail().toLowerCase();

      if (users.containsKey(user.getUserId())
            || usersByEmail.containsKey(email)) {
         System.out.println("User ID or email already registered.");
         return;
      }

      if (user.getReferralCode().isEmpty()) {
         user.setReferralCode(generateCode(user));
      }

      if (usersByCode.containsKey(user.getReferralCode())) {
          System.out.println("Referral code already exists.");
          return;


      }

      users.put(user.getUserId(), user);
      usersByEmail.put(email, user);
      usersByCode.put(user.getReferralCode(), user);

      System.out.println("Registered: " + user.getName()
           + " | Code: " + user.getReferralCode());
  }

  public Referral trackSignup(String code, String email) {
    User inviter = usersByCode.get(code);

      if (inviter == null) {
         System.out.println("Invalid referral code.");
         return null;
      }

      if (usersByEmail.containsKey(email.toLowerCase())
            || inviter.getEmail().equalsIgnoreCase(email)) {
         System.out.println("Duplicate signup or self-referral rejected.");
         return null;
      }

      for (Referral r : referrals) {
        if (r.getInviteeEmail().equalsIgnoreCase(email)) {
            System.out.println("Email already referred.");
            return null;
        }
      }

      Referral r = new Referral(nextReferralId++, inviter,
           email, pointsPerReferral);
      referrals.add(r);

      System.out.println("Signup tracked. Referral ID: " + r.getRefId());
      return r;
  }

  public void awardPoints(int refId) {
    for (Referral r : referrals) {
       if (r.getRefId() == refId) {
           if (!r.isPending()) {
              System.out.println("Referral already processed.");
              return;
           }

           r.setStatus("COMPLETED");
           r.getInviter().addPoints(r.getBonus());

           System.out.println(r.getBonus() + " points awarded to "
                + r.getInviter().getName());


              return;
          }
      }

      System.out.println("Referral not found.");
  }

  public void addReward(Reward reward) {
    if (rewards.containsKey(reward.getRewardId())) {
       System.out.println("Reward ID already exists.");
       return;
    }
    rewards.put(reward.getRewardId(), reward);
  }

  public boolean redeem(User user, int rewardId) {
    return redeemInternal(user, rewards.get(rewardId), null);
  }

  public boolean redeem(User user, int rewardId, String promo) {
    return redeemInternal(user, rewards.get(rewardId), promo);
  }

  public boolean redeem(User user, String rewardName) {
    return redeemInternal(user, findRewardByName(rewardName), null);
  }

  public boolean redeem(User user, String rewardName, String promo) {
    return redeemInternal(user, findRewardByName(rewardName), promo);
  }

  private Reward findRewardByName(String name) {
     for (Reward r : rewards.values()) {
        if (r.getName().equalsIgnoreCase(name)) return r;
     }
     return null;
  }

  private boolean redeemInternal(User user, Reward reward, String promo) {
     if (user == null || reward == null) {
        System.out.println("User or reward not found.");
        return false;
     }

      if (!reward.isAvailable()) {
          System.out.println("Reward unavailable or out of stock.");
          return false;
      }

      int cost = reward.getPointsRequired();

      if ("SAVE10".equalsIgnoreCase(promo)) {


          cost = (int) Math.ceil(cost * 0.90);
      }

      if (user.getPoints() < cost) {
          System.out.println("Insufficient points. Required: " + cost);
          return false;
      }

      if (!reward.consumeInventory()) return false;

      if (!user.deductPoints(cost)) {
         reward.restoreInventory();
         return false;
      }

      System.out.println("Redeemed " + reward.getName()
           + " for " + cost + " points.");

      reward.deliver(user);
      return true;
  }

  public void displayAllUsers() {
    System.out.println("\n--- USERS ---");
    for (User u : users.values()) u.displayUser();
  }

  public void displayAllReferrals() {
    System.out.println("\n--- REFERRALS ---");
    for (Referral r : referrals) r.displayReferral();
  }

  public void displayAllRewards() {
    System.out.println("\n--- REWARDS ---");
    for (Reward r : rewards.values()) r.displayReward();
  }

  public void displayLeaderboard() {
    System.out.println("\n--- LEADERBOARD ---");

      List<User> list = new ArrayList<>(users.values());
      list.sort((a, b) -> Integer.compare(
            b.getPoints(), a.getPoints()));

      int rank = 1;
      for (User u : list) {
         System.out.println(rank++ + ". " + u.getName()
              + " - " + u.getPoints() + " points");
      }
  }

  public void displayMonthlySummary(YearMonth month) {


         int total = 0, completed = 0, pending = 0, awarded = 0;

         for (Referral r : referrals) {
           if (YearMonth.from(r.getCreatedAt()).equals(month)) {
               total++;

                 if (r.getStatus().equals("COMPLETED")) {
                     completed++;
                     awarded += r.getBonus();
                 } else if (r.isPending()) {
                     pending++;
                 }
             }
         }

         System.out.println("\n--- MONTHLY SUMMARY: " + month + " ---");
         System.out.println("Total referrals: " + total);
         System.out.println("Completed: " + completed);
         System.out.println("Pending: " + pending);
         System.out.println("Points awarded: " + awarded);
     }

     public void showServiceInfo() {
       System.out.println("\nService: " + serviceName);
       System.out.println("Users: " + getUserCount()
             + " | Referrals: " + getReferralCount()
             + " | Rewards: " + getRewardCount());
     }
 }

 public class ReferralAppMain {
   public static void main(String[] args) {
      ReferralService service =
           new ReferralService("Referral & Rewards System", 100);

         User u1 = new User(1, "Arun", "arun@example.com");
         User u2 = new User(2, "Priya", "priya@example.com");
         User u3 = new User(3, "Kavin", "kavin@example.com");

         service.registerUser(u1);
         service.registerUser(u2);
         service.registerUser(u3);

         Referral r1 = service.trackSignup(
             u1.getReferralCode(), "newuser1@example.com");
         Referral r2 = service.trackSignup(
             u2.getReferralCode(), "newuser2@example.com");
         Referral r3 = service.trackSignup(
             u1.getReferralCode(), "newuser3@example.com");

         if (r1 != null) service.awardPoints(r1.getRefId());
         if (r2 != null) service.awardPoints(r2.getRefId());


         if (r3 != null) service.awardPoints(r3.getRefId());

         Reward voucher = new VoucherReward(
            101, "Shopping Voucher", 80, 5,
            "SHOP20", 20, "2027-12-31", true);

         Reward gift = new GiftReward(
            102, "Wireless Headphones", 150, 3,
            "Chennai", 0.5, "Gift Box", false);

         service.addReward(voucher);
         service.addReward(gift);

         service.displayAllUsers();
         service.displayAllRewards();

         service.redeem(u1, 101);
         service.redeem(u2, "Shopping Voucher", "SAVE10");
         service.redeem(u1, "Wireless Headphones");

         service.displayAllUsers();
         service.displayAllReferrals();
         service.displayAllRewards();
         service.displayLeaderboard();
         service.displayMonthlySummary(YearMonth.now());
         service.showServiceInfo();
     }
 }
