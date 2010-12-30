package jp.jagfukuoka.sodefuri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
/**
 * ユーザーのタイムラインを一覧で表示するActivity
 * @author shikajiro
 *
 */
public class TimeLineActivity extends ListActivity {
	private static final String TIMELINE_URL = "http://api.twitter.com/1/statuses/public_timeline.json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// timeline取得処理
		String screenName = getIntent().getStringExtra("screen_name");
		List<String> list = this.getTimeLine(screenName);

		setListAdapter(new ArrayAdapter<String>(this, R.layout.timeline_item, list));
	}

	private List<String> getTimeLine(String screenName) {
		List<String> result = new ArrayList<String>();

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(TIMELINE_URL);
		try {
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				Toast.makeText(TimeLineActivity.this, "publicタイムラインを取得しました", Toast.LENGTH_LONG).show();

				InputStream is = response.getEntity().getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line;
				String responsJson = "";
				while ((line = br.readLine()) != null) {
					responsJson += line;
				}

				JSONArray jsonArrays = new JSONArray(responsJson);
				for (int i = 0; i < jsonArrays.length(); i++) {
					JSONObject jsonObj = jsonArrays.getJSONObject(i);
					String screen_name = jsonObj.getString("text");
					result.add(screen_name);
				}

			}else{
				Toast.makeText(TimeLineActivity.this,"[タイムライン取得error]: " + response.getStatusLine(),	Toast.LENGTH_LONG).show();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}
