package com.bkav.edoc.sdk.util;

import org.apache.http.Header;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/*CodeJava.net*/
public class MultipartUtility {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset, List<Header> headers)
            throws IOException {
        this.charset = charset;
        this.boundary = "===" + System.currentTimeMillis() + "===";
        URL url = new URL(requestURL);
        this.httpConn = (HttpURLConnection) url.openConnection();
        this.httpConn.setUseCaches(false);
        this.httpConn.setDoOutput(true);
        this.httpConn.setDoInput(true);
        this.httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.boundary);
        for (Header header : headers) {
            httpConn.setRequestProperty(header.getName(), header.getValue());
        }
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName   name attribute in <input type="file" name="..." />
     * @param inputStream a File to be uploaded
     * @throws IOException
     **/
    public void addInputStreamPart(String fieldName, InputStream inputStream) throws IOException {
        byte[] buffer = new byte[4096];
        boolean var4 = true;

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            this.outputStream.write(buffer, 0, bytesRead);
        }

        this.outputStream.flush();
        inputStream.close();
        this.writer.append("\r\n");
        this.writer.flush();
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String finish() throws IOException {
        StringBuilder response = new StringBuilder();
        int status = this.httpConn.getResponseCode();
        BufferedReader reader;
        String line;
        if (status != 200 && status != 201) {
            reader = new BufferedReader(new InputStreamReader(this.httpConn.getErrorStream()));
            line = null;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            this.httpConn.disconnect();
        } else {
            reader = new BufferedReader(new InputStreamReader(this.httpConn.getInputStream()));
            line = null;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            this.httpConn.disconnect();
        }

        return response.toString();
    }
}
