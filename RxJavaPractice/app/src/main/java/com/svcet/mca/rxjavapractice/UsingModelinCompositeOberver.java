package com.svcet.mca.rxjavapractice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.svcet.mca.rxjavapractice.model.NoteModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class UsingModelinCompositeOberver extends AppCompatActivity {

    
    private CompositeDisposable compositeDisposable=new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_using_modelin_composite_oberver);

        compositeDisposable.add(getObservableNodeModelData()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(new Function<NoteModel, NoteModel>() {


                                @Override
                                public NoteModel apply(NoteModel noteModel) throws Exception {
                                     noteModel.setName(noteModel.getName().toUpperCase());
                                     return noteModel;
                                }
                            }).subscribeWith(getObserverData())

        );
        
    }

    private DisposableObserver<NoteModel> getObserverData(){
        return new DisposableObserver<NoteModel>() {
            @Override
            public void onNext(NoteModel noteModel) {
                Log.i("Note id and name :",noteModel.getId() +" : "+noteModel.getName());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.i("Complete ","success ");
            }
        };
    }



    private Observable<NoteModel> getObservableNodeModelData() {

        final List<NoteModel> listnote=getAllNoteList();

        return Observable.create(new ObservableOnSubscribe<NoteModel>() {
            @Override
            public void subscribe(ObservableEmitter<NoteModel> emitter) throws Exception {
                if(!emitter.isDisposed()){
                    for(NoteModel note:listnote){
                        emitter.onNext(note);
                    }
                }
                if(!emitter.isDisposed()){
                    emitter.onComplete();
                }
            }
        });
    }

    private List<NoteModel> getAllNoteList() {

        List<NoteModel> list=new ArrayList<>();
        list.add(new NoteModel(1,"go to shop"));
        list.add(new NoteModel(2,"go for lunch"));
        list.add(new NoteModel(3,"have a tea"));
        list.add(new NoteModel(4,"have a coffee"));
        list.add(new NoteModel(5,"have dinner"));
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        Log.i("destroy :","Destroy");
    }
}
