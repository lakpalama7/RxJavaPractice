package com.svcet.mca.rxjavapractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class FlowableActivity extends AppCompatActivity {


    private Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowable);

        Flowable<Integer> flowable=getFlowableData();
        SingleObserver<Integer> observer=getSingleObserverData();

        flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .reduce(0, new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        return integer+integer2;
                    }
                })
                .subscribe(observer);
    }

    private SingleObserver<Integer> getSingleObserverData() {
        return new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable=d;
            }

            @Override
            public void onSuccess(Integer integer) {
                Log.i("Total is :",""+integer);

            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

    private Flowable<Integer> getFlowableData() {
        return Flowable.range(1,10);
    }
}
