package Commons;

import java.io.Serializable;

public class Heartbeat implements Serializable
{

    private final int port;
    private String name;

    public Heartbeat(int port, String name)
    {
        this.port = port;
        this.name = name;
    }

    public int getPort()
    {
        return port;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
