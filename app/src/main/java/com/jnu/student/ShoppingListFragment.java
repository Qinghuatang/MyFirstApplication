package com.jnu.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.jnu.student.data.DataBank;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment {

    public static final int MENU_ID_ADD = 1;
    public static final int MENU_ID_DELETE = 2;
    public static final int MENU_ID_UPDATE = 3;

    private ActivityResultLauncher<Intent> addItemLauncher;
    private ActivityResultLauncher<Intent> updateItemLauncher;
    private ArrayList<Book> bookItems;
    private RecycleViewBookAdapater adapter;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        RecyclerView recycle_view_books = rootView.findViewById(R.id.recycle_view_books);
        recycle_view_books.setLayoutManager(new LinearLayoutManager(requireActivity())); // 设置布局管理器


        DataBank dataBank = new DataBank();
        bookItems = dataBank.LoadBookItems(requireActivity());

        if (0 == bookItems.size()) {
            bookItems.add(new Book("软件项目管理案例教程（第四版）", R.drawable.book_2));
            bookItems.add(new Book("创新工程实践", R.drawable.book_no_name));
            bookItems.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
        }

        adapter = new RecycleViewBookAdapater(bookItems);
        recycle_view_books.setAdapter(adapter);

        registerForContextMenu(recycle_view_books);     // 创建场景菜单事件


        addItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (null != result) {
                        Intent data = result.getData();
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bundle bundle = data.getExtras();
                            // 处理返回的数据
                            String name = bundle.getString("title");    // 获取返回的数据
                            bookItems.add(new Book(name, R.drawable.book_no_name));
                            adapter.notifyItemInserted(bookItems.size());

                            DataBank dataBank1 = new DataBank();
                            dataBank1.SaveBookItems(requireActivity(), bookItems);
                        }
                    }
                });

        updateItemLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (null != result) {
                        Intent data = result.getData();
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Bundle bundle = data.getExtras();

                            String title = bundle.getString("title");    // 获取返回的数据
                            int position = bundle.getInt("position");
                            bookItems.get(position).setTitle(title);

                            adapter.notifyItemChanged(position);
                        }
                    }
                });

        return rootView;
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
//        Toast.makeText(requireActivity(), "clicked" + item.getOrder(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case MENU_ID_ADD:
                Intent addIntent = new Intent(requireActivity(), BookDetailsActivity.class);
                addItemLauncher.launch(addIntent);
                break;
            case MENU_ID_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(R.string.confirmation);
                builder.setMessage(R.string.sure_to_delete);
                builder.setPositiveButton(R.string.yes,
                        (dialog, which) -> {
                            bookItems.remove(item.getOrder());
                            adapter.notifyItemRemoved(item.getOrder());

                            new DataBank().SaveBookItems(requireActivity(), bookItems);
                        });

                builder.setNegativeButton(R.string.no,
                        ((dialog, which) -> {

                        }));
                AlertDialog dialog = builder.create();
                dialog.show();

                break;
            case MENU_ID_UPDATE:
                Intent updateIntent = new Intent(requireActivity(), BookDetailsActivity.class);
                // item.getOrder() 获取该项的序号
                updateIntent.putExtra("position", item.getOrder());
                updateIntent.putExtra("name", bookItems.get(item.getOrder()).getTitle());
                updateItemLauncher.launch(updateIntent);
                break;
        }
        return super.onContextItemSelected(item);
    }
}


