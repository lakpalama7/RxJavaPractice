package com.svcet.mca.rxjavapractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.svcet.mca.rxjavapractice.model.NoteModel;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OperatorsRxJAVAActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operators_rx_java);

        NoteModel note=new NoteModel(1,"android programming");
        Completable observable=getCompletable(note);

        CompletableObserver observer=new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.i("Complete ","update success");
            }

            @Override
            public void onError(Throwable e) {

            }
        };

        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
/*
        Single<NoteModel> observable=getNoteObservable();

        SingleObserver<NoteModel> observer=new SingleObserver<NoteModel>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(NoteModel noteModel) {
                    Log.i("Name :",noteModel.getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.i("Error : ",e.getMessage());

            }
        };
        observable.subscribeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io()).subscribe(observer);*/

        /*Integer [] num={1,2,3,4,5,6,7,8,9,0,11,12,14,15,17};

        Observable.range(1,3)
                .repeat(3)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i("onSubscribe ","subscribe");

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i("onnext  ",""+integer);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("Complete ","complete");
                    }
                });*/
    }

    private Completable getCompletable(NoteModel note) {
        return Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                if(!emitter.isDisposed()){
                    Log.i("sleeping ","thread");
                    Thread.sleep(3000);
                    emitter.onComplete();
            }
        }
    });


}
/*
    private Single<NoteModel> getNoteObservable() {
        return Single.create(new SingleOnSubscribe<NoteModel>() {
            @Override
            public void subscribe(SingleEmitter<NoteModel> emitter) throws Exception {
                if(!emitter.isDisposed()) {
                    NoteModel note = new NoteModel(1, "hello world");
                    emitter.onSuccess(note);
                }
                else{
                    emitter.onError(new Throwable("some thing wrong"));
                }
            }
        });
    }*/
}
