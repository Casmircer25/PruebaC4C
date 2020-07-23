package com.example.pruebac4c;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.RenderScript;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView messagesTextView;
    EditText inputEditText;
    Button sendButton;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        messagesTextView = findViewById(R.id.messageTextView);
        inputEditText = findViewById(R.id.inputEditText);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = inputEditText.getText().toString();
                messagesTextView.append(Html.fromHtml("<p><b>Tu:</b> " + input + "</p>"));
                inputEditText.setText("");

                getResponse(input);
            }
        });

    }

    private void getResponse(String input){

        String workspaceId = "45c09d62-7326-4400-8bd9-c73681f9b1a6";
        String urlAssistant = "https://api.us-south.assistant.watson.cloud.ibm.com/instances/3796f435-4e00-4678-89df-dcd9747fd82a/v1/workspaces/"+workspaceId+"/message";

        String authentication = "ek1GUWdiZm1UN0plX2xleXVJWkg4YlMxbDFjalFUMGh3bjNSV2lBMWxROG0=";


        JSONObject inputJsonObject =new JSONObject();
        try {
            inputJsonObject.put(name:"text", input);
        } catch (JSONException e)  {
            e.printStackTrace();
        }

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(name:"input", inputJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        AndroidNetworking.post(urlAssistant)
                .addHeaders(key: "Content-Type", value: "application/json")
                .addHeaders(key: "Authorization", value: "Basic " + authentication)
                .addJSONObjectBody(jsonBody)
                .setPriority(Priority.HIGH)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String outputAssistant = "";

                        //veo la respuesta del json

                        try {
                            String outputJsonObject = response.getJSONObject("output").getJONArray(name:"text").getString(index:0);
                            messagesTextView.append((Html.fromHtml("<p><b>Chatbot:</b> " + outputJsonObject + "</p>"));
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(context, text: "Error de conexión", Toast.LENGTH_LONG).show();

                }
                });
        }
}
