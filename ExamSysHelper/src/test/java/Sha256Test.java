import org.zoom.Utils.Sha256Utils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/4/18 15:22
 **/

public class Sha256Test {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\ZooMEISTER\\Downloads\\智能基座昇腾OpenLab高校开发者体验项目v2.0.pdf";
        try {
            byte[] sha256 = Sha256Utils.calculateSHA256(filePath);
            String sha256Hex = Sha256Utils.bytesToHex(sha256);
            System.out.println("SHA256: " + sha256Hex);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
