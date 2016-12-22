package com.znshadows.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mistery.winddirection.R;

/**
 * Created by MisterY on 16.12.2015.
 */
public class ChooserDialog extends Dialog{


    private OnChooseListener onChooseListener = new OnChooseListener() {
        @Override
        public void onChoose(int result) {
            Log.e("PickerDialog", "listener isn't set" );
        }
    };


    public ChooserDialog(Context ctx, final String dialogTitle, final String[] elementsList, final OnChooseListener onChooseListener) {
        super(ctx);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        //simple layout with one ListView
        setContentView(R.layout.list_chooser_dialog);

        // setting custom title bar
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.dialog_title_bar);

        //setting title
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(dialogTitle);

            //Saving listener
        this.onChooseListener = onChooseListener;

            //preparing List
        ListView listView = (ListView) findViewById(R.id.listView);

            //filling up list view
        ArrayAdapter adapter = new ArrayAdapter(ctx, R.layout.list_item_view, elementsList);
        listView.setAdapter(adapter);

            //listView listener will call our custom listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onChooseListener.onChoose(position);
                dismiss();
            }
        });

        //show this dialog to user
        show();
    }

    public interface OnChooseListener {//To support low coupling
        /**
         * Called when user clicks on the list item
         * @param result id of element in list which was choosen
         */
        public void onChoose(int result);
    }

}
