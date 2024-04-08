package models;

/**
 * @author Shubham Punekar
 * Class to deserialise JWT Token
 */
public class JWTPayload {
    public Data data;
    public int nbf;
    public int iat;

    /**
     * @author Shubham Punekar
     * Class to store session
     */
    public class Data {
        public String session;
    }
}
