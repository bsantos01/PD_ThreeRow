package RmiServer;

public class Player {

    private String name;
    private String ipAndPort;
    private String state; //with or without pair

    public Player(String name, String ipAndPort, String state) {
        this.name = name;
        this.ipAndPort = ipAndPort;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public String getIpAndPort() {
        return ipAndPort;
    }

    public String getState() {
        return state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIpAndPort(String ipAndPort) {
        this.ipAndPort = ipAndPort;
    }

    public void setState(String state) {
        this.state = state;
    }

}
