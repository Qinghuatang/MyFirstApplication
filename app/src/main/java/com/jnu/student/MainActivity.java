package com.jnu.student;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jnu.student.data.Book;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int MENU_ID_ADD = 1;
    public static final int MENU_ID_DELETE = 2;
    public static final int MENU_ID_UPDATE = 3;

    private ActivityResultLauncher<Intent> addItemLauncher;
    private ActivityResultLauncher<Intent> updateItemLauncher;
    private ArrayList<Book> bookItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment6);

        RecyclerView recycle_view_books = findViewById(R.id.recycle_view_books);
        recycle_view_books.setLayoutManager(new LinearLayoutManager(this)); // 设置布局管理器

        bookItems = new ArrayList<>();
        bookItems.add(new Book("软件项目管理案例教程（第四版）", R.drawable.book_2));
        bookItems.add(new Book("创新工程实践", R.drawable.book_no_name));
        bookItems.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));

        RecycleViewBookAdapater adapter = new RecycleViewBookAdapater(bookItems);
        recycle_view_books.setAdapter(adapter);

        registerForContextMenu(recycle_view_books);     // 创建场景菜单事件


        addItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(null != result){
                        Intent data = result.getData();
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bundle bundle = data.getExtras();
                            // 处理返回的数据
                            String name = bundle.getString("name");    // 获取返回的数据
                            bookItems.add(new Book(name, R.drawable.book_no_name));
                            adapter.notifyItemInserted(bookItems.size());
                        }
                    }
                });

        updateItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(null != result){
                        Intent data = result.getData();
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Bundle bundle = data.getExtras();

                            String title = bundle.getString("name");    // 获取返回的数据
                            int position = bundle.getInt("position");
                            bookItems.get(position).setTitle(title);
                            adapter.notifyItemChanged(position);
                        }
                    }
                });
    }

    public static class RecycleViewBookAdapater extends RecyclerView.Adapter<RecycleViewBookAdapater.ViewHolder> {

        private ArrayList<Book> bookItemArrayList;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textViewTitle;
            private final ImageView imageViewCover;

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("具体操作");
                menu.add(0, MENU_ID_ADD, this.getAdapterPosition(), "添加");
                menu.add(0, MENU_ID_DELETE, this.getAdapterPosition(), "删除");
                menu.add(0, MENU_ID_UPDATE, this.getAdapterPosition(), "修改");

            }

            public ViewHolder(View itemView) {
                super(itemView);
                // Define click listener for the ViewHolder's View
                textViewTitle = (TextView) itemView.findViewById(R.id.text_view_book_title);
                imageViewCover = (ImageView) itemView.findViewById(R.id.image_view_book_cover);

                itemView.setOnCreateContextMenuListener(this);     // 创建一个上下文场景菜单监听器

            }

            public TextView getTextViewTitle() {
                return textViewTitle;
            }

            public ImageView getImageViewCover() {
                return imageViewCover;
            }
        }

        public RecycleViewBookAdapater(ArrayList<Book> bookItems) {
            bookItemArrayList = bookItems;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.book_item_row, viewGroup, false);

            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.getTextViewTitle().setText(bookItemArrayList.get(position).getTitle());
            viewHolder.getImageViewCover().setImageResource(bookItemArrayList.get(position).getCoverResourceId());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return bookItemArrayList.size();
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {   // 响应RecycleView中每一项的菜单
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        Toast.makeText(this, "clicked" + item.getOrder(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case MENU_ID_ADD:
                Intent addIntent = new Intent(this, BookDetailsActivity.class);
                addItemLauncher.launch(addIntent);
                break;
            case MENU_ID_DELETE:

                break;
            case MENU_ID_UPDATE:
                Intent updateIntent = new Intent(this, BookDetailsActivity.class);
                // item.getOrder() 获取该项的序号
                updateIntent.putExtra("position", item.getOrder());
                updateIntent.putExtra("name", bookItems.get(item.getOrder()).getTitle());
                updateItemLauncher.launch(updateIntent);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }
}
