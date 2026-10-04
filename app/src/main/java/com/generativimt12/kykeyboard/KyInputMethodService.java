package com.generativimt12.kykeyboard;

import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;

public class KyInputMethodService extends InputMethodService {
  private TextView status; private boolean hebrew=true; private boolean shift=false; private int lastKey=-1,lastIndex=-1; private long lastTime=0; private final Handler h=new Handler(Looper.getMainLooper());
  private final String[] HE={".,!?-()@/:;_+*#% 0","אבג2","דהו3","זחט4","יכל5","מנס6","עפצ7","קרש8","תץךםן9"," 0"," 1"};
  private final String[] EN={".,!?-()@/:;_+*#% 0","abc2","def3","ghi4","jkl5","mno6","pqrs7","tuv8","wxyz9"," 0"," 1"};
  @Override public View onCreateInputView(){ return buildView(); }
  private View buildView(){
    android.widget.LinearLayout root=new android.widget.LinearLayout(this); root.setOrientation(android.widget.LinearLayout.VERTICAL); root.setBackgroundColor(Color.WHITE); root.setPadding(4,4,4,4);
    status=new TextView(this); status.setText((hebrew?"עברית":"English")+"  |  "+(shift?"⇧":"")); status.setTextSize(16); status.setTextColor(Color.DKGRAY); status.setGravity(17); root.addView(status,new android.widget.LinearLayout.LayoutParams(-1,48));
    int[] keys={1,2,3,4,5,6,7,8,9,10,0,11}; String[] labels=hebrew?new String[]{"אבג\n2","דהו\n3","זחט\n4","יכל\n5","מנס\n6","עפצ\n7","קרש\n8","תץךםן\n9","⌫","שפה","0\nרווח","↵"}:new String[]{"ABC\n2","DEF\n3","GHI\n4","JKL\n5","MNO\n6","PQRS\n7","TUV\n8","WXYZ\n9","⌫","Lang","0\nSpace","↵"};
    android.widget.GridLayout g=new android.widget.GridLayout(this); g.setColumnCount(3); g.setRowCount(4);
    for(int i=0;i<12;i++){ ButtonEx b=new ButtonEx(this); b.setText(labels[i]); b.setTextSize(16); final int k=keys[i]; b.setOnClickListener(v->press(k)); android.widget.GridLayout.LayoutParams p=new android.widget.GridLayout.LayoutParams(); p.width=0;p.height=72;p.columnSpec=android.widget.GridLayout.spec(i%3,1,1f);p.rowSpec=android.widget.GridLayout.spec(i/3,1,1f);g.addView(b,p); }
    root.addView(g,new android.widget.LinearLayout.LayoutParams(-1,-1)); return root;
  }
  private static class ButtonEx extends android.widget.Button { ButtonEx(android.content.Context c){super(c); setAllCaps(false);} }
  private void press(int k){ InputConnection ic=getCurrentInputConnection(); if(ic==null)return;
    if(k==9){ic.deleteSurroundingText(1,0); reset();return;} if(k==10){hebrew=!hebrew; reset(); setInputView(buildView()); return;} if(k==11){ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_ENTER));ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,KeyEvent.KEYCODE_ENTER));reset();return;} if(k==0){commit(" ");reset();return;}
    int idx=k-1; String s=(hebrew?HE:EN)[idx]; long now=System.currentTimeMillis(); int pos=(lastKey==k && now-lastTime<900)?(lastIndex+1)%s.length():0; if(lastKey==k && now-lastTime<900){ic.deleteSurroundingText(1,0);} String ch=s.substring(pos,pos+1); if(shift)ch=ch.toUpperCase(); commit(ch); lastKey=k;lastIndex=pos;lastTime=now; shift=false; updateStatus();
    h.removeCallbacks(resetRunnable); h.postDelayed(resetRunnable,950);
  }
  private final Runnable resetRunnable=()->reset(); private void commit(String s){getCurrentInputConnection().commitText(s,1);} private void reset(){lastKey=-1;lastIndex=-1;lastTime=0;updateStatus();} private void updateStatus(){if(status!=null)status.setText((hebrew?"עברית":"English")+"  |  "+(shift?"⇧":""));}
  @Override public boolean onKeyDown(int keyCode, KeyEvent event){
    InputConnection ic=getCurrentInputConnection(); if(ic==null)return false;
    if(keyCode==KeyEvent.KEYCODE_DEL){ic.deleteSurroundingText(1,0);return true;} if(keyCode==KeyEvent.KEYCODE_ENTER){ic.sendKeyEvent(event);return true;} if(keyCode==KeyEvent.KEYCODE_SPACE){commit(" ");return true;}
    int k=-1; switch(keyCode){case KeyEvent.KEYCODE_0:k=0;break;case KeyEvent.KEYCODE_1:k=1;break;case KeyEvent.KEYCODE_2:k=2;break;case KeyEvent.KEYCODE_3:k=3;break;case KeyEvent.KEYCODE_4:k=4;break;case KeyEvent.KEYCODE_5:k=5;break;case KeyEvent.KEYCODE_6:k=6;break;case KeyEvent.KEYCODE_7:k=7;break;case KeyEvent.KEYCODE_8:k=8;break;case KeyEvent.KEYCODE_9:k=9;break;}
    if(k>=0){press(k);return true;} return super.onKeyDown(keyCode,event);
  }
}
