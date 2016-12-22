package netpro.keyTransmitter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class AddKeyDIalogFragment extends android.support.v4.app.DialogFragment {

    private OnKeyGeneratedListener listener;
    private List<View> keyCodeViewList = new ArrayList<>();

    public static AddKeyDIalogFragment newInstance() {
        AddKeyDIalogFragment fragment = new AddKeyDIalogFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final View view = View.inflate(getActivity(), R.layout.dialog_fragment_add_key, null);

        final TextInputLayout intervalLayout = (TextInputLayout) view.findViewById(R.id.intervalLayout);

        final Spinner columnCountSpinner = (Spinner) view.findViewById(R.id.columnCountSpinner);
        Integer[] columnCounts = {1, 2, 3, 4, 5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, columnCounts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        columnCountSpinner.setAdapter(adapter);
        columnCountSpinner.setSelection(0);

        final Spinner rowCountSpinner = (Spinner) view.findViewById(R.id.rowCountSpinner);
        Integer[] rowCounts = {1, 2, 3, 4};
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, rowCounts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rowCountSpinner.setAdapter(adapter);
        rowCountSpinner.setSelection(0);

        final Spinner keyTypeSpinner = (Spinner) view.findViewById(R.id.keyTypeSpinner);
        Key.Type[] keyTypes = Key.Type.values();
        String[] keyDescriptions = new String[keyTypes.length];
        for (int i = 0; i < keyDescriptions.length; i++) {
            keyDescriptions[i] = keyTypes[i].getDescription();
        }
        ArrayAdapter<String> keyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, keyDescriptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keyTypeSpinner.setAdapter(keyAdapter);
        keyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Spinner spinner = (Spinner) adapterView;
                String description = (String) spinner.getSelectedItem();
                switch (Key.Type.toType(description)) {
                    case PRESSING:
                        intervalLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        intervalLayout.setVisibility(GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        keyTypeSpinner.setSelection(0);

        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.keyCodesLayout);

        final Button addButton = (Button) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //Spinnerに入れるキーコードの種類を選択させるダイアログを生成
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final String[] items = {"アルファベット", "数字", "制御キー", "ファンクションキー", "記号"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View addKeyCodeView = View.inflate(getActivity(), R.layout.layout_add_key_code, null);

                        Spinner spinner = (Spinner) addKeyCodeView.findViewById(R.id.keyCodeSpinner);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
                        //選択結果に応じてSpinnerの選択肢を変える
                        switch (which) {
                            //アルファベット
                            case 0:
                                final int ALPHABET_SIZE = 'Z' - 'A';
                                char alphabet = 'A';
                                for (int i = 0; i <= ALPHABET_SIZE; i++) {
                                    adapter.add(String.valueOf(alphabet++));
                                }
                                break;
                            //数字
                            case 1:
                                for (int i = 0; i < 10; i++) {
                                    adapter.add(String.valueOf(i));
                                }
                                break;
                            //制御キー
                            case 2:
                                adapter.addAll("BackSpace", "Enter", "Shift", "Ctrl", "Alt", "Pause", "Space", "PageUp" + "PageDown", "End", "Home", "←", "↑", "→", "↓", "PrintScreen", "Insert", "Delete", "Win", "NumLock", "ScrollLock", "Esc", "Tab");
                                break;
                            //ファンクションキー
                            case 3:
                                for (int i = 1; i <= 12; i++) {
                                    adapter.add("F" + String.valueOf(i));
                                }
                                break;
                            //記号
                            case 4:
                                adapter.addAll(":*", ";+", ",<", "-=", ".>", "/?", "@`", "[{", "\\|", "]}", "^~", "\\_");
                                break;
                        }
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setSelection(0);

                        Button removeButton = (Button) addKeyCodeView.findViewById(R.id.removeButton);
                        removeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewGroup parent = (ViewGroup) view.getParent();
                                keyCodeViewList.remove(parent);
                                parent.removeAllViews();
                            }
                        });
                        linearLayout.addView(addKeyCodeView);
                        keyCodeViewList.add(addKeyCodeView);
                    }
                });
                builder.show();
            }
        });

        Button createButton = (Button) view.findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                try {
                    EditText editName = (EditText) view.findViewById(R.id.name);
                    String name = editName.getText().toString();
                    EditText editDescription = (EditText) view.findViewById(R.id.description);
                    String description = editDescription.getText().toString();

                    if (name != null && description != null && keyCodeViewList.size() > 0) {
                        int columnCount = (int) columnCountSpinner.getSelectedItem();
                        int rowCount = (int) rowCountSpinner.getSelectedItem();

                        Key.Type type = Key.Type.toType((String) keyTypeSpinner.getSelectedItem());
                        Key key = new EmptyKey();
                        switch (type) {
                            case RELEASED:
                                key = new NormalKey(columnCount, rowCount, name, description, type);
                                break;
                            case LONGPRESS:
                                key = new LongPressKey(columnCount, rowCount, name, description, type);
                                break;
                            case PRESSING:
                                EditText editInterval = (EditText) view.findViewById(R.id.interval);
                                int interval = Integer.parseInt(editInterval.getText().toString());
                                key = new PressingKey(columnCount, rowCount, name, description, type, interval);
                                break;
                            case EMPTY:
                                key = new EmptyKey(columnCount, rowCount, name, description, type);
                                break;
                        }
                        for (View v : keyCodeViewList) {
                            Spinner spinner = (Spinner) v.findViewById(R.id.keyCodeSpinner);
                            key.addKeyCode((String) spinner.getSelectedItem());
                        }
                        listener.onKeyGenerated(key);
                        dismiss();
                        return;
                    }

                } catch (NumberFormatException e) {
                } catch (NullPointerException e) {
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("エラー").setMessage("入力が不完全です").setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });

        Button cacelButton = (Button) view.findViewById(R.id.cancelButton);
        cacelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("キー生成").setView(view);
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

    public interface OnKeyGeneratedListener {
        public void onKeyGenerated(Key key);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnKeyGeneratedListener) {
            listener = (OnKeyGeneratedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnKeyGeneratedListener");
        }
    }
}
