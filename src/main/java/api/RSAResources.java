package api;

import jakarta.ws.rs.Produces;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import model.RSA;

@Path("/api/v1")
public class RSAResources {

    private RSA rsa;

    public RSAResources () throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        rsa = new RSA();
        rsa.genKeyPair(1024);
    }

    @GET
    @Path("/pub")
    @Produces("text/plain")
    public String PUBLIC_KEY () throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        rsa.Encrypt("public");

        return rsa.getPublicKeyString();
    }

    @GET
    @Path("/priv")
    @Produces("text/plain")
    public String PRIVATE_KEY () throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        rsa.Encrypt("private");

        return rsa.getPrivateKeyString();
    }
}
