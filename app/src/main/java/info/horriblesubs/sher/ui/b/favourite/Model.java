package info.horriblesubs.sher.ui.b.favourite;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import info.horriblesubs.sher.api.horrible.model.ShowDetail;
import info.horriblesubs.sher.db.DataMethods;

@SuppressLint("StaticFieldLeak")
public class Model extends ViewModel {

    private MutableLiveData<List<ShowDetail>> detail;
    private Context context;

    public Model() {
    }

    MutableLiveData<List<ShowDetail>> getResult(Context context) {
        if (this.detail == null) this.detail = new MutableLiveData<>();
        this.context = context;
        return detail;
    }

    void onRefresh() {
        detail.setValue(new DataMethods(context).onGetFavourites());
    }
}