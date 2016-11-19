package org.houxg.leanotelite.service;


import android.util.Log;

import org.houxg.leanotelite.model.Account;
import org.houxg.leanotelite.model.Notebook;
import org.houxg.leanotelite.network.ApiProvider;
import org.houxg.leanotelite.utils.RetrofitUtils;

public class NotebookService {

    private static final String TAG = "NotebookService";

    public static void addNotebook(String title, String parentNotebookId) {
        Notebook notebook = RetrofitUtils.excute(ApiProvider.getInstance().getNotebookApi().addNotebook(title, parentNotebookId));
        if (notebook == null) {
            throw new IllegalStateException("Network error");
        }
        if (notebook.isOk()) {
            Account account = AccountService.getCurrent();
            if (notebook.getUsn() - account.getLastSyncUsn() == 1) {
                Log.d(TAG, "update usn=" + notebook.getUsn());
                account.setLastUsn(notebook.getUsn());
                account.save();
            }
            notebook.insert();
        } else {
            throw new IllegalStateException(notebook.getMsg());
        }
    }
}
