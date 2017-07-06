package br.com.wemind.marketplacetribanco.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.wemind.marketplacetribanco.R;

/**
 * Created by kmkraiker on 24/05/2017.
 */

public class MainButtonsAdapter extends BaseAdapter {

    private List<MenuItem> items;
    private Context context;

    public MainButtonsAdapter(Context context) {
        this.context = context;
        XmlResourceParser parser = context.getResources().getXml(R.xml.main_buttons);
        items = new ArrayList<>();
        try {
            parser.next();
            parser.next();
            int event;
            while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    MenuItem item = new MenuItem();
                    item.id = parser.getAttributeValue(null, "id");
                    item.icon = parser.getAttributeValue(null, "icon");
                    item.title = parser.getAttributeValue(null, "title");
                    item.onClick = parser.getAttributeValue(null, "onClick");
                    items.add(item);
                }
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return context.getResources().getIdentifier(items.get(position).id, "id", context.getPackageName());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Button b;
        if (convertView == null) {
            b = (Button) inflater.inflate(R.layout.content_main_button, parent, false);
        } else {
            b = (Button) convertView;
        }
        Drawable icon = context.getResources().getDrawable(R.drawable.bg_button_3);
        icon.setBounds(0, 0, Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics())), Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, context.getResources().getDisplayMetrics())));
        b.setCompoundDrawables(icon, null, null, null);
        b.setText(items.get(position).title);
        b.setBackground(getBackgroundForPosition(position));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        items.get(position).click();
                    }
                });
            }
        });
        return b;
    }

    private Drawable getBackgroundForPosition(int position) {
        switch (position){
            case 0:
                return context.getResources().getDrawable(R.drawable.bg_ini_cota_xxxhdpi);
            case 1:

                return context.getResources().getDrawable(R.drawable.bg_cota_em_anda_xxxhdpi);
            case 2:

                return context.getResources().getDrawable(R.drawable.bg_criar_lista_xxxhdpi);
            case 3:

                return context.getResources().getDrawable(R.drawable.bg_minhas_lista_xxxhdpi);
            case 4:

                return context.getResources().getDrawable(R.drawable.bg_gere_prod_xxxhdpi);
            case 5:

                return context.getResources().getDrawable(R.drawable.bg_meus_forne_xxxhdpi);

        }
        return context.getResources().getDrawable(R.drawable.bg_button_2);
    }

    private class MenuItem {
        private String id;
        private String icon;
        private String title;
        private String onClick;

        private void click() {
            if (onClick.equals("")) return;
            try {
                Intent intent = new Intent(context, Class.forName(onClick));
                context.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
