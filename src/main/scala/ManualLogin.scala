import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.entity.UrlEncodedFormEntity
import java.util.ArrayList
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair
import java.io.BufferedReader
import java.io.InputStreamReader

object ManualLogin extends App {

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

}