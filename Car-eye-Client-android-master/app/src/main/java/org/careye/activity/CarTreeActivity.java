package org.careye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import org.careye.utils.TreeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    private String keyword;
    private TreeAdapter adapter;

    private List<DepartmentCar> allCars = new ArrayList<>();
    private List<DepartmentCar> cars = new ArrayList<>();

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
            initData();
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
                } else {
                    // 如果点击的是父类
                    deptCar.setExpand(!deptCar.isExpand());

                    expandCategoryData();
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

                    initData();
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

                    isAll = true;
                    initData();
                }

                break;
            case R.id.device_list_btn_online:
                if (!btn_online.isSelected()) {
                    btn_all.setSelected(false);
                    btn_online.setSelected(true);

                    isAll = false;
                    initData();
                }

                break;
        }
    }

    private void search() {
        keyword = et_filter.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            setKeyword();
        }
    }

    private void initData() {
        allCars.clear();
        cars.clear();

        List<DepartmentCar> temp;
        if (isAll) {
            temp = CarApplication.allList;
        } else {
            temp = CarApplication.onlineList;
        }

        for (DepartmentCar category : temp) {
            CarApplication.deptMap.put(category.getNodeId(), category);

            category.setExpand(false);
            category.setShow(false);
            allCars.add(category);

            if (cars.size() == 0) {
                category.setShow(true);
                cars.add(category);
            }
        }

        adapter = new TreeAdapter(this, cars);
        listView.setAdapter(adapter);
    }

    /**
     * 展开数据
     */
    private void expandCategoryData() {
        cars.clear();

        for (int i = 0; i < allCars.size(); i++) {
            DepartmentCar category = allCars.get(i);

            DepartmentCar parent = TreeUtils.getDeptCar(category.getParentId(), CarApplication.deptMap);
            if (parent != null) {
                category.setShow(parent.isExpand());
                if (!category.isShow() && category.getNodetype() == 1) {
                    category.setExpand(false);
                }
            }

            if (category.isShow()) {
                cars.add(category);
            }
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * 搜索的时候，先关闭所有的条目，然后，按照条件，找到含有关键字的数据
     * 如果是叶子节点，
     */
    public void setKeyword() {
        allCars.clear();
        cars.clear();

        List<DepartmentCar> temp;
        if (isAll) {
            temp = CarApplication.allList;
        } else {
            temp = CarApplication.onlineList;
        }

        for (DepartmentCar category : temp) {
            category.setExpand(false);
            category.setShow(false);
            allCars.add(category);
        }

        Iterator it = allCars.iterator();
        while (it.hasNext()) {
            DepartmentCar category = (DepartmentCar) it.next();

            if (category.getNodetype() == 2 && category.getNodeName().contains(keyword)) {
                category.setShow(true);
                // 展开从最顶层到该点的所有节点
                openExpand(category);
            }
        }

        for (DepartmentCar category : allCars) {
            if (category.isShow()) {
                cars.add(category);
            }
        }

        adapter.setKeyword(keyword);
    }

    /**
     * 从DepartmentCar开始一直展开到顶部
     *
     * @param cate
     */
    private void openExpand(DepartmentCar cate) {
        if (cate.getParentId().isEmpty()) {
            cate.setExpand(true);
            cate.setShow(true);
        } else {
            DepartmentCar item = CarApplication.deptMap.get(cate.getParentId());
            if (item != null) {
                item.setExpand(true);
                item.setShow(true);
                openExpand(item);
            } else {
                Log.i("", "");
            }
        }
    }
}
