//package com.rentian.rtsxy.common.view;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.AppCompatEditText;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.rentian.rentianoa.MyMSG;
//import com.rentian.rentianoa.R;
//import com.rentian.rentianoa.common.constant.Const;
//import com.rentian.rentianoa.common.utils.BlurBehind;
//import com.rentian.rentianoa.modules.communication.bean.EmployeeInfo;
//import com.rentian.rentianoa.modules.communication.view.ContactDetailsActivity;
//
//import java.util.ArrayList;
//
//public class SearchActivity extends AppCompatActivity {
//
//    private AppCompatEditText editText;
//    private ListView searchListView;
//
//    private SearchAdapter adapter;
//
//    private ArrayList<EmployeeInfo> contact;
//    private ArrayList<EmployeeInfo> searchContact;
//
//    private int[] colors = {R.drawable.head_image_0, R.drawable.head_image_1
//            , R.drawable.head_image_2, R.drawable.head_image_3
//            , R.drawable.head_image_4, R.drawable.head_image_5
//            , R.drawable.head_image_6};
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//        BlurBehind.getInstance()
//                .withAlpha(100)
//                .setBackground(this);
//        contact = MyMSG.Instance().getEmployeeInfo();
//        initEditText();
//        adapter = new SearchAdapter();
//        searchContact = new ArrayList<>();
//        searchListView = (ListView) findViewById(R.id.search_lv);
//        searchListView.setAdapter(adapter);
//        clickListener();
//    }
//
//    private void initEditText() {
//        editText = (AppCompatEditText) findViewById(R.id.search_edit);
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                searchContact.clear();
//                if ("".equals(s.toString())) {
//                    if (adapter != null) {
//                        adapter.notifyDataSetChanged();
//                    }
//                    return;
//                }
//                for (EmployeeInfo data : contact) {
//                    if (data.getName().contains(s) || data.getPinyin().contains(s)) {
//                        searchContact.add(data);
//                    }
//                }
//                if (adapter != null) {
//                    adapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
//
//    private void clickListener() {
//        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(SearchActivity.this, ContactDetailsActivity.class);
//                EmployeeInfo temp = searchContact.get(position);
//                Bundle bundle = new Bundle();
//                bundle.putString("name", temp.getName());
//                bundle.putString("tel", temp.getTel());
//                bundle.putString("phone", temp.getPhone());
//                bundle.putString("email", temp.getEmail());
//                bundle.putString("addr", temp.getAddr());
//                bundle.putString("img", temp.getImg());
//                bundle.putString("dept", temp.getDept());
//                bundle.putInt("uid", temp.getUid());
//                bundle.putString("position", temp.getPosition());
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//    }
//
//    class SearchAdapter extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return searchContact.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (null == convertView) {
//                holder = new ViewHolder();
//                convertView = LayoutInflater
//                        .from(SearchActivity.this)
//                        .inflate(R.layout.item_search, parent, false);
//                holder.img = (CircleImageView) convertView
//                        .findViewById(R.id.search_iv);
//                holder.imgTv = (TextView) convertView
//                        .findViewById(R.id.search_iv_tv);
//                holder.name = (TextView) convertView
//                        .findViewById(R.id.search_tv);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            if (searchContact.get(position).getImg().length() > 0) {
//                holder.imgTv.setText("");
//                String imageUrl = Const.RTOA.URL_BASE + searchContact.get(position).getImg();
//                Glide.with(SearchActivity.this)
//                        .load(imageUrl)
//                        .into(holder.img);
//            } else {
//                int lottery = searchContact.get(position).getUid() % 7;
//                Glide.with(SearchActivity.this)
//                        .load(colors[lottery])
//                        .into(holder.img);
//                String name = searchContact.get(position).getName();
//                holder.imgTv.setText(name.substring(name.length() - 1));
//            }
//            holder.name.setText(searchContact.get(position).getName());
//            return convertView;
//        }
//
//        class ViewHolder {
//            CircleImageView img;
//            TextView imgTv, name;
//        }
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.intent_search, R.anim.intent_search_out);
//    }
//
//    public void onFinish(View view) {
//        finish();
//    }
//}
