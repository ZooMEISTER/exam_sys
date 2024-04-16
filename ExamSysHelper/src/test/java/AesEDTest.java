import org.zoom.Utils.AesUtils;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/4/16 15:03
 **/

public class AesEDTest {
    public static void main(String[] args) throws Exception {
        String sfp = "D:/myPrograming/WEB/Exam_System/res/test/test_pdf.pdf";
        String edfp = "D:/myPrograming/WEB/Exam_System/res/test/test_pdf_e.pdf";
        String ddfp = "D:/myPrograming/WEB/Exam_System/res/test/test_pdf_d.pdf";
        String key = "jsudjfhaksdhcxjs";
        AesUtils.aesEncryptFile(sfp, edfp, key);
        AesUtils.aesDecryptFile(edfp, ddfp, key);
    }
}
