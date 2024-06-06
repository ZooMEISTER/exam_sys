package org.zoom.Utils;

import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.RecursiveTask;

/**
 * @Author ZooMEISTER
 * @Description: TODO
 * @DateTime 2024/5/19 13:57
 **/

public class ECDsaUtils {

    private static final String EC_ALGORITHM = "SHA256withECDSA";
    private static final String KEY_ALGORITHM = "EC";

    /**
    * @Author: ZooMEISTER
    * @Description: 获取密钥对
    * @DateTime: 2024/5/19 14:09
    * @Params:
    * @Return
    */
    public static KeyPair generateKeyPair_ECDSA() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(256);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        return keyPair;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 获取签名
    * @DateTime: 2024/5/19 14:12
    * @Params:
    * @Return
    */
    public static byte[] getSign_ECDSA(byte[] privateKey, byte[] data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey_ = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        Signature signature = Signature.getInstance(EC_ALGORITHM);
        signature.initSign(privateKey_);
        signature.update(data);

        byte[] sign = signature.sign();

        return sign;
    }

    /**
    * @Author: ZooMEISTER
    * @Description: 验证签名
    * @DateTime: 2024/5/19 14:56
    * @Params:
    * @Return
    */
    public static boolean verifySign(byte[] publicKey, byte[] sign, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey_ = keyFactory.generatePublic(x509EncodedKeySpec);

        Signature signature = Signature.getInstance(EC_ALGORITHM);
        signature.initVerify(publicKey_);
        signature.update(data);

        boolean bool = signature.verify(sign);

        return bool;
    }
}
