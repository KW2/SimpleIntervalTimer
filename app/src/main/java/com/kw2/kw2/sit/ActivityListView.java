package com.kw2.kw2.sit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by SAMSUNG on 2018-01-21.
 */

public class ActivityListView extends Activity implements com.kw2.kw2.sit.ListAdapter.ListBtnClickListener, RefreshOnClick {
    DBHelper helper;
    ArrayList<ListViewItem> items;
    com.kw2.kw2.sit.ListAdapter adapter;
    TextView blankText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);


        items = new ArrayList<ListViewItem>();

        // DB 생성
        helper = new DBHelper(ActivityListView.this);

        items.addAll(helper.getItems());

        adapter = new com.kw2.kw2.sit.ListAdapter(ActivityListView.this, R.layout.listview_item, items, this);

        blankText = (TextView) findViewById(R.id.list_text);

        BootstrapButton plusBtn = (BootstrapButton) findViewById(R.id.list_plusBtn);

        ListView listView = (ListView) findViewById(R.id.list_list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ActivityListView.this, ActivityTimer.class);
                intent.putExtra("id", items.get(position).getId());
                startActivity(intent);
            }

        });
        if (items.size() == 0) {
            blankText.setText(R.string.list_text);
        }


        // 추가 버튼 클릭
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog cstDialog = DialogMaker.newDialog(ActivityListView.this, -1, ActivityListView.this);

                cstDialog.show();
            }
        });


    }

    @Override
    public void onListBtnClick(int id) {
        final int recordId = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alert;
        builder.setTitle(R.string.delete_title)
                .setMessage(R.string.delete_body)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 데이터베이스 삭제
                                helper.deleteTime(recordId);
                                Toast.makeText(getApplication(), R.string.delete_ok, Toast.LENGTH_SHORT).show();

                                // 해당 값 items에서 삭제
                                for (Iterator<ListViewItem> it = items.iterator(); it.hasNext(); ) {
                                    ListViewItem item = it.next();
                                    if (recordId == item.getId()) {
                                        it.remove();
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                Log.d("test delete", "delete On");
                                if (items.size() == 0) {
                                    blankText.setText(R.string.list_text);
                                }

                            }
                        })
                .setNegativeButton(R.string.cancle,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
        alert = builder.create();
        alert.show();
    }


    @Override
    public void onRefreshClick(ListViewItem item) {
        items.add(item);

        blankText.setText(" ");

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        DBHelper helper = new DBHelper(ActivityListView.this);
        items.clear();
        items.addAll(helper.getItems());
        adapter.notifyDataSetChanged();
    }
}

