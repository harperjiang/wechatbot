import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList

import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicNameValuePair
import java.util.regex.Pattern
import com.google.zxing.common.BitMatrix
import java.io.File
import java.util.HashMap
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.MultiFormatWriter
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import java.nio.file.Path
import java.nio.file.FileSystems

object ManualLogin extends App {
  // Request UUID
  var client = HttpClientBuilder.create().build();

  var post = new HttpPost("https://login.weixin.qq.com/jslogin");

  var nvps = new ArrayList[NameValuePair]();
  //  nvps.add(new BasicNameValuePair("appid", "wxeb7ec651dd0aefa9"));
  nvps.add(new BasicNameValuePair("appid", "wx782c26e4c19acffb"));
  nvps.add(new BasicNameValuePair("fun", "new"));
  nvps.add(new BasicNameValuePair("lang", "zh_CN"));
  nvps.add(new BasicNameValuePair("_", (System.currentTimeMillis() / 1000).toString()));
  post.setEntity(new UrlEncodedFormEntity(nvps));

  var resp = client.execute(post);

  if (200 != resp.getStatusLine.getStatusCode) {
    throw new IllegalArgumentException(resp.getStatusLine.getStatusCode.toString);
  }
  var br = new BufferedReader(new InputStreamReader(resp.getEntity.getContent));
  var line = br.readLine();
  if (null == line || line.isEmpty())
    throw new IllegalArgumentException(line);
  System.out.println(line);

  var pattern = Pattern.compile("window.QRLogin.code = 200; window.QRLogin.uuid = \"([\\d\\w\\W=]+)\";")
  var matcher = pattern.matcher(line)
  if (!matcher.matches())
    throw new IllegalArgumentException(line);
  var uuid = matcher.group(1)

  System.out.println(uuid)

  // Generate QR Code
  var qrCodeData = "Hello World!";
  var filePath = "QRCode.png";
  var charset = "UTF-8"; // or "ISO-8859-1"
  var hintMap = new HashMap[EncodeHintType, ErrorCorrectionLevel]();
  hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

  var matrix = new MultiFormatWriter().encode(
    new String(qrCodeData.getBytes(charset), charset),
    BarcodeFormat.QR_CODE, 128, 128, hintMap);
  var path = FileSystems.getDefault().getPath(filePath);
  MatrixToImageWriter.writeToPath(matrix, filePath.substring(filePath
    .lastIndexOf('.') + 1), path);

}