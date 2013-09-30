package com.umassd.ece489.shiftcipher;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class Activity_ShiftCipher extends Activity implements OnClickListener
{
	EditText inputEditText = null;
	EditText outputEditText = null;
	
	NumberPicker numberPicker = null;
	
	CheckBox reverse = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity__shift_cipher);
		
		numberPicker = (NumberPicker)findViewById(R.id.numberPicker);
		
		numberPicker.setMinValue(1);
		numberPicker.setMaxValue(26);
		numberPicker.setValue(1);
		
		reverse = (CheckBox)findViewById(R.id.reverse);
		
		inputEditText = (EditText)findViewById(R.id.inputEditText);
		outputEditText = (EditText)findViewById(R.id.outputEditText);
		
		Button inputShift = (Button)findViewById(R.id.inputShiftButton);
		Button inputClear = (Button)findViewById(R.id.inputClearButton);
		Button outputCopy = (Button)findViewById(R.id.outputCopyButton);
		Button outputClear = (Button)findViewById(R.id.outputClearButton);
		
		inputShift.setOnClickListener(this);
		inputClear.setOnClickListener(this);
		outputCopy.setOnClickListener(this);
		outputClear.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		String inputString = inputEditText.getText().toString();
		String outputString = outputEditText.getText().toString();
		
		switch(v.getId())
		{
			case R.id.inputShiftButton:
				if(inputString.equals(""))
					Toast.makeText(this, R.string.prompt_empty_string_in, Toast.LENGTH_SHORT).show();
				else
				{
					String oldString = inputString;
					inputString = stripNonAlpha(inputString);

					String shiftedString = "";
					for(int i = 0; i < inputString.length(); i++)
						shiftedString += addAndCheckWrap(inputString.charAt(i));	
					
					outputEditText.setText(shiftedString);
					
					if(!oldString.equals(inputString))
						Toast.makeText(this, R.string.prompt_stripped, Toast.LENGTH_LONG).show();
					else
						Toast.makeText(this, R.string.prompt_shifted, Toast.LENGTH_SHORT).show();
				}

				break;
			case R.id.inputClearButton:
				inputEditText.setText("");
				break;
			case R.id.outputCopyButton:
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
			case R.id.outputClearButton:
				outputEditText.setText("");
				break;
		}
	}
	
	private char addAndCheckWrap(char c)
	{
		int C = ((int)c);
		
		if(reverse.isChecked())
		{
			for(int i = Math.abs(numberPicker.getValue()); i > 0; i--)
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
			for(int i = 0; i < numberPicker.getValue(); i++)
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