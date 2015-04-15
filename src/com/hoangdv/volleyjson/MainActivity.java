package com.hoangdv.volleyjson;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hoangdv.volleyjson.app.AppController;
import com.hoangdv.volleyjson.util.Utilities;

public class MainActivity extends Activity {

	// json object response url
	private String urlJsonObj = "http://api.androidhive.info/volley/person_object.json";
	// json array response url
	private String urlJsonArry = "http://api.androidhive.info/volley/person_array.json";

	static Utilities utilities;
	private Button btn_jsObj, btn_jsArr;
	private TextView txt_result;
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		utilities = new Utilities(this);
		dialog = new ProgressDialog(this);
		dialog.setMessage("Please wait...");
		dialog.setCancelable(false);
		getControl();
	}

	public void getControl() {
		btn_jsArr = (Button) findViewById(R.id.btn_jsArr);
		btn_jsObj = (Button) findViewById(R.id.btn_jsObj);
		txt_result = (TextView) findViewById(R.id.txt_result);
		// Event click
		btn_jsArr.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				makeJsonArrayRequest();
			}
		});

		btn_jsObj.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				makeJsonObjRequest();
			}
		});
	}

	public void makeJsonObjRequest() {
		showDialog();
		JsonObjectRequest request = new JsonObjectRequest(Method.GET,
				urlJsonObj, null, new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg0) {
						// TODO Auto-generated method stub
						try {
							StringBuilder builder = new StringBuilder();
							builder.append(arg0.getString("name") + "\n");
							builder.append(arg0.getString("email") + "\n");
							JSONObject object = arg0.getJSONObject("phone");
							builder.append(object.getString("home") + "\n");
							builder.append(object.getString("mobile"));
							txt_result.setText(builder.toString());
							hidenDialog();
						} catch (Exception ex) {
							Toast.makeText(getApplicationContext(),
									ex.getMessage(), Toast.LENGTH_SHORT).show();
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(),
								arg0.getMessage(), Toast.LENGTH_SHORT).show();
					}
				});
		AppController.getInstance().addToReqestQueue(request);
	}

	public void makeJsonArrayRequest() {
		showDialog();
		JsonArrayRequest request = new JsonArrayRequest(urlJsonArry,
				new Listener<JSONArray>() {
					public void onResponse(JSONArray arr) {
						StringBuilder builder = new StringBuilder();
						try {
							for (int i = 0; i < arr.length(); i++) {
								JSONObject object = (JSONObject) arr.get(i);
								builder.append(object.getString("name")+"\n");
								builder.append(object.getString("email")+"\n");
								JSONObject phone = object.getJSONObject("phone");
								builder.append(phone.getString("home")+"\n");
								builder.append(phone.getString("mobile")+"\n\n");
								builder.append("-----------------------------\n");
								txt_result.append(builder.toString());
							}
							hidenDialog();
						} catch (Exception ex) {
							Toast.makeText(getApplicationContext(),
									ex.getMessage(), Toast.LENGTH_SHORT).show();
						}

					};
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						// TODO Auto-generated method stub

					}
				});
		AppController.getInstance().addToReqestQueue(request);
	}

	public void showDialog() {
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	public void hidenDialog() {
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
