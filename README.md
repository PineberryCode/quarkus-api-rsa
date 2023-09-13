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
