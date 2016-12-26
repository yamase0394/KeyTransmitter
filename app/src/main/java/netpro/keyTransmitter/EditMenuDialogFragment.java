package netpro.keyTransmitter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EditMenuDialogFragment extends android.support.v4.app.DialogFragment {

    private OnListItemClickListener listener;

    public static EditMenuDialogFragment newInstance(int position, String title, String message) {
        EditMenuDialogFragment fragment = new EditMenuDialogFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("title", title);
        args.putString("message", message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.dialog_fragment_edit_menu, null);
        final String[] items = {"編集", "削除"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items);
        final ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listener.onListItemClicked(getArguments().getInt("position"), (String) listView.getItemAtPosition(i));
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title")).setMessage(getArguments().getString("message")).setView(view);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();

        Dialog dialog = getDialog();
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();

        //display metricsでdpのもと(?)を作る
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //LayoutParamsにdpを計算して適用(今回は横幅300dp)(※metrics.scaledDensityの返り値はfloat)
        float dialogWidth = 300 * metrics.scaledDensity;
        layoutParams.width = (int) dialogWidth;

        //LayoutParamsをセットする
        dialog.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    public interface OnListItemClickListener {
        void onListItemClicked(int positionm, String selectedStr);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListItemClickListener) {
            listener = (OnListItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListItemClickListener");
        }
    }
}
