package com.example.administrator.avdemo.utils;


import com.example.administrator.avdemo.model.LiveInfoJson;
import com.example.administrator.avdemo.viewinterface.MvpView;

/**
 * 进出房间回调接口
 */
public interface EnterQuiteRoomView extends MvpView {


    void enterRoomComplete(int id_status, boolean succ);

    void quiteRoomComplete(int id_status, boolean succ, LiveInfoJson liveinfo);

    void memberQuiteLive(String[] list);

    void memberJoinLive(String[] list);

    void alreadyInLive(String[] list);


}