class DialogMaker {
    public static Dialog newDialog(final Context context, final int dbId, final RefreshOnClick refreshOnClick) {
        final DBHelper helper = new DBHelper(context);
        final Dialog cstDialog = new Dialog(context);
        cstDialog.setContentView(R.layout.dialog_datasetting);
        WindowManager.LayoutParams params = cstDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        cstDialog.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        final TextView dialogTitle = (TextView) cstDialog.findViewById(R.id.input_title);

        final EditText timeName = (EditText) cstDialog.findViewById(R.id.input_timeName);

        BootstrapButton setsMinBtn = (BootstrapButton) cstDialog.findViewById(R.id.setsMinBtn);
        BootstrapButton setsPlsBtn = (BootstrapButton) cstDialog.findViewById(R.id.setsPlsBtn);

        BootstrapButton wtMinBtn = (BootstrapButton) cstDialog.findViewById(R.id.wtMinBtn);
        BootstrapButton wtPlsBtn = (BootstrapButton) cstDialog.findViewById(R.id.wtPlsBtn);

        BootstrapButton rtMinBtn = (BootstrapButton) cstDialog.findViewById(R.id.rtMinBtn);
        BootstrapButton rtPlsBtn = (BootstrapButton) cstDialog.findViewById(R.id.rtPlsBtn);

        final EditText setsNum = (EditText) cstDialog.findViewById(R.id.setsNum);

        final EditText wtMNum = (EditText) cstDialog.findViewById(R.id.wtMNum);
        final EditText wtSNum = (EditText) cstDialog.findViewById(R.id.wtSNum);

        final EditText rtMNum = (EditText) cstDialog.findViewById(R.id.rtMNum);
        final EditText rtSNum = (EditText) cstDialog.findViewById(R.id.rtSNum);

        BootstrapButton cancelBtn = (BootstrapButton) cstDialog.findViewById(R.id.input_cancle);
        final BootstrapButton saveBtn = (BootstrapButton) cstDialog.findViewById(R.id.input_save);


        View.OnClickListener setsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setNumStr = setsNum.getText().toString();
                try {
                    int setNumInt = Integer.parseInt(setNumStr);

                    switch (view.getId()) {
                        case R.id.setsMinBtn:
                            if (setNumInt != 1 && setNumInt != 0) {
                                setNumInt--;
                            }
                            setsNum.setText(String.valueOf(setNumInt));
                            break;
                        case R.id.setsPlsBtn:
                            setNumInt++;
                            setsNum.setText(String.valueOf(setNumInt));
                            break;
                    }
                } catch (Exception e) {
                    setsNum.setText("1");
                }
            }
        };

        View.OnClickListener wtListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check;
                int remainder;
                String wtMStr = wtMNum.getText().toString();
                String wtSStr = wtSNum.getText().toString();

                try {
                    int wtMInt = Integer.parseInt(wtMStr);
                    int wtSInt = Integer.parseInt(wtSStr);

                    switch (view.getId()) {
                        case R.id.wtMinBtn:
                            // option == 0 이면 1초에서 안떨어진다.
                            check = DialogMaker.testMinInt(wtMInt, wtSInt, 0);
                            if (check.equals("down")) {
                                wtMInt--;
                                wtSInt = 59;
                            } else if (check.equals("p")) {
                                wtSInt--;
                            } else if (check.equals("one")) {
                                wtSInt = 1;
                            }
                            wtMNum.setText(DialogMaker.twoStrForm(wtMInt));
                            wtSNum.setText(DialogMaker.twoStrForm(wtSInt));
                            break;
                        case R.id.wtPlsBtn:
                            remainder = DialogMaker.testPlsInt(wtMInt, wtSInt);
                            if (remainder == -1) {
                                wtSInt++;
                            } else if (remainder == -2) {
                                wtSInt++;
                            } else if (remainder != -3) {
                                wtSInt = remainder;
                                if (wtMInt != 99) {
                                    wtMInt++;
                                }
                            }
                            wtMNum.setText(DialogMaker.twoStrForm(wtMInt));
                            wtSNum.setText(DialogMaker.twoStrForm(wtSInt));
                            break;
                    }
                } catch (Exception e) {
                    wtMNum.setText("00");
                    wtSNum.setText("01");
                }
            }
        };

        View.OnClickListener rtListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check;
                int remainder;
                String rtMStr = rtMNum.getText().toString();
                String rtSStr = rtSNum.getText().toString();

                try {
                    int rtMInt = Integer.parseInt(rtMStr);
                    int rtSInt = Integer.parseInt(rtSStr);

                    switch (view.getId()) {
                        case R.id.rtMinBtn:
                            // option == 1 이면 0초에서 안떨어진다.
                            check = DialogMaker.testMinInt(rtMInt, rtSInt, 1);
                            if (check.equals("down")) {
                                rtMInt--;
                                rtSInt = 59;
                            } else if (check.equals("p")) {
                                rtSInt--;
                            }
                            rtMNum.setText(DialogMaker.twoStrForm(rtMInt));
                            rtSNum.setText(DialogMaker.twoStrForm(rtSInt));
                            break;
                        case R.id.rtPlsBtn:
                            remainder = DialogMaker.testPlsInt(rtMInt, rtSInt);
                            if (remainder == -1) {
                                rtSInt++;
                            } else if (remainder == -2) {
                                rtSInt++;
                            } else if (remainder != -3) {
                                rtSInt = remainder;
                                if (rtMInt != 99) {
                                    rtMInt++;
                                }
                            }
                            rtMNum.setText(DialogMaker.twoStrForm(rtMInt));
                            rtSNum.setText(DialogMaker.twoStrForm(rtSInt));
                            break;
                    }
                } catch (Exception e) {
                    rtMNum.setText("00");
                    rtSNum.setText("00");
                }
            }
        };

        setsMinBtn.setOnClickListener(setsListener);
        setsPlsBtn.setOnClickListener(setsListener);

        wtMinBtn.setOnClickListener(wtListener);
        wtPlsBtn.setOnClickListener(wtListener);

        rtMinBtn.setOnClickListener(rtListener);
        rtPlsBtn.setOnClickListener(rtListener);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cstDialog.dismiss();
            }
        });

        // set edittext 포커스변화리스너
        setsNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == false) {
                    if (setsNum.getText().toString().trim().equals("") || Integer.parseInt(setsNum.getText().toString()) == 0) {
                        setsNum.setText("1");
                    }
                }
            }
        });

        // 분 edittext 포커스변화리스너
        View.OnFocusChangeListener minChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == false) {
                    if (((EditText) view).getText().toString().trim().equals("")) {
                        ((EditText) view).setText("00");
                    }else{
                        ((EditText) view).setText(twoStrForm(Integer.parseInt(((EditText) view).getText().toString())));
                    }

                }
            }
        };

        wtMNum.setOnFocusChangeListener(minChangeListener);
        rtMNum.setOnFocusChangeListener(minChangeListener);

        // wroktime 초 포커스변화리스너
        wtSNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == false) {
                    if (wtSNum.getText().toString().trim().equals("") || Integer.parseInt(wtSNum.getText().toString()) == 0) {
                        if(Integer.parseInt(wtMNum.getText().toString()) == 0) {
                            wtSNum.setText("01");
                        }else{
                            wtSNum.setText("00");
                        }
                    } else if (Integer.parseInt(wtSNum.getText().toString()) >= 60) {

                        int wtMInt = Integer.parseInt(wtMNum.getText().toString()) + (Integer.parseInt(wtSNum.getText().toString()) / 60);
                        int wtSInt = Integer.parseInt(wtSNum.getText().toString()) % 60;

                        wtMNum.setText(twoStrForm(wtMInt));
                        wtSNum.setText(twoStrForm(wtSInt));

                    } else{
                        wtSNum.setText(twoStrForm(Integer.parseInt(wtSNum.getText().toString())));
                    }
                }
            }
        });

        // resttime 초 포커스변화리스너
        rtSNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == false) {
                    if (rtSNum.getText().toString().trim().equals("") || Integer.parseInt(rtSNum.getText().toString()) == 0) {
                        rtSNum.setText("00");
                    } else if (Integer.parseInt(rtSNum.getText().toString()) >= 60) {

                        int rtMInt = Integer.parseInt(rtMNum.getText().toString()) + (Integer.parseInt(rtSNum.getText().toString()) / 60);
                        int rtSInt = Integer.parseInt(rtSNum.getText().toString()) % 60;

                        rtMNum.setText(twoStrForm(rtMInt));
                        rtSNum.setText(twoStrForm(rtSInt));

                    } else{
                        rtSNum.setText(twoStrForm(Integer.parseInt(rtSNum.getText().toString())));
                    }
                }
            }
        });

        // 추가 작업
        if (dbId == -1) {

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveBtn.setFocusableInTouchMode(true);
                    saveBtn.requestFocus();
                    // timer 주가 작업
                    String timeNameStr = timeName.getText().toString();
                    int setInt = Integer.parseInt(setsNum.getText().toString());
                    String wkTimeStr = wtMNum.getText().toString() + ":" + wtSNum.getText().toString();
                    String rtTimeStr = rtMNum.getText().toString() + ":" + rtSNum.getText().toString();

                    if(timeNameStr.length() >= 30){
                        Toast.makeText(context, R.string.timeName_30,Toast.LENGTH_SHORT).show();
                        saveBtn.setFocusableInTouchMode(false);
                        saveBtn.clearFocus();
                    }else {
                        helper.insertTime(timeNameStr, setInt, wkTimeStr, rtTimeStr);

                        ListViewItem item = new ListViewItem();
                        item.setId(helper.getInsertId());
                        item.setTimeName(timeNameStr);
                        item.setSetNum(setInt);
                        item.setWorkTime(wkTimeStr);
                        item.setRestTime(rtTimeStr);

                        Toast.makeText(context, R.string.insert_ok, Toast.LENGTH_SHORT).show();
                        cstDialog.dismiss();

                        refreshOnClick.onRefreshClick(item);
                    }
                }
            });

        } else { // 수정 작업
            dialogTitle.setText(R.string.input_updateTitle);
            saveBtn.setText(R.string.update);

            ListViewItem item = helper.getItem(dbId);

            timeName.setText(item.getTimeName());
            timeName.setSelection(item.getTimeName().length());
            setsNum.setText(String.valueOf(item.getSetNum()));

            wtMNum.setText(item.getWorkTime().substring(0, 2));
            wtSNum.setText(item.getWorkTime().substring(3));

            rtMNum.setText(item.getRestTime().substring(0, 2));
            rtSNum.setText(item.getRestTime().substring(3));


            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveBtn.setFocusableInTouchMode(true);
                    saveBtn.requestFocus();
                    // timer 수정 작업
                    String timeNameStr = timeName.getText().toString();
                    int setInt = Integer.parseInt(setsNum.getText().toString());
                    String wkTimeStr = wtMNum.getText().toString() + ":" + wtSNum.getText().toString();
                    String rtTimeStr = rtMNum.getText().toString() + ":" + rtSNum.getText().toString();

                    if(timeNameStr.length() >= 30){
                        Toast.makeText(context, R.string.timeName_30,Toast.LENGTH_SHORT).show();
                        saveBtn.setFocusableInTouchMode(false);
                        saveBtn.clearFocus();
                    }else {
                        helper.updateTime(dbId, timeNameStr, setInt, wkTimeStr, rtTimeStr);

                        Toast.makeText(context, R.string.update_ok, Toast.LENGTH_SHORT).show();
                        cstDialog.dismiss();
                    }

                }
            });

        }

        return cstDialog;

    }

    public static String testMinInt(int mInt, int sInt, int option) {
        String check = "p";

        if (mInt != 0 && sInt == 0) {
            check = "down";
        } else if (mInt == 0 && sInt == 1 && option == 0) {
            check = "stop";
        } else if (mInt == 0 && sInt == 0 && option == 0) {
            check = "one";
        } else if (mInt == 0 && sInt == 0 && option == 1) {
            check = "zero";
        }

        return check;
    }

    public static int testPlsInt(int mInt, int sInt) {
        int remainder = -1;

        if (mInt == 99 && sInt == 99) {
            remainder = -3;
        } else if (mInt == 99 && sInt >= 59) {
            remainder = -2;
        } else if (sInt >= 59) {
            remainder = (sInt + 1) - 60;
        }

        return remainder;
    }

    public static String twoStrForm(int formInt) {
        String str = String.valueOf(formInt);
        if (str.length() == 1) {
            str = "0" + str;
        }
        return str;
    }


}

