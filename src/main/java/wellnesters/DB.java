package wellnesters;

import java.util.List;

public class DB {
    private Stats stats;
    private List<CompletionTime> completionTimes;

    // Constructor
    public DB() {
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

        // Getter and Setter
        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
