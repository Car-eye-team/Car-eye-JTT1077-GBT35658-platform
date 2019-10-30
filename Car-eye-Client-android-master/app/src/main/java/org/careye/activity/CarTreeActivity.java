package org.careye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.careye.CarEyeClient.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.careye.CarApplication;
import org.careye.adapter.TreeAdapter;
import org.careye.bll.PrefBiz;
import org.careye.model.DepartmentCar;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.GsonUtil;
import org.careye.utils.MD5Util;
import org.careye.utils.Tools;
import org.careye.utils.TreeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 车辆树界面
 */
public class CarTreeActivity extends AppCompatActivity implements OnClickListener {
    public final static String TAG = "CarTreeActivity";

    private ImageView iv_back;
    private Button btn_all, btn_online;
    private ListView listView;
    private EditText et_filter;
    private TextView et_filter_tv;

    private List<DepartmentCar> deptList = new ArrayList<>();
    private TreeAdapter adapter;

    private PrefBiz prefBiz;
    private boolean isAll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_tree);

        initView();
        addListener();

        int size = CarApplication.allList.size();
        if (size == 0) {
            getDeptAndCar();
        } else {
            new Handler().postDelayed(new Runnable(){
                public void run() {
                    showAllList();
                }

            }, 500);
        }
    }

    private void initView() {
        iv_back = findViewById(R.id.device_list_iv_back);
        btn_all = findViewById(R.id.device_list_btn_all);
        btn_online = findViewById(R.id.device_list_btn_online);
        listView = findViewById(R.id.device_listview_device);
        et_filter = findViewById(R.id.et_filter);
        et_filter_tv = findViewById(R.id.et_filter_tv);

        btn_all.setSelected(true);

        adapter = new TreeAdapter(this, deptList, CarApplication.deptMap);
        listView.setAdapter(adapter);

        prefBiz = new PrefBiz(this);
    }

    private void addListener(){
        iv_back.setOnClickListener(this);
        btn_all.setOnClickListener(this);
        btn_online.setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DepartmentCar deptCar = (DepartmentCar) adapter.getItem(position);

                if (deptCar.getNodetype() == 2) {   // 点击叶子节点
                    Intent data = new Intent();
                    data.putExtra("device", deptCar);
                    setResult(RESULT_OK, data);
                    CarTreeActivity.this.finish();
                } else {  // 如果点击的是父类
                    if (deptCar.isExpand()) {
                        deptCar.setExpand(false);
                    } else {
                        deptCar.setExpand(true);
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });

        et_filter_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        et_filter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();

                return true;
            }
        });
    }

    private void search() {
        if (isAll) {
            showAllList();
        } else {
            showOnlineList();
        }

        String keyword = et_filter.getText().toString();
        adapter.setKeyword(keyword);
    }

    private void initData(List<DepartmentCar> list) {
        deptList.clear();

        if (list != null) {
            deptList.addAll(list);

            try {
                updateData();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateData() {
//        Collections.sort(deptList, new Comparator<DepartmentCar>() {
//            @Override
//            public int compare(DepartmentCar lhs, DepartmentCar rhs) {
//                int leftLevel = TreeUtils.getLevel(lhs, CarApplication.deptMap);
//                int rightLevel = TreeUtils.getLevel(rhs, CarApplication.deptMap);
//
//                if (leftLevel == rightLevel) {
//                    if (lhs.getParentId().equals(rhs.getParentId())) {  //左边小
//                        return lhs.getDISPLAY_ORDER() > rhs.getDISPLAY_ORDER() ? 1 : -1;
//                    } else {  // 如果父辈id不相等
//                        // 同一级别，不同父辈
//                        DepartmentCar ltreePoint = TreeUtils.getDeptCar(lhs.getParentId(), CarApplication.deptMap);
//                        DepartmentCar rtreePoint = TreeUtils.getDeptCar(rhs.getParentId(), CarApplication.deptMap);
//                        return compare(ltreePoint, rtreePoint);  //父辈
//                    }
//                } else {  // 不同级别
//                    if (leftLevel > rightLevel) {   // 左边级别大, 左边小
//                        if (lhs.getParentId().equals(rhs.getNodeId())) {
//                            return 1;
//                        } else {
//                            DepartmentCar lreasonTreePoint = TreeUtils.getDeptCar(lhs.getParentId(), CarApplication.deptMap);
//                            return compare(lreasonTreePoint, rhs);
//                        }
//                    } else {   // 右边级别大, 右边小
//                        if (rhs.getParentId().equals(lhs.getNodeId())) {
//                            return -1;
//                        }
//
//                        DepartmentCar reasonTreePoint = TreeUtils.getDeptCar(rhs.getParentId(), CarApplication.deptMap);
//                        return compare(lhs, reasonTreePoint);
//                    }
//                }
//            }
//        });

        adapter.notifyDataSetChanged();
    }

    private void getDeptAndCar() {
        Retrofit retrofit = BaseRequest.getInstance().getRetrofit();
        CmsRequest.DeptTree request = retrofit.create(CmsRequest.DeptTree.class);

        String tradeno = DateUtil.getTodayDate(DateUtil.df6);
        String name = prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "");
        String pwd = prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "");
        String sign = MD5Util.MD5Encode(name + pwd + tradeno, "utf-8");

        JsonObject body = new JsonObject();
        body.addProperty("username", name);
        body.addProperty("tradeno", tradeno);
        body.addProperty("sign", sign);

        Call<JsonObject> call = request.deptTree(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resultObj = response.body();

                if (resultObj != null && resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
                    JsonArray array = resultObj.getAsJsonArray("resultData");

                    List<DepartmentCar> allList = new ArrayList<>();
                    List<DepartmentCar> onlineList = new ArrayList<>();
                    HashMap<String, DepartmentCar> deptMap = new HashMap<>();

                    for (int i = 0; i < array.size(); i++) {
                        DepartmentCar departmentCar = GsonUtil.jsonToBean(array.get(i).toString(), DepartmentCar.class);

                        deptMap.put(departmentCar.getNodeId(), departmentCar);
                        allList.add(departmentCar);

                        if (departmentCar.getNodetype() == 1 ||
                                (departmentCar.getCarstatus() != 1
                                        && departmentCar.getCarstatus() != 2
                                        && departmentCar.getCarstatus() != 3)) {
                            onlineList.add(departmentCar);
                        }
                    }

                    CarApplication.deptMap.clear();
                    CarApplication.allList.clear();
                    CarApplication.onlineList.clear();

                    CarApplication.deptMap.putAll(deptMap);
                    CarApplication.allList.addAll(allList);
                    CarApplication.onlineList.addAll(onlineList);

                    initData(CarApplication.allList);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.device_list_iv_back:
                finish();
                break;
            case R.id.device_list_btn_all:
                if (!btn_all.isSelected()) {
                    btn_all.setSelected(true);
                    btn_online.setSelected(false);

                    showAllList();
                }

                break;
            case R.id.device_list_btn_online:
                if (!btn_online.isSelected()) {
                    btn_all.setSelected(false);
                    btn_online.setSelected(true);

                    showOnlineList();
                }

                break;
        }
    }

    private void showAllList() {
        for (DepartmentCar departmentCar : CarApplication.allList) {
            departmentCar.setExpand(false);
            CarApplication.deptMap.put(departmentCar.getNodeId(), departmentCar);
        }

        isAll = true;
        initData(CarApplication.allList);
    }

    private void showOnlineList() {
        for (DepartmentCar departmentCar : CarApplication.onlineList) {
            departmentCar.setExpand(false);
            CarApplication.deptMap.put(departmentCar.getNodeId(), departmentCar);
        }

        isAll = false;
        initData(CarApplication.onlineList);
    }
}
