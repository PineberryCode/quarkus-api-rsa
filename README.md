# quarkus-api-rsa

Initialize the API with this in your terminal:
> Linux
```
./mvnw quarkus:dev
```
> Windows
```
.\mvnw.cmd quarkus:dev
```
If you want to set the port of the _quarkus-api-rsa_ application you can adjust it in the **application.properties** file:
```
quarkus.http.port= PORT
```

- - -

For using this API specifically the use of **keys (Private/Public)**<br><br>
First, you have to make two *get requests* to the API, one of them for the **private key** and the other for the **public key**:
<details>
  <summary><h6>Getting class</h6></summary>
  
```JAVA
public class Getting {

  private final static String urlString = "http://localhost:8081/api/v1";

  public static String PRIVATE_KEY () {
        String priv = urlString.concat("/priv");
        StringBuffer strBuffer = null;
        try {
            URL url = new URL(priv);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int response = connection.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                strBuffer = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    strBuffer.append(inputLine);
                }
                in.close();
            } else {
                System.out.println("GET request failed. Response Code: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return strBuffer.toString();
    }

  public static String PUBLIC_KEY () {
        final String pub = urlString.concat("/pub");
        StringBuffer strBuffer = null;
        try {
            URL url = new URL(pub);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int response = connection.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                strBuffer = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    strBuffer.append(inputLine);
                }
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strBuffer.toString();
    }
}
```
</details>

- - -

Subsequently, You can utilize the **private key** and **public key**, storing them in a variable into a new class:
```JAVA
private String priv = Getting.PRIVATE_KEY();
private String pub = Getting.PUBLIC_KEY();
```
Remember to utilize these variable types **(PublicKey/PrivateKey)** in your class:
```JAVA
private PublicKey KEY_PUBLIC;
private PrivateKey KEY_PRIVATE;
```
Now, to encrypt some input:
```JAVA
public String Encrypt (String str) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] bytesEncrypted;

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, this.KEY_PUBLIC);
        bytesEncrypted = cipher.doFinal(str.getBytes());
        
        return bytesToString(bytesEncrypted);
}

//Convert bytes to string:
private String bytesToString(byte[] byt) {
  byte[] secondByt = new byte[byt.length+1];
  secondByt[0] = 1;
  System.arraycopy(byt, 0, secondByt, 1, byt.length);
  return new BigInteger(secondByt).toString(36);
}
```
Decrypt some input:
```JAVA
public String Decrypt (String str) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
  byte[] bytesDecrypted;

  Cipher cipher = Cipher.getInstance("RSA");

  cipher.init(Cipher.DECRYPT_MODE, this.KEY_PRIVATE);
  bytesDecrypted = cipher.doFinal(stringToBytes(str));

  return new String (bytesDecrypted);
}
```
