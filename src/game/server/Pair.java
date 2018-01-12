package game.server;

public class Pair {

    private int id;
    private String user1;
    private String user2;
    private String status;
    private String winner;

    public Pair(int id, String user1, String user2, String status, String winner) {
        this.id = id;
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
        this.winner = winner;
    }

    public int getId() {
        return id;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

}
