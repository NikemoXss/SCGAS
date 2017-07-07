package com.lmq.card;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.czscg.R;
import com.lmq.main.api.BaseActivity;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class Card extends BaseActivity {

	final String TAG = getClass().getName();
	private Button scanButton;
	private TextView resultTextView;
	private int MY_SCAN_REQUEST_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_activity);
		resultTextView = (TextView) findViewById(R.id.textView1);
		scanButton = (Button) findViewById(R.id.button1);
		resultTextView.setText("扫描银行卡: " + CardIOActivity.sdkVersion() + "\nBuilt: " + CardIOActivity.sdkBuildDate());
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (CardIOActivity.canReadCardWithCamera(this)) {
			scanButton.setText("Scan a credit card with card.io");
		} else {
			scanButton.setText("Enter credit card information");
		}
	}
	
	public void onScanPress(View v) {
		Intent scanIntent = new Intent(this, CardIOActivity.class);
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); 
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); 
		scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); 
		// scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); 
		//scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);//是否显示右下角键盘输入，为不显示
		startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
		}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String resultStr;
		if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
			CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
			resultStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";
			if (scanResult.isExpiryValid()) {
				resultStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
			}
			if (scanResult.cvv != null) {
				resultStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
			}
			if (scanResult.postalCode != null) {
				resultStr += "Postal Code: " + scanResult.postalCode + "\n";
			}
		} else {
			resultStr = "Scan was canceled.";
		}
		resultTextView.setText(resultStr);

	}


}
