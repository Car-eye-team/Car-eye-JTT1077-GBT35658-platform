package org.careye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.careye.CarEyeClient.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.careye.adapter.TreeAdapter;
import org.careye.bll.PrefBiz;
import org.careye.model.DepartmentCar;
import org.careye.request.BaseRequest;
import org.careye.request.CmsRequest;
import org.careye.utils.Constants;
import org.careye.utils.DateUtil;
import org.careye.utils.MD5Util;
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
    private TreeAdapter adapter;
    private ListView listView;
    private EditText et_filter;
    private List<DepartmentCar> deptList = new ArrayList<>();
    private List<DepartmentCar> allList = new ArrayList<>();
    private List<DepartmentCar> onlineList = new ArrayList<>();
    private HashMap<String, DepartmentCar> deptMap = new HashMap<>();

    private PrefBiz prefBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        initView();
        getDeptAndCar();
        initData(null);
        addListener();
    }

    private void initView() {
        iv_back = findViewById(R.id.device_list_iv_back);
        btn_all = findViewById(R.id.device_list_btn_all);
        btn_online = findViewById(R.id.device_list_btn_online);
        btn_all.setSelected(true);
        adapter = new TreeAdapter(this, deptList, deptMap);
        listView =  findViewById(R.id.device_listview_device);
        listView.setAdapter(adapter);
        et_filter =  findViewById(R.id.et_filter);
        prefBiz = new PrefBiz(this);
    }

    private void searchAdapter(Editable s) {
        adapter.setKeyword(s.toString());
    }

    private void initData(List<DepartmentCar> list) {
        deptList.clear();
        if (list != null) {
            for (DepartmentCar departmentCar : list) {
                departmentCar.setExpand(false);
            }
            deptList.addAll(list);
            updateData();
        }
    }

    private void addListener(){
        iv_back.setOnClickListener(this);
        btn_all.setOnClickListener(this);
        btn_online.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DepartmentCar deptCar = (DepartmentCar) adapter.getItem(position);
                if (deptCar.getNodetype() == 2) {   //点击叶子节点
                    //处理回填
                    getTerminalList(deptCar);
                } else {  //如果点击的是父类
                    if (deptCar.isExpand()) {
                        for (DepartmentCar tempPoint : deptList) {
                            if (tempPoint.getParentId().equals(deptCar.getNodeId())) {
                                if (deptCar.getNodetype() == 1) {
                                    tempPoint.setExpand(false);
                                }
                            }
                        }
                        deptCar.setExpand(false);
                    } else {
                        deptCar.setExpand(true);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
        et_filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchAdapter(s);
            }
        });
    }

    private void updateData() {
        for (DepartmentCar departmentCar : deptList) {
            deptMap.put(departmentCar.getNodeId(), departmentCar);
        }
        Collections.sort(deptList, new Comparator<DepartmentCar>() {
            @Override
            public int compare(DepartmentCar lhs, DepartmentCar rhs) {
                int llevel = TreeUtils.getLevel(lhs, deptMap);
                int rlevel = TreeUtils.getLevel(rhs, deptMap);
                if (llevel == rlevel) {
                    if (lhs.getParentId().equals(rhs.getParentId())) {  //左边小
                        return lhs.getDISPLAY_ORDER() > rhs.getDISPLAY_ORDER() ? 1 : -1;
                    } else {  //如果父辈id不相等
                        //同一级别，不同父辈
                        DepartmentCar ltreePoint = TreeUtils.getDeptCar(lhs.getParentId(), deptMap);
                        DepartmentCar rtreePoint = TreeUtils.getDeptCar(rhs.getParentId(), deptMap);
                        return compare(ltreePoint, rtreePoint);  //父辈
                    }
                } else {  //不同级别
                    if (llevel > rlevel) {   //左边级别大       左边小
                        if (lhs.getParentId().equals(rhs.getNodeId())) {
                            return 1;
                        } else {
                            DepartmentCar lreasonTreePoint = TreeUtils.getDeptCar(lhs.getParentId(), deptMap);
                            return compare(lreasonTreePoint, rhs);
                        }
                    } else {   //右边级别大   右边小
                        if (rhs.getParentId().equals(lhs.getNodeId())) {
                            return -1;
                        }
                        DepartmentCar rreasonTreePoint = TreeUtils.getDeptCar(rhs.getParentId(), deptMap);
                        return compare(lhs, rreasonTreePoint);
                    }
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void getDeptAndCar() {
        Retrofit retrofit = BaseRequest.getInstance().getRetrofit();
        CmsRequest.DeptTree request = retrofit.create(CmsRequest.DeptTree.class);
        String tradeno = DateUtil.getTodayDate(DateUtil.df6);
        String sign = MD5Util.MD5Encode(prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, "") + prefBiz.getStringInfo(Constants.PREF_LOGIN_PW, "") + tradeno, "utf-8");
        JsonObject body = new JsonObject();
        body.addProperty("username", prefBiz.getStringInfo(Constants.PREF_LOGIN_NAME, ""));
        body.addProperty("tradeno", tradeno);
        body.addProperty("sign", sign);
        Call<JsonObject> call = request.deptTree(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resultObj = response.body();
                if (resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
                    JsonArray array = resultObj.getAsJsonArray("resultData");
                    JsonObject object;
                    DepartmentCar departmentCar;
                    for (int i = 0; i < array.size(); i++) {
                        departmentCar = new DepartmentCar();
                        object = array.get(i).getAsJsonObject();
                        departmentCar.setNodeId(object.get("nodeId").getAsString());
                        departmentCar.setParentId(object.get("parentId").getAsString());
                        departmentCar.setNodeName(object.get("nodeName").getAsString());
                        departmentCar.setNodetype(object.get("nodetype").getAsInt());
                        if (departmentCar.getNodetype() == 2) {
                            departmentCar.setCarstatus(object.get("carstatus").getAsInt());
                        }
                        if (departmentCar.getNodetype() == 1) {
                            departmentCar.setTotal(object.get("total").getAsInt());
                            departmentCar.setCaroffline(object.get("caroffline").getAsInt());
                            departmentCar.setLongoffline(object.get("longoffline").getAsInt());
                        }
                        departmentCar.setParent(object.get("parent").getAsBoolean());
                        departmentCar.setDISPLAY_ORDER(i);
                        allList.add(departmentCar);
                        if (departmentCar.getNodetype() == 1 || (departmentCar.getCarstatus() != 1 && departmentCar.getCarstatus() != 2 && departmentCar.getCarstatus() != 3)) {
                            onlineList.add(departmentCar);
                        }
                    }
                    initData(allList);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                String message = t.getMessage();
            }
        });
    }

    private void getTerminalList(final DepartmentCar departmentCar) {
        Retrofit retrofit = BaseRequest.getInstance().getRetrofit();
        CmsRequest.GetTerminalList request = retrofit.create(CmsRequest.GetTerminalList.class);
        String tradeno = DateUtil.getTodayDate(DateUtil.df6);
        String sign = MD5Util.MD5Encode("admin123456" + tradeno, "utf-8");
        JsonObject body = new JsonObject();
        body.addProperty("username", "admin");
        body.addProperty("tradeno", tradeno);
        body.addProperty("sign", sign);
        body.addProperty("carnumber", departmentCar.getNodeName());

        Call<JsonObject> call = request.getTerminalList(body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject resultObj = response.body();
                if (resultObj.has("errCode") && resultObj.get("errCode").getAsInt() == 0) {
                    JsonArray array = resultObj.getAsJsonArray("resultData");
                    List<String> terminalList = new ArrayList<>();
                    JsonObject object;
                    String terminal;
                    for (int i = 0; i < array.size(); i++) {
                        object = array.get(i).getAsJsonObject();
                        terminal = object.get("terminal").getAsString();
                        terminalList.add(terminal);
                    }
                    departmentCar.setTerminalList(terminalList);
                    Intent data = new Intent();
                    data.putExtra("device", departmentCar);
                    setResult(RESULT_OK, data);
                    CarTreeActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                String message = t.getMessage();
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
                    initData(allList);
                }
                break;
            case R.id.device_list_btn_online:
                if (!btn_online.isSelected()) {
                    btn_all.setSelected(false);
                    btn_online.setSelected(true);
                    initData(onlineList);
                }
                break;
        }
    }
}
