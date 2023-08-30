package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

    private static RSA instance;

    private PrivateKey KEY_PRIVATE;

    private static String name_file = "rsa".concat(".pri");
    private static String __dirname__ = System.getProperty("user.dir");
    public static String path_file = __dirname__.concat("/src/main/resources/"+name_file);

    private RSA() {}

    public static RSA getInstance() {
        if (instance == null) {
            instance = new RSA ();
        }
        return instance;
    }

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

    private void setPrivateKey (String key) {
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

    public String getPrivateKeyString () {
        PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(this.KEY_PRIVATE.getEncoded());
        return bytesToString(pkcs8.getEncoded());
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
    
        PrivateKey privateKey = keyPair.getPrivate();

        this.KEY_PRIVATE = privateKey;
    }

    public void saveToFilePrivateKey (String path) {
        try (Writer writer = new BufferedWriter(
            new OutputStreamWriter(
                new FileOutputStream(path), "UTF-8"))) {
            writer.write(this.getPrivateKeyString());
        } catch (IOException e) {e.printStackTrace();}
    }

    public void openFromFilePrivateKey (String path) {
        this.setPrivateKey(this.readingFileAsString(path));
    }

    private String readingFileAsString (String path) {
        StringBuffer data = new StringBuffer();
        try (BufferedReader reader = new BufferedReader (new FileReader(path))) {
        
            char[] buffer = new char[1024];
            int numRead = 0;

            while ((numRead = reader.read(buffer)) != -1) {
                data.append(String.valueOf(buffer,0,numRead));
            }
        } catch (IOException e) {e.printStackTrace();}
        
        return data.toString();
    }

    public boolean isExists () {
        File validating = new File (path_file);

        boolean status = validating.isFile() ? true : false;

        return status;
    }
}
