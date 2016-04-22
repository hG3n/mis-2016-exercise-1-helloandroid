package hg3n.myapplication;

import android.app.ActivityManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class main_screen extends AppCompatActivity {

    private static final String TAG = "Main_Activity";
    Button _button;
    EditText _text_input;
    WebView _web_view;
    TextView _text_view;
    WebViewClient _web_view_client_ptr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // init members
        _button = (Button)this.findViewById(R.id.connect_button);
        _text_input = (EditText)this.findViewById(R.id.input_url);
        _web_view = (WebView)this.findViewById(R.id.web_view);
        _text_view = (TextView)this.findViewById(R.id.text_view);

        _web_view_client_ptr = new WebViewClient() {
            public void onReceivedError(WebView view, int error_code, String description, String failing_url) {
                String error = "FAILURE: error "+ Integer.toString(error_code) +" occured\n" +
                        " > DESCRIPTION: " + description;

                _text_view.setText(error);

                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        };

        _text_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                button_clicked(_button);
                return true;
            }
        });
        // init new web view client
        // fixed cache error
        _web_view.setWebViewClient(_web_view_client_ptr);
        // enable javascript for webpages
        _web_view.getSettings().setJavaScriptEnabled(true);
    }

    public void button_clicked(View v) {
        String input_text = _text_input.getText().toString();
        String url = parse_url(input_text);
        if(v.getId() == R.id.connect_button) {
            // get text input from EditText field
            if(url.length() != 0) {
                _web_view.loadUrl(url);

                // error handling for html errors
                _text_view.setText("URL successfully loaded (" + input_text + ")");
            } else {
                _text_view.setText("Error loading URL (" + input_text + ")");
            }
        }
    }

    private String parse_url(String url) {

        if(!url.startsWith("http://"))
            url = "http://" + url;

        if(Patterns.WEB_URL.matcher(url).matches())
            return url;

        return "";
    }

}
