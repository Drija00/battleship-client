package main.java.shared.model;

import java.io.Serializable;

    public class PlayerMsg  implements Serializable {
        private int rank;
        private String username;
        private int wins;

        public PlayerMsg() {
        }

        public PlayerMsg(int rank, String username, int wins) {
            this.rank = rank;
            this.username = username;
            this.wins = wins;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getWins() {
            return wins;
        }

        public void setWins(int wins) {
            this.wins = wins;
        }

        @Override
        public String toString() {
            return "PlayerMsg{" +
                    "rank=" + rank +
                    ", username='" + username + '\'' +
                    ", wins=" + wins +
                    '}';
        }

}
