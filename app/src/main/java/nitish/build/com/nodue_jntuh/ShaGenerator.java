package nitish.build.com.nodue_jntuh;

import java.security.MessageDigest;

/**
 * Created by NitishPc on 10-07-2018.
 */

public class ShaGenerator {
    public static String getSha256(String value) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            return bytesToHex(md.digest());
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }


    /*
    public static void main(String args[]){
        String str = getSha256("114422");
        System.out.println(str);
    }
    */
}
