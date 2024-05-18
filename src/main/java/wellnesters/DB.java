package wellnesters;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private Stats stats;
    private List<CompletionTime> completionTimes; //added field

    // Constructor
    public DB() {
        this.stats = new Stats();
        this.completionTimes = new ArrayList<>();
    }

    // Getters and Setters
    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public List<CompletionTime> getCompletionTimes() {
        return completionTimes;
    }

    public void setCompletionTimes(List<CompletionTime> completionTimes) {
        this.completionTimes = completionTimes;
    }

    //added method
    // Method to update streaks
    public void updateStreaks() {
        int currentStreak = 0;
        int longestStreak = 0;
        int totalPerfectDays = 0;
        int totalDays = 0;

        if (completionTimes.size() > 0) {
            LocalDateTime previousCompletionTime = LocalDateTime.ofEpochSecond(
                completionTimes.get(0).getTimestamp(), 0, ZoneOffset.UTC
            ).truncatedTo(ChronoUnit.DAYS);

            currentStreak = 1;
            longestStreak = 1;
            totalPerfectDays = 1;
            totalDays = 1;

            for (int i = 1; i < completionTimes.size(); i++) {
                LocalDateTime currentCompletionTime = LocalDateTime.ofEpochSecond(
                    completionTimes.get(i).getTimestamp(), 0, ZoneOffset.UTC
                ).truncatedTo(ChronoUnit.DAYS);

                long daysBetween = ChronoUnit.DAYS.between(previousCompletionTime, currentCompletionTime);

                if (daysBetween == 1) {
                    currentStreak++;
                    totalPerfectDays++;
                    if (currentStreak > longestStreak) {
                        longestStreak = currentStreak;
                    }
                } else if (daysBetween > 1) {
                    currentStreak = 1;
                }

                totalDays++;
                previousCompletionTime = currentCompletionTime;
            }
        }

        stats.setCurrentStreak(currentStreak);
        stats.setLongestStreak(longestStreak);
        stats.setTotalPerfectDays(totalPerfectDays);
        stats.setTotalDays(totalDays);
        stats.setTotalTimesCompleted(completionTimes.size());
    }

    // Inner classes
    public static class Stats {
        private int currentStreak;
        private int longestStreak;
        private int totalPerfectDays;
        private int totalTimesCompleted;
        private int totalDays;

        // Constructor
        public Stats() {
        }

        // Getters and Setters
        public int getCurrentStreak() {
            return currentStreak;
        }

        public void setCurrentStreak(int currentStreak) {
            this.currentStreak = currentStreak;
        }

        public int getLongestStreak() {
            return longestStreak;
        }

        public void setLongestStreak(int longestStreak) {
            this.longestStreak = longestStreak;
        }

        public int getTotalPerfectDays() {
            return totalPerfectDays;
        }

        public void setTotalPerfectDays(int totalPerfectDays) {
            this.totalPerfectDays = totalPerfectDays;
        }

        public int getTotalTimesCompleted() {
            return totalTimesCompleted;
        }

        public void setTotalTimesCompleted(int totalTimesCompleted) {
            this.totalTimesCompleted = totalTimesCompleted;
        }

        public int getTotalDays() {
            return totalDays;
        }

        public void setTotalDays(int totalDays) {
            this.totalDays = totalDays;
        }
    }

    public static class CompletionTime {
        private long timestamp; // Assuming timestamp is in milliseconds

        // Constructor
        public CompletionTime() {
        }
        //added constructor
        public CompletionTime(long timestamp) {
            this.timestamp = timestamp;
        }

        // Getter and Setter
        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
