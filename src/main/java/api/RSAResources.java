package api;

import jakarta.ws.rs.Produces;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import model.RSA;

@Path("/api/v1")
public class RSAResources {

    private RSA rsa;

    public RSAResources () throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        rsa = RSA.getInstance();
        
        if (RSA.getInstance().isExists()) {
            rsa.openFromFilePrivateKey(RSA.path_file_private);
            rsa.openFromFilePublicKey(RSA.path_file_public);
        } else {
            rsa.genKeyPair(1024);
            rsa.saveToFilePrivateKey(RSA.path_file_private);
            rsa.saveToFilePublicKey(RSA.path_file_public);
        }
    }

    @GET
    @Path("/priv")
    @Produces("text/plain")
    public String PRIVATE_KEY () throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return rsa.getPrivateKeyString();
    }

    @GET
    @Path("/pub")
    @Produces("text/plain")
    public String PUBLIC_KEY () {
        return rsa.getPublicKeyString();
    }
}
