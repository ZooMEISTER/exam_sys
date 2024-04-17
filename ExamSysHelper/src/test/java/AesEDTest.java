import org.zoom.Utils.AesUtils;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/4/16 15:03
 **/

public class AesEDTest {
    public static void main(String[] args) throws Exception {
        String sfp = "C:\\Users\\ZooMEISTER\\Downloads\\智能基座昇腾OpenLab高校开发者体验项目v2.0.pdf";
        String edfp = "C:\\Users\\ZooMEISTER\\Downloads\\智能基座昇腾OpenLab高校开发者体验项目v2.0_e.pdf";
        String ddfp = "C:\\Users\\ZooMEISTER\\Downloads\\9pgUpbG5FIZH3dhWrHePFCCk3qXm9O1R智能基座昇腾OpenLab高校开发者体验项目v2.0_d.pdf";
        String key = "RiyN05VVOf4rFGKz";
        AesUtils.aesEncryptFile(sfp, edfp, key);
        AesUtils.aesDecryptFile(edfp, ddfp, key);
    }
}
