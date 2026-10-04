package com.generativimt12.kykeyboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
  @Override public void onCreate(Bundle b) { super.onCreate(b);
    LinearLayout l=new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); l.setPadding(32,40,32,32);
    TextView t=new TextView(this); t.setText("Ky Keyboard\n\nמקלדת למכשירי חצי-טאץ׳/מקשים. אינה משתמשת בהרשאת נגישות.\n\nהפעל אותה כמקלדת ברירת המחדל דרך הגדרות המכשיר."); t.setTextSize(18); l.addView(t);
    Button open=new Button(this); open.setText("פתיחת הגדרות מקלדות"); open.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))); l.addView(open);
    setContentView(l);
  }
}
