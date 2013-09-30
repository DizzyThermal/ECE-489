package com.umassd.ece489.shiftcipher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class Activity_ShiftCipher_Two extends Activity implements OnClickListener
{
	EditText inputEditText = null;
	EditText outputEditText = null;
	
	NumberPicker numberPicker = null;
	NumberPicker numberPicker2 = null;
	
	CheckBox reverse = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shift_cipher_two);
		
		numberPicker = (NumberPicker)findViewById(R.id.numberPicker_two);
		numberPicker2 = (NumberPicker)findViewById(R.id.numberPicker_two_2);
		
		numberPicker.setMinValue(1);
		numberPicker.setMaxValue(26);
		numberPicker.setValue(1);
		
		numberPicker2.setMinValue(1);
		numberPicker2.setMaxValue(26);
		numberPicker2.setValue(1);
		
		reverse = (CheckBox)findViewById(R.id.reverse_two);
		
		inputEditText = (EditText)findViewById(R.id.inputEditText_two);
		outputEditText = (EditText)findViewById(R.id.outputEditText_two);
		
		Button inputShift = (Button)findViewById(R.id.inputShiftButton_two);
		Button inputClear = (Button)findViewById(R.id.inputClearButton_two);
		Button outputCopy = (Button)findViewById(R.id.outputCopyButton_two);
		Button outputClear = (Button)findViewById(R.id.outputClearButton_two);
		
		inputShift.setOnClickListener(this);
		inputClear.setOnClickListener(this);
		outputCopy.setOnClickListener(this);
		outputClear.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId())
		{
			case R.id.activity_loopone:
				Intent message = new Intent(this, Activity_ShiftCipher.class);
				startActivity(message); finish();
			break;
		}
		
		return true;
	}
	
	@Override
	public void onClick(View v)
	{
		String inputString = inputEditText.getText().toString();
		String outputString = outputEditText.getText().toString();
		
		switch(v.getId())
		{
			case R.id.inputShiftButton_two:
				if(inputString.equals(""))
					Toast.makeText(this, R.string.prompt_empty_string_in, Toast.LENGTH_SHORT).show();
				else
				{
					String oldString = inputString;
					inputString = stripNonAlpha(inputString);

					String shiftedString = "";
					for(int i = 0; i < inputString.length(); i++)
						shiftedString += addAndCheckWrap(inputString.charAt(i), i);	
					
					outputEditText.setText(shiftedString);
					
					if(!oldString.equals(inputString))
						Toast.makeText(this, R.string.prompt_stripped, Toast.LENGTH_LONG).show();
					else
						Toast.makeText(this, R.string.prompt_shifted, Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.inputClearButton_two:
				inputEditText.setText("");
				break;
			case R.id.outputCopyButton_two:
				if(outputString.equals(""))
					Toast.makeText(this, R.string.prompt_empty_string_out, Toast.LENGTH_SHORT).show();
				else
				{
					ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE); 
					ClipData clip = android.content.ClipData.newPlainText("text label", outputString);
			    	clipboard.setPrimaryClip(clip);
			    	
			    	Toast.makeText(this, R.string.prompt_copied, Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.outputClearButton_two:
				outputEditText.setText("");
				break;
		}
	}
	
	private char addAndCheckWrap(char c, int loopCount)
	{
		int C = ((int)c);
		
		int loop = ((loopCount % 2) == 0)?numberPicker.getValue():numberPicker2.getValue();
		if(reverse.isChecked())
		{
			for(int i = loop; i > 0; i--)
			{
				if(!(C == ((int)' ')))
					C--;

				if(C == (((int)'A') - 1))
					C = ((int)'Z');
				if(C == (((int)'a') - 1))
					C = ((int)'z');
			}
		}
		else
		{
			for(int i = 0; i < loop; i++)
			{
				if(!(C == ((int)' ')))
					C++;

				if(C == (((int)'Z') + 1))
					C = ((int)'A');
				if(C == (((int)'z') + 1))
					C = ((int)'a');
			}
		}
		
		return ((char)C);
	}
	
	private String stripNonAlpha(String s)
	{
		String returnString = "";

		for(int i = 0; i < s.length(); i++)
		{
			int c = ((int)s.charAt(i));
			
			if((c >= ((int)'A') && c <= ((int)'Z')) || (c >= ((int)'a') && c <= ((int)'z')) || (c == ((int)' ')))
				returnString += ((char)c);
		}

		return returnString;
	}
}