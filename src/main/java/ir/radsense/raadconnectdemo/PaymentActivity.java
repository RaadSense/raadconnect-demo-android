package ir.radsense.raadconnectdemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ir.radsense.raadconnect.RaadConnect;

public class PaymentActivity extends AppCompatActivity {

    public static final String TAG = "PaymentActivity";
    public static final String API_KEY = "988a4197-4245-476e-b8aa-b3eb74405665";

    ProgressBar progressBar;
    Button payButton;

    RaadConnect raadConnect;

    String payAuth;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setTitle(getString(R.string.charging_account));

        final EditText inputPrice = (EditText) findViewById(R.id.price);
        TextView textView1 = (TextView) findViewById(R.id.text1);
        TextView textView2 = (TextView) findViewById(R.id.text2);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        payButton = (Button) findViewById(R.id.pay_button);
        Typefaces.setTypeface(this, Typefaces.IRAN_LIGHT, inputPrice, textView1, textView2, payButton);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputPrice.getText().toString())) {
                    Toast.makeText(PaymentActivity.this, R.string.enter_price, Toast.LENGTH_SHORT).show();
                    return;
                }

                setLoading(true);
                long price = Long.parseLong(inputPrice.getText().toString());
                raadConnect.generatePayAuthAsync(price, 1, "raadconnect", new RaadConnect.PayAuthListener() {
                    @Override
                    public void onSuccess(String s) {
                        setLoading(false);
                        payAuth = s;
                        raadConnect.startWebPayment(payAuth, token, PaymentActivity.this);
                    }

                    @Override
                    public void onFailure(String s) {
                        setLoading(false);
                        Toast.makeText(PaymentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        raadConnect = RaadConnect.getInstance(API_KEY);
        raadConnect.setWebPageToolbarColor(Color.BLUE);
        raadConnect.setOnGetTokenListener(new RaadConnect.GetTokenListener() {
            @Override
            public void onStartGetting() {
                Log.i(TAG, "on Getting Token");
                setLoading(true);
            }

            @Override
            public void onSuccess(String s) {
                Log.i(TAG, "on get token success");
                Toast.makeText(PaymentActivity.this, "on success: " + s, Toast.LENGTH_SHORT).show();
                setLoading(false);
            }

            @Override
            public void onFailure(String s) {
                Log.i(TAG, "on get token failure");
                Toast.makeText(PaymentActivity.this, "on failure: " + s, Toast.LENGTH_SHORT).show();
                setLoading(false);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        raadConnect.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent");
        raadConnect.onNewIntent(intent);
        /*token = RaadConnect.getTokenFromIntent(intent);
        if (!TextUtils.isEmpty(token)) {
            Toast.makeText(PaymentActivity.this, "Token: " + token, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PaymentActivity.this, "No token" + token, Toast.LENGTH_SHORT).show();
        }*/
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        payButton.setVisibility(loading ? View.GONE : View.VISIBLE);
    }

}
