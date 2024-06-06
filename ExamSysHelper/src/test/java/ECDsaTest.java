import org.zoom.Utils.ECDsaUtils;
import org.zoom.Utils.FileToByteUtils;
import org.zoom.Utils.HexUtils;

import java.io.File;
import java.security.KeyPair;
import java.util.Map;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/5/19 13:55
 **/

public class ECDsaTest {
    private static String str = "hello world!";
    private static String filePath = "C:\\Users\\ZooMEISTER\\Downloads\\CET6_202112_330362212204612_1.pdf";

    public static void main(String[] args) {
        try{
            KeyPair keyPair = ECDsaUtils.generateKeyPair_ECDSA();
            byte[] privateKey = keyPair.getPrivate().getEncoded();
            byte[] publicKey = (keyPair.getPublic().getEncoded());
            System.out.println("PrivateKey: " + HexUtils.bytesToHexString(privateKey));
            System.out.println("PublicKey: " + HexUtils.bytesToHexString(publicKey));

            byte[] sign = ECDsaUtils.getSign_ECDSA(privateKey, str.getBytes());
            System.out.println("Sign: " + HexUtils.bytesToHexString(sign));

            boolean result = ECDsaUtils.verifySign(publicKey, sign, str.getBytes());
            System.out.println(result);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("");

        try{
            byte[] fileByte = FileToByteUtils.readByByteArrayOutputStream(new File(filePath));

            KeyPair keyPair = ECDsaUtils.generateKeyPair_ECDSA();
            byte[] privateKey = keyPair.getPrivate().getEncoded();
            byte[] publicKey = keyPair.getPublic().getEncoded();
            System.out.println("PrivateKey: " + HexUtils.bytesToHexString(privateKey));
            System.out.println("PublicKey: " + HexUtils.bytesToHexString(publicKey));

            byte[] sign = ECDsaUtils.getSign_ECDSA(privateKey, fileByte);
            System.out.println("Sign: " + HexUtils.bytesToHexString(sign));

            boolean result = ECDsaUtils.verifySign(publicKey, sign, fileByte);
            System.out.println(result);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
