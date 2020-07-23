package com.example.sigeve_android.ui.main;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<String> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<String, String>() {
        @Override
        public String apply(String input) {
            return input;
        }
    });

    public void setIndex(String index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }
}