package com.jnu.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jnu.student.data.Book;

import java.util.ArrayList;

public class Experiment6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment6);

        RecyclerView recycle_view_books = findViewById(R.id.recycle_view_books);
        recycle_view_books.setLayoutManager(new LinearLayoutManager(this));     // 设置布局管理器

        ArrayList<Book> bookItems = new ArrayList<>();
        bookItems.add(new Book("软件项目管理案例教程（第四版）", R.drawable.book_2));
        bookItems.add(new Book("创新工程实践", R.drawable.book_no_name));
        bookItems.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));

        RecycleViewBookAdapater adapter = new RecycleViewBookAdapater(bookItems);
        recycle_view_books.setAdapter(adapter);
    }

    public class RecycleViewBookAdapater extends RecyclerView.Adapter<RecycleViewBookAdapater.ViewHolder> {

        private ArrayList<Book> bookItemArrayList;
        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textViewTitle;
            private final ImageView imageViewCover;

            public ViewHolder(View view) {
                super(view);
                // Define click listener for the ViewHolder's View
                textViewTitle = (TextView) view.findViewById(R.id.text_view_book_title);
                imageViewCover = (ImageView) view.findViewById(R.id.image_view_book_cover);
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

}