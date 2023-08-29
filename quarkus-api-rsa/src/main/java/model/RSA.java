package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RSA {
    //private String KEY_PUBLIC;
    //private String KEY_PRIVATE;
    /*public RSA (String KEY_PUBLIC, String KEY_PRIVATE) {
        this.KEY_PUBLIC = KEY_PUBLIC;
        this.KEY_PRIVATE = KEY_PRIVATE;
    }*/
    private PrivateKey KEY_PRIVATE;
    private PublicKey KEY_PUBLIC;

    /*public RSA (String KEY_PRIVATE, String KEY_PUBLIC) {
        this.KEY_PRIVATE = KEY_PRIVATE;
    }*/

    public RSA() {}

    protected String bytesToString(byte[] byt) {
        byte[] secondByt = new byte[byt.length+1];
        secondByt[0] = 1;
        System.arraycopy(byt, 0, secondByt, 1, byt.length);
        return new BigInteger(secondByt).toString(36);
    }
    
    protected byte[] stringToBytes (String str) {
        byte[] secondByt = new BigInteger(str,36).toByteArray();
        return Arrays.copyOfRange(secondByt, 1, secondByt.length);
    }

    public void setPrivateKey (String key) {
        try {
            byte[] privateKeyEncoded = stringToBytes(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(privateKeyEncoded);
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8);
            this.KEY_PRIVATE = privateKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public void setPublicKey (String key) {
        try {
            byte[] publicKeyEncoded = stringToBytes(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(publicKeyEncoded);
            PublicKey publicKey = keyFactory.generatePublic(x509);
            this.KEY_PUBLIC = publicKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    public String getPrivateKeyString () {
        PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(this.KEY_PRIVATE.getEncoded());
        return bytesToString(pkcs8.getEncoded());
    }

    public String getPublicKeyString () {
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(this.KEY_PUBLIC.getEncoded());
        return bytesToString(x509.getEncoded());
    }

    public void genKeyPair (int size) throws NoSuchAlgorithmException,
                                        NoSuchPaddingException,
                                        InvalidKeyException,
                                        IllegalBlockSizeException,
                                        BadPaddingException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstanceStrong();
        keyPairGenerator.initialize(size, random);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
    
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        this.KEY_PRIVATE = privateKey;
        this.KEY_PUBLIC = publicKey;
    }

    public String Encrypt (String str) throws NoSuchAlgorithmException, NoSuchPaddingException, 
                                        InvalidKeyException, IllegalBlockSizeException, 
                                        BadPaddingException {
        byte[] bytesEncrypted;

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, this.KEY_PUBLIC);
        bytesEncrypted = cipher.doFinal(str.getBytes());
        
        return bytesToString(bytesEncrypted);
    }

    public String Decrypt (String str) throws NoSuchAlgorithmException, NoSuchPaddingException, 
                                        InvalidKeyException, IllegalBlockSizeException, 
                                        BadPaddingException {
        byte[] bytesDecrypted;

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, this.KEY_PRIVATE);
        bytesDecrypted = cipher.doFinal(stringToBytes(str));

        return new String (bytesDecrypted);
    }

    /*FOR NOW BELOW CORREGNESS*/
    /*public void saveToDiskPrivateKey(String path) throws IOException {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            out.write(this.getPrivateKeyString());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveToDiskPublicKey(String path) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            out.write(this.getPublicKeyString());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFromDiskPublicKey(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String content = this.readFileAsString(path);
        this.setPublicKey(content);
    }
    
    public void openFromDiskPrivateKey(String path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String content = this.readFileAsString(path);
        this.setPrivateKey(content);
    }

    private String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }*/
}
