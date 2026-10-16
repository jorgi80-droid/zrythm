package org.zrythm.mobilepreview;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private final int bg = Color.rgb(16, 19, 18);
    private final int panel = Color.rgb(24, 30, 27);
    private final int raised = Color.rgb(34, 42, 38);
    private final int line = Color.rgb(48, 58, 52);
    private final int green = Color.rgb(41, 217, 137);
    private final int white = Color.rgb(237, 245, 239);
    private final int muted = Color.rgb(152, 167, 157);
    private LinearLayout tracks;
    private LinearLayout navPanel;
    private LinearLayout inspectorPanel;
    private TextView leftPanelButton;
    private TextView rightPanelButton;
    private boolean workspaceOpen = false;
    private boolean trackControlsOpen = false;
    private TextView playButton;
    private boolean playing = false;
    private String page = "Arrangement";

    @Override public void onCreate(Bundle saved) {
        super.onCreate(saved);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(buildRoot());
    }

    private GradientDrawable shape(int color, int stroke) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(8));
        if (stroke != 0) d.setStroke(dp(1), stroke);
        return d;
    }
    private int dp(float n) { return (int)(n * getResources().getDisplayMetrics().density + 0.5f); }
    private LinearLayout row() {
        LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL); return l;
    }
    private LinearLayout col() {
        LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); return l;
    }
    private TextView label(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }
    private LinearLayout.LayoutParams lp(int w, int h) { return new LinearLayout.LayoutParams(w < 0 ? w : dp(w), h < 0 ? h : dp(h)); }
    private void pad(View v, int l, int t, int r, int b) { v.setPadding(dp(l), dp(t), dp(r), dp(b)); }

    private View buildRoot() {
        LinearLayout root = col(); root.setBackgroundColor(bg);
        root.addView(buildTopBar(), lp(-1, 58));

        FrameLayout stage = new FrameLayout(this);
        root.addView(stage, new LinearLayout.LayoutParams(-1, 0, 1));

        LinearLayout body = row();
        stage.addView(body, new FrameLayout.LayoutParams(-1, -1));

        navPanel = (LinearLayout) buildNav();
        navPanel.setVisibility(View.GONE);
        body.addView(navPanel, lp(148, -1));

        LinearLayout center = col();
        center.setPadding(dp(8), 0, dp(8), dp(6));
        body.addView(center, new LinearLayout.LayoutParams(0, -1, 1));
        center.addView(buildHeading(), lp(-1, 44));

        ScrollView sc = new ScrollView(this); sc.setFillViewport(true);
        sc.setBackground(shape(Color.rgb(17,22,19), line));
        tracks = col(); pad(tracks, 8, 8, 8, 8); tracks.setGravity(Gravity.TOP);
        sc.addView(tracks); center.addView(sc, new LinearLayout.LayoutParams(-1, 0, 1));
        populateTracks();
        center.addView(buildTools(), lp(-1, 48));

        inspectorPanel = (LinearLayout) buildInspector();
        inspectorPanel.setVisibility(View.GONE);
        body.addView(inspectorPanel, lp(184, -1));

        leftPanelButton = floatingPanelButton("☷", "Open workspace");
        FrameLayout.LayoutParams leftParams = new FrameLayout.LayoutParams(dp(34), dp(48), Gravity.LEFT | Gravity.CENTER_VERTICAL);
        leftParams.leftMargin = dp(2);
        stage.addView(leftPanelButton, leftParams);
        leftPanelButton.setOnClickListener(v -> {
            workspaceOpen = !workspaceOpen;
            navPanel.setVisibility(workspaceOpen ? View.VISIBLE : View.GONE);
            leftPanelButton.setText(workspaceOpen ? "‹" : "☷");
            leftPanelButton.setContentDescription(workspaceOpen ? "Close workspace" : "Open workspace");
        });

        rightPanelButton = floatingPanelButton("☰", "Open track controls");
        FrameLayout.LayoutParams rightParams = new FrameLayout.LayoutParams(dp(34), dp(48), Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        rightParams.rightMargin = dp(2);
        stage.addView(rightPanelButton, rightParams);
        rightPanelButton.setOnClickListener(v -> {
            trackControlsOpen = !trackControlsOpen;
            inspectorPanel.setVisibility(trackControlsOpen ? View.VISIBLE : View.GONE);
            rightPanelButton.setText(trackControlsOpen ? "›" : "☰");
            rightPanelButton.setContentDescription(trackControlsOpen ? "Close track controls" : "Open track controls");
        });

        return root;
    }

    private TextView floatingPanelButton(String text, String description) {
        TextView button = label(text, 18, green, true);
        button.setGravity(Gravity.CENTER);
        button.setContentDescription(description);
        button.setBackground(shape(raised, line));
        return button;
    }

    private View buildTopBar() {
        LinearLayout bar = row(); bar.setBackgroundColor(panel); pad(bar, 12, 4, 12, 4);
        TextView logo = label("Z", 30, green, true); bar.addView(logo, lp(32, -1));
        LinearLayout brand = col(); brand.addView(label("ZRYTHM", 13, white, true)); brand.addView(label("MOBILE PREVIEW", 9, muted, false));
        bar.addView(brand, lp(115, -2));
        View sep = new View(this); sep.setBackgroundColor(line); LinearLayout.LayoutParams sepLp=lp(1,34); sepLp.setMargins(dp(6),0,dp(10),0); bar.addView(sep,sepLp);
        bar.addView(label("120.00 BPM", 13, white, true), lp(92,-1));
        bar.addView(label("4 / 4", 13, white, true), lp(48,-1));
        View spacer = new View(this); bar.addView(spacer,new LinearLayout.LayoutParams(0,1,1));
        playButton = label("▶", 22, green, true); playButton.setGravity(Gravity.CENTER);
        playButton.setBackground(shape(raised, green)); bar.addView(playButton, lp(48,42));
        playButton.setOnClickListener(v -> { playing=!playing; playButton.setText(playing?"Ⅱ":"▶"); Toast.makeText(this, "UI preview only — audio engine is not connected", Toast.LENGTH_SHORT).show(); });
        TextView stop = label("■",18,white,true); stop.setGravity(Gravity.CENTER); stop.setBackground(shape(raised,line)); LinearLayout.LayoutParams stopLp=lp(42,42); stopLp.setMargins(dp(6),0,dp(8),0); bar.addView(stop,stopLp);
        stop.setOnClickListener(v -> {playing=false;playButton.setText("▶");});
        bar.addView(label("ENGINE: NOT CONNECTED", 9, 0xffffc56e, true), lp(-2,-1));
        return bar;
    }

    private View buildNav() {
        LinearLayout nav=col(); nav.setBackgroundColor(Color.rgb(21,26,23)); pad(nav,7,8,7,8);
        nav.addView(label("WORKSPACE",10,muted,true),lp(-1,28));
        String[] pages={"Arrangement","Piano Roll","Mixer","Browser","Automation"};
        String[] glyphs={"▤","♫","☷","▣","⌁"};
        for(int i=0;i<pages.length;i++){
            final String p=pages[i];
            TextView b=label(glyphs[i]+"  "+p,11,p.equals(page)?green:white,true); pad(b,8,0,2,0);
            b.setBackground(shape(p.equals(page)?Color.rgb(32,76,56):Color.TRANSPARENT,p.equals(page)?green:Color.TRANSPARENT));
            LinearLayout.LayoutParams pLp=lp(-1,42); pLp.setMargins(0,dp(3),0,dp(3)); nav.addView(b,pLp);
            b.setOnClickListener(v->{page=p; Toast.makeText(this,p+" selected (preview only)",Toast.LENGTH_SHORT).show(); recreate();});
        }
        View spacer=new View(this); nav.addView(spacer,new LinearLayout.LayoutParams(1,0,1));
        nav.addView(label("PROJECT",10,muted,true),lp(-1,24)); nav.addView(label("Untitled Project",10,white,false),lp(-1,28));
        return nav;
    }
    private View buildHeading() {
        LinearLayout r=row(); r.addView(label(page,18,white,true),lp(-2,-1));
        TextView subtitle=label("   Song timeline",10,muted,false); r.addView(subtitle,lp(-2,-1));
        View spacer=new View(this); r.addView(spacer,new LinearLayout.LayoutParams(0,1,1));
        TextView zoom=label("−     100%     +",12,green,true); zoom.setGravity(Gravity.CENTER); r.addView(zoom,lp(108,-1));
        return r;
    }
    private void populateTracks() {
        if(tracks==null)return; tracks.removeAllViews();
        String[] names={"DRUMS","BASS","SYNTH PAD","LEAD","GUITAR","VOCALS","FX"};
        int[] colors={0xff2379c8,0xff24b978,0xff9270e8,0xffdca34c,0xffdca34c,0xffd95f72,0xff25b9b0};
        String[] sections={"INTRO","VERSE","CHORUS","BREAK","CHORUS","OUTRO"};
        LinearLayout ruler=row(); ruler.addView(label("TRACK",9,muted,true),lp(74,24));
        for(String s:sections){TextView t=label(s,9,white,true);t.setGravity(Gravity.CENTER);t.setBackground(shape(raised,line));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,dp(24),1);p.setMargins(dp(2),0,dp(2),0);ruler.addView(t,p);}
        tracks.addView(ruler,lp(-1,28));
        for(int i=0;i<names.length;i++){
            LinearLayout tr=row(); LinearLayout.LayoutParams trp=lp(-1,48);trp.setMargins(0,dp(2),0,dp(2));
            TextView name=label((i+1)+"  "+names[i],9,white,true);name.setGravity(Gravity.CENTER);name.setBackground(shape(panel,colors[i]));tr.addView(name,lp(74,-1));
            LinearLayout clips=row(); clips.setGravity(Gravity.CENTER_VERTICAL);
            for(int j=0;j<6;j++){
                TextView clip=label(i==0?"▰ MIDI":i==4||i==5?"≈ AUDIO":"▰ CLIP",8,white,true);clip.setGravity(Gravity.CENTER);
                int c=colors[i]; clip.setBackground(shape(c,Color.rgb(Math.min(255,Color.red(c)+28),Math.min(255,Color.green(c)+28),Math.min(255,Color.blue(c)+28))));
                LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(0,dp(34),1);cp.setMargins(dp(2),0,dp(2),0);
                if((i+j)%4==0) clip.setVisibility(View.INVISIBLE);
                clips.addView(clip,cp);
            }
            tr.addView(clips,new LinearLayout.LayoutParams(0,-1,1)); tracks.addView(tr,trp);
        }
        TextView hint=label("Touch-first landscape arrangement • placeholder clips • no audio playback yet",10,muted,false);
        pad(hint,6,8,6,4);tracks.addView(hint,lp(-1,34));
    }
    private View buildTools() {
        LinearLayout tools=row(); tools.setBackgroundColor(panel); String[] items={"Select","Draw","Split","Erase","Snap 1/16"};
        for(String s:items){TextView b=label(s,10,white,true);b.setGravity(Gravity.CENTER);b.setBackground(shape(raised,line));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,dp(36),1);p.setMargins(dp(3),dp(4),dp(3),dp(4));tools.addView(b,p);b.setOnClickListener(v->Toast.makeText(this,s+" selected (preview only)",Toast.LENGTH_SHORT).show());}
        return tools;
    }
    private View buildInspector() {
        LinearLayout box=col();box.setBackgroundColor(panel);pad(box,10,10,10,10);
        box.addView(label("INSPECTOR",10,muted,true),lp(-1,24));
        box.addView(label("Track controls",15,white,true),lp(-1,34));
        box.addView(label("Volume",10,muted,false),lp(-1,22));
        android.widget.SeekBar volume=new android.widget.SeekBar(this);volume.setProgress(78);box.addView(volume,lp(-1,32));
        box.addView(label("Pan",10,muted,false),lp(-1,22));
        android.widget.SeekBar pan=new android.widget.SeekBar(this);pan.setProgress(50);box.addView(pan,lp(-1,32));
        LinearLayout ms=row();for(String s:new String[]{"MUTE","SOLO"}){TextView b=label(s,10,white,true);b.setGravity(Gravity.CENTER);b.setBackground(shape(raised,line));LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(0,dp(36),1);p.setMargins(dp(2),dp(3),dp(2),dp(3));ms.addView(b,p);}box.addView(ms,lp(-1,42));
        box.addView(label("INSERT EFFECTS",10,muted,true),lp(-1,30));
        for(String s:new String[]{"EQ","Compressor","Reverb"}){TextView e=label("●  "+s,10,white,false);pad(e,8,0,0,0);e.setBackground(shape(raised,line));LinearLayout.LayoutParams p=lp(-1,34);p.setMargins(0,dp(3),0,dp(3));box.addView(e,p);}
        View spacer=new View(this);box.addView(spacer,new LinearLayout.LayoutParams(1,0,1));
        TextView note=label("Prototype only\nNo DSP / MIDI / plugins connected",9,0xffffc56e,false);note.setGravity(Gravity.BOTTOM);box.addView(note,lp(-1,48));
        return box;
    }
}
