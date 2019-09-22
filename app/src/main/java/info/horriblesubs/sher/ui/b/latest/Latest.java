package info.horriblesubs.sher.ui.b.latest;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import info.horriblesubs.sher.R;
import info.horriblesubs.sher.adapter.ListItemAdapter;
import info.horriblesubs.sher.api.horrible.model.ListItem;
import info.horriblesubs.sher.api.horrible.response.Result;
import info.horriblesubs.sher.common.FragmentRefresh;
import info.horriblesubs.sher.common.TaskListener;
import info.horriblesubs.sher.db.DataMethods;
import info.horriblesubs.sher.ui.b.Explore;
import info.horriblesubs.sher.ui.i.Show;
import info.horriblesubs.sher.ui.z.LoadingDialog;

public class Latest extends Fragment implements Observer<Result<ListItem>>, FragmentRefresh,
        TaskListener, ListItemAdapter.OnItemClick {

    private AppCompatTextView textView1, textView2;
    private RecyclerView recyclerView;
    private LoadingDialog dialog;
    private Model model;

    public Latest() {}

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.b_x_fragment_3, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(Model.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        model.getResult(this).observe(this, this);
        if (model.result.getValue() == null) model.onRefresh(false);
        textView2.setText(R.string.empty_latest_releases);
        textView1.setText(R.string.latest_releases);
    }

    @Override
    public void onChanged(Result<ListItem> result) {
        if (result == null || result.items == null || result.items.size() == 0) {
            Toast.makeText(getContext(), "No Data Received", Toast.LENGTH_SHORT).show();
            recyclerView.setAdapter(null);
            return;
        }
        new DataMethods(getContext()).onResetNotifications(result.items);
        recyclerView.setAdapter(ListItemAdapter.get(this, result.items, 6));
        String s = "Last refreshed on " + result.getTime();
        textView2.setText(s);
    }

    @Override
    public void onRefresh() {
        model.onRefresh(true);
    }

    @Override
    public void onDestroy() {
        if (dialog != null) dialog.dismiss();
        model.onStopTask();
        super.onDestroy();
        dialog = null;
    }

    @Override
    public void onPostExecute() {
        if (getActivity() instanceof Explore) ((Explore) getActivity()).onEndRefresh();
        if (dialog != null) dialog.dismiss();
        dialog = null;
    }

    @Override
    public void onPreExecute() {
        assert getContext() != null;
        if (dialog == null) dialog = new LoadingDialog(getContext());
        dialog.show();
    }

    @Override
    public void onItemClicked(@NotNull ListItem item) {
        if (item.link == null) return;
        Intent intent = new Intent(getActivity(), Show.class);
        intent.putExtra("show.link", item.link);
        startActivity(intent);
    }
}